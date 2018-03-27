package com.bfg.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Model.BattleStats;

@Repository
public interface BattleStatsRepository extends CrudRepository<BattleStats, Long> {

}
