package com.assessment.core.services.impl;

import com.assessment.core.config.WeatherConfig;
import com.assessment.core.services.WeatherService;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component(service = WeatherService.class, immediate = true)
public class WeatherServiceImpl implements WeatherService {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final long CACHE_TTL = TimeUnit.MINUTES.toMillis(5);

    private static class CacheEntry {
        final String value;
        final long timestamp;

        CacheEntry(String value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > CACHE_TTL;
        }
    }

    @Override
    public String getForecast(String city, Resource resource) throws IOException {
        // Context aware
        WeatherConfig config = resource.adaptTo(ConfigurationBuilder.class)
                .as(WeatherConfig.class);
        String apiKey = config.apiKey();
        String endpoint = config.endpoint();
        String cacheKey = city + "|" + apiKey;

        CacheEntry entry = cache.get(cacheKey);
        if (entry != null && !entry.isExpired()) {
            return entry.value;
        }

        // Fetch new data
        String url = String.format(endpoint, city) + "?apikey=" + apiKey;
        HttpGet httpGet = new HttpGet(url);
        String result = httpClient.execute(httpGet, response -> {
            if (response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                throw new IOException("Failed to fetch weather data: " + response.getStatusLine());
            }
        });

        cache.put(cacheKey, new CacheEntry(result));
        return result;
    }
}
