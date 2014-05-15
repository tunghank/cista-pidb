<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.admin.to.FunctionTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	List<FunctionTo> result = (List<FunctionTo>) request.getAttribute("allFunctions");	

	String queryStatusMsg = (String)request.getAttribute("statusMsg");
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function init() {
		autoFitBottomArea('resultPanel', 180);
	}
	window.onload = init;

	function deleteFunction() {
		if (confirm("Are you sure delete selected function?")) {
			$('functionQuery').action="<%=cp %>/admin/function_list.do?m=delete";
			$('functionQuery').submit();
		}
	}
	
	function update() {
		$('functionQuery').action="<%=cp %>/admin/function_edit.do?m=pre";
		$('functionQuery').submit();	
	}
</script>
</head>
<body>
<form name="functionQuery" action="<%=cp %>/admin/function_list.do?m=query" method="post">
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
			<div class="segmentHeader">Function List </div>
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg"><html:errors/><!--ErrorMessage here--></div>
					</td>
					<td>
					<div align="right">
					  <input
						name="createBtn" type="button" class="button" id="createBtn"
						value="Create" onclick="document.location.href='<%=cp %>/admin/function_create.do?m=pre'">
<%if(result!=null && result.size() > 0) { %>
		  <input
			name="updateBtn" type="button" class="button" id="updateBtn"
			value="Update Function" onclick="update()">
		  <input
			name="deleteBtn" type="button" class="button" id="deleteBtn"
			value="Delete Function" onclick="deleteFunction()">
<%} %>
					</div>
					</td>
				</tr>
			</table>

<%if(result!=null && result.size() > 0) { %>
<table border=0 cellpadding=0 cellspacing=0 width="400px"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th style="width:20px">&nbsp;</th>
					<th style="text-align:center">Function Name</th>
				</tr>
<%
	    boolean firstRow = true;
	    for(FunctionTo function : result) {
	        String checked = "";
	        if (firstRow) {
	            checked = "checked";
	            firstRow = false;
	        }
%>
	        	<tr>
					<td><input type="radio" name="selectedFunction" id="selectedFunction" value="<%=BeanHelper.getHtmlValueByColumn(function, "ID") %>" <%=checked %>></td>
					<td><%=BeanHelper.getHtmlValueByColumn(function, "FUNC_NAME") %>&nbsp;</td>
				</tr>
	        <%
	    }
%>
				</tbody>
			</table>
</div>
</td></tr></table>
<%} %>
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
</form>
</body>
</html>
