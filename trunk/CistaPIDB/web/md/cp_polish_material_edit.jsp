<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.CpPolishMaterialTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	CpPolishMaterialTo ref = (CpPolishMaterialTo) request.getAttribute("ref");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">

function doSave() {
   
    if ($F('cpPolishMaterialNum') == "") {	
		setMessage("error", "CP Polish Material Num. is must field.");
		return;
	}
	if ($F('projCodeWVersion') == "") {	
		setMessage("error", "Project Code With Version. is must field.");
		return;
	}
	if ($F('cpPolishVariant') == "") {	
		setMessage("error", "CP Polish VARIANT. is must field.");
		return;
	}
	if ($F('description') == "") {	
		setMessage("error", "DESCRIPTION. is must field.");
		return;
	}		

	var submitForm = true;
	if (submitForm ) {
		setMessage("error", "Saving CP Polish Maerial Number...");
		
		$('polishEdit').action = $('polishEdit').action + "?m=save";
		$('polishEdit').submit();	
	}
}

</script>
</head>
<body>
<form name="polishEdit" action="<%=cp %>/md/cp_polish_material_edit.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: Modify CP Polish Material Number</td>
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
			<th width="180"><div class="erp_label"><span class="star">*</span>CP Polish Material Num.</div></th>
			<td><input name="cpPolishMaterialNum" readonly type="text"
							class="text_protected" id="cpPolishMaterialNum" readonly
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_POLISH_MATERIAL_NUM") %>">					
	 </tr>
      <tr>
			<th width="180"><div class="erp_label"><span class="star">*</span>Project Code W Version</div></th>
			<td><input name="projCodeWVersion" readonly type="text" readonly
							class="text_protected" id="projCodeWVersion"
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJECT_CODE_W_VERSION") %>">					
	 </tr>
					<tr>
						<th width="180"><span class="star">*</span>CP Polish VARIANT</th>
						<td><input name="cpPolishVariant" type="text" class="text"  readonly
							id="cpPolishVariant" 
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP_POLISH_VARIANT") %>"></td>
					</tr>
					<tr>
						<th width="180"><div class="erp_label"><span class="star">*</span>DESCRIPTION</div></th>
						<td><input name="description" type="text" class="text" 
							id="description" 
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "DESCRIPTION") %>"></td>
					</tr>
					<tr>
						<th width="180" style="vertical-align:top">MP Status</th>
						<td><input name="mpStatus" type="text" class="text_protected" readonly
							id="mpStatus"  readonly
							value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS") %>">
						</td>
					</tr>
			   </tbody>
			</table>
			</div>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td>
					<div align="right">
					<input type="hidden" id="toErp" name="toErp">
					
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
						   
					<!--<input name="releaseBtn" type="button" class="erp_button" id="releaseBtn" value="Release To ERP" onClick="doReleaseToERP('erp')">-->
					<input name="saveBtn" type="button" class="button" id="saveBtn" value="Save" onClick="doSave()">
					
						   <%
						   }     
						%>
					
					<input name="resetBtn" type="reset" class="button" id="resetBtn" value="Reset"></div>
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
						<td>
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
