package com.zilch.customerservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.zilch.customerservice.dto.CustomerDTO;
import com.zilch.customerservice.exception.NoSuchElementFoundException;
import com.zilch.customerservice.persistance.dao.CustomerRepository;
import com.zilch.customerservice.persistance.entity.Customer;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerService customerService;

  private Customer customer;
  private CustomerDTO customerDTO;

  @BeforeEach
  public void setUp() {
    customer = new Customer("John", "Doe", 30);
    customer.setId(1L);

    customerDTO = CustomerDTO.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .age(30)
            .build();
  }

  @Test
  void itShouldGetCustomerById() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    CustomerDTO retrievedCustomerDTO = customerService.getById(1L);

    assertEquals(customerDTO, retrievedCustomerDTO);
    verify(customerRepository, times(1)).findById(1L);
  }

  @Test
  void itShouldSaveCustomer() {
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    CustomerDTO savedCustomerDTO = customerService.save(customerDTO);

    assertEquals(customerDTO, savedCustomerDTO);
    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  void itShouldUpdateCustomer() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);

    CustomerDTO updatedCustomerDTO = customerService.update(1L, customerDTO);

    assertEquals(customerDTO, updatedCustomerDTO);
    verify(customerRepository, times(1)).findById(1L);
    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  void itShouldDeleteCustomer() {
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

    customerService.delete(1L);

    verify(customerRepository, times(1)).findById(1L);
    verify(customerRepository, times(1)).delete(any(Customer.class));
  }

  @Test
  void itShouldThrowWhenCustomerNotFound() {
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NoSuchElementFoundException.class, () -> customerService.getById(1L));
    verify(customerRepository, times(1)).findById(1L);
  }
}

