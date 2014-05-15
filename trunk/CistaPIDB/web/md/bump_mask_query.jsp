<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.md.to.ProjectCodeTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">

</head>
<body>
<form id="bumpMaskQueryForm" action="<%=cp %>/md/bump_mask_query.do?m=query" method="post">		
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Bumping Mask Query</td>
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
						<th width="180">Project Name</th>
					  <td>
					    <input type="text" class="text" name="projName" id="projName" value="">
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
					  <div></div></th>
						<td><label> <select class="select_w130" name="fab" id="fab">
							<option value="">--Select--</option>
							<%
								List<FabCodeTo> fabCodeList = (List<FabCodeTo>) request.getAttribute("fabCodeList");
								for(FabCodeTo fab : fabCodeList) {
							%>
							<option value="<%=fab.getFab() %>"><%=fab.getFab() %>,<%=fab.getFabDescr() %></option>
							<%
								}
							%>
						</select></label></td>
					</tr>
					<tr>
					<th width="180">Option</th>
					  <td>
					    <label><select class="select_w130" name="projOption" id="projOption">
							<option value="">--Select--</option>
							<%
								List<String> projectCodeList = (List<String>) request.getAttribute("optionList");
								for(String option : projectCodeList) {
									if(option!=null && option.length()>0) {
							%>
							<option value="<%=option %>"><%=(option==null)?"":option %></option>
							<%
									}
								}
							%>
						</select></label>					  </td>
					</tr>
					<tr>
					<th width="180">Mask Name</th>
					  <td>
					    <input type="text" class="text" name="maskName" id="maskName" value="">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="maskNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"maskNameSSBtn",
                                inputField:"maskName",
                                table:"PIDB_BUMPING_MASK",
                                keyColumn:"MASK_NAME",
                                title:"Mask Name",
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
