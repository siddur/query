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


<script src="/js/jquery.js"></script>
<script>
	function toEdit(id){
		$(".disappear2").addClass("disappear");
		$(".disappear1").removeClass("disappear");
		$("#" + id + " .disappear2").removeClass("disappear");
		$("#" + id + " .disappear1").addClass("disappear");
	}
</script>
</head>
<body>

<%@include file="/jsp/common/manage.jsp" %>
<div class="crumb">
	<%= request.getAttribute("crumb")%>
</div>
<div class="body">
	<table>
		
	<%if(projects != null){%>
		<%for(ProjectInfo p : projects){ %>
			<tr id="<%=p.id%>"><form action="/query/project/update">
				<td width="300">
					<span class="disappear1 item"><%=p.name%></span>
					<input value="<%=p.id%>" name="id" type="hidden">
					<input class="disappear2 disappear" name="project" value="<%=p.name%>" type="text" >	
				</td>
				<td>
					<input class="disappear1 btn" value="edit" type="button" onclick="toEdit(<%=p.id%>);">
					<input class="disappear2 btn disappear" value="save" type="submit">
				</td>
			</form></tr>
		<%} %>
	<%}%>
	</table>
	<p style="margin-top: 50px;">
		<form method="post" action="/query/project/add">
			<input name="project" type="text">
			<input class="btn" type="submit" value="add">
		</form>
	</p>
	
</div>
<style>
	span.item{
		background-color: #A9DBA9;
		padding:5px 10px;;
	}
	
	.crumb{
		padding:5px;
		text-indent:10px;
		background-color:#FCF8E3;
	}
	input[type="text"]{
		 height:21px;
	}
	.disappear{
		display: none;
	}
</style>
</body>
</html>