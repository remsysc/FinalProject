package entities;

/**
 * Simple custom map: open addressing hash map with linear probing.
 * Keys: String (id), Values: Unit
 * No java.util.
 */
public class UnitMap {
  public String[] keys;
  public Unit[] values;
  public int size;

  public UnitMap(int capacity) {
    keys = new String[capacity];
    values = new Unit[capacity];
    size = 0;
  }


}
