package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A model for the items database where all items available in the game will be
 * stored
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Items")
public class Items {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_id")
	private Long item_id;
	
	@Column(name = "item_type")
	private String item_type;
	
	@Column(name = "item_value")
	private int item_value;
	
	@Column(name = "item_weight")
	private int item_weight;
	
	@Column(name = "item_power")
	private int item_power;
	
	@Column(name = "item_power_cost")
	private int item_power_cost;

	@Column(name = "item_name")
	private String item_name;

	/**
	 * An empty constructor
	 */
	public Items() {

	}

	/**
	 * Retrieves an item's id number
	 * 
	 * @return the item id number
	 */
	public Long getItem_id() {
		return item_id;
	}

	/**
	 * Auto generated
	 * 
	 * @param tem_id
	 *            Auto generated
	 */
	public void setItem_id(Long item_id) {
		this.item_id = item_id;
	}
	
	/**
	 * Retrieves the item type
	 * 
	 * @return the item type
	 */
	public String getItem_type() {
		return item_type;
	}

	/**
	 * Sets the type of the item
	 * 
	 * @param item_type
	 *            The type of the item
	 */
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}

	/**
	 * Retrieves the value of the item
	 * 
	 * @return The value of the item
	 */
	public int getItem_value() {
		return item_value;
	}

	/**
	 * Sets the items value
	 * 
	 * @param item_value
	 *            The value to set as for the item
	 */
	public void setItem_value(int item_value) {
		this.item_value = item_value;
	}

	/**
	 * Retrieves the items weight
	 * 
	 * @return the weight of the item
	 */
	public int getItem_weight() {
		return item_weight;
	}

	/**
	 * Sets the weight of the item
	 * 
	 * @param item_weight
	 *            The weight of the item
	 */
	public void setItem_buy(int item_weight) {
		this.item_weight = item_weight;
	}

	/**
	 * Retrieves the name of the item
	 * 
	 * @return the name of the item
	 */
	public String getItem_name() {
		return item_name;
	}

	/**
	 * Sets the name of the item
	 * 
	 * @param item_name
	 *            The name of item
	 */
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}
	
}