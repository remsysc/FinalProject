package entities;

import entities.graph.PathResult;

public class DispatchResult {
    public Emergency emergency;
    public Unit unit;
    public PathResult path;
    public long dispatchMs;
    public long arrivalMs;
    public boolean success;

    public String describe() {
        if (!success) {
            return "Dispatch failed for " + (emergency != null ? emergency.summary() : "unknown emergency");
        }
        return "Dispatched " + unit.id + " to " + emergency.location + " | " + path.describe();
    }
}
