package com.aceba1.test.sqltest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.Reader;
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
  public static final String SQL_INSERT_INTO_TABLE = "INSERT INTO ? (?) VALUES (?)";

  @Value("${spring.datasource.url}")
  private String URL;
  @Value("${spring.datasource.username}")
  private String USERNAME;
  @Value("${spring.datasource.password}")
  private String PASSWORD;

  public Connection getConnection() {
    try {
      return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public long uploadCSV(String table, Reader csv) {
    try {
      Connection conn = getConnection();
      var reader = new BufferedReader(csv);

      long count = 0;
      String line;

      PreparedStatement statement = conn.prepareStatement(SQL_INSERT_INTO_TABLE);
      statement.setString(1, table);
      statement.setString(2, reader.readLine());

      while (
        (line = reader.readLine()) != null &&
        line.length() != 0
      ) {
        statement.setString(3, line);
        statement.addBatch();

        if (++count % MAX_BATCH == 0)
          statement.executeBatch();
      }

      statement.executeBatch();
      statement.close();
      conn.close();

      return count;
    }
    catch (Exception e) {
      e.printStackTrace();
      return 0;
    }
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
