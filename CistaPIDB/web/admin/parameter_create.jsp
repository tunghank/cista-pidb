<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.admin.to.ParameterTo"%>
<%@ page import="com.cista.pidb.admin.dao.ParameterDao"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="javax.servlet.http.HttpServletResponse"%>
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
		
		$('createBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_parameter_exist.do',
			{
				//��ajax�e���ݦp�G�����媺���ܡA�@�w�n��post�A�d�U���n�ϥ�get�A�|�ܦ��ýX
				method: 'post',
				parameters: 'funName='+ $F('funName') + '&funFieldName=' + $F('funFieldName') + '&fieldValue=' + $F('fieldValue') + '&fieldShowName=' + $F('fieldShowName'),
				onComplete: checkParameterExistComplete
			}
		);
	}
	
function checkParameterExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "The Parameter Function Data is already exist.");		
		$('createBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		setMessage("error", "Saving Parameter Data...");		
										
		$('parameterCreate').action = $('parameterCreate').action+ "&funName=" + $F('funName')+ "&funFieldName=" + $F('funFieldName') + "&fieldValue=" + $F('fieldValue') + "&fieldShowName=" + $F('fieldShowName');
		$('parameterCreate').submit();		
	}
}

function getItemHandle(inputField, columns, value) {
	$('funFieldName').value = value[0]['FUN_FIELD_NAME'];
	new Ajax.Request(
			'<%=cp%>/ajax/parameter_item_list.do',
			{
				method: 'get',
				parameters: 'funName='+$F('funName')+'&funFieldName='+ $F('funFieldName'),
				onComplete: fetchItemComplete
			}
		);
}

function fetchItemComplete(r) {	
	var returnValue = r.responseText;
	$('item').value =returnValue;
}
</script>
</head>
<body>
<form name="parameterCreate" action="<%=cp %>/admin/parameter_create.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: Parameter management</td>
  </tr>
</table>
			<div class="content">
			<div class="segmentHeader">Parameter Function Add new List </div>
			<table class="formErrorAndButton">
				<tr>
					<td width="75%">
					<div class="formErrorMsg" id="error"><html:errors/></div>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="createBtn" type="button" class="button" id="createBtn"
						value="Create" onclick="doSave()">&nbsp;
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
						<input type="text" class="text" name="funName" id="funName" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "FUN_NAME") %>">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="funNameSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"funNameSSBtn",
                                inputField:"funName",
                                title:"Function Name",
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                columns:"FUN_NAME",
                                keyColumn:"FUN_NAME",
                                autoSearch:false,
                                mode:0
							});
						</script>
					  </td>
					</tr>
					<tr>
						<th width="180"><span class="star">*</span>Function Field Name</th>
					  <td>
						<input type="text" name="funFieldName" id="funFieldName" class="text" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "FUN_FIELD_NAME") %>">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="funFieldNameSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"funFieldNameSSBtn",
                                inputField:"funFieldName",
                                title:"Function Field Name",
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                columns:"FUN_FIELD_NAME",
                                keyColumn:"FUN_FIELD_NAME",
                                autoSearch:false,
                                mode:0,
                                whereCause:"FUN_NAME={funName}",
								callbackHandle:"getItemHandle"
							});
						</script>
					  </td>
					</tr>
					<tr>
					  <th width="180"><span class="star">*</span>Item</th>
					  <td>
							<input name="item" id="item" type="text" class="text" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "ITEM") %>">
					  </td>
					</tr>
					<tr>
					  <th width="180"><span class="star">*</span>Field Value </th>
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
