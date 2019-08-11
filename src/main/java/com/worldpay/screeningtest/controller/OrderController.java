package com.worldpay.screeningtest.controller;

import com.worldpay.screeningtest.domain.*;
import com.worldpay.screeningtest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/orders")
public class OrderController {

    Logger logger = Logger.getLogger(OrderController.class.getName());
    @Autowired
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value="add", method= RequestMethod.POST)
    @ResponseBody
    public Order addOrder(@RequestBody OrderCreationRequest orderCreationPayload) throws OrderException {
        logger.log(Level.INFO, "Creating order");
        Order order = orderService.createOrder(orderCreationPayload);
        logger.log(Level.INFO, "Created order with ID " + order.getId());
        return order;
    }

    @RequestMapping(value="get", method= RequestMethod.GET)
    @ResponseBody
    public Order getOrder(@RequestParam(value="order-id", required = true) long orderId) {
        logger.log(Level.INFO, "Retrieving order");
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderException.class)
    ResponseEntity<ErrorResponse> handleExceptions(OrderException exception) {
        logger.log(Level.INFO, "Error found " + exception.getErrorCode(),exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(),exception.getMessage());
        return ResponseEntity.status(400).body(errorResponse);
    }

}
