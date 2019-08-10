package com.worldpay.screeningtest.repository;

import com.worldpay.screeningtest.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
