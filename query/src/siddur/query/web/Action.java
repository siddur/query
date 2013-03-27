package siddur.query.web;

import javax.servlet.http.HttpServletRequest;

import siddur.query.dao.DerbyUtil;
import siddur.query.index.LuceneUtil;


public abstract class Action {

	protected DerbyUtil dbUtil = DerbyUtil.instance;
	protected LuceneUtil lcUtil = LuceneUtil.instance;
	public abstract String getPath();
	
	
	public static void HOME(HttpServletRequest req){
		req.getRequestDispatcher("/index.jsp");
	}
}
