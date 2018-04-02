package com.example.chat.bean;

import com.amap.api.services.weather.LocalDayWeatherForecast;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/10      23:40
 * QQ:             1981367757
 */

public class WeatherInfoBean implements Serializable {
        private String city;
        private String realTime;
        private String forecastTime;
        private String forecastInfo;
        private String wind;
        private String humidity;
        private String temperature;
        private String weatherStatus;


        private List<LocalDayWeatherForecast> forecastInfoList;


        public List<LocalDayWeatherForecast> getForecastInfoList() {
                return forecastInfoList;
        }

        public void setForecastInfoList(List<LocalDayWeatherForecast> forecastInfoList) {
                this.forecastInfoList = forecastInfoList;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getRealTime() {
                return realTime;
        }

        public void setRealTime(String realTime) {
                this.realTime = realTime;
        }

        public String getForecastTime() {
                return forecastTime;
        }

        public void setForecastTime(String forecastTime) {
                this.forecastTime = forecastTime;
        }

        public String getForecastInfo() {
                return forecastInfo;
        }

        public void setForecastInfo(String forecastInfo) {
                this.forecastInfo = forecastInfo;
        }

        public String getWind() {
                return wind;
        }

        public void setWind(String wind) {
                this.wind = wind;
        }

        public String getHumidity() {
                return humidity;
        }

        public void setHumidity(String humidity) {
                this.humidity = humidity;
        }

        public String getTemperature() {
                return temperature;
        }

        public void setTemperature(String temperature) {
                this.temperature = temperature;
        }

        public String getWeatherStatus() {
                return weatherStatus;
        }

        public void setWeatherStatus(String weatherStatus) {
                this.weatherStatus = weatherStatus;
        }
}
