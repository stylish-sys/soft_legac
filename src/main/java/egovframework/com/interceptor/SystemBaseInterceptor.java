package egovframework.com.interceptor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import egovframework.com.service.CommonLog;
import egovframework.com.service.CommonMap;
import egovframework.com.service.CommonService;
import egovframework.com.service.CommonUtil;

public class SystemBaseInterceptor extends HandlerInterceptorAdapter{

	Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "commonService")
	private CommonService commonService;

	@Resource(name = "globalsProperties")
	private Properties globalsProperties;

	@SuppressWarnings("unchecked")
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws ServletException {

		String reqUri = request.getRequestURI();

		// 레이아웃 생성( 신규생성 및 갱신 )
	}

	/**
	 * Controller 실행 요청 전에 수행되는 메서드
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

		return true;
	}

	// URI정보 보정
	public String getConverterURI(String reqUri){
		String modUri = reqUri;
		
		return modUri;
	}
}
