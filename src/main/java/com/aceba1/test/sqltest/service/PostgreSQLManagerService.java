package com.aceba1.test.sqltest.service;

import ch.qos.logback.core.db.dialect.PostgreSQLDialect;
import com.aceba1.test.sqltest.utils.SQLQuery;
import com.aceba1.test.sqltest.utils.SQLUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

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
public class PostgreSQLManagerService {

  @Value("${spring.datasource.url}")
  private String URL;
  @Value("${spring.datasource.username}")
  private String USERNAME;
  @Value("${spring.datasource.password}")
  private String PASSWORD;

  { instance = this; }
  private static PostgreSQLManagerService instance;
  public static PostgreSQLManagerService getInstance() { return instance; }

  public Connection getConnection() {
    try {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println("----  SQLManagerService  ----  Failed to acquire PostgreSQL driver!");
    }
  }
}
