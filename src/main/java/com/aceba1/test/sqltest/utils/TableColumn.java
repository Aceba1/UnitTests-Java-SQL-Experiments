package com.aceba1.test.sqltest.utils;

public class TableColumn {
  public enum Type {
    Text,
    Integer,
    BigInt,
    Double,

  }
  public static Type MapType(String typeName) {
    return switch(typeName.charAt(0)) {
      case 't' -> Type.Text;
      case 'i' -> Type.Integer;
      case 'b' -> Type.BigInt;
      case 'd', 'n' -> Type.Double;
      default -> throw new IllegalArgumentException("Unexpected typeName: " + typeName);
    };
  }
}
