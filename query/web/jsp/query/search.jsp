<%@page import="siddur.query.bean.Comment"%>
<%@page import="java.util.List"%>
<%@page import="siddur.query.bean.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search</title>
<style>
	.txt{
		padding-left:5px;
		font-size:16px;
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<form method="post" action="/query/query/search">
			<input name="key" class="txt" value="<%= request.getAttribute("key")%>">
			<input type="submit" value="search" class="btn">
			<input type="hidden" name="pageIndex" value="0">
			<input type="hidden" name="pageSize" value="5">
			
		</form>
		<%@include file="/jsp/common/search-list.jsp" %>
	</div>
</body>
</html>