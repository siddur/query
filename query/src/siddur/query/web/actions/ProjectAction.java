package siddur.query.web.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import siddur.query.bean.ProjectInfo;
import siddur.query.web.Action;
import siddur.query.web.ActionMapper.Result;

public class ProjectAction extends Action{

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Result list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		List<ProjectInfo> list = dbUtil.getProjects();
		req.setAttribute("projects", list);
		req.setAttribute("crumb", "manage > project");
		req.getRequestDispatcher("/jsp/project/list.jsp").forward(req, resp);
		return Result.ok();
	}
	
	public Result add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String p = req.getParameter("project");
		dbUtil.addProject(p);
		return list(req, resp);
	}
	
	public Result update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String name = req.getParameter("project");
		String id = req.getParameter("id");
		ProjectInfo p = new ProjectInfo();
		p.id = Integer.parseInt(id);
		p.name = name;
		dbUtil.editProject(p);
		return list(req, resp);
	}

}
