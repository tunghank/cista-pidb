<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ include file="/common/global.jsp"%>
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
    <td class="pageTitle">Master Data :: IC Wafer Query</td>
  </tr>
</table>
	<div class="content">
	<form id="masterDataQueryForm" action="<%=cp%>/md/ic_wafer_query.do?m=query" method="post">
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
                                table:"PIDB_IC_WAFER",
                                keyColumn:"MATERIAL_NUM",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
						<th width="180">Project Code </th>
					  <td>
					    <input  name="projCode" id="projCode" type="text"  class="text">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeSSBtn",
                                inputField:"projCode",
                                name:"ProjectCode",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
					<th width="180">Project Code w Version</th>
					  <td>
					    <input name="projCodeWVersion" id="projCodeWVersion" type="text" class="text">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCWVSSBtm">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCWVSSBtm",
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
					<th width="180">Wafer Body Version</th>
					  <td><select name="bodyVer" id="bodyVer"  class="select_w130">
					  <option value="">--Select--</option>
					  <% List<String> bodyVersionList = (List<String>)request.getAttribute("bodyVersionList");
					  	if (bodyVersionList != null && bodyVersionList.size() > 0 ) {
						  for (int i=0; i<bodyVersionList.size(); i++) {
							   if (bodyVersionList.get(i) != null) {
					 		
					  %>
					  	<option value="<%=bodyVersionList.get(i) %>"><%=bodyVersionList.get(i) %></option>
					  	<%}}} %> 
					  	</select>
					  </td>
					</tr>
					<tr>
					<th width="180">Wafer Option Version</th>
					  <td>
					 	<select name="optionVer" id="optionVer" class="select_w130">
					 		 <option value="">--Select--</option>
					  <%List<String> optionVersionList = (List<String>)request.getAttribute("optionVersionList"); 
					  if (optionVersionList != null && optionVersionList.size() > 0 ) {
						  for (int i=0; i<optionVersionList.size(); i++) {
					 	 if (optionVersionList.get(i) != null) {
					  %>
					  	<option value="<%=optionVersionList.get(i) %>"><%=optionVersionList.get(i) %></option>
					  	<%}}} %> 
					  	</select>
					  </td>
					</tr>
					<!-- Delete 2007/09/21 Hank Tang -->
					<!-- <tr>
					<th width="180">Routing (WF)</th>
					  <td>
						<label> <select style="width:130px" name="routingWf"
							id="routingWf">
							<option value="">--Select--</option>
							<option value="1">Yes</option>
							<option value="0">No</option>
							</select></label>					  
					  </td>
					</tr>
					<tr>
					<th width="180">Routing (BP)</th>
					  <td>
						<label> <select style="width:130px" name="routingBp"
							id="routingBp">
							<option value="">--Select--</option>
							<option value="1">Yes</option>
							<option value="0">No</option>
							</select></label>					  					  
					  </td>
					</tr>
					<tr>
					<th width="180">Routing (CP)</th>
					  <td>
						<label> <select style="width:130px" name="routingCp"
							id="routingCp">
							<option value="">--Select--</option>
							<option value="1">Yes</option>
							<option value="0">No</option>
							</select></label>						  
					  </td>
					</tr>-->
					<tr>
						<th width="180">Tape Out Date</th>
					  	<td><input name="tapeOutDateFrom" id="tapeOutDateFrom" class="text" readonly>
					    <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="tapeOutDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"tapeOutDateFrom",
								ifFormat:"%Y/%m/%d",
								button:"tapeOutDateBtn"
							});
						</script>
					    ~
				        <input name="tapeOutDateTo" id="tapeOutDateTo" class="text" readonly>
						<img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="tapeOutDateBtntoBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"tapeOutDateto",
								ifFormat:"%Y/%m/%d",
								button:"tapeOutDateBtntoBtn"
							});
						</script>
					   </td>
					</tr>
					<tr>
					<th width="180">Status</th>
					  <td><label>
					    <select name="status" id="status"  class="select_w130">
					      <option value="">--Select--</option>
					      <option value="Draft">Draft</option>
					      <option value="Completed">Completed</option>
					      <option value="Released">Released</option>
				        </select></label>
					  </td>
					</tr>
					<tr>
					<th width="180">Fab Device ID</th>
					  <td>

					  <input name="fabDeviceId" id="fabDeviceId" type="text" class="text">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="fabDeviceIdtm">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"fabDeviceIdtm",
                                inputField:"fabDeviceId",
                                title:"Fab Device ID",                                
                                table:"PIDB_IC_WAFER",
                                keyColumn:"FAB_DEVICE_ID",
								orderBy:"FAB_DEVICE_ID",
                                mode:1
							});
						</script>
					  <!-- <select name="fabDeviceId" id="fabDeviceId"  class="select_w130">
					  <option value="">--Select--</option>
					  <% List<String> fabDeviceIdList = (List<String>)request.getAttribute("fabDeviceIdList");
					  	if (fabDeviceIdList != null && fabDeviceIdList.size() > 0 ) {
						  for (int i=0; i<fabDeviceIdList.size(); i++) {
							   if (fabDeviceIdList.get(i) != null) {
					 		
					  %>
					  	<option value="<%=fabDeviceIdList.get(i) %>"><%=fabDeviceIdList.get(i) %></option>
					  	<%}}} %> 
					  	</select> -->
					  </td>
					</tr>
					<tr>
					<th width="180">Revision Item</th>
					  <td><input name="revisionItem" id="revisionItem" type="text" class="text">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="revisionItemtm">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"revisionItemtm",
                                inputField:"revisionItem",
                                title:"REVISION ITEM",                                
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                keyColumn:"FIELD_VALUE",
								columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='REVISION_ITEM'",
								orderBy:"ITEM",
                                mode:1
							});
						</script>
					  </td>
					</tr>
					<tr>
					<th width="180">Mask House</th>
					  <td><input name="maskHouse" id="maskHouse" type="text" class='text_200'>
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="MaskHouseFlowSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"MaskHouseFlowSSBtn",
                                inputField:"maskHouse",
                                title:"Mask House",                                
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                keyColumn:"FIELD_VALUE",
								columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='MASK_HOUSE' and fun_name='IC_WAFER'",
								orderBy:"ITEM",
                                mode:0
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
