package com.acme.clouddrive.shortener;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/short")
public class ShortController {
  private final ShortService service;
  public ShortController(ShortService service){ this.service=service; }

  @PostMapping
  public ShortDtos.ShortResponse create(Authentication auth, @RequestBody ShortDtos.CreateRequest req){
    return new ShortDtos.ShortResponse(service.create(auth.getName(), req));
  }

  @GetMapping
  public List<ShortDtos.ShortResponse> myActive(Authentication auth){
    return service.myActive(auth.getName()).stream().map(ShortDtos.ShortResponse::new).toList();
  }

  @PatchMapping("/{id}")
  public ShortDtos.ShortResponse update(Authentication auth, @PathVariable Long id, @RequestBody ShortDtos.UpdateRequest req){
    return new ShortDtos.ShortResponse(service.update(auth.getName(), id, req));
  }

  @GetMapping("/{id}/stats")
  public ShortDtos.StatsResponse stats(@PathVariable Long id){
    return service.stats(id);
  }
}
