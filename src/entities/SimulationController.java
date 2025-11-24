
/**
 * Generates synthetic emergencies and runs system end-to-end.
 */
class SimulationController {
  EmergencyBST bst;
  UnitMap units;
  CityGraph graph;
  SummaryReportManager reports;
  DispatchEngine engine;
  GraphVisualizer viz;

  SimulationController(EmergencyBST bst, UnitMap units, CityGraph graph, SummaryReportManager reports) {
    this.bst = bst;
    this.units = units;
    this.graph = graph;
    this.reports = reports;
    this.engine = new DispatchEngine(bst, units, graph, reports);
    this.viz = new GraphVisualizer(graph);
  }

  void generateEmergencies() {
    long now = System.currentTimeMillis();
    register(new Emergency(5, "fire", "Z12", "09:00", now));
    register(new Emergency(3, "medical", "Z05", "09:02", now + 1000));
    register(new Emergency(4, "police", "Z03", "09:03", now + 2000));
    register(new Emergency(2, "fire", "Z08", "09:04", now + 3000));
  }

  private void register(Emergency e) {
    bst.insert(e);
    reports.recordRegistration(e);
  }

  void runOnce() {
    viz.renderMap();
    bst.printInOrder();

    DispatchResult dr = engine.dispatchNext();
    System.out.println(dr.describe());
    viz.highlightRoute(dr.path);
    reports.printLiveSummary();

    // Complete and free unit
    engine.complete(dr);
    reports.printLiveSummary();
  }

  void runBatch(int iterations) {
    for (int i = 0; i < iterations; i++) {
      DispatchResult dr = engine.dispatchNext();
      System.out.println(dr.describe());
      engine.complete(dr);
    }
    reports.printLiveSummary();
  }
}
