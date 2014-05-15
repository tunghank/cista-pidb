<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cista.pidb.admin.to.FunctionTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
FunctionTo functionTo = (FunctionTo) request.getAttribute("functionTo");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
var oldfuncName = '<%=BeanHelper.getHtmlValueByColumn(functionTo, "FUNC_NAME")%>';
function update() {
	packAll();
	if ($F('funcName') == "") {
		setMessage("error", "Function name is must field.");
		return;
	}
	$('updateBtn').disabled = true;
	$('cancelBtn').disabled = true;
	setMessage("error", "Checking function exist...");
	var checkUser = new Ajax.Request(
		'<%=cp%>/ajax/check_function_exist.do',
		{
			method: 'get',
			parameters: 'funcName='+ $F('funcName'),
			onComplete: checkFunctionExistComplete
		}
	);
}

	function checkFunctionExistComplete(r) {
		var result = r.responseText;
		if(result=="true" && $F('funcName')!=oldfuncName) {
			setMessage("error", "Function is already exist.");
			$('updateBtn').disabled = false;
			$('cancelBtn').disabled = false;
		} else {
			setMessage("error", "Save function...");
			$('functionUpdate').submit();
		}
	}
</script>
<script language="javascript" type="text/javascript">
	function packAll(){
		packData('URI_Pattern', 'uriPattern', 0, 0);
	}
	function unpackAll() {
		unpackData('uriPattern', 'URI_Pattern', patternTemplate);
	}
</script>
</head>
<body>
<form name="functionUpdate" action="<%=cp %>/admin/function_edit.do?m=save" method="post">
<input type="hidden" id="id" name="id" value="<%=BeanHelper.getHtmlValueByColumn(functionTo, "ID")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: Function management</td>
  </tr>
</table>
			<div class="content">
			<div class="segmentHeader">Function Edit </div>
			<table class="formErrorAndButton">
				<tr>
					<td width="75%">
					<div class="formErrorMsg" id="error"><html:errors/></div>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="updateBtn" type="button" class="button" id="updateBtn"
						value="Update" onclick="update()">
					  <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="document.location.href='<%=cp %>/admin/function_list.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>


			<script language="javascript" type="text/javascript">
var patternTemplate = new Array(
"<INPUT id='#ID#' TYPE='text' class='text' SIZE='30'>",
"<img src='<%=cp%>/images/delete.gif' alt='Delete' width='15' height='15' style='cursor:pointer' onClick='deleteRow(this)'>"
);
</script>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180">Function Name </th>
					  <td>
							<input name="funcName" id="funcName" type="text" size="30" class="text" value="<%=BeanHelper.getHtmlValueByColumn(functionTo, "FUNC_NAME")%>">
					  </td>
					</tr>
					<tr>
					  <th width="180">Menu </th>
					  <td>
							<input name="isMenu" id="isMenu" type="checkbox" size="30" class="text" value="1"
							<%=functionTo.getIsMenu()?"checked":"" %>
							>
					  </td>
					</tr>
					<tr>
					  <th width="180">Position </th>
					  <td>
							<input name="position" id="position" type="text" size="30" class="text" value="<%=BeanHelper.getHtmlValueByColumn(functionTo, "POSITION")%>">
					  </td>
					</tr>										
					<tr>
					  <th width="180" style="vertical-align: top;">URI Pattern</th>
					  <td>
						<input type="hidden" id="uriPattern" name="uriPattern" value="<%=BeanHelper.getHtmlValueByColumn(functionTo, "URI_PATTERN")%>">
						<table id="URI_Pattern" width="150">
							<tr>
								<td colspan="2" style="text-align: left"><input
									class="button" type="button" value="add"
									onclick="newRow('URI_Pattern', patternTemplate)"></td>
							</tr>
						</table>
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
<script type="text/javascript">
unpackAll();
</script>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
