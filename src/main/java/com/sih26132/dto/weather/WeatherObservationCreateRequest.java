
package com.sih26132.dto.weather;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Map;

public class WeatherObservationCreateRequest {

    @NotNull
    private OffsetDateTime observedAt;

    @DecimalMin("-100.0")
    @DecimalMax("100.0")
    private Double temperatureC;

    @DecimalMin("0.0")
    @DecimalMax("100.0")
    private Double humidityPercent;

    @DecimalMin("0.0")
    private Double rainfallMm;

    @DecimalMin("0.0")
    private Double windSpeedKmh;

    @DecimalMin("0.0")
    private Double leafWetness;

    @Size(max = 100)
    private String source;

    private Map<String, Object> rawData;

    public OffsetDateTime getObservedAt() {
        return observedAt;
    }

    public void setObservedAt(OffsetDateTime observedAt) {
        this.observedAt = observedAt;
    }

    public Double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(Double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public Double getHumidityPercent() {
        return humidityPercent;
    }

    public void setHumidityPercent(Double humidityPercent) {
        this.humidityPercent = humidityPercent;
    }

    public Double getRainfallMm() {
        return rainfallMm;
    }

    public void setRainfallMm(Double rainfallMm) {
        this.rainfallMm = rainfallMm;
    }

    public Double getWindSpeedKmh() {
        return windSpeedKmh;
    }

    public void setWindSpeedKmh(Double windSpeedKmh) {
        this.windSpeedKmh = windSpeedKmh;
    }

    public Double getLeafWetness() {
        return leafWetness;
    }

    public void setLeafWetness(Double leafWetness) {
        this.leafWetness = leafWetness;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Map<String, Object> getRawData() {
        return rawData;
    }

    public void setRawData(Map<String, Object> rawData) {
        this.rawData = rawData;
    }
}
