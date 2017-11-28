package com.fhzz.business.entity;

import javax.persistence.*;


/**
 * CarbelongResult entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "carbelong_result", catalog = "vehicle"
)
public class CarbelongResult implements java.io.Serializable {
    // Fields
    private CarbelongResultId id;
    private Integer totalNums  = 0;
    private Integer provinceid = 0;


    // Constructors
    /**
     * default constructor
     */
    public CarbelongResult() {
    }

    /**
     * minimal constructor
     */
    public CarbelongResult(CarbelongResultId id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public CarbelongResult(CarbelongResultId id, Integer totalNums, Integer provinceid) {
        this.id = id;
        this.totalNums = totalNums;
        this.provinceid = provinceid;
    }


    // Property accessors
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "citySeq", column = @Column(name = "city_seq", nullable = false, length = 32)),
            @AttributeOverride(name = "jgday", column = @Column(name = "jgday", nullable = false, length = 10)),
            @AttributeOverride(name = "puid", column = @Column(name = "puid", nullable = false, length = 50))})
    public CarbelongResultId getId() {
        return this.id;
    }

    public void setId(CarbelongResultId id) {
        this.id = id;
    }

    @Column(name = "total_nums")
    public Integer getTotalNums() {
        return this.totalNums;
    }

    public void setTotalNums(Integer totalNums) {
        this.totalNums = totalNums;
    }

    @Column(name = "provinceid")
    public Integer getProvinceid() {
        return this.provinceid;
    }

    public void setProvinceid(Integer provinceid) {
        this.provinceid = provinceid;
    }
}