package com.aceba1.test.sqltest.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestGet {

  public List<ColumnProcessor> columns;

  //TODO: Relocate elsewhere, supply proper logic
//  public Collection<String> getTables() {
//    // As nice as streams are, this is nicer
//    Set<String> set = new HashSet<>();
//    for (Column column : columns)
//      set.add(column.table);
//
//    return set;
//  }
}
