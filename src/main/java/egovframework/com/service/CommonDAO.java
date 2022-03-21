package egovframework.com.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("commonDAO")
public class CommonDAO extends EgovAbstractMapper {
	
	/**
	 * 단건 Map 조회
	 * @param sqlId
	 * @param paramMap
	 * @return Map
	 * @thorws Exception
	 */
	public Map selectMap(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().selectOne(sqlId, paramMap);
	}
	
	/**
	 * 단건 Object 조회
	 * @param sqlId
	 * @param paramMap
	 * @return Object
	 * @thorws Exception
	 */
	public Object selectObject(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().selectOne(sqlId, paramMap);
	}
	
	/**
	 * 다건 조회
	 * @param sqlId
	 * @param paramMap
	 * @return List
	 * @thorws Exception
	 */
	public List selectList(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().selectList(sqlId, paramMap);
	}
	
	/**
	 * 다건 조회 페이징
	 * @param sqlId
	 * @param paramMap
	 * @return List
	 * @thorws Exception
	 */
	public Map selectPageList(String sqlId, Map paramMap, int listCo, HttpServletRequest request) throws IOException, SQLException {
		int totalCo = (int)selectObject(sqlId+"PagingCount", paramMap);
		int pageSn = 1;
		String mapPageSn = (String)paramMap.get("currPage");
		if(mapPageSn != null && !mapPageSn.equals(""))
			pageSn = Integer.parseInt(mapPageSn);
		paramMap.put("maxSn", pageSn*listCo);
		paramMap.put("minSn", (pageSn-1)*listCo);
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		returnMap.put("list", getSqlSession().selectList(sqlId+"Paging", paramMap));
		CommonPaging commonPaging = new CommonPaging(request, totalCo, pageSn, listCo, paramMap);
		returnMap.put("pageTag", commonPaging.getPageTag());
		returnMap.put("totalCo", totalCo);
		int maxIdx = totalCo-(pageSn-1)*listCo;
		returnMap.put("maxIdx", maxIdx);
		returnMap.put("totalCoComma", CommonUtil.getCommaString(""+totalCo));
		returnMap.put("currPage", pageSn);
		returnMap.put("totalPage", commonPaging.getTotalPage());
		return returnMap;
	}
	
	/**
	 * 데이터 Insert
	 * @param sqlId
	 * @param paramMap
	 * @return int(처리건수)
	 * @thorws Exception
	 */
	public int insertQuery(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().insert(sqlId, paramMap);
	}
	
	/**
	 * 데이터 Update
	 * @param sqlId
	 * @param paramMap
	 * @return int(처리건수)
	 * @thorws Exception
	 */
	public int updateQuery(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().update(sqlId, paramMap);
	}
	
	/**
	 * 데이터 Delete
	 * @param sqlId
	 * @param paramMap
	 * @return int(처리건수)
	 * @thorws Exception
	 */
	public int deleteQuery(String sqlId, Map paramMap) throws IOException, SQLException {
		return getSqlSession().delete(sqlId, paramMap);
	}
	
	
}
