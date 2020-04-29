package br.com.alura.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/cache")
public class CacheController {

    @Autowired
    private CacheManager manager;

    @GetMapping("/clear")
    public String clearCache() {
        manager.getCacheNames().stream().forEach(cacheName -> {
            System.out.println("Invalidating cache region: " + cacheName);
            manager.getCache(cacheName).clear();
        });
        return "cache cleared";
    }
}
