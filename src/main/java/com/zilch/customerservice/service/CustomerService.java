package com.zilch.customerservice.service;

import com.zilch.customerservice.dto.CustomerDTO;
import com.zilch.customerservice.exception.NoSuchElementFoundException;
import com.zilch.customerservice.persistance.dao.CustomerRepository;
import com.zilch.customerservice.persistance.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
  @Autowired
  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Cacheable("customerCache")
  public CustomerDTO getById(Long id) {
    return createCustomerDto(findCustomerById(id));
  }

  public CustomerDTO save(CustomerDTO customerDTO) {
    return createCustomerDto(customerRepository.save(mapToCustomer(customerDTO)));
  }

  @CachePut(value = "customerCache", key = "#id")
  public CustomerDTO update(Long id, CustomerDTO customerDTO) {
    Customer customer = findCustomerById(id);
    customer.setFirstName(customerDTO.getFirstName());
    customer.setLastName(customerDTO.getLastName());
    customer.setAge(customerDTO.getAge());
    return createCustomerDto(customerRepository.save(customer));
  }

  @CacheEvict(value = "customerCache", key = "#id")
  public void delete(Long id) {
    customerRepository.delete(findCustomerById(id));
  }

  private Customer findCustomerById(Long id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementFoundException("Customer Not Found"));
  }

  private CustomerDTO createCustomerDto(Customer customer) {
    return CustomerDTO.builder()
        .id(customer.getId())
        .firstName(customer.getFirstName())
        .lastName(customer.getLastName())
        .age(customer.getAge())
        .build();
  }

  private Customer mapToCustomer(CustomerDTO customerDTO) {
    return new Customer(
        customerDTO.getFirstName(), customerDTO.getLastName(), customerDTO.getAge());
  }
}
