<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.RwQueryTo"%>
<%@ page import="com.cista.pidb.md.to.RwTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.dao.ProductDao"%>
<%@ page import="com.cista.pidb.md.to.ProductTo"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
    RwQueryTo queryTo = (RwQueryTo) request.getAttribute("queryTo");
	List<RwTo> result = (List) request.getAttribute("result");
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
			document.location.href='<%=cp%>/md/rw_create.do?m=pre&ref=' + getRadioValue('ref');
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
    <td class="pageTitle">Master Data :: RW Query Result</td>
  </tr>
</table>
			<div class="content">
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
						value="Create New" onClick="document.location.href='<%=cp %>/md/rw_create.do?m=pre'">
					  <input
						name="button3" type="button" class="button" id="button4"
						value="Create With Reference" onclick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input
						name="button3" type="button" class="button" id="button5"
						value="New Query" onClick="document.location.href='<%=cp %>/md/rw_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>
			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="20%">Product Name</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_NAME") %>&nbsp;</td>
					  <th width="20%">Tray Drawing No. Ver.</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TRAY_DRAWING_NO_VER") %>&nbsp;</td>
					</tr>
					<tr>
					  <th width="20%">Product Code</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROD_CODE") %>&nbsp;</td>
					  <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
					</tr>
					<tr>					  
					  <th width="20%">Package Code</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PKG_CODE") %>&nbsp;</td>
					  <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
					</tr>
					<tr>
					  <th>Tray Size</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TRAY_SIZE") %>&nbsp;</td>
					  <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
					</tr>
					<tr>  
					  <th>Tray Drawing No.</th>
					  <td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TRAY_DRAWING_NO") %>&nbsp;</td>
					  <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
					</tr>
				</tbody>
			</table>
<%
	if (result != null && result.size() > 0) {
%>
<form name="pagingForm" action="<%=cp %>/md/rw_query.do" method="post">
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
						class="button" id="downloadBtn"  value="download all" onclick="reportDownload('pagingForm')"></div>
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
    <th>Product Name</th>
    <th>Product Code</th>    	
	<th>Package Code</th>
	<th>Tray Size</th>
	<th>Tray Drawing No(1).</th>
	<th>Tray Drawing No. Ver(1).</th>
	<th>Tray Drawing No(2).</th>
	<th>Tray Drawing No. Ver(2).</th>
	<th>Tray Drawing No(3).</th>
	<th>Tray Drawing No. Ver(3).</th>
	<th>Tray Drawing No(4).</th>
	<th>Tray Drawing No. Ver(4).</th>
	<th>Tray Drawing No(5).</th>
	<th>Tray Drawing No. Ver(5).</th>
	<th>Tray Drawing No(6).</th>
	<th>Tray Drawing No. Ver(6).</th>
  </tr>
   <%
	int idx = 0;
	for(RwTo rw : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		String prodCode=BeanHelper.getHtmlValueByColumn(rw, "PROD_CODE");
		String prodName="";
		ProductDao prodDao=new ProductDao();
		ProductTo prodTo=prodDao.findByProdCode(prodCode);
		if(prodTo != null){
			prodName=prodTo.getProdName();
		}
		
%>
  <tr>
  <td <%=tdcss %>><input type="radio" name="ref" value="<%=prodCode%>,<%=BeanHelper.getHtmlValueByColumn(rw, "PKG_CODE")%>"></td>
    <td <%=tdcss %>><a href="<%=cp %>/md/rw_edit.do?m=pre&prodCode=<%=BeanHelper.getHtmlValueByColumn(rw, "PROD_CODE") %>&pkgCode=<%=BeanHelper.getHtmlValueByColumn(rw, "PKG_CODE")%>"><%=(prodName==null)?"":prodName %></a></td>
    <td <%=tdcss %>><%=(prodCode==null)?"":prodCode %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "PKG_CODE")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "PKG_CODE") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_SIZE")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_SIZE") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO1")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO1") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER1")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER1") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO2")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO2") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER2")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER2") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO3")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO3") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER3")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER3") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO4")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO4") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER4")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER4") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO5")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO5") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER5")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER5") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO6")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO6") %>&nbsp;</td>
	<td <%=tdcss %>><%=(BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER6")==null)?"":BeanHelper.getHtmlValueByColumn(rw, "TRAY_DRAWING_NO_VER6") %>&nbsp;</td>
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
			</table>			</td>
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
