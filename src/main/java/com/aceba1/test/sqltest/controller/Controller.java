package com.aceba1.test.sqltest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

  @Autowired
  

  @GetMapping("/")
  public Object getStuff(
    @RequestBody RequestGet body
  ) {

  }

  @PostMapping("/")
  public Object postStuff() {

  }
}

class RequestGet {

}
