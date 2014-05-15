<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function createRole() {
	if ($F('roleName') == "") {
		setMessage("error", "Role name is must field.");
		return;
	}
	$('createBtn').disabled = true;
	$('cancelBtn').disabled = true;
	setMessage("error", "Checking role exist...");
	var checkUser = new Ajax.Request(
		'<%=cp%>/ajax/check_role_exist.do',
		{
			method: 'get',
			parameters: 'roleName='+ $F('roleName'),
			onComplete: checkRoleExistComplete
		}
	);
}

	function checkRoleExistComplete(r) {
		var result = r.responseText;
		if(result=="true") {
			setMessage("error", "Role is already exist.");
			$('createBtn').disabled = false;
			$('cancelBtn').disabled = false;
		} else {
			setMessage("error", "Save role...");
			$('roleCreate').submit();
		}
	}
</script>
</head>
<body>
<form name="roleCreate" action="<%=cp %>/admin/role_create.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: Role management </td>
  </tr>
</table>
			<div class="content">
			<div class="segmentHeader">Role Create </div>
			<table class="formErrorAndButton">
				<tr>
					<td width="75%">
					<div class="formErrorMsg" id="error"><html:errors/></div>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="createBtn" type="button" class="button" id="createBtn"
						value="Create" onclick="createRole()">
					  <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="document.location.href='<%=cp %>/admin/role_list.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>


			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180">Role Name </th>
					  <td>
							<input name="roleName" id="roleName" type="text" size="30" class="text">
					  </td>
					</tr>
					<tr>
					  <th width="180" style="vertical-align: top;">Access Control List</th>
					  <td>
							<%=request.getAttribute("functionTable")%>
					  </td>
					</tr>
				</tbody>
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
