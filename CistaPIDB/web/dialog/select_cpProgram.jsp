<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpTestProgramTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<CpTestProgramTo> toList = (List)request.getAttribute("selectList");
	String cpMaterialNum = (String) request.getAttribute("cpMaterialNum");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var callback = "<%=request.getAttribute("callback")%>";
	var cpMaterialNum = "<%=cpMaterialNum%>";
	function onSelect() {
		if (callback!="") {
			var selected = new Array();
			var all = document.getElementsByName("chkbox");
			if (all && all.length > 0) {
				for (var i=0; i<all.length; i++) {
					if (all[i].checked) {
						selected.push(all[i].value);
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
<form name="selectRes" action="<%=cp%>/dialog/select_cpProgram.do" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select CP Test Program Name</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=toList==null || toList.size()==0?"No object.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
<%
	if(toList != null && toList.size() > 0) {
%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>&nbsp;</th>
					<th>Project Code W Version</th>
					<th>CP Test Program Name</th>
				</tr>
				<%
					int idx = 0;
					for(CpTestProgramTo to : toList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						String chk = to.getProjCodeWVersion() + "|" + to.getCpTestProgName();
						%>
				<tr>
					<td <%=tdcss %>><input type="checkbox" value="<%=chk %>" name=chkbox></td>
					<td <%=tdcss %>><%=to.getProjCodeWVersion() %></td>
					<td <%=tdcss %>><%=to.getCpTestProgName() %></td>
				</tr>
						<%
					}
				%>
				</tbody>
			</table>
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
				src="<%=cp %>/images/shadow-2.gif" width="570" border="0"></td>
		</tr>
	</tbody>
</table>
<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
