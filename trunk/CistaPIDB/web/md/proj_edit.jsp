<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterIcTypeTo"%>
<%@ page import="com.cista.pidb.md.to.ProjectTo"%>
<%@ page import="com.cista.pidb.md.to.IcWaferTo"%>
<%@ page import="com.cista.pidb.md.dao.IcWaferDao"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	ProjectTo ref = (ProjectTo) request.getAttribute("ref");
	boolean completed = false;
	if (ref.getStatus().equalsIgnoreCase("Completed")) {
		completed = true;
	}
	
	boolean displayFab = true;
	IcWaferDao icWaferDao = new IcWaferDao();
	List<IcWaferTo> icWaferList = icWaferDao.findByProjCode(ref.getProjCode());
	if (icWaferList != null && icWaferList.size() > 0) {
	    displayFab = false;
	}
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function doRelease() {
    if (!checkRequired()) return;
	
	$('releaseBtn').disabled = true;
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	var incompleted = "";
	if ($F('fab') == "") {
		incompleted += ", FAB";
	}
	/*if ($F('subFab') == "") {
		incompleted += ", Sub Fab";
	}*/
	if ($F('prodFamily') == "") {
		incompleted += ", Product Family";
	}
	if ($F('panelType') == "") {
		incompleted += ", Panel Type";
	}
	if ($F('icType') == "") {
		incompleted += ", IC Type";
	}
	if ($F('prodLine') == "") {
		incompleted += ", Product Line";
	}
	if ($F('prodLine') != "IP" && $('prodCodeList').options.length == 0) {
		incompleted += ", Product Code";
	}
	
	var submitForm = false;
	
	if (incompleted != "") {
		if (confirm("The following required fields is incomplete, the project will be saved as draft:\r\n" + incompleted.substring(2))) {
			submitForm = true;
		} else {
			$('releaseBtn').disabled = false;
			$('saveBtn').disabled = false;
			$('resetBtn').disabled = false;
			setMessage("error", "User cancel.");
		}
	} else {
		submitForm = true;
	}
	
	if (submitForm ) {
		//setMessage("error", "Releasing project...");
		$('fab').disabled = false;
		$('subFab').disabled = false;
		$('prodFamily').disabled = false;
		$('prodCodeList').disabled = false;
		$('prodLine').disabled = false;
		$('panelType').disabled = false;
		$('icType').disabled = false;
		selectAllOptions($('prodCodeList'));
		selectAllOptions($('assignTo'));
		
		new Ajax.Request(
			'<%=cp%>/ajax/fetch_proj_codes.do',
			{
				method: 'get',
				parameters: 'projName='+ $F('projName'),
				onComplete: fetchProjCodesComplete
			}
		);
	}
}


function fetchProjCodesComplete(r) {
    var codes = r.responseText;
    var act = "&actType=release";
    if (confirm("There are project codes:" + codes + " with this project name, release all?")) {
        act += "&releaseType=all";
    } else {
        act += "&releaseType=single"
    }
    $('projectEdit').action += act;
    setMessage("error", "Releasing project...");
    $('projectEdit').submit();
}

function doSave() {
    if (!checkRequired()) return;
	var incompleted = "";
	if ($F('fab') == "") {
		incompleted += ", FAB";
	}
	/*if ($F('subFab') == "") {
		incompleted += ", Sub Fab";
	}*/

	if ($F('prodFamily') == "") {
		incompleted += ", Product Family";
	}
	if ($F('panelType') == "") {
		incompleted += ", Panel Type";
	}
	if ($F('icType') == "") {
		incompleted += ", IC Type";
	}
	if ($F('prodLine') == "") {
		incompleted += ", Product Line";
	}

	
	if ($F('prodLine') != "IP" && $('prodCodeList').options.length == 0) {
		incompleted += ", Product Code";
	}
	
	var submitForm = false;
	
	if (incompleted != "") {
		if (confirm("The following required fields is incomplete, the project will be saved as draft:\r\n" + incompleted.substring(2))) {
			submitForm = true;
		} else {
			$('releaseBtn').disabled = false;
			$('saveBtn').disabled = false;
			$('resetBtn').disabled = false;
			setMessage("error", "User cancel.");
		}
	} else {
		submitForm = true;
	}
	
	if (submitForm ) {
		setMessage("error", "Saving project...");
		$('fab').disabled = false;
		$('subFab').disabled = false;
		$('prodFamily').disabled = false;
		$('prodCodeList').disabled = false;
		$('prodLine').disabled = false;
		$('panelType').disabled = false;
		$('icType').disabled = false;
		selectAllOptions($('prodCodeList'));
		selectAllOptions($('assignTo'));
		$('projectEdit').submit();	
	}
}

function checkRequired() {

	if ($F('projName') == "" || $F('projName').length != 6) {
		setMessage("error", "Project name is must field with length 6.");
		return false;
	}
	if ($F('projOption') != "" & $F('projOption').length != 2) {
		setMessage("error", "Option is must field with length 2.");
		return false;
	}

	if ($F('projOption') != "" & isNaN($('projOption').value)){
		setMessage("error", "Option is must Number.");
		return false;
	}

	if ($F('fab') == "" || $F('fab').length == 0) {
		setMessage("error", "Fab must be choice.");
		return false;
	}

	if ($F('subFab') == "" || $F('subFab').length == 0) {
		setMessage("error", "Sub Fab must be choice.");
		return false;
	}

	if ($F('releaseTo') == "" || $F('releaseTo').length == 0) {
		setMessage("error", "Company must be choice.");
		return false;
	}

	if ($F('fcstCpYield') == "" || !IsNumber($F('fcstCpYield'))) {
		setMessage("error", "Forecast CP Yield(%) is must number.");
		return;		
	}		
	if ($F('grossDie') == "" || !IsNumber($F('grossDie'))) {
		setMessage("error", "Wafer Gross is must number.");
		return;		
	}		
	if ($F('pitch') == "" || !IsNumber($F('pitch'))) {
		setMessage("error", "Pitch (UM) is must number.");
		return;		
	}
	if ($F('releaseTo') == "" || $F('releaseTo').length == 0) {
		setMessage("error", "Company must be choice.");
		return;
	}
	return true;
}


function selectProdCode() {
	var target = "<%=cp%>/dialog/select_product1.do?m=list&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product1","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i];
			addOption($('prodCodeList'), prod, prod);
		}
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
		setMessage("error", "Retrieve user's email success.");
	}
	
	$('assignEmail').value = successItem;
	$('retrieveBtn').disabled = false;
}

function selFabVendorCode() {
	$('subFab').value = "";
	var nIndex;
	for (i=0 ; i<$('fab').length; i++){
		if ($('fab').options[i].value == $('fab').value) {
			nIndex = i;
		}
	}
	var fabCode = new Array();
	fabCode = $('fab').options[nIndex].text.split(",");
	$('fabVendorCode').value = fabCode[2] + "%";
}

function firstFabVendorCode() {
	var nIndex;
	for (i=0 ; i<$('fab').length; i++){
		if ($('fab').options[i].value == $('fab').value) {
			nIndex = i;
		}
	}
	var fabCode = new Array();
	fabCode = $('fab').options[nIndex].text.split(",");
	$('fabVendorCode').value = fabCode[2] + "%";
}

</script>
</head>
<body onLoad='firstFabVendorCode()'>
<form name="projectEdit" action="<%=cp %>/md/proj_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
		<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Modify Project</td>
				</tr>
			</table>
			
			<div class="content"> <!--  this is div start -->
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					</td>
					<td>
					<div align="right">
						Created by 
						<input name="createBy" id="createBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY") %>">&nbsp;&nbsp;
						Modified by 
						<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY") %>">&nbsp;&nbsp;
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="11%"><div class="erp_label">Project Code</div></th>
						<td width="22%"><input type="text" class="text_protected" name="projCode" id="projCode" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE") %>" readonly></td>
						<th width="11%"><div class="erp_label">Wafer Gross</div></th>
						<td width="22%">
						   <input type="text" class="text" name="grossDie" id="grossDie" value="<%=BeanHelper.getHtmlValueByColumn(ref, "GROSS_DIE") %>">
						   &nbsp;&nbsp;&nbsp;Est. 
						   <input type="checkbox" id="estimated" name="estimated" value="1" <%=ref!=null && ref.getEstimated()?"checked":"" %>>
						</td>
						<th width="11%">Project Type</th>
						<td width="22%">
							<select class="select_w130" name="projectType" id="projectType">
								<option value="">--Select--</option>
								<%
								List<FunctionParameterTo> projectTypeList = (List<FunctionParameterTo>) request.getAttribute("projectTypeList");
								for (FunctionParameterTo projectType : projectTypeList) 
								{
									String v = projectType.getFieldValue();
									String selected = "";
									if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "PROJECT_TYPE"))) 
									{
										selected = "selected";
									}
								%>
									<option value="<%=v %>" <%=selected %>><%=projectType.getFieldShowName() %></option>
								<%
								}
								%>
							</select>
						</td>	
					</tr>
					
					<tr>
						<th width="11%">
						    <div class="erp_label"><span class="star">*</span> Project Name</div>
						</th>
						<td width="22%">
						    <input type="text" class="text_protected" name="projName" id="projName" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_NAME") %>" readonly>
						</td>
						<th width="11%"><div class="erp_label">Forecast CP Yield (%)</div></th>
						<td width="22%"><input type="text" class="text" name="fcstCpYield" id="fcstCpYield" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FCST_CP_YIELD") %>"></td>
					    <th width="11%">APP Engineer</th>
					    <td width="23%"><input type="text" class="text" name="appEngr" id="appEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "APP_ENGR") %>"></td>

					</tr>
					
					<tr>
						<th width="11%"><div class="erp_label">Fab/ Fab Description</div></th>
						<td width="22%">
							<select class="select_w180" name="fab" id="fab" <%=completed || !displayFab ? "disabled" : "" %> onChange='selFabVendorCode()'>
								<option value="">--Select--</option>
								<%
							
								List<FabCodeTo> fabCodeList = (List<FabCodeTo>) request.getAttribute("fabCodeList");
								for (FabCodeTo fabCode : fabCodeList) 
								{
									String v = fabCode.getFab() + "," + fabCode.getFabDescr()+ "," + fabCode.getVendorCode();
									String selected = "";
									String fab = fabCode.getFab() == null ? "" : fabCode.getFab();
									if (fab.equals(BeanHelper.getHtmlValueByColumn(ref, "FAB"))) 
									{
										selected = "selected";
									}
							   %>
								<option value="<%=fab%>" <%=selected %>><%=v %></option>
								<%
							   }
								%>
							</select>
							<input type="hidden" id="fabVendorCode" name="fabVendorCode" >
						</td>
						<th width="11%">TO Include Sealring</th>
						<td width="22%">
							<select style="width:130px" name="toIncludeSealring" id="toIncludeSealring">
								<option value="">--Select--</option>
								<option value="1" <%="true".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(ref, "TO_INCLUDE_SEALRING"))?"selected":"" %>>YES</option>
								<option value="0" <%="false".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(ref, "TO_INCLUDE_SEALRING"))?"selected":"" %>>NO</option>
							</select>
						</td>
					   <th width="11%">PM</th>
					   <td width="23%">
					      <input type="text" class="text" name="pm" id="pm" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PM") %>">
					   </td>
					</tr>
					<tr>
						<th width="11%"><div class="erp_label">Sub Fab</div></th>
						<td width="22%"><input type="text"  name="subFab" id="subFab" value="<%=BeanHelper.getHtmlValueByColumn(ref, "SUB_FAB") %>" class="text" readonly>
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="subFabSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"subFabSSBtn",
                                inputField:"subFab",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='SUB_FAB' And FUN_NAME='PROJECT' And FIELD_SHOW_NAME Like {fabVendorCode} ",
								orderBy:"ITEM",
                                title:"SUB FAB",
                                mode:1,
                                autoSearch:false,
								callbackHandle:"subFabCallback"
							});

							function subFabCallback(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";
							
									for(var i = 0; i < value.length; i++) {
										tempValue += "," + value[i][columns[0]];
									}
							
									if(tempValue != "") {
										$(inputField).value = tempValue.substring(1);
									}
								}
							}
							</script>



						</td>
						<th width="11%">Sealring itme "(one-sided)"</th>
						<td width="22%">
							<select class="select_w130" name="sealring" id="sealring">
								<option value="">--Select--</option>
								<%
								List<FunctionParameterTo> sealringList = (List<FunctionParameterTo>) request.getAttribute("sealringList");
								for (FunctionParameterTo sealring : sealringList) 
								{
									String v = sealring.getFieldValue();
									String selected = "";
									if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "SEALRING"))) 
									{
										selected = "selected";
									}
								%>
								<option value="<%=v %>" <%=selected %>><%=sealring.getFieldShowName() %></option>
								<%
								}
							   %>
							</select>
						</td>
					   <th width="11%">QA Engineer</th>
					   <td width="23%">
					       <input type="text" class="text" name="qaEngr" id="qaEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "QA_ENGR") %>">
					   </td>

					</tr>
					<tr>
						<th width="11%">Option</th>
						<td width="22%"><input type="text" class="text_protected" name="projOption" id="projOption" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_OPTION") %>" readonly></td>
						<th width="11%">Scribe line(two-sided)</th>
						<td width="22%">
						   <select class="select_w130" name="scribeLine" id="scribeLine">
								<option value="">--Select--</option>
								<%
								List<FunctionParameterTo> scribeLineList = (List<FunctionParameterTo>) request.getAttribute("scribeLineList");
								for (FunctionParameterTo scribeLine : scribeLineList) 
								{
									String v = scribeLine.getFieldValue();
									String selected = "";
									if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "SCRIBE_LINE")))
									{
										selected = "selected";
									}
								%>
								<option value="<%=v %>" <%=selected %>><%=scribeLine.getFieldShowName() %></option>
								<%
								}
								%>		
						   </select>
						</td>
					   <th width="11%">Sales Engineer</th>
					   <td width="23%"><input type="text" class="text" name="salesEngr" id="salesEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "SALES_ENGR") %>"></td>
					</tr>

					<tr>
						<th width="11%" rowspan=3>Function</th>
						<td width="22%" rowspan=3>
						   <textarea id="funcRemark" name="funcRemark" cols="20" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "FUNC_REMARK") %></textarea>
						</td>
						<th width="11%">Test Line</th>
						<td width="22%"><input type="text" class="text" name="testLine" id="testLine" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TEST_LINE") %>"></td>
					     <th width="11%" rowspan="2">AssignTo</th>
					        <td width="23%" rowspan="2">
					    	  <table border="0" cellpadding="0" cellspacing="0">
					    	  <tr>
					    	     <td rowspan="2">
					  				 <select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    			 <%
					    			 if ( BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO") != null) 
					    			 {
						    			 String listStr = BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO");
						    			 String[] list = listStr.split(",");
						    			 for (String s : list) 
						    			 {
						    			   if (s != null && s.length() > 0) 
						    			   {
						    		 %>
						    		      <option value="<%=s %>"><%=s %></option>
						    		 <%
						    			   }
						    		   }
					    		    }
					    	       %>					  	
					  				 </select>								    
					    		  </td>
					    		  <td>
					    			 <input id="userBtn" type="button" class="button" value="User" >
					    		  </td>
					    		  <td>
					    		     <input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))">
					    		  </td>
					        </tr>
					        <tr>
					           <td>
					    			 <input id="roleBtn" type="button" class="button" value="Role" >
					    		  </td>
					    	  </tr>
					    	  </table>
					        </td>
							   <script type="text/javascript">
							   SmartSearch.setup({cp:"<%=cp%>", button:"userBtn", inputField:"assignTo", name:"AssignToUser", autoSearch:false, callbackHandle:"selectUserCallback" });
							   SmartSearch.setup({cp:"<%=cp%>", button:"roleBtn", inputField:"assignTo", name:"AssignToRole", autoSearch:false, callbackHandle:"selectRoleCallback"});
								function selectUserCallback(selectField, columns, value) 
								{
									if ($(selectField) && value != null && value.length > 0) 
									{
										for(var i = 0; i < value.length; i++) 
										{
											addOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
										}
									}
								}	
								function selectRoleCallback(selectField, columns, value) {
									if ($(selectField) && value != null && value.length > 0) {
										for(var i = 0; i < value.length; i++) {
											addOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
										}
									}
								}		
					         </script>
					</tr>

					<tr>
						<th width="11%">Wafer Thickness</th>
						<td width="22%">
						   <select class="select_w130" name="waferThickness" id="waferThickness"">
							<option value="">--Select--</option>
							<%
							List<FunctionParameterTo> waferThicknessList = (List<FunctionParameterTo>) request.getAttribute("waferThicknessList");
							for (FunctionParameterTo waferThickness : waferThicknessList) 
							{
								String v = waferThickness.getFieldValue();
								String selected = "";
								if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_THICKNESS"))) 
								{
									selected = "selected";
								}
						   %>
						      <option value="<%=v %>" <%=selected %>><%=waferThickness.getFieldShowName() %></option>
							<%
							}
							%>						
						   </select>
						</td>
					</tr>

					<tr>
						<th width="11%">Process Name</th>
						<td width="22%">
						   <input type="text" class="text" name="procName" id="procName" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROC_NAME") %>">
						</td>
					   <th width="11%">Status</th>
					   <td width="23%"><input type="text" class="text_protected" name="status" id="status" value="<%=BeanHelper.getHtmlValueByColumn(ref, "STATUS") %>" readOnly></td>
				   </tr>
					<tr>
						<th width="11%">Nickname</th>
						<td width="22%"><input type="text" class="text" name="nickName" id="nickName" value="<%=BeanHelper.getHtmlValueByColumn(ref, "NICK_NAME") %>"></td>
						<th width="11%">Any IP Usage</th>
						<td width="22%">
						   <select class="select_w130" name="anyIpUsage" id="anyIpUsage">
							    <option value="">--Select--</option>
							    <option value="1" <%="true".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(ref, "ANY_IP_USAGE"))?"selected":"" %>>YES</option>
							    <option value="0" <%="false".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(ref, "ANY_IP_USAGE"))?"selected":"" %>>NO</option>
						   </select>
						</td>
					   <th width="11%">Product Line</th>
					      <td width="23%">
					         <input class="text" readonly name="prodLine" id="prodLine" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_LINE") %>"> 
					         <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodLineSSBtn">
							   <script type="text/javascript">
							   SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodLineSSBtn",
                                inputField:"prodLine",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='PRODUCT_LINE'",
								orderBy:"ITEM",
                                title:"Product Line",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"prodLineCallback"
							   });

								function prodLineCallback(inputField, columns, value) 
								{
									if ($(inputField) && value != null && value.length > 0) 
									{
										var tempValue = "";							
										for(var i = 0; i < value.length; i++) 
										{
											tempValue += "," + value[i][columns[0]];
										}								
										if(tempValue != "") 
										{
											$(inputField).value = tempValue.substring(1);
										}
									}
								}
							   </script>
						   </td>
				   </tr>
				   
					<tr>
						<th width="11%">Customer</th>
						<td width="22%"><input type="text" class="text" name="cust" id="cust" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CUST") %>"></td>					
						<th width="11%">Embedded OTP</th>
						<td width="22%">
							<select class="select_w130" name="embeddedOtp" id="embeddedOtp">
								<option value="">--Select--</option>
								<option value="1" <%="true".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(ref, "EMBEDDED_OTP"))?"selected":"" %>>YES</option>
								<option value="0" <%="false".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(ref, "EMBEDDED_OTP"))?"selected":"" %>>NO</option>
							</select>
						</td>
					   <th width="11%"><div class="erp_label">Product Family</div></th>
					   <td width="23%">
					      <select class="select_w130" name="prodFamily" id="prodFamily">
							<option value="">--Select--</option>
							<%
								List<SapMasterProductFamilyTo> productFamilyList = (List<SapMasterProductFamilyTo>) request.getAttribute("productFamilyList");
								for (SapMasterProductFamilyTo productFamily : productFamilyList) 
								{
									String v = productFamily.getProductFamily();
									String selected = "";
									if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "PROD_FAMILY"))) 
									{
										selected = "selected";
									}
							%>
							<option value="<%=v %>" <%=selected %>><%=productFamily.getDescription() %></option>
							<%
								}
							%>
						   </select>
					   </td>
				   </tr>

					<tr>
						<th width="11%">
						   <div class="erp_label">Pitch (UM)</div>
						</th>
						<td width="22%">
						   <input type="text" class="text" name="pitch" id="pitch" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PITCH") %>">
						</td>					
						<th width="11%">OTP Size</th>
						<td width="22%">
						   <input type="text" class="text" name="otpSize" id="otpSize" value="<%=BeanHelper.getHtmlValueByColumn(ref, "OTP_SIZE") %>">
						</td>
					      <th width="11%">Panel Type</th>
					   <td width="23%">
					   <select class="select_w130" name="panelType" id="panelType">
							<option value="">--Select--</option>
							<option value="TFT-LCD" <%="TFT-LCD".equals(BeanHelper.getHtmlValueByColumn(ref, "PANEL_TYPE"))?"selected":"" %>>TFT-LCD</option>
							<option value="LTPS" <%="LTPS".equals(BeanHelper.getHtmlValueByColumn(ref, "PANEL_TYPE"))?"selected":"" %>>LTPS</option>
							<option value="OLED" <%="OLED".equals(BeanHelper.getHtmlValueByColumn(ref, "PANEL_TYPE"))?"selected":"" %>>OLED</option>
							<option value="PDP" <%="PDP".equals(BeanHelper.getHtmlValueByColumn(ref, "PANEL_TYPE"))?"selected":"" %>>PDP</option>
						</select>
					   </td>						
				    </tr>

					<tr>
						<th width="11%">Pad Type</th>
						<td width="22%">
							<select name="padType" id="padType" class="select_w130">
								<option value="">--Select--</option>
								<option value="2L Stagger" <%="2L Stagger".equals(BeanHelper.getHtmlValueByColumn(ref, "PAD_TYPE"))?"selected":"" %>>2L Stagger</option>
								<option value="3L Stagger" <%="3L Stagger".equals(BeanHelper.getHtmlValueByColumn(ref, "PAD_TYPE"))?"selected":"" %>>3L Stagger</option>
								<option value="Linear" <%="Linear".equals(BeanHelper.getHtmlValueByColumn(ref, "PAD_TYPE"))?"selected":"" %>>Linear</option>								
							</select>
						</td>					

						<th width="11%">Kick-off Date</th>
						<td width="22%"><input type="text" class="text" name="kickOffDate" id="kickOffDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "KICK_OFF_DATE", "yyyy/MM/dd") %>" readonly>
							<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="kickoffdateBtn"> 
							<script type="text/javascript">
								Calendar.setup({
									inputField:"kickOffDate",
									ifFormat:"%Y/%m/%d",
									button:"kickoffdateBtn"
								});
							</script>
						</td>
					   <th width="11%"><div class="erp_label">IC Type</div></th>
					   <td width="23%">
					       <select class="select_w130" name="icType" id="icType">
							    <option value="">--Select--</option>
								 <%
								 List<SapMasterIcTypeTo> icTypeList = (List<SapMasterIcTypeTo>) request.getAttribute("icTypeList");
								 for (SapMasterIcTypeTo icType : icTypeList) 
								 {
									 String v = icType.getIcType();
									 String selected = "";
									 if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "IC_TYPE"))) 
									 {
										selected = "selected";
									 }
								 %>
								 <option value="<%=v %>" <%=selected %>><%=icType.getDescription() %></option>
								 <%
								 }
								 %>
						    </select>
					   </td>
					</tr>

					<tr>
						<th width="11%">FS Date</th>
						<td width="22%">
						   <input type="text" class="text" name="fsDate" id="fsDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FS_DATE", "yyyy/MM/dd") %>" readonly>
							<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="fsDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
								Calendar.setup({
									inputField:"fsDate",
									ifFormat:"%Y/%m/%d",
									button:"fsDateBtn"
								});
							</script>
						</td>	
						<th width="11%">Project Leader</th>
						<td width="22%">
						    <input type="text" class="text" name="projLeader" id="projLeader" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_LEADER") %>">
						</td>
					   <th width="11%">Remark</th>
					   <td width="23%">
					       <input type="text" class="text" name="remark" id="remark" value="<%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %>">
					   </td>
					</tr>
					
					<tr>
						<th width="11%">
						   <div class="erp_label">Process Technology</div>
						</th>
						<td width="22%">
							<select class="select_w130" name="procTech" id="procTech">
								<option value="">--Select--</option>
								<%
								List<FunctionParameterTo> procTech = (List<FunctionParameterTo>) request.getAttribute("processTechnology");
								for (FunctionParameterTo prodc : procTech) 
								{
									String v = prodc.getFieldValue();
									String selected = "";
									if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "PROC_TECH"))) 
									{
										selected = "selected";
									}
								%>
									<option value="<%=v %>" <%=selected %>><%=prodc.getFieldShowName() %></option>
								<%
								}
								%>							
							</select>
						</td>							
						<th width="11%">Design Engineer</th>
						<td width="22%">
						    <input type="text" class="text" name="designEngr" id="designEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "DESIGN_ENGR") %>">
						</td>
				      <th width="11%" rowspan="7" style="vertical-align:top">Product Code</th>
				      <td width="23%" rowspan="7" style="vertical-align:top">
					      <input type="button" class="button" value="Add" onclick="selectProdCode()">
					      <input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('prodCodeList'))">
					      <br>
					      <select size="8" multiple style="width:100%" name="prodCodeList" id="prodCodeList">
					    	<%
				    		if ( BeanHelper.getHtmlValueByColumn(ref, "PROD_CODE_LIST") != null) 
				    		{
				    			String prodCodeListStr = BeanHelper.getHtmlValueByColumn(ref, "PROD_CODE_LIST");
				    			String[] prodCodeList = prodCodeListStr.split(",");
					    		for (String prodCode : prodCodeList) 
					    		{
					    			if (prodCode != null && prodCode.length() > 0) 
					    			{
					    	%>
					    			<option value="<%=prodCode %>"><%=prodCode %></option>
					    	<%
					    			}
					    		}
				    		}
					    	%>
					      </select>
					   </td>
					</tr>

					<tr>
						<th width="11%"><div class="erp_label">Poly Metal Layers</div></th>
						<td width="22%">
							<select name="polyMetalLayers" id="polyMetalLayers" class="select_w130">
							  <option value="">--Select--</option>
							  <option value="1P2M">1P2M</option>
							  <option value="1P3M">1P3M</option>
							  <option value="1P4M">1P4M</option>
							  <option value="1P5M">1P5M</option>
							  <option value="1P6M">1P6M</option>
							  <option value="1P7M">1P7M</option>
							  <option value="1P8M">1P8M</option>
							  <option value="2P2M">2P2M</option>
							  <option value="2P3M">2P3M</option>
							  <option value="2P4M">2P4M</option>
							</select>
						</td>						
						<th width="11%">Product Engineer</th>
						<td width="22%">
						   <input type="text" class="text" name="prodEngr" id="prodEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_ENGR") %>">
						</td>
					</tr>

					<tr>
						<th width="11%"><div class="erp_label">Voltage</div></th>
						<td width="22%">
						   <input type="text" cols="80%" readonly name="voltage" id="voltage" value="<%=BeanHelper.getHtmlValueByColumn(ref, "VOLTAGE") %>" class="text"> 
					      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="voltageSSBtn">
							<script type="text/javascript">
							   SmartSearch.setup({
                              cp:"<%=cp%>",
                              button:"voltageSSBtn",
                              inputField:"voltage",
                              table:"PIDB_FUN_PARAMETER_VALUE",
								  		keyColumn:"FIELD_VALUE",
                              columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								  		whereCause:"FUN_FIELD_NAME='VOLTAGE' and FUN_NAME='PROJECT'",
								  		orderBy:"ITEM",
                              title:"Voltage",
                              mode:0,
                              autoSearch:false,
								  		callbackHandle:"voltageCallback"
							   });

								function voltageCallback(inputField, columns, value) 
								{
									if ($(inputField) && value != null && value.length > 0) 
									{
										var tempValue = "";
								
										for(var i = 0; i < value.length; i++) 
										{
											tempValue += "," + value[i][columns[0]];
										}
								
										if(tempValue != "") 
										{
											$(inputField).value = tempValue.substring(1);
										}
									}
								}
							</script>
						</td>	
						<th width="11%">ESD Engineer</th>
						<td width="22%">
						     <input type="text" class="text" name="esdEngr" id="esdEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "ESD_ENGR") %>">
						</td>
					</tr>

					<tr>
						<th width="11%">Wafer Type</th>
						<td width="22%">
							<select class="select_w130" name="waferType" id="waferType">
								<option value="">--Select--</option>
								<option value="Non-epi" <%="Non-eip".equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_TYPE"))?"selected":"" %>>Non-epi</option>
								<option value="Epi" <%="Epi".equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_TYPE"))?"selected":"" %>>Epi</option>
								<option value="Hi Wafer" <%="Hi Wafer".equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_TYPE"))?"selected":"" %>>Hi Wafer</option>
							</select>
						</td>
						<th width="11%">APR Engineer</th>
						<td width="22%">
						     <input type="text" class="text" name="aprEngr" id="aprEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "APR_ENGR") %>">
						</td>
					</tr>

					<tr>
						<th width="11%">Mask Layers No.</th>
						<td width="22%">
							<select name="maskLayersNo" id="maskLayersNo" class="select_w130">
							   <option value="">--Select--</option>
								<%
								List<FunctionParameterTo> maskLayersList = (List<FunctionParameterTo>) request.getAttribute("maskLayersList");
								for (FunctionParameterTo maskLayers : maskLayersList) 
								{
									String v = maskLayers.getFieldValue();
									String selected = "";
									if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "MASK_LAYERS_NO"))) 
									{
										selected = "selected";
									}
								%>
								<option value="<%=v %>" <%=selected %>><%=maskLayers.getFieldShowName() %></option>
								<%
								}
								%>
							</select>
						</td>										
						<script>$('polyMetalLayers').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "POLY_METAL_LAYERS") %>";</script>
						</td>	
						
						<th width="11%">Layout Engineer</th>
					   <td width="22%">
					   	<input type="text" class="text" name="layoutEngr" id="layoutEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "LAYOUT_ENGR") %>">
					  	</td>
					</tr>

					<tr>
						<th width="11%"><div class="erp_label">Processing Layers No.</div></th>
						<td width="22%">
							<select name="procLayerNo" id="procLayerNo" class="select_w130">
						   	<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> processingLayersList = (List<FunctionParameterTo>) request.getAttribute("processingLayersList");
									for (FunctionParameterTo processingLayers : processingLayersList) 
									{
										String v = processingLayers.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "PROC_LAYER_NO"))) 
										{
											selected = "selected";
										}
							   %>
								      <option value="<%=v %>" <%=selected %>><%=processingLayers.getFieldShowName() %></option>
								<%
								   }
								%>	
							</select>
						</td>	
						<th width="11%">Test Engineer</th>
						<td width="22%">
							<input type="text" class="text" name="testEngr" id="testEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TEST_ENGR") %>">
						</td>
				   </tr>

					<tr>
						<th width="11%"><div class="erp_label">Wafer Inch</div></th>
						<td width="22%">
						   <select class="select_w130" name="waferInch" id="waferInch">
								<option value="">--Select--</option>
								<option value="6&quot;" <%="6&quot;".equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_INCH"))?"selected":"" %>>6&quot;</option>
								<option value="8&quot;" <%="8&quot;".equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_INCH"))?"selected":"" %>>8&quot;</option>
								<option value="12&quot;" <%="12&quot;".equals(BeanHelper.getHtmlValueByColumn(ref, "WAFER_INCH"))?"selected":"" %>>12&quot;</option>
						   </select>
						</td>	
						<th width="11%">ASSY Engineer</th>
						<td width="22%">
						   <input type="text" class="text" name="assyEngr" id="assyEngr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "ASSY_ENGR") %>">
						</td>
				   </tr>

					<tr>
						<th width="11%">X Size (UM;含SR)</th>
						<td width="22%">
						   <input type="text" class="text" name="xSize" id="xSize" value="<%=BeanHelper.getHtmlValueByColumn(ref, "X_SIZE") %>">
						</td>																						
						<th width="11%">
						   <div class="erp_label">2nd Foundry Project</div>
						</th>
						<td width="22%">
						   <select class="select_w130" name="secondFoundryProject" id="secondFoundryProject">
								<option value="">--Select--</option>
								<option value="Yes" <%="Yes".equals(BeanHelper.getHtmlValueByColumn(ref, "SECOND_FOUNDRY_PROJECT"))?"selected":"" %>>Yes</option>
								<option value="No" <%="No".equals(BeanHelper.getHtmlValueByColumn(ref, "SECOND_FOUNDRY_PROJECT"))?"selected":"" %>>No</option>
							</select>
						</td>
						<th width="11%"></th>
						<td width="22%"></td>
				    </tr>
				    
				    <tr>
						<th width="11%">Y Size</th>
						<td width="22%">
						    <input type="text" class="text" name="ySize" id="ySize" value="<%=BeanHelper.getHtmlValueByColumn(ref, "Y_SIZE") %>">
						</td>				    
						<th width="11%">
						    <div class="erp_label"><span class="star">*</span>Company</div>
						</th>
						<td width="22%">
							 <select class="select_w130" name="releaseTo" id="releaseTo">
							 <option value="">--Select--</option>
							 <%
							 List<FunctionParameterTo> companyList = (List<FunctionParameterTo>) request.getAttribute("companyNameList");
							 for (FunctionParameterTo company : companyList) 
							 {
								  String v = company.getFieldValue();
								  String selected = "";
								  if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "RELEASE_TO"))) 
								  {
										selected = "selected";
									}
							 %>
							    <option value="<%=v %>" <%=selected %>><%=company.getFieldShowName() %></option>
							 <%
							 }
							 %>
							 </select>
						</td>			
						<th width="11%">&nbsp;</th>
						<td width="22%">&nbsp;</td>	
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
						if (checkedRoles != null) 
						{
	                  for (RoleTo roleTo : checkedRoles) 
	                  {
	                     if (roleTo.getRoleName().equals("Guest") )  
	                     {
	                          isGuest="Yes";
	                      } 
	                   }
	               } 
						if (isGuest.equals("No") )  
						{
						%>
						   <input name="releaseBtn" type="button" class="erp_button" id="releaseBtn" value="Release To ERP" onclick="doRelease()"> 
						   <input name="saveBtn" type="button" class="button" id="saveBtn" value="Save" onClick="doSave()"> 
						<%
						}     
						%>
						<input name="resetBtn" type="reset" class="button" id="resetBtn" value="Reset">
					</div>
					</td>
				</tr>
			</table>
			
			<!-- Content end -->
			</div> <!--  this is div end -->
		</td>
		<td width="5" valign="bottom" background="<%=cp %>/images/shadow-1.gif">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=cp %>/images/bgs.gif">
			<tr>
				<td height="15">
					<img src="<%=cp %>/images/spacer.gif" width="1" height="1" alt="">
				</td>
			</tr>
			</table>
		</td>
		</tr>
		
		<tr>
			<td colspan="2">
			   <img height="2" alt="" src="<%=cp %>/images/shadow-2.gif" width="100%" border="0">
			</td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
<%
String error = (String) request.getAttribute("error");
if (error != null) {
	out.print("<script>setMessage('error' ,'" + error +"');</script>");
}
%>