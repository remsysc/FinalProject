
class Unit {
  String id; // unique
  String type; // fire, medical, police
  String location; // current graph node
  String status; // AVAILABLE, DEPLOYED, BUSY
  long lastDispatchMs;

  Unit(String id, String type, String location, String status) {
    this.id = id;
    this.type = type;
    this.location = location;
    this.status = status;
  }

  String summary() {
    return "[Unit id=" + id + ", type=" + type + ", loc=" + location + ", status=" + status + "]";
  }
}
