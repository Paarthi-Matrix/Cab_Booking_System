package com.i2i.zapcab.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
