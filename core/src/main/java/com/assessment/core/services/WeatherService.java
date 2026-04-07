package com.assessment.core.services;

import org.apache.sling.api.resource.Resource;

import java.io.IOException;

public interface WeatherService {

    String getForecast(String city, Resource resource) throws IOException;
}

