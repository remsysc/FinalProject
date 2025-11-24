
public class Main {
  public static void main(String[] args) {
    // Build systems
    EmergencyBST bst = new EmergencyBST();
    UnitMap units = new UnitMap(17);
    CityGraph graph = new CityGraph(8);
    SummaryReportManager reports = new SummaryReportManager();

    // Build city graph
    graph.addNode("HQ");
    graph.addNode("Z01");
    graph.addNode("Z03");
    graph.addNode("Z05");
    graph.addNode("Z08");
    graph.addNode("Z12");
    graph.addNode("Z15");

    graph.addEdge("HQ", "Z01", 3, true);
    graph.addEdge("Z01", "Z03", 5, true);
    graph.addEdge("Z03", "Z05", 4, true);
    graph.addEdge("Z05", "Z08", 6, true);
    graph.addEdge("Z08", "Z12", 7, true);
    graph.addEdge("Z12", "Z15", 2, true);
    graph.addEdge("HQ", "Z05", 10, true);
    graph.addEdge("Z03", "Z12", 9, true);

    // Seed units
    units.put("F-01", new Unit("F-01", "fire", "HQ", "AVAILABLE"));
    units.put("F-02", new Unit("F-02", "fire", "Z01", "AVAILABLE"));
    units.put("M-01", new Unit("M-01", "medical", "Z05", "AVAILABLE"));
    units.put("P-01", new Unit("P-01", "police", "Z03", "AVAILABLE"));
    units.put("M-02", new Unit("M-02", "medical", "Z12", "AVAILABLE"));

    // Run simulation
    SimulationController sim = new SimulationController(bst, units, graph, reports);
    sim.generateEmergencies();
    sim.runOnce(); // render map, dispatch one, show summary
    sim.runBatch(3); // complete rest
  }
}
