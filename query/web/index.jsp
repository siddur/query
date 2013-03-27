<%@page import="siddur.query.bean.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>home</title>
<style>
	.content{
		text-align:center;
		position:relative;
		top:100px;
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="content">
		<form method="post" action="/query/query/search">
			<input name="key" class="search_input txt">
			<input class="search_button btn" type="submit" value="search">
			<input type="hidden" name="pageIndex" value="0">
			<input type="hidden" name="pageSize" value="5">
		</form>
	</div>
</body>
</html>