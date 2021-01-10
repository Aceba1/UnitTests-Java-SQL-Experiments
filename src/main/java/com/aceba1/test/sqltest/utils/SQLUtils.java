package com.aceba1.test.sqltest.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLUtils {

  // Replace all not-commas with question marks
  public static String genSqlInsertParamRow(String columns) {

    return columns.replaceAll("[^,]+", "?");
  }

  public static String genSqlInsert(String table, String columns, int rows) {

    return genSqlInsert(table, columns, genSqlInsertParamRow(columns), rows);
  }

  public static String genSqlInsert(String table, String columns, String paramRow, int rows) {

    String base = genSqlInsert(table, columns, paramRow);
    if (rows <= 1) return base;
    return base + (",(" + paramRow + ")").repeat(rows - 1);
  }

  public static String genSqlInsert(String table, String columns) {

    return genSqlInsert(table, columns, genSqlInsertParamRow(columns));
  }

  public static String genSqlInsert(String table, String columns, String paramRow) {

    if (!columns.matches("^[A-Za-z0-9,_\\- ]*$"))
      throw new IllegalArgumentException("Invalid column header line: " + columns);
    return "INSERT INTO " + table + "(" + columns + ")VALUES(" + paramRow + ")";
  }

  /**
   * @return [i] int, [b] long, [nd] double, [c] string
   */
  public static ColumnType[] mapTableTypes(String columns, ResultSet resultSet) throws SQLException {

    String[] names = columns.replaceAll(" ", "").toLowerCase().split(",");
    int size = names.length;
    ColumnType[] result = new ColumnType[size];

    //System.out.println("COLUMN TYPES ( ");
    while (resultSet.next()) {
      String check = resultSet.getString(1).toLowerCase();

      for (int i = 0; i < size; i++) {
        if (check.equals(names[i])) {
          //System.out.println("  " + column + " : " + type);
          result[i] = MapType(resultSet.getString(2));
          break;
        }
      }
    }
    //System.out.println(")");

    return result;
  }

  public static void appendStatement(
    PreparedStatement statement,
    int position,
    Object value
  ) throws SQLException {

    if (value == null)
      statement.setObject(position, null);
    if (value instanceof String)
      statement.setString(position, (String) value);
    else if (value instanceof Double)
      statement.setDouble(position, (Double) value);
    else if (value instanceof Long)
      statement.setLong(position, (Long) value);
    else if (value instanceof Integer)
      statement.setInt(position, (Integer) value);
    else
      throw new IllegalArgumentException("Unknown type '" + value.getClass().toString() + "'");
  }

  public static void appendStatement(
    PreparedStatement statement,
    int position,
    ColumnType type,
    String value
  ) throws SQLException {

    switch (type) {
      case Text -> statement.setString(position, value);
      case Integer -> statement.setInt(position, Integer.parseInt(value));
      case BigInt -> statement.setLong(position, Long.parseLong(value));
      case Double -> statement.setDouble(position, Double.parseDouble(value));
      default -> throw new IllegalArgumentException("Unknown type '" + type + "'");
    }
  }
}
