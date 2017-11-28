package com.fhzz.core.tools;

import java.util.Random;

/**
 *@FileName : (GenerateRandomStr.java) 
 *
 *@description : 生成指定位数的随机数
 *@author : Cluo 
 *@version : Version No.1
 *@create : 2015-5-25 下午04:34:18
 *@modify : 2015-5-25 下午04:34:18
 *@copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
public class GenerateRandom 
{
	/**
	 * @Description : 生成位数为6的随机数字符串
	 * @return
	 */
	public static String generateRandomStr_6()
	{
		return generateRandomString(6);
	}

	/**
	 * @Description : 生成位数为32的随机数字符串
	 * @return
	 */
	public static String generateRandomStr_32() 
	{
		return generateRandomString(32);
	}
	
	/**
	 * @Description : 生成位数为randStrLength的随机数(字符串)
	 * @param randStrLength
	 * @return
	 */
	public static String generateRandomString(int randStrLength) 
	{
		StringBuffer generateRandStr = new StringBuffer();
		Random rand = new Random();
		int randNum = rand.nextInt(9) + 1;
		
		generateRandStr.append(randNum);
		
		for (int i = 1; i < randStrLength; i++) 
		{
			randNum = rand.nextInt(10);
			generateRandStr.append(randNum);
		}
		
		return generateRandStr.toString();
	}
	
	/**
	 * @Description : 生成位数为randStrLength的随机数
	 * @param randStrLength
	 * @return
	 */
	public static Long generateRandomStr(int randStrLength) 
	{
		StringBuffer generateRandStr = new StringBuffer();
		String psid;
		Random rand = new Random();
		int randNum = rand.nextInt(9) + 1;
		
		generateRandStr.append(randNum);
		
		for (int i = 1; i < randStrLength; i++) 
		{
			randNum = rand.nextInt(10);
			generateRandStr.append(randNum);
		}
		
		psid = generateRandStr.toString();
		
		return new Long(psid);
	}
}
