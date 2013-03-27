<%@page import="siddur.query.bean.Comment"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	String comment = (String)request.getAttribute("comment");
	if(comment == null){
		comment = "";
	}
	boolean hasMsg = request.getAttribute("msg") != null;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ask</title>

<script type="text/javascript">
	function mySubmit(){
		var txt = document.getElementById("comment").value;
		if(!txt.trim()){
			alert("Comment must not be empty.");
			return false;
		}
	}
</script>
</head>
<body>
	<%@include file="/jsp/common/head.jsp" %>
	<div class="body">
		<form method="post" action="/query/query/ask">
			<div>Question:</div>
			<textarea name="comment" id="comment" rows="6" cols="60"><%=comment%></textarea>
			<br>
			<input type="hidden" name="ask">
			<input type="submit" class="btn" value='<%= hasMsg?"still ask" : "ask"%>' onclick="return mySubmit();" name="type">
			<input type="button" class="btn" value="cancel" onclick="window.location='/query'">
		</form>
		
		<h4><%= hasMsg? "Here lists some similar quetions:" : ""%></h4>
		<%@include file="/jsp/common/search-list.jsp" %>
	</div>
</body>
</html>