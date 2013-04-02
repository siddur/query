<%@page import="java.util.List"%>
<%@page import="siddur.query.bean.Comment"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/query.tld" prefix="s" %>
<% 
	List<Comment> list = (List<Comment>)request.getAttribute("comments");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Questions</title>
<style>
	.list{
		margin-top:15px;
		border-bottom: solid 1px #FCF8E3;
		width:800px;
	}
	.content{
		font-size:16px;
		max-height: 40px;
		overflow: hidden;
	}
	.crumb{
		padding:5px;
		text-indent:10px;
		background-color:#FCF8E3;
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

<%@include file="/jsp/common/my.jsp" %>
	<div class="crumb">
		<%= request.getAttribute("crumb")%>
	</div>
	<div class="body">
	<%
		for(Comment c : list){
	%>
			<div class="list">
				<div style="float:right; margin-top:5px;">
				<% if(request.getAttribute("crumb").toString().endsWith("questions") && c.target==-1){ %>
					<a href="/query/query/closequestion?id=<%= c.id%>">close</a>
				<%} %>
				</div>
				<div class="content">[<%= c.target==0 ? "close" : "open"%>]<a href="/query/query/detail?id=<%= c.id%>"><%= c.content%></a></div>
				<s:commentDetail comment="<%=c %>"/>
			</div>
	<%
		}
	%>
	</div>
</body>
</html>