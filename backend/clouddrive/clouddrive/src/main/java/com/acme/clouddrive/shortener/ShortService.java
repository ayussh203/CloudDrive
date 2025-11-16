package com.acme.clouddrive.shortener;

import com.acme.clouddrive.user.User;
import com.acme.clouddrive.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

@Service
public class ShortService {
  private final ShortUrlRepository urls;
  private final ShortUrlHitRepository hits;
  private final UserRepository users;
  private static final String ALPH = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final SecureRandom RND = new SecureRandom();

  public ShortService(ShortUrlRepository urls, ShortUrlHitRepository hits, UserRepository users){
    this.urls=urls; this.hits=hits; this.users=users;
  }

  private Long ownerId(String email){
    return users.findByEmail(email.toLowerCase()).map(User::getId)
        .orElseThrow(() -> new IllegalArgumentException("user_not_found"));
  }

  private String genCode(int len){
    while(true){
      StringBuilder sb = new StringBuilder(len);
      for(int i=0;i<len;i++) sb.append(ALPH.charAt(RND.nextInt(ALPH.length())));
      String c = sb.toString();
      if (!urls.existsByCodeIgnoreCase(c)) return c;
    }
  }

  @Transactional
  public ShortUrl create(String email, ShortDtos.CreateRequest req){
    Long owner = ownerId(email);

    if (req.slug()!=null && !req.slug().isBlank()){
      String slug = req.slug().trim().toLowerCase(Locale.ROOT);
      if (!slug.matches("^[a-z0-9-]{3,64}$")) throw new IllegalArgumentException("invalid_slug");
      if (urls.existsByOwnerIdAndSlugIgnoreCase(owner, slug))
        throw new IllegalStateException("slug_taken");
    }

    if (req.targetUrl()==null || req.targetUrl().isBlank())
      throw new IllegalArgumentException("target_url_blank");
    String t = req.targetUrl().trim();
    if (!(t.startsWith("https://") || t.startsWith("http://")))
      throw new IllegalArgumentException("target_url_invalid");

    ShortUrl s = new ShortUrl();
    s.setOwnerId(owner);
    s.setCode(genCode(8));
    s.setSlug(req.slug());
    s.setTargetUrl(t);
    if (req.expiresInSec()!=null && req.expiresInSec()>0)
      s.setExpiresAt(Instant.now().plusSeconds(req.expiresInSec()));
    return urls.save(s);
  }

  @Transactional
  public ShortUrl update(String email, Long id, ShortDtos.UpdateRequest req){
    Long owner = ownerId(email);
    ShortUrl s = urls.findById(id).orElseThrow(() -> new IllegalArgumentException("not_found"));
    if (!Objects.equals(owner, s.getOwnerId())) throw new IllegalArgumentException("forbidden");

    if (req.active()!=null) s.setActive(req.active());
    if (req.extendBySec()!=null && req.extendBySec()>0){
      Instant base = Optional.ofNullable(s.getExpiresAt()).orElse(Instant.now());
      s.setExpiresAt(base.plusSeconds(req.extendBySec()));
    }
    return urls.save(s);
  }

  public List<ShortUrl> myActive(String email){
    return urls.findActiveByOwner(ownerId(email), Instant.now());
  }

  @Transactional
  public Optional<String> resolveAndLog(String code, String ip, String referer, String ua){
    Optional<ShortUrl> os = urls.findByCode(code);
    if (os.isEmpty()) return Optional.empty();

    ShortUrl s = os.get();
    if (!s.isActive()) return Optional.empty();
    if (s.getExpiresAt()!=null && s.getExpiresAt().isBefore(Instant.now())) return Optional.empty();

    ShortUrlHit h = new ShortUrlHit();
    h.setShortId(s.getId());
    h.setIp(ip);
    h.setReferer(referer);
    h.setUserAgent(ua);
    hits.save(h);

    return Optional.of(s.getTargetUrl());
  }

  public ShortDtos.StatsResponse stats(Long id){
    Instant now = Instant.now();
    long last24h = hits.countByShortIdAndHitAtBetween(id, now.minusSeconds(86400), now);
    long last7d  = hits.countByShortIdAndHitAtBetween(id, now.minusSeconds(7*86400), now);
    long total   = hits.countByShortIdAndHitAtBetween(id, Instant.EPOCH, now);
    return new ShortDtos.StatsResponse(last24h, last7d, total);
  }
}
