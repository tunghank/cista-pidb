<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.HtolRaQueryTo"%>
<%@ page import="com.cista.pidb.md.dao.HtolRaDao"%>
<%@ page import="com.cista.pidb.md.to.HtolRaTo"%>
<%@ page import="com.cista.pidb.admin.to.ParameterTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.code.dao.FunctionParameterDao"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	HtolRaQueryTo queryTo = (HtolRaQueryTo) request.getAttribute("queryTo");
	List<HtolRaQueryTo> result = (List) request.getAttribute("result");
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
	
	function createWithRef() {
		if (!$('ref') || getRadioValue('ref') == null) {
			setMessage("error", "Please select an object for reference with.");
			return;
		} else {
			document.location.href='<%=cp%>/md/htol_ra_create.do?m=pre&ref=' + getRadioValue('ref');
		}
	}	
</script>
</head>
<body>
<span class="segmentHeader"></span>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: HTOL RA Query Result</td>
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
			<table border="0" cellpadding="0" cellspacing="0" class="segmentHeader" width="100%">
				<tr>
					<td style="font-size: 12px;">
					<a href="#" onClick="showHidePanel('queryCriteriaPanel');init()">Query Criteria</a>
					</td>
					<td >
					<div align="right">
					
					  	<% //Added on 3/9
						String isGuest="No";
						RoleDao roleDao = new RoleDao();
						UserTo currentUser = PIDBContext.getLoginUser(request);
						List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());
						if (checkedRoles != null) {
                                                    for (RoleTo roleTo : checkedRoles) {
                                                          if (roleTo.getRoleName().equals("Guest") )  {
                                                            isGuest="Yes";
                                                            } 
                                                        }
                                                      } 
						if ( isGuest.equals("No"))  {
						   %>
					
					  <input name="button1" type="button" class="button" id="button1"
						value="Create New" onClick="document.location.href='<%=cp%>/md/htol_ra_create.do?m=pre'">
					  <input name="button2" type="button" class="button" id="button2"
						value="Create With Reference" onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3"
						value="New Query" onClick="document.location.href='<%=cp%>/md/htol_ra_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Product Name</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_NAME") %>&nbsp;</td>
						<th width="20%">RA Test Result</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "RA_TEST_RESULT") %>&nbsp;</td>					
					</tr>
					<tr>
						<th width="20%">Project Name</th>
					 	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_NAME") %>&nbsp;</td>					
						<th width="20%">Owner</th>
					  	<td width="30%"> <%
					  String funName = "RA";
					  String funFiledName= "OWNER";
					  String fieldValue = BeanHelper.getQueryCriteriaByColumn(queryTo, "OWNER");
					  FunctionParameterDao dao = new FunctionParameterDao();
					  FunctionParameterTo to  = new FunctionParameterTo();
						to = dao.findValueByFiledValue(funName,funFiledName,fieldValue);
						if (to != null){
							//System.out.println(to.getFieldShowName());
							%>
						
					  <%=to.getFieldShowName()%>&nbsp;</td>
					  <%
						  }
					  
					  %></td>							 	
					</tr>
					<tr>
						<th width="20%">Project Code With Version</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_CODE_W_VERSION") %>&nbsp;</td>					
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
				</tbody>
			</table>

<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/htol_ra_query.do" method="post">
<%=BeanHelper.generateHtmlHiddenField(queryTo, true) %>
			<table class="formErrorAndButton">
				<tr>
					<td><%=queryTo!=null?queryTo.getPaging():"" %>
					&nbsp;</td>
					<td>
					  <% 
					  	boolean downloadable = false;
					  
						if (checkedRoles != null) {
                            for (RoleTo roleTo : checkedRoles) {
                                  if (roleTo.getRoleName().equalsIgnoreCase("downloadable") )  {
                                      downloadable = true;
                                  } 
                            }
                         } 	
						
						if (downloadable) {
					  %>						
					<div align="right"><input name="downloadBtn" type="button"
						class="button" id="downloadBtn" value="download all" onclick="reportDownload('pagingForm')"></div>
						<%
						}
						%>
					</td>
				</tr>
			</table>

			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td>
					<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
					<table class="grid" border="0" cellpadding="1" cellspacing="1">
						<tbody>
							<tr>
								<th width="1%">&nbsp;</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Name </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Name </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Code With Version </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">RA Test Result </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Owner </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">RA Test Item </th>
							</tr>
<%
	int idx = 0;
	for(HtolRaQueryTo htolRa : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		HtolRaDao paraDao = new HtolRaDao();
		ParameterTo paraTo = paraDao.findByFieldValue(BeanHelper.getHtmlValueByColumn(htolRa, "OWNER"));	
%>
							<tr>
								<td <%=tdcss %>><input type="radio" name="ref" value="<%=BeanHelper.getHtmlValueByColumn(htolRa, "PROJ_CODE_W_VERSION") %>"></td>
								<td <%=tdcss %>>
								<a href="<%=cp %>/md/htol_ra_edit.do?m=pre&projCodeWVersion=<%=BeanHelper.getHtmlValueByColumn(htolRa, "PROJ_CODE_W_VERSION") %>&raTestItem=<%=BeanHelper.getHtmlValueByColumn(htolRa, "RA_TEST_ITEM") %>"><%=BeanHelper.getHtmlValueByColumn(htolRa, "PROD_NAME") %></a>&nbsp;
								</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(htolRa, "PROJ_NAME") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(htolRa, "PROJ_CODE_W_VERSION") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(htolRa, "RA_TEST_RESULT") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(paraTo, "FIELD_SHOW_NAME") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(htolRa, "RA_TEST_ITEM") %>&nbsp;</td>
							</tr>
<%
	}
%>
						</tbody>
					</table>
					</div>
					</td>
				</tr>
			</table>
</form>
<% 
	} else {
%>
No result.
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
</body>
</html>
