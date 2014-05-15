<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="com.cista.pidb.md.to.ZsUsbramTo" %>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>

<%
	ZsUsbramTo ref = (ZsUsbramTo) request.getAttribute("ref");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
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
    <td class="pageTitle">Master Data :: USBRAM Query</td>
  </tr>
</table>
	<div class="content">
	<form id="masterDataQueryForm" action="<%=cp%>/md/zs_usbram_query.do?m=query" method="post">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg">
					<%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
					&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull"  border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="180">Material Number </th>
					  <td>
					    <input  name="materialNum" id="materialNum"  type="text" class="text">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="materialNumSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"materialNumSSBtn",
                                inputField:"materialNum",
                                title:"Material Number",
                                table:"PIDB_USB_RAM",
                                keyColumn:"MATERIAL_NUM",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
						<th width="180">MEMORY SIZE </th>
					  <td>
					    <input  name="memorySize" id="memorySize" type="text"  class="text">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="memorySizeSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"memorySizeSSBtn",
                                inputField:"memorySize",
                                title:"MEMORY SIZE",
                                table:"PIDB_USB_RAM",
								keyColumn:"MEMORY_SIZE",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
					<th width="180">Operation Voltage</th>
					  <td><input name="operationVoltage" id="operationVoltage"  type="text" class="text">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="operationVoltageSSBtm">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"operationVoltageSSBtm",
                                inputField:"operationVoltage",
                                title:"operationVoltage",                                
                                table:"PIDB_USB_RAM",
                                keyColumn:"OPERATION_VOLTAGE",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
					<th width="180">USBRAM Vendor</th>
					  <% 
							SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
							SapMasterVendorTo sapMasterVendorTo = sapMasterVendorDao.findByVendorCode(BeanHelper.getHtmlValueByColumn(ref, "VENDOR_CODE"));
						%>
						<td><input class="text" type="text" readonly id="usbramVendor" name="usbramVendor"  value=""  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="usbramVendorSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"usbramVendorSSBtn",
                                inputField:"usbramVendor",
                                table:"WM_SAP_MASTER_VENDOR",
                                keyColumn:"SHORT_NAME",
                                columns:"SHORT_NAME,VENDOR_CODE",
                                autoSearch:false,
                                title:"USBRAM VENDOR"
							});
							</script>
					  </td>
					</tr>
					<th width="180">Input/Output</th>
					  <td>
					    <input name="io" id="io" type="text" class="text">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="ioSSBtm">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"ioSSBtm",
                                inputField:"io",
                                title:"io",                                
                                table:"PIDB_USB_RAM",
                                keyColumn:"IO",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<th width="180">Support Max Resolution</th>
					  <td>
					    <input name="maxResolution" id="maxResolution" type="text" class="text">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="maxResolutionSSBtm">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"maxResolutionSSBtm",
                                inputField:"maxResolution",
                                title:"maxResolution",                                
                                table:"PIDB_USB_RAM",
                                keyColumn:"MAX_RESOLUTION",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
						<th width="180">APPLICATION PRODUCT</th>
					  <td>
					    <input  name="applicationProduct" id="applicationProduct" type="text"  class="text_300">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="applicationProductSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"applicationProductSSBtn",
                                inputField:"applicationProduct",
                                title:"APPLICATION PRODUCT",
                                table:"PIDB_PRODUCT",
								keyColumn:"PROD_CODE",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
					<th width="180">Compay</th>
					  <td>
						<select class="select_w130" name="releaseTo" id="releaseTo">
							<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> companyList = (List<FunctionParameterTo>) request.getAttribute("companyNameList");
									for (FunctionParameterTo company : companyList) {
										String v = company.getFieldValue();
										
										%>
								<option value="<%=v %>"><%=company.getFieldShowName() %></option>
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
					<input name="button2" type="Reset" class="button" id="button2" value="Reset">
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
			<td colspan="2"><img height="2" alt=""
				src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
