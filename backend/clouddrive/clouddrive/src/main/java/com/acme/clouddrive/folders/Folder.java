package com.acme.clouddrive.folders;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "folders",
  uniqueConstraints = @UniqueConstraint(name="uq_folder_name_per_parent",
    columnNames = {"owner_id","parent_id","name"}))
public class Folder {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="owner_id", nullable=false)
  private Long ownerId;

  @Column(nullable=false)
  private String name;

  @Column(name="parent_id")
  private Long parentId;

  @Column(nullable=false)
  private String path;          // '/<id>/' or '/<parent>/<id>/'

  @Column(name="created_at")
  private Instant createdAt = Instant.now();

  // getters & setters
  public Long getId(){ return id; }
  public void setId(Long id){ this.id = id; }
  public Long getOwnerId(){ return ownerId; }
  public void setOwnerId(Long ownerId){ this.ownerId = ownerId; }
  public String getName(){ return name; }
  public void setName(String name){ this.name = name; }
  public Long getParentId(){ return parentId; }
  public void setParentId(Long parentId){ this.parentId = parentId; }
  public String getPath(){ return path; }
  public void setPath(String path){ this.path = path; }
  public Instant getCreatedAt(){ return createdAt; }
  public void setCreatedAt(Instant createdAt){ this.createdAt = createdAt; }
}
