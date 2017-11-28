package com.fhzz.core.kafka;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.sf.json.JSONObject;

/**
 * @FileName : (JdctxConstants.java)
 *
 * @description :
 * @author : Zhihao.Du
 * @version : Version No.1
 * @create : 2016年7月8日 上午10:13:20
 * @modify : 2016年7月8日 上午10:13:20
 * @copyright : FiberHome FHZ Telecommunication Technologies Co.Ltd.
 */
public class JdctxConstants
{
	
	public JDCTXModel create(){
		int seed = 1000;// 1秒一个种子
		int days = 90;// 90天数据分布
		return create(seed, days);
	}

	public JDCTXModel create(int seed, int days)
	{
		JDCTXModel jdctxModel = new JDCTXModel();

		// 车辆信息编号
		jdctxModel.setClxxbhModel(new CLXXBHModel());
		// 表名
		jdctxModel.setTableNameModel(new TABLE_NAMEModel());
		// 车牌号
		jdctxModel.setHphmModel(new HPHMModel(seed));
		// 车辆类型
		jdctxModel.setCllxModel(new CLLXModel(seed));
		// 车辆品牌
		jdctxModel.setClppModel(new CLPPModel(seed));
		// 卡口编号
		jdctxModel.setKkbhModel(new KKBHModel(seed));
		// PUID
		jdctxModel.setPuidModel(new PUIDModel(seed));
		// IVU的ID
		jdctxModel.setIvuidModel(new IVUIDModel(seed));
		// 方向编号
		jdctxModel.setFxbhModel(new FXBHModel());
		// 抓拍类型
		jdctxModel.setZplxModel(new ZPLXModel());
		// 数据描述
		jdctxModel.setSjmsModel(new SJMSModel());
		// 车道编号
		jdctxModel.setCdbhModel(new CDBHModel(seed));
		// 行驶方向 0
		jdctxModel.setXsfxModel(new XSFXModel(seed));
		// 车牌颜色
		jdctxModel.setHpysModel(new HPYSModel(seed));
		// 车牌颜色
		jdctxModel.setClsdModel(new CLSDModel(seed));
		// 车身颜色
		jdctxModel.setCsysModel(new CSYSModel(seed));
		// 车牌种类
		jdctxModel.setHpzlModel(new HPZLModel(seed));
		// 行驶状态
		jdctxModel.setXsztModel(new XSZTModel());
		// 经过时刻
		jdctxModel.setJgskModel(new JGSKModel(seed, days));
		// 图像数量
		jdctxModel.setTxslModel(new TXSLModel());
		// 图像名称
		jdctxModel.setTxmc1Model(new TXMC1Model(jdctxModel.getPuidModel(),
				seed, "http://10.0.92.250:88//mnt/ivcspic/zc/"));

		return jdctxModel;
	}

	public class JDCTXModel
	{
		private CLXXBHModel clxxbhModel;
		private TABLE_NAMEModel tableNameModel;
		private HPHMModel hphmModel;
		private CLLXModel cllxModel;
		private CLPPModel clppModel;
		private KKBHModel kkbhModel;
		private PUIDModel puidModel;
		private IVUIDModel ivuidModel;
		private FXBHModel fxbhModel;
		private ZPLXModel zplxModel;
		private SJMSModel sjmsModel;
		private CDBHModel cdbhModel;
		private XSFXModel xsfxModel;
		private HPYSModel hpysModel;
		private CLSDModel clsdModel;
		private CSYSModel csysModel;
		private HPZLModel hpzlModel;
		private XSZTModel xsztModel;
		private JGSKModel jgskModel;
		private TXSLModel txslModel;
		private TXMC1Model txmc1Model;
		private TXMC2Model txmc2Model;
		private TXMC3Model txmc3Model;
		private TXMC4Model txmc4Model;

		/**
		 * @Description : 车辆信息编号
		 * @return
		 */
		public CLXXBHModel getClxxbhModel()
		{
			return clxxbhModel;
		}

		public void setClxxbhModel(CLXXBHModel clxxbhModel)
		{
			this.clxxbhModel = clxxbhModel;
		}

		public TABLE_NAMEModel getTableNameModel()
		{
			return tableNameModel;
		}

		public void setTableNameModel(TABLE_NAMEModel tableNameModel)
		{
			this.tableNameModel = tableNameModel;
		}

		public HPHMModel getHphmModel()
		{
			return hphmModel;
		}

		public void setHphmModel(HPHMModel hphmModel)
		{
			this.hphmModel = hphmModel;
		}

		public CLLXModel getCllxModel()
		{
			return cllxModel;
		}

		public void setCllxModel(CLLXModel cllxModel)
		{
			this.cllxModel = cllxModel;
		}

		public CLPPModel getClppModel()
		{
			return clppModel;
		}

		public void setClppModel(CLPPModel clppModel)
		{
			this.clppModel = clppModel;
		}

		public KKBHModel getKkbhModel()
		{
			return kkbhModel;
		}

		public void setKkbhModel(KKBHModel kkbhModel)
		{
			this.kkbhModel = kkbhModel;
		}

		public PUIDModel getPuidModel()
		{
			return puidModel;
		}

		public void setPuidModel(PUIDModel puidModel)
		{
			this.puidModel = puidModel;
		}

		public IVUIDModel getIvuidModel()
		{
			return ivuidModel;
		}

		public void setIvuidModel(IVUIDModel ivuidModel)
		{
			this.ivuidModel = ivuidModel;
		}

		public FXBHModel getFxbhModel()
		{
			return fxbhModel;
		}

		public void setFxbhModel(FXBHModel fxbhModel)
		{
			this.fxbhModel = fxbhModel;
		}

		public ZPLXModel getZplxModel()
		{
			return zplxModel;
		}

		public void setZplxModel(ZPLXModel zplxModel)
		{
			this.zplxModel = zplxModel;
		}

		public SJMSModel getSjmsModel()
		{
			return sjmsModel;
		}

		public void setSjmsModel(SJMSModel sjmsModel)
		{
			this.sjmsModel = sjmsModel;
		}

		public CDBHModel getCdbhModel()
		{
			return cdbhModel;
		}

		public void setCdbhModel(CDBHModel cdbhModel)
		{
			this.cdbhModel = cdbhModel;
		}

		public XSFXModel getXsfxModel()
		{
			return xsfxModel;
		}

		public void setXsfxModel(XSFXModel xsfxModel)
		{
			this.xsfxModel = xsfxModel;
		}

		public HPYSModel getHpysModel()
		{
			return hpysModel;
		}

		public void setHpysModel(HPYSModel hpysModel)
		{
			this.hpysModel = hpysModel;
		}

		public CLSDModel getClsdModel()
		{
			return clsdModel;
		}

		public void setClsdModel(CLSDModel clsdModel)
		{
			this.clsdModel = clsdModel;
		}

		public CSYSModel getCsysModel()
		{
			return csysModel;
		}

		public void setCsysModel(CSYSModel csysModel)
		{
			this.csysModel = csysModel;
		}

		public HPZLModel getHpzlModel()
		{
			return hpzlModel;
		}

		public void setHpzlModel(HPZLModel hpzlModel)
		{
			this.hpzlModel = hpzlModel;
		}

		public XSZTModel getXsztModel()
		{
			return xsztModel;
		}

		public void setXsztModel(XSZTModel xsztModel)
		{
			this.xsztModel = xsztModel;
		}

		public JGSKModel getJgskModel()
		{
			return jgskModel;
		}

		public void setJgskModel(JGSKModel jgskModel)
		{
			this.jgskModel = jgskModel;
		}

		public TXSLModel getTxslModel()
		{
			return txslModel;
		}

		public void setTxslModel(TXSLModel txslModel)
		{
			this.txslModel = txslModel;
		}

		public TXMC1Model getTxmc1Model()
		{
			return txmc1Model;
		}

		public void setTxmc1Model(TXMC1Model txmc1Model)
		{
			this.txmc1Model = txmc1Model;
		}

		public TXMC2Model getTxmc2Model()
		{
			return txmc2Model;
		}

		public void setTxmc2Model(TXMC2Model txmc2Model)
		{
			this.txmc2Model = txmc2Model;
		}

		public TXMC3Model getTxmc3Model()
		{
			return txmc3Model;
		}

		public void setTxmc3Model(TXMC3Model txmc3Model)
		{
			this.txmc3Model = txmc3Model;
		}

		public TXMC4Model getTxmc4Model()
		{
			return txmc4Model;
		}

		public void setTxmc4Model(TXMC4Model txmc4Model)
		{
			this.txmc4Model = txmc4Model;
		}

	}

	// JDCTX的model基类
	public abstract class JDCTXBaseModel
	{
		protected int seed = 0;

		public JDCTXBaseModel()
		{

		}

		public JDCTXBaseModel(int seed)
		{
			this.seed = seed;
		}
	}

	// 车辆信息编号
	public class CLXXBHModel extends JDCTXBaseModel
	{
		private String clxxbh = "";

		public String getClxxbh()
		{
			clxxbh = UUID.randomUUID().toString().replaceAll("-", "")
					.toUpperCase();
			return clxxbh;
		}

	}

	// 表名
	public class TABLE_NAMEModel extends JDCTXBaseModel
	{
		private String tableName = "jdctx";

		public String getTableName()
		{
			return tableName;
		}

	}

	// 经过时刻
	public class JGSKModel extends JDCTXBaseModel
	{
		private Date jgsk;
		private String jgday = "";
		private int days;

		public String getJgsk()
		{
			Calendar calendar = Calendar.getInstance();
			int randomNum = ConstantsUtil.getNumber(days, seed);

			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH, -randomNum);
			calendar.add(Calendar.HOUR_OF_DAY,
					ConstantsUtil.getNumber(24, seed));
			calendar.set(Calendar.MINUTE, ConstantsUtil.getNumber(60, seed * 1));
			calendar.set(Calendar.SECOND, ConstantsUtil.getNumber(60, seed * 7));
			

			jgsk = calendar.getTime();

			// 设置jgday
			jgday = ConstantsUtil.getDateFormat(jgsk,ConstantsUtil.sdfday);
			
			
			String str =  ConstantsUtil.getDateFormat(jgsk, ConstantsUtil.sdffullTime);

			return str;
		}
		
		public Date getJgskDate() {
			return jgsk;
		}

		// 经过时刻(yyyy-MM-dd)
		public String getJgday()
		{
			return jgday;
		}

		public JGSKModel(int seed, int days)
		{
			super(seed);

			this.days = days;

		}

	}

	// 车牌号
	public class HPHMModel extends JDCTXBaseModel
	{
		private String hphm = "";
		private String hphm1 = "";
		private String hphm2 = "";
		// 市区的车牌号前缀
		private String[] areas = { "鄂A", "鄂A", "鄂A", "鄂A", "鄂A", "鄂A", "鄂A",
				"鄂B", "鄂C", "鄂D", "鄂E", "鄂F", "鄂G", "鄂H", "鄂I", "鄂J", "鄂K",
				"湘A", "黑A", "京A", "沪A", "赣B", "苏A", "粤A", "湘A", "蒙A" };
		private String[] nos = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
				"9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
				"l", "m", "n", "o", "p", "q", "s", "t", "r", "u", "v", "w",
				"x", "v", "z" };

		public String getHphm()
		{
			hphm1 = areas[ConstantsUtil.getNumber(areas.length, seed)];
			hphm2 = nos[ConstantsUtil.getNumber(36, 1 + seed)]
					+ nos[ConstantsUtil.getNumber(36, 2 + seed)]
					+ nos[ConstantsUtil.getNumber(10, 3 + seed)]
					+ nos[ConstantsUtil.getNumber(10, 4 + seed)]
					+ nos[ConstantsUtil.getNumber(10, 5 + seed)];

			hphm = hphm1 + hphm2;

			return hphm;
		}

		public String getHphm1()
		{
			return hphm1;
		}

		public String getHphm2()
		{
			return hphm2;
		}

		public HPHMModel(int seed)
		{
			super(seed);
		}

	}

	// 车辆类型
	public class CLLXModel extends JDCTXBaseModel
	{

		private String[] cllxArr = { "K11", "K21", "K31", "K32", "K33", "K38",
				"K39", "K41", "H11", "H21", "H31", "H35", "H41", "M21" };

		private String clxx = "";

		public CLLXModel(int seed)
		{
			super(seed);
		}

		public String[] getCllxArr()
		{
			return cllxArr;
		}

		public String getClxx()
		{
			clxx = cllxArr[ConstantsUtil.getNumber(cllxArr.length, seed)];
			return clxx;
		}

	}

	// 车辆品牌
	public class CLPPModel extends JDCTXBaseModel
	{

		private String[] clppArr = { "001", "002", "003", "004", "005", "006",
				"007", "008", "009", "010", "011", "012", "013", "014", "015",
				"016", "017", "018", "019", "020", "021", "022", "023", "024" };

		private String clpp = "";

		public String[] getClppArr()
		{
			return clppArr;
		}

		public String getClpp()
		{
			clpp = clppArr[ConstantsUtil.getNumber(clppArr.length, seed)];
			return clpp;
		}

		public CLPPModel(int seed)
		{

			super(seed);
			// TODO Auto-generated constructor stub
		}

	}

	// 卡口编号
	public class KKBHModel extends JDCTXBaseModel
	{
		private String[] kkbhArr = null;
		private String kkbh = "";

		public String[] getKkbhArr()
		{
			return kkbhArr;
		}

		public String getKkbh()
		{
			kkbh = kkbhArr[ConstantsUtil.getNumber(kkbhArr.length, seed)];
			return kkbh;
		}

		public KKBHModel(int count, int wei, int seed)
		{
			super(seed);
			kkbhArr = ConstantsUtil.createContinueKey("420000006888", 1, count,
					wei);
		}

		public KKBHModel(int seed)
		{
			this(20, 3, seed);
		}

	}

	// PU的ID
	public class PUIDModel extends JDCTXBaseModel
	{
		private String[] puidArr = null;
		private String puid = "";

		public String[] getPuidArr()
		{
			return puidArr;
		}

		public String getPuid()
		{
			puid = puidArr[ConstantsUtil.getNumber(puidArr.length, seed)];
			return puid;
		}

		public PUIDModel(int count, int wei, int seed)
		{
			super(seed);
			puidArr = ConstantsUtil.createContinueKey("420100500", 1, count,
					wei);

		}

		public PUIDModel(int seed)
		{
			this(20, 3, seed);
		}

	}

	// IVU的ID
	public class IVUIDModel extends JDCTXBaseModel
	{
		private String[] kkbhArr = null;
		private String kkbh = "";

		public String getKkbh()
		{
			kkbh = kkbhArr[ConstantsUtil.getNumber(kkbhArr.length, seed)];
			return kkbh;
		}

		public String[] getKkbhArr()
		{
			return kkbhArr;
		}

		public IVUIDModel(int count, int wei, int seed)
		{
			super(seed);
			kkbhArr = ConstantsUtil.createContinueKey("42011100002371934", 5,
					count, wei);

		}

		public IVUIDModel(int seed)
		{
			this(20, 3, seed);
		}

	}

	// 方向编号
	public class FXBHModel extends JDCTXBaseModel
	{
		private String[] fxbhArr = { "" };

		private String fxbh = "";

		public String[] getFxbhArr()
		{
			return fxbhArr;
		}

		public String getFxbh()
		{
			return fxbh;
		}

		public FXBHModel()
		{
			fxbh = fxbhArr[0];
		}

	}

	// 抓拍类型
	public class ZPLXModel extends JDCTXBaseModel
	{
		private String[] zplxArr = { "" };
		private String zplx = "";

		public String[] getZplxArr()
		{
			return zplxArr;
		}

		public String getZplx()
		{
			return zplx;
		}

		public ZPLXModel()
		{
			zplx = zplxArr[0];

		}

	}

	// 数据描述
	public class SJMSModel extends JDCTXBaseModel
	{
		private String[] sjmsArr = { "" };
		private String sjms = "";

		public String[] getSjmsArr()
		{
			return sjmsArr;
		}

		public String getSjms()
		{
			return sjms;
		}

		public SJMSModel()
		{
			sjms = sjmsArr[0];
		}

	}

	// 车道编号
	public class CDBHModel extends JDCTXBaseModel
	{
		private String[] cdbhArr = null;
		private String cdbh = "";

		public String[] getCdbhArr()
		{
			return cdbhArr;
		}

		public String getCdbh()
		{
			cdbh = cdbhArr[ConstantsUtil.getNumber(cdbhArr.length, seed)];
			return cdbh;
		}

		public CDBHModel(int seed)
		{
			super(seed);
			cdbhArr = ConstantsUtil.createContinueKey("", 0, 5, 2);
		}

	}

	// 行驶方向 0
	public class XSFXModel extends JDCTXBaseModel
	{
		private String[] xsfxArr = { "0", "1" };
		private String xsfx = "";

		public String[] getXsfxArr()
		{
			return xsfxArr;
		}

		public String getXsfx()
		{
			xsfx = xsfxArr[ConstantsUtil.getNumber(xsfxArr.length, seed)];
			return xsfx;
		}

		public XSFXModel(int seed)
		{

			super(seed);
			// TODO Auto-generated constructor stub
		}

	}

	// 车牌颜色
	public class HPYSModel extends JDCTXBaseModel
	{
		private String[] hpysArr = { "0", "1", "2", "3", "4" };
		private String hpys = "";

		public String[] getHpysArr()
		{
			return hpysArr;
		}

		public String getHpys()
		{
			hpys = hpysArr[ConstantsUtil.getNumber(hpysArr.length, seed)];
			return hpys;
		}

		public HPYSModel(int seed)
		{

			super(seed);
			// TODO Auto-generated constructor stub
		}

	}

	// 车辆速度
	public class CLSDModel extends JDCTXBaseModel
	{
		private Number[] clsdArr;
		private Number clsd = 0;

		public String getClsd()
		{
			clsd = clsdArr[ConstantsUtil.getNumber(clsdArr.length, seed)];
			return clsd.toString();
		}

		public CLSDModel(int seed)
		{
			super(seed);

			// 生成车辆速度 100个样本
			int count = 100;
			clsdArr = new Number[count];
			for (int i = 0; i < count; i++)
			{
				int number = ConstantsUtil.getNumber(count, seed);
				clsdArr[i] = number;
			}

		}

	}

	// 车身颜色
	public class CSYSModel extends JDCTXBaseModel
	{
		private String[] csysArr = { "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "Z" };
		private String csys = "";

		public String[] getCsysArr()
		{
			return csysArr;
		}

		public String getCsys()
		{
			csys = csysArr[ConstantsUtil.getNumber(csysArr.length, seed)];
			return csys;
		}

		public CSYSModel(int seed)
		{

			super(seed);
			// TODO Auto-generated constructor stub
		}

	}

	// 车牌种类
	public class HPZLModel extends JDCTXBaseModel
	{
		private String[] hpzlArr = { "01", "02", "03", "04", "05", "06", "07",
				"08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
				"23", "24", "99" };
		private String hpzl = "";

		public String[] getHpzlArr()
		{
			return hpzlArr;
		}

		public String getHpzl()
		{
			hpzl = hpzlArr[ConstantsUtil.getNumber(hpzlArr.length, seed)];
			return hpzl;
		}

		public HPZLModel(int seed)
		{

			super(seed);
			// TODO Auto-generated constructor stub
		}

	}

	// 行驶状态
	public class XSZTModel extends JDCTXBaseModel
	{
		private String[] xsztArr = { "1301", "1603", "1101", "1302", "1208",
				"1042", "1019", "9999", "4646", "4308", "8888", "1300" };
		private String xszt = "";

		public String[] getXsztArr()
		{
			return xsztArr;
		}

		public String getXszt()
		{
			xszt = xsztArr[ConstantsUtil.getNumber(xsztArr.length, seed)];
			return xszt;
		}

	}

	// 图像数量
	public class TXSLModel extends JDCTXBaseModel
	{
		private String[] txslArr = { "1" };
		private String txsl = "";

		public String[] getTxslArr()
		{
			return txslArr;
		}

		public String getTxsl()
		{
			return txsl;
		}

		public TXSLModel()
		{
			super();
			txsl = txslArr[0];
		}

	}

	// 图像名称
	public class TXMC1Model extends JDCTXBaseModel
	{

		private String txmc1 = "";
		private PUIDModel puid;
		private String qz = "";

		// 动态生成
		// "http://10.0.92.250:88//mnt/ivcspic/zc/420000140379/20160408/09/3644543-420000006825437-2-1.jpg",

		public String getTxmc1()
		{

			txmc1 = qz + "/" + puid.getPuid()
					+ "/20141104/18/4545615-420155696415804-2-1.jpg";
			return txmc1;
		}

		public TXMC1Model(PUIDModel puid, int seed, String qz)
		{
			super(seed);
			this.puid = puid;
			this.qz = qz;
		}
	}

	// 图像名称
	public class TXMC2Model
	{
		private String[] txmc2 = { "" };

		public String[] getTxmc2()
		{
			return txmc2;
		}

	}

	// 图像名称
	public class TXMC3Model
	{
		private String[] txmc3 = { "" };

		public String[] getTxmc3()
		{
			return txmc3;
		}

	}

	// 图像名称
	public class TXMC4Model
	{
		private String[] txmc4 = { "" };

		public String[] getTxmc4()
		{
			return txmc4;
		}

	}

	public static class ConstantsUtil
	{

		public static SimpleDateFormat sdf = new SimpleDateFormat();

		// 完整时间格式化
		public static String sdffullTime = "yyyy-MM-dd HH:mm:ss";
		// Day时间格式化
		public static String sdfday = "yyyy-MM-dd";

		public static synchronized String getDateFormat(Date date,
				String pattern) {
			synchronized (sdf) {
				String str = null;
				sdf.applyPattern(pattern);
				str = sdf.format(date);
				return str;
			}
		}

		/**
		 * @Description : 生成连续key
		 * @param qz
		 *            前缀
		 * @param begin
		 *            开始数字
		 * @param count
		 *            个数
		 * @return
		 */
		public static String[] createContinueKey(String qz, int begin,
				int count, int wei)
		{
			String[] resultArr = new String[count];

			while (count > 0)
			{
				Integer temp = begin + count - 1;
				String tempString = temp.toString();
				while (tempString.length() < wei)
					tempString = "0" + tempString;
				resultArr[count - 1] = qz + tempString;
				count--;
			}

			return resultArr;
		}

		public static String[] createContinueKey(String qz, int begin, int count)
		{
			String[] resultArr = new String[count];

			while (count > 0)
			{
				Integer temp = begin + count - 1;
				String tempString = temp.toString();

				resultArr[count - 1] = qz + tempString;
				count--;
			}

			return resultArr;
		}

		public static int getNumber(int num, int seed)
		{
			Random rdm = new Random(System.currentTimeMillis() * seed);

			return Math.abs(rdm.nextInt()) % num;
		}

	}
	
	
	@SuppressWarnings("unused")
	public static String createMsg()
	{

		JdctxConstants jdctxConstants = new JdctxConstants();
		JDCTXModel jdctxModel = jdctxConstants.create();

		String clxxbh = jdctxModel.getClxxbhModel().getClxxbh();
		String tableName = jdctxModel.getTableNameModel().getTableName();
		String kkbh = jdctxModel.getKkbhModel().getKkbh();
		String puid = jdctxModel.getPuidModel().getPuid();
		String cdbh = jdctxModel.getCdbhModel().getCdbh();
		String xsfx = jdctxModel.getXsfxModel().getXsfx();
		String jgsk = jdctxModel.getJgskModel().getJgsk();
		String jgday = jdctxModel.getJgskModel().getJgday();
		String hphm = jdctxModel.getHphmModel().getHphm();
		String hphm1 = jdctxModel.getHphmModel().getHphm1();
		String hphm2 = jdctxModel.getHphmModel().getHphm2();
		String hpys = jdctxModel.getHpysModel().getHpys();
		String csys = jdctxModel.getCsysModel().getCsys();
		String clxx = jdctxModel.getCllxModel().getClxx();
		String hpzl = jdctxModel.getHpzlModel().getHpzl();
		String xszt = jdctxModel.getXsztModel().getXszt();
		String clsd = jdctxModel.getClsdModel().getClsd();
		String txmc1 = jdctxModel.getTxmc1Model().getTxmc1();

		Map<String, String> map = new HashMap<String, String>();
		map.put("CLSD", clsd);
		map.put("TXMC1", txmc1);
	    map.put("HPZL", hpzl);
		map.put("XSFX", xsfx);
		map.put("CLXXBH", clxxbh);
		map.put("JGDAY", jgday);
	    map.put("PUID", puid);
		map.put("JGSK", jgsk);
		map.put("CDBH", cdbh);
		map.put("HPHM", hphm);
		map.put("ROWKEY", clxxbh);
		map.put("KKBH", kkbh);
	    map.put("CSYS", csys);
	    map.put("HPYS", hpys);
		map.put("CLLX", clxx);
		map.put("XSZT", xszt);
		map.put("TXSL", "");
	    map.put("TXMC2", "");
		map.put("TXMC3", "");
		map.put("TXMC4", "");

		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
	}
	
	public static void main(String[] args)
	{
		System.out.println(JdctxConstants.createMsg());
	}

}
