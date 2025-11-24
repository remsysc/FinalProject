
/**
 * Simple custom map: open addressing hash map with linear probing.
 * Keys: String (id), Values: Unit
 * No java.util.
 */
class UnitMap {
  String[] keys;
  Unit[] values;
  int size;

  UnitMap(int capacity) {
    keys = new String[capacity];
    values = new Unit[capacity];
    size = 0;
  }

  private int hash(String s) {
    // Simple FNV-like
    long h = 1469598103934665603L;
    for (int i = 0; i < s.length(); i++) {
      h ^= s.charAt(i);
      h *= 1099511628211L;
    }
    // fold to int
    return (int) (h ^ (h >>> 32));
  }

  private int findSlot(String key) {
    int cap = keys.length;
    int idx = (hash(key) & 0x7fffffff) % cap;
    for (int i = 0; i < cap; i++) {
      int p = (idx + i) % cap;
      if (keys[p] == null || keys[p].equals(key))
        return p;
    }
    return -1;
  }

  private void resizeIfNeeded() {
    if (size * 2 >= keys.length) {
      String[] oldK = keys;
      Unit[] oldV = values;
      keys = new String[oldK.length * 2 + 1];
      values = new Unit[oldV.length * 2 + 1];
      size = 0;
      for (int i = 0; i < oldK.length; i++) {
        if (oldK[i] != null)
          put(oldK[i], oldV[i]);
      }
    }
  }

  void put(String key, Unit value) {
    resizeIfNeeded();
    int slot = findSlot(key);
    if (slot == -1)
      return; // should not happen with resize
    if (keys[slot] == null)
      size++;
    keys[slot] = key;
    values[slot] = value;
  }

  Unit get(String key) {
    int cap = keys.length;
    int idx = (hash(key) & 0x7fffffff) % cap;
    for (int i = 0; i < cap; i++) {
      int p = (idx + i) % cap;
      String k = keys[p];
      if (k == null)
        return null;
      if (k.equals(key))
        return values[p];
    }
    return null;
  }

  void updateStatus(String id, String newStatus) {
    Unit u = get(id);
    if (u != null)
      u.status = newStatus;
  }

  Unit[] allUnits() {
    Unit[] arr = new Unit[size];
    int c = 0;
    for (int i = 0; i < keys.length; i++) {
      if (keys[i] != null)
        arr[c++] = values[i];
    }
    return arr;
  }

  // Find nearest available unit by graph distance (linear scan).
  Unit findNearestAvailable(String type, String emergencyLoc, CityGraph graph) {
    Unit[] arr = allUnits();
    Unit best = null;
    int bestCost = Integer.MAX_VALUE;
    for (int i = 0; i < arr.length; i++) {
      Unit u = arr[i];
      if (u == null)
        continue;
      if (!"AVAILABLE".equals(u.status))
        continue;
      if (type != null && !type.equals(u.type))
        continue;
      PathResult path = graph.shortestPath(u.location, emergencyLoc);
      if (path == null)
        continue;
      if (path.totalCost < bestCost) {
        bestCost = path.totalCost;
        best = u;
      }
    }
    return best;
  }
}
