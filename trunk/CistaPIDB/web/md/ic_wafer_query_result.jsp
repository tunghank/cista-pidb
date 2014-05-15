<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.IcWaferQueryTo" %>
<%@ page import="com.cista.pidb.md.to.IcWaferTo" %>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%@ include file="/common/global.jsp"%>
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
			document.location.href='<%=cp%>/md/ic_wafer_create.do?m=pre&ref=' + getRadioValue('ref');
		}
	}
</script>
</head>
<body>
<% List<IcWaferTo> result = (List<IcWaferTo>)request.getAttribute("result");
	IcWaferQueryTo criteria = (IcWaferQueryTo)request.getAttribute("criteria");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td class="pageTitle">Master Data :: IC Wafer Query Result</td>
			  </tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />
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
					 	 onClick="document.location.href='<%=cp%>/md/ic_wafer_create.jsp'">
					  <input name="button2" type="button" class="button" id="button2" value="Create With Reference"
					  	onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3" value="New Query" 
					  onClick="document.location.href='<%=cp%>/md/ic_wafer_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>
			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Material Number</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "MATERIAL_NUM") %>
					  	&nbsp;</td>
					  	<th width="20%">Project Code</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROJ_CODE") %>
					  	&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Project Code w Version</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROJ_CODE_W_VERSION") %>
					  	&nbsp;</td>
					  	<th width="20%">Wafer Body Version</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "BODY_VER") %>
					  	&nbsp; </td>
					</tr>
					<tr>
						<th width="20%">Wafer Option Version</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "OPTION_VER") %>					  	
					  	&nbsp; </td>
						<th width="20%">Routing (WF)</th>
					  	<td width="30%">
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(criteria, "ROUTING_WF"))){ 
					  						out.print("Yes");
					  					} else if("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(criteria, "ROUTING_WF"))) {
					  						out.print("No");
					  					} else {
					  					    out.print("All");
					  					}%>
						&nbsp;		
						</td>
					</tr>
					<tr>
						<th width="20%">Routing (BP)</th>
					  	<td width="30%">
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(criteria, "ROUTING_BP"))){ 
					  						out.print("Yes");
					  					} else if("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(criteria, "ROUTING_BP"))) {
					  						out.print("No");
					  					} else {
					  					    out.print("All");
					  					}%>
						&nbsp;
						</td>
						<th width="20%">Routing (CP)</th>
					 	<td width="30%">
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(criteria, "ROUTING_CP"))){ 
					  						out.print("Yes");
					  					} else if("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(criteria, "ROUTING_CP"))) {
					  						out.print("No");
					  					} else {
					  					    out.print("All");
					  					}%>
						&nbsp;
						</td>
					</tr>
					<tr>
					 	<th width="20%">Tape Out Date </th>
					  	<td width="30%"><% if (criteria.getTapeOutDateFrom() == null && criteria.getTapeOutDateTo() == null)out.print("All");else {%>
					  						<%= BeanHelper.getQueryCriteriaByColumn(criteria, "TAPE_OUT_DATE_FROM","yyyy/MM/dd HH:mm:ss") %>
					  						~
					  						<%=BeanHelper.getQueryCriteriaByColumn(criteria, "TAPE_OUT_DATE_TO","yyyy/MM/dd HH:mm:ss") %>
					  					<%} %>
					  					
					  	&nbsp;</td>
						<th width="20%">Status</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "STATUS") %>					  	
					  	&nbsp;</td>
					</tr>
					<tr>
					 	<th width="20%">Fab Device ID</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "FAB_DEVICE_ID") %>&nbsp;</td>
						<th width="20%">Revision Item</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "REVISION_ITEM") %>&nbsp;</td>
					</tr>
					<tr>
					 	<th width="20%">Mask House</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "MASK_HOUSE") %>&nbsp;</td>
						<th width="20%">COMPANY</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "RELEASE_TO") %>	</td>
					</tr>
				</tbody>
			</table>
	<%if (result != null && result.size() > 0) { %>
	<form name="pagingForm" action="<%=cp %>/md/ic_wafer_query.do" method="post">
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Material Number </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Code </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Code w Version</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Material Description</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Tape Out Date </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Fab device ID </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Mask Layer Combination </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Revision Item </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Wafer Body Version</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Wafer Option Version</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Routing (WF)</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Routing (BP)</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Routing (CP)</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">MP_Status</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Status</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">BP </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">CP</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">DS</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Mask House</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">COMPANY</th>
							</tr>
<%
	int idx = 0;
	for(IcWaferTo oneIc : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";		
%>
							<tr>
								<td <%=tdcss %>>
								<input type="radio" name="ref" value="<%=oneIc.getMaterialNum() %>">
								</td>
								<td <%=tdcss %>>
								<a href="<%=cp%>/md/ic_wafer_edit.do?m=pre&materialNum=<%=oneIc.getMaterialNum() %>&projCodeWVersion=<%=oneIc.getProjCodeWVersion()%>"><%=oneIc.getMaterialNum()%></a>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE_W_VERSION") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "MATERIAL_DESC") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "TAPE_OUT_DATE","yyyy/MM/dd") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "FAB_DEVICE_ID") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "MASK_LAYER_COM") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "REVISION_ITEM") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "BODY_VER") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "OPTION_VER") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=oneIc.getRoutingWf()?"Yes":"No" %>
								&nbsp;</td>
								<td <%=tdcss %>><%=oneIc.getRoutingBp()?"Yes":"No" %>
								&nbsp;</td>
								<td <%=tdcss %>><%=oneIc.getRoutingCp()?"Yes":"No" %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "MP_STATUS") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "STATUS") %>
								&nbsp;</td>		
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "BP") %>
								&nbsp;</td>	
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "CP") %>
								&nbsp;</td>	
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "DS") %>
								&nbsp;</td>	
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "MASK_HOUSE") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "RELEASE_TO") %>
								&nbsp;</td>
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
