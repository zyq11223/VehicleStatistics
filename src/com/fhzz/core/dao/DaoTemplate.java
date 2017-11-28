package com.fhzz.core.dao;

import com.fhzz.core.tools.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @FileName : (DaoTemplate.java)
 * 
 * @description : 数据操作模板统一接口
 * @author : mrlans 
 * @version : Version No.1
 * @create : 2015-5-19 下午03:08:42
 * @modify : 2015-5-19 下午03:08:42
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
@Service("daoTemplate")
public class DaoTemplate implements Externalizable {
//	/**
//	 * 关系数据库（master Mysql）数据库JDBC操作
//	 */
//	@Autowired
//	private JdbcTemplate mysqlJdbcTemplate;

	/**
	 * 关系数据库（Master mysql）数据库Hibernate操作
	 */
	@Autowired
	private HibernateTemplateSupport mysqlHibernateTemplate;

	public HibernateTemplateSupport getMysqlHibernateTemplate() {
		return mysqlHibernateTemplate;
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		byte[] strBytes  = StreamUtil.ObjectToByte(mysqlHibernateTemplate);
		int length = strBytes.length;
		out.writeInt(length);
		out.write(strBytes);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int length = in.readInt();
		byte[] strBytes = new byte[length];
		in.readFully(strBytes);
		this.mysqlHibernateTemplate = (HibernateTemplateSupport) StreamUtil.ByteToObject(strBytes);
	}
}
