package com.worldpay.screeningtest.service;

import com.worldpay.screeningtest.domain.Order;
import com.worldpay.screeningtest.domain.OrderCreationRequest;
import com.worldpay.screeningtest.domain.OrderException;
import com.worldpay.screeningtest.domain.OrderStatus;
import com.worldpay.screeningtest.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService underTest;

    @Before
    public void setUp() {
        underTest = new OrderService(orderRepository);
    }

    @Test
    public void shouldCreateValidOrder() throws OrderException {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(348.21);
        orderCreationRequest.setValidFrom(LocalDate.now().minusMonths(1));
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // When
        underTest.createOrder(orderCreationRequest);

        // Then
        verify(orderRepository).save(orderCaptor.capture());
        Order order = orderCaptor.getValue();
        assertThat(order.getDescription()).isEqualTo(orderCreationRequest.getDescription());
        assertThat(order.getPrice()).isEqualTo(orderCreationRequest.getPrice());
        assertThat(order.getValidFrom()).isEqualTo(orderCreationRequest.getValidFrom());
        assertThat(order.getValidForYears()).isEqualTo(orderCreationRequest.getValidForYears());
        assertThat(order.getValidForMonths()).isEqualTo(orderCreationRequest.getValidForMonths());
        assertThat(order.getValidForDays()).isEqualTo(orderCreationRequest.getValidForDays());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.VALID);
    }

    @Test
    public void shouldCreateExpiredOrder() throws OrderException {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(348.21);
        orderCreationRequest.setValidFrom(LocalDate.now().minusYears(2));
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // When
        underTest.createOrder(orderCreationRequest);

        // Then
        verify(orderRepository).save(orderCaptor.capture());
        Order order = orderCaptor.getValue();
        assertThat(order.getDescription()).isEqualTo(orderCreationRequest.getDescription());
        assertThat(order.getPrice()).isEqualTo(orderCreationRequest.getPrice());
        assertThat(order.getValidFrom()).isEqualTo(orderCreationRequest.getValidFrom());
        assertThat(order.getValidForYears()).isEqualTo(orderCreationRequest.getValidForYears());
        assertThat(order.getValidForMonths()).isEqualTo(orderCreationRequest.getValidForMonths());
        assertThat(order.getValidForDays()).isEqualTo(orderCreationRequest.getValidForDays());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.EXPIRED);
    }

    @Test
    public void shouldCreateNonStartedOrder() throws OrderException {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(348.21);
        orderCreationRequest.setValidFrom(LocalDate.now().plusMonths(1));
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // When
        underTest.createOrder(orderCreationRequest);

        // Then
        verify(orderRepository).save(orderCaptor.capture());
        Order order = orderCaptor.getValue();
        assertThat(order.getDescription()).isEqualTo(orderCreationRequest.getDescription());
        assertThat(order.getPrice()).isEqualTo(orderCreationRequest.getPrice());
        assertThat(order.getValidFrom()).isEqualTo(orderCreationRequest.getValidFrom());
        assertThat(order.getValidForYears()).isEqualTo(orderCreationRequest.getValidForYears());
        assertThat(order.getValidForMonths()).isEqualTo(orderCreationRequest.getValidForMonths());
        assertThat(order.getValidForDays()).isEqualTo(orderCreationRequest.getValidForDays());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.NOT_STARTED);
    }

    @Test
    public void shouldThrowExceptionBecauseOfNullDescription() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription(null);
        orderCreationRequest.setPrice(348.21);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12001);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order without description");
    }

    @Test
    public void shouldThrowExceptionBecauseOfEmptyDescription() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("");
        orderCreationRequest.setPrice(348.21);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12001);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order without description");
    }

    @Test
    public void shouldThrowExceptionBecauseOfNullStartingDate() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(348.21);
        orderCreationRequest.setValidFrom(null);
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12002);
        assertThat(e.getMessage()).isEqualTo("Cannot create order with empty starting date");
    }

    @Test
    public void shouldThrowExceptionBecauseOfNullPrice() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(null);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12003);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order without price");
    }

    @Test
    public void shouldThrowExceptionBecauseOfNonPositivePrice() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(0.0);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(14);
        orderCreationRequest.setValidForMonths(6);
        orderCreationRequest.setValidForYears(1);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12004);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order with negative or zero price");
    }

    @Test
    public void shouldThrowExceptionBecauseAllDurationTermsAreNull() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(100.0);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(null);
        orderCreationRequest.setValidForMonths(null);
        orderCreationRequest.setValidForYears(null);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12005);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order without duration terms");
    }

    @Test
    public void shouldThrowExceptionBecauseDurationTermInMonthsIsNegative() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(100.0);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(0);
        orderCreationRequest.setValidForMonths(-1);
        orderCreationRequest.setValidForYears(0);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12006);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order wit negative duration terms");
    }

    @Test
    public void shouldThrowExceptionBecauseDurationTermInDaysIsNegative() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(100.0);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(-1);
        orderCreationRequest.setValidForMonths(0);
        orderCreationRequest.setValidForYears(0);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12006);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order wit negative duration terms");
    }

    @Test
    public void shouldThrowExceptionBecauseDurationTermInYearsIsNegative() {
        // Given
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setDescription("Order Description");
        orderCreationRequest.setPrice(100.0);
        orderCreationRequest.setValidFrom(LocalDate.now());
        orderCreationRequest.setValidForDays(0);
        orderCreationRequest.setValidForMonths(0);
        orderCreationRequest.setValidForYears(-1);

        // When
        Throwable e = catchThrowable(() -> underTest.createOrder(orderCreationRequest));

        // Then
        assertThat(e).isInstanceOf(OrderException.class);
        assertThat(((OrderException)e).getErrorCode()).isEqualTo(12006);
        assertThat(e.getMessage()).isEqualTo("Cannot crate order wit negative duration terms");
    }
}
