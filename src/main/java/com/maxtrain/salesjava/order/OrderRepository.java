package com.maxtrain.salesjava.order;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Integer>{
	Iterable<Order> findByStatus(String status);
}
