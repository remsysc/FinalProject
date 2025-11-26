package service;

import entities.*;
import entities.graph.PathResult;

/**
 * Core dispatch engine integrating BST, Map, Graph, and Reporting.
 * Implements the full emergency response workflow.
 */
public class DispatchEngineImpl {
    private final EmergencyBSTService bstService;
    private final UnitMapServiceImpl unitService;
    private final SummaryReportManager reportManager;
    private final GraphServiceImpl graphService;

    public DispatchEngineImpl(EmergencyBSTService bstService,
                              UnitMapServiceImpl unitService,
                              SummaryReportManager reportManager,
                              GraphServiceImpl graphService) {
        this.bstService = bstService;
        this.unitService = unitService;
        this.reportManager = reportManager;
        this.graphService = graphService;
    }

    public Emergency findMostUrgent() {
        return bstService.peekHighestPriority();
    }


    public Unit findNearestResponder(Emergency emergency) {
        if (emergency == null) {
            return null;
        }

        return unitService.findNearestAvailable(
                emergency.type,
                emergency.location,
                graphService
        );
    }


    public PathResult shortestPath(String from, String to) {
        return graphService.shortestPath(from, to);
    }


    public DispatchResult dispatch() {
        DispatchResult result = new DispatchResult();
        result.success = false;
        result.dispatchMs = System.currentTimeMillis();

        Emergency emergency = findMostUrgent();
        if (emergency == null) {
            System.out.println("[DISPATCH] No emergencies pending.");
            return result;
        }

        result.emergency = emergency;
        System.out.println("[DISPATCH] Processing: " + emergency.summary());

        Unit unit = findNearestResponder(emergency);
        if (unit == null) {
            System.out.println("[DISPATCH] No available units for type: " + emergency.type);
            return result;
        }

        result.unit = unit;
        System.out.println("[DISPATCH] Assigned unit: " + unit.summary());

        PathResult path = shortestPath(unit.location, emergency.location);
        if (path == null) {
            System.out.println("[DISPATCH] No route available from " + unit.location +
                    " to " + emergency.location);
            return result;
        }

        result.path = path;

        // Update unit status and location
        unitService.updateStatus(unit.id, "DEPLOYED");
        unit.location = emergency.location;
        unit.lastDispatchMs = result.dispatchMs;

        // Calculate estimated arrival
        result.arrivalMs = result.dispatchMs + (path.totalCost * 60000L); // Assume 1 minute per cost unit

        // Record dispatch metrics
        reportManager.recordDispatch(emergency, unit, path.totalCost);

        result.success = true;

        // Step 8: Show ASCII path with travel details
        System.out.println("[DISPATCH] " + result.describe());
        System.out.println();

        return result;
    }


    public void delete(Emergency emergency) {
        if (emergency == null) {
            return;
        }

        bstService.remove(emergency);
        System.out.println("[RESOLVED] Emergency removed: " + emergency.summary());
    }

    /**
     * Complete a dispatch cycle: mark emergency resolved and free up unit
     */
    public void completeDispatch(DispatchResult dispatch) {
        if (dispatch == null || !dispatch.success) {
            return;
        }

        delete(dispatch.emergency);

        // Free up the unit
        unitService.updateStatus(dispatch.unit.id, "AVAILABLE");

        // Record completion
        reportManager.recordCompletion(dispatch.emergency, dispatch.unit);

        System.out.println("[COMPLETE] Unit " + dispatch.unit.id + " now available.");
        System.out.println();
    }


    public void updateSummary() {
        reportManager.printLiveSummary();
    }
}