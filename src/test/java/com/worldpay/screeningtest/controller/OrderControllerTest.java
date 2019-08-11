package com.worldpay.screeningtest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.worldpay.screeningtest.domain.*;
import com.worldpay.screeningtest.service.OrderService;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService)).build();
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        // Given
        given(orderService.createOrder(any()))
                .willReturn(getTestOrder(LocalDate.of(2018, 12, 15), OrderStatus.VALID));

        // When
        ResultActions result = mockMvc.perform(post("/orders/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"description\" : \"Description of goods or services in the order\"," +
                        "\"price\" : \"398.41\"," +
                        "\"validFrom\" : \"2018-12-15\" ," +
                        "\"validForYears\" : 1," +
                        "\"validForMonths\" : 3," +
                        "\"validForDays\" : 25" +
                        "}"));

        // Then
        result.andExpect(status().isOk());
        MvcResult mvcResult = result.andReturn();
        Order returnedOrder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(returnedOrder.getId()).isEqualTo(1556L);
        assertThat(returnedOrder.getDescription()).isEqualTo("Description of goods or services in the order");
        assertThat(returnedOrder.getPrice()).isEqualTo(398.41);
        assertThat(returnedOrder.getValidFrom()).isEqualTo(LocalDate.of(2018, 12, 15));
        assertThat(returnedOrder.getValidForYears()).isEqualTo(1);
        assertThat(returnedOrder.getValidForMonths()).isEqualTo(6);
        assertThat(returnedOrder.getValidForDays()).isEqualTo(25);
        assertThat(returnedOrder.getStatus()).isEqualTo(OrderStatus.VALID);
    }

    @Test
    public void shouldRetrieveErrorWhenNewOrderCannotBeAdded() throws Exception {
        // Given
        given(orderService.createOrder(any()))
                .willThrow(new OrderException(12000, "Error text explaining the reason for the failure"));

        // When
        ResultActions result = mockMvc.perform(post("/orders/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"description\" : \"Description of goods or services in the order\"," +
                        "\"price\" : \"398.41\"," +
                        "\"validFrom\" : \"2018-12-15\" ," +
                        "\"validForYears\" : 1," +
                        "\"validForMonths\" : 3," +
                        "\"validForDays\" : 25" +
                        "}"));

        result.andExpect(status().isBadRequest());
        MvcResult mvcResult = result.andReturn();
        ErrorResponse errorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(errorResponse.getApiErrorCode()).isEqualTo(12000);
        assertThat(errorResponse.getApiErrorMessage()).isEqualTo("Error text explaining the reason for the failure");
    }

    @Test
    public void shouldRetrieveActiveOrder() throws Exception {
        // Given
        given(orderService.getOrder(1556)).willReturn(getTestOrder(LocalDate.of(2019, 9, 1), OrderStatus.VALID));

        // When
        ResultActions result = mockMvc.perform(get("/orders/get?order-id=1556"));

        result.andExpect(status().isOk());
        MvcResult mvcResult = result.andReturn();
        Order returnedOrder = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);
        assertThat(returnedOrder.getId()).isEqualTo(1556L);
        assertThat(returnedOrder.getDescription()).isEqualTo("Description of goods or services in the order");
        assertThat(returnedOrder.getPrice()).isEqualTo(398.41);
        assertThat(returnedOrder.getValidFrom()).isEqualTo(LocalDate.of(2019, 9, 1));
        assertThat(returnedOrder.getValidForYears()).isEqualTo(1);
        assertThat(returnedOrder.getValidForMonths()).isEqualTo(6);
        assertThat(returnedOrder.getValidForDays()).isEqualTo(25);
        assertThat(returnedOrder.getStatus()).isEqualTo(OrderStatus.VALID);
    }

    @Test
    public void shouldRetrieveNullWhenOrderIsNotActive() throws Exception {
        // Given
        given(orderService.getOrder(1556)).willReturn(getTestOrder(LocalDate.of(2019, 9, 1), OrderStatus.EXPIRED));

        // When
        ResultActions result = mockMvc.perform(get("/orders/get?order-id=1556"));

        result.andExpect(status().isOk());
        MvcResult mvcResult = result.andReturn();
        assertThat(mvcResult.getResponse().getContentLength()).isEqualTo(0);
    }

    private Order getTestOrder(LocalDate orderStartDate, OrderStatus orderCreationStatus) {
        Order order = new Order();
        order.setId(1556L);
        order.setDescription("Description of goods or services in the order");
        order.setPrice(398.41);
        order.setValidFrom(orderStartDate);
        order.setValidForYears(1);
        order.setValidForMonths(6);
        order.setValidForDays(25);
        order.setStatus(orderCreationStatus);
        return order;
    }
}
