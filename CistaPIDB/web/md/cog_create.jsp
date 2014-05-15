<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.CogTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	CogTo ref = (CogTo) request.getAttribute("ref");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function doSave() {
	if ($F('prodCode') == "") {
	
		setMessage("error", "Product code is must field.");
		return;
	}
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	
	if ($F('traySize') == "") {
	
		setMessage("error", "Tray Size is must field.");
		return;
	}
	if ($F('trayDrawingNo1') == "") {
		setMessage("error", "Tray Drawing No. (1) is must field.");
		return;
	}
	if ($F('trayDrawingNoVer1') == "") {
		setMessage("error", "Tray Drawing No. Ver. (1) is must field.");
		return;
	}
	
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking cog exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_cog_exist.do',
		{
			method: 'get',			
			parameters: 'pkgCode='+ $F('pkgCode') + '&prodCode='+ $F('prodCode'),			
			onComplete: checkCogExistComplete
		}
	);
}

function checkCogExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "Project name is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {		
			setMessage("error", "Saving project...");
			selectAllOptions($('assignTo'));			
			$('cogCreateForm').action = $('cogCreateForm').action + "?m=save";
			$('cogCreateForm').submit();
	}
}
function retrieveEmail() {
	$('retrieveBtn').disabled = true;
	setMessage("error", "Retrieving assign emails...");
	new Ajax.Request(
		'<%=cp%>/ajax/fetch_email.do',
		{
			method: 'get',
			parameters: 'users='+ $F('assignTo'),
			onComplete: retrieveEmailComplete
		}
	);
}
function retrieveEmailComplete(r) {
	var returnValue = r.responseText.split("|");
	var errorItem = "";
	var successItem = "";
	if (returnValue.length == 1) {
		if (r.responseText.startWith("|")) {
			successItem = returnValue[0];
		} else {
			errorItem = returnValue[0];
		}
	} else {
		errorItem = returnValue[0];
		successItem = returnValue[1];
	}
	if (errorItem.length > 0) {
		setMessage("error", "The following user's email not found: " + errorItem + ".");
	} else {
		setMessage("error", "Retrieve user's email success. ");
	}
	
	$('assignEmail').value = successItem;
	$('retrieveBtn').disabled = false;
}


</script>
</head>
<body>
<form name="cogCreateForm" action="<%=cp %>/md/cog_create.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Create COG</td>					
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />
                         <%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
                         &nbsp;
					</div>
					</td>
					<td>
					<div align="right">
					Created by <input name="createdBy" id="createdBy" type="text" class="text_protected" readonly>&nbsp;&nbsp;
					Modified by <input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly>&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><span class="star">*</span>Product Code</th>
						<td width="30%"><input type="text" class="text_required" readonly name="prodCode" id="prodCode">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
                                name:"ProductCode",
                                autoSearch:false,
                                mode:0
							});
						
							</script>							</td>
						<td width="50%" colspan="2" rowspan="4">
						   <table class="formFull" border="1" cellpadding="1" cellspacing="1">
						    <tr><td>
						    <table class="formFull" border="0" cellpadding="1" cellspacing="1">
						      <tr>
                                <th width="200"><span class="star">*</span>Tray Drawing No. (1)</th>
						        <td><input  type="text" class="text_required" name="trayDrawingNo1" id="trayDrawingNo1"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO1") %>"></td>
					          </tr>
						      <tr>
                                <th width="200"><span class="star">*</span>Tray Drawing No. Ver. (1)</th>
						        <td><input type="text" class="text_required" name="trayDrawingNoVer1" id="trayDrawingNoVer1"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO_VER1") %>"></td>
					          </tr>
						      <tr>
                                <th width="200">Color / Pocket Qty (請以 / 區分) (1)</th>
						        <td><input type="text" class="text"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR1") %>"
							name="color1" id="color1"></td>
					          </tr>
						      <tr>
                                <th width="200">COG Customer Name (1)</th>
						        <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COG_CUST_NAME1") %>"
							name="cogCustName1" id="cogCustName1" ></td>
					          </tr>
						     </table>
							 </td></tr></table>						</td>
					</tr>
					<tr>
						<th><span class="star">*</span>Package Code</th>
						<td><input type="text" class="text_required" name="pkgCode" id="pkgCode" maxlength="6"></td>
					</tr>
					<tr>
						<th><span class="star">*</span>Tray Size</th>
						<td><select name="traySize" id="traySize"  class="select_w130_required">
						    <option value="">--Select--</option>
							<option value="2''" <%="2''".equals(BeanHelper.getHtmlValueByColumn(ref, "TRAY_SIZE"))?"selected":"" %>>2''</option>
							<option value="3''" <%="3''".equals(BeanHelper.getHtmlValueByColumn(ref, "TRAY_SIZE"))?"selected":"" %>>3''</option>
							<option value="4''" <%="4''".equals(BeanHelper.getHtmlValueByColumn(ref, "TRAY_SIZE"))?"selected":"" %>>4''</option>
						</select></td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>Vendor</th>
						<td><select name="vendor" id="vendor"  class="select_w130_required">
						        <option value="">--Select--</option>
								<option value="利機" <%="利機".equals(BeanHelper.getHtmlValueByColumn(ref, "VENDOR"))?"selected":"" %>>利機</option>
								<option value="樺塑" <%="樺塑".equals(BeanHelper.getHtmlValueByColumn(ref, "VENDOR"))?"selected":"" %>>樺塑</option>
								<option value="原津" <%="原津".equals(BeanHelper.getHtmlValueByColumn(ref, "VENDOR"))?"selected":"" %>>原津</option>
								<option value="Megic" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "VENDOR"))?"selected":"" %>>Megic</option>
						</select></label></td>
						 
						<th colspan="2" rowspan="4">
						<table class="formFull" border="1" cellpadding="1" cellspacing="1">
                          <tr>
                            <td><table class="formFull" border="0" cellpadding="1" cellspacing="1">
                                <tr>
                                  <th width="200">Tray Drawing No. (2)</th>
                                  <td><input type="text" class="text"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO2") %>"
							name="trayDrawingNo2" id="trayDrawingNo2" ></td>
                                </tr>
                                <tr>
                                  <th width="200">Tray Drawing No. Ver. (2)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO_VER2") %>"
							name="trayDrawingNoVer2" id="trayDrawingNoVer2"></td>
                                </tr>
                                <tr>
                                  <th width="200">Color / Pocket Qty (請以 / 區分) (2)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR2") %>"
							name="color2" id="color2"></td>
                                </tr>
                                <tr>
                                  <th width="200">COG Customer Name (2)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COG_CUST_NAME2") %>"
							name="cogCustName2" id="cogCustName2" ></td>
                                </tr>
                                
                            </table></td>
                          </tr>
					    </table></th>
					</tr>
					<tr>
						<th>Drawing Approve Date</th>
						<td><input type="text" class="text" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "DRAWING_APPROVE_DATE", "yyyy/MM/dd") %>"
							name="drawingApproveDate" id="drawingApproveDate"> <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" name="drawingApproveDateBtn" id="drawingApproveDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"drawingApproveDate",
								ifFormat:"%Y/%m/%d",
								button:"drawingApproveDateBtn"
							});
						</script></td>
					</tr>
					<tr>
						<th>Sample Ready Date (Plan)</th>
						<td><input type="text" class="text" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "SAMPLE_READY_DATE", "yyyy/MM/dd") %>"
							name="sampleReadyDate" id="sampleReadyDate">
							 <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" name="sampleReadyDateBtn" id="sampleReadyDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"sampleReadyDate",
								ifFormat:"%Y/%m/%d",
								button:"sampleReadyDateBtn"
							});
						</script></td>
					</tr>

					<tr>
						<th>Assembly Subcon 1</th>                                     
						<td><select name="assySubcon1" 
							id="assySubcon1"  class="select_w130">
							<option value="">--Select--</option>
							<option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>ChipBond</option>
							<option value="ChipMORE" <%="ChipMORE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>ChipMORE</option>
							<option value="ChipMOS" <%="ChipMOS".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>ChipMOS</option>
							<option value="ChipMOS(SH)" <%="ChipMOS(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>ChipMOS(SH)</option>
							<option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>SPIL</option>
							<option value="SPIl(SH)" <%="SPIl(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>SPIl(SH)</option>
							<option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>IST</option>
							<option value="Megic" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>Megic</option>
							<option value="KYEC" <%="KYEC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON1"))?"selected":"" %>>KYEC</option>
						</select></td>
					</tr>

					<tr>
						<th>Assembly Subcon 2</th>
						<td> <select name="assySubcon2" id="assySubcon2"  class="select_w130">
						    <option value="">--Select--</option>
							<option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>ChipBond</option>
							<option value="ChipMORE" <%="ChipMORE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>ChipMORE</option>
							<option value="ChipMOS" <%="ChipMOS".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>ChipMOS</option>
							<option value="ChipMOS(SH)" <%="ChipMOS(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>ChipMOS(SH)</option>
							<option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>SPIL</option>
							<option value="SPIl(SH)" <%="SPIl(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>SPIl(SH)</option>
							<option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>IST</option>
							<option value="Megic" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>Megic</option>
							<option value="KYEC" <%="KYEC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON2"))?"selected":"" %>>KYEC</option>
						</select> </td>
						<th colspan="2" rowspan="4"><table class="formFull" border="1" cellpadding="1" cellspacing="1">
                          <tr>
                            <td><table class="formFull" border="0" cellpadding="1" cellspacing="1">
                                <tr>
                                  <th width="200">Tray Drawing No. (3)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO3") %>"
							name="trayDrawingNo3" id="trayDrawingNo3"></td>
                                </tr>
                                <tr>
                                  <th width="200">Tray Drawing No. Ver. (3)</th>
                                  <td><input type="text" class="text"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO_VER3") %>"
							name="trayDrawingNoVer3" id="trayDrawingNoVer3" ></td>
                                </tr>
                                <tr>
                                  <th width="200">Color / Pocket Qty (請以 / 區分) (3)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR3") %>"
							name="color3" id="color3"></td>
                                </tr>
                                <tr>
                                  <th width="200">COG Customer Name (3)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COG_CUST_NAME3") %>"
							name="cogCustName3" id="cogCustName3" ></td>
                                </tr>
                                
                            </table></td>
                          </tr>
                        </table></th>
					</tr>

					<tr>
						<th>Assembly Subcon 3</th>
						<td><select name="assySubcon3" id="assySubcon3" class="select_w130">
						    <option value="">--Select--</option>
							<option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>ChipBond</option>
							<option value="ChipMORE" <%="ChipMORE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>ChipMORE</option>
							<option value="ChipMOS" <%="ChipMOS".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>ChipMOS</option>
							<option value="ChipMOS(SH)" <%="ChipMOS(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>ChipMOS(SH)</option>
							<option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>SPIL</option>
							<option value="SPIl(SH)" <%="SPIl(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>SPIl(SH)</option>
							<option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>IST</option>
							<option value="Megic" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>Megic</option>
							<option value="KYEC" <%="KYEC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON3"))?"selected":"" %>>KYEC</option>
						</select></td>
					</tr>

					<tr>
						<th>Assembly Subcon 4</th>
						<td><select name="assySubcon4"
							id="assySubcon4" class="select_w130">
							<option value="">--Select--</option>
							<option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>ChipBond</option>
							<option value="ChipMORE" <%="ChipMORE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>ChipMORE</option>
							<option value="ChipMOS" <%="ChipMOS".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>ChipMOS</option>
							<option value="ChipMOS(SH)" <%="ChipMOS(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>ChipMOS(SH)</option>
							<option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>SPIL</option>
							<option value="SPIl(SH)" <%="SPIl(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>SPIl(SH)</option>
							<option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>IST</option>
							<option value="Megic" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>Megic</option>
							<option value="KYEC" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON4"))?"selected":"" %>>KYEC</option>
						</select></td>
					</tr>

					<tr>
						<th>Assembly Subcon 5</th>
						<td><select name="assySubcon5"
							id="assySubcon5"  class="select_w130">
							<option value="">--Select--</option>
							<option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>ChipBond</option>
							<option value="ChipMORE" <%="ChipMORE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>ChipMORE</option>
							<option value="ChipMOS" <%="ChipMOS".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>ChipMOS</option>
							<option value="ChipMOS(SH)" <%="ChipMOS(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>ChipMOS(SH)</option>
							<option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>SPIL</option>
							<option value="SPIl(SH)" <%="SPIl(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>SPIl(SH)</option>
							<option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>IST</option>
							<option value="Megic" <%="Megic".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>Megic</option>
							<option value="KYEC" <%="KYEC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_SUBCON5"))?"selected":"" %>>KYEC</option>
						</select></td>
					</tr>

					<tr>
						<th>Chip Size(Including Sealring S/L)</th>
						<td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CHIP_SIZE") %>"
							name="chipSize" id="chipSize"></td>
						<th colspan="2" rowspan="3"><table class="formFull" border="1" cellpadding="1" cellspacing="1">
                          <tr>
                            <td><table class="formFull" border="0" cellpadding="1" cellspacing="1">
                                <tr>
                                  <th width="200">Tray Drawing No. (4)</th>
                                  <td><input  type="text" class="text" name="trayDrawingNo4" id="trayDrawingNo4"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO4") %>"></td>
                                </tr>
                                <tr>
                                  <th width="200">Tray Drawing No. Ver. (4)</th>
                                  <td><input type="text" class="text" name="trayDrawingNoVer4" id="trayDrawingNoVer4"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO_VER4") %>"></td>
                                </tr>
                                <tr>
                                  <th width="200">Color / Pocket Qty (請以 / 區分) (4)</th>
                                  <td><input type="text" class="text"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR4") %>"
							name="color4" id="color4"></td>
                                </tr>
                                <tr>
                                  <th width="200">COG Customer Name (4)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COG_CUST_NAME4") %>"
							name="cogCustName4" id="cogCustName4" ></td>
                                </tr>
                                
                            </table></td>
                          </tr>
                        </table></th>
					</tr>

					<tr>
						<th>Cavity Size</th>
						<td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CAVITY_SIZE") %>"
							name="cavitySize" id="cavitySize">						</td>
					</tr>
					<tr>
					  <th>AssignTo</th>
					  <td><table border="0" cellspace="0" cellpadding="0" margin="0">
                        <tr>
                          <td rowspan="2">
					  	<select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO");
					    			String[] list = listStr.split(",");
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			%>
						    			<option value="<%=s %>"><%=s %></option>
						    			<%
						    			}
						    		}
					    		}
					    	%>					  	
					  	</select>								
							</td>
                          <td><input name="button4" type="button" class="button" id="userBtn" value="User" >
                              <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"userBtn",
                                inputField:"assignTo",
								name:"AssignToUser",
                                autoSearch:false,
                                callbackHandle:"selectUserCallback" 
							});
							function selectUserCallback(selectField, columns, value) {
								if ($(selectField) && value != null && value.length > 0) {
									for(var i = 0; i < value.length; i++) {
										addOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
									}
								}
							}				
							</script>                          </td>
							<td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
                        </tr>
                        <tr>
                          <td><input name="button4" type="button" class="button" id="roleBtn" value="Role" >
                              <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"roleBtn",
                                inputField:"assignTo",
								name:"AssignToRole",
                                autoSearch:false,
                                callbackHandle:"selectRoleCallback" 
							});
							function selectRoleCallback(selectField, columns, value) {
								if ($(selectField) && value != null && value.length > 0) {
									for(var i = 0; i < value.length; i++) {
										addOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
									}
								}
							}			
							</script>                          </td>
                        </tr>
                                            </table></td>
					</tr>
					
					<tr>
					  <th>Remark</th>
					  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %>"
							name="remark" id="remark"></td>
					  <th colspan="2" rowspan="4"><table class="formFull" border="1" cellpadding="1" cellspacing="1">
                          <tr>
                            <td><table class="formFull" border="0" cellpadding="1" cellspacing="1">
                                <tr>
                                  <th width="200">Tray Drawing No. (5)</th>
                                  <td><input  type="text" class="text" name="trayDrawingNo5" id="trayDrawingNo5"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO5") %>"></td>
                                </tr>
                                <tr>
                                  <th width="200">Tray Drawing No. Ver. (5)</th>
                                  <td><input type="text" class="text" name="trayDrawingNoVer5" id="trayDrawingNoVer5"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO_VER5") %>"></td>
                                </tr>
                                <tr>
                                  <th width="200">Color / Pocket Qty (請以 / 區分) (5)</th>
                                  <td><input type="text" class="text"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR5") %>"
							name="color5" id="color5"></td>
                                </tr>
                                <tr>
                                  <th width="200">COG Customer Name (5)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COG_CUST_NAME5") %>"
							name="cogCustName5" id="cogCustName5" ></td>
                                </tr>
                                
                            </table></td>
                          </tr>
                        </table></th>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						<th colspan="2" rowspan="4"><table class="formFull" border="1" cellpadding="1" cellspacing="1">
                          <tr>
                            <td><table class="formFull" border="0" cellpadding="1" cellspacing="1">
                                <tr>
                                  <th width="200">Tray Drawing No. (6)</th>
                                  <td><input  type="text" class="text" name="trayDrawingNo6" id="trayDrawingNo6"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO6") %>"></td>
                                </tr>
                                <tr>
                                  <th width="200">Tray Drawing No. Ver. (6)</th>
                                  <td><input type="text" class="text" name="trayDrawingNoVer6" id="trayDrawingNoVer6"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TRAY_DRAWING_NO_VER6") %>"></td>
                                </tr>
                                <tr>
                                  <th width="200">Color / Pocket Qty (請以 / 區分) (6)</th>
                                  <td><input type="text" class="text"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR6") %>"
							name="color6" id="color6"></td>
                                </tr>
                                <tr>
                                  <th width="200">COG Customer Name (6)</th>
                                  <td><input type="text" class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COG_CUST_NAME6") %>"
							name="cogCustName6" id="cogCustName6" ></td>
                                </tr>
                                
                            </table></td>
                          </tr>
                        </table></th>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>	
				</tbody>
			</table>

			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td>
					<div align="right">
					
					<% //Added on 3/9
						String isGuest="No";
						RoleDao roleDao = new RoleDao();
						UserTo currentUser = PIDBContext.getLoginUser(request);
						List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());
						if (checkedRoles != null) {
                                                    for (RoleTo roleTo : checkedRoles) {
                                                          if (roleTo.getRoleName().equals("Guest") )  {
                                                            isGuest="Yes";
                                                            } 
                                                        }
                                                      } 
						if ( isGuest.equals("No"))  {
						   %>
						   
					<input name="saveBtn" type="button"
						class="button" id="saveBtn" value="Save" onClick="doSave()">
						   <%
						   }     
						%>
					
					<input name="resetBtn" type="reset" class="button" id="resetBtn"
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
