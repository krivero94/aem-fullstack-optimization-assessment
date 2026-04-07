package com.assessment.core.models;

import com.assessment.core.services.WeatherService;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WeatherModel {

    @Inject
    private String city;

    @Inject
    private Page currentPage;

    @Inject
    private WeatherService weatherService;

    @SlingObject
    private Resource resource;

    private String weatherJson;

    @PostConstruct
    protected void init() {
        try {
            String requestedCity = city != null ? city : "Bogota";
            weatherJson = weatherService.getForecast(requestedCity, resource);
        } catch (Exception e) {
            weatherJson = "{\"error\": \"Unable to fetch weather data\"}";
        }
    }

    public String getCity() {
        return city != null ? city : "Bogota";
    }

    public String getWeatherJson() {
        return weatherJson;
    }

    public String getPageTitle() {
        return currentPage != null ? currentPage.getTitle() : "Weather Page";
    }
}
