package siddur.query.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class PagingTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	
	private Integer pageIndex, pageSize, total;
	
	@Override
	public int doStartTag() throws JspException {
		int pageNum = (int)Math.ceil((double)total/pageSize);
		
		JspWriter out = this.pageContext.getOut();
		
		try {
			out.println("<div class='paging'>");
			String style = "unselected";
			
			String last = "<span id='last'>last</span>";
			out.println(last);
			
			for (int i = 0; i < pageNum; i++) {
				
				if(i == pageIndex){
					style = "selected";
				}else{
					style = "unselected";
				}
				out.println("<span class='"+style+"' id='"+i+"'>"+(i+1)+"</span>");
			}
			
			String next = "<span id='next'>next</span>";
			out.println(next);
			
			out.println("</div>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
	
}
