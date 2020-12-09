package com.aceba1.test.sqltest.model;

import java.util.List;

public class ColumnProcessor {
  public static final String PUBLIC_TABLESALT = "__PUBLIC__";

  public static String verifyWrappedTable(String table, String userTableSalt) {
    return table.startsWith(PUBLIC_TABLESALT) ? table : wrapPrivateTable(table, userTableSalt);
  }

  public static String wrapPrivateTable(String tableName, String userTableSalt) {
    return "__" + userTableSalt + "__" + tableName;
  }

  public ProcessorFragment expr;

  public String asName;


  //TODO: Parameterize / automate table joining externally
  // One to One, One to Many
  //public List<String> tables;

  @Override
  @Deprecated
  public String toString() {
    return expr.toString() + " AS " + asName;
  }

  public String toString(String userTableSalt) {
    return expr.toString(userTableSalt) + " AS " + asName;
  }
}
