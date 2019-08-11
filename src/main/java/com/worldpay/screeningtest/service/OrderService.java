package com.worldpay.screeningtest.service;

import com.worldpay.screeningtest.domain.Order;
import com.worldpay.screeningtest.domain.OrderCreationRequest;
import com.worldpay.screeningtest.domain.OrderException;
import com.worldpay.screeningtest.domain.OrderStatus;
import com.worldpay.screeningtest.repository.OrderRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderCreationRequest orderCreationRequest) throws OrderException {
        validateOrderInputData(orderCreationRequest);

        Order order = new Order();
        order.setDescription(orderCreationRequest.getDescription());
        order.setPrice(orderCreationRequest.getPrice());
        order.setValidFrom(orderCreationRequest.getValidFrom());
        order.setValidForYears(orderCreationRequest.getValidForYears());
        order.setValidForMonths(orderCreationRequest.getValidForMonths());
        order.setValidForDays(orderCreationRequest.getValidForDays());
        order.setStatus(calculateOrderStatus(order));

        return orderRepository.save(order);
    }

    public Order getOrder(long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && order.get().getStatus() == OrderStatus.VALID) {
            return order.get();
        }
        return null;
    }

    private void validateOrderInputData(OrderCreationRequest orderCreationRequest) throws OrderException {
        if (StringUtils.isEmpty(orderCreationRequest.getDescription())) {
            throw new OrderException(12001, "Cannot crate order without description");
        }
        if (orderCreationRequest.getValidFrom() == null) {
            throw new OrderException(12002, "Cannot create order with empty starting date");
        }
        if (orderCreationRequest.getPrice() == null) {
            throw new OrderException(12003, "Cannot crate order without price");
        }
        if (orderCreationRequest.getPrice() <= 0.0) {
            throw new OrderException(12004, "Cannot crate order with negative or zero price");
        }
        if (orderCreationRequest.getValidForDays() == null && orderCreationRequest.getValidForMonths() == null && orderCreationRequest.getValidForYears() == null) {
            throw new OrderException(12005, "Cannot crate order without duration terms");
        }
        if ((orderCreationRequest.getValidForDays() != null && orderCreationRequest.getValidForDays() < 0) ||
                (orderCreationRequest.getValidForMonths() != null && orderCreationRequest.getValidForMonths() < 0) ||
                (orderCreationRequest.getValidForYears() != null && orderCreationRequest.getValidForYears() < 0)) {
            throw new OrderException(12006, "Cannot crate order wit negative duration terms");
        }
    }

    private LocalDate calculateExpirationDate(Order order) {
        LocalDate expirationDate = order.getValidFrom();
        if ((order.getValidForDays() != null && order.getValidForDays() > 0)) {
            expirationDate = expirationDate.plusDays(order.getValidForDays());
        }
        if ((order.getValidForMonths() != null && order.getValidForMonths() > 0)) {
            expirationDate = expirationDate.plusMonths(order.getValidForMonths());
        }
        if ((order.getValidForYears() != null && order.getValidForYears() > 0)) {
            expirationDate = expirationDate.plusYears(order.getValidForYears());
        }
        return expirationDate;
    }

    private OrderStatus calculateOrderStatus(Order order) {
        LocalDate today = LocalDate.now();
        LocalDate orderExpirationDate =  calculateExpirationDate(order);

        if (order.getValidFrom().isAfter(today)) {
             return OrderStatus.NOT_STARTED;
        } else
        if (orderExpirationDate.isBefore(today)) {
            return OrderStatus.EXPIRED;
        } else {
            return OrderStatus.VALID;
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void expireOrders() {
        orderRepository.expireOrders(new Date());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void activateOrders() {
        orderRepository.activateOrders(new Date());
    }


}
