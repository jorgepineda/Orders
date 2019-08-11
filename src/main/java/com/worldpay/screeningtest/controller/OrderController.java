package com.worldpay.screeningtest.controller;

import com.worldpay.screeningtest.domain.*;
import com.worldpay.screeningtest.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

   @Autowired
   private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(value="add", method= RequestMethod.POST)
    @ResponseBody
    public Order addOrder(@RequestBody OrderCreationRequest orderCreationPayload) throws OrderException {
        // TODO : Insert business logic here
        return null;
    }

    @RequestMapping(value="get", method= RequestMethod.GET)
    @ResponseBody
    public Order getOrder(@RequestParam(value="order-id", required = true) long orderId) {
        // TODO : Insert business logic here
        return null;
    }

}
