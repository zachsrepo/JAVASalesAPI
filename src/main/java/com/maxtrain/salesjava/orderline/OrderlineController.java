package com.maxtrain.salesjava.orderline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maxtrain.salesjava.item.Item;
import com.maxtrain.salesjava.item.ItemRepository;
import com.maxtrain.salesjava.order.Order;
import com.maxtrain.salesjava.order.OrderRepository;



@CrossOrigin
@RestController
@RequestMapping("/api/orderlines")
public class OrderlineController {
	@Autowired
	private OrderlineRepository ordlineRepo;
	@Autowired
	private OrderRepository ordRepo;
	@Autowired
	private ItemRepository itemRepo;
	
	@GetMapping
	public ResponseEntity<Iterable<Orderline>> getOrderlines(){
		Iterable<Orderline> orderlines = ordlineRepo.findAll();
		return new ResponseEntity<Iterable<Orderline>>(orderlines, HttpStatus.OK);
	}	
	@GetMapping("{id}")
	public ResponseEntity<Orderline> getOrderline(@PathVariable int id){
		Optional<Orderline> orderline = ordlineRepo.findById(id);
		if(orderline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Orderline>(orderline.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Orderline> postOrderline(@RequestBody Orderline orderline){
		Orderline newOrderline = ordlineRepo.save(orderline);
		ordlineRepo.findById(newOrderline.getId());
		Optional<Order> order = ordRepo.findById(orderline.getOrder().getId());
		if(!order.isEmpty()) {
			boolean success = recalculateOrderTotal(order.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<Orderline>(newOrderline, HttpStatus.CREATED);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putOrderline(@PathVariable int id, @RequestBody Orderline orderline) {
		if(orderline.getId() != id) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		ordlineRepo.save(orderline);
		Optional<Order> order = ordRepo.findById(orderline.getOrder().getId());
		if(!order.isEmpty()) {
			boolean success = recalculateOrderTotal(order.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteOrderline(@PathVariable int id) {
		Optional<Orderline> orderline = ordlineRepo.findById(id);
		if(orderline.isEmpty()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		ordlineRepo.delete(orderline.get());
		Optional<Order> order = ordRepo.findById(orderline.get().getOrder().getId());
		if(!order.isEmpty()) {
			boolean success = recalculateOrderTotal(order.get().getId());
			if(!success) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
	private boolean recalculateOrderTotal(int orderId){
		Optional<Order> anOrder = ordRepo.findById(orderId);
		if(anOrder.isEmpty()) {
			return false;
		}
		Order order = anOrder.get();
		//get all the orderlines attached to the order
		Iterable<Orderline> orderlines = ordlineRepo.findByOrderId(orderId);
		double total = 0;
		for(Orderline ol : orderlines) {
			if(ol.getItem().getName() == null) {
				Item item = itemRepo.findById(ol.getItem().getId()).get();
				ol.setItem(item);
			}
			total += ol.getQuantity() * ol.getItem().getPrice();
		}
		//update the total in the order
		order.setTotal(total);
		ordRepo.save(order);
		return true;
	}
	
}
