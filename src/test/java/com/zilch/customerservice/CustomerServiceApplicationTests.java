package com.zilch.customerservice;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.zilch.customerservice.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = CustomerServiceApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceApplicationTests {

  private static final String BASE_URL = "/api/customers";

  private final TestRestTemplate restTemplate = new TestRestTemplate();

  private final HttpHeaders headers = new HttpHeaders();

  @LocalServerPort
  private int port;

  @Autowired
  CacheManager cacheManager;
  
  @Test
  void itShouldFailForCreateNewCustomer() {
    // create
    String firstName = "Kim12345678901234568901234567890";
    String lastName = "White12345678901234568901234567890";
    int age = 124;
    ResponseEntity<CustomerDTO> customerResponseEntity =
        postCustomer(createCustomerHttpEntity(firstName, lastName, age));
    assertThat(customerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  void itShouldCreateNewCustomer() {
    // create
    String firstName = "Kim";
    String lastName = "White";
    int age = 24;
    ResponseEntity<CustomerDTO> customerResponseEntity =
        postCustomer(createCustomerHttpEntity(firstName, lastName, age));
    assertThat(customerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    // assert
    CustomerDTO customerDTO = customerResponseEntity.getBody();
    assertCustomer(firstName, lastName, age, customerDTO);
  }

  @Test
  void itShouldGetCustomerById() {
    // create customer
    String firstName = "Courtney";
    String lastName = "Blank";
    int age = 24;
    Long id = postCustomer(createCustomerHttpEntity(firstName, lastName, age)).getBody().getId();

    // get and assert customer
    ResponseEntity<CustomerDTO> customerResponseEntity = getCustomer(id);
    assertThat(customerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    CustomerDTO customerDTO = customerResponseEntity.getBody();
    assertCustomer(firstName, lastName, age, customerDTO);

    // assert customer saved on cache
    Cache.ValueWrapper cachedCustomer = getCachedCustomer(id);
    assertThat(cachedCustomer).isNotNull();
    assertCustomer(firstName, lastName, age, (CustomerDTO) cachedCustomer.get());
  }

  @Test
  void itShouldUpdateCustomer() {
    // create customer
    String firstName = "Kim";
    String lastName = "Blank";
    int age = 29;
    Long id = postCustomer(createCustomerHttpEntity(firstName, lastName, age)).getBody().getId();

    // update customer
    String updatedFirstName = "KimK";
    String updatedLastName = "Blank-White";
    int updatedAge = 31;
    updateCustomer(createCustomerHttpEntity(updatedFirstName, updatedLastName, updatedAge), id);

    // assert customer updated in cache
    Cache.ValueWrapper cachedCustomer = getCachedCustomer(id);
    assertThat(cachedCustomer).isNotNull();
    assertCustomer(
        updatedFirstName, updatedLastName, updatedAge, (CustomerDTO) cachedCustomer.get());
  }

  @Test
  void itShouldDeleteCustomerById() {
    // create customer
    String firstName = "Courtney";
    String lastName = "Blank";
    int age = 24;
    Long id = postCustomer(createCustomerHttpEntity(firstName, lastName, age)).getBody().getId();

    // delete customer
    deleteCustomer(id);

    // get and assert
    ResponseEntity<CustomerDTO> customerResponseEntity = getCustomer(id);
    assertThat(customerResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    // assert customer deleted from cache
    Cache.ValueWrapper cachedCustomer = getCachedCustomer(id);
    assertThat(cachedCustomer).isNull();
  }

  private HttpEntity<CustomerDTO> createCustomerHttpEntity(
      String firstName, String lastName, int age) {
    return new HttpEntity<>(createCustomerDTO(firstName, lastName, age), headers);
  }

  private CustomerDTO createCustomerDTO(String firstName, String lastName, int age) {
    return CustomerDTO.builder().firstName(firstName).lastName(lastName).age(age).build();
  }

  private ResponseEntity<CustomerDTO> getCustomer(Long id) {
    return restTemplate.getForEntity(getUri(BASE_URL + "/" + id), CustomerDTO.class);
  }

  private ResponseEntity<CustomerDTO> postCustomer(HttpEntity<CustomerDTO> entity) {
    return restTemplate.postForEntity(getUri(BASE_URL), entity, CustomerDTO.class);
  }

  private void updateCustomer(HttpEntity<CustomerDTO> entity, Long id) {
    restTemplate.put(getUri(BASE_URL + "/" + id), entity);
  }

  private void deleteCustomer(Long id) {
    restTemplate.delete(getUri(BASE_URL + "/" + id));
  }

  private String getUri(String uri) {
    return "http://localhost:" + port + uri;
  }

  private static void assertCustomer(
      String firstName, String lastName, int age, CustomerDTO customerDTO) {
    assertThat(customerDTO).isNotNull();
    assertThat(customerDTO.getFirstName()).isEqualTo(firstName);
    assertThat(customerDTO.getLastName()).isEqualTo(lastName);
    assertThat(customerDTO.getAge()).isEqualTo(age);
  }

  private Cache.ValueWrapper getCachedCustomer(Long id) {
    Cache customerCache = cacheManager.getCache("customerCache");
    return customerCache.get(id);
  }
}
