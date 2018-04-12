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

	@Column(name = "item_name")
	private String item_name;

	@Column(name = "item_picture")
	private String item_picture;

	@Column(name = "item_buy")
	private String item_buy;

	@Column(name = "item_sell")
	private String item_sell;

	@Column(name = "item_type")
	private String item_type;

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

	/**
	 * Retrieves the picture of the item
	 * 
	 * @return The picture of the item
	 */
	public String getItem_picture() {
		return item_picture;
	}

	/**
	 * Sets the items picture
	 * 
	 * @param item_picture
	 *            The picture to set as for the item
	 */
	public void setItem_picture(String item_picture) {
		this.item_picture = item_picture;
	}

	/**
	 * Retrieves the items buying price
	 * 
	 * @return the price of the item
	 */
	public String getItem_buy() {
		return item_buy;
	}

	/**
	 * Sets the buying price of the item
	 * 
	 * @param item_buy
	 *            The price of the item
	 */
	public void setItem_buy(String item_buy) {
		this.item_buy = item_buy;
	}

	/**
	 * Sets the selling of the item
	 * 
	 * @return the selling item
	 */
	public String getItem_sell() {
		return item_sell;
	}

	/**
	 * Sets the items selling price
	 * 
	 * @param item_sell
	 *            The selling price of the item
	 */
	public void setItem_sell(String item_sell) {
		this.item_sell = item_sell;
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
}