package com.acme.clouddrive.shortener;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublicRedirectController {
  private final ShortService service;
  public PublicRedirectController(ShortService service){ this.service=service; }

  // e.g., GET /u/AbC12xYz -> 302 Location: <target>
  @GetMapping("/u/{code}")
  public ResponseEntity<?> go(HttpServletRequest req, @PathVariable String code){
    String ip = req.getRemoteAddr();
    String ref = req.getHeader("referer");
    String ua = req.getHeader("user-agent");

    return service.resolveAndLog(code, ip, ref, ua)
      .<ResponseEntity<?>>map(url -> ResponseEntity.status(302).header("Location", url).build())
      .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
