<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.cista.pidb.code.dao.MpCustDao"%>
<%@ page import="com.cista.pidb.code.to.MpCustTo"%>

<%@ include file="/common/global.jsp"%>
<%
	MpCustDao mpCustDao = new MpCustDao();
	List<MpCustTo> mpCustList = mpCustDao.findAll();
	
	/*String str = "Arch,Boe-Hydis,CASIO,Ceramate,DI,Dotstech,Funai,FUNAI HK,Cista,Honeywell,Hydis,IDT,InnoLux,ITOCHU,LG Innotek,LOI,LPL,MDTI,NEC";
	str += ",NextGen,Optrex,Orange,Pan.,Perfect,Pochun,QDI,Samsung,Sanyo Epson,SEC,SHARP,Shinden,SVA,SVA-LCD,SVA-NEC,SVA-O,Toppoly,Topsun,Top-sun,TOVIS,Waxnic,Yokogawa";
	str += ",力盛,久正,元太,友達,台盛,台灣表面,光碁,奇美,奇菱,國喬,眾福,勝華,富相,晶采,晶采光電,華映,盟訓,緯晶,翰宇,興益,環電,豐裕,瀚宇,寶成";
	
	
	String[] strs = str.split(",");*/
	List<String> smcList = new ArrayList<String>();
	
	for (MpCustTo o : mpCustList) {
	    smcList.add(o.getCustCode());
	}
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var callback = "<%=request.getParameter("callback")%>";
	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	window.onload = init;
	function onSelect() {
		if (callback!="") {
			var selectedSmc = new Array();
			var allUser = document.getElementsByName("SmcCheckbox");
			if (allUser && allUser.length > 0) {
				for (var i=0; i<allUser.length; i++) {
					if (allUser[i].checked) {
						selectedSmc.push(allUser[i].value);
					}
				}
			}	
			//if (selectedSmc.length > 0) {
				eval("window.opener."+callback+"(selectedSmc)");
			//}
		}
		window.close();
	}
	
</script>
</head>
<body>
<form name="selectSmc" action="<%=cp %>/dialog/select_sapMasterCustomer.do?m=list" method="post">
<input type="hidden" name="callback" value="<%=request.getParameter("callback")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select Customer</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=smcList==null || smcList.size()==0?"No Customer.":"" %>&nbsp;</div>
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
					<th>Customer</th>
				</tr>
				<%
					int idx = 0;
					for(String smc : smcList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						if (smc == null) {
							break;
						}
						%>
				<tr>
					<td <%=tdcss %>><input type="checkbox" value="<%=smc %>" name=SmcCheckbox></td>
					<td <%=tdcss %>><%=(smc==null)? "" : smc %></td>
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
