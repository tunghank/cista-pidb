<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.MaskLayerMappingTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="java.util.List" %>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>


<%
	MaskLayerMappingTo ref = (MaskLayerMappingTo) request.getAttribute("ref");

%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>

<script type="text/javascript">
var oldMaskNum = '<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_NUM")%>';

function doSave() {
	if ($F('maskNum') == "") {
		setMessage("error", "Mask No. is must field.");
		return;
	}
	if ($F('maskLayer') == "") {
		setMessage("error", "Mask Layer is must field.");
		return;
	}
	
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking Mask Layer Mapping exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_maskLayerMapping_exist.do',
		{
			method: 'get',
			parameters: 'maskNum='+ $F('maskNum'),
			onComplete: checkMaskLayerMappingExistComplete
		}
	);
}

function checkMaskLayerMappingExistComplete(r) {
	var result = r.responseText;
	if(result=="true" && $F('maskNum')!= oldMaskNum ) {
		setMessage("error", "Mask Layer Mapping is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		setMessage("error", "Saving Mask Layer Mapping...");
		$('updateForm').submit();
	}
}


</script>

<form name="updateForm" action="<%=cp %>/md/mask_layer_map_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Modify Mask Layer Mapping</td>
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
					<div align="right">&nbsp;</div>
					</td>
				</tr>
			</table>				

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div><span class="star">*</span> Mask No.</div></th>
					 	<td width="30%"><input class="text_protected" type="text" readonly id="maskNum" name="maskNum" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_NUM") %>" width="150px"></td>
						<th width="20%"><div><span class="star">*</span> Mask Layer</div></th>
					 	<td width="30%"><input class="text_required" type="text" id="maskLayer" name="maskLayer" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_LAYER") %>" width="150px"></td>
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
					
					        <input
						name="saveBtn" type="button" class="button" id="saveBtn"
						value="Save" onClick="doSave()"> 
						   <%
						   }     
						%>
						
						<input
						name="resetBtn" type="reset" class="button" id="resetBtn"
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

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
