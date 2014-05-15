<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td ><label class="formErrorMsg" style="color:red"><%=request.getAttribute("error") != null ? request.getAttribute("error") : "" %></label></td>
  </tr>
  <tr>
    <td class="pageTitle">Welcome, Dear Cista User </td>
  </tr>
</table>
<!-- Content start -->
<div class="content">
<table height="300">
	<tr>
		<td>
		</td>
	</tr>
</table>
</div>
<!-- Content end -->
			<!--Html End--></td>
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
