package com.aceba1.test.sqltest.controller;

import com.aceba1.test.sqltest.model.RequestGet;
import com.aceba1.test.sqltest.service.SQLManagerService;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.Statement;

@RestController
public class Controller {

  @Autowired
  SQLManagerService sqlMan;

  @GetMapping("/")
  public Object getStuff(
    @RequestBody RequestGet body
  ) {
    // Test

    String[] result = new String[body.columns.size()];

    for (int i = 0; i < body.columns.size(); i++)
      result[i] = body.columns.get(i).toString("USER_OID_SALT");

    return result;
  }

  @PostMapping("/")
  public Object postStuff(
    @RequestParam String table,
    @RequestBody String columns,
    @RequestBody String values
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

