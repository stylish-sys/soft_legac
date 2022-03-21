package egovframework.com.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class CommonPaging {
	
	// 첫페이지
	private int firstPage;
	// 마지막페이지
	private int lastPage;
	// 현재페이지
	private int currPage;
	// 이전페이지
	private int prevPage;
	// 다음페이지
	private int nextPage;
	// 페이징
	private String pageTag;
	// 전체페이지
	private int totalPage;
	// 페이징 단위
	private int pageDefaultCo = 10;
	
	public CommonPaging(HttpServletRequest request, int totalCo, int pageSn, int listCo, Map paramMap){
		// 현재페이지
		currPage = pageSn;
		// 첫페이지
		firstPage = 1;
		// 마지막페이지
		totalPage = totalCo/listCo;
		int addList = totalCo%listCo;
		if(addList > 0)
			totalPage += 1;
		lastPage = totalPage;
		// 이전페이지
		prevPage = 0;
		if(currPage > pageDefaultCo){
			if(currPage%pageDefaultCo>0){
				prevPage = currPage/pageDefaultCo*pageDefaultCo;
			} else {
				prevPage = (currPage/pageDefaultCo-1)*pageDefaultCo;
			}
		}
		// 다음페이지
		nextPage = 0;
		if(lastPage > pageDefaultCo){
			if(currPage%pageDefaultCo>0){
				nextPage = (currPage/pageDefaultCo+1)*pageDefaultCo+1;
			} else {
				nextPage = (currPage/pageDefaultCo)*pageDefaultCo+1;
			}
			if(nextPage > lastPage){
				nextPage = 0;
			}
		}
		// 페이징 그리기
		pageTag = createPageTag(request, totalCo, listCo, paramMap);
	}
	
	public String createPageTag(HttpServletRequest request, int totalCo, int listCo, Map paramMap){
		String currentURL = request.getRequestURI();
		StringBuffer sb = new StringBuffer();
		// 1건 이상일경우 페이징 처리
		if(totalCo > 0){
			sb.append("<form name=\"pagingForm\" action=\""+currentURL+"\" method=\"post\">\n");
			sb.append("<input type=\"hidden\" name=\"currPage\" value=\""+currPage+"\" />\n");
			Set<String> keySet = paramMap.keySet();

			Iterator<String> keyIterator = keySet.iterator();

			while(keyIterator.hasNext()) {
			    String key = keyIterator.next();
			    
			    if(!key.equals("currPage")){
			    	String value = paramMap.get(key).toString();
					/*
					 * 작업자 : sjLee (200731)
					 * 작업안 : 웹 취약점( XSS ) 방지를 위한 <, > 태그 조건 처리 
					 */
					if ( value.indexOf("<") == -1 && value.indexOf(">") == -1 && key.indexOf("<") == -1 && key.indexOf(">") == -1 ) {
						sb.append("<input type=\"hidden\" name=\""+key+"\" value=\""+value+"\" />\n");
					}
			    }
			}  
			
			String pageClassNm = "bbs_pagerA";
			String parPageClassNm = Objects.toString(paramMap.get("pageClass"),"");
			if(!parPageClassNm.equals("")){
				pageClassNm = parPageClassNm;
			}
			
			sb.append("<div class=\""+pageClassNm+"\">\n");
			if(totalPage > pageDefaultCo){
				int sIdx = 1;
				int eIdx = lastPage;
				if(currPage%pageDefaultCo > 0)
					sIdx = currPage/pageDefaultCo*pageDefaultCo+1;
				else
					sIdx = (currPage/pageDefaultCo-1)*pageDefaultCo+1;
				eIdx = sIdx + (pageDefaultCo-1);
				if(eIdx > lastPage)
					eIdx = lastPage;
				if (currPage == 1) {
					sb.append("<a href=\"javascript:\" class=\"bbs_arr pgeL2\">첫 페이지</a>\n");
				} else {
					sb.append("<a href=\"javascript:\" onclick=\"goPaging(1)\" class=\"bbs_arr pgeL2\">첫 페이지</a>\n");
				}
				if(prevPage > 0){
					sb.append("<a href=\"javascript:\" onclick=\"goPaging("+prevPage+")\" class=\"bbs_arr pgeL1\">이전 페이지</a>\n");
				}
				for(int idx=sIdx;idx<=eIdx;idx++){
					if(currPage == idx){
						sb.append("<strong class=\"bbs_pge_num\" title=\"현재페이지\">"+idx+"</strong>\n");
					} else {
						sb.append("<a href=\"javascript:\" onclick=\"goPaging("+idx+")\" class=\"bbs_pge_num\" >"+idx+"</a>\n");
					}
				}
				if(nextPage > 0){
				sb.append("<a href=\"javascript:\" onclick=\"goPaging("+nextPage+")\" class=\"bbs_arr pgeR1\">다음 페이지</a>\n");
				}
				if (currPage == lastPage) {
					sb.append("<a href=\"javascript:\" class=\"bbs_arr pgeR2\" >끝 페이지</a>\n");
				} else {
					sb.append("<a href=\"javascript:\" onclick=\"goPaging("+lastPage+")\" class=\"bbs_arr pgeR2\" >끝 페이지</a>\n");
				}
			} else if(totalPage > 1 && totalPage <( pageDefaultCo+1)) {
				sb.append("<a href=\"javascript:\" onclick=\"goPaging(1)\" class=\"bbs_arr pgeL2\">첫 페이지</a>\n");
				for(int idx=1;idx<=lastPage;idx++){
					if(currPage == idx){
						sb.append("<strong class=\"bbs_pge_num\" title=\"현재페이지\">"+idx+"</strong>\n");
					} else {
						sb.append("<a href=\"javascript:\" onclick=\"goPaging("+idx+")\" class=\"bbs_pge_num\" >"+idx+"</a>\n");
					}
				}
				sb.append("<a href=\"javascript:\" onclick=\"goPaging("+lastPage+")\" class=\"bbs_arr pgeR2\" >끝 페이지</a>\n");
			} else {
				sb.append("<strong class=\"bbs_pge_num\" title=\"현재페이지\">1</strong>\n");
			}
			sb.append("</div>\n");	
			sb.append("</form>\n");	
			sb.append("<script>function goPaging(pageSn){document.pagingForm.currPage.value=pageSn;document.pagingForm.submit();}</script>\n");
		} 
		return sb.toString();
	}
	
	
	public int getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}
	public int getLastPage() {
		return lastPage;
	}
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public String getPageTag(){
		return pageTag;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
