package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class HealthController {
  @GetMapping("/health")
  public ResponseEntity<String> getHealth(){
    return ResponseEntity.status(HttpStatus.OK).body("running health controller !");
  }
}