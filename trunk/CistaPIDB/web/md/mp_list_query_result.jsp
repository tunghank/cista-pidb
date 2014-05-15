<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.MpListQueryTo"%>
<%@ page import="com.cista.pidb.md.to.MpListTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>

<%
    MpListQueryTo queryTo = (MpListQueryTo) request.getAttribute("queryTo");
	List<MpListTo> result = (List) request.getAttribute("result");
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
			document.location.href='<%=cp%>/md/mp_list_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: MP List Query Result</td>
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
						value="Create New" onClick="document.location.href='<%=cp %>/md/mp_list_create.do?m=pre'">
					  <input
						name="button3" type="button" class="button" id="button4"
						value="Create With Reference" onClick="createWithRef()">
						   <%
						   }     
						%>
						
					  <input
						name="button3" type="button" class="button" id="button5"
						value="New Query" onClick="document.location.href='<%=cp %>/md/mp_list_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>
			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="20%">Part Number</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PART_NUM") %>&nbsp;</td>
					  <th width="20%">MP Release Date</th>
					  <td width="30%">
					  <% if (queryTo.getMpReleaseDateFrom() == null && queryTo.getMpReleaseDateTo() == null)out.print("All");else {%>
					  						<%= BeanHelper.getQueryCriteriaByColumn(queryTo, "MP_RELEASE_DATE_FROM") %>
					  						~
					  						<%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MP_RELEASE_DATE_TO") %>
					  					<%} %>&nbsp;</td>
					</tr>
					<tr>
					  <th>Product Code</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_CODE") %>&nbsp;</td>
					  <th>FG Material</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "MATERIAL_NUM") %>&nbsp;</td>
					</tr>
					<tr>
					  <th>Project Code</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_CODE") %>&nbsp;</td>
					  <th>Revision Item</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "REVISION_ITEM") %>&nbsp;</td>
					</tr>
					<tr>
					  <th>Project  Code w Version</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_CODE_W_VERSION") %>&nbsp;</td>
					  <th>Approve Customer</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "APPROVE_CUST") %>&nbsp;</td>
					</tr>
					<tr>
					  <th width="20%">Package Code(for COG and TRD PKG)</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PKG_CODE") %>&nbsp;</td>
					  <th width="20%">COMPANY</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "RELEASE_TO") %>	</td>
					</tr>
					<tr>
					  <th>Tape Name</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TAPE_NAME") %>&nbsp;</td>
					  <th>&nbsp;</th>
					  <td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
			
<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/mp_list_query.do" method="post">
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
    <th onclick="sort(this, 1)" style="cursor:pointer">Part Number</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">FG Material</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Product Code</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Project Code</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Project Code w Version</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Package Code(for COG and TRD PKG)</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Tape Name</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">MP Status</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">MP Release Date</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Revision Item</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">Approve Customer</th>
	<th onclick="sort(this, 1)" style="cursor:pointer">COMPANY</th>
  </tr>
  
  <%
	int idx = 0;
	for(MpListTo mpList : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		String partNum=BeanHelper.getHtmlValueByColumn(mpList, "PART_NUM");
		String prodCode=BeanHelper.getHtmlValueByColumn(mpList, "PROD_CODE");
		String projCode=BeanHelper.getHtmlValueByColumn(mpList, "PROJ_CODE");
		String projCodeWVersion=BeanHelper.getHtmlValueByColumn(mpList, "PROJ_CODE_W_VERSION");
		String pkgCode=BeanHelper.getHtmlValueByColumn(mpList, "PKG_CODE");
		String tapeName=BeanHelper.getHtmlValueByColumn(mpList, "TAPE_NAME");
		String revisionItem=BeanHelper.getHtmlValueByColumn(mpList, "REVISION_ITEM");
		String mpStatus=BeanHelper.getHtmlValueByColumn(mpList, "MP_STATUS");
		String approveCust=BeanHelper.getHtmlValueByColumn(mpList, "APPROVE_CUST");
		String company=BeanHelper.getHtmlValueByColumn(mpList, "RELEASE_TO");

		mpStatus =null!=mpStatus?mpStatus:"";
		if ( mpStatus.equals("0") ){
			mpStatus = "InActive";
		}else if (mpStatus.equals("1")){
			mpStatus = "Active";
		}else{
			mpStatus = "";
		}

		String icFgMaterialNum = mpList.getIcFgMaterialNum();
%>
  
  <tr>
  <td <%=tdcss %>><input type="radio" name="ref" value="<%=partNum%>,<%=icFgMaterialNum %>,<%=projCodeWVersion %>,<%=tapeName%>,<%=pkgCode%>"></td>
    <td <%=tdcss %>><a href="<%=cp %>/md/mp_list_edit.do?m=pre&partNum=<%=partNum%>&icFgMaterialNum=<%=icFgMaterialNum %>&projCodeWVersion=<%=projCodeWVersion%>&tapeName=<%=tapeName%>&pkgCode=<%=pkgCode %>"><%=partNum %></a></td>
	<td <%=tdcss %>><%=icFgMaterialNum==null?"":icFgMaterialNum %>&nbsp;</td>
	<td <%=tdcss %>><%=prodCode==null?"":prodCode %>&nbsp;</td>
	<td <%=tdcss %>><%=projCode==null?"":projCode %>&nbsp;</td>
	<td <%=tdcss %>><%=projCodeWVersion==null?"":projCodeWVersion %>&nbsp;</td>
	<td <%=tdcss %>><%=pkgCode==null?"":pkgCode %>&nbsp;</td>
	<td <%=tdcss %>><%=tapeName==null?"":tapeName %>&nbsp;</td>
	<td <%=tdcss %>><%=mpStatus==null?"":mpStatus %>&nbsp;</td>
	<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(mpList, "MP_RELEASE_DATE", "yyyy/MM/dd") %>&nbsp;</td>
	<td <%=tdcss %>><%=revisionItem==null?"":revisionItem %>&nbsp;</td>
	<td <%=tdcss %>>
			<%
			if ( BeanHelper.getHtmlValueByColumn(mpList, "APPROVE_CUST") != null) {
				String listStr = BeanHelper.getHtmlValueByColumn(mpList, "APPROVE_CUST");
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
	<td <%=tdcss %>><%=company==null?"":company %>&nbsp;</td>
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
