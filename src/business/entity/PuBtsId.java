package business.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * PuBtsId entity. @author MyEclipse Persistence Tools
 */
@Embeddable

public class PuBtsId  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 4044568534992884219L;
	private String puId;
     private String btsId;


    // Constructors

    /** default constructor */
    public PuBtsId() {
    }

    
    /** full constructor */
    public PuBtsId(String puId, String btsId) {
        this.puId = puId;
        this.btsId = btsId;
    }

   
    // Property accessors

    @Column(name="PU_ID", nullable=false, length=32)

    public String getPuId() {
        return this.puId;
    }
    
    public void setPuId(String puId) {
        this.puId = puId;
    }

    @Column(name="BTS_ID", nullable=false, length=32)

    public String getBtsId() {
        return this.btsId;
    }
    
    public void setBtsId(String btsId) {
        this.btsId = btsId;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof PuBtsId) ) return false;
		 PuBtsId castOther = ( PuBtsId ) other; 
         
		 return ( (this.getPuId()==castOther.getPuId()) || ( this.getPuId()!=null && castOther.getPuId()!=null && this.getPuId().equals(castOther.getPuId()) ) )
 && ( (this.getBtsId()==castOther.getBtsId()) || ( this.getBtsId()!=null && castOther.getBtsId()!=null && this.getBtsId().equals(castOther.getBtsId()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getPuId() == null ? 0 : this.getPuId().hashCode() );
         result = 37 * result + ( getBtsId() == null ? 0 : this.getBtsId().hashCode() );
         return result;
   }   





}