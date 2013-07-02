package siddur.query.web.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.query.bean.ProjectInfo;
import siddur.query.bean.UserInfo;
import siddur.query.web.Action;
import siddur.query.web.ActionMapper.Result;

public class UserAction extends Action{

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public Result login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String username = req.getParameter("username");
		UserInfo u = dbUtil.getUser(username);
		if(u != null){
			if(u.password.equals(req.getParameter("password"))){
				req.getSession().setAttribute("user", u);
				Cookie c = new Cookie("username", username);
				c.setPath("/");
				c.setMaxAge(60 * 60 * 24 * 7);
				resp.addCookie(c);
				req.getRequestDispatcher("/query").forward(req, resp);
				return Result.ok();
			}
		}
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
		return Result.error("login failed");
	}
	
	public Result changepwd(HttpServletRequest req, HttpServletResponse resp){
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		String oldPassword = req.getParameter("old_password");
		String newPassword = req.getParameter("new_password");
		if (!u.password.equals(oldPassword)){
			return Result.error("Password is incorrect.");
		}
		
		u.password = newPassword;
		boolean r = dbUtil.updateUser(u);
		if(r){
			return Result.ok();
		}
		return Result.error();
				
	}
	
	public Result logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u != null){
			Cookie c = new Cookie("username", u.username);
			c.setPath("/");
			c.setMaxAge(0);
			resp.addCookie(c);
		}
		req.getSession().invalidate();
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
		
		return Result.ok();
	}
	
	public Result add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = new UserInfo();
		u.username = req.getParameter("username");
		u.password = req.getParameter("password");
		String pId = req.getParameter("project");
		u.project = pId == null ? -1 : Integer.parseInt(pId);
		String pType = req.getParameter("type");
		u.type = pType == null ? -1 : Integer.parseInt(pType);
		dbUtil.addUser(u);
		return list(req, resp);
	}
	
	public Result delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u.username.equals("superAdmin")){
			String username = req.getParameter("username");
			dbUtil.deleteUser(username);
		}
		return list(req, resp);
	}
	
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		UserInfo u = (UserInfo)req.getSession().getAttribute("user");
		if(u.username.equals("superAdmin")){
			List<UserInfo> list = dbUtil.listUsers();
			req.setAttribute("list", list);
			
			List<ProjectInfo> projects = dbUtil.getProjects();
			req.setAttribute("projects", projects);
			req.setAttribute("crumb", "manage > user");
			req.getRequestDispatcher("/jsp/user/user-list.jsp").forward(req, resp);
		}
		return Result.ok();
	}
	
	public Result me(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<ProjectInfo> list = dbUtil.getProjects();
		req.setAttribute("projects", list);
		req.getRequestDispatcher("/jsp/user/user-me.jsp").forward(req, resp);
		return Result.ok();
	}
}
