package com.example.demo.model;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // Tells this class is a database table
@Table(name = "projects") // sets the table name in the database
@Getter // Part of Lombok avoid verbosity by not having to write Getters
@Setter // Part of Lombok avoid verbosity by not having to write Setters
@NoArgsConstructor // Part of Lombok - no argument constuctor
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // Basically like a primary key auto generated

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = true, length = 500)
  private String description;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false, updatable = true)
  private LocalDateTime updatedAt;
}
