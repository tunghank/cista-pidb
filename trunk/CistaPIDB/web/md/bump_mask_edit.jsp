<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.BumpMaskTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	BumpMaskTo ref = (BumpMaskTo) request.getAttribute("ref");
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
	
	if ($F('projCode') == "") {
		setMessage("error", "Project Code is must field.");
		return;
	}
		var submitForm = true;
	if (submitForm ) {
		setMessage("error", "Saving bumping mask...");
		selectAllOptions($('assignTo'));
		
		$('bumpMaskEdit').action = $('bumpMaskEdit').action + "?m=save&toErp=" + $F('toErp');
		$('bumpMaskEdit').submit();	
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
<form name="bumpMaskEdit" action="<%=cp %>/md/bump_mask_edit.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
		          <td class="pageTitle">Master Data :: Modify Bumping Mask</td>
             </tr>
           </table>
	
			<div class="content">
			  <table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />
					<%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>&nbsp;</div>
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
						<th width="20%"><div class="erp_label"><span class="star">*</span>Project Code</div></th>
					  <td width="30%"><input type="text" name="projCode" id="projCode" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE") %>">							</td>
					  <th width="20%">Remark </th>
					  <td width="30%"><input class="text" type="text" name="remark" id="remark" value="<%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %>">
  &nbsp;</td>
					</tr>
					<tr>
						<th><span class="star">*</span>Mask Name</th>
					  <td>
					  	<input type="text" name="maskName" id="maskName" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_NAME") %>">
&nbsp;					  </td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th width="20%"><div class="erp_label">RDL</div></th>
					  <td width="30%"><select id="rdl" name="rdl" class="select_w130">
                          <option value="N/A">N/A</option>
                          <option value="1L" <%="1L".equals(BeanHelper.getHtmlValueByColumn(ref, "RDL"))?"selected":"" %>>1L</option>
                          <option value="2L" <%="2L".equals(BeanHelper.getHtmlValueByColumn(ref, "RDL"))?"selected":"" %>>2L</option>
                          <option value="3L" <%="3L".equals(BeanHelper.getHtmlValueByColumn(ref, "RDL"))?"selected":"" %>>3L</option>
                        </select>                      </td>
					<th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
				  </tr>
					 
					 <tr>
					   <th>PI</th>
					   <td><select id="pi" name="pi" class="select_w130">
                           <option value="N/A">N/A</option>
                           <option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(ref, "PI"))?"selected":"" %>>1</option>
                           <option value="2" <%="2".equals(BeanHelper.getHtmlValueByColumn(ref, "PI"))?"selected":"" %>>2</option>
                         </select>
  &nbsp; </td>
					<th >&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					
					<tr>
					  <th>Bump Height </th>
					  <td><input type="text" class="text" name="bumpHeight" id="bumpHeight" value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HEIGHT") %>">                      </td>
					<th >&nbsp;</th>
					  <td>&nbsp;</td>
				  </tr>
					  <tr>
					    <th >Bump Hardness</th>
					    <td><input type="text" class="text" name="bumpHardness" id="bumpHardness" value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HARDNESS") %>">                        </td>
					<th >&nbsp;</th>
					  <td>&nbsp;</td>
					 </tr>
					 
					 <tr>
					   <th >Bumping House 1</th>
					   <td><select id="bumpHouse1" name="bumpHouse1" class="select_w130">
                           <option value="">--Select--</option>
                           <option value="IST(Hsin Chu)" <%="IST(Hsin Chu)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>IST(Hsin Chu)</option>
                           <option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>IST</option>
                           <option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>ChipBond</option>
                           <option value="Fupo" <%="Fupo".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>Fupo</option>
                           <option value="Chipmos" <%="Chipmos".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>Chipmos</option>
                           <option value="Spil" <%="Spil".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>Spil</option>
                           <option value="Chipmore" <%="Chipmore".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>Chipmore</option>
                           <option value="Chipmos(SH)" <%="Chipmos(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>Chipmos(SH)</option>
                           <option value="ChipPAC" <%="ChipPAC".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1"))?"selected":"" %>>ChipPAC</option>
                         </select>
					     &nbsp; </td>
					<th >&nbsp;</th>
					  <td>&nbsp;</td>
					 </tr>
					 <tr>
					   <th nowrap>Bumping House 1: Mask Ready Date (Plan)</th>
					   <td><input class="text" type="text"
							name="bumpHouse1Mrdp" id="bumpHouse1Mrdp" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE1_MRDP", "yyyy/MM/dd") %>">
                           <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="bumpHouse1MrdpBrn">
                           <script type="text/javascript">
							Calendar.setup({
								inputField:"bumpHouse1Mrdp",
								ifFormat:"%Y/%m/%d",
								button:"bumpHouse1MrdpBrn"
							});
						 </script>                       </td>
					   <th >&nbsp;</th>
					  <td>&nbsp;</td>
					 </tr>
					 <tr>
					   <th nowrap>Bumping House 2</th>
					   <td><select id="bumpHouse2" name="bumpHouse2" class="select_w130">
                           <option value="">--Select--</option>
                           <option value="IST(Hsin Chu)" <%="IST(Hsin Chu)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>IST(Hsin Chu)</option>
                           <option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>IST</option>
                           <option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>ChipBond</option>
                           <option value="Fupo" <%="Fupo".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>Fupo</option>
                           <option value="Chipmos" <%="Chipmos".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>Chipmos</option>
                           <option value="Spil" <%="Spil".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>Spil</option>
                           <option value="Chipmore" <%="Chipmore".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>Chipmore</option>
                           <option value="Chipmos(SH)" <%="Chipmos(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>Chipmos(SH)</option>
                           <option value="ChipPAC" <%="ChipPAC".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2"))?"selected":"" %>>ChipPAC</option>
                         </select>
					     &nbsp; </td>
					 <th>&nbsp;</th>
					 <td>&nbsp;</td>
					 </tr>
					 <tr>
					   <th nowrap>Bumping House 2: Mask Ready Date (Plan)</th>
					   <td><input class="text" type="text"
							name="bumpHouse2Mrdp" id="bumpHouse2Mrdp" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE2_MRDP", "yyyy/MM/dd") %>">
                           <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="bumpHouse2MrdpBrn">
                           <script type="text/javascript">
							Calendar.setup({
								inputField:"bumpHouse2Mrdp",
								ifFormat:"%Y/%m/%d",
								button:"bumpHouse2MrdpBrn"
							});
						 </script>                       </td>
					 <th>&nbsp;</th>
						<td>&nbsp;</td>
					 </tr>
					 <tr>
					   <th nowrap>Bumping House 3</th>
					   <td><select id="bumpHouse3" name="bumpHouse3" class="select_w130">
                           <option value="">--Select--</option>
                           <option value="IST(Hsin Chu)" <%="IST(Hsin Chu)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>IST(Hsin Chu)</option>
                           <option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>IST</option>
                           <option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>ChipBond</option>
                           <option value="Fupo" <%="Fupo".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>Fupo</option>
                           <option value="Chipmos" <%="Chipmos".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>Chipmos</option>
                           <option value="Spil" <%="Spil".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>Spil</option>
                           <option value="Chipmore" <%="Chipmore".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>Chipmore</option>
                           <option value="Chipmos(SH)" <%="Chipmos(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>Chipmos(SH)</option>
                           <option value="ChipPAC" <%="ChipPAC".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3"))?"selected":"" %>>ChipPAC</option>
                         </select>
					     &nbsp; </td>
					   <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th nowrap>Bumping House 3: Mask Ready Date (Plan)</th>
					  <td><input class="text" type="text"
							name="bumpHouse3Mrdp" id="bumpHouse3Mrdp" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE3_MRDP", "yyyy/MM/dd") %>">
                          <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="bumpHouse3MrdpBrn">
                          <script type="text/javascript">
							Calendar.setup({
								inputField:"bumpHouse3Mrdp",
								ifFormat:"%Y/%m/%d",
								button:"bumpHouse3MrdpBrn"
							});
						</script>                      </td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th nowrap>Bumping House 4</th>
					  <td><select id="bumpHouse4" name="bumpHouse4" class="select_w130">
                          <option value="">--Select--</option>
                          <option value="IST(Hsin Chu)" <%="IST(Hsin Chu)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>IST(Hsin Chu)</option>
                          <option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>IST</option>
                          <option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>ChipBond</option>
                          <option value="Fupo" <%="Fupo".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>Fupo</option>
                          <option value="Chipmos" <%="Chipmos".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>Chipmos</option>
                          <option value="Spil" <%="Spil".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>Spil</option>
                          <option value="Chipmore" <%="Chipmore".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>Chipmore</option>
                          <option value="Chipmos(SH)" <%="Chipmos(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>Chipmos(SH)</option>
                          <option value="ChipPAC" <%="ChipPAC".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4"))?"selected":"" %>>ChipPAC</option>
                        </select>
					    &nbsp; </td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th nowrap>Bumping House 4: Mask Ready Date (Plan)</th>
					  <td><input class="text" type="text"
							name="bumpHouse4Mrdp" id="bumpHouse4Mrdp" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE4_MRDP", "yyyy/MM/dd") %>">
                          <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="bumpHouse4MrdpBrn">
                          <script type="text/javascript">
							Calendar.setup({
								inputField:"bumpHouse4Mrdp",
								ifFormat:"%Y/%m/%d",
								button:"bumpHouse4MrdpBrn"
							});
						</script>                      </td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th nowrap>Bumping House 5</th>
					  <td><select id="bumpHouse5" name="bumpHouse5" class="select_w130">
                          <option value="">--Select--</option>
                          <option value="IST(Hsin Chu)" <%="IST(Hsin Chu)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>IST(Hsin Chu)</option>
                          <option value="IST" <%="IST".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>IST</option>
                          <option value="ChipBond" <%="ChipBond".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>ChipBond</option>
                          <option value="Fupo" <%="Fupo".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>Fupo</option>
                          <option value="Chipmos" <%="Chipmos".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>Chipmos</option>
                          <option value="Spil" <%="Spil".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>Spil</option>
                          <option value="Chipmore" <%="Chipmore".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>Chipmore</option>
                          <option value="Chipmos(SH)" <%="Chipmos(SH)".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>Chipmos(SH)</option>
                          <option value="ChipPAC" <%="ChipPAC".equals(BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5"))?"selected":"" %>>ChipPAC</option>
                        </select>
  &nbsp; </td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th nowrap>Bumping House 5: Mask Ready Date (Plan)</th>
					  <td><input class="text" type="text"
							name="bumpHouse5Mrdp" id="bumpHouse5Mrdp" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "BUMP_HOUSE5_MRDP", "yyyy/MM/dd") %>">
                          <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="bumpHouse5MrdpBrn">
                          <script type="text/javascript">
							Calendar.setup({
								inputField:"bumpHouse5Mrdp",
								ifFormat:"%Y/%m/%d",
								button:"bumpHouse5MrdpBrn"
							});
						</script>                      </td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
					<tr>
					  <th>AssignTo </th>
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
                          <td><input name="button4" type="button" class="button" id="userBtn" value="User" >
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
							</script>                          </td>
							<td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
                        </tr>
                        <tr>
                          <td><input name="button4" type="button" class="button" id="roleBtn" value="Role" >
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
							</script>                          </td>
                        </tr>
                      </table></td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
			</div>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
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

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
