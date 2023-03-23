package com.maxtrain.salesjava.orderline;

import com.maxtrain.salesjava.item.Item;
import com.maxtrain.salesjava.order.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="orderlines")
public class Orderline {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int quantity;
	@ManyToOne(optional=false)
	@JoinColumn(name="itemId", columnDefinition="int")
	private Item item;
	@ManyToOne(optional=false)
	@JoinColumn(name="orderId", columnDefinition="int")
	private Order order;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Orderline() {}
}
