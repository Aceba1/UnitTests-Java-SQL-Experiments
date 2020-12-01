package com.aceba1.test.sqltest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

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