package com.aceba1.test.sqltest.utils;

public class SQLColumn {

  public String name;
  public String rawValue;
  public ColumnType type;

  public SQLColumn(String columnName, String rawValue, String type) {
    this(columnName, rawValue, mapType(type));
  }

  public SQLColumn(String columnName, String rawValue, ColumnType type) {
    this.name = columnName;
    this.rawValue = rawValue;
    this.type = type;
  }

  public String getText() {
    return rawValue;
  }

  public int getInt() {
    return Integer.parseInt(rawValue);
  }

  public long getLong() {
    return Long.parseLong(rawValue);
  }

  public double getDouble() {
    return Double.parseDouble(rawValue);
  }

  public static ColumnType mapType(String typeName) {
    return switch(typeName.charAt(0)) {
      case 't' -> ColumnType.Text;
      case 'i' -> ColumnType.Integer;
      case 'b' -> ColumnType.BigInt;
      case 'd', 'n' -> ColumnType.Double;
      default -> throw new IllegalArgumentException("Unexpected typeName: " + typeName);
    };
  }
}
