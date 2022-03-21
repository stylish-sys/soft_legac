package egovframework.com.service;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CommonUtil {
	
	final static Logger log = Logger.getLogger(CommonUtil.class);
	
	final static String returnPath = "nfu/la/";

	/**
	 * 메인레이아웃
	 * @param sysID
	 * @return
	 */
	public static String getMainLayoutPathBySysID(String sysID){
		return returnPath+sysID+"/mainLayout";
	}
	
	/**
	 * 서브레이아웃
	 * @param sysID
	 * @return
	 */
	public static String getSubLayoutPathBySysID(String sysID){
		return returnPath+sysID+"/subLayout";
	}
	
	/**
	 * 솔트값 생성
	 * @return
	 */
	public static String getSalt() {
		Random random = new Random();     
		byte[] salt = new byte[10];

		random.nextBytes(salt);     

		StringBuffer sb = new StringBuffer();

		for(int i=0; i<salt.length; i++) {
			sb.append(String.format("%02x", salt[i]));
		}     

		return sb.toString();
	}
	
	/**
	 * 사용자 Public Ip 조회
	 * @param request
	 * @return
	 */
	public static String getPublicIpByUser(HttpServletRequest request){
		String cmnuseIp = request.getHeader("X-Forwarded-For");
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("Proxy-Client-IP"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("WL-Proxy-Client-IP"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("HTTP_CLIENT_IP"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getHeader("HTTP_X_FORWARDED_FOR"); 
	 	} 
	 	if (cmnuseIp == null || cmnuseIp.length() == 0 || "unknown".equalsIgnoreCase(cmnuseIp)) { 
	 		cmnuseIp = request.getRemoteAddr(); 
	 	}
	 	return cmnuseIp;
	}
	
	/**
	 * 예외처리
	 * @param response
	 * @param message 예외처리시 나타낼 문장
	 * @return
	 */
	public static String alertException(HttpServletResponse response, String message){
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter pwOut = null;
		
		try {
			pwOut = response.getWriter();
			if (message.length() > 100) { // 100자 이상의 메시지일 경우 한줄로 잘라낸다.
				StringTokenizer tokenMessage = new StringTokenizer(message, "\n");
				message = tokenMessage.nextToken();
			}
			message = strToAlert(message);
			pwOut.println("<!DOCTYPE html><html lang=\"ko\"><head><title>시스템안내</title><meta charset=\"utf-8\">");
			pwOut.println("<script language='javascript'>");
			pwOut.println("alert('" + message + "');");
			pwOut.println("history.back();");
			pwOut.println("</script>");
			pwOut.println("</head></html>");
		} catch (IOException e) {
			log.debug("CommonUtil [alertException] : "+e.getMessage());
			return null;
		} finally {
			pwOut.flush();
		}

		return null;
		
	}
	
	/**
	 * 예외처리
	 * @param response
	 * @param message 예외처리시 나타낼 문장
	 * @param url 이동할 url
	 * @return
	 */
	public static String alertException(HttpServletResponse response, String message, String url){
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter pwOut = null;
		
		try {
			pwOut = response.getWriter();
			if (message.length() > 100) { // 100자 이상의 메시지일 경우 한줄로 잘라낸다.
				StringTokenizer tokenMessage = new StringTokenizer(message, "\n");
				message = tokenMessage.nextToken();
			}
			message = strToAlert(message);
			pwOut.println("<!DOCTYPE html><html lang=\"ko\"><head><title>시스템안내</title><meta charset=\"utf-8\">");
			pwOut.println("<script language='javascript'>");
			pwOut.println("alert('" + message + "');");
			pwOut.println("location.href='" + url + "';");
			pwOut.println("</script>");
			pwOut.println("</head></html>");
		} catch (IOException e) {
			log.debug("CommonUtil [alertException] : "+e.getMessage());
			return null;
		} finally {
			pwOut.flush();
		}

		return null;
		
	}
	
	/**
	 * 문자열 띄어쓰기 탭 나타나게
	 * @param s 
	 * @return
	 */
	private static String strToAlert(String s) {
		if (s == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		char[] c = s.toCharArray();
		int len = c.length;
		for (int i = 0; i < len; i++) {
			if (c[i] == '\n') {
				buf.append("\\n");
			} else if (c[i] == '\t') {
				buf.append("\\t");
			} else if (c[i] == '"') {
				buf.append(" ");
			} else if (c[i] == '\'') {
				buf.append("\\'");
			} else if (c[i] == '\r') {
				buf.append("\\r");
			} else {
				buf.append(c[i]);
			}
		}
		return buf.toString();
	}
	
	/**
	 * GET 방식 파라미터 전달
	 * 메뉴 FORM 파라미터
	 * 다건, 단건 ComonMap 파라미터 전달
	 * @param  
	 * @return
	 */
	public static CommonMap getParamMapInfo(CommonMap commonMap) {
		
		// 파라미터 전달
		if (commonMap.get("paramVal") != null && commonMap.get("paramNm") != null) {
			String paramValArr[] = commonMap.get("paramVal").toString().split(",");
			String paramNmArr[] = commonMap.get("paramNm").toString().split(",");
			
			// 복수 파라미터 전달
			if (paramValArr.length > 1) {
				for (int i = 0; i < paramValArr.length; i++) {
					commonMap.put(paramNmArr[i], paramValArr[i]);
				}
				
			// 단건 파라미터 전달
			} else {
				commonMap.put((String) commonMap.get("paramNm"), commonMap.get("paramVal"));
			}
		}
		
	return commonMap;
	}
	
	/**
	 * 문자열 분리
	 * @param _strData
	 * @param _strSplit
	 * @return
	 */
	public static Vector GetEleFromString(String _strData, String _strSplit) {
		Vector	veResult = new Vector();
		String	strBuf = "";

		if(_strData == null || _strData.equals("")){
			return veResult;
		}
		for(int i=0;i<_strData.length();i++){
			if(_strSplit.equals("" + _strData.charAt(i))){
				veResult.add(strBuf);
				strBuf = "";
			}else{
				strBuf += _strData.charAt(i);
			}
		}

		veResult.add(strBuf);

		return veResult;
	}
	
	/**
	 * 문자의 세자리마다 ,를 삽입한다.<br>
	 * 즉, '1000000'를 '1,000,000'으로 만들어 준다.
	 * 
	 * @param str
	 *            원래 문자열
	 * @return ,를 삽입한 문자열
	 */
	public static String getCommaString(String str) {
	    if(str == null) return "";
	    // 소수점을 잘라둔다.
	    Vector veNum = GetEleFromString(str, ".");
	    String strExt = "";
	    if(veNum.size() > 1) {
			str 	= "" + veNum.get(0);
			strExt 	= "" + veNum.get(veNum.size()-1);
	    }

		StringBuffer sb = new StringBuffer();
		char aChar;
		int len = str.length();

		int commaIndex = (len - 1) % 3;

		for (int i = 0; i < len; i++) {
			aChar = str.charAt(i);

			sb.append(aChar);
			if (i == commaIndex && i < len - 1) {
				sb.append(',');
				commaIndex += 3;
			}
		}
		
		String strResult = sb.toString();
		if(!strExt.equals(""))
		    strResult += "." + strExt;

		return strResult;
	}
	
	/**
	 * 태그제거
	 * @param reqValue
	 * @return
	 */
	public static String getDelTag(String reqValue) {
		if(reqValue == null) {
			return "";
		}
	    String wResult = reqValue;

	    wResult = wResult.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
	    wResult = wResult.replaceAll("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", "");
	    wResult = wResult.replaceAll("<\\w+\\s+[^<]*\\s*>", "");
      	wResult = wResult.replaceAll("&[^;]+;", "");
	    
	    return  wResult;
	}
	
	/**
	 *  2013 ~ 현재년도+5 년도 리턴하기
	 */
	public static List getPdYear() {
	    Calendar wNow = Calendar.getInstance();
	    
	    //현재 +5 년도
	    int fYear = wNow.get(Calendar.YEAR)+5;
	    
	    List yearList = new ArrayList();
	    Map yearMap = new HashMap();
	    
	    for ( int y = 2013 ; y <= fYear ; y++ ) {
	    	   yearMap.put("year", y);
	    	   yearList.add(yearMap);
	    	   yearMap = new HashMap();
	    }
	    
	    return yearList;
	}
	
	  
	/**
	 *  작년 년도 리턴하기
	 */
	public static String getPreYear() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR) - 1;
	    wDate.append(wYear);
	    
	    return wDate.toString();

	}
	
	/**
	 *  현재년도 리턴하기
	 */
	public static String getYear() {
		StringBuffer wDate = new StringBuffer();
		
		Calendar wNow = Calendar.getInstance();
		int wYear    = wNow.get(Calendar.YEAR);
		wDate.append(wYear);
		
		return wDate.toString();
		
	}
	  
	/**
	 *  현재월 리턴하기
	 */
	public static String getMonth() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;

	    wDate.append(wYear + "/");

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append(wMonth);

	    return wDate.toString();

	}
	
	/**
	 *  월 더하기
	 */
	public static String getAddMonth(String paraDate, int addCnt){
		
		String resultDt = "";
		
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
			Calendar cal = Calendar.getInstance();
			
			//날짜설정
			Date date = format.parse(paraDate);
	        cal.setTime(date);
	        
	        //월 더하기
	        cal.add(Calendar.MONTH, addCnt); 
	        resultDt = format.format(cal.getTime());
	        
		} catch (ParseException e) {
			log.debug("CommonUtil [alertException] : "+e.getMessage());
		}
		
		
	return resultDt;	
	}
	  
	/**
	 *  현재일 리턴하기
	 */
	public static String getDate() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;
	    int wDay     = wNow.get(Calendar.DATE);

	    wDate.append(wYear + "/");

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append(wMonth + "/");

	    if(wDay < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wDay);

	    return wDate.toString();

	}
	
	/**
	 *  현재일+시간 리턴하기(구분값 없음)
	 */
	public static String getDateTime() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;
	    int wDay     = wNow.get(Calendar.DATE);
	    int wHour     = wNow.get(Calendar.HOUR);
	    int wMin     = wNow.get(Calendar.MINUTE);
	    int wSec     = wNow.get(Calendar.SECOND);

	    wDate.append("" + wYear);

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wMonth);

	    if(wDay < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wDay);
	    
	    if(wHour < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wHour);
	    
	    if(wMin < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wMin);
	    
	    if(wSec < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wSec);

	    return wDate.toString();

	}
	  
	/**
	 *  전일 리턴하기
	 */
	public static String getYestesrDate() {
	    StringBuffer wDate = new StringBuffer();

	    Calendar wNow = Calendar.getInstance();
	    int wYear    = wNow.get(Calendar.YEAR);
	    int wMonth   = wNow.get(Calendar.MONTH) + 1;
	    wNow.add(Calendar.DATE, -1);
	    int wDay     = wNow.get(Calendar.DATE);

	    wDate.append(wYear + "/");

	    if(wMonth < 10){
	    	wDate.append("0");
	    }
	    wDate.append(wMonth + "/");

	    if(wDay < 10){
	    	wDate.append("0");
	    }
	    wDate.append("" + wDay);

	    return wDate.toString();

	}
	
	/**
	 *  다음일 리턴하기
	 */
	public static String getTomorrowDate() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.add(Calendar.DATE, 1);
		int month = calendar.get(calendar.MONTH)+1;
		
		String wDate = "";
		wDate += calendar.get(Calendar.YEAR) +"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
	}
	
	/**
	 *  월 첫째날
	 */
	public static String getMonthFirst() {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getMinimum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  월 말  날짜
	 */
	public static String getMonthEnmt() {
		
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(calendar.MONTH)+1;
		String wDate = "";
		wDate  += calendar.get(calendar.YEAR)+"/";
		wDate += month < 10 ? "0"+month : month;
		wDate += "/";
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		wDate += day < 10 ? "0"+day : day;
		
		return wDate.toString();
		
	}
	
	/**
	 *  브라우저 정보
	 */
	public static String getBrwsr(HttpServletRequest request) {

		//헤더 null 값 체크
		if ( request.getHeader("User-Agent") != null){
			 String header = request.getHeader("User-Agent");
			 
			 //브라우저  체크
			 if ( header.indexOf("Edge") > -1 ){
				   return "IE";
				   
			 } else if (header.indexOf("Trident") > -1 || header.indexOf("MSIE") > -1) {
				   return "IE";
			    
			 } else if (header.indexOf("Chrome") > -1) {
				   return "Chrome";
			    
		     } else if (header.indexOf("Safari") > -1) {
				   return "Safari";
			    
		     } else if (header.indexOf("Opera") > -1) {
				   return "Opera";
				    
		     } else {  
				   return "Firefox";
		     }
			 
		} else {
			return "etc";
		}
		  
	}
	
	/**
	 *  브라우저 버전  정보
	 */
	public static String getBrwsrVer(HttpServletRequest request) {
		
		//헤더 null 값 체크
		if ( request.getHeader("User-Agent") != null){
			
			//헤더
			String header = request.getHeader("User-Agent");
			
			//edge 체크
			if ( header.indexOf("Edge") > -1 ) {
					return "Edge";
					
			//Trident 버전 체크		
			} else if ( header.indexOf("Trident") > - 1 ) {
				String brwsrVer = "";
				String trident = "Trident/7.0%Trident/6.0%Trident/5.0%Trident/4.0%";
				String tridentAr [] = trident.split("%");
				
				for ( int tr = 0 ; tr < tridentAr.length ; tr++ ){
						
						if ( header.indexOf(tridentAr[tr]+";") > -1 ) {
							brwsrVer = tridentAr[tr];
						}
						
				}
				return brwsrVer;
			
			//MSIE 버전 체크
			} else if ( header.indexOf("MSIE") > - 1 ) {
				String brwsrVer = "";
				String msie = "MSIE 10.0%MSIE 9.0%MSIE 8.0%MSIE 7.0%MSIE 6.0%";
				String msieAr [] = msie.split("%");
				
				for ( int tr = 0 ; tr < msieAr.length ; tr++ ){
						
						if ( header.indexOf(msieAr[tr]+";") > -1 ) {
							brwsrVer = msieAr[tr];
						}
						
				}
				return brwsrVer;
			
			//Chrome 체크
			} else if (header.indexOf("Chrome") > -1) {
				int splitVal = header.indexOf("Chrome")+7;
				String brwsrVer ="";
				brwsrVer = header.substring(splitVal, splitVal+13);
				return brwsrVer;
			
			//Opera 체크
		    } else if (header.indexOf("Opera") > -1) {
				   return "Opera";
			
			//Safari 체크    
		    }  else if (header.indexOf("Safari") > -1) {
		    	   return "Safari";
			
		    //Firefox 체크    
		    } else {
				return "Firefox";
		    }
			
		}
			
		return "etc";
	}
	
	/**
	 *  단말기 정보
	 */
	public static String getTrmnl(HttpServletRequest request) {

		// 헤더 null 값 체크
		if (request.getHeader("User-Agent") != null) {
			String header = request.getHeader("User-Agent");

			// 단말기 체크
			if (header.indexOf("Mobile") > -1) {
				return "mobile";
			} else {
				return "PC";
			}

		} else {
			return "etc";
		}

	}
	
	/**
	 *  OS 정보
	 */
	public static String getOs(HttpServletRequest request) {
		
		String header = request.getHeader("User-Agent");
		
		//  null 값 체크
		if (header != null) {
			
			//모바일 체크
		    if (header.indexOf("Mobile") > -1 && header.indexOf("Android") > -1) {
		    	 return "Android";
		    	 
			} else if (header.indexOf("Mobile") > -1 && header.indexOf("iPhone") > -1) {
				 return "iOS";
			}

			
			//os 정보 호출
			if(header.indexOf("Windows") > -1){
				return "Windows";
				
			} else if(header.indexOf("Mac OS") > -1){
				return "Mac";
				
			} else if(header.indexOf("Linux" ) > -1){
				return "Linux";
				
			} else if(header.indexOf("Unix")  > -1) {
				return "Unix";
				
			} else if(header.indexOf("Sunos")  > -1 ||header.indexOf("Solaris")  > -1) {
				return "Solaris";
				
			//모바일 OS 확인
			} else {
				return "etc";
			}
				
		} else {
			return "none";
		}
		
	}
	
	/**
	 * OS 버전 확인
	 */
	public static String getOsVer(HttpServletRequest request) {
		
		// 헤더 null 값 체크
		if (request.getHeader("User-Agent") != null) {
			
			String header = request.getHeader("User-Agent");
			
			//모바일 체크
		    if (header.indexOf("Mobile") > -1 && header.indexOf("Android") > -1) {
		    	 return "Android";
		    	 
			} else if (header.indexOf("Mobile") > -1 && header.indexOf("iPhone") > -1) {
				 return "iOS";
			}
			
			if(header.indexOf("NT 10.0") != -1) 
				return "Windows 10";
			else if(header.indexOf("NT 6.1") != -1 || header.indexOf("NT 7.0") != -1) 
				return "Windows 7";
			else if(header.indexOf("NT 6.0") != -1) 
				return "Windows Vista/Server 2008";
			else if(header.indexOf("NT 5.2") != -1) 
				return "Windows Server 2003";
			else if(header.indexOf("NT 5.1") != -1) 
				return "Windows XP";
			else if(header.indexOf("NT 5.0") != -1) 
				return "Windows 2000";
			else if(header.indexOf("NT") != -1) 
				return "Windows NT";
			else if(header.indexOf("9x 4.90") != -1) 
				return "Windows Me";
			else if(header.indexOf("98") != -1)
				return "Windows 98";
			else if(header.indexOf("95") != -1) 
				return "Windows 95";
			else if(header.indexOf("Win16") != -1) 
				return "Windows 3.x";
			else if(header.indexOf("Linux") != -1) 
				return "Linux";
			else if(header.indexOf("Macintosh") != -1) 
				return "Macintosh";

		} else {
			return "etc";
		}

	 return "etc";
	}
	
	/**
	 * Reference Adress 정보
	 * @param request
	 * @return
	 */
	public static String getRefer(HttpServletRequest request) {
		
		String referer = request.getHeader("referer");
		
		if(referer == null){
			return "";
		} else {
			return referer;
		}
	}
	
    /**
     * AES 방식의 암호화
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public static String encryptAes(String message) {

        // use key coss2
    	String aesSecretKey = "nForUv3AesSecret";
    	if(aesSecretKey == null) return null;
        SecretKeySpec skeySpec = new SecretKeySpec(aesSecretKey.getBytes(), "AES");

        // Instantiate the cipher
        Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.debug("CommonUtil [encryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        try {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			log.debug("CommonUtil [encryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}

        byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(message.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.debug("CommonUtil [encryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        return byteArrayToHex(encrypted);
    }

    /**
     * AES 방식의 복호화
     * 
     * @param message
     * @return
     * @throws Exception
     */
    public static String decryptAes(String encrypted) {
    	
        // use key coss2
    	String aesSecretKey = "nForUv3AesSecret";
    	if(aesSecretKey == null) return null;
        SecretKeySpec skeySpec = new SecretKeySpec(aesSecretKey.getBytes(), "AES");

        Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			log.debug("CommonUtil [decryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		} catch (InvalidKeyException e) {
			log.debug("CommonUtil [decryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        
        byte[] original = null;
		try {
			original = cipher.doFinal(hexToByteArray(encrypted));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			log.debug("CommonUtil [decryptAes] "+e.getClass().getName()+" : "+e.getMessage());
		}
        String originalString = new String(original);
        return originalString;
    }
    
    /**
     * hex to byte[] : 16진수 문자열을 바이트 배열로 변환한다.
     * 
     * @param hex    hex string
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return ba;
    }

    /**
     * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 바꾼다.
     * 
     * @param ba        byte[]
     * @return
     */
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }
        return sb.toString();
    } 
    
}
