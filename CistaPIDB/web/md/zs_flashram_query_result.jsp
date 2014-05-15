<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.ZsFlashramQueryTo" %>
<%@ page import="com.cista.pidb.md.to.ZsFlashramTo" %>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>


<% 
	List<ZsFlashramTo> result = (List<ZsFlashramTo>)request.getAttribute("result");
	ZsFlashramQueryTo criteria = (ZsFlashramQueryTo)request.getAttribute("criteria");
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
			document.location.href='<%=cp%>/md/zs_flashram_create.do?m=pre&ref=' + getRadioValue('ref');
		}
	}
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
				<td class="pageTitle">Master Data :: FLASHRAM Query Result</td>
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
					 	 onClick="document.location.href='<%=cp%>/md/zs_flashram_create.jsp'">
					  <input name="button2" type="button" class="button" id="button2" value="Create With Reference"
					  	onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3" value="New Query" 
					  onClick="document.location.href='<%=cp%>/md/zs_flashram_query.do?m=pre'">
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
					  	<th width="20%">Memory Size</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "MEMORY_SIZE") %>
					  	&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Speed</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "SPEED") %>
					  	&nbsp; </td>
					  	<th width="20%">Operation Voltage</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "OPERATION_VOLTAGE") %>					  	
					  	&nbsp; </td>
					</tr>
					<tr>
						<th width="20%">Vendor</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "VENDOR_CODE") %>	</td>
						<th width="20%">APPLICATION PRODUCT</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "APPLICATION_PRODUCT") %>
						</td>
					</tr>
					<tr>
						<th width="20%">COMPANY</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "RELEASE_TO") %>	</td>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
				</tbody>
			</table>
	<%if (result != null && result.size() > 0) { %>
	<form name="pagingForm" action="<%=cp %>/md/zs_flashram_query.do" method="post">
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Description </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Memory_Size</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Speed</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Operation Voltage</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Vendor Code</th>	
								<th onclick="sort(this, 1)" style="cursor:pointer">CREATED_BY</th>	
								<th onclick="sort(this, 1)" style="cursor:pointer">MODIFIED_BY</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">APPLICATION PRODUCT</th>
								<th onclick="sort(this, 1)" style="cursor:pointer">COMPANY</th>
							</tr>
<%
	int idx = 0;
	for(ZsFlashramTo flashram : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";		
%>
							<tr>
								<td <%=tdcss %>>
								<input type="radio" name="ref" value="<%=flashram.getMaterialNum() %>">
								</td>
								<td <%=tdcss %>>
								<a href="<%=cp%>/md/zs_flashram_edit.do?m=pre&materialNum=<%=flashram.getMaterialNum() %>"><%=flashram.getMaterialNum()%></a>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "DESCRIPTION") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "MEMORY_SIZE") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "SPEED") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "OPERATION_VOLTAGE") %>
								&nbsp;</td>
								<td <%=tdcss %>>
								<%
					    		if ( BeanHelper.getHtmlValueByColumn(flashram, "VENDOR_CODE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(flashram, "VENDOR_CODE");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {												
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName()+"," %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	
							</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "CREATED_BY") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "MODIFIED_BY") %>
								&nbsp;</td>								
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "APPLICATION_PRODUCT") %>
								&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(flashram, "RELEASE_TO") %>
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
