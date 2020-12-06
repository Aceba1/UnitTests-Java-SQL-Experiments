package com.aceba1.test.sqltest.utils;

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
  public static char[] mapTableTypes(String columns, ResultSet resultSet) throws SQLException {
    String[] names = columns.replaceAll(" ", "").toLowerCase().split(",");
    int size = names.length;
    char[] result = new char[size];

    //System.out.println("COLUMN TYPES ( ");
    while (resultSet.next()) {
      String check = resultSet.getString(1).toLowerCase();

      for (int i = 0; i < size; i++) {
        if (check.equals(names[i])) {
          //System.out.println("  " + column + " : " + type);
          result[i] = resultSet.getString(2).charAt(0);
          break;
        }
      }
    }
    //System.out.println(")");

    return result;
  }
}
