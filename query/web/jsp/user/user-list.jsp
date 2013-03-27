<%@page import="siddur.query.bean.ProjectInfo"%>
<%@page import="java.util.List"%>
<%@page import="siddur.query.bean.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% 
	List<UserInfo> list = (List<UserInfo>)request.getAttribute("list");
	List<ProjectInfo> projects = (List<ProjectInfo>)request.getAttribute("projects");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Users</title>
<style>
	table{
		border-spacing: 0;
		width:600px;
	}
	td{
		border-bottom: 1px solid #DDDDDD;
		padding:8px;
	}
	
	tr:first-child{
		background-color:#DDDDDD;
		font-weight: bolder;
		font-size: smaller;
	}
	
	tr.item{
		cursor:pointer;
	}
	
	.add_user{
		margin:20px;
	}
	.add_user div{
		padding-bottom:10px;
	}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>
<script>
	$(document).onReady(function(){
			$(.item)
		});
</script>
</head>
<body>

<%@include file="/jsp/common/manage.jsp" %>
	<div class="body">
	<%if(list != null){%>
		<table>
			<tr>
				<td>USERNAME</td>
				<td>PASSWORD</td>
				<td>PROJECT</td>
				<td>TYPE</td>
				<td>&nbsp;</td>
			</tr>
			<%for(UserInfo u : list){ %>
			<tr class="item">
				<td><%=u.username%></td>
				<td><%=u.password%></td>
				<td><%=ProjectInfo.findProjectById(projects, u.project)%></td>
				<td>
				<%
					if(u.type == 1){
						out.append("manager");
					}
					else if(u.type == 2){
						out.append("vendor");
					}
					else{
						out.append("customer");
					}
				%>
				</td>
				<td>
					<form action="/query/user/delete">
						<input type="hidden" name="username" value="<%=u.username%>">
						<input class="btn" type="submit" value="delete">
					</form>
				</td>
			</tr>
			<%} %>
		</table>
		<div class="add_user">
			<form method="post" action="/query/user/add">
				<div>
					Username:
					<input name="username">
				</div>
				<div>
					Password:
					<input name="password">
				</div>
				<div>
					Project:
					<select name="project">
						<%
							for(ProjectInfo p : projects){
						%>
								<option value="<%= p.id%>"><%= p.name%></option>
						<%
							}
						%>
					</select>
				</div>
				<div>
					Type:
					<select name="type">
						<option value="1">manager</option>
						<option value="2">vendor</option>
						<option value="3">customer</option>
					</select>
				</div>
				<input class="btn" type="submit" value="save&add">
			</form>
		</div>
	<%}%>
	</div>
</body>
</html>