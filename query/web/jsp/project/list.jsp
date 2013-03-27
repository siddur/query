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
<title>Projects</title>
<style>
	p.item span{
		background-color: #A9DBA9;
		padding:5px;
	}
</style>
</head>
<body>

<%@include file="/jsp/common/manage.jsp" %>
<div class="body">
	<%if(projects != null){%>
		<%for(ProjectInfo p : projects){ %>
			<p class="item"><span><%=p.name%></span></p>
		<%} %>
	<%}%>
	
	<p>
		<form method="post" action="/query/project/add">
			<input name="project" style="height:19px">
			<input class="btn" type="submit" value="add">
		</form>
	</p>
</div>
</body>
</html>