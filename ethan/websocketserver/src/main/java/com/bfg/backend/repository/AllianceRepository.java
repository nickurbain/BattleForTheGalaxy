package com.bfg.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bfg.backend.model.Alliance;

@Repository
public interface AllianceRepository extends CrudRepository<Alliance, String>{

}
