<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.FtTestProgramQueryTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
    FtTestProgramQueryTo queryTo = (FtTestProgramQueryTo) request.getAttribute("queryTo");
	List<FtTestProgramQueryTo> result = (List) request.getAttribute("result");
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
			document.location.href='<%=cp%>/md/ft_test_prog_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: FT Test Program Query Result</td>
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
						   
					  <input
						name="button3" type="button" class="button" id="button3"
						value="Create New" onClick="document.location.href='<%=cp %>/md/ft_test_prog_create.do?m=pre'">
					  <input
						name="button3" type="button" class="button" id="button4"
						value="Create With Reference" onClick="createWithRef()">
					           <%
						   }     
						%>
					  
					  <input
						name="button3" type="button" class="button" id="button5"
						value="New Query" onClick="document.location.href='<%=cp %>/md/ft_test_prog_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>
			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="20%">Part Number</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PART_NUM") %>&nbsp;</td>
				      <th width="20%">FT Material Num.</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FT_MATERIAL_NUM") %>&nbsp;</td>
				    </tr>
					<tr>
					  <th width="20%">Product Code</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_CODE") %>&nbsp;</td>
				      <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
				    </tr>
					<tr>
					  <th>FT Test Program Name</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FT_TEST_PROG_NAME") %>&nbsp;</td>
				      <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
				    </tr>
					<tr>
					  <th>FT Test Program Revision</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FT_TEST_PROG_REVISION") %>&nbsp;</td>
				      <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
				    </tr>
				</tbody>
			</table>
			
<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/ft_test_prog_query.do" method="post">
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
	<th onclick="sort(this, 1)" style="cursor:pointer">FT Material Num.</th>
    <th onclick="sort(this, 1)" style="cursor:pointer">Part Number</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Product Name</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">FT Test Program Name</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">FT Test Program Revision</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Tester.</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Multiple Stage</th>
  </tr>
  
  <%
	int idx = 0;
    List<String> prodNameList = (List<String>)request.getAttribute("prodNameList");
	for(FtTestProgramQueryTo ftp : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		String ftMaterialNum = BeanHelper.getHtmlValueByColumn(ftp, "FT_MATERIAL_NUM");
		String partNum = BeanHelper.getHtmlValueByColumn(ftp, "PART_NUM");
		String prodName = prodNameList.get(idx - 1);
		String ftTestProgName = BeanHelper.getHtmlValueByColumn(ftp, "FT_TEST_PROG_NAME");
		String ftTestProgRevision = BeanHelper.getHtmlValueByColumn(ftp, "FT_TEST_PROG_REVISION");

		String tester=BeanHelper.getHtmlValueByColumn(ftp, "TESTER");
		String multipleStage=BeanHelper.getHtmlValueByColumn(ftp, "MULTIPLE_STAGE");
%>				
  
  <tr>
    <td <%=tdcss %>><input type="radio" name="ref" value="<%=ftMaterialNum%>,<%=ftTestProgName %>"></td>
	<td <%=tdcss %>><a href="<%=cp %>/md/ft_test_prog_edit.do?m=pre&ftTestProgName=<%=ftTestProgName %>&ftMaterialNum=<%=ftMaterialNum%>"><%=(ftMaterialNum == null)?"":ftMaterialNum %>&nbsp;</td>
	<td <%=tdcss %>><a href="<%=cp %>/md/ft_test_prog_edit.do?m=pre&ftTestProgName=<%=ftTestProgName %>&ftMaterialNum=<%=ftMaterialNum%>"><%=(partNum == null)?"":partNum %>&nbsp;</td>
	<td <%=tdcss %>><%=(prodName == null)?"":prodName %>&nbsp;</td>
	<td <%=tdcss %>><%=(ftTestProgName == null)?"": ftTestProgName%>&nbsp;</td>
	<td <%=tdcss %>><%=(ftTestProgRevision == null)?"": ftTestProgRevision%>&nbsp;</td>
	<td <%=tdcss %>><%=(tester==null)?"":tester %>&nbsp;</td>
	<td <%=tdcss %>><%=(multipleStage==null)?"":multipleStage %>&nbsp;</td>
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
