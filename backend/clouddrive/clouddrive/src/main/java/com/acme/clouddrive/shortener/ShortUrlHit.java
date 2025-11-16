package com.acme.clouddrive.shortener;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name="short_url_hits", indexes = {
  @Index(name="idx_hits_short_time", columnList="shortId, hitAt")
})
public class ShortUrlHit {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false) private Long shortId;
  @Column(nullable=false) private Instant hitAt = Instant.now();
  private String ip;        // store as text for simplicity
  private String referer;
  private String userAgent;

  // getters/setters
  public Long getId(){return id;}
  public Long getShortId(){return shortId;}
  public void setShortId(Long v){shortId=v;}
  public Instant getHitAt(){return hitAt;}
  public void setHitAt(Instant v){hitAt=v;}
  public String getIp(){return ip;}
  public void setIp(String v){ip=v;}
  public String getReferer(){return referer;}
  public void setReferer(String v){referer=v;}
  public String getUserAgent(){return userAgent;}
  public void setUserAgent(String v){userAgent=v;}
}
