package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="Items")
public class Items {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="item_id")
    private Long item_id;

    @Column(name="item_name")
    private String item_name;
    
    @Column(name="item_picture")
    private String item_picture;
    
    @Column(name="item_buy")
    private String item_buy;
    
    @Column(name="item_sell")
    private String item_sell;
    
    @Column(name="item_type")
    private String item_type;

    public Items() {
    	
    }
    
	public Long getItem_id() {
		return item_id;
	}

	public void setItem_id(Long item_id) {
		this.item_id = item_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_picture() {
		return item_picture;
	}

	public void setItem_picture(String item_picture) {
		this.item_picture = item_picture;
	}

	public String getItem_buy() {
		return item_buy;
	}

	public void setItem_buy(String item_buy) {
		this.item_buy = item_buy;
	}

	public String getItem_sell() {
		return item_sell;
	}

	public void setItem_sell(String item_sell) {
		this.item_sell = item_sell;
	}

	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}
    
}
