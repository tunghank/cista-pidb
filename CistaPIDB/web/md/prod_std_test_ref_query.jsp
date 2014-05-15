<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>
<form id="prodStdTestRefForm" action="<%=cp %>/md/prod_std_test_ref_query.do?m=query" method="post">	
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: Product Standard Test Reference Query</td>
  </tr>
</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="180"><div>Product Line</div></th>
					  	<td><select id="productLine" name="productLine" class="select_w130">
					  	    <option value="">--Select--</option>
					        <option value="OA">OA</option>
					        <option value="AV">AV</option>
					        <option value="Mobile">Mobile</option>
					        <option value="Video">Video</option>
					        <option value="ISP">ISP</option>
					        <option value="Power">Power</option>
					        <option value="OP">OP</option>
					        <option value="Vcom">Vcom</option>
					        <option value="PMP">PMP</option>
					        <option value="IP">IP</option>
					        <option value="Others">Others</option>
					  	</select></td>
					</tr>
					<tr>
						<th width="180"><div>Product Family</div></th>
					  	<td><select id="productFamily" name="productFamily" class="select_w130">
					  <option value="">--Select--</option>
							<%
								List<SapMasterProductFamilyTo> productFamily = (List<SapMasterProductFamilyTo>) request.getAttribute("productFamily");
								for(SapMasterProductFamilyTo option : productFamily) {
							%>
							<option value="<%=option.getProductFamily() %>"><%=option.getDescription() %></option>
							<%
								}
							%>
					  	</select></td>
					</tr>
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right"><input name="button1" type="submit"
						class="button" id="button1" value="Search"> <input
						name="button2" type="Reset" class="button" id="button2"
						value="Reset"></div>
					</td>
				</tr>
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
				src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>