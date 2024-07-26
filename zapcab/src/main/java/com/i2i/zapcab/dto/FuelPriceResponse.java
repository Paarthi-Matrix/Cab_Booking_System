package com.i2i.zapcab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p>
 * Data Transfer Object for capturing the response of a fuel price API.
 * This class maps the JSON response to Java objects for easy access and manipulation.
 * </p>
 */

@Data
public class FuelPriceResponse {
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

    public static class Fuel {
        @JsonProperty("petrol")
        private Petrol petrol;

        @JsonProperty("diesel")
        private Diesel diesel;

        public Diesel getDiesel() {
            return diesel;
        }

        public void setDiesel(Diesel diesel) {
            this.diesel = diesel;
        }

        public Lpg getLpg() {
            return lpg;
        }

        public void setLpg(Lpg lpg) {
            this.lpg = lpg;
        }

        @JsonProperty("lpg")
        private Lpg lpg;

        public Petrol getPetrol() {
            return petrol;
        }

        public void setPetrol(Petrol petrol) {
            this.petrol = petrol;
        }
    }

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


        public double getRetailPrice() {
            return retailPrice;
        }


        public void setRetailPrice(double retailPrice) {
            this.retailPrice = retailPrice;
        }
    }

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

    public double getPetrolPrice() {
        return this.fuel.getPetrol().getRetailPrice();
    }
}
