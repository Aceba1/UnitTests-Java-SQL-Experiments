package com.aceba1.test.sqltest.controller;

import com.aceba1.test.sqltest.model.RequestNewTable;
import com.aceba1.test.sqltest.service.PostgreSQLManagerService;
import com.aceba1.test.sqltest.utils.MapBuilder;
import com.aceba1.test.sqltest.utils.SQLInsert;
import com.aceba1.test.sqltest.utils.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@RestController
public class Controller {

  @Autowired
  PostgreSQLManagerService sqlMan;

  //TODO: Segment sent data!
  @PostMapping("/csv")
  //@CrossOrigin(origins = "http://localhost:3000") // important?
  public Object postCSV(
    @RequestParam String table,
    @RequestParam(defaultValue = "100") Integer batch,
    @RequestParam(defaultValue = "1000") Integer rows,
    HttpServletRequest request
  ) {
    try {
      return SQLInsert.uploadCSV(sqlMan.getConnection(), table,
        new InputStreamReader(request.getInputStream()),
        rows, batch);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(e.getMessage());
    }
  }

  @GetMapping("/table/size")
  public Object getTableSize(
    @RequestParam String table
  ) throws SQLException {
    Connection conn = sqlMan.getConnection();
    Map result = new MapBuilder(
      "table", table,
      "prettySize", SQLQuery.getTableSizePretty(conn, table),
      "byteSize", SQLQuery.getTableSizeBytes(conn, table)
    );
    conn.close();
    return result;
  }

  @PostMapping("/table")
  public Object createTable(
    @RequestBody RequestNewTable request
  ) {

//    Connection conn = sqlMan.getConnection();
//    conn.beginRequest();
//
//    Statement s = conn.createStatement();
//    s.execute("INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")");
//
//    conn.endRequest();
//    conn.close();
    return null;
  }
}

