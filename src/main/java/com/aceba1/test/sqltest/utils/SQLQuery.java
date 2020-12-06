package com.aceba1.test.sqltest.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQuery {

  private static final String SQL_QUERY_TABLE_DETAILS =
    "SELECT column_name,data_type FROM information_schema.columns WHERE table_name=?";

  public static ResultSet getTableDetails(Connection connection, String table) throws SQLException {
    PreparedStatement statement = connection.prepareStatement(SQL_QUERY_TABLE_DETAILS);
    statement.setString(1, table);

    ResultSet set = statement.executeQuery();
    statement.close();

    return set;
  }
}
