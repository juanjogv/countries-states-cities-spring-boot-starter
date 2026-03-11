package com.csc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "csc")
public class CscProperties {

    private boolean enabled = true;
    private boolean exposeApi = true;
    private String basePath = "/api/v1/geo";
    private boolean seedData = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isExposeApi() {
        return exposeApi;
    }

    public void setExposeApi(boolean exposeApi) {
        this.exposeApi = exposeApi;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isSeedData() {
        return seedData;
    }

    public void setSeedData(boolean seedData) {
        this.seedData = seedData;
    }
}
