<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.md.to.IcTapeTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>

<%
List<String> pkgCodeList = (List<String>) request.getAttribute("pkgCodeList");
List<String> pkgVersionList = (List<String>) request.getAttribute("pkgVersionList");
List<String> tapeWidthList = (List<String>) request.getAttribute("tapeWidthList");
IcTapeTo ref = (IcTapeTo) request.getAttribute("ref");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function selectMaterialNum() {
	var target = "<%=cp%>/dialog/select_ic_tape_material_num.do?m=list&callback=selectMaterialNumComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_ic_tape_material_num","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectMaterialNumComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('materialNum').value = selects.substring(1);
		}
	}
}

function selectTapeName() {
	var target = "<%=cp%>/dialog/select_ic_tape_name.do?m=list&callback=selectTapeNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_ic_tape_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectTapeNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('tapeName').value = selects.substring(1);
		}
	}
}

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

function selectSprocketHoleNum() {
   
	var target = "<%=cp%>/dialog/select_sprocket_hole_num.do?m=list&callback=selectSprocketHoleNumComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sprocket_hole_num","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSprocketHoleNumComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('sprocketHoleNum').value = selects.substring(1);
		}
	}
}

function selectMinPitch() {
   
	var target = "<%=cp%>/dialog/select_min_pitch.do?m=list&callback=selectMinPitchComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_min_pitch","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectMinPitchComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('minPitch').value = selects.substring(1);
		}
	}
} 
</script>
</head>
<body>
<form id="masterDataQueryForm" action="<%=cp %>/md/ic_tape_query.do?m=query" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: IC-Tape Query</td>
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
						<th width="180">Material Number</th>
						<td><input type="text" class="text" name="materialNum"
							id="materialNum" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="materialNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"materialNumSSBtn",
                                inputField:"materialNum",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"MATERIAL_NUM",
                                columns:"MATERIAL_NUM",
                                title:"Material Number",
                                mode:1
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Package Code</th>
						<td><select style="width:130px" name="pkgCode" id="pkgCode">
						<option value="">--Select--</option>
						<% 
							for (String pkgCode : pkgCodeList) {
							    out.print("<option value=\""+pkgCode+"\">"+pkgCode+"</option>");
							}
						%>
						</select></td>
					</tr>
					<tr>
						<th width="180">IC Tape Version</th>
						<td><select class="select_w130" name="pkgVersion" id="pkgVersion">
						<option value="">--Select--</option>
						<% 
							for (String pkgVersion : pkgVersionList) {
							    out.print("<option value=\""+pkgVersion+"\">"+pkgVersion+"</option>");
							}
						%>						
						</select></td>
					</tr>
					<tr>
						<th width="180">Tape Name (請加版本) </th>
						<td><input type="text" class="text" name="tapeName"
							id="tapeName" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeNameSSBtn",
                                inputField:"tapeName",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"TAPE_NAME",
                                columns:"TAPE_NAME",
                                title:"Tape Name (請加版本)",
                                mode:1
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Product Name</th>
						<td><input type="text" class="text" name="prodName"
							id="prodName" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"PROD_NAME",
                                columns:"PROD_NAME",
                                title:"Product Name",
                                mode:1
							});
							</script></td>
					</tr>
					<tr>
						<th width="180">Tape Width</th>
						<td><label> <select class="select_w130"
							name="tapeWidth" id="tapeWidth">
							<option value="">--Select--</option>
						<% 
							for (String tapeWidth : tapeWidthList) {
							    out.print("<option value=\""+tapeWidth+"\">"+tapeWidth+"</option>");
							}
						%>										
						</select></label></td>
					</tr>
					<tr>
						<th width="180">Sprocket Hole No.</th>
						<td><input type="text" class="text" name="sprocketHoleNum"
							id="sprocketHoleNum" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="sprocketHoleNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"sprocketHoleNumSSBtn",
                                inputField:"sprocketHoleNum",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"SPROCKET_HOLE_NUM",
                                columns:"SPROCKET_HOLE_NUM",
                                title:"Sprocket Hole No.",
                                mode:1
							});
							</script></td>
					</tr>
					<tr>
						<th width="180">Min. Pitch</th>
						<td><input type="text" class="text" name="minPitch"
							id="minPitch" value=""> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="minPitchSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"minPitchSSBtn",
                                inputField:"minPitch",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"MIN_PITCH",
                                columns:"MIN_PITCH",
                                title:"Min. Pitch",
                                mode:1
							});
							</script></td>
					</tr>
					<tr>
						<th width="180">Tape Customer</th>
						<% 
							SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
							SapMasterCustomerTo sapMasterCustomerTo = sapMasterCustomerDao.findByVendorCode(BeanHelper.getHtmlValueByColumn(ref, "TAPE_CUST"));
						%>
						<td><input class="text" type="text" readonly id="tapeCust" name="tapeCust"  value="<%=BeanHelper.getHtmlValueByColumn(sapMasterCustomerTo, "CUSTOMER_CODE")%>"  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeCustSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeCustSSBtn",
                                inputField:"tapeCust",
                                table:"WM_SAP_MASTER_CUSTOMER",
                                keyColumn:"CUSTOMER_CODE",
                                columns:"CUSTOMER_CODE,SHORT_NAME",
                                autoSearch:false,
                                title:"Tape Customer"
							});
							</script>
					   </td>
					</tr>
					<tr>
						<th width="180">Tape Vendor</th>
						<% 
							SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
							SapMasterVendorTo sapMasterVendorTo = sapMasterVendorDao.findByVendorCode(BeanHelper.getHtmlValueByColumn(ref, "TAPE_VENDOR"));
						%>
						<td><input class="text" type="text" readonly id="tapeVendor" name="tapeVendor"  value="<%=BeanHelper.getHtmlValueByColumn(sapMasterVendorTo, "VENDOR_CODE")%>"  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeVendorSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeVendorSSBtn",
                                inputField:"tapeVendor",
                                table:"WM_SAP_MASTER_VENDOR",
                                keyColumn:"VENDOR_CODE",
                                columns:"VENDOR_CODE,SHORT_NAME",
                                autoSearch:false,
                                title:"Tape VENDOR"
							});
							</script>
					   </td>
					</tr>
					<tr>
					<th width="180">Compay</th>
					  <td>
						<select class="select_w130" name="releaseTo" id="releaseTo">
							<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> companyList = (List<FunctionParameterTo>) request.getAttribute("companyNameList");
									for (FunctionParameterTo company : companyList) {
										String v = company.getFieldValue();
										
										%>
								<option value="<%=v %>"><%=company.getFieldShowName() %></option>
										<%
									}
								%>
						</select>				  					  
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
