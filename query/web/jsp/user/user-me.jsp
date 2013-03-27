<%@page import="siddur.query.bean.ProjectInfo"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	List<ProjectInfo> projects = (List<ProjectInfo>)request.getAttribute("projects");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<form method="post" action="/query/user/changepwd">
		username:<%= me.username %><br>
		password:<input type="password" value="<%= me.password %>"><br>
		project:
		<select name="project" value="<%= me.project%>">
			<%
				for(ProjectInfo p : projects){
			%>
					<option value="<%= p.id%>"><%= p.name%></option>
			<%
				}
			%>
		</select>
		<div>
			<a href="/jsp/user/user-pwd.jsp">change password</a><br>
		</div>
	</form>
</body>
</html>