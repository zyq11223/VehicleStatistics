package business.tools;

import java.util.Arrays;
import java.util.List;

import com.fhzz.core.tools.StringUtils;

/**
 * @FileName : (ListUtil.java)
 *
 * @description : 集合工具类
 * @author : Zhihao.Du
 * @version : Version No.1
 * @create : 2016年8月26日 上午10:17:31
 * @modify : 2016年8月26日 上午10:17:31
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
public class ListUtil {

	/**
	 * @Description : 获取list字符串
	 * @param list
	 * @param split
	 *            分隔符
	 * @param qz
	 *            前缀
	 * @param hz
	 *            后缀
	 * @return
	 */
	public static String getListString(List<String> list, String split,
			String qz, String hz) {
		if (list == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (String string : list) {
			sb.append(qz + string + hz + split);
		}
		String result = sb.toString();
		if (result.length() > 0)
			result = result.substring(0, result.length() - split.length());
		return result;

	}

	/**
	 * @Description : 获取list字符串(，号分割 '' 前缀后缀)
	 * @param list
	 * @return
	 */
	public static String getListString(List<String> list) {
		return getListString(list, ",", "'", "'");

	}

	/**
	 * @Description : 转换成list（排除空情况）
	 * @param data
	 * @param split
	 * @return
	 */
	public static List<String> asList(String data, String split) {
		if (StringUtils.isNullOREmpty(data))
			return null;
		String[] dataArr = data.split(split);
		
		return Arrays.asList(dataArr);
	}

	/**
	 * @Description : 转换成list（排除空情况）
	 * @param data
	 * @return
	 */
	public static List<String> asList(String data) {
		return asList(data, ",");
	}

}
