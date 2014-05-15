<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.FtTestProgramTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">

function selectPartNum() {
	var target = "<%=cp%>/dialog/select_partNum_checkbox.do?m=list&callback=selectPartNumComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_partNum","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectPartNumComplete(selectedItems) {
	
  if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('partNum').value = selects.substring(1);
		}
	}
}

function selectProdCode() {
	var target = "<%=cp%>/dialog/select_icFgProdCode.do?m=list&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icFgProdCode","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedItems) {
	$('prodCode').value = selectedItems;
}

function selectFtTestProgName() {
	var target = "<%=cp%>/dialog/select_ft_test_prog_name_cb.do?m=list&callback=selectFtTestProgNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cp_test_prog_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectFtTestProgNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('ftTestProgName').value = selects.substring(1);
		}
	}
}
</script>
</head>
<body>
<form id="ftTestProgQueryForm" action="<%=cp %>/md/ft_test_prog_query.do?m=query" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle"><div>Master Data :: FT Test Program Query</div>
	</td>
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
						<th width="180"><div>FT Material Num.</div></th>
					  <td><input class="text" type="text" id="ftMaterialNum" name="ftMaterialNum">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="ftMaterialNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"ftMaterialNumSSBtn",
                                inputField:"ftMaterialNum",
								title:"FT Material Number",
                                table:"PIDB_IC_FG",
                                keyColumn:"MATERIAL_NUM",
                                mode:1
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Part Number</th>
					    <td><input class="text" name="partNum" id="partNum" >
					         <img src="<%=cp%>/images/lov.gif" alt="LOV" id="partNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"partNumSSBtn",
                                inputField:"partNum",
                                table:"PIDB_FT_TEST_PROGRAM",
                                keyColumn:"PART_NUM",
                                title:"Part Number",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th>Product Code</th>
					    <td><input class="text" name="prodCode" id="prodCode">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeBtn",
                                inputField:"prodCode",
                                table:"PIDB_IC_FG",
                                keyColumn:"PROD_CODE",
                                title:"Product Code",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th>FT Test Program Name</th>
					    <td><input class="text" name="ftTestProgName" id="ftTestProgName">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="ftTestProgNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"ftTestProgNameSSBtn",
                                inputField:"ftTestProgName",
                                table:"PIDB_FT_TEST_PROGRAM",
                                keyColumn:"FT_TEST_PROG_NAME",
                                title:"FT Test Program Name",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th>FT Test Program Revision</th>
						<td><select name="ftTestProgRevision" id="ftTestProgRevision" style="width:130px">
                          <option value="">--Select--</option>
							<%
								List<String> ftTestProgRevisionList = (List<String>) request.getAttribute("ftTestProgRevisionList");
								for(String option : ftTestProgRevisionList) {
							%>
							<option value="<%=option %>"><%=(option==null)?"":option %></option>
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