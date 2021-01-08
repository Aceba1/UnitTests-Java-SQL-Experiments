package com.aceba1.test.sqltest.utils;

import com.aceba1.test.sqltest.model.ColumnProcessor;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLCreate {
  // Make column types enum?
  // Make column class
  public static void createTable(
    Connection connection,
    String wrappedTableName,
    String processorQuery
  ) throws SQLException {
    var statement = connection.createStatement();
    statement.execute("CREATE TABLE " + wrappedTableName +
      " AS " + processorQuery);
    statement.close();
  }

  //TODO: Populate with defined Columns
  public static void createTable(
    Connection connection,
    String wrappedTableName
  ) throws SQLException {
    var statement = connection.createStatement();
    statement.execute("CREATE TABLE " + wrappedTableName);
    statement.close();
  }
}
