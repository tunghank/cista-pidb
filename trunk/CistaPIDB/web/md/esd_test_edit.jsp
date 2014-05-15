<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.dao.EsdTestDao"%>
<%@ page import="com.cista.pidb.md.to.EsdTestTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="java.util.List" %>
<%
	EsdTestTo ref = (EsdTestTo) request.getAttribute("ref");
	String failMessage = (String)request.getAttribute("error");
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function doSave() {
		if ($F('prodCode') == "") {
			setMessage("error", "Project code is must field.");
			return;
		}
		if ($F('projCodeWVersion') == "") {
			setMessage("error", "Project Code w Version is must field.");
			return;
		}
		if ($F('idEsdTesting')==""){
			setMessage("error", "ID_ESD_Testing is must field.");
			return;
		}
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		
		setMessage("error", "Updating esd test...");	
		selectAllOptions($('assignTo'));
											
		$('createForm').action = $('createForm').action + "?m=save";
		$('createForm').submit();
	}
	
</script>
</head>
<body>

<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td class="pageTitle">Master Data :: Modify ESD Test </td>
			  </tr>
			</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/esd_test_edit.do" method="post">
			<table class="formErrorAndButton">
				<tbody>
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />
					<%if (failMessage != null && failMessage.length() > 0) { %>
						<%=failMessage %>
					<%} %>
					&nbsp;</div>
					</td>
					<td>
					<div align="right">
					Created by 
					<input name="createBy" id="createBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY") %>">
					&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
  					<th width="20%"><span class="star">*</span> Product Code</th>
					  <td width="30%">
					  	<input type="text" class="text_protected" name="prodCode" id="prodCode" value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROD_CODE") %>" readonly>
					  	 
					  	&nbsp;
					  </td>
					   <th rowspan="2">Results--HBM</th>
					  <td rowspan="2"><textarea class="textarea" type="text" cols="42" rows="2" name="resultsHbm" id="resultsHbm"
					  ><%=BeanHelper.getHtmlValueByColumn(ref,"RESULTS_HBM")%></textarea>
						&nbsp;
					  	<select  class="select_w80" name="resultsHbmPf" id="resultsHbmPf">
					      <option value="">--Select--</option>
					      <option value="PASS" <%="PASS".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_HBM_PF"))?"selected":"" %> >PASS</option> 
					      <option value="FAIL" <%="FAIL".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_HBM_PF"))?"selected":"" %>>FAIL</option>
				        </select>
					  </td>
					</tr>
					<tr>
						<th width="20%"><span class="star">*</span> Project Code w Version</th>
					  	<td width="30%">
					  	<input type="text" class="text_protected" name="projCodeWVersion" id="projCodeWVersion" value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE_W_VERSION") %>" readonly>
					  	&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><span class="star">*</span>ID_ESD_Testing</th>
						<td width="30%">
					  	<input type="text" class="text" name="idEsdTesting" id="idEsdTesting" value="<%=BeanHelper.getHtmlValueByColumn(ref,"ID_ESD_TESTING") %>">
					  &nbsp;</td>					
						<th rowspan="2">Results--MM</th>
						<td rowspan="2"><textarea class="textarea" type="text" cols="42" rows="2" name="resultsMm" id="resultsMm" 
					  ><%=BeanHelper.getHtmlValueByColumn(ref,"RESULTS_MM") %></textarea>
					  	&nbsp;
					  	<select  class="select_w80" name="resultsMmPf" id="resultsMmPf">
					      <option value="">--Select--</option>
					      <option value="PASS" <%="PASS".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_MM_PF"))?"selected":"" %> >PASS</option> 
					      <option value="FAIL" <%="FAIL".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_MM_PF"))?"selected":"" %>>FAIL</option>
				        </select>
					  </td>
					</tr>
					<tr>
						<th>Priority</th>
						<td>
						   <select  class="select_w130" name="priority" id="priority">
						     <option value="">--Select--</option>
						     <option value="Urgent" <%="Urgent".equals(BeanHelper.getHtmlValueByColumn(ref, "PRIORITY"))?"selected":"" %> >Urgent</option> 
						   <option value="High" <%="High".equals(BeanHelper.getHtmlValueByColumn(ref, "PRIORITY"))?"selected":"" %>>High</option>
						   <option value="Normal" <%="Normal".equals(BeanHelper.getHtmlValueByColumn(ref, "PRIORITY"))?"selected":"" %>>Normal</option>
						 </select>
						 &nbsp;
						 </td>		
					</tr>
					<tr>
						<th>Testing House</th>
						<td>
					     <select class="select_w130" name="testingHouse" id="testingHouse">
					      <option value="">--Select--</option>
					      <option value="Himax" <%="Himax".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":"" %>>Himax</option> 
					      <option value="Himax/IST" <%="Himax/IST".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":"" %>>Himax/IST</option>
					      <option value="Himax/Ma-tek" <%="Himax/Ma-tek".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":"" %>>Himax/Ma-tek</option>
					      <option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":""%>>IST</option>
					      <option value="Ma-tek" <%="Ma-tek".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":"" %>>Ma-tek</option>
						  <option value="Spirox" <%="Spirox".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":"" %>>Spirox</option>
						  <option value="ITRI" <%="ITRI".equals(BeanHelper.getHtmlValueByColumn(ref, "TESTING_HOUSE"))?"selected":"" %>>ITRI</option>
				        </select>
				        &nbsp;
					  </td>			  
					  <th rowspan="2">Results--CDM</th>
					  <td rowspan="2"><textarea class="textarea" type="text" cols="42" rows="2" name="resultsCdm" id="resultsCdm" 
					  ><%=BeanHelper.getHtmlValueByColumn(ref,"RESULTS_CDM") %></textarea>
					  	&nbsp;
					  	<select  class="select_w80" name="resultsCdmPf" id="resultsCdmPf">
					      <option value="">--Select--</option>
					      <option value="PASS" <%="PASS".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_CDM_PF"))?"selected":"" %> >PASS</option> 
					      <option value="FAIL" <%="FAIL".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_CDM_PF"))?"selected":"" %>>FAIL</option>
				        </select>
					 </td>
					 </tr>
					 <tr>
						<th>Test to Fail?</th>
						<td>
						 <select class="select_w130" name="testToFail" id="testToFail">
						  <option value="">--Select--</option>
						  <option value="Y" <%="Y".equals(BeanHelper.getHtmlValueByColumn(ref, "TEST_TO_FAIL"))?"selected":"" %>>YES</option>
						  <option value="N" <%="N".equals(BeanHelper.getHtmlValueByColumn(ref, "TEST_TO_FAIL"))?"selected":"" %>>NO</option>
						 </select>&nbsp;
						</td>
					</tr>
					 <tr>
						<th>Lot ID</th>
						<td><input type="text" class="text" name="lotId" id="lotId" 
							value="<%=BeanHelper.getHtmlValueByColumn(ref,"LOT_ID") %>">
							&nbsp;
						</td>
						<th rowspan="2">Results--LU</th>
					    <td rowspan="2"><textarea class="textarea" type="text" cols="42" rows="2" name="resultsLu" id="resultsLu" 
					  ><%=BeanHelper.getHtmlValueByColumn(ref,"RESULTS_LU") %></textarea>
					  	&nbsp;
					  	<select  class="select_w80" name="resultsLuPf" id="resultsLuPf">
					      <option value="">--Select--</option>
					      <option value="PASS" <%="PASS".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_LU_PF"))?"selected":"" %> >PASS</option> 
					      <option value="FAIL" <%="FAIL".equals(BeanHelper.getHtmlValueByColumn(ref, "RESULTS_LU_PF"))?"selected":"" %>>FAIL</option>
				        </select>
					  </td>
					  </tr>
					  <tr>
						<th rowspan="4">Test Mode</th>
						<td>
					  <%if( ref != null && ref.getTestHbm() == true) {%>
					    <input type="checkbox" name="testHbm" value="Y" checked>
					  <%} else { %>
					  	<input type="checkbox" name="testHbm" value="Y">
					  <%} %>
						HBM&nbsp
						<select class="select_w130" name="testHbmEa" id="testHbmEa">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> testModeList = (List<FunctionParameterTo>) request.getAttribute("testModeList");
									for (FunctionParameterTo hbm : testModeList) {
										String v = hbm.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "TEST_HBM_EA"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=hbm.getFieldShowName() %></option>
										<%
									}
								%>				
						</select></td>
					  </tr>
					  <tr>
						 <td>
					  <%if( ref != null && ref.getTestMm() == true) {%>
					    <input type="checkbox" name="testMm" value="Y" checked>
					  <%} else { %>
					  	<input type="checkbox" name="testMm" value="Y">
					  <%} %>
					     MM&nbsp;&nbsp;&nbsp;
						<select class="select_w130" name="testMmEa" id="testMmEa">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> testModeMmList = (List<FunctionParameterTo>) request.getAttribute("testModeList");
									for (FunctionParameterTo mm : testModeMmList) {
										String v = mm.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "TEST_MM_EA"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=mm.getFieldShowName() %></option>
										<%
									}
								%>				
						</select></td>
					  <th>FA Request?</th>
					  <td><select class="select_w130" name="faRequest" id="faRequest">
					    <option value="">--Select--</option>
					    <option value="Y" <%="Y".equals(BeanHelper.getHtmlValueByColumn(ref, "FA_REQUEST"))?"selected":"" %>>YES</option>
					    <option value="N" <%="N".equals(BeanHelper.getHtmlValueByColumn(ref, "FA_REQUEST"))?"selected":"" %>>NO</option>
					    </select>
						&nbsp;</td>
					  </tr>					  
					  <tr>
						 <td>
					  <%if( ref != null && ref.getTestCdm() == true) {%>
					    <input type="checkbox" name="testCdm" value="Y" checked>
					  <%} else { %>
					  	<input type="checkbox" name="testCdm" value="Y">
					  <%} %>
					    CDM&nbsp;					
						<select class="select_w130" name="testCdmEa" id="testCdmEa">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> testModeCdList = (List<FunctionParameterTo>) request.getAttribute("testModeList");
									for (FunctionParameterTo cd : testModeMmList) {
										String v = cd.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "TEST_CDM_EA"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=cd.getFieldShowName() %></option>
										<%
									}
								%>				
						</select>
						</td>
						 <th>ESD RA Test Report Doc.</th>
						<td><input type="text" class="text_200" name="esdRaTestRptDoc" id="esdRaTestRptDoc" value="<%=BeanHelper.getHtmlValueByColumn(ref,"ESD_RA_TEST_RPT_DOC") %>">
					  </td>
					  </tr>
					  <tr>
						<td>
					  <%if( ref != null && ref.getTestLu() == true) {%>
					    <input type="checkbox" name="testLu" value="Y" checked>
					  <%} else { %>
					  	<input type="checkbox" name="testLu" value="Y">
					  <%} %>					  
					   LU&nbsp;&nbsp;&nbsp;&nbsp;
						<select class="select_w130" name="testLuEa" id="testLuEa">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> testModeLuList = (List<FunctionParameterTo>) request.getAttribute("testModeList");
									for (FunctionParameterTo lu : testModeMmList) {
										String v = lu.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "TEST_LU_EA"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=lu.getFieldShowName() %></option>
										<%
									}
								%>				
						</select>
						</td>
						<th>ESD Report Version</th>
					    <td>
					    <select name="esdRptVersion" id="esdRptVersion" class="select_w130">
					      <option value="">--Select--</option>
					      <option value="01" <%="01".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>01</option> 
					      <option value="02" <%="02".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>02</option>
					      <option value="03" <%="03".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>03</option>
					      <option value="04" <%="04".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>04</option>
					      <option value="05" <%="05".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>05</option>
					      <option value="06" <%="06".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>06</option>
					      <option value="07" <%="07".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>07</option>
					      <option value="08" <%="08".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>08</option>
					      <option value="09" <%="09".equals(BeanHelper.getHtmlValueByColumn(ref, "ESD_RPT_VERSION"))?"selected":"" %>>09</option>
				        </select>
					  &nbsp;
					  </td>
					  </tr>
					  <tr>
						<th>Sample Receive Date</th> 
					    <td><input type="text" class="text" name="sampleReceiveDate" id="sampleReceiveDate" readonly  value="<%=BeanHelper.getHtmlValueByColumn(ref,"SAMPLE_RECEIVE_DATE", "yyyy/MM/dd")%>" >
					    <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="sampleReceiveDateBrn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"sampleReceiveDate",
								ifFormat:"%Y/%m/%d",
								button:"sampleReceiveDateBrn"
							});
						</script>
						&nbsp;
					   </td>
						<th rowspan="2">Remark</th>
						<td rowspan="2"><textarea class="textarea" type="text" cols="60" rows="2" name="remark" id="remark"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					  </tr>
					  <tr>
						<th>ESD Finish Date</th> 
					    <td><input type="text" class="text" name="esdFinishDate" id="esdFinishDate" readonly					  value="<%=BeanHelper.getHtmlValueByColumn(ref,"ESD_FINISH_DATE", "yyyy/MM/dd")%>" >
					    <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="esdFinishDateBrn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"esdFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"esdFinishDateBrn"
							});
						</script>
						&nbsp;
					   </td>
					  </tr>
					  <tr>
					    <th  rowspan="2"><div>AssignTo</div></th>
					  	<td  rowspan="2">
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
						<th>&nbsp;</th>
						<td><input type="hidden" id="createdBy" name="createdBy" 
					 	value="<%=BeanHelper.getHtmlValueByColumn(ref,"CREATED_BY") %>">&nbsp;</td>				
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
					  
					  <input name="resetBtn" type="button" class="button" id="resetBtn" value="Reset">
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
