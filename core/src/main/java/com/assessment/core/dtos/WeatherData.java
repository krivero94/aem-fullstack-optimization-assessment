package com.assessment.core.dtos;

import java.util.List;

public class WeatherData {
    private String temperature;
    private String wind;
    private String description;
    private List<ForecastDay> forecast;

    // GETTERS
    public String getTemperature() { return temperature; }
    public String getWind() { return wind; }
    public String getDescription() { return description; }
    public List<ForecastDay> getForecast() { return forecast; }

    // SETTERS
    public void setTemperature(String t) { this.temperature = t; }
    public void setWind(String w) { this.wind = w; }
    public void setDescription(String d) { this.description = d; }
    public void setForecast(List<ForecastDay> f) { this.forecast = f; }

    public static class ForecastDay {
        private String day;
        private String temperature;
        private String wind;
        
        public String getDay() { return day; }
        public void setDay(String d) { this.day = d; }
        public String getTemperature() { return temperature; }
        public void setTemperature(String t) { this.temperature = t; }
        public String getWind() { return wind; }
        public void setWind(String w) { this.wind = w; }
    }
}