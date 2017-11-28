package business.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * VehicleFlowStaticsResultId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class VehicleFlowStaticsResultId implements java.io.Serializable
{

	// Fields

	private String puid;
	private Date jgday;

	// Constructors

	/** default constructor */
	public VehicleFlowStaticsResultId()
	{
	}

	/** full constructor */
	public VehicleFlowStaticsResultId(String puid, Date jgday)
	{
		this.puid = puid;
		this.jgday = jgday;
	}

	// Property accessors

	@Column(name = "puid", nullable = false, length = 50)
	public String getPuid()
	{
		return this.puid;
	}

	public void setPuid(String puid)
	{
		this.puid = puid;
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

	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof VehicleFlowStaticsResultId))
			return false;
		VehicleFlowStaticsResultId castOther = (VehicleFlowStaticsResultId) other;

		return ((this.getPuid() == castOther.getPuid()) || (this.getPuid() != null
				&& castOther.getPuid() != null && this.getPuid().equals(
				castOther.getPuid())))
				&& ((this.getJgday() == castOther.getJgday()) || (this
						.getJgday() != null && castOther.getJgday() != null && this
						.getJgday().equals(castOther.getJgday())));
	}

	public int hashCode()
	{
		int result = 17;

		result = 37 * result
				+ (getPuid() == null ? 0 : this.getPuid().hashCode());
		result = 37 * result
				+ (getJgday() == null ? 0 : this.getJgday().hashCode());
		return result;
	}

}