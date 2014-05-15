<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>

<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.CornerLotCharTo"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="java.util.List" %>

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
		if ($F('projCodeWVersion') == "") {
			setMessage("error", "Project Code w Version is must field.");
			return;
		}
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_cornerLotChar_exist.do',
			{
				method: 'get',
				parameters: 'wversion='+ $F('projCodeWVersion'),
				onComplete: checkCornerLotCharExistComplete
			}
		);	
	}	
	
	function checkCornerLotCharExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "corner lot char is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {		
		setMessage("error", "Saving corner lot char...");	
		selectAllOptions($('assignTo'));									
		$('createForm').action = $('createForm').action + "?m=save";
		$('createForm').submit();
	}
}
</script>
</head>

<body>
<%
	CornerLotCharTo ref = (CornerLotCharTo) request.getAttribute("ref");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td class="pageTitle">Master Data :: Create Corner Lot Characterization</td>
			  </tr>
			</table>
			
			<div class="content">
			<form id="createForm" action="<%=cp %>/md/corner_lot_char_create.do" method="post">
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
						<th width="180"><span class="star">*</span> Product Code</th>
					  	<td width=""><input class="text" type="text" id="prodCode" name="prodCode" readonly
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROD_CODE") %>">
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
						</script>
					  	</td>
					 </tr>
					<tr>
					 	<th width="180"><span class="star">*</span> Project Code w Version</th>
					    <td width=""><input class="text" type="text" id="projCodeWVersion" readonly
					    name="projCodeWVersion" >
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCWVSSBtm">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCWVSSBtm",
                                inputField:"projCodeWVersion",
                                title:"Project Code With Version",                                
                                table:"PIDB_IC_WAFER",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                autoSearch:false,
                                mode:0
							});
						</script>
					    </td>
					</tr>
					<tr>
						<th width="180">Device Owner</th>
					  	<td width=""><input class="text" type="text" id="deviceOwner"  name="deviceOwner"
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"DEVICE_OWNER") %>"></td>
					</tr>
					<tr>
						<th width="180">Corner Lot ID</th>
					  	<td width=""><input class="text" type="text" id="cornerLotId" name="cornerLotId" 
					  	 value="<%=BeanHelper.getHtmlValueByColumn(ref,"CORNER_LOT_ID") %>"></td>
					</tr>
					<tr>
						<th width="180">Corner Lot Analysis Status</th>
					  	<td width=""><select id="cornerLotAnalyStatus" name="cornerLotAnalyStatus" class="select_w130">
								<option value="">--Select--</option>
								<option value="Going" <%="Going".equals(BeanHelper.getHtmlValueByColumn(ref, "CORNER_LOT_ANALY_STATUS"))?"selected":"" %>>Going</option>
								<option value="Finish" <%="Finish".equals(BeanHelper.getHtmlValueByColumn(ref, "CORNER_LOT_ANALY_STATUS"))?"selected":"" %>>Finish</option>
					  		</select></td>
					 </tr>
					<tr>
						<th width="180">Corner Lot Analysis Finish Date</th>						
					  	<td width="">
						<input class="text" type="text" id="cornerLotAnalyFinishDate" name="CORNER_LOT_ANALY_STATUS" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"CORNER_LOT_ANALY_FINISH_DATE","yyyy/MM/dd") %>">
						<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="dateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"cornerLotAnalyFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"dateBtn"
							});
						</script></td>
					</tr>
					<tr>
					  	<th width="180">Corner Lot Yield Analysis Summary</th>
					  	<td width="">
						<input class="text" type="text" id="cornerLotYieldAnalySum" name="cornerLotYieldAnalySum"  
						 value="<%=BeanHelper.getHtmlValueByColumn(ref,"CORNER_LOT_YIELD_ANALY_SUM") %>"></td>
					</tr>
					<tr>
						<th width="180">Characterization Lot ID</th>
					  	<td width=""><input class="text" type="text" id="charLotId" name="charLotId" 
					  	 value="<%=BeanHelper.getHtmlValueByColumn(ref,"CHAR_LOT_ID") %>"></td>
					</tr>
					<tr>
						<th width="180">Characterization Lot Analysis Status</th>
					  	<td width=""><select id="charLotAnalyStatus" name="charLotAnalyStatus" class="select_w130">
										<option value="">--Select--</option>
										<option value="Going" <%="Going".equals(BeanHelper.getHtmlValueByColumn(ref, "CHAR_LOT_ANALY_STATUS"))?"selected":"" %>>Going</option>
										<option value="Finish" <%="Finish".equals(BeanHelper.getHtmlValueByColumn(ref, "CHAR_LOT_ANALY_STATUS"))?"selected":"" %>>Finish</option>
							</select></td>
					</tr>
					<tr>
						<th width="180">Characterization Lot Analysis Finish Date</th>
					  	<td width="">
						<input class="text" type="text" id="charLotAnalyFinishDate" name="charLotAnalyFinishDate"  readonly
						 value="<%=BeanHelper.getHtmlValueByColumn(ref,"CHAR_LOT_ANALY_FINISH_DATE","yyyy/MM/dd") %>">
						<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="charDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"charLotAnalyFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"charDateBtn"
							});
						</script>
						</td>
					</tr>
					<tr>
						<th width="180">Characterization Analysis Summary</th>
					  	<td width=""><input class="text" type="text" id="charAnalySummary" name="charAnalySummary" 
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"CHAR_ANALY_SUMMARY") %>"></td>
					</tr>
					<tr>
						<th width="180">Remark</th>
					  	<td width=""><input class="text" type="text" id="remark" name="remark"
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"REMARK") %>"></td>
					</tr>
					<tr>
						<th width="180" ><div>AssignTo</div></th>
					  	<td width="" >
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
