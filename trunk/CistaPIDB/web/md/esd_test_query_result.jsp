<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.dao.EsdTestDao"%>
<%@ page import="com.cista.pidb.md.to.EsdTestQueryTo" %>
<%@ page import="com.cista.pidb.md.to.EsdTestTo" %>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.ProjectTo" %>
<%@ page import="com.cista.pidb.md.dao.ProjectDao" %>
<%@ page import="com.cista.pidb.md.dao.ProjectCodeDao" %>
<%@ page import="com.cista.pidb.md.dao.ProductDao" %>
<%@ page import="com.cista.pidb.md.to.ProjectCodeTo" %>
<%@ page import="com.cista.pidb.md.to.ProductTo" %>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.code.dao.FunctionParameterDao"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<% 	List<EsdTestQueryTo> result = (List)request.getAttribute("result");
	EsdTestQueryTo criteria = (EsdTestQueryTo)request.getAttribute("criteria");
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
			document.location.href='<%=cp%>/md/esd_test_create.do?m=pre&ref=' + getRadioValue('ref');
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
				<td class="pageTitle">Master Data :: ESD Test Query Result </td>
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
					 	 onClick="document.location.href='<%=cp%>/md/esd_test_create.jsp'">
					  <input name="button2" type="button" class="button" id="button2" value="Create With Reference"
					  	onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3" value="New Query" 
					  onClick="document.location.href='<%=cp%>/md/esd_test_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>

			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="20%">Product Code</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROD_CODE") %>&nbsp;</td>
					  <th width="20%">Project Code w Version</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "PROJ_CODE_W_VERSION") %>&nbsp;</td>
					</tr>
					<tr> 
					  <th width="20%">ESD Finish Date</th>
					  <td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(criteria, "ESD_FINISH_DATE","yyyy/MM/dd") %>&nbsp;</td>
					  <th width="20%">Owner</th>
					  <td width="30%">
					  <%
					  String funName = "RA";
					  String funFiledName= "OWNER";
					  String fieldValue = BeanHelper.getQueryCriteriaByColumn(criteria, "OWNER");
					  FunctionParameterDao dao = new FunctionParameterDao();
					  FunctionParameterTo to  = new FunctionParameterTo();
						to = dao.findValueByFiledValue(funName,funFiledName,fieldValue);
						if (to != null){
							//System.out.println(to.getFieldShowName());
							%>
						
					  <%=to.getFieldShowName()%>&nbsp;</td>
					  <%
						  }
					  
					  %>
					</tr>
					</tr>
				</tbody>
			</table>
			
<%if (result != null && result.size() > 0) { %>
<form name="pagingForm" action="<%=cp %>/md/esd_test_query.do" method="post">
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
						class="button" id="downloadBtn" value="download all" onClick="reportDownload('pagingForm')"></div>
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
					<th onclick="sort(this, 1)" style="cursor:pointer">Project Code w 
					Version</th>
					<th onclick="sort(this, 1)" style="cursor:pointer">ID ESD Testing</th>
					<th onclick="sort(this, 1)" style="cursor:pointer">ESD Finish Date</th>
					<th onclick="sort(this, 1)" style="cursor:pointer">Owner</th>
				</tr>
				<%
					int idx = 0;
					String fundName = "RA";
					String funFieldName = "OWNER";
					for(EsdTestQueryTo oneIc : result) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						EsdTestDao paraDao = new EsdTestDao();
						FunctionParameterDao fpDao = new FunctionParameterDao();
						FunctionParameterTo paraTo = fpDao.findValueByFiledValue(fundName,funFieldName,BeanHelper.getHtmlValueByColumn(oneIc, "OWNER"));
				%>
							<tr>
								<td <%=tdcss %>>
								<input type="radio" name="ref" value="<%=oneIc.getProjCodeWVersion() %>">
								</td>
								<td <%=tdcss %>><a href="<%=cp%>/md/esd_test_edit.do?m=pre&projCodeWVersion=<%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE_W_VERSION") %>&idEsdTesting=<%=BeanHelper.getHtmlValueByColumn(oneIc, "ID_ESD_TESTING")%>">									
								<%=BeanHelper.getHtmlValueByColumn(oneIc, "PROD_CODE")%>
									</a>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "PROJ_CODE_W_VERSION") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "ID_ESD_TESTING") %>&nbsp;</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(oneIc, "ESD_FINISH_DATE","yyyy/MM/dd") %></td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(paraTo, "FIELD_SHOW_NAME") %>&nbsp;</td>
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
