<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.ProjectQueryTo"%>
<%@ page import="com.cista.pidb.md.to.ProjectTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterProductFamilyDao"%>
<%@ page import="com.cista.pidb.code.dao.FabCodeDao"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
	ProjectQueryTo queryTo = (ProjectQueryTo) request.getAttribute("queryTo");
	List<ProjectTo> result = (List) request.getAttribute("result");
	SapMasterProductFamilyDao sapMasterProductFamilyDao = new SapMasterProductFamilyDao();
	FabCodeDao fabCodeDao = new FabCodeDao();
	String desc = sapMasterProductFamilyDao.findDescByProdFamily(BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_FAMILY"));
	desc = desc != null && desc.length() > 0 ? desc : "All";
	
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
			document.location.href='<%=cp%>/md/proj_create.do?m=pre&ref=' + getRadioValue('ref');
		}
	}
</script>
</head>
<body>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Project Query Result</td>
				</tr>
			</table>
			<div class="content">
			<table border="0" cellpadding="0" cellspacing="0"
				class="segmentHeader" width="100%">
				<tr>
					<td style="font-size: 12px;"><a href="#"
						onclick="showHidePanel('queryCriteriaPanel');init()">Query
					Criteria</a></td>
					<td>
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
					        <input name="button3" type="button"
						   class="button" id="button3" value="Create New"
						   onclick="document.location.href='<%=cp%>/md/proj_create.do?m=pre'">
					        <input name="button3" type="button" class="button" id="button4"
						   value="Create With Reference"
						   onclick="createWithRef()">
					        <%
						   }     
				        %>
					<input name="button3" type="button" class="button" id="button5"
						value="New Query"
						onclick="document.location.href='<%=cp%>/md/proj_query.do?m=pre'">
					
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Project Code</th>
						<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_CODE") %>&nbsp;</td>
						<th width="20%">Product Family</th>
						<td width="30%"><%=desc %>&nbsp;</td>
					</tr>
					<tr>
						<th>Project Name</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_NAME") %>&nbsp;</td>
						<th>Product Line</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_LINE") %>&nbsp;</td>
					</tr>
					<tr>
						<th>Product Code</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_CODE") %>&nbsp;</td>
						<th>Process Technology</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROC_TECH") %>&nbsp;</td>
					</tr>
					<tr>
						<th>Fab</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FAB") %>&nbsp;</td>
						<th>Customer</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "CUST") %>&nbsp;</td>
					</tr>
					<tr>
						<!-- Delete Option , Add Nick Name for Hank Tang 2007/10/02 -->
						<!--<th>Option</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_OPTION") %>&nbsp;</td>-->
						<th>Nick Name</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "NICK_NAME") %>&nbsp;</td>
						<th>Team Member</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TEAM_MEMBER") %>&nbsp;</td>
					</tr>
					<tr>
						<th>Panel Type</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PANEL_TYPE") %>&nbsp;</td>
						<th>Kick-off Date</th>
						<td>
						<%if (queryTo.getKickOffDateFrom() == null && queryTo.getKickOffDateTo() == null) out.print("All"); else {%>
						<%=BeanHelper.getHtmlValueByColumn(queryTo, "KICK_OFF_DATE_FROM") %>~<%=BeanHelper.getHtmlValueByColumn(queryTo, "KICK_OFF_DATE_TO") %>
						<%}%>
						</td>
					</tr>
					<tr>
						<th>Status</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "STATUS") %>&nbsp;</td>
						<th>Est. Wafer Gross</th>
					  	<td>
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "ESTIMATED"))){ 
					  						out.print("Yes");
					  					} else if("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "ESTIMATED"))) {
					  						out.print("No");
					  					} else {
					  					    out.print("All");
					  					}%>
						&nbsp;					  	
					  	</td>
					</tr>
					<tr>
						<th>COMPANY</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "RELEASE_TO") %>&nbsp;</td>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/proj_query.do" method="post">
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Name</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Code</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Code</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Nickname</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Fab</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Option</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Panel Type</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Family</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Line</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Process Technology</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Function</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Customer</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Team Member</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Kick-off Date</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Status</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">COMPANY</th>
							</tr>
<%
	int idx = 0;
	for(ProjectTo project : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		String teammember = "";
		teammember += project.getProjLeader() != null ? "<br>Leader: " + project.getProjLeader() : "";
		teammember += project.getPm() != null ? "<br>PM: " + project.getPm() : "";
		teammember += project.getDesignEngr() != null ? "<br>Design Engineer: " + project.getDesignEngr() : "";
		teammember += project.getProdEngr() != null ? "<br>Product Engineer: " + project.getProdEngr() : "";
		teammember += project.getEsdEngr() != null ? "<br>ESD Engineer: " + project.getEsdEngr() : "";
		teammember += project.getAprEngr() != null ? "<br>APR Engineer: " + project.getAprEngr() : "";
		teammember += project.getLayoutEngr() != null ? "<br>Layout Engineer: " + project.getLayoutEngr() : "";
		teammember += project.getTestEngr() != null ? "<br>Test Engineer: " + project.getTestEngr() : "";
		teammember += project.getAssyEngr() != null ? "<br>ASSY Engineer: " + project.getAssyEngr() : "";
		teammember += project.getAppEngr() != null ? "<br>APP Engineer: " + project.getAppEngr() : "";
		teammember += project.getQaEngr() != null ? "<br>QA Engineer: " + project.getQaEngr() : "";
		teammember += project.getSalesEngr() != null ? "<br>Sales Engineer: " + project.getSalesEngr() : "";
		if (teammember.length() > 0) {
			teammember = teammember.substring(4);
		}
		
		String projName = BeanHelper.getHtmlValueByColumn(project, "PROJ_NAME");
		String projCode = BeanHelper.getHtmlValueByColumn(project, "PROJ_CODE");
		
		String fabdesc = fabCodeDao.getFabDescr(BeanHelper.getHtmlValueByColumn(project, "FAB"));
		String fab = BeanHelper.getHtmlValueByColumn(project, "FAB");
		String fabStr = "";
		if (fabdesc != null && fabdesc.length() > 0 && fab.length() > 0) {
		    fabStr = fab + "," + fabdesc;
		}
		
%>
							<tr>
								<td <%=tdcss %>><input type="radio" name="ref" value="<%=BeanHelper.getHtmlValueByColumn(project, "PROJ_CODE") %>"></td>
								<td <%=tdcss %>><a href="<%=cp %>/md/proj_edit.do?m=pre&projCode=<%=projCode%>"><%=BeanHelper.getHtmlValueByColumn(project, "PROJ_NAME") %></a>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROJ_CODE") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROD_CODE_LIST") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "NICK_NAME") %>&nbsp;</td>
								<td <%=tdcss %>><%=fabStr %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROJ_OPTION") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PANEL_TYPE") %>&nbsp;</td>
								<td <%=tdcss %>><%=sapMasterProductFamilyDao.findDescByProdFamily(BeanHelper.getHtmlValueByColumn(project, "PROD_FAMILY"))%>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROD_LINE") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROC_TECH") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FUNC_REMARK") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CUST") %>&nbsp;</td>
								<td <%=tdcss %>><%=teammember %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "KICK_OFF_DATE", "yyyy/MM/dd") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "STATUS") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "RELEASE_TO") %>&nbsp;</td>
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

			<!-- Content end --></div>
			</td>
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
