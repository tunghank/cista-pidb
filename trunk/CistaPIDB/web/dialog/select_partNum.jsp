<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.IcFgTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<IcFgTo> smcList = (List)request.getAttribute("selectList");
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
			var allUser = document.getElementsByName("SmcCheckbox");
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
<form name="selectSmc" action="<%=cp %>/dialog/select_partNum.do" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select Part Number</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=smcList==null || smcList.size()==0?"No smc.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
<%
	if(smcList != null && smcList.size() > 0) {
%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>&nbsp;</th>
					<th>Part Number</th>
					<th>IC FG Material Number</th>
					<th>Product Code</th>
					<th>Package Code</th>
					<th>Package Type</th>
				</tr>
				<%
					int idx = 0;
					for(IcFgTo smc : smcList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						if (smc == null ) {
							break;
						}
						String userStr = smc.getPartNum()+"|"+smc.getMaterialNum()+"|"+smc.getProdCode()+"|"+smc.getPkgCode()+"|"+smc.getPkgType();
						String partNum = ((smc.getPartNum() == null) ? "" : smc.getPartNum());
						String materialNum = ((smc.getMaterialNum() == null) ? "" : smc.getMaterialNum());
						String prodCode = ((smc.getProdCode() == null) ? "" : smc.getProdCode());
						String pkgCode = ((smc.getPkgCode() == null) ? "" : smc.getPkgCode());
						String pkgType = ((smc.getPkgType() == null) ? "" : smc.getPkgType());
						%>
				<tr>
					<td <%=tdcss %>><input type="radio" value="<%=userStr %>" name=SmcCheckbox></td>
					<td <%=tdcss %>><%=partNum %></td>
					<td <%=tdcss %>><%=materialNum %></td>
					<td <%=tdcss %>><%=prodCode %></td>
					<td <%=tdcss %>><%=pkgCode %></td>
					<td <%=tdcss %>><%=pkgType%></td>
				</tr>
						<%
					}
				%>
				</tbody>
			</table></div>
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
