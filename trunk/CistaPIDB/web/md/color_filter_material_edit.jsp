<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.ColorFilterMaterialTo"%>
<%@ page import="com.cista.pidb.code.dao.FunctionParameterDao"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<%

	List<FunctionParameterTo> mlTypeList = (List<FunctionParameterTo>) request.getAttribute("mlTypeList");
	mlTypeList =null!=mlTypeList?mlTypeList:new ArrayList();

	List<FunctionParameterTo> newMaskList = (List<FunctionParameterTo>) request.getAttribute("newMaskList");
	newMaskList =null!=newMaskList?newMaskList:new ArrayList();

	List<FunctionParameterTo> rbgThicknessList = (List<FunctionParameterTo>) request.getAttribute("rbgThicknessList");
	rbgThicknessList =null!=rbgThicknessList?rbgThicknessList:new ArrayList();

	List<FunctionParameterTo> revisionItemList = (List<FunctionParameterTo>) request.getAttribute("revisionItemList");
	revisionItemList =null!=revisionItemList?revisionItemList:new ArrayList();

	List<FunctionParameterTo> tapeOutTypeList = (List<FunctionParameterTo>) request.getAttribute("tapeOutTypeList");
	tapeOutTypeList =null!=tapeOutTypeList?tapeOutTypeList:new ArrayList();


%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">

<script type="text/javascript">
function doSave() {
		if ($F('projectCodeWVersion') == "") {
			setMessage("error", "Project Code w Version is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('mpStatus') == "") {
			setMessage("error", "MP Status is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('sLayer') == "") {
			setMessage("error", "S Layer is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('lightPipe') == "") {
			setMessage("error", "Light Pipe is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('mlType') == "") {
			setMessage("error", "ML Type is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('rbgThickness') == "") {
			setMessage("error", "RBG Thickness is must field.");
			$('isRelease').value='0';	
			return;
		}
		if ($F('mlThickness') == "") {
			setMessage("error", "ML Thickness is must field.");
			$('isRelease').value='0';	
			return;
		}
		selectAllOptions($('revisionItem'));

		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Saving ...");
		$('createForm').action = $('createForm').action + "?m=save";
		$('createForm').submit();
}	

	

function setMaskLayerCom() {
	setMessage("error", "Fetchs Mask Layer Combination ...");	
	new Ajax.Request(
			'<%=cp%>/ajax/auto_fetch_maskLayerCom.do',
			{
				method: 'get',
				parameters: 'projCode='+ $F('projCode'),
				onComplete: fetchMaskLayerComplete
			}
		);
}
function fetchMaskLayerComplete(r) {
	var result = r.responseText;
	
	if (result == "") {
	  	setMessage("error", "Can not fetch Mask Layer Combination.");		  	
	} else {
		setMessage("error","");
		setMessage("fetchMaskLayerCom", result);	
	}
}

function doClearDate() {
	$('tapeOutDate').value='';
}

function selectFunctionParameter() {
	var target = "<%=cp%>/dialog/select_functionParameter.do?m=list&callback=selectFunctionParameterComplete&funName=CF&funFieldName=REVISION_ITEM";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_functionParameter","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectFunctionParameterComplete(selectedProds) {
	//$('approveCust').length=0;
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('revisionItem'), prod[0], prod[0]);
		}
	}
}
</script>
</head>
<body>
<%
	ColorFilterMaterialTo ref = (ColorFilterMaterialTo) request.getAttribute("ref");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
		<td class="pageTitle">Master Data :: Edit Color Filter Material</td>
  </tr>
</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/color_filter_material_edit.do" method="post">
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
					  <th width="20%"><div class="erp_label"><span class="star">*</span>CF Material Num</div></th>
					  <td width="30%"><input class="text_protected" readonly
							name="colorFilterMaterialNum" id="colorFilterMaterialNum" value="<%=BeanHelper.getHtmlValueByColumn(ref, "COLOR_FILTER_MATERIAL_NUM") %>" >
						</td>
						<th width="20%">&nbsp;</th>
					  	<td width="25%">&nbsp;
						</td>
					</tr>
					<tr>
					  <th width="20%"><div class="erp_label"><span class="star">*</span> Project Code w Version</div></th>
					  <td width="30%"><input class="text_protected" readonly
							name="projectCodeWVersion" id="projectCodeWVersion" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJECT_CODE_W_VERSION") %>" >
						</td>
						<th width="20%">Tape Out Date</th>
					  	<td width="25%">
						   <input class="text"  name="tapeOutDate" id="tapeOutDate" readonly	value="<%=BeanHelper.getHtmlValueByColumn(ref,"TAPE_OUT_DATE","yyyy/MM/dd") %>" >
					      <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="tapeOutDateBtn">
							<script type="text/javascript">
								Calendar.setup({
									inputField:"tapeOutDate",
									ifFormat:"%Y/%m/%d",
									button:"tapeOutDateBtn"
								});
							</script>&nbsp;
						   <input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear"	onClick="doClearDate()">
						
						</td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Project Code</div></th>
					  	<td width="25%">					  	
					  	<input type="text" class="text_protected" name="projCode" id="projCode" readonly
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE") %>">
						</td>
						<th width="20%">Tape out type</th>     
						<td width="13%">
					 		<select class="select_w130" name="tapeOutType" id="tapeOutType" >
							  	<option value="">--Select--</option>
								<%
									
									for (FunctionParameterTo tapeOutType : tapeOutTypeList) {
										String v = tapeOutType.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_OUT_TYPE"))) {
											selected = "selected";
										}
										%>
									<option value="<%=v %>" <%=selected %>><%=tapeOutType.getFieldShowName() %></option>
										<%
									}
								%>
							</select>

						</td>   

					</tr>
					<tr>
					  <th width="20%">CF Material Description</th>
					  <td width="30%"><input type="text" class="text" name="description" id="description" value="<%=BeanHelper.getHtmlValueByColumn(ref, "DESCRIPTION") %>"></td>
					  <th width="20%">Fab Device ID</th>
					  <td width="25%">
							<input type="text" class="text"  name="fabDeviceId" id="fabDeviceId" value="<%=BeanHelper.getHtmlValueByColumn(ref,"FAB_DEVICE_ID") %>">
					  </td>

					</tr>
					<tr>
					  <th width="20%"><div class="erp_label"><span class="star">*</span> MP Status<div></th>
					  <td width="30%"><select name="mpStatus" id="mpStatus" class="select_w130">
					      <option value="">--Select--</option>
                          <option value="MP" <%="MP".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>MP</option>
                          <option value="Eng" <%="Eng".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Eng</option>
                      </select>
					  </td>
					  	<th width="20%">Mask House</th>
						 <td width="25%"><input type="text" cols="80%" readonly name="maskHouse" id="maskHouse" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_HOUSE") %>" class='text_200'> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="MaskHouseFlowSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"MaskHouseFlowSSBtn",
                                inputField:"maskHouse",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='MASK_HOUSE' and fun_name='CF'",
								orderBy:"ITEM",
                                title:"MASK_HOUSE",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"MaskHouseCallback"
							});

							function MaskHouseCallback(inputField, columns, value) {
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
						<th width="20%">Mask Layer Combination</th>
					  	<td width="25%">
					  		<textarea id="maskLayerCom" name="maskLayerCom" cols="60" rows="5" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "MASK_LAYER_COM") %></textarea>
							<input type="button" value="Fetch" class="button" id="fetchMask" name="fetchMask" onClick="setMaskLayerCom()">
		         		</td>
						<th width="20%">Fetched Mask Layer Combination </th>
						<td width="25%"><textarea id="fetchMaskLayerCom" name="fetchMaskLayerCom" readonly cols="60" rows="5" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "FETCH_MASK_LAYER_COM") %></textarea></td>	
					</tr>

					<tr>
						<th width="20%">Mask Id</th>
					  	<td width="25%">
					  		<input type="text" class="text"  name="maskId" id="maskId"
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"MASK_ID") %>"></td>
						<th width="20%">New Mask Number</th>
						<td width="25%">
						 <select class="select_w130" name="newMaskNum" id="newMaskNum">
							<option value="">--Select--</option>
								<%
									
									for (FunctionParameterTo maskNum : newMaskList) {
										String v = maskNum.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "NEW_MASK_NUM"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=maskNum.getFieldShowName() %></option>
										<%
									}
								%>
						</select>
						</td>

					</tr>


					<tr>
					  	<th width="20%" style="vertical-align:top"><div class="erp_label"><span class="star">*</span>S Layer</div></th>
						<td width="25%" style="vertical-align:top" ><select name="sLayer" id="sLayer" class="select_w130">
					      <option value="">--Select--</option>
                          <option value="Yes" <%="Yes".equals(BeanHelper.getHtmlValueByColumn(ref, "S_LAYER"))?"selected":"" %>>Yes</option>
                          <option value="No" <%="No".equals(BeanHelper.getHtmlValueByColumn(ref, "S_LAYER"))?"selected":"" %>>No</option>
						 </select>
					    </td>
					  	<th width="20%" style="vertical-align:top"><div class="erp_label"><span class="star">*</span>RBG Thickness</div></th>
						<td width="25%" style="vertical-align:top" ><select name="rbgThickness" id="rbgThickness" class="select_w130">
					      <option value="">--Select--</option>
								<%
									
									for (FunctionParameterTo rbgThickness : rbgThicknessList) {
										String v = rbgThickness.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "RBG_THICKNESS"))) {
											selected = "selected";
										}
										%>
									<option value="<%=v %>" <%=selected %>><%=rbgThickness.getFieldShowName() %></option>
										<%
									}
								%>
						 </select>
					    </td>
					</tr>

					<tr>
					  	<th width="20%" style="vertical-align:top"><div class="erp_label"><span class="star">*</span>Light Pipe</div></th>
						<td width="25%" style="vertical-align:top" ><select name="lightPipe" id="lightPipe" class="select_w130">
					      <option value="">--Select--</option>
                          <option value="Yes" <%="Yes".equals(BeanHelper.getHtmlValueByColumn(ref, "LIGHT_PIPE"))?"selected":"" %>>Yes</option>
                          <option value="No" <%="No".equals(BeanHelper.getHtmlValueByColumn(ref, "LIGHT_PIPE"))?"selected":"" %>>No</option>
						 </select>
					    </td>
					  	<th width="20%" style="vertical-align:top"><div class="erp_label"><span class="star">*</span>ML Thickness</div></th>
						<td width="25%" style="vertical-align:top" ><input type="text" class="text"  name="mlThickness" id="mlThickness" value="<%=BeanHelper.getHtmlValueByColumn(ref,"ML_THICKNESS") %>">
					    </td>
					</tr>
					<tr>
					  	<th width="20%" style="vertical-align:top">Revision Item</th>
						<td width="25%" style="vertical-align:top" ><input name="button" type="button" class="button" onClick="selectFunctionParameter()" value="Add">
                          <input name="button3" type="button" class="button" onClick="removeSelectedOptions($('revisionItem'))" value="Remove">
                          <br>
                          <select name="revisionItem" size="4" multiple class="select_w130" id="revisionItem">
							<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "REVISION_ITEM") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "REVISION_ITEM");
					    			String[] list = listStr.split(",");
					    			
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
											FunctionParameterTo curr = null;
											for (FunctionParameterTo rItem : revisionItemList) {
												if( rItem.getFieldValue().equals(s) ){
													curr = rItem;
													break;
												}
											}
						    			    
						    			    if (curr != null) {
						    			%>
						    			<option value="<%=curr.getFieldValue() %>"><%=curr.getFieldShowName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>
                          </select></td>
						  <th width="12%">Remark</th>
					 	<td width="30%">
					 	<textarea id="remark" name="remark" cols="60" rows="5" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>	
					</tr>
					<tr>
					  	<th width="20%" style="vertical-align:top"><div class="erp_label"><span class="star">*</span>ML Type</div></th>
						<td width="25%" style="vertical-align:top" ><select name="mlType" id="mlType" class="select_w130">
					      <option value="">--Select--</option>
								<%
									
									for (FunctionParameterTo mlType : mlTypeList) {
										String v = mlType.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "ML_TYPE"))) {
											selected = "selected";
										}
										%>
									<option value="<%=v %>" <%=selected %>><%=mlType.getFieldShowName() %></option>
										<%
									}
								%>
						 </select>
					    </td>
					  	<th width="20%">&nbsp;</th>
						<td width="25%">&nbsp;</td>
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

