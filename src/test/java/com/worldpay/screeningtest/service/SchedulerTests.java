package com.worldpay.screeningtest.service;

import com.worldpay.screeningtest.Orders;
import com.worldpay.screeningtest.domain.Order;
import com.worldpay.screeningtest.domain.OrderStatus;
import com.worldpay.screeningtest.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Orders.class})
public class SchedulerTests {

    @Autowired
    private OrderRepository orderRepository;

    private OrderService underTest;

    @Before
    public void setUp() {
        underTest = new OrderService(orderRepository);
    }

    @Test
    public void shouldExpireOrders() {
        // Given
        Order order1Before = new Order();
        order1Before.setDescription("Order to be expired");
        order1Before.setPrice(500.0);
        order1Before.setValidFrom(LocalDate.now().minusMonths(6));
        order1Before.setValidForMonths(3);
        order1Before.setStatus(OrderStatus.VALID);
        orderRepository.save(order1Before);

        Order order2Before = new Order();
        order2Before.setDescription("Order not to be changed");
        order2Before.setPrice(500.0);
        order2Before.setValidFrom(LocalDate.now().plusMonths(6));
        order2Before.setValidForMonths(3);
        order2Before.setStatus(OrderStatus.NOT_STARTED);
        orderRepository.save(order2Before);

        Order order3Before = new Order();
        order3Before.setDescription("Order not to be changed");
        order3Before.setPrice(500.0);
        order3Before.setValidFrom(LocalDate.now().minusMonths(6));
        order3Before.setValidForMonths(3);
        order3Before.setStatus(OrderStatus.EXPIRED);
        orderRepository.save(order3Before);

        // When
        underTest.expireOrders();

        // Then
        Optional<Order> order1After = orderRepository.findById(order1Before.getId());
        Optional<Order> order2After = orderRepository.findById(order2Before.getId());
        Optional<Order> order3After = orderRepository.findById(order3Before.getId());

        // Checks that only the status in order1 was change
        assertThat(order1After.get().getStatus()).isEqualTo(OrderStatus.EXPIRED);
        assertThat(order1After.get().getDescription()).isEqualTo(order1Before.getDescription());
        assertThat(order1After.get().getPrice()).isEqualTo(order1Before.getPrice());
        assertThat(order1After.get().getValidFrom()).isEqualTo(order1Before.getValidFrom());
        assertThat(order1After.get().getValidForYears()).isEqualTo(order1Before.getValidForYears());
        assertThat(order1After.get().getValidForMonths()).isEqualTo(order1Before.getValidForMonths());
        assertThat(order1After.get().getValidForDays()).isEqualTo(order1Before.getValidForDays());

        // Checks that the other rows did not change
        assertThat(order2After.get()).isEqualToComparingFieldByField(order2Before);
        assertThat(order3After.get()).isEqualToComparingFieldByField(order3Before);
    }

    @Test
    public void shouldActivateOrders() {
        // Given
        Order order1Before = new Order();
        order1Before.setDescription("Order to be activated");
        order1Before.setPrice(500.0);
        order1Before.setValidFrom(LocalDate.now().minusDays(1));
        order1Before.setValidForYears(1);
        order1Before.setStatus(OrderStatus.NOT_STARTED);
        orderRepository.save(order1Before);

        Order order2Before = new Order();
        order2Before.setDescription("Order not to be changed");
        order2Before.setPrice(500.0);
        order2Before.setValidFrom(LocalDate.now().minusMonths(6));
        order2Before.setValidForMonths(12);
        order2Before.setStatus(OrderStatus.VALID);
        orderRepository.save(order2Before);

        Order order3Before = new Order();
        order3Before.setDescription("Order not to be changed");
        order3Before.setPrice(500.0);
        order3Before.setValidFrom(LocalDate.now().minusMonths(6));
        order3Before.setValidForMonths(3);
        order3Before.setStatus(OrderStatus.EXPIRED);
        orderRepository.save(order3Before);

        // When
        underTest.activateOrders();

        // Then
        Optional<Order> order1After = orderRepository.findById(order1Before.getId());
        Optional<Order> order2After = orderRepository.findById(order2Before.getId());
        Optional<Order> order3After = orderRepository.findById(order3Before.getId());

        // Checks that only the status in order1 was change
        assertThat(order1After.get().getStatus()).isEqualTo(OrderStatus.VALID);
        assertThat(order1After.get().getDescription()).isEqualTo(order1Before.getDescription());
        assertThat(order1After.get().getPrice()).isEqualTo(order1Before.getPrice());
        assertThat(order1After.get().getValidFrom()).isEqualTo(order1Before.getValidFrom());
        assertThat(order1After.get().getValidForYears()).isEqualTo(order1Before.getValidForYears());
        assertThat(order1After.get().getValidForMonths()).isEqualTo(order1Before.getValidForMonths());
        assertThat(order1After.get().getValidForDays()).isEqualTo(order1Before.getValidForDays());

        // Checks that the other rows did not change
        assertThat(order2After.get()).isEqualToComparingFieldByField(order2Before);
        assertThat(order3After.get()).isEqualToComparingFieldByField(order3Before);
    }

}
