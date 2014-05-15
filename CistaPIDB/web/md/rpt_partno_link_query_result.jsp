<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>

<%@ page import="com.cista.pidb.code.dao.SapMasterProductFamilyDao"%>
<%@ page import="com.cista.pidb.code.dao.FabCodeDao"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>



<%@ page import="com.cista.pidb.md.to.RptLinkTo"%>
<%@ page import="com.cista.pidb.md.to.RptLinkQueryTo"%>

<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List"%>

<%
   RptLinkQueryTo queryTo = (RptLinkQueryTo) request.getAttribute("queryTo");
	List<RptLinkTo> result = (List) request.getAttribute("result");
%>
<html>
<head>
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">

<script type="text/javascript">
	function init() {
		autoFitBottomArea('resultPanel', 180);
	}
	window.onload = init;
	
</script>
</head>
<body>

<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
				<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="pageTitle">Master Data :: Part Number Query Result</td>
					</tr>
				</table>
				<div class="content">
				
				<table border="0" cellpadding="0" cellspacing="0" class="segmentHeader" width="100%">
				   <tr>
						<td style="font-size: 12px;"><a href="#" onclick="showHidePanel('queryCriteriaPanel');init()">Query Criteria</a></td>
						<td>
						<div align="right">																		
						<input name="button3" type="button" class="button" id="button5" value="New Query" 
						       onclick="document.location.href='<%=cp%>/md/rpt_link_query.do?m=pre'">					
					   </div>
						</td>
					</tr>
				</table>
				
				
				<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>					
					<tr>
						<th width="20%">Part No</th>
						<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PART_NO") %>&nbsp;</td>
						<th width="20%">Project Code</th>
						<td width="30%"><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "PROJ_CODE") %>&nbsp;</td>						
					</tr>				
					<tr>
						<th>FAB</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "fab") %>&nbsp;</td>						
						<th>Tape Name</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "TAPE_NAME") %>&nbsp;</td>
					</tr>
					<tr>
						<th >CP Tester</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "CP_TESTER") %>&nbsp;</td>
						<th>FT Tester</th>
						<td><%=BeanHelper.getQueryCriteriaByColumn(queryTo, "FT_TESTER") %>&nbsp;</td>
					</tr>					
				</tbody>
				</table>
				
				<%
				if (result != null && result.size() > 0) 
				{
				%>
				<form name="pagingForm" action="<%=cp %>/md/rpt_link_query.do" method="post">
				<%=BeanHelper.generateHtmlHiddenField(queryTo, true) %>
				<table class="formErrorAndButton">
					<tr>
						<td>
						     <%=queryTo!=null?queryTo.getPaging():"" %>&nbsp;
					   </td>
						<td>
							  <% 
							  	boolean downloadable = false;
							   String isGuest="No";
								RoleDao roleDao = new RoleDao();
								UserTo currentUser = PIDBContext.getLoginUser(request);
								List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());
								
								if (checkedRoles != null) 
								{
                            for (RoleTo roleTo : checkedRoles)
                            {
                                  if (roleTo.getRoleName().equalsIgnoreCase("downloadable") )  
                                  {
                                      downloadable = true;
                                  } 
                            }
		                  } 	
								
								if (downloadable) 
								{
							  %>						
								   <div align="right"><input name="downloadBtn" type="button" class="button" 
								          id="downloadBtn" value="download all" onclick="reportDownload('pagingForm')"></div>
						    <%} %>
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
									<th onclick="sort(this, 1)" style="cursor:pointer">PART NUM</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">IC FG MATERIAL NUM </th>
									<th onclick="sort(this, 1)" style="cursor:pointer">Project Code</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">Product Name</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">FUNCTION REMARK</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">Fab</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">Fab Device Id</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">PROJ CODE W VERSION</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">VOLTAGE</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">Poly Metal Layers</th>
									<!-- 11 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">Proc Tech </th>									
									<th onclick="sort(this, 1)" style="cursor:pointer">PROC_LAYER_NO</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">MASK_LAYERS_NO</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">PITCH</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">WAFER_INCH</th>							
									<th onclick="sort(this, 1)" style="cursor:pointer">GROSS_DIE</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">WAFER_THICKNESS</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">X SIZE</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">Y SIZE</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">SCRIBE LINE</th>
									<!-- 21 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">SEALRING</th>									
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HOUSE1</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HOUSE2</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HOUSE3</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HOUSE4</th>							
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HOUSE5</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HEIGHT</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">BUMP HARDNESS</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">TAPE NAME</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">TAPE CUST PROJ NAME</th>									
									<!--31 -->									
									<th onclick="sort(this, 1)" style="cursor:pointer">TAPE VENDOR</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSY SITE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TAPE WIDTH</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">SPROCKET HOLE NUM</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MIN PITCH</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TAPE CUST</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO1</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO VER1</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">COLOR1</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO2</th> 									 
									<!--41 -->	
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO VER2</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">COLOR2</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO3</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO VER3</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">COLOR3</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO4</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAY DRAWING NO VER4</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">COLOR4</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">TRAD PKG TYPE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">PIN COUNT</th> 									
									<!--51 -->	 
									<th onclick="sort(this, 1)" style="cursor:pointer">LEAD FRAME TYPE</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">BODY SIZE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MCP DIE QTY</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MCP PKG</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">SUBTRACT LAYER</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSY HOUSE1</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSY HOUSE2</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSY HOUSE3</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSY HOUSE4</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP TEST PROG NAME</th> 									
									<!--61 -->	 
									<th onclick="sort(this, 1)" style="cursor:pointer">MULTIPLE STAGE</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">CP TEST PROG REVISION</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP TEST PROG RELEASE DATE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP CPU TIME</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP INDEX TIME</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP CONTACT DIE QTY</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP TESTER</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP TESTER CONFIG</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FIRST CP TEST HOUSE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT TEST PROG NAME</th> 									
									<!--71 -->	 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT TEST PROG REVISION</th>
									<th onclick="sort(this, 1)" style="cursor:pointer">FT TEST PROG RELEASE DATE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT CPU TIME</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT INDEX TIME</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT CONTACT DIE QTY</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT TESTER</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">FT TESTER CONFIG</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">PKG CODE</th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">PACKAGE TYPE</th>									  								
									<th onclick="sort(this, 1)" style="cursor:pointer">MP STATUS </th> 
									<!--81 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">MP RELEASE DATE </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE CUST </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE TAPE VENDOR </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE BP VENDOR </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE CP HOUSE </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE ASSY HOUSE </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSIGN TO </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">ASSIGN EMAIL </th> 									
									<th onclick="sort(this, 1)" style="cursor:pointer">CREATED BY </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MODIFIED BY </th> 
									<!--91 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO1 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO VER1 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP COLOR1 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP CUSTOMER NAME1 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO2 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO VER2 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP COLOR2 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP CUSTOMER NAME2 </th> 									
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO3 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO VER3 </th> 
									<!--101 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">MP COLOR3 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP CUSTOMER NAME3 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO4 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP TRAY DRAWING NO VER4 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP COLOR4 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MP CUSTOMER NAME4 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">PROCESS FLOW </th> 									
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT TAPE </th> 									
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT BP </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT CP </th> 
									<!--111 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT AS </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT WF </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">REMARK </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE FT HOUSE </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">REVISION ITEM </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">UPDATE TIME </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CDT </th> 									
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE POLISH VENDOR </th>									 
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT POLISH </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE COLOR FILTER VENDOR </th> 
									<!--121 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT CF </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE WAFER CF VENDOR </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT WAFERCF </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MAT CSP </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">APPROVE CP CSP VENDOR </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">RELEASE TO </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CP BIN </th> 									
									<th onclick="sort(this, 1)" style="cursor:pointer">MCP PROD1 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MCP PROD2 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">MCP PROD3 </th> 
									<!--131 -->
									<th onclick="sort(this, 1)" style="cursor:pointer">MCP PROD4 </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">LF TOOL </th> 
									<th onclick="sort(this, 1)" style="cursor:pointer">CLOSE LF NAME </th> 
									
								</tr>
								<%
									int idx = 0;
									for(RptLinkTo project : result) 
									{
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
										
								%>
								<tr>
									<td <%=tdcss %>> </td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PART_NUM") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "IC_FG_MATERIAL_NUM") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROJ_CODE") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROJ_NAME") %>&nbsp;</td>																		
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FUNC_REMARK") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FAB") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FAB_DEVICE_ID") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROJ_CODE_W_VERSION") %>&nbsp;</td>
									<td <%=tdcss %>> <%=BeanHelper.getHtmlValueByColumn(project, "VOLTAGE")%> &nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "POLY_METAL_LAYERS") %>&nbsp;</td>
									<!-- 11 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROC_TECH") %>&nbsp;</td>									
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROC_LAYER_NO") %>&nbsp;</td>								
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MASK_LAYERS_NO") %> </td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PITCH") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "WAFER_INCH") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "GROSS_DIE") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "WAFER_THICKNESS") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "X_SIZE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "Y_SIZE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "SCRIBE_LINE") %>&nbsp;</td> 
									 
									<!-- 21 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "SEALRING") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HOUSE1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HOUSE2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HOUSE3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HOUSE4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HOUSE5") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HEIGHT") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BUMP_HARDNESS") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TAPE_NAME") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TAPE_CUST_PROJ_NAME") %>&nbsp;</td> 
									
																	
									<!-- 31 -->
									<td <%=tdcss %>> <%=BeanHelper.getHtmlValueByColumn(project, "TAPE_VENDOR") %></td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSY_SITE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TAPE_WIDTH") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "SPROCKET_HOLE_NUM") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MIN_PITCH") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TAPE_CUST") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO_VER1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "COLOR1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO2") %>&nbsp;</td> 
									 
									<!-- 41 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO_VER2") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "COLOR2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO_VER3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "COLOR3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAY_DRAWING_NO_VER4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "COLOR4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "TRAD_PKG_TYPE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PIN_COUNT") %>&nbsp;</td> 
									 
									<!-- 51 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "LEAD_FRAME_TYPE") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "BODY_SIZE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MCP_DIE_QTY") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MCP_PKG") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "SUBTRACT_LAYER") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSY_HOUSE1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSY_HOUSE2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSY_HOUSE3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSY_HOUSE4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_TEST_PROG_NAME") %>&nbsp;</td> 
									 
									<!-- 61 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MULTIPLE_STAGE") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_TEST_PROG_REVISION") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_TEST_PROG_RELEASE_DATE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_CPU_TIME") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_INDEX_TIME") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_CONTACT_DIE_QTY") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_TESTER") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_TESTER_CONFIG") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FIRST_CP_TEST_HOUSE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_TEST_PROG_NAME") %>&nbsp;</td> 
									 
									<!-- 71 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_TEST_PROG_REVISION") %>&nbsp;</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_TEST_PROG_RELEASE_DATE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_CPU_TIME") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_INDEX_TIME") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_CONTACT_DIE_QTY") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_TESTER") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "FT_TESTER_CONFIG") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PKG_CODE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PACKAGE_TYPE") %>&nbsp;</td> 									 																		 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_STATUS") %>&nbsp;</td> 
									<!-- 81 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_RELEASE_DATE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_CUST") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_TAPE_VENDOR") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_BP_VENDOR") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_CP_HOUSE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_ASSY_HOUSE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSIGN_TO") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "ASSIGN_EMAIL") %>&nbsp;</td> 									
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CREATED_BY") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MODIFIED_BY") %>&nbsp;</td> 
									<!-- 91 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO_VER1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_COLOR1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_CUSTOMER_NAME1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO_VER2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_COLOR2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_CUSTOMER_NAME2") %>&nbsp;</td> 									
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO_VER3") %>&nbsp;</td>
									<!-- 101 --> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_COLOR3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_CUSTOMER_NAME3") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_TRAY_DRAWING_NO_VER4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_COLOR4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MP_CUSTOMER_NAME4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "PROCESS_FLOW") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_TAPE") %>&nbsp;</td> 									
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_BP") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_CP") %>&nbsp;</td> 
									<!-- 111 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_AS") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_WF") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "REMARK") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_FT_HOUSE") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "REVISION_ITEM") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "UPDATE_TIME") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CDT") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_POLISH_VENDOR") %>&nbsp;</td> 									
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_POLISH") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_COLOR_FILTER_VENDOR") %>&nbsp;</td> 
									<!-- 121 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_CF") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_WAFER_CF_VENDOR") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_WAFERCF") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MAT_CSP") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "APPROVE_CP_CSP_VENDOR") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "RELEASE_TO") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CP_BIN") %>&nbsp;</td> 									 																	 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MCP_PROD1") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MCP_PROD2") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MCP_PROD3") %>&nbsp;</td> 
									<!-- 131 -->
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "MCP_PROD4") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "LF_TOOL") %>&nbsp;</td> 
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(project, "CLOSE_LF_NAME") %>&nbsp;</td> 
									
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

			   
			   </div> <!-- Content end -->
			</td>
			<td width="5" valign="bottom" background="<%=cp %>/images/shadow-1.gif">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=cp %>/images/bgs.gif">
					<tr>
						<td height="15"><img src="<%=cp %>/images/spacer.gif" width="1" height="1" alt=""></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2">
			    <img height="2" alt="" src="<%=cp %>/images/shadow-2.gif" width="100%" border="0">
			</td>
		</tr>
	</tbody>
</table>
			
			
</body>
</html>