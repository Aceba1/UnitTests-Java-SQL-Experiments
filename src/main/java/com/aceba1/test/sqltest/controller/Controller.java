package com.aceba1.test.sqltest.controller;

import com.aceba1.test.sqltest.model.RequestGet;
import com.aceba1.test.sqltest.service.SQLManagerService;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.RequestContext;
import org.yaml.snakeyaml.reader.StreamReader;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

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

  //TODO: Segment sent data!
  @PostMapping("/csv")
  @CrossOrigin(origins = "http://localhost:3000") // Very important
  public Object postCSV(
    @RequestParam String table,
    HttpServletRequest request
  ) {
    try {
      return sqlMan.uploadCSV(table, new InputStreamReader(request.getInputStream()));
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(e.getMessage());
    }
  }

//  @PostMapping("/upload")
//  public ResponseEntity<Object> uploadCSV(
//    HttpServletRequest request,
//    @RequestParam Map<String, String> params
//  ) {
//    try {
//      System.out.println(new BufferedReader(new InputStreamReader(request.getInputStream())).readLine());
//      System.out.println(params.values());
//      return ResponseEntity.status(200).build();
//    } catch (IOException e) {
//      e.printStackTrace();
//      return ResponseEntity.status(400).build();
//    }
//    // return ResponseEntity.status(100).build(); // Retry
//  }

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

