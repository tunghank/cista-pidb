<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.IcWaferTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<String> icWaferList = (List)request.getAttribute("projCodeWVersionList");
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
			var selected = new Array();
			var allItem = document.getElementsByName("rad");
			if (allItem && allItem.length > 0) {
				for (var i=0; i<allItem.length; i++) {
					if (allItem[i].checked) {
						selected.push(allItem[i].value);
					}
				}
			}
			if (selected.length > 0) {
				eval("window.opener."+callback+"(selected)");
			}
		}
		window.close();
	}
</script>
</head>
<body>
<form name="selectForm" action="<%=cp %>/dialog/select_icWafer_projCodeWVersion.do" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select Project Code W Version</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=icWaferList==null || icWaferList.size()==0?"No IcWafer Project Code W Version.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
<%
	if(icWaferList != null && icWaferList.size() > 0) {
%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>&nbsp;</th>
					<th>Project Code W Version</th>
				</tr>
				<%
					int idx = 0;
					for(String icWafer : icWaferList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						if (icWafer == null ) {
							icWafer = "";
						}
						%>
				<tr>
					<td <%=tdcss %>><input type="radio" value="<%=icWafer %>" name="rad"></td>
					<td <%=tdcss %>><%=icWafer%></td>
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