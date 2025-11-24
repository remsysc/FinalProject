
class DispatchResult {
  Emergency emergency;
  Unit unit;
  PathResult path;
  long dispatchMs;
  long arrivalMs;
  boolean success;

  String describe() {
    if (!success)
      return "Dispatch failed for " + emergency.summary();
    return "Dispatched " + unit.id + " to " + emergency.location + " | " + path.describe();
  }
}

class DispatchEngine {
  EmergencyBST emergencyBST;
  UnitMap unitMap;
  CityGraph graph;
  SummaryReportManager reports;

  DispatchEngine(EmergencyBST bst, UnitMap units, CityGraph graph, SummaryReportManager reports) {
    this.emergencyBST = bst;
    this.unitMap = units;
    this.graph = graph;
    this.reports = reports;
  }

  DispatchResult dispatchNext() {
    DispatchResult dr = new DispatchResult();
    Emergency e = emergencyBST.peekHighestPriority();
    if (e == null) {
      dr.success = false;
      return dr;
    }
    // Find nearest available unit of matching type
    Unit u = unitMap.findNearestAvailable(e.type, e.location, graph);
    if (u == null) {
      dr.success = false;
      dr.emergency = e;
      return dr;
    }

    PathResult path = graph.shortestPath(u.location, e.location);
    if (path == null) {
      dr.success = false;
      dr.emergency = e;
      return dr;
    }

    // Dispatch
    long now = System.currentTimeMillis();
    u.status = "DEPLOYED";
    u.lastDispatchMs = now;
    dr.emergency = e;
    dr.unit = u;
    dr.path = path;
    dr.dispatchMs = now;
    dr.arrivalMs = now + path.totalCost; // naive: cost as time
    dr.success = true;

    // Remove emergency from BST after dispatch
    emergencyBST.remove(e);

    // Update report stats
    reports.recordDispatch(e, u, path.totalCost);

    return dr;
  }

  // Mark unit free after arrival (simplified)
  void complete(DispatchResult dr) {
    if (dr != null && dr.success && dr.unit != null) {
      dr.unit.location = dr.emergency.location;
      dr.unit.status = "AVAILABLE";
      reports.recordCompletion(dr.emergency, dr.unit);
    }
  }
}
