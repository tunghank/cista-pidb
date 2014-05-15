<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.admin.to.ParameterTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	ParameterTo queryTo = (ParameterTo)request.getAttribute("queryTo");
	List<ParameterTo> result = (List<ParameterTo>) request.getAttribute("result");
	
	boolean noQuery = true;
	if(queryTo!=null) {
	    noQuery = false;
	}
	
	String queryStatusMsg = (String)request.getAttribute("statusMsg");;
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function init() {
		autoFitBottomArea('resultPanel', 180);
	}
	window.onload = init;
	
	function deletePara() {
		if (confirm("Are you sure delete selected Parameter List?")) {
			$('parameterQuery').action="<%=cp %>/admin/parameter_list.do?m=delete";
			$('parameterQuery').submit();
		}
	}
</script>
</head>
<body>
			<form name="parameterQuery" action="<%=cp %>/admin/parameter_list.do?m=query" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: Pamameter management</td>
  </tr>
</table>
			<div class="content">
			<table border="0" cellpadding="0" cellspacing="0" class="segmentHeader" width="100%">
				<tr>
					<td style="font-size: 12px;">
					<a href="#" onclick="showHidePanel('queryCriteriaPanel');init()">Parameter List Search</a>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="searchBtn" type="submit" class="button" id="searchBtn"
						value="Search">
					  <input name="button2" type="Reset" class="button" id="button2" 		 	value="Reset">
					</div>
					</td>
				</tr>
			</table>

			<table id="queryCriteriaPanel" class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180">Function Name</th>
					  <td>
						<input type="text" class="text" name="funName" id="funName" readonly  value="<%=BeanHelper.getHtmlValueByColumn(queryTo, "FUN_NAME") %>">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="funNameSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"funNameSSBtn",
                                inputField:"funName",
                                title:"Function Name",
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                columns:"FUN_NAME",
                                keyColumn:"FUN_NAME",
                                autoSearch:false,
                                mode:0
							});
						</script>
					  </td>
					</tr>
					<tr>
						<th width="180">Function Field Name</th>
					  <td>
						<input type="text" name="funFieldName" id="funFieldName" class="text" readonly value="<%=BeanHelper.getHtmlValueByColumn(queryTo, "FUN_FIELD_NAME") %>">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="funFieldNameSSBtn">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"funFieldNameSSBtn",
                                inputField:"funFieldName",
                                title:"Function Field Name",
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                columns:"FUN_FIELD_NAME",
                                keyColumn:"FUN_FIELD_NAME",
                                autoSearch:false,
                                mode:0,
                                whereCause:"FUN_NAME={funName}"
							});
						</script>
					  </td>
					</tr>
				</tbody>
			</table>
<%
if(!noQuery) {
%>
<div class="segmentHeader"><%=queryStatusMsg%> </div>
<%if(result!=null && result.size() > 0) { %>
<table class="formErrorAndButton">
	<tr>
		<td>
		<div class="formErrorMsg" id="error"></div>
		</td>
		<td>
		<div align="right">
		</div>
		</td>
	</tr>
</table>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th style="width:20px">&nbsp;</th>
					<th style="text-align:center">Function Name</th>
					<th style="text-align:center">Function Field Name</th>
					<th style="text-align:center">Field Value</th>
					<th style="text-align:center">Item</th>
					<th style="text-align:center">Field Show Name</th>
				</tr>
<%
	    boolean firstRow = true;
		int idx = 0;
	    for(ParameterTo para : result) {
			idx ++;
			String tdcss = "class=\"c" + idx % 2+"\"";
	        String checked = "";
	        if (firstRow) {
	            checked = "checked";
	            firstRow = false;
	        }
%>
	        	<tr>
					<td <%=tdcss %>><input type="radio" name="ref" id="ref" value="<%=BeanHelper.getHtmlValueByColumn(para, "FUN_NAME") %>,<%=BeanHelper.getHtmlValueByColumn(para, "FUN_FIELD_NAME") %>,<%=BeanHelper.getHtmlValueByColumn(para, "FIELD_VALUE") %>,<%=BeanHelper.getHtmlValueByColumn(para, "FIELD_SHOW_NAME") %>" <%=checked %>></td>
					<td <%=tdcss %>><a href="<%=cp %>/admin/parameter_edit.do?m=pre&funName=<%=BeanHelper.getHtmlValueByColumn(para, "FUN_NAME") %>&funFieldName=<%=BeanHelper.getHtmlValueByColumn(para, "FUN_FIELD_NAME") %>&fieldValue=<%=BeanHelper.getHtmlValueByColumn(para, "FIELD_VALUE") %>&fieldShowName=<%=BeanHelper.getHtmlValueByColumn(para, "FIELD_SHOW_NAME") %>"><%=BeanHelper.getHtmlValueByColumn(para, "FUN_NAME") %>&nbsp;</a></td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(para, "FUN_FIELD_NAME") %>&nbsp;</td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(para, "FIELD_VALUE") %>&nbsp;</td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(para, "ITEM") %>&nbsp;</td>
					<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(para, "FIELD_SHOW_NAME") %>&nbsp;</td>
				</tr>
	        <%
	    }
%>
				</tbody>
			</table>
</div>
</td></tr></table>
<%} %>
			</div>
<%} %>
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
