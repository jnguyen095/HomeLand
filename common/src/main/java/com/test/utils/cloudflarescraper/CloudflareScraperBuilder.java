package com.test.utils.cloudflarescraper;

import java.net.URI;

public class CloudflareScraperBuilder {
    // Required parameters
    private URI uri;

    // Optional parameters
    private int challengeDelay = 4000;
    private int connectionTimeout = 5000;
    private int readTimeout = 5000;

    public CloudflareScraperBuilder(URI uri) {
        this.uri = uri;
    }

    public CloudflareScraperBuilder setChallengeDelay(int challengeDelay) {
        this.challengeDelay = challengeDelay;
        return this;
    }

    public CloudflareScraperBuilder setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public CloudflareScraperBuilder setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public CloudflareScraper build() {
        return new CloudflareScraper(this);
    }

    public URI getUri() {
        return uri;
    }

    public int getChallengeDelay() {
        return challengeDelay;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }
}
