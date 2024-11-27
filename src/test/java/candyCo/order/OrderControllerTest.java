package candyCo.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    static List<Order> orders = new ArrayList<>();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @BeforeAll
    static void setUpBeforeClass() {
        for (int i = 1; i <= 5; i++) {
            orders.add(new Order(i, "Order " + i, null, null, null));
        }
    }
    @Test
    void createOrder() throws Exception {

        Order newOrder = new Order(6L, "New Order", null, null, null);

        when(orderService.createOrderFromRequest(any(OrderRequest.class))).thenReturn(newOrder);

        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newOrder)))
                .andExpect(status().isCreated());

    }
    @Test
    void getOrderById() throws Exception {
        Order order = orders.get(0);

        when(orderService.getOrderById(1L)).thenReturn(order);

        this.mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk());

    }
    @Test
    void getAllOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(orders);

        this.mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());

    }
    @Test
    void deleteOrder() throws Exception {
        this.mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteAllOrders() throws Exception {
        this.mockMvc.perform(delete("/api/orders/deleteAll"))
                .andExpect(status().isNoContent());
    }
    @Test
    void updateOrder() throws Exception {
        OrderUpdateRequest updateRequest = new OrderUpdateRequest();

        Order updatedOrder = new Order(1L, "Updated Order", null, null, null);

        when(orderService.updateOrder(1L, updateRequest)).thenReturn(updatedOrder);

        this.mockMvc.perform(patch("/api/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

    }
}
