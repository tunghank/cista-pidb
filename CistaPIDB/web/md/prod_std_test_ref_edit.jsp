<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.ProdStdTestRefTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterProductFamilyDao"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
ProdStdTestRefTo ref = (ProdStdTestRefTo) request.getAttribute("ref");


%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function doSave() {

	
	if ($F('cpIndexTimeE') == "") {	
		setMessage("error", "CP Index Time(E) is must field.");
		return;
	}
	if ($F('cpCpuTimeE') == "") {
		setMessage("error", "CP CPU Time(E) is must field.");
		return;
	}
	if ($F('cpTesterE') == "") {
		setMessage("error", "CP Tester(E) is must field.");
		return;
	}
	if ($F('ftIndexTimeE') == "") {	
		setMessage("error", "FT Index Time(E) is must field.");
		return;
	}
	if ($F('ftCpuTimeE') == "") {
		setMessage("error", "FT CPU Time(E) is must field.");
		return;
	}
	if ($F('ftTesterE') == "") {
		setMessage("error", "FT CPU Time(E) is must field.");
		return;
	}
	if ($F('cpContactDieQty') == "") {
		setMessage("error", "CP Contact Die Quantity is must field.");
		return;
	}
	if ($F('ftContactDieQty') == "") {
		setMessage("error", "FT Contact Die Quantity is must field.");
		return;
	}
	if ($F('cpCpuTimeE') != "" && !IsNumber($F('cpCpuTimeE'))) {
		setMessage("error", "CP CPU Time(E) is must number.");
		return;		
	}		
	if ($F('cpIndexTimeE') != "" && !IsNumber($F('cpIndexTimeE'))) {
		setMessage("error", "CP Index Time(E) is must number.");
		return;		
	}			
	if ($F('ftIndexTimeE') != "" && !IsNumber($F('ftIndexTimeE'))) {
		setMessage("error", "FT Index Time(E) is must number.");
		return;		
	}	
	if ($F('ftCpuTimeE') != "" && !IsNumber($F('ftCpuTimeE'))) {
		setMessage("error", "FT CPU Time(E) is must number.");
		return;		
	}	
	
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	
		
		var submitForm = true;
	if (submitForm ) {
		setMessage("error", "Saving product standard test reference...");
		selectAllOptions($('assignTo'));
		
		$('prodStdTestRefEdit').submit();	
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
<form name="prodStdTestRefEdit"
	action="<%=cp %>/md/prod_std_test_ref_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: Modify Product Standard Test Reference </td>
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
					  <th width="180"><div class="erp_label"><span class="star">*</span>Test Reference ID</div></th>
						<td><input class="text_protected" readonly type="text" id="testReferenceId" name="testReferenceId" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TEST_REFERENCE_ID") %>"></td>
					</tr>
					<tr>
					  <th width="180"><span class="star">*</span>Variant</th>
					  <td><input class="text_protected" readonly type="text" id="variant" name="variant" value="<%=BeanHelper.getHtmlValueByColumn(ref, "VARIANT") %>" ></td>
					</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span>Product Line</div></th>
						<td><input class="text_protected" readonly type="text" id="productLine" name="productLine" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PRODUCT_LINE") %>"></td>
					</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span>Product Family</div></th>
					  <% 
						  SapMasterProductFamilyDao sapMasterProductFamilyDao = new SapMasterProductFamilyDao();
						  String desc = sapMasterProductFamilyDao.findDescByProdFamily(BeanHelper.getHtmlValueByColumn(ref, "PRODUCT_FAMILY"));					  
					  %>
					  <td><input class="text_protected" readonly type="text" id="productFamilyDesc" name="productFamilyDesc" value="<%=desc %>">
					  <input type="hidden" id="productFamily" name="productFamily" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PRODUCT_FAMILY") %>">
					  </td>
					</tr>
					<tr>
					<th>Sub Classification</th>
					<td><input type="text" class="text" name="subClassification" id="subClassification" value="<%=BeanHelper.getHtmlValueByColumn(ref, "SUB_CLASSIFICATION")%>"/></td>
					</tr>					
					<tr>
					  <th><div class="erp_label"><span class="star">*</span>CP Index Time(E)</div></th>
						<td><input class="text_required" type="text" id="cpIndexTimeE" name="cpIndexTimeE" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_INDEX_TIME_E") %>" ></td>
					</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span>CP CPU Time(E)</div></th>
					  <td><input class="text_required" type="text" id="cpCpuTimeE" name="cpCpuTimeE" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_CPU_TIME_E") %>" ></td>
					</tr>
					<tr>
						<th><div class="erp_label"><span class="star">*</span>CP Tester(E)</div></th>
						<td><select id="cpTesterE" name="cpTesterE" class="select_w130_required">
						    <option value="">--Select--</option>
					        <option value="ND1" <%="ND1".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>ND1</option>
					        <option value="TS6700" <%="TS6700".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>TS6700</option>
					        <option value="ST6730" <%="ST6730".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>ST6730</option>
					        <option value="J750" <%="J750".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>J750</option>
					        <option value="AMIDA-1000" <%="AMIDA-1000".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>AMIDA-1000</option>
					        <option value="HP93000" <%="HP93000".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>HP93000</option>
					        <option value="HP83000" <%="HP83000".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>HP83000</option>
					        <option value="ASL-1000" <%="ASL-1000".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>ASL-1000</option>
					        <option value="Chroma" <%="Chroma".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>Chroma</option>
					        <option value="T7315" <%="T7315".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>T7315</option>
					        <option value="TIGER" <%="TIGER".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>TIGER</option>
					        <option value="SC312" <%="SC312".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>SC312</option>
					        <option value="V V" <%="V V".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>V V</option>
					        <option value="UltraFLEX" <%="UltraFLEX".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>UltraFLEX</option>
					        <option value="IP750" <%="IP750".equals(BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_E"))?"selected":"" %>>IP750</option>
					        </select></td>
				  	</tr>
					<tr>
					<th><div class="erp_label"><span class="star">*</span> CP Contact Die Quantity:</div></th>
					<td>
					<select name="cpContactDieQty" class="select_w130">
					  <option value="">--select--</option>
					  <option value="1">1</option>
					  <option value="2">2</option>
					  <option value="4">4</option>
					</td>
					</tr>
					<script>$('cpContactDieQty').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "CP_Contact_Die_QTY") %>";</script>				  	
					<tr>
					  <th><div class="erp_label">CP Tester Spec(E)</div></th>
					  <td><input class="text" type="text" id="cpTesterSpecE" name="cpTesterSpecE" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_TESTER_SPEC_E") %>" ></td>
					</tr><tr>
						<th><div class="erp_label"><span class="star">*</span>FT Index Time(E)</div></th>
						<td><input class="text_required" type="text" id="ftIndexTimeE" name="ftIndexTimeE" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_INDEX_TIME_E") %>" ></td>					
						</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span>FT CPU Time(E)</div></th>
					  <td><input class="text_required" type="text" id="ftCpuTimeE" name="ftCpuTimeE" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_CPU_TIME_E") %>" ></td>
					</tr>
					<tr>
						<th><div class="erp_label"><span class="star">*</span>FT Tester(E)</div></th>
					  	<td><select id="ftTesterE" name="ftTesterE" class="select_w130_required">
					     	<option value="">--Select--</option>
					        <option value="ND1" <%="ND1".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>ND1</option>
					        <option value="TS6700" <%="TS6700".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>TS6700</option>
					        <option value="ST6730" <%="ST6730".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>ST6730</option>
					        <option value="J750" <%="J750".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>J750</option>
					        <option value="AMIDA-1000" <%="AMIDA-1000".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>AMIDA-1000</option>
					        <option value="HP93000" <%="HP93000".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>HP93000</option>
					        <option value="HP83000" <%="HP83000".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>HP83000</option>
					        <option value="ASL-1000" <%="ASL-1000".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>ASL-1000</option>
					        <option value="Chroma" <%="Chroma".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>Chroma</option>
					        <option value="T7315" <%="T7315".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>T7315</option>
					        <option value="TIGER" <%="TIGER".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>TIGER</option>
					        <option value="SC312" <%="SC312".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>SC312</option>
					        <option value="V V" <%="V V".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>V V</option>
					        <option value="UltraFLEX" <%="UltraFLEX".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>UltraFLEX</option>
					        <option value="IP750" <%="IP750".equals(BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_E"))?"selected":"" %>>IP750</option>					        
					        </select></td>
					</tr>
					<tr>
					<th><div class="erp_label"><span class="star">*</span> FT Contact Die Quantity:</div></th>
					<td>
					<select name="ftContactDieQty" class="select_w130">
					  <option value="">--select--</option>
					  <option value="1">1</option>
					  <option value="2">2</option>
					  <option value="4">4</option>
					</td>
					</tr>
					<script>$('ftContactDieQty').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "FT_Contact_Die_QTY") %>";</script>					
					<tr>
					  <th><div class="erp_label">FT Tester Spec(E)</div></th>
					  <td><input class="text" type="text" id="ftTesterSpecE" name="ftTesterSpecE" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FT_TESTER_SPEC_E") %>" ></td>
					</tr>
					<tr>
					  <th>Note</th>
						<td><input class="text" type="text" id="note" name="note" value="<%=BeanHelper.getHtmlValueByColumn(ref, "NOTE") %>" ></td>
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
