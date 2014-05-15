<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.ProductTo"%>
<%@ include file="/common/global.jsp"%>
<%
	ProductTo productTo = (ProductTo)request.getAttribute("productTo");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>
<form name="selectForm" action="<%=cp %>/dialog/product_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Edit Product Code</td>
				</tr>
			</table>
			<div class="content">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<input type="hidden" id="prodName" name="prodName" value="<%=productTo.getProdName() %>">
				<input type="hidden" id="prodOption" name="prodOption" value="<%=productTo.getProdOption() %>">
				<tr>
					<th width="120" nowrap><div align="right">Product Code:</div></th>
					<td class="c0"><input type="text" class="text_protected" readonly name="prodCode" value="<%=productTo.getProdCode() %>"></td>
				</tr>
				<tr>
				    <th style="text-align:right">Remark:</th>
				    <td class="c0"><input type="text" class="text" name="remark" value="<%=productTo.getRemark() %>"><input type="button" class="button" value="Edit" onclick="submit();" ></td>
				</tr>
				</tbody>
			</table>



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
				src="<%=cp %>/images/shadow-2.gif" width="585" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
