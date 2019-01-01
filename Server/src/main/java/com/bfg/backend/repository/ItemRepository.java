package com.bfg.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bfg.backend.model.Items;

@Repository
public interface ItemRepository extends CrudRepository<Items, String> {
	
	@Query(value =  "SELECT item_name FROM Items WHERE item_name = ?1", nativeQuery = true)
	String findItemByName(@Param(value = "item_name") String item_name);
	
	@Query(value =  "SELECT item_name FROM Items WHERE item_id = ?1", nativeQuery = true)
	String findItemNameByID(@Param(value = "item_id") long item_id);
	
	@Query(value =  "SELECT item_id FROM Items WHERE item_name = ?1", nativeQuery = true)
	Long findItemIdByName(@Param(value = "item_name") String item_name);
	
	@Query(value =  "SELECT item_type FROM Items WHERE item_id = ?1", nativeQuery = true)
	int findItemTypeByID(@Param(value = "item_id") long item_id);
	
	@Query(value =  "SELECT item_value FROM Items WHERE item_id = ?1", nativeQuery = true)
	int findItemValueByID(@Param(value = "item_id") long item_id);
	
}
