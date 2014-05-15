<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.MaskLayerMappingQueryTo"%>
<%@ page import="com.cista.pidb.md.to.MaskLayerMappingTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="java.util.List" %>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	MaskLayerMappingQueryTo queryTo = (MaskLayerMappingQueryTo) request.getAttribute("queryTo");
	List<MaskLayerMappingTo> result = (List) request.getAttribute("result");
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
			document.location.href='<%=cp%>/md/mask_layer_map_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: Mask Layer Mapping Query</td>
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
						value="Create New" onClick="document.location.href='<%=cp%>/md/mask_layer_map_create.do?m=pre'">
					  <input name="button2" type="button" class="button" id="button2"
						value="Create With Reference" onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3"
						value="New Query" onClick="document.location.href='<%=cp%>/md/mask_layer_map_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Mask No.</th>
					  	<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MASK_NUM") %>&nbsp;</td>
					
						<th width="20%">&nbsp;</th>
					 	<td width="30%">&nbsp;</td>
					</tr>
					
				</tbody>
			</table>

<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/mask_layer_map_query.do" method="post">
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
								<th onclick="sort(this, 1)" style="cursor:pointer">Mask No. </th>
								<th onclick="sort(this, 1)" style="cursor:pointer">Mask Layer </th>
							</tr>
<%
	int idx = 0;
	for(MaskLayerMappingTo res : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
%>
							<tr>
								<td <%=tdcss %>><input type="radio" name="ref" value="<%=BeanHelper.getHtmlValueByColumn(res, "MASK_NUM") %>"></td>
								<td <%=tdcss %>>
								<a href="<%=cp %>/md/mask_layer_map_edit.do?m=pre&maskNum=<%=BeanHelper.getHtmlValueByColumn(res, "MASK_NUM") %>"><%=BeanHelper.getHtmlValueByColumn(res, "MASK_NUM") %></a>&nbsp;
								</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(res, "MASK_LAYER") %>&nbsp;</td>
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
