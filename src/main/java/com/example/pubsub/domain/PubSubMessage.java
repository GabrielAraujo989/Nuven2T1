package com.example.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PubSubMessage {
    private String uuid;

    @JsonProperty("created_at")
    private String createdAt;

    private String type;
    private Customer customer;
    private List<Room> rooms;

    // Getters e Setters
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Customer {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Room {
        @JsonProperty("id")
        private int id;

        @JsonProperty("daily_rate")
        private double dailyRate;

        @JsonProperty("number_of_days")
        private int numberOfDays;

        @JsonProperty("reservation_date")
        private String reservationDate;

        @JsonProperty("category")
        private Category category;

        // Getters e Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getDailyRate() {
            return dailyRate;
        }

        public void setDailyRate(double dailyRate) {
            this.dailyRate = dailyRate;
        }

        public int getNumberOfDays() {
            return numberOfDays;
        }

        public void setNumberOfDays(int numberOfDays) {
            this.numberOfDays = numberOfDays;
        }

        public String getReservationDate() {
            return reservationDate;
        }

        public void setReservationDate(String reservationDate) {
            this.reservationDate = reservationDate;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Category {
        @JsonProperty("id")
        private String id;

        @JsonProperty("sub_category")
        private SubCategory subCategory;

        // Getters e Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public SubCategory getSubCategory() {
            return subCategory != null ? subCategory : new SubCategory();
        }

        public void setSubCategory(SubCategory subCategory) {
            this.subCategory = subCategory;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SubCategory {
        @JsonProperty("id")
        private String id = "";

        // Getter e Setter
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id != null ? id : "";
        }
    }
}