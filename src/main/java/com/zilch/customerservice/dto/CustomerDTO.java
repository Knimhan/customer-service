package com.zilch.customerservice.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.*;
import javax.xml.bind.ValidationException;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Invalid FirstName: Empty name")
    @NotNull(message = "Invalid FirstName: Name is NULL")
    @Size(min = 3, max = 30, message = "Invalid FirstName: Must be of 3 - 30 characters")
    private String firstName;

    @NotBlank(message = "Invalid LastName: Empty name")
    @NotNull(message = "Invalid LastName: Name is NULL")
    @Size(min = 3, max = 30, message = "Invalid LastName: Must be of 3 - 30 characters")
    private String lastName;

    @Min(value = 1, message = "Invalid Age: Equals to zero or Less than zero")
    @Max(value = 100, message = "Invalid Age: Exceeds 100 years")
    private Integer age;

    public void validate() throws ValidationException {
        Set<ConstraintViolation<CustomerDTO>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(this);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        }
    }
}
