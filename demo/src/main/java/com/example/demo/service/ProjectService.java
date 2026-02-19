package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.model.Project;
import com.example.demo.repository.ProjectRepository;

@Service
public class ProjectService {
  private final ProjectRepository projectRepository;

  public ProjectService(ProjectRepository repository) {
    this.projectRepository = repository;
  }

  @Transactional
  public Project createProject(Project project) {
    return this.projectRepository.save(project);
  }
}
