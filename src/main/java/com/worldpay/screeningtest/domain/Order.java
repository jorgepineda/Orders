package com.worldpay.screeningtest.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

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

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate validFrom;

    private Integer validForYears;

    private Integer validForMonths;

    private Integer validForDays;

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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
