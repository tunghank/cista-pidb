<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">


</script>
</head>
<body>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: WLO Material Query</td>
  </tr>
</table>
<div class="content">
		<form id="masterDataQueryForm" action="<%=cp%>/md/wlo_query.do?m=query" method="post">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg">
					<%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
					&nbsp;</div>
					</td>
				</tr>
			</table>
			
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180">Material Number</th>
					  <td><input class="text" name="materialNum" id="materialNum" type="text"> 
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="materialNumBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"materialNumBtn",
                                inputField:"materialNum",
								title:"WLO Material Number",
                                table:"PIDB_WLO",
                                keyColumn:"MATERIAL_NUM",
                                mode:1
							});
							</script>&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<th width="180">Product Name</th>
					  <td>
					  <input class="text"  name="prodName" id="prodName"  type="text">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameBtn">
					   <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameBtn",
                                inputField:"prodName",
                                title:"Product Name",
                                table:"PIDB_WLO",
                                keyColumn:"PROD_NAME",                              
                                mode:1
							});
						</script>
					  
					  </td>
					</tr>

					<tr>
						<th width="180">Product type</th>
					  <td><select class="select_w130" name="prodType" id="prodType" >
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> prodTypeList = (List<FunctionParameterTo>) request.getAttribute("prodTypeList");
										for (FunctionParameterTo version : prodTypeList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select>
				      </td>
					</tr>

					<tr>
						<th width="180">Vendor Code</th>
					  <td><select class="select_w130" name="vendorCode" id="vendorCode" >
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> vendorCodeList = (List<FunctionParameterTo>) request.getAttribute("vendorCodeList");
										for (FunctionParameterTo version : vendorCodeList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select>
				      </td>
					</tr>
				</tbody>
			</table>
			
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right">
					<input name="button1" type="submit"	class="button" id="button1" value="Search">
					<input name="button2" type="reset" class="button" id="button2" value="Reset">
					</div>
					</td>
				</tr>
			</table>
			</form>
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
			<td height="131" colspan="2"><img height="2" alt=""
				src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
