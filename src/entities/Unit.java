package entities;

public class Unit {
  public String id; // unique
  public String type; // fire, medical, police
  public String location; // current graph node
  public String status; // AVAILABLE, DEPLOYED, BUSY
  public long lastDispatchMs;

  public Unit(String id, String type, String location, String status) {
    this.id = id;
    this.type = type;
    this.location = location;
    this.status = status;
  }

  public String summary() {
    return "[Unit id=" + id + ", type=" + type + ", loc=" + location + ", status=" + status + "]";
  }
}
