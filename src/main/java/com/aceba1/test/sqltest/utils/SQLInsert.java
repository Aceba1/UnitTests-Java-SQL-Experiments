package com.aceba1.test.sqltest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLInsert {
  public static final int MAX_BATCH = 1000;
  
  public static long uploadCSV(Connection connection, String table, Reader csv) throws SQLException, IOException {
    var reader = new BufferedReader(csv);

    String columns = reader.readLine();
    PreparedStatement statement = connection.prepareStatement(SQLUtils.genSqlInsert(table, columns));

    char[] types = SQLUtils.mapTableTypes(columns, SQLQuery.getTableDetails(connection, table));
    int columnCount = types.length;

    String row;
    long count = 0;
    while (
      (row = reader.readLine()) != null &&
        row.length() != 0
    ) {
      String[] values = row.split(",");
      for (int i = 0; i < columnCount; i++) {
        appendStatement(statement, i + 1, types[i], values[i]);
      }

      statement.addBatch();
      if (++count % MAX_BATCH == 0) {
        statement.executeBatch();
        System.out.println("Batch " + count + " executing");
      }
    }

    statement.executeBatch();
    statement.close();

    System.out.println("Final batch executing");

    return count;
  }

  private static void appendStatement(
    PreparedStatement statement,
    int position,
    char type,
    String value
  ) throws SQLException {

    switch (type) {
      case 'c' -> statement.setString(position, value);
      case 'i' -> statement.setInt(position, Integer.parseInt(value));
      case 'b' -> statement.setLong(position, Long.parseLong(value));
      case 'd','n' -> statement.setDouble(position, Double.parseDouble(value));
      default -> throw new IllegalArgumentException("Unknown type '" + type + "'");
    }
  }

}
