package com.tiktokviewer.proxy.service;

import com.tiktokviewer.proxy.model.ProxyState;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProxyPoolManager {

    private final Map<String, ProxyState> proxies = new ConcurrentHashMap<>();
    private final List<String> tier1 = new ArrayList<>();
    private final List<String> tier2 = new ArrayList<>();
    private String torProxy = null;

    public ProxyPoolManager() {
        // WebShare proxies would be loaded from config
        tier1.add("45.67.89.12:8080");
        tier1.add("45.67.89.13:8080");
        tier2.add("scraped.proxy.1:8080");
        torProxy = "127.0.0.1:9050";

        for (String addr : tier1) {
            proxies.put(addr, new ProxyState(addr, "tier_1", "healthy"));
        }
        for (String addr : tier2) {
            proxies.put(addr, new ProxyState(addr, "tier_2", "healthy"));
        }
    }

    public synchronized ProxyState getNextHealthy() {
        for (String addr : tier1) {
            ProxyState p = proxies.get(addr);
            if (p != null && "healthy".equals(p.getStatus())) {
                return p;
            }
        }
        for (String addr : tier2) {
            ProxyState p = proxies.get(addr);
            if (p != null && "healthy".equals(p.getStatus())) {
                return p;
            }
        }
        if (torProxy != null) {
            return new ProxyState(torProxy, "tier_3", "healthy");
        }
        return null;
    }

    public void markHealthy(String address) {
        ProxyState p = proxies.get(address);
        if (p != null) {
            p.setStatus("healthy");
            p.setConsecutiveFailures(0);
        }
    }

    public void markDegraded(String address) {
        ProxyState p = proxies.get(address);
        if (p != null) {
            p.setConsecutiveFailures(p.getConsecutiveFailures() + 1);
            if (p.getConsecutiveFailures() >= 3) {
                p.setStatus("burned");
            } else {
                p.setStatus("degraded");
            }
        }
    }
}
