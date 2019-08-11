package com.worldpay.screeningtest.repository;

import com.worldpay.screeningtest.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    // TODO: Insert business logic here is any required
}
