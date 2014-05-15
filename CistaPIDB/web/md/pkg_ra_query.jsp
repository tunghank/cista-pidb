<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
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
<%List<String> pkgVersionList = (List<String>)request.getAttribute("pkgVersionList"); %>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td class="pageTitle"><div>Master Data :: Package RA Query</div>
				</td>
			  </tr>
			</table>
			<div class="content">
			<form action="<%=cp %>/md/pkg_ra_query.do?m=query" method="post">
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
						<th width="180">Product Name</th>
					    <td><input class="text" type="text" name="prodName" id="prodName">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
					        <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                name:"ProductName",
                                mode:1
							});
							</script>
					        &nbsp;</td>
					</tr>
					<tr>
						<th width="180">Project Name</th>
					    <td><input class="text" type="text" name="projName" id="projName">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projNameSSBtn">
					         <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projNameSSBtn",
                                inputField:"projName",
                                name:"ProjectName",
                                mode:1
							});
							</script>&nbsp;</td>
					</tr>
					<tr>
						<th width="180">Package Code</th>
					    <td><input class="text" type="text" name="pkgCode" id="pkgCode">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="pkgCodeSSBtn">
					         <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pkgCodeSSBtn",
                                inputField:"pkgCode",
                                title:"Package Code",
                                table:"PIDB_PACKAGE_RA",
                                keyColumn:"PKG_CODE",
                                mode:1
							});
							</script>&nbsp;</td>
					</tr>
					<tr>
						<th width="180">Worksheet Number</th>
					    <td><input class="text" type="text" name="worksheetNumber" id="worksheetNumber">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="worksheetNumSSBtn">
					         <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"worksheetNumSSBtn",
                                inputField:"worksheetNumber",
                                title:"Worksheet Number",
                                table:"PIDB_PACKAGE_RA",
                                keyColumn:"WORKSHEET_NUMBER",
                                mode:0
							});
							</script>&nbsp;</td>
					</tr>
					<tr>
						<th width="180">Part Num</th>
					    <td><input class="text" type="text" name="partNum" id="partNum">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="partNumSSBtn">
					         <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"partNumSSBtn",
                                inputField:"partNum",
                                title:"part Num",
                                table:"PIDB_PACKAGE_RA",
                                keyColumn:"PART_NUM",
                                mode:0
							});
							</script>&nbsp;</td>
					</tr>
					<tr>
						<th width="180">Finish Period</th>
						<td>
						
								<input class="text" type="text" name="pkgRaActualStartTime" id="pkgRaActualStartTime" readonly>
						  <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="pkgRaRealStartBtn">
						  <script type="text/javascript">
								Calendar.setup({
									inputField:"pkgRaActualStartTime",
									ifFormat:"%Y/%m/%d",
									button:"pkgRaRealStartBtn"
								});
						  </script>&nbsp;~
						 
							<input class="text" type="text" name="pkgRaActualEndTime" id="pkgRaActualEndTime" readonly>
						  <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="pkgRaRealEndBtn">
						  <script type="text/javascript">
								Calendar.setup({
									inputField:"pkgRaActualEndTime",
									ifFormat:"%Y/%m/%d",
									button:"pkgRaRealEndBtn"
								});
						  </script>&nbsp;</td>
							
					</tr>
					<tr>
						<th width="180">Tape vendor</th>
					    <td><input class="text" type="text" name="tapeVendor" id="tapeVendor">
					        <img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeVendorSSBtn">
					         <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeVendorSSBtn",
                                inputField:"tapeVendor",
                                title:"Tape Vendor",
                                table:"PIDB_PACKAGE_RA a,WM_SAP_MASTER_VENDOR b",
                                keyColumn:"SHORT_NAME",
								whereCause:"a.TAPE_VENDOR=b.VENDOR_CODE",
                                mode:0
							});
							</script>&nbsp;</td>
					</tr>
					<tr>
						<th width="180">Assembly Site</th>
						<td><input type="text" readonly name="assySite" id="assySite" class="text"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="assemblySiteSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"assemblySiteSSBtn",
                                inputField:"assySite",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"(FUN_FIELD_NAME='ASSEMBLY_SITE' or FUN_FIELD_NAME='ASSEMBLY_HOUSE') and (fun_name='IC_TAPE' or fun_name='TRAD_PKG')",
								orderBy:"ITEM",
                                title:"ASSEMBLY_SITE",
                                mode:1,
								autoSearch:false,
								callbackHandle:"assemblySiteCallback"
							});

							function assemblySiteCallback(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";
							
									for(var i = 0; i < value.length; i++) {
										tempValue += "," + value[i][columns[0]];
									}
							
									if(tempValue != "") {
										$(inputField).value = tempValue.substring(1);
									}
								}
							}
							</script></td>
					</tr>
					<tr>
						<th width="180"><div>Owner</div></th>
					  <td>
							<select class="select_w130" name="owner" id="owner">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> ownerList = (List<FunctionParameterTo>) request.getAttribute("ownerList");
									for (FunctionParameterTo owner : ownerList) {
										String v = owner.getFieldValue();
										%>
								<option value="<%=v %>"><%=owner.getFieldShowName() %></option>
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
