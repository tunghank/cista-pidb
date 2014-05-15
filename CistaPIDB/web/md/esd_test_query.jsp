<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
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
    <td class="pageTitle">Master Data :: ESD Test Query</td>
  </tr>
</table>
	<div class="content">
	<form id="masterDataQueryForm" action="<%=cp%>/md/esd_test_query.do?m=query" method="post">
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
						<th width="180">Product Code</th>
					  <td>
					    <input type="text"  class="text" name="prodCode" id="prodCode" >
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
								title:"Product Name",
                                table:"PIDB_PRODUCT",
                                keyColumn:"PROD_CODE",
								columns:"PROD_CODE,PROD_NAME",
                                mode:1
							});
							</script>
					  </td>
					</tr>
					<!--<tr>
						<th width="180">Project Name</th>
					  <td>
					    <input type="text" class="text" name="projName" id="projName">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projNameSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projNameSSBtn",
                                inputField:"projName",
                                name:"ProjectName",
                                mode:1
							});
							</script>
					  </td>
					</tr>-->
					<tr>
					<th width="180">Project Code w Version</th>
					  <td>
					    <input type="text" class="text" name="projCodeWVersion" id="projCodeWVersion">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="pjCWVSSBtn">
					     <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pjCWVSSBtn",
                                inputField:"projCodeWVersion",
                                title:"Project Code With Version",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                mode:1
							});
							</script>
					  </td>
					</tr>
					<tr>
					<th width="180">ESD Finish Date</th>
					  <td><input type="text" class="text" name="esdFinishDate" id="esdFinishDate" readonly >
					    <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="esdFinishDateBrn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"esdFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"esdFinishDateBrn"
							});
						</script>
						&nbsp;
					   </td>
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
