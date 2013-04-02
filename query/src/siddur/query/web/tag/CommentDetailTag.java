package siddur.query.web.tag;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import siddur.query.bean.Comment;

/**
 * <div class="detail">
		asked by <font color="#FAA732"><%= c.writeBy%></font> at <font color="#5BB75B"><%= c.writeAt%></font>
	</div>
 *
 */
public class CommentDetailTag extends TagSupport{
	private static final long serialVersionUID = 1L;
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private Comment comment;

	public void setComment(Comment comment) {
		this.comment = comment;
	}
	
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			out.println("<div class='comment_detail'>");
			out.println("asked by <font color='#FAA732'>"+comment.writeBy+"</font> at <font color='#5BB75B'>"+dateFormat.format(comment.writeAt)+"</font>");
			out.println("</div>");
		} catch (IOException e) {}
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
}
