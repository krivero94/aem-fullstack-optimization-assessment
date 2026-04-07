package com.assessment.core.config;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Weather Service Configuration", description = "Configuration for weather service API")
public @interface WeatherConfig {

    @Property(label = "API Key", description = "API key for weather service")
    String apiKey() default "";

    @Property(label = "Endpoint URL", description = "Base URL for weather service")
    String endpoint() default "https://goweather.xyz/weather/%s";

    @Property(label = "Cache TTL", description = "Cache time to live in seconds")
    int cacheTtl() default 300;

}