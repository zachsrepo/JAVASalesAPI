package com.maxtrain.salesjava.customer;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	Optional<Customer> findByCode(String code);
}
