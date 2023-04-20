package com.zilch.customerservice.persistance.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Customer {
	@Id
	@SequenceGenerator(name = "customer_seq",
			sequenceName = "customer_sequence",
			initialValue = 100, allocationSize = 20)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
	@Column
	private Long id;

	@NotNull
	@Column
	private String firstName;

	@NotNull
	@Column
	private String lastName;

	@NotNull
	@Column
	private Integer age;

	public Customer(String firstName, String lastName, Integer age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
	}
}
