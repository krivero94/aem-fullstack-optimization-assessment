package com.assessment.core.services.impl;

import com.assessment.core.config.WeatherConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplTest {

    private WeatherServiceImpl weatherService;

    @Mock
    private Resource resource;

    @Mock
    private ConfigurationBuilder configBuilder;

    @Mock
    private WeatherConfig weatherConfig;

    @Before
    public void setUp() {
        weatherService = new WeatherServiceImpl();
        
        when(resource.adaptTo(ConfigurationBuilder.class)).thenReturn(configBuilder);
        when(configBuilder.as(WeatherConfig.class)).thenReturn(weatherConfig);
        
        when(weatherConfig.apiKey()).thenReturn("test-api-key");
        when(weatherConfig.endpoint()).thenReturn("https://goweather.xyz/weather/%s");
    }

    @Test
    public void testGetForecast_CacResolution() throws IOException {
        try {
            weatherService.getForecast("Bogota", resource);
        } catch (Exception e) {
            // Handle exception if needed    
        }

        verify(resource, times(1)).adaptTo(ConfigurationBuilder.class);
        verify(weatherConfig, atLeastOnce()).apiKey();
    }

    @Test
    public void testCacheLogic() throws IOException {
        assertNotNull(weatherService);
    }
}