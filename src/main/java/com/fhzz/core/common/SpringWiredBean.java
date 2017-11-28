package com.fhzz.core.common;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * @FileName : (SpringWiredBean.java)
 *
 * @description :Spring容器工具类
 * @author : Zhihao.Du
 * @version : Version No.1
 * @create : 2016年11月28日 下午5:28:19
 * @modify : 2016年11月28日 下午5:28:19
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
public class SpringWiredBean extends SpringBeanAutowiringSupport {

	/**
	 * 自动装配注解会让Spring通过类型匹配为beanFactory注入示例
	 */
	@Autowired
	private BeanFactory beanFactory;

	private SpringWiredBean() {
	}

	private static SpringWiredBean instance;

	static {
		// 静态块，初始化实例
		instance = new SpringWiredBean();
	}

	/**
	 * 实例方法 使用的时候先通过getInstance方法获取实例
	 * 
	 * @param beanId
	 * @return
	 */
	public Object getBeanById(String beanId) {
		return beanFactory.getBean(beanId);
	}

	/**
	 * @Description : 获取实例
	 * @return
	 */
	public static SpringWiredBean getInstance() {
		return instance;
	}

}
