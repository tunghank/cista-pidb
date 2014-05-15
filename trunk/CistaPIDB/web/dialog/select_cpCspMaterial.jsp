<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpCspMaterialTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.CpMaterialTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<CpCspMaterialTo> cpCspList = (List)request.getAttribute("cpCspList");
	String projCodeWVersion = (String)request.getAttribute("projCodeWVersion");
	projCodeWVersion =null!=projCodeWVersion?projCodeWVersion:"";

	String message = (String)request.getAttribute("message");
	message =null!=message?message:"";
%>
<html>
<head>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<script type="text/javascript" src="<%=cp %>/js/prototype163.js"></script>
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
var callback = "<%=request.getAttribute("callback")%>";
window.onload = init;

function init() {

}

function onAddCSP(){
	if ($('cspVariant').value==""){
		alert("CP Material is must choose");
		return;
	
	}	
		
	if ($('cspDesc').value==""){
		alert("CSP Material Desc must be insert");
		return;
	}
	
	
	$("cspMaterialCreateForm").submit();
}

function onExit(){
	window.close();
}
</script>


</head>
<body onContextMenu="return false" onunload="if(history.length>0)history.go(+1);window.opener.location.reload()"> 
<form id="cspMaterialCreateForm" name="cspMaterialCreateForm" action="<%=cp%>/ajax/fetch_maxVariantCsp.do" method="post">
<input type="hidden" name="projCodeWVersion" value="<%=projCodeWVersion%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: CP CSP Modify </td>
				</tr>
			</table>

<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>Csp Material Variant:
						 <select class="select_w130" name="cspVariant" id="cspVariant">
						 <option value="">--Select--</option>
						 <%
									List<CpMaterialTo> variantList = (List<CpMaterialTo>) request.getAttribute("variantList");
									for (CpMaterialTo variant : variantList) {
										String v = variant.getCpMaterialNum();
										/*String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(variant, "CP_MATERIAL_NUM"))) {
											selected = "selected";
										}*/
										
										%>
								<option value="<%=v %>"><%=v %></option>
										<%
									}
								%>
							
							</select>
					</th>
					<th align="left">Version:
							<select class="select_w130" name="cspVersion" id="cspVersion">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> versionList = (List<FunctionParameterTo>) request.getAttribute("versionList");
									for (FunctionParameterTo version : versionList) {
										String v = version.getFieldValue();
										String selected = "";
										if (v.equals(request.getAttribute("version"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v%>" <%=selected%>><%=version.getFieldShowName()%> -> <%=v%></option>
										<%
									}
								%>
							</select>

					</th>
					<th>CSP Material Desc:
					  <input type="text" class="text" name="cspDesc" id="cspDesc" value="">
					</th>
					<th>
						<input
						name="addBtn" type="button" class="add_button" id="addBtn"
						value="Add" onclick="onAddCSP()">
					</th>
				</tr>
				</tbody>
			</table>
			<P>
			<div id="message" style="color:#FF0000">&nbsp;<%=message%></div>
			<!--EOL List Table-->
			<table border=0 cellpadding=0 cellspacing=0 width="100%">
			<tr><td>
						<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
							<tbody>
							<tr>
								<th>Cp Csp Material Num</th>
								<th>ProjectCode W version</th>
								<th>Cp Csp Desc</th>
								<th>Remark</th>
							</tr>
							</tbody>
						<%if (cpCspList != null && cpCspList.size() > 0) {%>
							<%
								int idx = 0;
								for(CpCspMaterialTo csp : cpCspList) {
									if (csp != null){
								idx ++;
								String tdcss = "class=\"c" + idx % 2+"\"";
							%>
							<tr>
							<td <%=tdcss %>><%=csp.getCpCspMaterialNum()%>&nbsp;
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(csp, "PROJECT_CODE_W_VERSION") %>&nbsp;
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(csp, "DESCRIPTION") %>&nbsp;
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(csp, "REMARK") %>&nbsp;
							</td>
							</tr>
							<%
								}
							}
							%>
						
						</table>
				</td>
			</tr>
			</table>
	</td>
</tr>
</table>

<%
	}
%>
			<table class="formErrorAndButton" align="right">
				<tr>
					<td>
					<div >
					  <input
						name="okBtn" type="button" class="button" id="okBtn"
						value="OK" onclick="onExit()">
					 <!-- <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="window.close()">-->
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
