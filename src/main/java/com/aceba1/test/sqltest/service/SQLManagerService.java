package com.aceba1.test.sqltest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.*;

/* NOTES:
  * Columns will be calculated within a SELECT and set to a new table
  * Can expose Rest API to test column without appending
  * Can page queries with LIMIT and OFFSET
  * ... LIMIT size OFFSET start;
  *
  * Should check JSON for illegal characters #-/\()';
  *
  *
 */

@Service
public class SQLManagerService {

  public static final int MAX_BATCH = 1000;
  private static final String SQL_QUERY_TABLE_DETAILS =
    "SELECT column_name,data_type FROM information_schema.columns WHERE table_name=?";

  @Value("${spring.datasource.url}")
  private String URL;
  @Value("${spring.datasource.username}")
  private String USERNAME;
  @Value("${spring.datasource.password}")
  private String PASSWORD;

  private Connection connection;

  public Connection getConnection() {
    try {
      if (connection == null || connection.isClosed()) {
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        System.out.println("|----  SQLManagerService  ----| Connected!");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }

  private static String genSqlInsert(String table, String columns) {
    if (!columns.matches("^[A-Za-z0-9,_\\- ]*$"))
      throw new IllegalArgumentException("Invalid column header line: " + columns);
    return "INSERT INTO " + table +
      "(" + columns +
      ")VALUES(" + columns.replaceAll( // Replace all not-commas with question marks
        "[^,]+",
        "?") +
      ")";
  }

  private ResultSet getTableDetails(String table) throws SQLException {
    Connection conn = getConnection();
    PreparedStatement statement = conn.prepareStatement(SQL_QUERY_TABLE_DETAILS);
    statement.setString(1, table);
    ResultSet set = statement.executeQuery();
    statement.close();

    return set;
  }


  /**
   * @return [i] int, [b] long, [nd] double, [c] string
   */
  private char[] mapTableTypes(String table, String columns) throws SQLException {
    String[] columnNames = columns.replaceAll(" ", "").toLowerCase().split(",");
    int columnCount = columnNames.length;

    ResultSet set = getTableDetails(table);

//    if (set.getFetchSize() < columnCount)
//      throw new IllegalArgumentException("Columns provided exceeds table quantity!");

    char[] result = new char[columnCount];

    System.out.println("COLUMN TYPES ( ");
    while (set.next()) {
      String column = set.getString(1).toLowerCase();

      for (int i = 0; i < columnCount; i++) {
        if (column.equals(columnNames[i])) {
          String type = set.getString(2);
          System.out.println("  " + column + " : " + type);
          result[i] = type.charAt(0);
          break;
        }
      }
    }
    System.out.println(")");

    return result;
  }

  public long uploadCSV(String table, Reader csv) throws SQLException, IOException {
      Connection conn = getConnection();
      var reader = new BufferedReader(csv);

      long count = 0;
      String row;

      String firstLine = reader.readLine();

      String sql = genSqlInsert(table, firstLine);
      System.out.println(sql);
      PreparedStatement statement = conn.prepareStatement(sql);

      char[] types = mapTableTypes(table, firstLine);
      int columnCount = types.length;

      while (
        (row = reader.readLine()) != null &&
          row.length() != 0
      ) {
        String[] values = row.split(",");
        for (int i = 0; i < columnCount; i++) {
          switch (types[i]) {
            case 'c' -> statement.setString(i + 1, values[i]);
            case 'i' -> statement.setInt(i + 1, Integer.parseInt(values[i]));
            case 'b' -> statement.setLong(i + 1, Long.parseLong(values[i]));
            case 'd','n' -> statement.setDouble(i + 1, Double.parseDouble(values[i]));
            default -> throw new UnsupportedEncodingException("Unknown type '" + types[i] + "'");
          }
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

  public void foo() {
    Connection conn = getConnection();
    try {
      conn.beginRequest();
      Statement m = conn.createStatement();
      ResultSet query = m.executeQuery("SELECT * FROM demotable");
      while (query.next()) {
        System.out.println(query.getString(1));
      }

      conn.endRequest();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
