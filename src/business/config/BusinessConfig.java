package business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 * @FileName : (BunessConfig.java)
 *
 * @description : 业务配置
 * @author : Zhihao.Du
 * @version : Version No.1
 * @create : 2016年9月7日 下午6:33:29
 * @modify : 2016年9月7日 下午6:33:29
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
@Repository("businessConfig")
public class BusinessConfig {

	@Value("${local_cp}")
	private String local_cp;

	@Value("${tpcfx}")
	private String tpcfx;

	@Value("${people.flow.statistics}")
	private String peopleFlowStatistics;

	@Value("${dispatched.table}")
	private String dispatched;

	@Value("${dispatched.jdctx.table}")
	private String dispatchedJdctx;

	@Value("${dispatched.fence.table}")
	private String dispatchedFence;

	@Value("${dispatched.wifi.table}")
	private String dispatchedWifi;

	/**
	 * @Description : 获取本地车牌
	 * @return
	 */
	public String getLocal_cp() {
		return local_cp;
	}

	/**
	 * @Description : 获取Mysql上的TPCFX表
	 * @return
	 */
	public String getTpcfx() {
		return tpcfx;
	}

	/**
	 * @Description : 获取Mysql上的布控表
	 * @return
	 */
	public String getDispatched() {
		return dispatched;
	}

	/**
	 * @Description : 获取Mysql上Jdctx的布控表
	 * @return
	 */
	public String getDispatchedJdctx() {
		return dispatchedJdctx;
	}

	/**
	 * @Description : 获取Mysql上Fence的布控表
	 * @return
	 */
	public String getDispatchedFence() {
		return dispatchedFence;
	}

	/**
	 * @Description : 获取Mysql上Wifi的布控表
	 * @return
	 */
	public String getDispatchedWifi() {
		return dispatchedWifi;
	}

	/**
	 * @Description : 获取Mysql上人流量统计表
	 * @return
	 */
	public String getPeopleFlowStatistics() {
		return peopleFlowStatistics;
	}

}
