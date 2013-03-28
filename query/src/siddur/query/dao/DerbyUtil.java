package siddur.query.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import siddur.query.bean.Comment;
import siddur.query.bean.ProjectInfo;
import siddur.query.bean.UserInfo;

public class DerbyUtil {
	
	public static DerbyUtil instance = new DerbyUtil();
	private Connection conn = null;
	private DerbyUtil(){}
	
	public void init() throws Exception{
		Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
		try {
			conn = DriverManager.getConnection("jdbc:derby:query");
		} catch (Exception e) {
			conn = DriverManager.getConnection("jdbc:derby:query;create=true");
			Statement st = conn.createStatement();
			//table USER_INFO
			String sql = "create table USER_INFO(username varchar(10) NOT NULL, password varchar(10) NOT NULL, project int, user_type int, primary key (username))";
			st.execute(sql);
			sql = "insert into USER_INFO(username, password, project, user_type) values('superAdmin', 'password', 1, 1)";
			st.execute(sql);
			
			//table PROJECT_INFO
			sql = "create table PROJECT_INFO(id int not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
					"project varchar(10) NOT NULL)";
			st.execute(sql);
			sql = "insert into PROJECT_INFO(project) values('hisoft')";
			st.execute(sql);
			
			//table QUESTION
			sql = "create table COMMENT(" +
						"id int not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
						"content varchar(500), " +
						"writeAt TIMESTAMP, " +
						"writeBy varchar(10), " +
						"target int, " +
						"project int" +
					")";
			st.execute(sql);
			
			sql = "create table COMMENT_CUSTOMER(" +
					"id int not null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
					"target int, " +
					"customer varchar(10)," +
					"status int default 1" +
				")";
			st.execute(sql);
			
			st.close();
		}
	}
	
	public void destroy() throws SQLException {
		conn.close();
	}
	
	public Comment addComment(Comment c){
		try {
			String sql = "insert into COMMENT(content, writeAt, writeBy, target, project) values(?, ?, ?, ?, ?)";
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, c.content);
			pst.setTimestamp(2, new Timestamp(c.writeAt.getTime()));
			pst.setString(3, c.writeBy);
			pst.setInt(4, c.target);
			pst.setInt(5, c.project);
			pst.execute();
			pst.close();
			
			sql = "select max(id) from COMMENT";
			Statement st = conn.createStatement();
			ResultSet r = st.executeQuery(sql);
			if(r.next()){
				c.id = r.getInt(1);
			}
			st.close();
			return c;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean closeComment(int id){
		boolean result = false;
		try {
			Statement st = conn.createStatement();
			String sql = "update COMMENT c set c.target=0 where c.id=" + id;
			st.execute(sql);
			st.close();
			result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Comment> listAsks(){
		List<Comment> list = new ArrayList<Comment>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT c where c.target<1";
			ResultSet rs =st.executeQuery(sql);
			while(rs.next()){
				Comment c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
				list.add(c);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public Comment getCommentById(int id){
		Comment c = null;
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT c where c.id = " + id;
			ResultSet rs =st.executeQuery(sql);
			if(rs.next()){
				c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return c;
	}
	
	public List<Comment> listAnswers(int ask){
		List<Comment> list = new ArrayList<Comment>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT c where c.target=" + ask + " order by writeAt desc";
			ResultSet rs =st.executeQuery(sql);
			while(rs.next()){
				Comment c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
				list.add(c);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Comment> listMyAnswers(String username){
		List<Comment> list = new ArrayList<Comment>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT cc where cc.id in (select c.target from COMMENT c where c.writeBy='" + username + "') order by cc.writeAt desc";
			ResultSet rs =st.executeQuery(sql);
			rs.setFetchSize(100);
			while(rs.next()){
				Comment c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
				list.add(c);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Comment> listAllOpenQuestions(){
		List<Comment> list = new ArrayList<Comment>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT c where c.target=-1  order by c.writeAt desc";
			ResultSet rs =st.executeQuery(sql);
			rs.setFetchSize(100);
			while(rs.next()){
				Comment c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
				list.add(c);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Comment> listMyQuestions(String username){
		List<Comment> list = new ArrayList<Comment>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT c where c.target<1 and c.writeBy='" + username + "' order by c.target asc, c.writeAt desc";
			ResultSet rs =st.executeQuery(sql);
			rs.setFetchSize(100);
			while(rs.next()){
				Comment c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
				list.add(c);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public boolean addUser(UserInfo user){
		try {
			Statement st = conn.createStatement();
			String sql = "insert into USER_INFO values('"+user.username+"', '"+user.password+"', "+user.project+", "+user.type + ")";
			st.execute(sql);
			st.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public List<UserInfo> listCustomers(int project){
		try {
			Statement st = conn.createStatement();
			String sql = "select * from USER_INFO u where u.user_type=3 and u.project=" + project;
			ResultSet r = st.executeQuery(sql);
			List<UserInfo> list = new ArrayList<UserInfo>();
			while(r.next()){
				UserInfo u = new UserInfo();
				u.username = r.getString(1);
				u.password = r.getString(2);
				u.project = r.getInt(3);
				u.type = r.getInt(4);
				list.add(u);
			}
			r.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<UserInfo> listUsers(){
		try {
			Statement st = conn.createStatement();
			String sql = "select * from USER_INFO";
			ResultSet r = st.executeQuery(sql);
			List<UserInfo> list = new ArrayList<UserInfo>();
			while(r.next()){
				UserInfo u = new UserInfo();
				u.username = r.getString(1);
				u.password = r.getString(2);
				u.project = r.getInt(3);
				u.type = r.getInt(4);
				list.add(u);
			}
			r.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteUser(String username){
		try {
			Statement st = conn.createStatement();
			String sql = "delete from USER_INFO u where u.username = '" + username + "'";
			st.execute(sql);
			st.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean updateUser(UserInfo u){
		try {
			Statement st = conn.createStatement();
			String sql = "update USER_INFO u set u.password='"+u.password+"', u.project="+u.project+" where u.username = '" + u.username + "'";
			st.execute(sql);
			st.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public UserInfo getUser(String username){
		UserInfo u = null;
		try {
			Statement st = conn.createStatement();
			String sql = "select * from USER_INFO u where u.username='" + username + "'";
			ResultSet r = st.executeQuery(sql);
			if(r.next()){
				u = new UserInfo();
				u.username = r.getString(1);
				u.password = r.getString(2);
				u.project = r.getInt(3);
				u.type = r.getInt(4);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}
	
	public List<ProjectInfo> getProjects(){
		List<ProjectInfo> list = new ArrayList<ProjectInfo>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from PROJECT_INFO";
			ResultSet r = st.executeQuery(sql);
			while(r.next()){
				ProjectInfo p = new ProjectInfo();
				p.id = r.getInt(1);
				p.name = r.getString(2);
				list.add(p);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean addProject(String project){
		boolean result = false;
		try {
			Statement st = conn.createStatement();
			String sql = "insert into PROJECT_INFO(project) values('"+project+"')";
			st.execute(sql);
			st.close();
			result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean askCustomer(String username, int comment){
		boolean result = false;
		try {
			Statement st = conn.createStatement();
			String sql = "insert into COMMENT_CUSTOMER(target, customer) values("+comment+", '"+username+"')";
			st.execute(sql);
			st.close();
			result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean customerAnswer(String username, int comment){
		boolean result = false;
		try {
			Statement st = conn.createStatement();
			String sql = "update COMMENT_CUSTOMER c set c.status=0 where c.target=" + comment + " and c.customer='" + username +"'";
			st.execute(sql);
			st.close();
			result = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Comment> questions4Customer(String username){
		List<Comment> list = new ArrayList<Comment>();
		try {
			Statement st = conn.createStatement();
			String sql = "select * from COMMENT c, COMMENT_CUSTOMER cc where cc.target=c.id and cc.status=1 and cc.customer='" + username + "' order by writeAt desc";
			ResultSet rs =st.executeQuery(sql);
			while(rs.next()){
				Comment c = new Comment();
				c.id = rs.getInt(1);
				c.content = rs.getString(2);
				c.writeAt = rs.getTimestamp(3);
				c.writeBy = rs.getString(4);
				c.target = rs.getInt(5);
				c.project = rs.getInt(6);
				list.add(c);
			}
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
}
