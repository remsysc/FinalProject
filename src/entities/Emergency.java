package entities;

public class Emergency {
  public int severity; // Higher means more urgent
  public String type; // fire, medical, police
  public String location; // Node name in graph
  String time; // Simple string timestamp
  public long createdAtMs; // For tie-breaks and metrics

  public Emergency(int severity, String type, String location, String time, long nowMs) {
    this.severity = severity;
    this.type = type;
    this.location = location;
    this.time = time;
    this.createdAtMs = nowMs;
  }

  public String summary() {
    return "[Emergency type=" + type + ", severity=" + severity + ", location=" + location + ", time=" + time + "]";
  }
}
