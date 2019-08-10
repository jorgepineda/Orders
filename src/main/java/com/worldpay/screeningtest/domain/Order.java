package com.worldpay.screeningtest.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String description;

    private double price;

    private LocalDate validFrom;

    private int validForYears;

    private int validForMonths;

    private int validForDays;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public int getValidForYears() {
        return validForYears;
    }

    public void setValidForYears(int validForYears) {
        this.validForYears = validForYears;
    }

    public int getValidForMonths() {
        return validForMonths;
    }

    public void setValidForMonths(int validForMonths) {
        this.validForMonths = validForMonths;
    }

    public int getValidForDays() {
        return validForDays;
    }

    public void setValidForDays(int validForDays) {
        this.validForDays = validForDays;
    }
}
