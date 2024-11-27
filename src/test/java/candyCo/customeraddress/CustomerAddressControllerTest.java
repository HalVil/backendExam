package candyCo.customeraddress;

import candyCo.application.error.CustomerAddressNotFoundException;
import candyCo.customer.Customer;
import candyCo.customer.CustomerService;
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

@WebMvcTest(controllers = CustomerAddressController.class)
class CustomerAddressControllerTest {

    static List<CustomerAddress> customerAddresses = new ArrayList<>();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerAddressService customerAddressService;

    @MockBean
    CustomerService customerService;

    @BeforeAll
    static void setUpBeforeClass() {
        for (int i = 0; i < 5; i++) {
            customerAddresses.add(new CustomerAddress(
                    "Street " + i,
                    "City " + i,
                    "State " + i,
                    10000 + i,
                    null
            ));
        }
    }
    @Test
    void getAllAddresses() throws Exception {
        when(customerAddressService.getAllAddresses()).thenReturn(customerAddresses);

        this.mockMvc.perform(get("/api/customer-addresses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].street").value("Street 0"));
    }
    @Test
    void createCustomerAddress() throws Exception {
        var newAddress = new CustomerAddressRequest();
        newAddress.setStreet("New Street");
        newAddress.setCity("New City");
        newAddress.setState("New State");
        newAddress.setZipCode(20000);
        newAddress.setCustomerId(1L);

        when(customerService.getCustomerById(1L)).thenReturn(new Customer("Test", "Customer", 1234567890, "test@example.com"));

        when(customerAddressService.createAddress(any(CustomerAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        this.mockMvc.perform(post("/api/customer-addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newAddress)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("New Street"))
                .andExpect(jsonPath("$.city").value("New City"))
                .andExpect(jsonPath("$.state").value("New State"))
                .andExpect(jsonPath("$.zipCode").value(20000));
    }
    @Test
    void deleteCustomerAddress() throws Exception {
        this.mockMvc.perform(delete("/api/customer-addresses/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteAllCustomerAddresses() throws Exception {
        this.mockMvc.perform(delete("/api/customer-addresses/deleteAll"))
                .andExpect(status().isNoContent());
    }
    @Test
    void getCustomerAddressById_CustomerAddressNotFound() throws Exception {
        when(customerAddressService.getAddressById(99L)).thenThrow(new CustomerAddressNotFoundException(" Address with ID 99 not found"));

        this.mockMvc.perform(get("/api/customer-addresses/99"))
                .andExpect(status().isNotFound());

    }
    @Test
    void getAddressById() throws Exception {
        this.mockMvc.perform(get("/api/customer-addresses/4"))
                .andExpect(status().isOk());

    }
}
