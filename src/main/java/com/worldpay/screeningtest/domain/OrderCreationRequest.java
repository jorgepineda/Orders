package com.worldpay.screeningtest.domain;

import java.time.LocalDate;

public class OrderCreationRequest {
    private String description;

    private Double price;

    private LocalDate validFrom;

    private Integer validForYears;

    private Integer validForMonths;

    private Integer validForDays;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public Integer getValidForYears() {
        return validForYears;
    }

    public void setValidForYears(Integer validForYears) {
        this.validForYears = validForYears;
    }

    public Integer getValidForMonths() {
        return validForMonths;
    }

    public void setValidForMonths(Integer validForMonths) {
        this.validForMonths = validForMonths;
    }

    public Integer getValidForDays() {
        return validForDays;
    }

    public void setValidForDays(Integer validForDays) {
        this.validForDays = validForDays;
    }
}
