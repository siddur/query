<%@page import="siddur.query.bean.Comment"%>
<%@page import="java.util.List"%>
<%@page import="siddur.query.bean.UserInfo"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	List<Comment> answers =(List<Comment>)request.getAttribute("answers");
	List<UserInfo> customers =(List<UserInfo>)request.getAttribute("customers");
	Comment ask = (Comment)request.getAttribute("ask");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Answer-list</title>

<script src="/js/jquery.js"></script>
<script>
	function change(event){
		var target = document.getElementById("customer");
		target.disabled = !event.checked;

		if(event.checked){
			var ask = $("#ask").text();
			$("#comment").val(ask);
			$("#answer_btn").val("answer&assign");
		}else{
			$("#comment").val("");
			$("#answer_btn").val("answer");
		}
	}
	function mySubmit(){
		var txt = document.getElementById("comment").value;
		if(!txt.trim()){
			alert("Comment must not be empty.");
			return false;
		}
	}
</script>

<style>
	.content{
		font-size:16px;
	}
	.detail{
		text-indent: 5px;
		font-size:14px;
		font-style: italic;
	}
	.title{
		font-weight: bold;
	}
	.content-unit{
		padding:7px;
		background-color: #D6E9C6;
		margin:3px;
	}
	.form{
		padding-top:20px;
	}
</style>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<div class="title">Question:</div>
		<div class="content-unit">
			<div id="ask" class="content"><%= ask.content.replace("\n", "<br>") %></div>
			<div class="detail">asked by <font color="#FAA732"><%= ask.writeBy%></font> at <font color="#5BB75B"><%= ask.writeAt%></font></div>
		</div>
		<div class="title">Answers:</div>
		<% 
			for(Comment c : answers){
		%>
				<div class="content-unit">
					<div class="content">
						<%= c.content.replace("\n", "<br>")%>
					</div>
					<div class="detail">
						answered by <font color="#FAA732"><%= c.writeBy%></font> at <font color="#5BB75B"><%= c.writeAt%></font>
					</div>
				</div>
		<%
			}
		%>
		<div class="form">
			<form method="post" action="/query/query/answer">
				<input type="hidden" name="id" value="<%=ask.id%>">
				<input type="checkbox" onclick="change(this)">
				Select who to answer the question:
				<select name="customer" id="customer" disabled="disabled">
					<%
						for(UserInfo u : customers){
					%>
							<option value="<%=u.username%>"><%=u.username%></option>
					<%
						}
					%>
				</select>
				<br/>
				<textarea id="comment" name="comment" rows="6" cols="60"></textarea><br>
				<input id="answer_btn" class="btn" type="submit" onclick="return mySubmit();" value="answer"><br>
			</form>
		</div>
	</div>
</body>
</html>