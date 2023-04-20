package com.zilch.customerservice.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import javax.xml.bind.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CustomerDTOTest {

  @ParameterizedTest(name = "{index} - First Name: {0}, Last Name: {1}, Age: {2}")
  @MethodSource("nameProvider")
  void itShouldTestDtoValidations(
      String firstName, String lastName, Integer age, String expectedMessage) {
    CustomerDTO customer =
        CustomerDTO.builder().firstName(firstName).lastName(lastName).age(age).build();
    if (expectedMessage == null) {
      assertDoesNotThrow(customer::validate);
    } else {
      ValidationException ex = assertThrows(ValidationException.class, customer::validate);
      assertTrue(ex.getMessage().contains(expectedMessage));
    }
  }

  static Stream<Arguments> nameProvider() {
    return Stream.of(
        arguments("", "Kim", 30, "Invalid FirstName: Empty name"),
        arguments(null, "Kim", 50, "Invalid FirstName: Name is NULL"),
        arguments("Ki", "White", 10, "Invalid FirstName: Must be of 3 - 30 characters"),
        arguments(
            "Kim12345678901234568901234567890",
            "White",
            20,
            "Invalid FirstName: Must be of 3 - 30 characters"),
        arguments("Kim", "", 40, "Invalid LastName: Empty name"),
        arguments("White", null, 30, "Invalid LastName: Name is NULL"),
        arguments(
            "White",
            "White12345678901234568901234567890",
            50,
            "Invalid LastName: Must be of 3 - 30 characters"),
        arguments("Kim", "White", 0, "Invalid Age: Equals to zero or Less than zero"),
        arguments("Kim", "White", 101, "Invalid Age: Exceeds 100 years"));
  }
}
