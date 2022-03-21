package egovframework.soft;

import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.service.CommonMap;
import egovframework.com.service.CommonService;

@Controller
public class main {
	
	@Resource(name = "commonService")
	private CommonService commonService;
	
	@Resource(name = "globalsProperties")
	private Properties globalsProperties;
	
	@RequestMapping(value =  "/idx")
	public String mainAction(HttpServletRequest request, CommonMap paramMap, ModelMap model) {
		
		String mappingUrl = request.getServletPath();
		
		return "/soft/index.jsp";
	}
	
}
