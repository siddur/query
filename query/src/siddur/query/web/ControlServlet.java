package siddur.query.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.query.bean.UserInfo;
import siddur.query.dao.DerbyUtil;
import siddur.query.index.LuceneUtil;
import siddur.query.web.ActionMapper.Result;

public class ControlServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private ActionMapper actionMapper;
	
	@Override
	public void init() throws ServletException {
		
		actionMapper = new ActionMapper();
		
		try {
			DerbyUtil.instance.init();
			LuceneUtil.instance.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void destroy() {
		try {
			DerbyUtil.instance.destroy();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!authenticate(req, resp)){
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
			return;
		}
		
		String path = req.getPathInfo();
		
		Result r = actionMapper.exec(path, req, resp);
		if(r == Result.NotFound){
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
		
	}
	
	private boolean authenticate(HttpServletRequest req, HttpServletResponse resp){
		if(null != req.getParameter("username")){
			return true;
		}
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u!=null)
			return true;
		
		for (Cookie c : req.getCookies()){
			if("username".equals(c.getName())){
				String username = c.getValue();
				u = DerbyUtil.instance.getUser(username);
				if(u != null){
					req.getSession().setAttribute("user", u);
					return true;
				}
			}
		}
		return false;
	}
}
