package com.zilch.customerservice.persistance.dao;

import com.zilch.customerservice.persistance.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CustomerRepositoryTest {

    private static final String FIRST_NAME = "FirstTestName";
    private static final String LAST_NAME = "LastTestName";
    private static final int AGE = 40;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldReturnEmptyCustomerListWhenCustomersNotCreated() {
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList.size()).isZero();
    }

    @Test
    void itShouldCreateCustomer() {
        Optional<Customer> customer = customerRepository.findById(saveCustomer().getId());
        assertOnCustomer(customer);
    }

    @Test
    void itShouldGetCustomerById() {
        Long id = saveCustomer().getId();
        Optional<Customer> customer = customerRepository.findById(id);
        assertOnCustomer(customer);
    }

    @Test
    void itShouldDeleteCustomer() {
        Long id = saveCustomer().getId();
        customerRepository.deleteById(id);
        assertThat(customerRepository.findById(id)).isEmpty();
    }

    private Customer saveCustomer() {
        return customerRepository.save(new Customer(FIRST_NAME, LAST_NAME, AGE));
    }

    private void assertOnCustomer(Optional<Customer> customer) {
        assertThat(customer).isPresent();
        assertThat(customer.get().getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(customer.get().getLastName()).isEqualTo(LAST_NAME);
        assertThat(customer.get().getAge()).isEqualTo(AGE);
    }

}