package business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @FileName : (ImpalaConfig.java)
 *
 * @description : impala配置
 * @author : Zhihao.Du
 * @version : Version No.1
 * @create : 2016年9月7日 下午5:22:29
 * @modify : 2016年9月7日 下午5:22:29
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
@Repository("impalaConfig")
public class ImpalaConfig {

	/**
	 * jdctx impala表名
	 */
	@Value("${jdctx.impala.table.name}")
	private String jdctxImpalaTableName;
	
	/**
	 * fence impala表名
	 */
	@Value("${fence.impala.table.name}")
	private String btsImpalaTableName;
	
	@Value("${diff.car.table}")
	private String diffCarTableName;
	
	@Value("${ccrc.table}")
	private String ccrcTableName;
	
	@Value("${jdctx.invalid.hphm}")
	private String invalidHphm;

	@Value("${wifi.invalid.mac}")
	private String invalidMac;

	@Value("${fence.invalid.imsi}")
	private String invalidImsi;

	@Value("${fence.invalid.imei}")
	private String invalidImei;

	@Value("${jdctx.fence.table}")
	private String jdctxFenceTable;
	


	/**
	 * 电子围栏impala 表名
	 */
	@Value("${fence.impala.table.name}")
	private String fenceImpalaTableName;
	
	/**
	 * wifi围栏impala 表名
	 */
	@Value("${wifi.impala.table.name}")
	private String wifiImpalaTableName;
	
	/**
	 * 人脸impala 表名
	 */
	@Value("${face.impala.table.name}")
	private String faceImpalaTableName;
	
	

	public String getJdctxFenceTable() {
		return jdctxFenceTable;
	}

	public String getBtsImpalaTableName() {
		return btsImpalaTableName;
	}

	/**
	 * @Description : 获取impala表名
	 * @return
	 */
	public String getJdctxImpalaTableName() {
		return jdctxImpalaTableName;
	}

	/**
	 * @Description : 获取不同车牌仓库表
	 * @return
	 */
	public String getDiffCarTableName() {
		return diffCarTableName;
	}

	/**
	 * @Description : 获取初次入城仓库表
	 * @return
	 */
	public String getCcrcTableName() {
		return ccrcTableName;
	}

	/**
	 * @Description : 获取无效号牌号码(例如 '','-')
	 * @return
	 */
	public String getInvalidHphm() {
		return invalidHphm;
	}
	
	public String getInvalidMac() {
		return invalidMac;
	}

	public String getInvalidImsi() {
		return invalidImsi;
	}

	public String getInvalidImei() {
		return invalidImei;
	}

	public String getFenceImpalaTableName() {
		return fenceImpalaTableName;
	}

	public String getWifiImpalaTableName() {
		return wifiImpalaTableName;
	}

	public String getFaceImpalaTableName() {
		return faceImpalaTableName;
	}

}