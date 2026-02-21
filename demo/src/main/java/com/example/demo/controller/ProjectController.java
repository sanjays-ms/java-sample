package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Project;
import com.example.demo.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

  private final ProjectService projectService;

  /**
   * Constructor injection - Spring auto-provides the service.
   */
  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  // @GetMapping
  // public ResponseEntity<String> getHealth() {
  // return ResponseEntity.status(HttpStatus.OK).body("Dummy controller for now");
  // }


  /**
   * Create a new project. POST /api/v1/projects
   *
   * @param project - project data from request body (JSON)
   * @return ResponseEntity with created project and 201 Created status
   */
  @PostMapping
  public ResponseEntity<Project> createProject(@RequestBody Project project) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(project));
  }
}
