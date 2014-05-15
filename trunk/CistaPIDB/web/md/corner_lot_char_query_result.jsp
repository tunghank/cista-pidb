<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.CornerLotCharQueryTo" %>
<%@ page import="com.cista.pidb.md.to.CornerLotCharTo" %>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.ProjectTo" %>
<%@ page import="com.cista.pidb.md.dao.ProjectDao" %>
<%@ page import="com.cista.pidb.md.dao.ProjectCodeDao" %>
<%@ page import="com.cista.pidb.md.to.ProjectCodeTo" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.cista.pidb.code.dao.FabCodeDao" %>
<%@ page import="com.cista.pidb.md.dao.IcWaferDao" %>
<%@ page import="com.cista.pidb.code.to.FabCodeTo" %>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>


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
			document.location.href='<%=cp%>/md/corner_lot_char_create.do?m=pre&ref=' + getRadioValue('ref');
		}
	}
</script>
</head>
<body>
<% 	List<CornerLotCharQueryTo> result = (List<CornerLotCharQueryTo>)request.getAttribute("result");
	CornerLotCharQueryTo criteria = (CornerLotCharQueryTo)request.getAttribute("criteria");
	FabCodeDao fabCodeDao = new FabCodeDao();
%>
<span class="segmentHeader"></span>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: Corner Lot Characterization Query Results </td>
  </tr>
</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg">
					<%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
					&nbsp;</div>
					</td>
				</tr>
			</table>
			<table border="0" cellpadding="0" cellspacing="0" class="segmentHeader" width="100%">
				<tr>
					<td style="font-size: 12px;">
					<a href="#" onClick="showHidePanel('queryCriteriaPanel');init()">Query Criteria</a>
					</td>
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
					  
					  <input name="button1" type="button" class="button" id="button1"value="Create New" 
					 	 onClick="document.location.href='<%=cp%>/md/corner_lot_char_create.do?m=pre'">
					  <input name="button2" type="button" class="button" id="button2" value="Create With Reference"
					  	onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3" value="New Query" 
					  onClick="document.location.href='<%=cp%>/md/corner_lot_char_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Product Code</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROD_CODE") %></td>
					  <th width="20%">Project Name</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROJ_NAME") %></td>
					</tr>
					<tr>
						<th width="20%">Fab/Fab Description</th>
						<% 
							String fabTemp = BeanHelper.getQueryCriteriaByColumn(criteria, "FAB") ;
							String realFabTemp = "";
							if (fabTemp != null && fabTemp.length() > 0 && !fabTemp.equalsIgnoreCase("All")) {
							    realFabTemp = fabTemp + "," + fabCodeDao.getFabDescr(fabTemp);
							} else {
							    realFabTemp = "All";
							}
						%>
					  	<td width="30%"><%=realFabTemp %>&nbsp;</td>	
					  <th width="20%">Option</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROJ_OPTION") %></td>
					</tr>
					<tr>
					 <th width="20%">Project Code w Version</th>
					  <td colspan="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROJ_CODE_W_VERSION") %></td>
					</tr>
				</tbody>
			</table>
	<%if (result != null && result.size() > 0) { %>
	<form name="pagingForm" action="<%=cp %>/md/corner_lot_char_query.do" method="post">
	<%=BeanHelper.generateHtmlHiddenField(criteria, true) %>
			<table class="formErrorAndButton">
				<tr>
					<td><%=criteria!=null?criteria.getPaging():"" %>
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Code</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Name</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Fab/Fab Description</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Option</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Code w Version</th>
							</tr>
<%
	int idx = 0;
	for(CornerLotCharQueryTo oneIc : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";		
%>
							<tr>
								<td <%=tdcss %>><input type="radio" name="ref" value="<%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE_W_VERSION") %>"></td>
								<td <%=tdcss %>>
								<a href="<%=cp%>/md/corner_lot_char_edit.do?m=pre&wversion=<%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE_W_VERSION") %>"><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROD_CODE") %></a>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_NAME") %>&nbsp;</td>
								<% 
									String fab = BeanHelper.getHtmlValueByColumn(oneIc, "FAB");
									String realFab = "";
									if (fab != null && fab.length() > 0) {
									    realFab = fab + "," + fabCodeDao.getFabDescr(fab);
									}
								%>
								<td <%=tdcss %>><%=realFab%>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_OPTION") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE_W_VERSION") %>&nbsp;</td>
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
