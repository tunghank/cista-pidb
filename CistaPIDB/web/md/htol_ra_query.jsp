<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%
List<String> fabList = (List<String>) request.getAttribute("fabList");
List<String> optionList = (List<String>) request.getAttribute("optionList");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function selectProdName() {
   
	var target = "<%=cp%>/dialog/select_prod_name.do?m=list&callback=selectProdNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_prod_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('prodName').value = selects.substring(1);
		}
	}
}

function selectProjName() {
	var target = "<%=cp%>/dialog/select_proj_name.do?m=pre&callback=selectProjNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_proj_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('projName').value = selects.substring(1);
		}
	}
}


function selectProjCodeWVersion() {
	var target = "<%=cp%>/dialog/select_htolProjCodeWVersion.do?m=list&callback=selectProjCodeWVersionComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_projCodeWVersion","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeWVersionComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('projCodeWVersion').value = selects.substring(1);
		}
	}
}
</script>
</head>
<body>
<form id="masterDataQueryForm" action="<%=cp %>/md/htol_ra_query.do?m=query" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: HTOL RA Query&#13;</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="180"><div>Product Name&#13;</div></th>
						<td><input type="text" class="text" name="prodName"
							id="prodName" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                name:"ProductName",
                                mode:1
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180"><div>Project Name&#13;</div></th>
						<td><input type="text" class="text" name="projName"
							id="projName" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="projNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projNameSSBtn",
                                inputField:"projName",
                                name:"ProjectName",
                                mode:1
							});
							</script></td>
					</tr>
					<tr>
						<th width="180">Project Code w Version</th>
						<td><input type="text" class="text" name="projCodeWVersion"
							id="projCodeWVersion" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeWVersionSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_HTOL_RA",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                columns:"PROJ_CODE_W_VERSION",
                                title:"Project Code w Version",
                                mode:1
							});
							</script></td>
					</tr>
					<tr>
						<th width="180"><div>RA Test Result</div></th>
					  	<td><label>
							<select class="select_w130" name="raTestResult" id="raTestResult" width="150px">
								<option value="" >--Select--</option>
								<option value="OnGoing">On Going</option>
								<option value="Pass" >Pass</option>
								<option value="Fail">Fail</option>
							</select></label>					  	
					  	</td>
					</tr>
					<tr>
						<th width="180"><div>Owner</div></th>
					  	<td>
							<select class="select_w130" name="owner" id="owner">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> ownerList = (List<FunctionParameterTo>) request.getAttribute("ownerList");
									for (FunctionParameterTo owner : ownerList) {
										String v = owner.getFieldValue();
										%>
								<option value="<%=v %>"><%=owner.getFieldShowName() %></option>
										<%
									}
								%>				
						</select></td>
					</tr>
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right"><input name="button1" type="submit"
						class="button" id="button1" value="Search"> <input
						name="button2" type="Reset" class="button" id="button2"
						value="Reset"></div>
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
				src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
