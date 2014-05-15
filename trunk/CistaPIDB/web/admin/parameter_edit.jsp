<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.admin.to.ParameterTo"%>
<%@ page import="com.cista.pidb.admin.dao.ParameterDao"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	ParameterTo ref = (ParameterTo) request.getAttribute("ref");
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">

	function doSave() {
		if ($F('funName') == "") {
			setMessage("error", "Function Name is must field.");
			return;
		}
		if ($F('funFieldName') == "") {
			setMessage("error", "Function Field Name is must field.");
			return;
		}
		if ($F('fieldValue') == "") {
			setMessage("error", "Function Value is must field.");
			return;
		}
		if ($F('fieldShowName') == "") {
			setMessage("error", "Function Show Name is must field.");
			return;
		}
		
		$('updateBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_parameter_exist.do',
			{
				method: 'get',
				parameters: 'funName='+ $F('funName') + '&funFieldName=' + $F('funFieldName') + '&fieldValue=' + $F('fieldValue') + '&fieldShowName=' + $F('fieldShowName'),
				onComplete: checkParameterExistComplete
			}
		);
	}
	
function checkParameterExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "The Parameter Function Data is already exist.");		
		$('updateBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		setMessage("error", "Saving Parameter Data...");		
										
		$('UpdateForm').action = $('UpdateForm').action;
		$('UpdateForm').submit();		
	}
}
</script>
</head>
<body>
<form name="UpdateForm" action="<%=cp %>/admin/parameter_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: Parameter management </td>
  </tr>
</table>
			<div class="content">
			<div class="segmentHeader">Parameter Function Edit List </div>
			<table class="formErrorAndButton">
				<tr>
					<td width="75%">
					<div class="formErrorMsg" id="error"><html:errors/></div>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="updateBtn" type="button" class="button" id="updateBtn"
						value="Update" onclick="doSave()">&nbsp;
						<input name="resetBtn" type="reset" class="button" id="resetBtn"
						value="Reset">
					</div>
					</td>
				</tr>
			</table>


			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180"><span class="star">*</span>Function Name</th>
					  <td>
						<input type="text" class="text_protected" name="funName" id="funName" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "FUN_NAME") %>">
					  </td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>Function Field Name</th>
					  <td>
						<input type="text" name="funFieldName" id="funFieldName" class="text_protected" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FUN_FIELD_NAME") %>">
					  </td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>Item</th>
					  <td>
						<input type="text" name="item" id="item" class="text_protected" value="<%=BeanHelper.getHtmlValueByColumn(ref, "ITEM") %>">
					  </td>
					</tr>
					<tr>
					  <th width="180"><span class="star">*</span>Field Value</th>
					  <td>
							<input name="fieldValue" id="fieldValue" type="text" class="text_120" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FIELD_VALUE") %>">
					  </td>
					</tr>
					<tr>
					  <th width="180"><span class="star">*</span>Field Show Name </th>
					  <td>
							<input name="fieldShowName" id="fieldShowName" type="text"  class="text_120" value="<%=BeanHelper.getHtmlValueByColumn(ref, "FIELD_SHOW_NAME") %>">
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
					<td height="15">
					<input type="hidden" id="item" name="item" value="<%=BeanHelper.getHtmlValueByColumn(ref, "ITEM") %>">
					<img src="<%=cp %>/images/spacer.gif"
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
