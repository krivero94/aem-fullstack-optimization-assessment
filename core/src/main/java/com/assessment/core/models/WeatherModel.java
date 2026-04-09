package com.assessment.core.models;

import com.assessment.core.dtos.WeatherData;
import com.assessment.core.services.WeatherService;
import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class WeatherModel {

    @Inject
    private String city;

    @Inject
    private Page currentPage;

    @Inject
    private WeatherService weatherService;

    @SlingObject
    private Resource resource;

    private WeatherData weatherData;

    @PostConstruct
    protected void init() {
        try {
            String requestedCity = city != null ? city : "Bogota";
            String json = weatherService.getForecast(requestedCity, resource);
            if (json != null && !json.isEmpty()) {
                this.weatherData = new Gson().fromJson(json, WeatherData.class);
            }
        } catch (Exception e) {
            this.weatherData = null;
        }
    }

    public String getCity() {
        return city != null ? city : "Bogota";
    }

    public String getPageTitle() {
        return currentPage != null ? currentPage.getTitle() : "Weather Page";
    }


    public WeatherData getWeatherData() {
        return weatherData;
    }
}
