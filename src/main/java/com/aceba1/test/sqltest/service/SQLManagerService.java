package com.aceba1.test.sqltest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
