<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.dao.PkgRaDao"%>
<%@ page import="com.cista.pidb.md.to.PkgRaTo" %>
<%@ page import="com.cista.pidb.md.to.PkgRaQueryTo" %>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.dao.ProductDao" %>
<%@ page import="com.cista.pidb.md.dao.ProjectDao" %>
<%@ page import="com.cista.pidb.md.to.ProductTo" %>
<%@ page import="com.cista.pidb.md.to.ProjectTo" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.cista.pidb.admin.to.ParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.code.dao.FunctionParameterDao"%>

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
			document.location.href='<%=cp%>/md/pkg_ra_create.do?m=pre&ref=' + getRadioValue('ref');
		}
	}
</script>
</head>
<body>
<% 	List<PkgRaTo> result = (List<PkgRaTo>)request.getAttribute("result");
	PkgRaQueryTo criteria = (PkgRaQueryTo)request.getAttribute("criteria");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
				<td class="pageTitle">Master Data :: Package RA Query Result</td>
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
					 	 onClick="document.location.href='<%=cp%>/md/pkg_ra_create.do?m=pre'">
					  <input name="button2" type="button" class="button" id="button2" value="Create With Reference"
					  	onClick="createWithRef()">
					  	   <%
						   }     
						%>
					  
					  <input name="button3" type="button" class="button" id="button3" value="New Query" 
					  onClick="document.location.href='<%=cp%>/md/pkg_ra_query.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>
			<table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="20%">Product Name</th>
					  <td width="30%"><%if(criteria.getProdName() != null && !criteria.getProdName().equals("")) { %>									 
									<%=criteria.getProdName() %>
									<%}else { %>
									All
									<%} %>
					  &nbsp;</td>
					  <th width="20%">Package Code</th>
					  <td width="30%"><%if(criteria.getPkgCode() != null && !criteria.getPkgCode().equals("")) { %>									 
									<%=criteria.getPkgCode() %>
									<%}else { %>
									All
									<%} %>
					  &nbsp;</td>
					</tr>
					<tr>
					  <th width="20%">Project Name</th>
					  <td width="30%"><%if(criteria.getProjName() != null && !criteria.getProjName().equals("")) { %>									 
									<%=criteria.getProjName() %>
									<%}else { %>
									All
									<%} %>
					 &nbsp;</td>
					  <th width="20%">Worksheet Number</th>
					  <td width="30%"><%if(criteria.getWorksheetNumber() != null && !criteria.getWorksheetNumber().equals("")) { %>									 
									<%=criteria.getWorksheetNumber() %>
									<%}else { %>
									All
									<%} %>
					 &nbsp;</td>
					</tr>
					<tr>
					  <th width="20%">Part Num</th>
					  <td width="30%"><%if(criteria.getPartNum() != null && !criteria.getPartNum().equals("")) { %>									 
									<%=criteria.getPartNum() %>
									<%}else { %>
									All
									<%} %>
					 &nbsp;</td>
					  <th width="20%">finish period</th>
					  <td width="30%"><%if(criteria.getPkgRaActualStartTime() != null && !criteria.getPkgRaActualStartTime().equals("") && criteria.getPkgRaActualEndTime() != null && !criteria.getPkgRaActualEndTime().equals("")) { %>									 
									<%=criteria.getPkgRaActualStartTime()+"~" + criteria.getPkgRaActualEndTime() %>
									<%}else { %>
									All
									<%} %>&nbsp;</td>
					</tr>
					<tr>
					  <th width="20%">Tape vendor</th>
					  <td width="30%"><%if(criteria.getTapeVendor() != null && !criteria.getTapeVendor().equals("")) { %>									 
									<%=criteria.getTapeVendor() %>
									<%}else { %>
									All
									<%} %>
					 &nbsp;</td>
					  <th width="20%">Assembly Site</th>
					  <td width="30%"><%if(criteria.getAssySite() != null && !criteria.getAssySite().equals("")) { %>									 
						<%=criteria.getAssySite() %>
						<%}else { %>All
						<%} %>
					 &nbsp;</td>
					</tr>
					<tr>
					  <th width="20%">Owner</th>
					  <td width="30%"><%if(criteria.getOwner() != null && !criteria.getOwner().equals("")) { 
					  String funName = "RA";
					  String funFiledName= "OWNER";
					  String fieldValue = criteria.getOwner();
					  FunctionParameterDao dao = new FunctionParameterDao();
					  FunctionParameterTo to  = new FunctionParameterTo();
						to = dao.findValueByFiledValue(funName,funFiledName,fieldValue);
						if (to != null){
							//System.out.println(to.getFieldShowName());
							%>
						<%=to.getFieldShowName()%>&nbsp;</td>				
						<%}
							}else { %>All
						<%} %>&nbsp;</td>
					  <th width="20%">&nbsp;</th>
					  <td width="30%">&nbsp;</td>
					</tr>
				</tbody>
			</table>
<%if (result != null && result.size() > 0) { %>
	<form name="pagingForm" action="<%=cp %>/md/pkg_ra_query.do" method="post">
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
							<th onclick="sort(this, 1)" style="cursor:pointer">Product Name</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Project Name</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Package Code</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Worksheet Number</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Tape Name</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Part Num</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">finish period</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Tape vendor</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Assembly Site</th>
							<th onclick="sort(this, 1)" style="cursor:pointer">Owner</th>
						  </tr>
						  
<%
	int idx = 0;
	for(PkgRaTo oneIc : result) {
		idx ++;
		String tdcss = "class=\"c" + idx % 2+"\"";
		SapMasterVendorDao dao = new SapMasterVendorDao();
		SapMasterVendorTo vendorTo = dao.findByVendorCode(BeanHelper.getHtmlValueByColumn(oneIc, "TAPE_VENDOR"));
		String funName = "RA";
		String funField = "OWNER";
		PkgRaDao paraDao = new PkgRaDao();
		ParameterTo paraTo = paraDao.findByFieldValue(funName,funField,BeanHelper.getHtmlValueByColumn(oneIc, "OWNER"));
		

%>
							<tr>
								<td <%=tdcss %>>
								<input type="radio" name="ref" value="<%=oneIc.getProdName() %>,<%=oneIc.getPkgCode() %>,<%=oneIc.getPkgType()%>,<%=oneIc.getWorksheetNumber()%>">
								</td>
								<td <%=tdcss %>>
								<a href="<%=cp%>/md/pkg_ra_edit.do?m=pre&pdn=<%=oneIc.getProdName() %>&pgc=<%=oneIc.getPkgCode() %>&wsn=<%=oneIc.getWorksheetNumber()%>&pgt=<%=oneIc.getPkgType()%>"><%=oneIc.getProdName() %></a>&nbsp;</td>
								<td <%=tdcss %>>
								<%ProductDao prodDao = new ProductDao();
								 List<ProductTo> prodToList = prodDao.findByProdName(oneIc.getProdName());
								String projResult = "";
								if (prodToList != null && prodToList.size() > 0) {
									ProjectDao projDao = new ProjectDao();
									ProjectTo projTo = new ProjectTo();
									Map<String,Object> map = new HashMap<String, Object>();
									for (int i=0; i < prodToList.size(); i++) {
										if (prodToList.get(i) != null ){
											List<ProjectTo> projToL = projDao.findByProdCodes(prodToList.get(i).getProdCode());
											if (projToL != null && projToL.size() > 0) {
												for (int j=0; j< projToL.size(); j++) {
													map.put(projToL.get(j).getProjName(),projToL.get(j).getProjName());
												}
											}
										}
									}
									for (String k : map.keySet()) {
										if (projResult.length() == 0) {
											projResult = (String)map.get(k);
										} else {
											projResult += " / " + (String)map.get(k);
										}
									}
								}
								%>
								<%=projResult %>&nbsp;</td>
								<td <%=tdcss %>><%=oneIc.getPkgCode() %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getWorksheetNumber() != null) { %>
								<%=oneIc.getWorksheetNumber()%>
								<%} %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getTapeName() != null) { %>
								<%=oneIc.getTapeName()%>
								<%} %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getPartNum() != null) { %>
								<%=oneIc.getPartNum()%>
								<%} %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getPkgRaActualFinishTime() != null) { %><%=BeanHelper.getHtmlValueByColumn(oneIc, "PKG_RA_ACTUAL_FINISH_TIME","yyyy/MM/dd") %>
								<%} %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getTapeVendor() != null) { %>
								<%=vendorTo.getShortName()%>
								<%} %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getAssySite() != null) { %>
								<%=oneIc.getAssySite()%>
								<%} %>&nbsp;</td>
								<td <%=tdcss %>><%if (oneIc.getOwner() != null) { %>
								<%=paraTo.getFieldShowName()%>
								<%} %>&nbsp;</td>
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
