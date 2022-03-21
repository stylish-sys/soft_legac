package egovframework.com.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("commonService")
public class CommonServiceImpl extends EgovAbstractServiceImpl implements CommonService{
	
	@Resource(name = "commonDAO")
	private CommonDAO commonDAO;
	
	/**
	 * 단건 Map 조회
	 * @param sqlId
	 * @param paramMap
	 * @return Map
	 * @thorws Exception
	 */
	public Map selectMap(String sqlId, Map paramMap) throws IOException, SQLException {
		return commonDAO.selectMap(sqlId, paramMap);
	}
	
	/**
	 * 단건 Object 조회
	 * @param sqlId
	 * @param paramMap
	 * @return Object
	 * @thorws Exception
	 */
	public Object selectObject(String sqlId, Map paramMap) throws IOException, SQLException {
		return commonDAO.selectObject(sqlId, paramMap);
	}
	
	/**
	 * 다건 조회
	 * @param sqlId
	 * @param paramMap
	 * @return List
	 * @thorws Exception
	 */
	public List selectList(String sqlId, Map paramMap) throws IOException, SQLException {
		return commonDAO.selectList(sqlId, paramMap);
	}
	
	/**
	 * 다건 조회 페이징
	 * @param sqlId
	 * @param paramMap
	 * @return List
	 * @thorws Exception
	 */
	public Map selectPageList(String sqlId, Map paramMap, int listCo, HttpServletRequest request) throws IOException, SQLException {
		return commonDAO.selectPageList(sqlId, paramMap, listCo, request);
	}
	
	/**
	 * 데이터 Insert
	 * @param sqlId
	 * @param paramMap
	 * @return int(처리건수)
	 * @thorws Exception
	 */
	public int insertQuery(String sqlId, Map paramMap) throws IOException, SQLException {
		return commonDAO.insertQuery(sqlId, paramMap);
	}
	
	/**
	 * 데이터 Update
	 * @param sqlId
	 * @param paramMap
	 * @return int(처리건수)
	 * @thorws Exception
	 */
	public int updateQuery(String sqlId, Map paramMap) throws IOException, SQLException {
		return commonDAO.updateQuery(sqlId, paramMap);
	}
	
	/**
	 * 데이터 Delete
	 * @param sqlId
	 * @param paramMap
	 * @return int(처리건수)
	 * @thorws Exception
	 */
	public int deleteQuery(String sqlId, Map paramMap) throws IOException, SQLException {
		return commonDAO.deleteQuery(sqlId, paramMap);
	}
	
	
}
