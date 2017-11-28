package business.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CarbelongResultId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class CarbelongResultId implements java.io.Serializable
{

	// Fields
	private String citySeq;
	private Date jgday;
	private String puid;

	// Constructors

	/** default constructor */
	public CarbelongResultId()
	{
	}

	/** full constructor */
	public CarbelongResultId(String citySeq, Date jgday, String puid)
	{
		this.citySeq = citySeq;
		this.jgday = jgday;
		this.puid = puid;
	}

	// Property accessors

	@Column(name = "city_seq", nullable = false, length = 32)
	public String getCitySeq()
	{
		return this.citySeq;
	}

	public void setCitySeq(String citySeq)
	{
		this.citySeq = citySeq;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "jgday", nullable = false, length = 10)
	public Date getJgday()
	{
		return this.jgday;
	}

	public void setJgday(Date jgday)
	{
		this.jgday = jgday;
	}

	@Column(name = "puid", nullable = false, length = 50)
	public String getPuid()
	{
		return this.puid;
	}

	public void setPuid(String puid)
	{
		this.puid = puid;
	}

	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CarbelongResultId))
			return false;
		CarbelongResultId castOther = (CarbelongResultId) other;

		return ((this.getCitySeq() == castOther.getCitySeq()) || (this
				.getCitySeq() != null && castOther.getCitySeq() != null && this
				.getCitySeq().equals(castOther.getCitySeq())))
				&& ((this.getJgday() == castOther.getJgday()) || (this
						.getJgday() != null && castOther.getJgday() != null && this
						.getJgday().equals(castOther.getJgday())))
				&& ((this.getPuid() == castOther.getPuid()) || (this.getPuid() != null
						&& castOther.getPuid() != null && this.getPuid()
						.equals(castOther.getPuid())));
	}

	public int hashCode()
	{
		int result = 17;

		result = 37 * result
				+ (getCitySeq() == null ? 0 : this.getCitySeq().hashCode());
		result = 37 * result
				+ (getJgday() == null ? 0 : this.getJgday().hashCode());
		result = 37 * result
				+ (getPuid() == null ? 0 : this.getPuid().hashCode());
		return result;
	}

}