<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpMaterialMatainTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
CpMaterialMatainTo ref = (CpMaterialMatainTo) request.getAttribute("ref");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function doReleaseToERP(s) {
  doSave(s);
}
function doSave(s) {
	
	if ($F('cpMaterialNum') == "") {	
		setMessage("error", "CP Material Num. is must field.");
		return;
	}
	if ($F('projCodeWVersion') == "") {	
		setMessage("error", "Project Code With Version. is must field.");
		return;
	}
	if ($F('cpVariant') == "") {	
		setMessage("error", "Cp Variant. is must field.");
		return;
	}
	if ($F('cpTestProgramNameList') == "") {	
		setMessage("error", "CP TEST PROGRAM NAME LIST. is must field.");
		return;
	}
	if ($F('description') == "") {	
		setMessage("error", "DESCRIPTION. is must field.");
		return;
	}
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking cp test program exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_cpMaterial_exist.do',
		{
			method: 'get',
			parameters: 'cpMaterialNum=' + $F('cpMaterialNum')+'&projCodeWVersion=' + $F('projCodeWVersion')+'&cpVariant=' + $F('cpVariant')+'&cpTestProgramNameList=' + $F('cpTestProgramNameList'),
			onComplete: checkCpMaterialExistComplete
		}
	);
}

function checkCpMaterialExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "CP Material Num is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
			setMessage("error", "Saving CP Material Num...");	
			selectAllOptions($('assignTo'));
			
			$('cpMaterialCreate').action = $('cpMaterialCreate').action + "?m=save";
			$('cpMaterialCreate').submit();
	}
}

function findMaterialDesc() {
	if($('cpMaterialNum').value !=""){
 	   
		new Ajax.Request( 
		'<%=cp%>/ajax/fetch_icWaferMaterialDesc.do',
		{
			method: 'post',
			parameters: 'projWver=' + $('projCodeWVersion').value+'&cpMaterialNum='+$('cpMaterialNum').value,
			onComplete: fetchMaterialDescComplete
		} );

	}
}


function fetchMaterialDescComplete(r) {	
   var returnValue = r.responseText.split(",");
   //alert(returnValue[0]);
	$('description').value =returnValue[0];
}

function forCreateWithRef(){
	var projW = $('projCodeWVersion').value;
	if($('projCodeWVersion') != ""){
		forFindmaterialName(projW);
	}

}

function forFindmaterialName(projW) {
   if (projW != null) {    
	   removeOptions($('cpMaterialNum'));								
		new Ajax.Request( 
		'<%=cp%>/ajax/fetch_icCpMaterialNum.do',
		{
			method: 'post',
			parameters: 'projWver=' + projW,
			onComplete: fetchIcCpMaterialNumComplete
			
		} );
	}
}

</script>
</head>
<body onLoad="forCreateWithRef()">
<form name="cpMaterialCreate"
	action="<%=cp %>/md/cp_material_create.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Create CP Material Number</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>					
					<td>
					<div id="error" class="formErrorMsg">
                         <%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
                         &nbsp;
                    </div>
					</td>
					<td>
					<div align="right">Created by <input name="createdBy"
						id="createdBy" type="text" class="text_protected" readonly>&nbsp;&nbsp;
					Modified by <input name="modifiedBy" id="modifiedBy" type="text"
						class="text_protected" readonly>&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>CP Material Num.</div></th>
						<td>
						<select name="cpMaterialNum" id="cpMaterialNum" class="select_w130" >
							<option value="">--Select--</option>
						</select>
						</td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>Project Code W Version</div></th>
						<td><input name="projCodeWVersion" type="text" class="text"
							id="projCodeWVersion" readonly
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE_W_VERSION") %>">
						<img src="<%=cp%>/images/lov.gif" alt="LOV"
							id="projCodeWVersionSSBtn"> <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                title:"Project Code W Version",
                                autoSearch:false,
                                mode:0,
								callbackHandle:"materialNameCallback"
							});
						
							function materialNameCallback(inputField, columns, value) {
							   if (value != null && value.length > 0) {    
								   $('projCodeWVersion').value = value[0]["PROJ_CODE_W_VERSION"];
								   removeOptions($('cpMaterialNum'));								
									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icCpMaterialNum.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcCpMaterialNumComplete
										
									} );

								}
							}

							function fetchIcCpMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('cpMaterialNum'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('cpMaterialNum'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('cpMaterialNum').length == 2 ){
										removeOption($('cpMaterialNum'),0);
									}else if ( $('cpMaterialNum').length > 2 ) {
										
									}else{
										removeOption($('cpMaterialNum'));
									}
								}
									findMaterialDesc();
							}
							</script></td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>CP VARIANT</th>
						<td><input name="cpVariant" type="text" class="text"
							id="cpVariant" 
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_VARIANT") %>"></td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>CP TEST PROGRAM NAME LIST</th>
						<td><input name="cpTestProgramNameList" type="text" class="text"
							 id="cpTestProgramNameList"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_TEST_PROGRAM_NAME_LIST") %>"></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>DESCRIPTION</div></th>
						<td><input name="description" type="text" class="text_protected" readonly
							id="description" 
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "DESCRIPTION") %>"></td>
					</tr>
					<tr>
						<th width="180">MP Status</th>
						 <td><select name="mpStatus" id="mpStatus" class="select_w130">
					      <option value="">--Select--</option>
                          <option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Active</option>
                          <option value="0" <%="0".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Inactive</option>
                      </select></td>
					</tr>
					<tr>
						<th width="180" style="vertical-align:top">Remark</th>
						<td style="vertical-align:top"><textarea id="remark" name="remark" cols="60" rows="4" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					</tr>
			</table>
			</div>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td><input type="hidden" id="toErp" name="toErp">
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
						   
					      <!--  <input name="releaseBtn" type="button"
						class="erp_button" id="releaseBtn" value="Release To ERP" onClick="doReleaseToERP('erp')">--><input
						name="saveBtn" type="button" class="button" id="saveBtn"
						value="Save" onClick="doSave()"> 
						
						   <%
						   }     
						%>
						
						<input name="resetBtn"
						type="reset" class="button" id="resetBtn" value="Reset"></div>
					</td>
				</tr>
			</table>

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

<!-- This is footer --> <jsp:include page="/common/footer.jsp"
	flush="true" /></form>
</body>
</html>
