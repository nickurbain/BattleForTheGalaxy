package com.bfg.backend.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The model for the BattleStats
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name="BattleStats")
public class BattleStats {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="battle_id")
    private Long battle_id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user_name;
    
    @Column(name="AODM_score") // All out death match score
    private String AODM_score;
    
    @Column(name="ADM_score") // Alliance death match score
    private String ADM_score;
    
    @Column(name="FB_score") // Faction battle score
    private String FB_score;
    
    @Column(name="TDM_score") // Team death match score
    private String TDM_score;
    
    /**
     * Empty constructor 
     */
    public BattleStats() {
    	
    }

    /**
     * Retrieves the battle_id
     * @return the battle_id
     */
	public Long getId() {
		return battle_id;
	}
	
	/**
	 * Battle id is auto generated
	 * @param battle_id Auto generated
	 */
	public void setId(Long battle_id) {
		this.battle_id = battle_id;
	}

	/**
	 * Returns the user name
	 * @return the user name
	 */
	public User getName() {
		return user_name;
	}

	/**
	 * Sets the user name associated with the battle statistic
	 * @param user_name The user_name
	 */
	public void setName(User user_name) {
		this.user_name = user_name;
	}

	/**
	 * Gets the ALL_OUT DEATH MATCH SCORE
	 * @return the ALL_OUT DEATH MATCH SCORE
	 */
	public String getAODM_score() {
		return AODM_score;
	}

	/**
	 * Sets the ALL_OUT DEATH MATCH SCORE
	 * @param aODM_score The score amount to set
	 */
	public void setAODM_score(String aODM_score) {
		AODM_score = aODM_score;
	}

	/**
	 * Retrieves the ALLIANCE DEATH MATCH SCORE
	 * @return the ALLIANCE DEATH MATCH SCORE
	 */
	public String getADM_score() {
		return ADM_score;
	}

	/**
	 * Sets the ALLIANCE DEATH MATCH SCORE
	 * @param aDM_score The score amount to set
	 */
	public void setADM_score(String aDM_score) {
		ADM_score = aDM_score;
	}

	/**
	 * Retrieves the TEAM DEATH MATCH SCORE
	 * @return the TEAM DEATH MATCH SCORE
	 */
	public String getTDM_score() {
		return TDM_score;
	}

	/**
	 * Sets the TEAM DEATH MATCH SCORE
	 * @param tDM_score The score amount to set
	 */
	public void setTDM_score(String tDM_score) {
		TDM_score = tDM_score;
	}
}