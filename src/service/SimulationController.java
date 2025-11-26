package service;

import entities.*;

/**
 * Simulation controller that orchestrates the emergency dispatch workflow.
 * Generates emergencies and runs dispatch cycles.
 */
public class SimulationController {
  private final EmergencyBSTService bstService;
  private final SummaryReportManager reportManager;
  private final DispatchEngineImpl dispatchEngine;
  private final GraphVisualizer visualizer;

  public SimulationController(EmergencyBSTService bstService,
      UnitMapServiceImpl unitService,
      SummaryReportManager reportManager,
      DispatchEngineImpl dispatchEngine,
      GraphVisualizer visualizer) {
    this.bstService = bstService;
    this.reportManager = reportManager;
    this.dispatchEngine = dispatchEngine;
    this.visualizer = visualizer;
  }

  public void generateEmergencies() {
    System.out.println("\n=== Generating Emergency Reports ===\n");

    long baseTime = System.currentTimeMillis();

    // Create diverse emergencies with different severities
    Emergency e1 = new Emergency(7, "fire", "Z03", "10:15", baseTime);
    Emergency e2 = new Emergency(9, "medical", "Z12", "10:20", baseTime + 5000);
    Emergency e3 = new Emergency(5, "police", "Z01", "10:22", baseTime + 7000);
    Emergency e4 = new Emergency(8, "fire", "Z15", "10:25", baseTime + 10000);

    bstService.insert(e1);
    reportManager.recordRegistration(e1);
    System.out.println("[REGISTERED] " + e1.summary());

    bstService.insert(e2);
    reportManager.recordRegistration(e2);
    System.out.println("[REGISTERED] " + e2.summary());

    bstService.insert(e3);
    reportManager.recordRegistration(e3);
    System.out.println("[REGISTERED] " + e3.summary());

    bstService.insert(e4);
    reportManager.recordRegistration(e4);
    System.out.println("[REGISTERED] " + e4.summary());

    System.out.println("\n[BST] Current emergency queue (in-order traversal):");
    bstService.printInOrder();
    System.out.println();
  }

  public void runOnce() {
    System.out.println("\n=== Running Single Dispatch Cycle ===\n");

    // Render the city map
    visualizer.renderMap();
    System.out.println();

    DispatchResult result = dispatchEngine.dispatch();

    if (result.success) {
      dispatchEngine.completeDispatch(result);
    }

    dispatchEngine.updateSummary();
  }

  /**
   * Run multiple dispatch cycles in batch
   */
  public void runBatch(int count) {
    System.out.println("\n=== Running Batch Dispatch (" + count + " cycles) ===\n");

    for (int i = 0; i < count; i++) {
      System.out.println("--- Cycle " + (i + 1) + " ---");

      DispatchResult result = dispatchEngine.dispatch();

      if (result.success) {
        dispatchEngine.completeDispatch(result);
      }

      // Small delay for readability
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }

    System.out.println("\n=== Final Summary After Batch ===");
    dispatchEngine.updateSummary();
  }

  /**
   * Run full simulation cycle
   */
  public void runFullSimulation() {
    // Step 1-3: Already done in Main (initialize system, graph, units)

    // Step 4: Generate emergencies
    generateEmergencies();

    // Steps 5-10: Dispatch all
    runOnce(); // First dispatch with full visualization

    Emergency next = dispatchEngine.findMostUrgent();
    int remaining = 0;

    // Count remaining
    Emergency temp = next;
    while (temp != null) {
      remaining++;
      // We can't peek further without modifying BST structure
      break;
    }

    if (remaining > 0) {
      runBatch(3); // Dispatch remaining emergencies
    }
  }
}
