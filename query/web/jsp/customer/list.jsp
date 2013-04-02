<%@page import="siddur.query.bean.Comment"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% 
	List<Comment> list = (List<Comment>)request.getAttribute("comments");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Questions</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script>
	function ready4add(id, target){
		$(".add").remove();
		var html = '<div class="add"><form method="post"><textarea id="comment" name="comment" rows="6" cols="60"></textarea></br><input type="submit" onclick="return mySubmit();" class="btn" value="answer"></form></div>';
		$("#" + id).parent().parent().after(html);
		$("div.add form").attr("action","/query/query/answer1?target=" + target + "&id=" + id);
	}

	function mySubmit(){
		var txt = document.getElementById("comment").value;
		if(!txt.trim()){
			alert("Comment must not be empty.");
			return false;
		}
		return true;
	}
</script>

<style>
	.list{
		width:50%;
		margin-top:15px;
		
	}
	.content{
		font-size:16px;
	}
	.detail{
		font-size:14px;
	}
</style>
</head>
<body>


<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<%
			if(list != null){
				for(Comment c : list){
			%>
					<div class="list">
						<div class="content"><a id="<%= c.id%>" href="javascript:ready4add(<%= c.id%>, <%= c.target%>)"><%= c.content.replace("\n", "<br>")%></a></div>
						<div class="detail">asked at <font color="#5BB75B"><%= c.writeAt%></font></div>
					</div>
			<%
				}
			}
		%>
	</div>
</body>
</html>