package business.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * PuBts entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="PU_BTS"
    ,schema="VBDS"
)
public class PuBts  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -6528516390709822534L;
	private PuBtsId id;
     private String bz;


    // Constructors

    /** default constructor */
    public PuBts() {
    }

	/** minimal constructor */
    public PuBts(PuBtsId id) {
        this.id = id;
    }
    
    /** full constructor */
    public PuBts(PuBtsId id, String bz) {
        this.id = id;
        this.bz = bz;
    }

   
    // Property accessors
    @EmbeddedId
    
    @AttributeOverrides( {
        @AttributeOverride(name="puId", column=@Column(name="PU_ID", nullable=false, length=32) ), 
        @AttributeOverride(name="btsId", column=@Column(name="BTS_ID", nullable=false, length=32) ) } )

    public PuBtsId getId() {
        return this.id;
    }
    
    public void setId(PuBtsId id) {
        this.id = id;
    }
    
    @Column(name="BZ", length=200)

    public String getBz() {
        return this.bz;
    }
    
    public void setBz(String bz) {
        this.bz = bz;
    }
   








}