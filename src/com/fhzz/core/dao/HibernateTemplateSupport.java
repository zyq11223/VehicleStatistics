package com.fhzz.core.dao;

import com.fhzz.core.tools.StreamUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.HibernateTemplate;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * @FileName : (HibernateTemplateSupport.java)
 * 
 * @description : TODO(这里用一句话描述这个类的作用)
 * @author : xx
 * @version : Version No.1
 * @create : 2015-6-1 下午01:27:32
 * @modify : 2015-6-1 下午01:27:32
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
public class HibernateTemplateSupport extends HibernateTemplate implements Externalizable {

	private SessionFactory sessionFactory;
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Hibernate Session 获取
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	public Session getOtherSession() {
		return sessionFactory.openSession();
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		byte[] strBytes  = StreamUtil.ObjectToByte(sessionFactory);
		int length = strBytes.length;
		out.writeInt(length);
		out.write(strBytes);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		int length = in.readInt();
		byte[] strBytes = new byte[length];
		in.readFully(strBytes);
		this.sessionFactory = (SessionFactory) StreamUtil.ByteToObject(strBytes);
	}
}
