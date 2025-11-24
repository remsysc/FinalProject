package service;

import entities.Unit;
import entities.UnitMap;
import entities.graph.PathResult;

public class UnitMapServiceImpl {
    private  final UnitMap map;

    public UnitMapServiceImpl(UnitMap unitMap){
        this.map = unitMap;
    }

    private int hash(String s) {
        long h = 1469598103934665603L;
        for (int i = 0; i < s.length(); i++) {
            h ^= s.charAt(i);
            h *= 1099511628211L;
        }
        return Long.hashCode(h);
    }

    private int findSlot( String key) {
        int cap = map.keys.length;
        int idx = (hash(key) & 0x7fffffff) % cap;
        for (int i = 0; i < cap; i++) {
            int p = (idx + i) % cap;
            if (map.keys[p] == null || map.keys[p].equals(key))
                return p;
        }
        return -1;
    }

    private void resizeIfNeeded() {
        if (map.size * 2 >= map.keys.length) {
            String[] oldK = map.keys;
            Unit[] oldV = map.values;
            map.keys = new String[oldK.length * 2 + 1];
            map.values = new Unit[oldV.length * 2 + 1];
            map.size = 0;
            for (int i = 0; i < oldK.length; i++) {
                if (oldK[i] != null)
                    put( oldK[i], oldV[i]);
            }
        }
    }

    public void put( String key, Unit value) {
        resizeIfNeeded();
        int slot = findSlot( key);
        if (slot == -1) return;
        if (map.keys[slot] == null) map.size++;
        map.keys[slot] = key;
        map.values[slot] = value;
    }

    public Unit get(String key) {
        int cap = map.keys.length;
        int idx = (hash(key) & 0x7fffffff) % cap;
        for (int i = 0; i < cap; i++) {
            int p = (idx + i) % cap;
            String k = map.keys[p];
            if (k == null) return null;
            if (k.equals(key)) return map.values[p];
        }
        return null;
    }

    public void updateStatus(String id, String newStatus) {
        Unit u = get(id);
        if (u != null) u.status = newStatus;
    }

    public Unit[] allUnits() {
        Unit[] arr = new Unit[map.size];
        int c = 0;
        for (int i = 0; i < map.keys.length; i++) {
            if (map.keys[i] != null)
                arr[c++] = map.values[i];
        }
        return arr;
    }

    // Find nearest available unit by graph distance
    public Unit findNearestAvailable(String type, String emergencyLoc,
                                     GraphServiceImpl graphService) {
        Unit[] arr = allUnits();
        Unit best = null;
        int bestCost = Integer.MAX_VALUE;

        for (Unit u : arr) {
            if (u == null) continue;
            if (!"AVAILABLE".equals(u.status)) continue;
            if (type != null && !type.equals(u.type)) continue;

            PathResult path = graphService.shortestPath(u.location, emergencyLoc);
            if (path == null) continue;

            if (path.totalCost < bestCost) {
                bestCost = path.totalCost;
                best = u;
            }
        }
        return best;
    }
}
