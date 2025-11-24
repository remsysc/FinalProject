
class Emergency {
  int severity; // Higher means more urgent
  String type; // fire, medical, police
  String location; // Node name in graph
  String time; // Simple string timestamp
  long createdAtMs; // For tie-breaks and metrics

  Emergency(int severity, String type, String location, String time, long nowMs) {
    this.severity = severity;
    this.type = type;
    this.location = location;
    this.time = time;
    this.createdAtMs = nowMs;
  }

  String summary() {
    return "[Emergency type=" + type + ", severity=" + severity + ", location=" + location + ", time=" + time + "]";
  }
}
