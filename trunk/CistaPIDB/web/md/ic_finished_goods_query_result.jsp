<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.IcFgQueryTo"%>
<%@ page import="com.cista.pidb.md.to.IcFgTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.FabCodeDao"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>

<%@ page import="java.util.List" %>

<%
	IcFgQueryTo queryTo = (IcFgQueryTo) request.getAttribute("queryTo");
	List<IcFgQueryTo> result = (List) request.getAttribute("result");
	FabCodeDao fabCodeDao = new FabCodeDao();

	/*SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
	SapMasterCustomerTo sapMasterCustomerTo = sapMasterCustomerDao.findByVendorCode(BeanHelper.getQueryCriteriaByColumn(queryTo, "CUST"));*/
	
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
			document.location.href='<%=cp%>/md/ic_fg_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: IC Finished Goods Query Result</td>
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
						value="Create New" onClick="document.location.href='<%=cp%>/md/ic_fg_create.do?m=pre'">
					  <input name="button2" type="button" class="button" id="button2"
						value="Create With Reference" onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3"
						value="New Query" onClick="document.location.href='<%=cp%>/md/ic_fg_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div>Material Number </div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MATERIAL_NUM") %>&nbsp;</td>
					
						<th width="20%"><div>Package Code</div></th>
					 	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PKG_CODE") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><div>Part Number </div></th>
					 	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PART_NUM") %>&nbsp;</td>					
						<th width="20%"><div>Product Code</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_CODE") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><div>Fab</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FAB") %>&nbsp;</td>					
						<th width="20%"> <div>Option</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_OPTION") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><div>Project Code</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_CODE") %>&nbsp;</td>					
						<th width="20%"><div>Routing (FG)</div></th>
					  	<td width="30%">
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "ROUTING_FG"))){ 
					  						out.print("Yes");
					  					} else if("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "ROUTING_FG"))) {
					  						out.print("No");
					  					} else {
					  					    out.print("All");
					  					}%>
						&nbsp;					  	
					  	</td>
					</tr>
					<tr>
						<th width="20%"><div>Routng (AS)</div></th>
					  	<td>
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "ROUTING_AS"))){
					  						out.print("Yes");
					  					} else if ("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "ROUTING_AS"))) {
					  						out.print("No");
					  					} else {
					  					    out.print("All");
					  					}%>
						&nbsp;							  	
						</td>					
						<th width="20%"><div>MP Status</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MP_STATUS") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><div>Customer</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "CUST")%>&nbsp;</td>					
						<th width="20%"><div>AP Model</div></th>
					  	<td width="20%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "AP_MODEL") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"> <div>MCP Package</div></th>
					  	<td width="30%">
						<%if ("1".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "MCP_PKG"))){
					  						out.print("Yes");
					  					} else if ("0".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "MCP_PKG"))) {
					  						out.print("No");
					  					} else if ("N/A".equalsIgnoreCase(BeanHelper.getHtmlValueByColumn(queryTo, "MCP_PKG"))) {
					  					    out.print("N/A");
					  					} else {
					  					  out.print("All");
					  					}%>&nbsp;
					  	</td>					
						<th width="20%"> <div>Status</div></th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "STATUS") %>&nbsp;</td>
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
<form name="pagingForm" action="<%=cp %>/md/ic_fg_query.do" method="post">
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Material Number </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Package Code</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Package Type</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Part Number </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Code</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Fab</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Option </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Project Code</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Routing (FG)</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Routng (AS)</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">MP Status </th>					
								<th onclick="sort(this, 1)" style="cursor:pointer">Custome</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">AP Model</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">MCP Package</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Status </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">COMPANY </th>
							</tr>
<%
	int idx = 0;
	for(IcFgQueryTo icFg : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		
%>
							<tr>
								<td <%=tdcss %>><input type="radio" name="ref" value="<%=BeanHelper.getHtmlValueByColumn(icFg, "MATERIAL_NUM") %>,<%=BeanHelper.getHtmlValueByColumn(icFg, "FAB") %>"></td>
								<td <%=tdcss %>>
								<a href="<%=cp %>/md/ic_fg_edit.do?m=pre&materialNum=<%=BeanHelper.getHtmlValueByColumn(icFg, "MATERIAL_NUM") %>&fab=<%=BeanHelper.getHtmlValueByColumn(icFg, "FAB") %>"><%=BeanHelper.getHtmlValueByColumn(icFg, "MATERIAL_NUM") %></a>&nbsp;
								</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "PKG_CODE") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "PKG_TYPE") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "PART_NUM") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "PROD_CODE") %>&nbsp;</td>
								<% 
									String fab = BeanHelper.getHtmlValueByColumn(icFg, "FAB");
									String realFab = "";
									if (fab != null && fab.length() > 0) {
									    realFab = fab + "," + fabCodeDao.getFabDescr(fab);
									}
								%>
								<td <%=tdcss %>><%=realFab%>&nbsp;</td>		
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "PROJ_OPTION") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "PROJ_CODE") %>&nbsp;</td>
								<td <%=tdcss %>><%="1".equals(icFg.getRoutingFg())?"Yes":"No" %>&nbsp;</td>
								<td <%=tdcss %>><%="1".equals(icFg.getRoutingAs())?"Yes":"No" %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "MP_STATUS") %>&nbsp;</td>
								<td <%=tdcss %>>
								<%
					    		if ( BeanHelper.getHtmlValueByColumn(icFg, "CUST") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(icFg, "CUST");
					    			String[] list = listStr.split(",");
					    			SapMasterCustomerDao customerDao = new SapMasterCustomerDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterCustomerTo to = customerDao.findByVendorCode(s);
						    			    if (to != null) {												
						    			%>
						    			<option value="<%=to.getCustomerCode() %>"><%=to.getShortName()+"," %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	
							</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "AP_MODEL") %>&nbsp;</td>
								<td <%=tdcss %>><%="1".equals(icFg.getMcpPkg())?"Yes":"" %><%="0".equals(icFg.getMcpPkg())?"No":"" %>
								<%="N/A".equals(icFg.getMcpPkg())?"N/A":"" %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "STATUS") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icFg, "RELEASE_TO") %>&nbsp;</td>
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
