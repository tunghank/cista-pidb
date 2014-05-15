<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.PkgRaTo"%>
<%@ page import="com.cista.pidb.md.to.PkgRaQueryTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.to.SapMasterPackageTypeTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterPackageTypeDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function doSave() {
		if ($F('prodName') == "") {
			setMessage("error", "Product Name is must field.");
			return;
		}
		if ($F('pkgCode') == "") {
			setMessage("error", "Package Code is must field.");
			return;
		}
		if ($F('worksheetNumber') == "") {
			setMessage("error", "WorkSheet Number is must field.");
			return;
		}
		
		if ($F('pkgType') == "") {
			setMessage("error", "Package Type is must field.");
			return;
		}
		
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_pkgra_exist.do',
			{
				method: 'get',
				parameters: 'prodName='+ $F('prodName') + '&pkgCode=' + $F('pkgCode')+'&worksheetNumber='+$F('worksheetNumber')+'&pkgType='+$F('pkgType'),
				onComplete: checkPkgRaExistComplete
			}
		);
	}
	
function checkPkgRaExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "The package ra is already exist.");		
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		setMessage("error", "Saving package ra...");		
		selectAllOptions($('assignTo'));
										
		$('createForm').action = $('createForm').action + "?m=save";
		$('createForm').submit();		
	}
}
	
function getPkgCodeHandle(inputField, columns, value) {
	if (value != null && value.length > 0) {
		 $('pkgCode').value = value[0]["PKG_CODE"];
		new Ajax.Request(
				'<%=cp%>/ajax/package_type_list.do',
				{
					method: 'post',
					parameters: 'prodName='+$F('prodName')+"&pkgCode="+$F('pkgCode'),
					onComplete: fetchPkgTypeComplete
				}
			);
		 }
	}

function fetchPkgTypeComplete(r) {
	var returnValue = r.responseText.split("|");
	if (returnValue.length > 0){
		$('pkgType').value = returnValue[0];
	}
	getAssemblySite();
}

function getAssemblySite() {
	clearList();
		new Ajax.Request(
				'<%=cp%>/ajax/assembly_site_list.do',
				{
					method: 'post',
					parameters: 'pkgType='+$F('pkgType'),
					onComplete: fetchAssemblySiteComplete
				}
			);
		 }

function fetchAssemblySiteComplete(r) {
	
	var returnValue = r.responseText.split("|");	
			if (returnValue.length > 0) {
				for (i = 0 ; i < returnValue.length; i++) {
					if ( returnValue[i].length > 1 ) {
							addOption($('assySite'), returnValue[i], returnValue[i]);
						}
				}
				if ( $('assySite').length == 2 ){
					removeOption($('assySite'),0);
					}else if ( $('assySite').length > 2 ) {
					}else{
						removeOption($('assySite'));
					}
			}
	}


function clearList(){
	var len = document.forms(0).assySite.length;
	for (j = 0 ; j < len; j++  ){
		document.forms(0).assySite[0] = null;
	}

	document.forms(0).assySite[0]=new Option( '--Select--', '', false);
}

</script>
</head>
<body>
<%
	PkgRaTo ref = (PkgRaTo) request.getAttribute("ref");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
		<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td class="pageTitle">Master Data :: Create Package RA</td>
			  </tr>
			</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/pkg_ra_create.do" method="post">
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
					<input name="createBy" id="createBy" type="text" class="text_protected" readonly 
					value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly 
					value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY") %>">
					&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><span class="star">*</span> Product Name</th>
					    <td width="30%">
						<input class="text" type="text" name="prodName" id="prodName" readonly >
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                title:"PRODUCT NAME",
                                table:"PIDB_PRODUCT",
                                columns:"PROD_NAME",
                                keyColumn:"PROD_NAME",
                                autoSearch:false,
                                mode:0
							});
						</script>&nbsp;</td>
					    <th>PKG RA real start date</th>
						  <td><input class="text" type="text" name="pkgRaActualStartTime" id="pkgRaActualStartTime" readonly
						  value="<%=BeanHelper.getHtmlValueByColumn(ref,"PKG_RA_ACTUAL_START_TIME","yyyy/MM/dd") %>" >
						  <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="pkgRaRealStartBtn">
						  <script type="text/javascript">
								Calendar.setup({
									inputField:"pkgRaActualStartTime",
									ifFormat:"%Y/%m/%d",
									button:"pkgRaRealStartBtn"
								});
						  </script>&nbsp;</td>					   
					</tr>
					<tr>
						<th><span class="star">*</span> Package Code</th>
					    <td>
						<input class="text" type="text" name="pkgCode" id="pkgCode" readonly >
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="pkgCodeSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pkgCodeSSBtn",
                                inputField:"pkgCode",
                                title:"PKG CODE",
                                table:"PIDB_IC_FG fg",
                                columns:"PKG_CODE",
                                keyColumn:"PKG_CODE",
                                autoSearch:false,
                                mode:0,
								whereCause:"fg.prod_code in ( SELECT c.prod_code FROM pidb_product c where c.prod_name={prodName})",
								callbackHandle:"getPkgCodeHandle"
							});
							</script>&nbsp;
						</td>
					    <th>PKG RA real finish date</th>
						  <td><input class="text" type="text" name="pkgRaActualFinishTime" id="pkgRaActualFinishTime" readonly
						  value="<%=BeanHelper.getHtmlValueByColumn(ref,"PKG_RA_ACTUAL_FINISH_TIME","yyyy/MM/dd") %>" >
						  <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="pkgRaRealFinStartBtn">
						  <script type="text/javascript">
								Calendar.setup({
									inputField:"pkgRaActualFinishTime",
									ifFormat:"%Y/%m/%d",
									button:"pkgRaRealFinStartBtn"
								});
						  </script>&nbsp;</td>					    
					</tr>
					<tr>
						<th><span class="star">*</span> Package Type</th> 
					    <td>
					    <input class="text_protected" type="text" name="pkgType" id="pkgType" readonly>
					    
					    </td>
					     <th width="20%">RA Test Result</th>
						<td width="30%">
						<select name="raTestResult" id="raTestResult" class=select_w130"">
                          <option value="">--Select--</option>
                          <option value="Pass"  
						  <%="Pass".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_RESULT"))?"selected":"" %>> Pass</option>
                          <option value="Fail" 
						  <%="Fail".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_RESULT"))?"selected":"" %>>Fail</option>
                        </select>&nbsp;</td>                        
					</tr>
					<tr>
						<th width="20%"><span class="star">*</span> Worksheet Number</th>
						<td width="30%">
						<input class="text" type="text" name="worksheetNumber" id="worksheetNumber" value="<%=BeanHelper.getHtmlValueByColumn(ref, "WORKSHEET_NUMBER") %>"></td>
						<th>RA Test Report Doc</th>
					  	<td><input class="text_200" type="text" name="raTestReportDoc" id="raTestReportDoc"
						value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_REPORT_DOC") %>"></td>	
					</tr>
					<tr>
						<th width="20%">Tape Name</th>
							<td width="30%">
							<input class="text" readonly type="text" name="tapeName" id="tapeName">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeNameSSBtn">
							<script type="text/javascript">
								SmartSearch.setup({
									cp:"<%=cp%>",
									button:"tapeNameSSBtn",
									inputField:"tapeName",
									title:"Tape Name",
									table:"PIDB_IC_TAPE ic",
									columns:"TAPE_NAME",
									keyColumn:"TAPE_NAME",
									autoSearch:false,
									mode:0,
									whereCause:"ic.PKG_CODE={pkgCode}"
								});
							</script>
							&nbsp;</td>
						<th>Report Version</th>
					    <td><select name="rptVersionList" id="rptVersionList" style="width:150px">
                        <option value="">--Select--</option>                        
							<%
									List<FunctionParameterTo> reportVersionList = (List<FunctionParameterTo>) request.getAttribute("reportVersionList");
									for (FunctionParameterTo reportVersion : reportVersionList) {
										String v = reportVersion.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION_LIST"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=reportVersion.getFieldShowName() %></option>
										<%
									}
								%>				
						</select>
                      </select></td>

					</tr>
					<tr>
						<th>Tape Vendor</th>
						<td><input class="text" type="text" name="tapeVendor" id="tapeVendor" readonly>
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeVenSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeVenSSBtn",
                                inputField:"tapeVendor",
                                title:"Tape Vendor",
                                table:"WM_SAP_MASTER_VENDOR sap,PIDB_IC_TAPE ic",
                                columns:"SHORT_NAME,VENDOR_CODE",
                                keyColumn:"sap.SHORT_NAME",
                                autoSearch:false,
                                mode:0,
                                whereCause:"pidb_include(ic.TAPE_VENDOR,',',sap.VENDOR_CODE)>=1 and ic.PKG_CODE={pkgCode}"
							});
							</script>					
						&nbsp;</td>	
						 <th rowspan="3">Remark</th>
							<td rowspan="3"><textarea class="textarea" cols="55" rows="4" name="remark" id="remark"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					</tr>
					<tr>
						<th width="20%"></span> Part Number</th>
						<td width="30%">
						<input class="text_200" type="text" name="partNum" id="partNum" readonly>
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="partNumSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"partNumSSBtn",
                                inputField:"partNum",
                                title:"Part Number",
                                table:"PIDB_IC_FG fg",
                                columns:"PART_NUM",
                                keyColumn:"PART_NUM",
                                autoSearch:false,
                                mode:0,
                                //whereCause:"fg.PROD_NAME={prodName}",
								whereCause:"fg.pkg_code={pkgCode} and fg.prod_code in ( SELECT c.prod_code FROM pidb_product c where c.prod_name={prodName})"
								
							});
						</script>
						&nbsp;
						</td>								
					</tr>
					<tr>
					  <th>Assembly Site</th>
					  <td><select class="select_w130" name="assySite" id="assySite">
							<option value="">--Select--</option>
										
						</select></td>
					</tr>
					<tr>
						<th>Customer</th>
							<td>
							<input class="text" readonly type="text" name="cust" id="cust" 
							  value="<%=BeanHelper.getHtmlValueByColumn(ref,"CUST") %>" >
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="custSSBtn">
							<script type="text/javascript">
								SmartSearch.setup({
									cp:"<%=cp%>",
									button:"custSSBtn",
									inputField:"cust",
									title:"Customer",
									table:"WM_SAP_MASTER_CUSTOMER",
									columns:"SHORT_NAME,customer_code",
									keyColumn:"SHORT_NAME",
									autoSearch:false,
									mode:0
								});
							</script>
							&nbsp;</td>
						<th width="20%"><div>Owner</div></th>
					  	<td width="30%">
						<select class="select_w130" name="owner" id="owner">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> ownerList = (List<FunctionParameterTo>) request.getAttribute("ownerList");
									for (FunctionParameterTo owner : ownerList) {
										String v = owner.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "OWNER"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=owner.getFieldShowName() %></option>
										<%
									}
								%>				
						</select></td>
						</tr>					
					<tr>
					  <th rowspan="7">PKG RA Test Item</th>
					  <td rowspan="7">
					  <%if( ref != null && ref.getPkgRaTestItemHtst() == true) {%>
					    <input type="checkbox" name="pkgRaTestItemHtst" value="Y" checked>
					  <%} else { %>
					  	<input type="checkbox" name="pkgRaTestItemHtst" value="Y">
					  <%} %>
				      HTST
				      <br><%if( ref != null && ref.getPkgRaTestItemLtst() == true) {%>
				      	<input type="checkbox" name="pkgRaTestItemLtst" value="Y" checked>
				      <%} else { %> 
				      	<input type="checkbox" name="pkgRaTestItemLtst" value="Y">
				      <%} %>
				      LTST
				      <br><%if( ref != null && ref.getPkgRaTestItemTht() == true) {%>
				     	 <input type="checkbox" name="pkgRaTestItemTht" value="Y" checked>
				      <%} else { %>
				     	<input type="checkbox" name="pkgRaTestItemTht" value="Y">
				      <%} %>
				      THT
				      <br><%if( ref != null && ref.getPkgRaTestItemPct() == true) {%>
				      	<input type="checkbox" name="pkgRaTestItemPct" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemPct" value="Y">
				      <%} %>
				      PCT
				      <br><%if( ref != null && ref.getPkgRaTestItemTct() == true) {%>
				      	<input type="checkbox" name="pkgRaTestItemTct" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemTct" value="Y">
				      <%} %>
				      TCT
				      <br><%if( ref != null && ref.getPkgRaTestItemTst() == true) {%>
				      	<input type="checkbox" name="pkgRaTestItemTst" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemTst" value="Y">
				      <%} %>
				      TST
				      <br><%if( ref != null && ref.getPkgRaTestItemMst() == true) {%>
				     	<input type="checkbox" name="pkgRaTestItemMst" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemMst" value="Y">
				      <%} %>
				      Pre-condition (MST)
					  <!--Added 2007/09/18 Hank Tang-->
					  <br><%if( ref != null && ref.getPkgRaTestItemSolderability() == true) {%>
				     	<input type="checkbox" name="pkgRaTestItemSolderability" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemSolderability" value="Y">
				      <%} %>
				      Solderability
					  <br><%if( ref != null && ref.getPkgRaTestItemLeadFatigue() == true) {%>
				     	<input type="checkbox" name="pkgRaTestItemLeadFatigue" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemLeadFatigue" value="Y">
				      <%} %>
				      Lead Fatigue					  
					  <br><%if( ref != null && ref.getPkgRaTestItemMarkPerman() == true) {%>
				     	<input type="checkbox" name="pkgRaTestItemMarkPerman" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemMarkPerman" value="Y">
				      <%} %>
				      Mark Permanency						  
					  <br><%if( ref != null && ref.getPkgRaTestItemOthers() == true) {%>
				     	<input type="checkbox" name="pkgRaTestItemOthers" value="Y" checked>
				      <%} else { %>
				      	<input type="checkbox" name="pkgRaTestItemOthers" value="Y">
				      <%} %>
				     <font color='red'>Others</font>	
					  <br>
					  <textarea class="text" type="text" cols="40" rows="2" name="pkgRaTestItemOthersText" id="pkgRaTestItemOthersText"><%=BeanHelper.getHtmlValueByColumn(ref, "PKG_RA_TEST_ITEM_OTHERS_TEXT") %>
					  </textarea>
					  </td>
				      <th  ><div>AssignTo</div></th>
					  	<td  >
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
					  	<td>
					  	<input id="userBtn" type="button" class="button" value="User" >
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
							<td>
					  	<input id="roleBtn" type="button" class="button" value="Role" >
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
						</table>												  	
					  	</td>
					</tr>
					<tr>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr><th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr><th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr><th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr><th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr><th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
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
					 
					 <input name="saveBtn" type="button" class="button" id="saveBtn" value="Save" onClick="doSave()">
					  &nbsp;
					  	   <%
						   }     
						%>
					  
					  <input name="resetBtn" type="reset" class="button" id="resetBtn" value="Reset">
					</div>
					</td>
				</tr>
			</table>
			</form>
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
</body>
</html>
