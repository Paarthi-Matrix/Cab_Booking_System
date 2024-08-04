package com.i2i.zapcab.dto;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * This class is responsible for managing the fuel price response details.
 * </p>
 * <p>
 * It contains:
 *   <ol>
 *       <li> City ID where the fuel prices are applicable. </li>
 *       <li> Name of the city where the fuel prices are applicable. </li>
 *       <li> State ID where the fuel prices are applicable. </li>
 *       <li> Name of the state where the fuel prices are applicable. </li>
 *       <li> Country ID where the fuel prices are applicable. </li>
 *       <li> Name of the country where the fuel prices are applicable. </li>
 *       <li> Date when the fuel prices are applicable. </li>
 *       <li> Fuel pricing details including petrol, diesel, and LPG. </li>
 *   </ol>
 * </p>
 */
@Getter
@Setter
public class FuelPriceResponseDto {
    @JsonProperty("cityId")
    private String cityId;
    @JsonProperty("cityName")
    private String cityName;
    @JsonProperty("stateId")
    private String stateId;
    @JsonProperty("stateName")
    private String stateName;
    @JsonProperty("countryId")
    private String countryId;
    @JsonProperty("countryName")
    private String countryName;
    @JsonProperty("applicableOn")
    private String applicableOn;
    @JsonProperty("fuel")
    private Fuel fuel;

    @Getter
    @Setter
    public static class Fuel {
        @JsonProperty("petrol")
        private Petrol petrol;

        @JsonProperty("diesel")
        private Diesel diesel;

        @JsonProperty("lpg")
        private Lpg lpg;
    }

    @Getter
    @Setter
    public static class Petrol {
        @JsonProperty("retailPrice")
        private double retailPrice;

        @JsonProperty("retailPriceChange")
        private double retailPriceChange;

        @JsonProperty("retailUnit")
        private String retailUnit;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("retailPriceChangeInterval")
        private String retailPriceChangeInterval;
    }

    @Getter
    @Setter
    public static class Diesel {
        @JsonProperty("retailPrice")
        private double retailPrice;

        @JsonProperty("retailPriceChange")
        private double retailPriceChange;

        @JsonProperty("retailUnit")
        private String retailUnit;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("retailPriceChangeInterval")
        private String retailPriceChangeInterval;
    }

    public static class Lpg {
        @JsonProperty("retailPrice")
        private double retailPrice;

        @JsonProperty("retailPriceChange")
        private double retailPriceChange;

        @JsonProperty("retailUnit")
        private String retailUnit;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("retailPriceChangeInterval")
        private String retailPriceChangeInterval;
    }
}
