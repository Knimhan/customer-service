package com.zilch.customerservice.controller;

import com.zilch.customerservice.dto.CustomerDTO;
import com.zilch.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/customers")
public class CustomerResource {

  @Autowired 
  private CustomerService customerService;

  @ExternalDocumentation(
      description =
          "Returns a customer as per the id from cache if not present it cache it will hit db")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found")
      })
  @GetMapping(value = "/{id}")
  public CustomerDTO getById(@PathVariable("id") Long id) {
    return customerService.getById(id);
  }

  @ExternalDocumentation(description = "Saves customer in DB, lso validates request")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "400", description = "Validation problem"),
      })
  @PostMapping
  public CustomerDTO save(@RequestBody @Valid CustomerDTO customerDTO) {
    return customerService.save(customerDTO);
  }

  @ExternalDocumentation(description = "Updates customer in DB and cache, also validates request")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "400", description = "Validation problem"),
        @ApiResponse(responseCode = "404", description = "Not found"),
      })
  @PutMapping(value = "/{id}")
  public CustomerDTO update(
      @PathVariable("id") Long id, @RequestBody @Valid CustomerDTO customerDTO) {
    return customerService.update(id, customerDTO);
  }

  @ExternalDocumentation(description = "Updates customer in DB. Also validates request")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Not found"),
      })
  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable("id") Long id) {
    customerService.delete(id);
  }
}
