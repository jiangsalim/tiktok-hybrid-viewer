package com.tiktokviewer.proxy.model;

public class ProxyState {
    private String address;
    private String tier;
    private String status;
    private int consecutiveFailures;
    private long lastUsed;

    public ProxyState() {}

    public ProxyState(String address, String tier, String status) {
        this.address = address;
        this.tier = tier;
        this.status = status;
        this.consecutiveFailures = 0;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getConsecutiveFailures() { return consecutiveFailures; }
    public void setConsecutiveFailures(int consecutiveFailures) { this.consecutiveFailures = consecutiveFailures; }
    public long getLastUsed() { return lastUsed; }
    public void setLastUsed(long lastUsed) { this.lastUsed = lastUsed; }
}
