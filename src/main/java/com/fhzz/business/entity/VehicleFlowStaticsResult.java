package com.fhzz.business.entity;

import javax.persistence.*;

/**
 * VehicleFlowStaticsResult entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "vehicle_flow_statistics_result", catalog = "vehicle")
public class VehicleFlowStaticsResult implements java.io.Serializable
{

	// Fields
	private VehicleFlowStaticsResultId id;
	private Integer totalNums=0;
	private Integer zeroOclock=0;
	private Integer oneOclock=0;
	private Integer twoOclock=0;
	private Integer threeOclock=0;
	private Integer fourOclock=0;
	private Integer fiveOclock=0;
	private Integer sixOclock=0;
	private Integer sevenOclock=0;
	private Integer eightOclock=0;
	private Integer nineOclock=0;
	private Integer tenOclock=0;
	private Integer elevenOclock=0;
	private Integer twelveOclock=0;
	private Integer thirteenOclock=0;
	private Integer fourteenOclock=0;
	private Integer fifteenOclock=0;
	private Integer sixteenOclock=0;
	private Integer seventeenOclock=0;
	private Integer eighteenOclock=0;
	private Integer nineteenOclock=0;
	private Integer twentyOclock=0;
	private Integer twentyOneOclock=0;
	private Integer twentyTwoOclock=0;
	private Integer twentyThreeOclock=0;

	// Constructors

	/** default constructor */
	public VehicleFlowStaticsResult()
	{
	}

	/** minimal constructor */
	public VehicleFlowStaticsResult(VehicleFlowStaticsResultId id,
			Integer zeroOclock, Integer oneOclock, Integer twoOclock,
			Integer threeOclock, Integer fourOclock, Integer fiveOclock,
			Integer sixOclock, Integer sevenOclock, Integer eightOclock,
			Integer nineOclock, Integer tenOclock, Integer elevenOclock,
			Integer twelveOclock, Integer thirteenOclock,
			Integer fourteenOclock, Integer fifteenOclock,
			Integer sixteenOclock, Integer seventeenOclock,
			Integer eighteenOclock, Integer nineteenOclock,
			Integer twentyOclock, Integer twentyOneOclock,
			Integer twentyTwoOclock, Integer twentyThreeOclock)
	{
		this.id = id;
		this.zeroOclock = zeroOclock;
		this.oneOclock = oneOclock;
		this.twoOclock = twoOclock;
		this.threeOclock = threeOclock;
		this.fourOclock = fourOclock;
		this.fiveOclock = fiveOclock;
		this.sixOclock = sixOclock;
		this.sevenOclock = sevenOclock;
		this.eightOclock = eightOclock;
		this.nineOclock = nineOclock;
		this.tenOclock = tenOclock;
		this.elevenOclock = elevenOclock;
		this.twelveOclock = twelveOclock;
		this.thirteenOclock = thirteenOclock;
		this.fourteenOclock = fourteenOclock;
		this.fifteenOclock = fifteenOclock;
		this.sixteenOclock = sixteenOclock;
		this.seventeenOclock = seventeenOclock;
		this.eighteenOclock = eighteenOclock;
		this.nineteenOclock = nineteenOclock;
		this.twentyOclock = twentyOclock;
		this.twentyOneOclock = twentyOneOclock;
		this.twentyTwoOclock = twentyTwoOclock;
		this.twentyThreeOclock = twentyThreeOclock;
	}

	/** full constructor */
	public VehicleFlowStaticsResult(VehicleFlowStaticsResultId id,
			Integer totalNums, Integer zeroOclock, Integer oneOclock,
			Integer twoOclock, Integer threeOclock, Integer fourOclock,
			Integer fiveOclock, Integer sixOclock, Integer sevenOclock,
			Integer eightOclock, Integer nineOclock, Integer tenOclock,
			Integer elevenOclock, Integer twelveOclock, Integer thirteenOclock,
			Integer fourteenOclock, Integer fifteenOclock,
			Integer sixteenOclock, Integer seventeenOclock,
			Integer eighteenOclock, Integer nineteenOclock,
			Integer twentyOclock, Integer twentyOneOclock,
			Integer twentyTwoOclock, Integer twentyThreeOclock)
	{
		this.id = id;
		this.totalNums = totalNums;
		this.zeroOclock = zeroOclock;
		this.oneOclock = oneOclock;
		this.twoOclock = twoOclock;
		this.threeOclock = threeOclock;
		this.fourOclock = fourOclock;
		this.fiveOclock = fiveOclock;
		this.sixOclock = sixOclock;
		this.sevenOclock = sevenOclock;
		this.eightOclock = eightOclock;
		this.nineOclock = nineOclock;
		this.tenOclock = tenOclock;
		this.elevenOclock = elevenOclock;
		this.twelveOclock = twelveOclock;
		this.thirteenOclock = thirteenOclock;
		this.fourteenOclock = fourteenOclock;
		this.fifteenOclock = fifteenOclock;
		this.sixteenOclock = sixteenOclock;
		this.seventeenOclock = seventeenOclock;
		this.eighteenOclock = eighteenOclock;
		this.nineteenOclock = nineteenOclock;
		this.twentyOclock = twentyOclock;
		this.twentyOneOclock = twentyOneOclock;
		this.twentyTwoOclock = twentyTwoOclock;
		this.twentyThreeOclock = twentyThreeOclock;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "puid", column = @Column(name = "puid", nullable = false, length = 50)),
			@AttributeOverride(name = "jgday", column = @Column(name = "jgday", nullable = false, length = 10)) })
	public VehicleFlowStaticsResultId getId()
	{
		return this.id;
	}

	public void setId(VehicleFlowStaticsResultId id)
	{
		this.id = id;
	}

	@Column(name = "total_nums")
	public Integer getTotalNums()
	{
		return this.totalNums;
	}

	public void setTotalNums(Integer totalNums)
	{
		this.totalNums = totalNums;
	}

	@Column(name = "zero_oclock", nullable = false)
	public Integer getZeroOclock()
	{
		return this.zeroOclock;
	}

	public void setZeroOclock(Integer zeroOclock)
	{
		this.zeroOclock = zeroOclock;
	}

	@Column(name = "one_oclock", nullable = false)
	public Integer getOneOclock()
	{
		return this.oneOclock;
	}

	public void setOneOclock(Integer oneOclock)
	{
		this.oneOclock = oneOclock;
	}

	@Column(name = "two_oclock", nullable = false)
	public Integer getTwoOclock()
	{
		return this.twoOclock;
	}

	public void setTwoOclock(Integer twoOclock)
	{
		this.twoOclock = twoOclock;
	}

	@Column(name = "three_oclock", nullable = false)
	public Integer getThreeOclock()
	{
		return this.threeOclock;
	}

	public void setThreeOclock(Integer threeOclock)
	{
		this.threeOclock = threeOclock;
	}

	@Column(name = "four_oclock", nullable = false)
	public Integer getFourOclock()
	{
		return this.fourOclock;
	}

	public void setFourOclock(Integer fourOclock)
	{
		this.fourOclock = fourOclock;
	}

	@Column(name = "five_oclock", nullable = false)
	public Integer getFiveOclock()
	{
		return this.fiveOclock;
	}

	public void setFiveOclock(Integer fiveOclock)
	{
		this.fiveOclock = fiveOclock;
	}

	@Column(name = "six_oclock", nullable = false)
	public Integer getSixOclock()
	{
		return this.sixOclock;
	}

	public void setSixOclock(Integer sixOclock)
	{
		this.sixOclock = sixOclock;
	}

	@Column(name = "seven_oclock", nullable = false)
	public Integer getSevenOclock()
	{
		return this.sevenOclock;
	}

	public void setSevenOclock(Integer sevenOclock)
	{
		this.sevenOclock = sevenOclock;
	}

	@Column(name = "eight_oclock", nullable = false)
	public Integer getEightOclock()
	{
		return this.eightOclock;
	}

	public void setEightOclock(Integer eightOclock)
	{
		this.eightOclock = eightOclock;
	}

	@Column(name = "nine_oclock", nullable = false)
	public Integer getNineOclock()
	{
		return this.nineOclock;
	}

	public void setNineOclock(Integer nineOclock)
	{
		this.nineOclock = nineOclock;
	}

	@Column(name = "ten_oclock", nullable = false)
	public Integer getTenOclock()
	{
		return this.tenOclock;
	}

	public void setTenOclock(Integer tenOclock)
	{
		this.tenOclock = tenOclock;
	}

	@Column(name = "eleven_oclock", nullable = false)
	public Integer getElevenOclock()
	{
		return this.elevenOclock;
	}

	public void setElevenOclock(Integer elevenOclock)
	{
		this.elevenOclock = elevenOclock;
	}

	@Column(name = "twelve_oclock", nullable = false)
	public Integer getTwelveOclock()
	{
		return this.twelveOclock;
	}

	public void setTwelveOclock(Integer twelveOclock)
	{
		this.twelveOclock = twelveOclock;
	}

	@Column(name = "thirteen_oclock", nullable = false)
	public Integer getThirteenOclock()
	{
		return this.thirteenOclock;
	}

	public void setThirteenOclock(Integer thirteenOclock)
	{
		this.thirteenOclock = thirteenOclock;
	}

	@Column(name = "fourteen_oclock", nullable = false)
	public Integer getFourteenOclock()
	{
		return this.fourteenOclock;
	}

	public void setFourteenOclock(Integer fourteenOclock)
	{
		this.fourteenOclock = fourteenOclock;
	}

	@Column(name = "fifteen_oclock", nullable = false)
	public Integer getFifteenOclock()
	{
		return this.fifteenOclock;
	}

	public void setFifteenOclock(Integer fifteenOclock)
	{
		this.fifteenOclock = fifteenOclock;
	}

	@Column(name = "sixteen_oclock", nullable = false)
	public Integer getSixteenOclock()
	{
		return this.sixteenOclock;
	}

	public void setSixteenOclock(Integer sixteenOclock)
	{
		this.sixteenOclock = sixteenOclock;
	}

	@Column(name = "seventeen_oclock", nullable = false)
	public Integer getSeventeenOclock()
	{
		return this.seventeenOclock;
	}

	public void setSeventeenOclock(Integer seventeenOclock)
	{
		this.seventeenOclock = seventeenOclock;
	}

	@Column(name = "eighteen_oclock", nullable = false)
	public Integer getEighteenOclock()
	{
		return this.eighteenOclock;
	}

	public void setEighteenOclock(Integer eighteenOclock)
	{
		this.eighteenOclock = eighteenOclock;
	}

	@Column(name = "nineteen_oclock", nullable = false)
	public Integer getNineteenOclock()
	{
		return this.nineteenOclock;
	}

	public void setNineteenOclock(Integer nineteenOclock)
	{
		this.nineteenOclock = nineteenOclock;
	}

	@Column(name = "twenty_oclock", nullable = false)
	public Integer getTwentyOclock()
	{
		return this.twentyOclock;
	}

	public void setTwentyOclock(Integer twentyOclock)
	{
		this.twentyOclock = twentyOclock;
	}

	@Column(name = "twenty_one_oclock", nullable = false)
	public Integer getTwentyOneOclock()
	{
		return this.twentyOneOclock;
	}

	public void setTwentyOneOclock(Integer twentyOneOclock)
	{
		this.twentyOneOclock = twentyOneOclock;
	}

	@Column(name = "twenty_two_oclock", nullable = false)
	public Integer getTwentyTwoOclock()
	{
		return this.twentyTwoOclock;
	}

	public void setTwentyTwoOclock(Integer twentyTwoOclock)
	{
		this.twentyTwoOclock = twentyTwoOclock;
	}

	@Column(name = "twenty_three_oclock", nullable = false)
	public Integer getTwentyThreeOclock()
	{
		return this.twentyThreeOclock;
	}

	public void setTwentyThreeOclock(Integer twentyThreeOclock)
	{
		this.twentyThreeOclock = twentyThreeOclock;
	}

}