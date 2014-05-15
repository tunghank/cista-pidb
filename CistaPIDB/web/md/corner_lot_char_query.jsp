<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
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
    <td class="pageTitle">Master Data :: Corner Lot Characterization Query</td>
  </tr>
</table>
			<div class="content">
			<form id="masterDataQueryForm" action="<%=cp%>/md/corner_lot_char_query.do?m=query" method="post">
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
						<th width="180">Product Code</th>
					  <td><input class="text" type="text" id="prodCode" name="prodCode">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
                                title:"Product Code",
                                table:"PIDB_CORNER_LOT_CHAR",
                                keyColumn:"PROD_CODE",
                                mode:1
							});
							</script></td>
					</tr>
					<tr>
						<th width="180">Project Name</th>
					  <td><input class="text" type="text" id="projName" name="projName">
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
					</tr>
					<tr>
						<th width="180">Fab/Fab Description
						 </th>
					  <td><select name="fab" id="fab"  class="select_w130">
					  	<option value="">--Select--</option>
						 <%List<FabCodeTo> fabCodeList = (List<FabCodeTo>) request.getAttribute("fabCodeList");
								for(FabCodeTo fab : fabCodeList) {
							%>
							<option value="<%=fab.getFab() %>"><%=fab.getFab() %>,<%=fab.getFabDescr() %></option>
							<%
								}
							%>
						</select>
				      </td>
					</tr>
					<tr>
						<th width="180">Option</th>
					  <td><select name="option" id="option"  class="select_w130">
					  	<option value="">--Select--</option>
						<%List<String> optionList = (List<String>)request.getAttribute("optionList"); 
						 if (optionList != null && optionList.size() > 0 ) {
						  for (int i=0; i<optionList.size(); i++) {
					 		if (optionList.get(i) != null) {
					 	 %>
					  	<option value="<%=optionList.get(i) %>"><%=optionList.get(i) %></option>
					  	<%}}} %> 
						</select></td>
					</tr>
					<tr>
					<th width="180">Project Code w Version</th>
					  <td><input class="text" type="text" id="projCodeWVersion" name="projCodeWVersion">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="pjCWVSSBtn">
					     <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pjCWVSSBtn",
                                inputField:"projCodeWVersion",
                                title:"Project Code With Version",
                                table:"PIDB_CORNER_LOT_CHAR",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                mode:1
							});
							</script>
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
