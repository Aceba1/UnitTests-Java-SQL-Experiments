package com.aceba1.test.sqltest.utils;

import org.springframework.util.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLProcessor {

  public static void collect(Connection connection) throws SQLException {

    StopWatch sw = new StopWatch("SQL Primitive Collector");
    sw.start("Query ResultSet");

    PreparedStatement statement = connection.prepareStatement("SELECT amount FROM transaction");
    statement.setFetchSize(10000);
    ResultSet set = statement.executeQuery();
    statement.execute();
    sw.stop();
    sw.start("Process ResultSet");

    long totalAmount = 0;
    while (set.next())
      totalAmount += set.getInt(1);
    System.out.println("Collected Sum: " + totalAmount);

    sw.stop();
    System.out.println(sw.prettyPrint());
  }
}
