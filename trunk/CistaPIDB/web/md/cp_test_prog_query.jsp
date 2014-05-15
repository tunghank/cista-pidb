<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.md.to.ProjectCodeTo"%>
<%@ page import="com.cista.pidb.md.to.CpTestProgramTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">

function selectProdCode() {
	var target = "<%=cp%>/dialog/select_product2.do?m=pre&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product2","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		var prods = "";
		for(var i=0; i<selectedProds.length; i++) {
			prods += "," + selectedProds[i];
		}
		if (prods.length > 0) {
			$('prodCode').value = prods.substring(1);
		}
	}
}
function selectProdName() {
	var target = "<%=cp%>/dialog/select_prod_name.do?m=list&callback=selectProdNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product2","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdNameComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		var prods = "";
		for(var i=0; i<selectedProds.length; i++) {
			prods += "," + selectedProds[i];
		}
		if (prods.length > 0) {
			$('prodName').value = prods.substring(1);
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
	var target = "<%=cp%>/dialog/select_icWafer_projCodeWVersion.do?m=pre&callback=selectProjCodeWVersionComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icWafer_projCodeWVersion","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
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

function selectCpTestProgName() {
	var target = "<%=cp%>/dialog/select_cp_test_prog_name_cb.do?m=list&callback=selectCpTestProgNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cp_test_prog_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectCpTestProgNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('cpTestProgName').value = selects.substring(1);
		}
	}
}
</script>
</head>
<body>
<form id="cpTestProgQueryForm" action="<%=cp %>/md/cp_test_prog_query.do?m=query" method="post">	
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: CP Test Program Query</td>
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
						<th width="180"><div>CP Material Num.</div></th>
					  <td><input class="text" type="text" id="cpMaterialNum" name="cpMaterialNum">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="cpMaterialNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"cpMaterialNumSSBtn",
                                inputField:"cpMaterialNum",
								title:"CP Material Number",
                                table:"PIDB_CP_MATERIAL",
                                keyColumn:"CP_MATERIAL_NUM",
                                mode:1
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180"><div>Product Code</div></th>
					  <td><input class="text" type="text" id="prodCode" name="prodCode">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
                                name:"ProductCode",
                                mode:0
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Product Name</th>
					  	<td><input class="text" type="text" id="prodName" name="prodName"  >
                        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                name:"ProductName",
                                mode:0
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Project Name</th>
					  	<td><input class="text" type="text" id="projName" name="projName" >
                       <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projNameSSBtn",
                                inputField:"projName",
                                name:"ProjectName",
                                title:"Project Name",
                                mode:1
							});
						
							</script>
							</td>
					</tr>					
					<tr>
						<th width="180"><div>Fab/Fab Description </div></th>
					  	<td><select id="fab" name="fab" class="select_w130">
					  		<option value="">--Select--</option>
							<%
								List<FabCodeTo> fabCodeList = (List<FabCodeTo>) request.getAttribute("fabCodeList");
								for(FabCodeTo fab : fabCodeList) {
							%>
							<option value="<%=fab.getFab() %>"><%=fab.getFab() %>,<%=fab.getFabDescr() %></option>
							<%
								}
							%>
					  		</select> </td>
					</tr>
					<tr>
						<th width="180"><div>Option</div></th>
					  <td><select id="projOption" name="projOption" class="select_w130">
					  <option value="">--Select--</option>
							<%
								List<String> projectCodeList = (List<String>) request.getAttribute("optionList");
								for(String option : projectCodeList) {
									if(option!=null && option.length()>0) {
							%>
							<option value="<%=option %>"><%=(option==null)?"":option %></option>
							<%
									}
								}
							%>
					  	</select></td>
					</tr>
					<tr>
						<th width="180">Project Code w Version</th>
					  <td><input class="text" type="text" id="projCodeWVersion" name="projCodeWVersion" >
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeWVersionSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_CP_TEST_PROGRAM",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                title:"Project Code w Version",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">CP Test Program Name</th>
					  <td><input class="text" type="text" id="cpTestProgName" name="cpTestProgName" >
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="cpTestProgNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"cpTestProgNameSSBtn",
                                inputField:"cpTestProgName",
                                table:"PIDB_CP_TEST_PROGRAM",
                                keyColumn:"CP_TEST_PROG_NAME",
                                title:"CP Test Program Name",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180"><div>CP Test Program Revision</div></th>
					  <td><select id="cpTestProgRevision" name="cpTestProgRevision" class="select_w130">
					  <option value="">--Select--</option>
							<%
								List<String> cpTestProgRevisionList = (List<String>) request.getAttribute("cpTestProgRevisionList");
								for(String option : cpTestProgRevisionList) {
							%>
							<option value="<%=option %>"><%=(option==null)?"":option %></option>
							<%
								}
							%>
					  	</select></td>
					</tr>
					<tr>
						<th width="180">Vendor Code</th>
						<td><input class="text" type="text" readonly id="vendorCode" name="vendorCode" width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="vendorCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"vendorCodeSSBtn",
                                inputField:"vendorCode",
                                table:"WM_SAP_MASTER_VENDOR",
                                keyColumn:"VENDOR_CODE",
                                columns:"SHORT_NAME,VENDOR_CODE",
                                autoSearch:false,
                                title:"VENDOR Code"
							});
							</script>
					   </td>
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