package com.aceba1.test.sqltest.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQuery {

  private static final String SQL_TABLE_DETAILS =
    "SELECT column_name,data_type FROM information_schema.columns WHERE table_name=?";

  private static final String SQL_TABLE_SIZE_BYTES = "SELECT pg_total_relation_size(?)";
  private static final String SQL_TABLE_SIZE_PRETTY = "SELECT pg_size_pretty(pg_total_relation_size(?))";

  public static long getTableSizeBytes(Connection connection, String table) throws SQLException {

    ResultSet set = preparedQuery(connection, SQL_TABLE_SIZE_BYTES, table);
    set.first();
    return set.getLong(1);
  }

  public static String getTableSizePretty(Connection connection, String table) throws SQLException {

    ResultSet set = preparedQuery(connection, SQL_TABLE_SIZE_PRETTY, table);
    set.first();
    return set.getString(1);
  }

  public static ResultSet getTableDetails(Connection connection, String table) throws SQLException {

    return preparedQuery(connection, SQL_TABLE_DETAILS, table);
  }

  public static ResultSet preparedQuery(Connection connection, String query, Object... params) throws SQLException {

    PreparedStatement statement = connection.prepareStatement(query);

    if (params != null && params.length != 0) {
      int index = 0;
      for (Object param : params)
        SQLUtils.appendStatement(statement, ++index, param);
    }

    ResultSet set = statement.executeQuery();
    statement.close();

    return set;
  }
}
