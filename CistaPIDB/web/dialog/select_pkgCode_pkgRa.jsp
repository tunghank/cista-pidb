<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.cista.pidb.dialog.action.SmartSearch"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	List<String> resultList = (List<String>)request.getAttribute("selectList");
	String inputValue = (String) request.getAttribute("inputValue");
	String callback = (String) request.getAttribute("callback");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var callback = "<%=request.getAttribute("callback")%>";
	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	window.onload = init;
	function onSelect() {
		if (callback!="") {
			var selectedSmc;
			var allUser = document.getElementsByName("rad");
			if (allUser && allUser.length > 0) {
				for (var i=0; i<allUser.length; i++) {
					if (allUser[i].checked) {
						selectedSmc = allUser[i].value;
					}
				}
			}
			if (selectedSmc) {
				eval("window.opener."+callback+"(selectedSmc)");
			}
		}
		window.close();
	}
</script>
</head>
<body>
<form name="selectForm" action="<%=cp %>/dialog/select_PkgCode_PkgRa.do?m=lpc" method="post">
<input type="hidden" name="callback" value="<%=callback %>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Smart Search :: Package Code</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=resultList==null || resultList.size()==0?"No result found.":"" %>&nbsp;</div>
					</td>
				</tr>
				<tr>
					<td>
					<input type="text" class="text" name="in" value="<%= inputValue!=null?inputValue:"" %>">
					<input type="submit" class="button" value="Search">
					</td>
				</tr>
			</table>
<%
	if(resultList != null && resultList.size() > 0) {%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th width="1%">&nbsp;</th>					
					<th>Package Code</th>
				</tr>
				<%
					int idx = 0;
					for(String item : resultList) {
						if (item!= null) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						%>
				<tr>
					<td <%=tdcss %>><input type="radio" value="<%=item%>" name="rad"></td>
					<td <%=tdcss %>><%=item %></td>
				</tr>
					<%}} %>
				</tbody>
			</table>
</div>
	</td>
</tr>
</table>
<%
	}
%>
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div align="right">
					  <input
						name="okBtn" type="button" class="button" id="okBtn"
						value="OK" onclick="onSelect()">
					  <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="window.close()">
					</div>
					</td>
				</tr>
			</table>
			</div>
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
				src="<%=cp %>/images/shadow-2.gif" width="585" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>