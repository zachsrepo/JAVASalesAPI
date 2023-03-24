package com.maxtrain.salesjava.orderline;

import org.springframework.data.repository.CrudRepository;

public interface OrderlineRepository extends CrudRepository<Orderline, Integer>	{
	Iterable<Orderline> findByOrderId (int orderId);
}
