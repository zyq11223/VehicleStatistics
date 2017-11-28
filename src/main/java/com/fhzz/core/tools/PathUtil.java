package com.fhzz.core.tools;

import org.springframework.util.Assert;


/**
 * @FileName : (PathUtil.java)
 *
 * @description : 路径工具类
 * @author : Zhihao.Du
 * @version : Version No.1
 * @create : 2016年10月9日 上午11:34:48
 * @modify : 2016年10月9日 上午11:34:48
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
public class PathUtil {

	/**
	 * @Description : 获取绝对路径
	 * @param resoucePath 资源路径
	 * @return
	 */
	public static String getAsolutePath(String resoucePath) {
		Assert.notNull(resoucePath, "资源路径不能为空");
		return PathUtil.class.getResource(resoucePath).getPath();
	}

}
