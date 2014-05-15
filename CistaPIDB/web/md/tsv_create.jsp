<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.TsvTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="java.util.List"%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">

<script type="text/javascript">
function doSave() {
		if ($F('projName') == "") {
			setMessage("error", "Project Name is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('pkgName') == "") {
			setMessage("error", "Package Name is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('pkgCode') == "") {
			setMessage("error", "Package Code is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('tsvType') == "") {
			setMessage("error", "TSV Package Type is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('pinCount') == "") {
			setMessage("error", "Pin Count is must field.");
			$('isRelease').value='0';	
			return;
		}else{
			if (!IsNumber($F('pinCount'))) {
				setMessage("error",	 "Pin Count must be number.");
				$('isRelease').value='0';
				return;
			}
		}

		if ($F('greenPkg') == "") {
			setMessage("error", "Green Package is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('mcpDieQty') == "") {
			setMessage("error", "MCP Die Quantity is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('mcpPkg') == "") {
			setMessage("error", "MCP Package is must field.");
			$('isRelease').value='0';	
			return;
		}

		if (!IsNumber($F('goldenWireWidth'))) {
			setMessage("error",	 "Golden Wire Width must be number.");
			$('isRelease').value='0';
			return;
		}
		
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_tsv_exist.do',
			{
				method: 'get',
				parameters: 'pkgName='+ $F('pkgName'),
				onComplete: checkTsvExistComplete
			}
		);
	}	
	
 function checkTsvExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "The TSV package is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';	
	} else {
		
			setMessage("error", "Saving TSV package...");		
			selectAllOptions($('assignTo'));
									
			$('createForm').action = $('createForm').action + "?m=save";
			$('createForm').submit();
	}
}
function onlyNumPinCount(){
	if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57)) 
	|| (window.event.keyCode == 13) || (window.event.keyCode == 46) 
	|| (window.event.keyCode == 45))){
		window.event.keyCode = 0 ;
		setMessage("error", "Pin Count must be number");
	}
}



function onlyNumWidth(){
	if ( !(((window.event.keyCode >= 48) && (window.event.keyCode <= 57)) 
	|| (window.event.keyCode == 13) || (window.event.keyCode == 46) 
	|| (window.event.keyCode == 45))){
		window.event.keyCode = 0 ;
		setMessage("error", "Golden Wire Width be number");
	}
}

function doRelease() {
	$('isRelease').value='1';
	doSave();
}

</script>
</head>
<body>
<%
	TsvTo ref = (TsvTo) request.getAttribute("ref");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
		<td class="pageTitle">Master Data :: Create TSV Package</td>
  </tr>
</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/tsv_create.do" method="post">
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
						<th width="20%"><div class="erp_label"><span class="star">*</span> Project Name</div></th>
					    <td width="30%">
						<input class="text" type="text" name="projName" id="projName" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_NAME") %>">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projNameSSBtn">
						 <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projNameSSBtn",
                                inputField:"projName",                                
                                name:"ProjectName",
                                autoSearch:false,
                                mode:0
							});
							</script>&nbsp;</td>
					    <th width="20%">Assembly House 2</th>
						<td width="30%">
						<select name="assyHouse2" id="assyHouse2" class="select_w130">
                          <option value="">--Select--</option>
                          <option value="OSE" <%="OSE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE2"))?"selected":"" %>>OSE</option>
                          <option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE2"))?"selected":"" %>>SPIL</option>
                          <option value="超豐" <%="超豐".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE2"))?"selected":"" %>>超豐</option>
                          <option value="PGC" <%="PGC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE2"))?"selected":"" %>>PGC</option>
                          <option value="典範" <%="典範".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE2"))?"selected":"" %>>典範</option>
						  <option value="菱生" <%="菱生".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE2"))?"selected":"" %>>菱生</option>
					  	</select></td>
					</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span> Package Name</div></th>
					  <td><input class="text" type="text" name="pkgName" id="pkgName" ></td>
					  <th>Assembly House 2 Spec No</th>
					  <td><input class="text" type="text" name="assyHouse2SpecNum" id="assyHouse2SpecNum" 
					 value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSY_HOUSE2_SPEC_NUM") %>" ></td>
					</tr>
					<tr>
						<th><div class="erp_label"><span class="star">*</span>Package Code</div></th>
					  <td>
					  <select class="select_w130" name="pkgCode" id="pkgCode">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> tsvVersionList = (List<FunctionParameterTo>) request.getAttribute("tsvVersionList");
									for (FunctionParameterTo version : tsvVersionList) {
										String v = version.getFieldValue();
										%>
								<option value="<%=v %>"><%=version.getFieldShowName() %></option>
										<%
									}
								%>
					 </select>
					  
					  </td>
                      <th>Assembly House 3</th>
					  <td><select name="assyHouse3" id="assyHouse3" class="select_w130">
                       	<option value="">--Select--</option>
                          <option value="OSE" <%="OSE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE3"))?"selected":"" %>>OSE</option>
                          <option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE3"))?"selected":"" %>>SPIL</option>
                          <option value="超豐" <%="超豐".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE3"))?"selected":"" %>>超豐</option>
                          <option value="PGC" <%="PGC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE3"))?"selected":"" %>>PGC</option>
                          <option value="典範" <%="典範".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE3"))?"selected":"" %>>典範</option>
						  <option value="菱生" <%="菱生".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE3"))?"selected":"" %>>菱生</option>
                      	</select></td>
					</tr>
					<tr>
						<th><div class="erp_label"><span class="star">*</span> TSV Package Type</div></th>
						<td><input class="text" type="text" name="tsvType" id="tsvType"
					  value="<%=BeanHelper.getHtmlValueByColumn(ref,"TSV_TYPE") %>" >
						</td>
						<th>Assembly House 3 Spec No</th>
					  <td><input class="text" type="text" name="assyHouse3SpecNum" id="assyHouse3SpecNum"  
					   value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSY_HOUSE3_SPEC_NUM") %>" ></td>
					</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span> Pin Count</div></th>
					  <td><input class="text" type="text" name="pinCount" id="pinCount"  onKeypress="onlyNumPinCount()"
					  value="<%=BeanHelper.getHtmlValueByColumn(ref,"PIN_COUNT") %>" ></td>
					  <th>Assembly House 4</th>
						<td><select name="assyHouse4" id="assyHouse4" class="select_w130">
                          <option value="" >--Select--</option>
                          <option value="OSE" <%="OSE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE4"))?"selected":"" %>>OSE</option>
                          <option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE4"))?"selected":"" %>>SPIL</option>
                          <option value="超豐" <%="超豐".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE4"))?"selected":"" %>>超豐</option>
                          <option value="PGC" <%="PGC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE4"))?"selected":"" %>>PGC</option>
                          <option value="典範" <%="典範".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE4"))?"selected":"" %>>典範</option>
						  <option value="菱生" <%="菱生".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE4"))?"selected":"" %>>菱生</option>
                        </select></td>
					</tr>
					<tr>
					  <th>Lead Frame Type</th>
					  <td><select name="leadFrameType" id="leadFrameType" class="select_w130">
                           <option value="">--Select--</option>
                           <option value="N/A" <%="N/A".equals(BeanHelper.getHtmlValueByColumn(ref, "LEAD_FRAME_TYPE"))?"selected":"" %>>N/A</option>
                           <option value="Etiching" <%="Etiching".equals(BeanHelper.getHtmlValueByColumn(ref, "LEAD_FRAME_TYPE"))?"selected":"" %>>Etiching</option>
                           <option value="Stamping" <%="Stamping".equals(BeanHelper.getHtmlValueByColumn(ref, "LEAD_FRAME_TYPE"))?"selected":"" %>>Stamping</option>
						 
                        </select>
                      </td>
					  <th>Assembly House 4 Spec No</th>
						<td><input class="text" type="text" name="assyHouse4SpecNum" id="assyHouse4SpecNum" 
						 value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSY_HOUSE4_SPEC_NUM") %>" ></td>
					</tr>
					<tr>
					  <th>Heat Sink Type</th>
					  <td>
                        <select name="edhsLayer" id="edhsLayer" class="select_w130">
                           <option value="">--Select--</option>
                           <option value="None" <%="None".equals(BeanHelper.getHtmlValueByColumn(ref, "EDHS_LAYER"))?"selected":"" %>>None</option>
                           <option value="E-Pad" <%="E-Pad".equals(BeanHelper.getHtmlValueByColumn(ref, "EDHS_LAYER"))?"selected":"" %>>E-Pad</option>
                           <option value="EDHS" <%="EDHS".equals(BeanHelper.getHtmlValueByColumn(ref, "EDHS_LAYER"))?"selected":"" %>>EDHS</option>
                           <option value="Others" <%="Others".equals(BeanHelper.getHtmlValueByColumn(ref, "EDHS_LAYER"))?"selected":"" %>>Others</option> 
                        </select>
                     </td>
					  <th>Assembly House 5</th>
					  <td><select name="assyHouse5" id="assyHouse5" class="select_w130">
                         <option value="">--Select--</option>
                          <option value="OSE" <%="OSE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE5"))?"selected":"" %>>OSE</option>
                          <option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE5"))?"selected":"" %>>SPIL</option>
                          <option value="超豐" <%="超豐".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE5"))?"selected":"" %>>超豐</option>
                          <option value="PGC" <%="PGC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE5"))?"selected":"" %>>PGC</option>
                          <option value="典範" <%="典範".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE5"))?"selected":"" %>>典範</option>
						  <option value="菱生" <%="菱生".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE5"))?"selected":"" %>>菱生</option>
                      </select></td>
					</tr>
					<tr>
					  <th><div class="erp_label"><span class="star">*</span> Green Package</div></th>
					  <td>
                        <select name="greenPkg" id="greenPkg" class="select_w130">
                          <option value="Y">Yes</option>                          
                          <option value="N" <%="N".equals(BeanHelper.getHtmlValueByColumn(ref, "GREEN_PKG"))?"selected":"" %>>No</option>
                          <option value="H" <%="H".equals(BeanHelper.getHtmlValueByColumn(ref, "GREEN_PKG"))?"selected":"" %>>Halgon Free</option>
                        </select></td>
					  <th>Assembly House 5 Spec No</th>
					  <td><input class="text" type="text" name="assyHouse5SpecNum" id="assyHouse5SpecNum"  
					   value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSY_HOUSE5_SPEC_NUM") %>" ></td>
					</tr>
					
					<tr>
					  <th>Golden Wire Width</th>
					  <td>
                        <input class="text" type="text" name="goldenWireWidth" id="goldenWireWidth" onKeypress="onlyNumWidth()"
                        value="<%=BeanHelper.getHtmlValueByColumn(ref,"GOLDEN_WIRE_WIDTH") %>" >
                     </td>
					  <th>Remark</th>
						<td><input class="text" type="text" name="remark" id="remark"
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"REMARK") %>" ></td>
					</tr>
					
					<tr>
					  <th>Body Size</th>
					  <td>
                        <input class="text" type="text" name="bodySize" id="bodySize"
                        value="<%=BeanHelper.getHtmlValueByColumn(ref,"BODY_SIZE") %>" >
                      </td>
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
					</tr>
					
					<tr>
					  <th><div class="erp_label"><span class="star">*</span> MCP Die Quantity</div></th>
					  <td>
                        <select name="mcpDieQty" id="mcpDieQty" class="select_w130">
                          <option value="N/A" <%="N/A".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY"))?"selected":"" %>>N/A</option>
                          <option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY"))?"selected":"" %>>1</option>
                          <option value="2" <%="2".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY"))?"selected":"" %>>2</option>
                          <option value="3" <%="3".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY"))?"selected":"" %>>3</option>
                          <option value="4" <%="4".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY"))?"selected":"" %>>4</option>
                          <option value="5" <%="5".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY"))?"selected":"" %>>5</option>
                        </select>
                      </td>
					  </tr>
					  <tr>
					  <th><div class="erp_label"><span class="star">*</span> MCP Package</div></th>
					  <td>
                        <select name="mcpPkg" id="mcpPkg" class="select_w130">
                           <option value="">--Select--</option>
                           <option value="Y" <%="Y".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_PKG"))?"selected":"" %>>Yes</option>
                           <option value="N" <%="N".equals(BeanHelper.getHtmlValueByColumn(ref, "MCP_PKG"))?"selected":"" %> selected>No</option>
                        </select>
                     </td>	
                     	<th>&nbsp;</th>
                     	<td>&nbsp;</td>
                     </tr>
					 <tr>
					  <th>MCP Type</th>
					  <td>
                        <input class="text" type="text" name="mcpType" id="mcpType"
                        value="<%=BeanHelper.getHtmlValueByColumn(ref,"MCP_TYPE") %>" >
                      </td>	
                     	<th>&nbsp;</th>
                     	<td>&nbsp;</td>
                     </tr>
                     <tr>				
					  <th>Substrate Layer</th>
					  <td>
                        <select name="subtractLayer" id="subtractLayer" class="select_w130">
                          <option value="N/A" <%="N/A".equals(BeanHelper.getHtmlValueByColumn(ref, "SUBTRACT_LAYER"))?"selected":"" %>>N/A</option>
                          <option value="2" <%="2".equals(BeanHelper.getHtmlValueByColumn(ref, "SUBTRACT_LAYER"))?"selected":"" %>>2</option>
                          <option value="4" <%="4".equals(BeanHelper.getHtmlValueByColumn(ref, "SUBTRACT_LAYER"))?"selected":"" %>>4</option>
                          <option value="6" <%="6".equals(BeanHelper.getHtmlValueByColumn(ref, "SUBTRACT_LAYER"))?"selected":"" %>>6</option>
                          <option value="8" <%="8".equals(BeanHelper.getHtmlValueByColumn(ref, "SUBTRACT_LAYER"))?"selected":"" %>>8</option>
                        </select>
                     </td>
                     <th>&nbsp;</th>
                     	<td>&nbsp;</td>
                     </tr>
					 <tr>
					  <th>Passive Component</th>
					  <td>
                        <input class="text" type="text" name="passiveComponent" id="passiveComponent"
                        value="<%=BeanHelper.getHtmlValueByColumn(ref,"PASSIVE_COMPONENT") %>" >
                      </td>	
                     	<th>&nbsp;</th>
                     	<td>&nbsp;</td>
                     </tr>
					<tr>
					 <th>Assembly House 1</th>
					  <td>
                        <select name="assyHouse1" id="assyHouse1" class="select_w130">
                          <option value="">--Select--</option>
                          <option value="OSE" <%="OSE".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE1"))?"selected":"" %>>OSE</option>
                          <option value="SPIL" <%="SPIL".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE1"))?"selected":"" %>>SPIL</option>
                          <option value="超豐" <%="超豐".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE1"))?"selected":"" %>>超豐</option>
                          <option value="PGC" <%="PGC".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE1"))?"selected":"" %>>PGC</option>
                          <option value="典範" <%="典範".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE1"))?"selected":"" %>>典範</option>
						  <option value="菱生" <%="菱生".equals(BeanHelper.getHtmlValueByColumn(ref, "ASSY_HOUSE1"))?"selected":"" %>>菱生</option>
                        </select>
                     </td>
                     <th>&nbsp;</th>
                     	<td>&nbsp;</td>
                   </tr>
                   <tr>
					  <th>Assembly House 1 Spec No</th>
					  <td><input class="text" name="assyHouse1SpecNum" id="assyHouse1SpecNum"
					  value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSY_HOUSE1_SPEC_NUM") %>" ></td>
					  <th>&nbsp;</th>
                     	<td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right">
					<input type="hidden" id="isRelease" name="isRelease" value="0">
					
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
					<!-- <input name="releaseBtn" type="button" class="erp_button" id="releaseBtn" value="Release To ERP"
						onclick="doRelease()"> 	 -->
					&nbsp;
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

