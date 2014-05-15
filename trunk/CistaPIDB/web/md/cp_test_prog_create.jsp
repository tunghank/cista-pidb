<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpTestProgramTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>

<%
CpTestProgramTo ref = (CpTestProgramTo) request.getAttribute("ref");
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
	if (s && s=="erp") {
		$('toErp').value = 1;
	} else {
		$('toErp').value = 0;
	}
	if ($F('cpMaterialNum') == "") {	
		setMessage("error", "CP Material Num. is must field.");
		return;
	}
	if ($F('prodCode') == "") {	
		setMessage("error", "Product Code is must field.");
		return;
	}
	if ($F('projCode') == "") {	
		setMessage("error", "Project Code is must field.");
		return;
	}
	if ($F('prodName') == "") {	
		setMessage("error", "Product Name is must field.");
		return;
	}
	if ($F('projCodeWVersion') == "") {	
		setMessage("error", "Project Code w Version is must field.");
		return;
	}	
	if ($F('cpTestProgName') == "") {
		setMessage("error", "CP Test Program Name is must field.");
		return;
	}
	if ($F('cpTestProgRevision') == "") {
		setMessage("error", "CP Test Program Revision is must field.");
		return;
	}
	if ($F('cpCpuTime') != "" && !IsNumber($F('cpCpuTime'))) {
		setMessage("error", "CP CPU Time is must number.");
		return;		
	}		
	if ($F('cpIndexTime') != "" && !IsNumber($F('cpIndexTime'))) {
		setMessage("error", "CP Index Time is must number.");
		return;		
	}			
	
	$('releaseBtn').disabled = true;
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking cp test program exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_cpTestProgram_exist.do',
		{
			method: 'get',
			parameters: 'cpTestProgName='+ $F('cpTestProgName') + '&projCodeWVersion=' + $F('projCodeWVersion')+ '&cpMaterialNum=' + $F('cpMaterialNum'),
			onComplete: checkCpTestProgramExistComplete
		}
	);
}

function checkCpTestProgramExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "CP Test Program Name is already exist.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
			setMessage("error", "Saving cp test program...");	
			selectAllOptions($('assignTo'));
			
			$('cpTestProgCreate').action = $('cpTestProgCreate').action + "?m=save&toErp=" + $F('toErp');
			$('cpTestProgCreate').submit();
	}
}

function selectProjCode() {
	var target = "<%=cp%>/dialog/select_projCode_radio.do?m=list2&callback=selectProjCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_proj_code","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeComplete(selectedItems) {
	
	if (selectedItems && selectedItems.length>0) {
		var items = selectedItems.split("|");
		if (items.length > 0) {
			$('projCode').value = items[0];
			$('prodCode').value = items[1];
			$('prodName').value = items[2];
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
		setMessage("error", "Retrieve user's email success. ");
	}
	
	$('assignEmail').value = successItem;
	$('retrieveBtn').disabled = false;
}

function findMaterialDesc() {
	if($('cpMaterialNum').value !=""){
		//alert($('cpMaterialNum').value);
 	   
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
	$('materialDesc').value =returnValue[0];
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
<form name="cpTestProgCreate"
	action="<%=cp %>/md/cp_test_prog_create.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Create CP Test Program</td>
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
						<select name="cpMaterialNum" id="cpMaterialNum" class="select_w130" onChange="findMaterialDesc();">
							<option value="">--Select--</option>
						</select>
						</td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>Project Code</div></th>
						<td><input name="projCode" readonly type="text"
							class="text_required" id="projCode"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE") %>" >
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projectCode"
							onClick="selectProjCode()"> &nbsp;</td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>Product Code</th>
						<td><input name="prodCode" type="text" class="text_protected"
							id="prodCode" readonly
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_CODE") %>"></td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>Product Name</th>
						<td><input name="prodName" type="text" class="text_protected"
							readonly id="prodName"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_NAME") %>"></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>Project Code W Version</div></th>
						<td><input name="projCodeWVersion" type="text" class="text"
							id="projCodeWVersion" readonly
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE_W_VERSION") %>" onChange="findMaterialDesc()">
						<img src="<%=cp%>/images/lov.gif" alt="LOV"
							id="projCodeWVersionSSBtn"> <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                whereCause:" PROJ_CODE = '{projCode}' and ROUTING_CP ='1'",
                                title:"Project Code W Version",
                                autoSearch:false,
                                mode:0,
								callbackHandle:"materialNameCallback"
							});
						
							function materialNameCallback(inputField, columns, value) {
							   if (value != null && value.length > 0) {    
								   $('projCodeWVersion').value = value[0]["PROJ_CODE_W_VERSION"];
								   removeOptions($('cpMaterialNum'));								
								   //removeOptions($('MaterialDesc'));
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
						<th width="180"><span class="star">*</span>Material Description</th>
						<td><input name="materialDesc" type="text" class="text_protected"
							readonly id="materialDesc"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "MATERIAL_DESC") %>"></td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>CP Test Program
						Name</th>
						<td><input name="cpTestProgName" type="text"
							class="text_300" id="cpTestProgName"></td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>CP Test Program
						Revision</th>
						<td><input name="cpTestProgRevision" type="text"
							class="text_required" id="cpTestProgRevision"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_TEST_PROG_REVISION") %>"></td>
					</tr>
					<tr>
						<th width="180">CP Test Program Release Date</th>
						<td><input name="cpTestProgReleaseDate" type="text"
							class="text" id="cpTestProgReleaseDate" readonly
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_TEST_PROG_RELEASE_DATE", "yyyy/MM/dd") %>">
						<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="cpTestProgReleaseDateBtn"> <script
							type="text/javascript">
							Calendar.setup({
								inputField:"cpTestProgReleaseDate",
								ifFormat:"%Y/%m/%d",
								button:"cpTestProgReleaseDateBtn"
							});
						</script></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label">CP CPU Time</div></th>
						<td><input name="cpCpuTime" type="text" class="text"
							id="cpCpuTime"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_CPU_TIME") %>"></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label">CP Index Time</div></th>
						<td><input name="cpIndexTime" type="text" class="text"
							id="cpIndexTime"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_INDEX_TIME") %>"></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label">Contact Die Quantity</div></th>
						<td><select name="contactDieQty" id="contactDieQty"
							class="select_w130">
							<option value="">--Select--</option>
							<option value="1"
								<%="1".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>1</option>
							<option value="1.2"
								<%="1.2".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>1.2</option>
							<option value="2"
								<%="2".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>2</option>
							<option value="2.4"
								<%="2.4".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>2.4</option>
							<option value="3"
								<%="3".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>3</option>	
							<option value="4"
								<%="4".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>4</option>
							<option value="8"
								<%="8".equals(BeanHelper.getHtmlValueByColumn(ref, "CONTACT_DIE_QTY"))?"selected":"" %>>8</option>
						</select></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label">Tester</div></th>
						<td><input type="text" cols="80%" readonly name="tester" id="tester" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TESTER") %>" class="text"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="testerSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"testerSSBtn",
                                inputField:"tester",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='TESTER' and fun_name='CP_TEST_PROG'",
								orderBy:"ITEM",
                                title:"TESTER",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"testerCallback"
							});

							function testerCallback(inputField, columns, value) {
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
					</tr>
					<tr>
						<th width="180"><div class="erp_label">Tester Config</div></th>
						<td><input name="testerConfig" type="text" class="text"
							id="testerConfig"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "TESTER_CONFIG") %>"></td>
					</tr>
					<tr>
						<th width="180">1st CP Test House</th>
						<td><select name="firstCpTestHouse" id="firstCpTestHouse"
							class="select_w130">
							<option value="">--Select--</option>
							<%
										List<SapMasterVendorTo> vendorList = (List<SapMasterVendorTo>) request
										.getAttribute("vendorList");
								for (SapMasterVendorTo to : vendorList) {
									String verdorCode = to.getVendorCode();
									if (verdorCode != null && verdorCode.length() > 0) {
							%>
							<option value="<%=verdorCode %>"
								<%=verdorCode.equals(BeanHelper.getHtmlValueByColumn(ref, "FIRST_CP_TEST_HOUSE"))?"selected":"" %>><%=to.getShortName()%></option>
							<%
								}
								}
							%>
						</select></label></td>
					</tr>
					<tr>
						<th width="180">Remark</th>
						<td><input class="text" name="remark" id="remark"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %>">
						&nbsp;</td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label">Multiple Stage</div></th>
						<td>
						<input type="checkbox" id="multipleStage" name="multipleStage" value="1" <%=ref!=null && ref.getMultipleStage()?"checked":"" %>>
						</td>
					</tr>
					<tr>
						<th width="180">AssignTo</th>
						<td>
						<table border="0" cellspace="0" cellpadding="0" margin="0">
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
								<td><input name="button" type="button" class="button"
									id="userBtn" value="User"> <script
									type="text/javascript">
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
							</script></td>
							<td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
							</tr>
							<tr>
								<td><input name="button" type="button" class="button"
									id="roleBtn" value="Role"> <script
									type="text/javascript">
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
							</script></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<th width="180">Vendor Code</th>
							<% 
								SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
								SapMasterVendorTo sapMasterVendorTo = sapMasterVendorDao.findByVendorCode(BeanHelper.getHtmlValueByColumn(ref, "VENDOR_CODE"));
							%>
						<td><input class="text" type="text" readonly id="vendorCode" name="vendorCode"  value="<%=BeanHelper.getHtmlValueByColumn(sapMasterVendorTo, "SHORT_NAME")%>"  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="vendorCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"vendorCodeSSBtn",
                                inputField:"vendorCode",
                                table:"WM_SAP_MASTER_VENDOR",
                                keyColumn:"SHORT_NAME",
                                columns:"SHORT_NAME,VENDOR_CODE",
                                autoSearch:false,
                                title:"Vendor Code"
							});
							</script></td>

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
						   
					        <input name="releaseBtn" type="button"
						class="erp_button" id="releaseBtn" value="Release To ERP" onClick="doReleaseToERP('erp')"> <input
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
