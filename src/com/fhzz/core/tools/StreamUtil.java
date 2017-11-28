package com.fhzz.core.tools;

import java.io.*;

import org.apache.log4j.Logger;


/**
 * 流工具
 * @description: 流转换工具
 * @author yanqisong
 *
 */
public class StreamUtil
{

	private static final Logger LOG = Logger.getLogger(StreamUtil.class);


	/**
	 * 将对像转换为字节
     */
	public static byte[] ObjectToByte(Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			LOG.error("translation" + e.getMessage());
			e.printStackTrace();
		}
		return bytes;
	}


	/**
	 * 将字节转换为对象
     */
	public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			LOG.error("translation" + e.getMessage());
			throw new RuntimeException("ByteArrayOutputStream ByteToObject fail: "+e.getMessage(), e);
		}
		return obj;
	}




	/**
	 * 字节数组长度
	 */
	private static int  BYTE_SIZE = 1024;
	
	 /**
     * 从输入流获取数据
     * @param inputStream
     * @return
     * @throws Exception
     */
	public static byte[] readInputStream(InputStream inputStream)
	{
		ByteArrayOutputStream outputStream  = null;
		try
		{
			if(inputStream == null)
			{
				throw new IllegalArgumentException("inputStream can not be null!");
			}
			
			byte[] buffer = new byte[BYTE_SIZE];
			int len = -1;
			outputStream = new ByteArrayOutputStream();
			while ((len = inputStream.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, len);
			}
		}
		catch (IOException e)
		{
			LOG.error("ByteArrayOutputStream close Bytes Stream fail: "+e.getMessage(), e);
			throw new RuntimeException("ByteArrayOutputStream close Bytes Stream fail: "+e.getMessage(), e);
		}
		finally
		{
			try
			{
				if(outputStream != null)
				{
					outputStream.close();
				}
			}
			catch (IOException e)
			{
				LOG.error("close Bytes Stream fail: "+e.getMessage(), e);
				throw new RuntimeException("close Bytes Stream fail: "+e.getMessage(), e);
			}
		}
		return outputStream.toByteArray();
	}
}
