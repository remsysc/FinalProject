
/**
 * In-memory live stats and console reporting.
 */
class SummaryReportManager {
  int totalEmergenciesRegistered;
  int totalDispatches;
  int totalCompletions;
  long sumResponseCost; // sum of path cost as response time proxy
  int unitsUtilized; // unique count approximated
  String[] utilizedIds; // track IDs to avoid double count
  int utilizedCount;

  SummaryReportManager() {
    utilizedIds = new String[16];
    utilizedCount = 0;
  }

  void recordRegistration(Emergency e) {
    totalEmergenciesRegistered++;
  }

  void recordDispatch(Emergency e, Unit u, int pathCost) {
    totalDispatches++;
    sumResponseCost += pathCost;
    trackUtilized(u.id);
  }

  void recordCompletion(Emergency e, Unit u) {
    totalCompletions++;
  }

  private void trackUtilized(String id) {
    for (int i = 0; i < utilizedCount; i++) {
      if (utilizedIds[i].equals(id))
        return;
    }
    if (utilizedCount >= utilizedIds.length) {
      String[] na = new String[utilizedIds.length * 2 + 1];
      for (int i = 0; i < utilizedIds.length; i++)
        na[i] = utilizedIds[i];
      utilizedIds = na;
    }
    utilizedIds[utilizedCount++] = id;
    unitsUtilized = utilizedCount;
  }

  double avgResponseCost() {
    if (totalDispatches == 0)
      return 0.0;
    return ((double) sumResponseCost) / ((double) totalDispatches);
  }

  void printLiveSummary() {
    System.out.println("=== Live Summary ===");
    System.out.println("Emergencies Registered: " + totalEmergenciesRegistered);
    System.out.println("Dispatches: " + totalDispatches);
    System.out.println("Completions: " + totalCompletions);
    System.out.println("Units Utilized: " + unitsUtilized);
    System.out.println("Average Response Cost: " + avgResponseCost());
    System.out.println("====================");
  }
}
