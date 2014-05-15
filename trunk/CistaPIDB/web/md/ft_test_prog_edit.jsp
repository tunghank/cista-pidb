<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.FtTestProgramTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
   FtTestProgramTo ref = (FtTestProgramTo) request.getAttribute("ref");
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
	if ($F('ftMaterialNum') == "") {	
		setMessage("error", "FT Material Num. is must field.");
		return;
	}
	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('ftTestProgName') == "") {
		setMessage("error", "FT Test Program Name is must field.");
		return;
	}
	if ($F('ftTestProgRevision') == "") {
		setMessage("error", "FT Test Program Revision is must field.");
		return;
	}
	if ($F('ftCpuTime') != "" && !IsNumber($F('ftCpuTime'))) {
		setMessage("error", "FT CPU Time is must number.");
		return;		
	}	
	if ($F('ftIndexTime') != "" && !IsNumber($F('ftIndexTime'))) {
		setMessage("error", "FT Index Time is must number.");
		return;		
	}				
	
		var submitForm = true;
	if (submitForm ) {
		setMessage("error", "Saving ft test program...");
		selectAllOptions($('assignTo'));
		
		$('ftTestProgEdit').action = $('ftTestProgEdit').action + "?m=save&toErp=" + $F('toErp');
		$('ftTestProgEdit').submit();	
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
<form name="ftTestProgEdit" action="<%=cp %>/md/ft_test_prog_edit.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Modify FT Test Program</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg">
                     <%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
                     &nbsp;</div>
					</td>
					<td>
					<div align="right">
					Created by 
					<input name="createdBy" id="createdBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY") %>">&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				    <tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>FT Material Num.</div></th>
						<td><input name="ftMaterialNum" readonly type="text"
							class="text_protected" id="ftMaterialNum"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_MATERIAL_NUM") %>">					
				    </tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>Part Number</div></th>
						<td><input class="text_protected" name="partNum" readonly
							id="partNum"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PART_NUM") %>">						</td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>FT Test Program Name</div></th>
						<td><input class="text_protected" name="ftTestProgName" readonly
							id="ftTestProgName"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_TEST_PROG_NAME")%>"></td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>FT Test Program Revision</th>
						<td><input class="text_protected" maxlength="20" size="20" readonly
							name="ftTestProgRevision" id="ftTestProgRevision"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_TEST_PROG_REVISION") %>"></td>
					</tr>
					<tr>
						<th>FT Test Program Release Date</th>
						<td><input name="ftTestProgReleaseDate" type="text" readonly
							class="text" id="ftTestProgReleaseDate"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_TEST_PROG_RELEASE_DATE", "yyyy/MM/dd") %>">
						<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="ftTestProgReleaseDateBtn"> <script
							type="text/javascript">
							Calendar.setup({
								inputField:"ftTestProgReleaseDate",
								ifFormat:"%Y/%m/%d",
								button:"ftTestProgReleaseDateBtn"
							});
						</script></td>
					</tr>
					<tr>
						<th><div class="erp_label">FT CPU Time</div></th>
						<td><input class="text" name="ftCpuTime" id="ftCpuTime" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_CPU_TIME") %>"></td>
					</tr>
					<tr>
						<th><div class="erp_label">FT Index Time</div></th>
						<td><input class="text" name="ftIndexTime" id="ftIndexTime" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_INDEX_TIME") %>"></td>
					</tr>
					<tr>
					  <th><div class="erp_label">Contact Die Quantity</div></th>
					  <td><select name="contactDieQty"
							id="contactDieQty" class="select_w130">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> contactDieQtyList = (List<FunctionParameterTo>) request.getAttribute("contactDieQtyList");
										for (FunctionParameterTo contactDieQty : contactDieQtyList) {
											String v = contactDieQty.getFieldValue();
											String selected = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"CONTACT_DIE_QTY")))
											{
												selected = "selected";
											}
								%>
											<option value="<%=v %>" <%=selected%>><%=contactDieQty.getFieldShowName() %></option>
								<%
										}
								%>
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
								whereCause:"FUN_FIELD_NAME='TESTER' and fun_name='FT_TEST_PROG'",
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
					  <th><div class="erp_label">Tester Config</div></th>
					  <td><input class="text" maxlength="20" size="20"
							name="testerConfig" id="testerConfig"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "TESTER_CONFIG") %>"></td>
				    </tr>
					<tr>
					  <th>1st FT Test House</th>
					  <td><select name="firstFtTestHouse" id="firstFtTestHouse"
							class="select_w130">                      
                      <option value="">--Select--</option>
						   <%
								List<SapMasterVendorTo> vendorList = (List<SapMasterVendorTo>) request.getAttribute("vendorList");
								for(SapMasterVendorTo to : vendorList) {
									String verdorCode = to.getVendorCode();
									if(verdorCode != null && verdorCode.length()>0) {
							%>
							<option value="<%=verdorCode %>" <%=verdorCode.equals(BeanHelper.getHtmlValueByColumn(ref, "FIRST_FT_TEST_HOUSE"))?"selected":"" %>><%=to.getShortName() %></option>
							<%
									}
								}
							%>
						</select></label></td>
				    </tr>
					<tr>
					  <th>Remark</th>
					  <td><input class="text" name="remark" id="remark" value="<%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %>"></td>
				    </tr>
					<tr>
						<th width="180"><div class="erp_label">Multiple Stage</div></th>
						<td>
						<input type="checkbox" id="multipleStage" name="multipleStage" value="1" <%=ref!=null && ref.getMultipleStage()?"checked":"" %>>
						</td>
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
                          <td><input name="button" type="button" class="button" id="userBtn" value="User" >
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
							</script>
                          </td>
                          <td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
                        </tr>
                        <tr>
                          <td><input name="button" type="button" class="button" id="roleBtn" value="Role" >
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
							</script>
                          </td>
                        </tr>
                      </table></td>
				    </tr>
				</tbody>
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
						
						<input name="releaseBtn" type="button" class="erp_button" id="releaseBtn" value="Release To ERP" onClick="doReleaseToERP('erp')">
						<input name="saveBtn" type="button" class="button" id="saveBtn" value="Save" onClick="doSave()">
						
					           <%
						   }     
						%>
						
						<input name="resetBtn" type="reset" class="button" id="resetBtn" value="Reset">
					</div>
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
