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
    ResultSet set = statement.executeQuery();

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
