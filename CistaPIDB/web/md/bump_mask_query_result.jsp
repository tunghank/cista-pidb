<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.BumpMaskQueryTo"%>
<%@ page import="com.cista.pidb.code.dao.FabCodeDao"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
    BumpMaskQueryTo queryTo = (BumpMaskQueryTo) request.getAttribute("queryTo");
	List<BumpMaskQueryTo> result = (List) request.getAttribute("result");
	String fabDescription="";
	if(queryTo!=null){
		fabDescription = new FabCodeDao().getFabDescr(queryTo.getFab());
	}
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
			document.location.href='<%=cp%>/md/bump_mask_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: Bumping Mask Query Result </td>
  </tr>
</table>
			<div class="content">
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
						   
					  <input
						name="button3" type="button" class="button" id="button3"
						value="Create New" onClick="document.location.href='<%=cp %>/md/bump_mask_create.do?m=pre'">
					  <input
						name="button3" type="button" class="button" id="button4"
						value="Create With Reference" onClick="createWithRef()">
					  	   <%
						   }     
						%>
						

					  <input
						name="button3" type="button" class="button" id="button5"
						value="New Query" onClick="document.location.href='<%=cp %>/md/bump_mask_query.do?m=pre'">

					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%">Project Name</th>
					    <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_NAME") %>&nbsp;					 </td>
				        <th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
				    </tr>

					<tr>
						<th width="20%">Fab/Fab Description</th>
					    <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FAB") %>
					    <%=(fabDescription==null || "".equals(fabDescription))?"":","+fabDescription%>&nbsp;</td>
				        <th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
				    </tr>
					<tr>
					   <th width="20%">Option</th>
					   <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_OPTION") %>&nbsp;					 </td>
					   <th width="20%">&nbsp;</th>
					   <td width="30%">&nbsp;</td>
				    </tr>
					<tr>
					   <th width="20%">Mask Name</th>
					   <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MASK_NAME") %>&nbsp;					 </td>
					   <th width="20%">&nbsp;</th>
					   <td width="30%">&nbsp;</td>
				    </tr>
				</tbody>
			</table>

<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/bump_mask_query.do" method="post">
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
						class="button" id="downloadBtn" value="download all" onClick="reportDownload('pagingForm')"></div>
					<% 
						}
					%>						
					</td>
				</tr>
			</table>
			
<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr><td>
		<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
					<table class="grid" border="0" cellpadding="1" cellspacing="1">
						<tbody>
				<tr>
				    <th width="1%">&nbsp;</th>
					<th onclick="sort(this, 1)" style="cursor:pointer">Project Name </th>
					<th onclick="sort(this, 1)" style="cursor:pointer">Fab/Fab Description</th>
					<th onclick="sort(this, 1)" style="cursor:pointer">Option</th>
					<th onclick="sort(this, 1)" style="cursor:pointer">Mask Name</th>
				</tr>
	 <%
		int idx = 0;
		for(BumpMaskQueryTo bumpMask : result) {
			idx ++;
			String tdcss = "class=\"c" + idx % 2+"\"";
			String maskName=BeanHelper.getHtmlValueByColumn(bumpMask, "MASK_NAME");
			String projName=BeanHelper.getHtmlValueByColumn(bumpMask, "PROJ_NAME");
			String option=BeanHelper.getHtmlValueByColumn(bumpMask, "PROJ_OPTION");
			String fab=BeanHelper.getHtmlValueByColumn(bumpMask, "FAB");
			String fabDescr=BeanHelper.getHtmlValueByColumn(bumpMask, "FAB_DESCR");
			String projCode = BeanHelper.getHtmlValueByColumn(bumpMask, "PROJ_CODE");
	%>
			<tr>
				<td <%=tdcss %>><input type="radio" name="ref" value="<%=maskName %>,<%=projCode %>"></td>
				<td <%=tdcss %>><a href="<%=cp %>/md/bump_mask_edit.do?m=pre&maskName=<%=maskName %>&projCode=<%=projCode %>">
				    <%=(projName==null)? "" : projName %></a>&nbsp;</td>
	            <td <%=tdcss %>><%=(fab==null)? "" : fab %><%=(fabDescr==null || "".equals(fabDescr))? "" : ","+fabDescr %>&nbsp;</td>
	            <td <%=tdcss %>><%=(option==null)? "" : option %>&nbsp;</td>
				<td <%=tdcss %>><%=(maskName==null)? "" : maskName %>&nbsp;</td>
			</tr>
	<%
		}
	%>
				</tbody>
			</table>
			
</div>
</td></tr></table>
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
