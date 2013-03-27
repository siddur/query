<%@page import="siddur.query.web.tag.Paging"%>
<%@page import="siddur.query.bean.Comment"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/paging.tld" prefix="s" %>
<link rel="stylesheet" type="text/css" href="/css/tag.css" />
<style>
	.list{
		width:50%;
		margin-top:15px;
		
	}
	.content{
		font-size:16px;
		max-height: 40px;
		overflow: hidden;
	}
	.detail{
		font-size:14px;
		
	}
</style>
<script type="text/javascript" src="/js/jquery.js"></script>

<div>
<%
	Paging<Comment> paging =(Paging<Comment>)request.getAttribute("comments");
	if(paging != null){
		for(Comment c : paging.data){
	%>
			<div class="list">
				<div class="content"><a href="/query/query/detail?id=<%= c.id%>"><%= c.content%></a></div>
				<div class="detail">ask by <font color="#FAA732"><%= c.writeBy%></font> at <font color="#5BB75B"><%= c.writeAt%></font></div>
			</div>
	<%
		}
	%>
		<br/>
		<br/>
		<s:paging pageIndex="<%=paging.pageIndex %>" pageSize="<%=paging.pageSize %>" total="<%=paging.total %>"/>
		
		<script>
			var pageIndex = <%= paging.pageIndex%>;
			$(document).ready(function(){
				var spans = $(".paging span");
				spans.click(function(e){
					var span = e.currentTarget;
					var id = span.id;
					if(id == "last"){
						pageIndex += -1;
					}
					else if(id == "next"){
						pageIndex += 1;
					}
					else{
						pageIndex = id;
					}
					if(pageIndex < 0){
						pageIndex = 0
					}else if(pageIndex > spans.length - 3){
						pageIndex = spans.length - 3
					}
					$("[name='pageIndex']").val(pageIndex);
					$("form").submit();
				});
			});
		</script>
	<%
	}
%>
	
</div>
