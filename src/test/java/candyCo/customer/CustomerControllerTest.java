package candyCo.customer;

import candyCo.application.error.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerTest {

    static List<Customer> customers = new ArrayList<>();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @BeforeAll
    static void setUpBeforeClass() {
        for (int i = 0; i < 10; i++) {
            customers.add(new Customer(
                    "Customer " + i,
                    "LastName " + i,
                    1234567890 + i,
                    "email" + i + "@example.com"
            ));
        }
    }
    @Test
    void getAllCustomers() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(customers);

        this.mockMvc.perform(get("/api/customers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Customer 0"));
    }
    @Test
    void createCustomer() throws Exception {
        var newCustomer = new Customer(
                "NewCustomer",
                "NewLastName",
                1234567890,
                "newcustomer@example.com"
        );
        when(customerService.createCustomer(any(Customer.class))).thenReturn(newCustomer);

        this.mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newCustomer)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("NewCustomer"))
                .andExpect(jsonPath("$.lastName").value("NewLastName"))
                .andExpect(jsonPath("$.email").value("newcustomer@example.com"))
                .andExpect(jsonPath("$.phone").value(1234567890));
    }
    @Test
    void deleteCustomer() throws Exception {
        this.mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isOk());
    }
    @Test
    void deleteAllCustomers() throws Exception {
        this.mockMvc.perform(delete("/api/customers/deleteAll"))
                .andExpect(status().isNoContent());
    }
    @Test
    void updateCustomerContact() throws Exception {
        var updateRequest = new CustomerContactUpdateRequest(
                "updated.email@example.com",
                987654321
        );

        var updatedCustomer = new Customer(
                "Customer 1",
                "LastName 1",
                987654321,
                "updated.email@example.com"
        );

        when(customerService.updateCustomerContact(1L, updateRequest.getEmail(), updateRequest.getPhone()))
                .thenReturn(updatedCustomer);

        this.mockMvc.perform(patch("/api/customers/1/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated.email@example.com"))
                .andExpect(jsonPath("$.phone").value(987654321));
    }
    @Test
    void getCustomerById_CustomerNotFound() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new CustomerNotFoundException("Customer with ID 99 not found"));

        this.mockMvc.perform(get("/api/customers/99"))
                .andExpect(status().isNotFound());

    }
}
