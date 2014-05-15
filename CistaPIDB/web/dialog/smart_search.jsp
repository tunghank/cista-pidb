<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="com.cista.pidb.dialog.action.SmartSearch"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	List<Map<String, Object>> resultList = (List<Map<String, Object>>)request.getAttribute("result");
	SmartSearch ss = (SmartSearch) request.getAttribute("SmartSearch");
	String inputField = (String) request.getAttribute("inputField");
	String inputFieldValue = (String) request.getAttribute("inputFieldValue");
	String callbackHandle = (String) request.getAttribute("callbackHandle");
	String columns = "";
	for(String column : ss.getColumns()) {
		columns += "," + column;
	}
	if (columns.length() > 0) {
		columns = columns.substring(1);
	}
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var vHandle = "<%=callbackHandle%>";
	var vInputField = "<%=inputField%>";
	var vColumns = new Array();
	<%
		for(String column : ss.getColumns()) {
			%>
	vColumns.push('<%=column%>');
			<%
		}
	%>
	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	window.onload = init;
</script>
</head>
<body>
<form name="selectForm" action="<%=cp %>/dialog/smart_search.do?m=search" method="post">
<input type="hidden" name="name" value="<%=BeanHelper.getHtmlValueByColumn(ss, "NAME") %>">

<input type="hidden" name="table" value="<%=BeanHelper.getHtmlValueByColumn(ss, "TABLE") %>">
<input type="hidden" name="keyColumn" value="<%=BeanHelper.getHtmlValueByColumn(ss, "KEY_COLUMN") %>">
<input type="hidden" name="columns" value="<%=columns %>">

<input type="hidden" name="title" value="<%=BeanHelper.getHtmlValueByColumn(ss, "TITLE") %>">
<input type="hidden" name="mode" value="<%=BeanHelper.getHtmlValueByColumn(ss, "MODE") %>">
<input type="hidden" name="whereCause" value="<%=BeanHelper.getHtmlValueByColumn(ss, "WHERE_CAUSE") %>">
<input type="hidden" name="orderBy" value="<%=BeanHelper.getHtmlValueByColumn(ss, "ORDER_BY") %>">
<input type="hidden" name="inputField" value="<%=inputField %>">
<input type="hidden" name="callbackHandle" value="<%=callbackHandle %>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Smart Search :: <%=new String(ss.getTitle().getBytes("iso8859-1"), "utf-8") %></td>
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
					<input type="text" class="text" name="inputFieldValue" value="<%=inputFieldValue!=null?inputFieldValue:"" %>">
					<input type="submit" class="button" value="Search">
					</td>
				</tr>
			</table>
<%
	if(resultList != null && resultList.size() > 0) {
%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th width="1%">&nbsp;</th>
					<%
						if (ss != null) {
							for(String column : ss.getColumns()) {
							%>
					<th><%= column%></th>
							<%
							}
						}
					%>
				</tr>
				<%
					int idx = 0;
					for(Map item : resultList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						%>
				<tr>
					<td <%=tdcss %>>
						<%
							if ((ss != null && ss.getMode() == 0)) {
								%>
						<input type="radio" value="<%=item.get(ss.getKeyColumn()) %>" name="item">
								<%
							} else {
								%>
						<input type="checkbox" value="<%=item.get(ss.getKeyColumn()) %>" name="item">
								<%
							}
						%>
					</td>
					<%
						for(String column : ss.getColumns()) {
							%>
					<td <%=tdcss %>><%=item.get(column)!=null?item.get(column):"&nbsp;" %></td>
							<%
						}
					%>
				</tr>
						<%
					}
				%>
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
						value="OK" onclick="onSSSelect()">
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