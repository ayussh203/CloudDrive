package com.acme.clouddrive.shortener;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "short_urls",
       indexes = {
         @Index(name="idx_short_urls_owner", columnList = "ownerId"),
         @Index(name="idx_short_urls_active", columnList = "isActive")
       },
       uniqueConstraints = {
         @UniqueConstraint(name="uq_short_code", columnNames = "code"),
         @UniqueConstraint(name="uq_owner_slug", columnNames = {"ownerId","slug"})
       })
public class ShortUrl {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false) private Long ownerId;
  @Column(nullable=false, length=24) private String code;
  @Column(length=64) private String slug;
  @Column(nullable=false, columnDefinition="text") private String targetUrl;
  private Instant expiresAt;
  @Column(nullable=false) private boolean isActive = true;
  @Column(nullable=false) private Instant createdAt = Instant.now();

  // getters/setters
  public Long getId(){return id;}
  public Long getOwnerId(){return ownerId;}
  public void setOwnerId(Long v){ownerId=v;}
  public String getCode(){return code;}
  public void setCode(String v){code=v;}
  public String getSlug(){return slug;}
  public void setSlug(String v){slug=v;}
  public String getTargetUrl(){return targetUrl;}
  public void setTargetUrl(String v){targetUrl=v;}
  public Instant getExpiresAt(){return expiresAt;}
  public void setExpiresAt(Instant v){expiresAt=v;}
  public boolean isActive(){return isActive;}
  public void setActive(boolean v){isActive=v;}
  public Instant getCreatedAt(){return createdAt;}
  public void setCreatedAt(Instant v){createdAt=v;}
}
