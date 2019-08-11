package com.worldpay.screeningtest.service;

import com.worldpay.screeningtest.domain.Order;
import com.worldpay.screeningtest.domain.OrderCreationRequest;
import com.worldpay.screeningtest.domain.OrderException;
import com.worldpay.screeningtest.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderCreationRequest orderCreationRequest) throws OrderException {
        // TODO: Insert business logic here
        return null;
    }

    public Order getOrder(long orderId) {
        // TODO: Insert business logic here
        return null;
    }
}
