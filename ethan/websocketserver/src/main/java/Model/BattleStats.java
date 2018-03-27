package Model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.bfg.backend.User;

//import org.hibernate.validator.constraints.NotEmpty;


@Entity // This tells Hibernate to make a table out of this class
@Table(name="BattleStats")
public class BattleStats {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="battle_id")
    private Long battle_id;

    @OneToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "user_name")
    private User user_name;
    
    @Column(name="AODM_score") // All out death match score
    private String AODM_score;
    
    @Column(name="ADM_score") // Alliance death match score
    private String ADM_score;
    
    @Column(name="FB_score") // Faction battle score
    private String FB_score;
    
    @Column(name="TDM_score") // Team death match score
    private String TDM_score;
    
    public BattleStats() {}

	public Long getId() {
		return battle_id;
	}
	
	public void setId(Long battle_id) {
		this.battle_id = battle_id;
	}

	public User getName() {
		return user_name;
	}

	public void setName(User user_name) {
		this.user_name = user_name;
	}

	public String getAODM_score() {
		return AODM_score;
	}

	public void setAODM_score(String aODM_score) {
		AODM_score = aODM_score;
	}

	public String getADM_score() {
		return ADM_score;
	}

	public void setADM_score(String aDM_score) {
		ADM_score = aDM_score;
	}

	public String getFB_score() {
		return FB_score;
	}

	public void setFB_score(String fB_score) {
		FB_score = fB_score;
	}

	public String getTDM_score() {
		return TDM_score;
	}

	public void setTDM_score(String tDM_score) {
		TDM_score = tDM_score;
	}
}