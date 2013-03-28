package siddur.query.web.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.query.bean.Comment;
import siddur.query.bean.UserInfo;
import siddur.query.web.Action;
import siddur.query.web.ActionMapper.Result;
import siddur.query.web.tag.Paging;

public class QueryAction extends Action{

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public Result ask(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String comment = req.getParameter("comment");
		if(!validate(comment)){
			req.getRequestDispatcher("/jsp/query/ask.jsp").forward(req, resp);
			return Result.error();
		}
		
		String type = req.getParameter("type");
		if(type.equals("still ask")){
			doAsk(comment, req);
			return search(req, resp);
		}
		
		Paging<Comment> list = doSearch(comment, req, resp);
		if(list.data.isEmpty()){
			doAsk(comment, req);
			return search(req, resp);
		}
		
		hesitate(comment, req, resp);
		req.getRequestDispatcher("/jsp/query/ask.jsp").forward(req, resp);
		return Result.ok();
	}
	
	private boolean validate(String comment){
		if(comment == null || "".equals(comment)){
			return false;
		}
		return true;
	}
	
	private void doAsk(String comment, HttpServletRequest req){
		Comment c = new Comment();
		c.content = comment;
		UserInfo user = (UserInfo)req.getSession().getAttribute("user");
		c.writeBy = user.username;
		c.project = user.project;
		c = dbUtil.addComment(c);
		if(c != null){
			lcUtil.addComment(c);
		}
	}
	
	private void hesitate(String comment, HttpServletRequest req, HttpServletResponse resp){
		req.setAttribute("comment", comment);
		req.setAttribute("msg", "1");
		
		Paging<Comment> list = doSearch(comment, req, resp);
		req.setAttribute("comments", list);
	}
	
	
	public Result answer(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String comment = req.getParameter("comment");
		Comment c = new Comment();
		c.content = comment;
		UserInfo user = (UserInfo)req.getSession().getAttribute("user");
		c.writeBy = user.username;
		c.project = user.project;
		
		int askId = Integer.parseInt(req.getParameter("id"));
		c.target = askId;
		c = dbUtil.addComment(c);
		
		String customer = req.getParameter("customer");
		if(customer != null){
			dbUtil.askCustomer(customer, c.id);
		}
		
		return detail(req, resp);
	}
	
	public Result answer1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String comment = req.getParameter("comment");
		Comment c = new Comment();
		c.content = comment;
		UserInfo user = (UserInfo)req.getSession().getAttribute("user");
		c.writeBy = user.username;
		c.project = user.project;
		
		int target = Integer.parseInt(req.getParameter("target"));
		
		c.target = target;
		dbUtil.addComment(c);
		
		int id = Integer.parseInt(req.getParameter("id"));
		dbUtil.customerAnswer(user.username, id);
		
		return questions(req, resp);
	}
	
	public Result closeQuestion(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String id = req.getParameter("id");
		int commentId = Integer.parseInt(id);
		boolean success = dbUtil.closeComment(commentId);
		if(success){
			Comment c = dbUtil.getCommentById(commentId);
			lcUtil.updateComment(c);
		}
		return myQuestions(req, resp);
	}
	
	
	public Result detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String idStr = req.getParameter("id");
		int id = Integer.parseInt(idStr);
		List<Comment> list = dbUtil.listAnswers(id);
		Comment ask = dbUtil.getCommentById(id);
		
		req.setAttribute("ask", ask);
		req.setAttribute("answers", list);
		
		List<UserInfo> customers = dbUtil.listCustomers(ask.project);
		req.setAttribute("customers", customers);
		
		req.getRequestDispatcher("/jsp/query/answer-list.jsp").forward(req, resp);
		return Result.ok();
	}
	
	public Result search(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String key = req.getParameter("key");
		Paging<Comment> paging = doSearch(key, req, resp);
		req.setAttribute("comments", paging);
		req.getRequestDispatcher("/jsp/query/search.jsp").forward(req, resp);
		return Result.ok();
	}
	
	public Result questions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		List<Comment> list = dbUtil.questions4Customer(u.username);
		req.setAttribute("comments", list);
		req.getRequestDispatcher("/jsp/customer/list.jsp").forward(req, resp);
		return Result.ok();
	}
	
	public Result questions1(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<Comment> list = dbUtil.listAllOpenQuestions();
		req.setAttribute("comments", list);
		req.getRequestDispatcher("/jsp/query/opens.jsp").forward(req, resp);
		return Result.ok();
	}
	
	
	
	
	private Paging<Comment> doSearch(String key, HttpServletRequest req, HttpServletResponse resp){
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		int pageIndex, pageSize;
		try {
			pageIndex = Integer.parseInt(req.getParameter("pageIndex"));
		} catch (NumberFormatException e) {
			pageIndex = 0;
		}
		try {
			pageSize = Integer.parseInt(req.getParameter("pageSize"));
		} catch (NumberFormatException e) {
			pageSize = 15;
		}
		
		req.setAttribute("key", key == null ? "" : key);
		return lcUtil.search(u.project, key, pageIndex, pageSize);
	}
	
	public Result mine(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		//vendor
		if(u.type == 2){
			return myQuestions(req, resp);
		}else{
			return myAnswers(req, resp);
		}
	}
	
	public Result myQuestions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		String username = u.username;
		List<Comment> list = dbUtil.listMyQuestions(username);
		req.setAttribute("comments", list);
		req.setAttribute("crumb", "mine > my questions");
		req.getRequestDispatcher("/jsp/my/questions.jsp").forward(req, resp);
		return Result.ok();
	}
	
	public Result myAnswers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		String username = u.username;
		List<Comment> list = dbUtil.listMyAnswers(username);
		req.setAttribute("comments", list);
		req.setAttribute("crumb", "mine > my answers");
		req.getRequestDispatcher("/jsp/my/questions.jsp").forward(req, resp);
		return Result.ok();
	}
}
