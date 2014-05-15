<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.IcTapeQueryTo"%>
<%@ page import="com.cista.pidb.md.to.IcTapeTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="java.util.List" %>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>

<%
	IcTapeQueryTo queryTo = (IcTapeQueryTo) request.getAttribute("queryTo");
	List<IcTapeQueryTo> result = (List) request.getAttribute("result");

	SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
	SapMasterCustomerTo sapMasterCustomerTo = sapMasterCustomerDao.findByVendorCode(BeanHelper.getQueryCriteriaByColumn(queryTo, "TAPE_CUST"));

	SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
	SapMasterVendorTo sapMasterVendorTo = sapMasterVendorDao.findByVendorCode(BeanHelper.getQueryCriteriaByColumn(queryTo, "TAPE_VENDOR"));
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
			document.location.href='<%=cp%>/md/ic_tape_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: IC-Tape Query Result</td>
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
						value="Create New" onClick="document.location.href='<%=cp%>/md/ic_tape_create.do?m=pre'">
					  <input name="button2" type="button" class="button" id="button2"
						value="Create With Reference" onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3"
						value="New Query" onClick="document.location.href='<%=cp%>/md/ic_tape_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Material Number</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MATERIAL_NUM") %>&nbsp;</td>
					
						<th width="20%">Package Code</th>
					 	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PKG_CODE") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">IC Tape Version</th>
					 	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PKG_VERSION") %>&nbsp;</td>					
						<th width="20%">Tape Name (請加版本)</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TAPE_NAME") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Product Name</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_NAME") %>&nbsp;</td>					
						<th width="20%">Tape Width</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TAPE_WIDTH") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Sprocket Hole No.</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "SPROCKET_HOLE_NUM") %>&nbsp;</td>					
						<th width="20%">Min. Pitch</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MIN_PITCH") %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Tape Customer</th>
					  	<td width="30%"><%=sapMasterCustomerTo!=null?sapMasterCustomerTo.getShortName():"All" %>&nbsp;</td>					
						<th width="20%">Tape Vendor</th>
					  	<td width="30%"><%=sapMasterVendorTo!=null?sapMasterVendorTo.getShortName():"All" %>&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">COMPANY</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "RELEASE_TO") %>&nbsp;</td>					
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
				</tbody>
			</table>

<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/ic_tape_query.do" method="post">
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Package Code </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">IC Tape Version </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Tape Name (請加版本)</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Product Name</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Tape Width</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Sprocket Hole No.</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Min. Pitch</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Tape Cust.</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">AP Model.</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Tape Vendor</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">COMPANY</th>
							</tr>
<%
	int idx = 0;
	for(IcTapeQueryTo icTape : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
%>
							<tr>
								<td <%=tdcss %>><input type="radio" name="ref" value="<%=BeanHelper.getHtmlValueByColumn(icTape, "MATERIAL_NUM")%>,<%=BeanHelper.getHtmlValueByColumn(icTape, "TAPE_NAME")%>"></td>
								<td <%=tdcss %>>
								<a href="<%=cp %>/md/ic_tape_edit.do?m=pre&materialNum=<%=BeanHelper.getHtmlValueByColumn(icTape, "MATERIAL_NUM") %>&tapeName=<%=BeanHelper.getHtmlValueByColumn(icTape, "TAPE_NAME") %>"><%=BeanHelper.getHtmlValueByColumn(icTape, "MATERIAL_NUM") %></a>&nbsp;
								</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "PKG_CODE") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "PKG_VERSION") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "TAPE_NAME") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "PROD_NAME") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "TAPE_WIDTH") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "SPROCKET_HOLE_NUM") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "MIN_PITCH") %>&nbsp;</td>

								<td <%=tdcss %>>
								<%
					    		if ( BeanHelper.getHtmlValueByColumn(icTape, "TAPE_CUST") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(icTape, "TAPE_CUST");
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
					    	%>&nbsp;</td>

								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "TAPE_CUST_PROJ_NAME") %>&nbsp;</td>

								<td <%=tdcss %>>
								<%
					    		if ( BeanHelper.getHtmlValueByColumn(icTape, "TAPE_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(icTape, "TAPE_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao vendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);
						    			    if (to != null) {												
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName()+"," %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>&nbsp;</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(icTape, "RELEASE_TO") %>&nbsp;</td>
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
