package com.assessment.core.services.impl;

import com.assessment.core.config.WeatherConfig;
import com.assessment.core.services.WeatherService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WeatherServiceImplTest {

    private WeatherService weatherService;

    @Mock
    private Resource resource;

    @Mock
    private ConfigurationBuilder configBuilder;

    @Mock
    private WeatherConfig weatherConfig;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        weatherService = new WeatherServiceImpl();
        when(resource.adaptTo(ConfigurationBuilder.class)).thenReturn(configBuilder);
        when(configBuilder.as(WeatherConfig.class)).thenReturn(weatherConfig);
        when(weatherConfig.apiKey()).thenReturn("test-key");
        when(weatherConfig.endpoint()).thenReturn("https://goweather.xyz/weather/%s");
    }

    @Test
    public void testServiceInstantiation() {
        assertNotNull(weatherService);
    }
}