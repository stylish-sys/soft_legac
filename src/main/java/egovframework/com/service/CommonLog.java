package egovframework.com.service;

import org.apache.log4j.Logger;

public class CommonLog {
	
	final static Logger log = Logger.getLogger(CommonUtil.class);
	
	public static void debug(Exception e, @SuppressWarnings("rawtypes") Class c, String methodNm){
		log.debug("nforu_"+c.getName()+"["+methodNm+"] "+e.getClass().getName());
	}
	
	
}
