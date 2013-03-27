<%@page import="siddur.query.bean.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	UserInfo me = (UserInfo) session.getAttribute("user");
	String username = null;
	boolean isSuper = false;
	boolean isCustomer = false;
	if(me != null){
		username = me.username;
		isSuper = username.equals("superAdmin");
		isCustomer = me.type == 3;
	}
	
%>
<link rel="stylesheet" type="text/css" href="/css/common.css" />
<style>
	.small-txt{
		width:80px;
		font-size:14px;
		height: 20px;
	}
	.log-btn{
		height:24px;
		padding:2px;
		background-color: #F5F5F5;
		background-image: linear-gradient(to bottom, #FFFFFF, #E6E6E6);
    	background-repeat: repeat-x;
    	color:#333333;
    	font-size:12px;
	}
</style>
<div class="header">
	<div class="logo">
		Query
	</div>
	
	<div class="user">
	<%
		if(username != null){
	%>
		<form method="get" action="/query/user/logout">
			<a href="/query/user/me"><%= username %></a>&nbsp;&nbsp;
			<input class="btn log-btn" type="submit" value="logout">
			&nbsp;
		</form>
	<%
		}else{
	%>
		<form method="post" action="/query/user/login">
			<input class="small-txt txt" type="text" name="username" placeholder="username">
			<input class="small-txt txt" type="text" name="password" placeholder="password">
			<input class="btn log-btn" type="submit" value="login">
			&nbsp;
		</form>
	<%			
		}
	%>
	</div>
</div>
<div class="border">
	<a href="/query">home</a>
	<%
		if(username != null && !isCustomer){
	%>
	<a href="/jsp/query/ask.jsp">ask</a>
	<%
		}
	%>
	<%
		if(isSuper){
	%>
		<a href="/query/user/list">manage</a>
	<%
		}
	%>
	
	
</div>
