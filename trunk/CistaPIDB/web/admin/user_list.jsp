<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	UserTo queryTo = (UserTo)request.getAttribute("queryTo");
	List<UserTo> result = (List<UserTo>) request.getAttribute("result");
	
	boolean noQuery = true;
	if(queryTo!=null) {
	    noQuery = false;
	}
	
	String queryStatusMsg = (String)request.getAttribute("statusMsg");;
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function init() {
		autoFitBottomArea('resultPanel', 180);
	}
	window.onload = init;
	
	function deleteUser() {
		if (confirm("Are you sure delete selected user?")) {
			$('userQuery').action="<%=cp %>/admin/user_list.do?m=delete";
			$('userQuery').submit();
		}
	}
	
	function updateUser() {
		$('userQuery').action="<%=cp %>/admin/user_edit.do?m=pre";
		$('userQuery').submit();	
	}
</script>
</head>
<body>
			<form name="userQuery" action="<%=cp %>/admin/user_list.do?m=query" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: User management</td>
  </tr>
</table>
			<div class="content">
			<table border="0" cellpadding="0" cellspacing="0" class="segmentHeader" width="100%">
				<tr>
					<td style="font-size: 12px;">
					<a href="#" onclick="showHidePanel('queryCriteriaPanel');init()">User Search</a>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="searchBtn" type="submit" class="button" id="searchBtn"
						value="Search">
					  <input
						name="createBtn" type="button" class="button" id="createBtn"
						value="Create" onclick="document.location.href='<%=cp %>/admin/user_create.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table id="queryCriteriaPanel" class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180">User ID </th>
					  <td>
							<input name="userId" id="userId" type="text" size="20" class="text" value="<%=BeanHelper.getHtmlValueByColumn(queryTo, "USER_ID") %>">
					  </td>
					</tr>
					<tr>
						<th width="180">First Name</th>
					  <td>
							<input name="firstName" id="firstName" type="text" size="20" class="text" value="<%=BeanHelper.getHtmlValueByColumn(queryTo, "FIRST_NAME") %>">
					  </td>
					</tr>
					<tr>
					  <th width="180">Last Name </th>
					  <td>
							<input name="lastName" id="lastName" type="text" size="20" class="text" value="<%=BeanHelper.getHtmlValueByColumn(queryTo, "LAST_NAME") %>">
					  </td>
					</tr>
				</tbody>
			</table>
<%
if(!noQuery) {
%>
<div class="segmentHeader"><%=queryStatusMsg%> </div>
<%if(result!=null && result.size() > 0) { %>
<table class="formErrorAndButton">
	<tr>
		<td>
		<div class="formErrorMsg" id="error"></div>
		</td>
		<td>
		<div align="right">
		  <input
			name="updateBtn" type="button" class="button" id="updateBtn"
			value="Update User" onclick="updateUser()">
		  <input
			name="deleteBtn" type="button" class="button" id="deleteBtn"
			value="Delete User" onclick="deleteUser()">
		</div>
		</td>
	</tr>
</table>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th style="width:20px">&nbsp;</th>
					<th style="text-align:center">User ID</th>
					<th style="text-align:center">First Name</th>
					<th style="text-align:center">Last Name</th>
					<th style="text-align:center">E-Mail</th>
					<th style="text-align:center">Status</th>
				</tr>
<%
	    boolean firstRow = true;
		int idx = 0;
	    for(UserTo user : result) {
			idx ++;
			String tdcss = "class=\"c" + idx % 2+"\"";
	        String checked = "";
	        if (firstRow) {
	            checked = "checked";
	            firstRow = false;
	        }
%>
	        	<tr>
					<td <%=tdcss %>><input type="radio" name="selectedUser" id="selectedUser" value="<%=BeanHelper.getHtmlValueByColumn(user, "ID") %>" <%=checked %>></td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(user, "USER_ID") %>&nbsp;</td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(user, "FIRST_NAME") %>&nbsp;</td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(user, "LAST_NAME") %>&nbsp;</td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(user, "EMAIL") %>&nbsp;</td>
					<td <%=tdcss %>><%=user.getActive()?"Active":"Inactive" %>&nbsp;</td>
				</tr>
	        <%
	    }
%>
				</tbody>
			</table>
</div>
</td></tr></table>
<%} %>
			</div>
<%} %>
			<!-- Content end --></td>
			<td width="5" valign="bottom"
				background="<%=cp %>/images/shadow-1.gif">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				background="<%=cp %>/images/bgs.gif">
				<tr>
					<td height="15"><img src="<%=cp %>/images/spacer.gif"
						width="1" height="1" alt=""></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="2"><img height="2" alt=""
				src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
