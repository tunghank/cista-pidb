<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.ProjectTo"%>
<%@ page import="com.cista.pidb.md.to.ProductTo"%>
<%@ page import="com.cista.pidb.md.dao.ProductDao"%>
<%
	List<ProjectTo> toList = (List)request.getAttribute("selectList");
    String projCode = (String)request.getAttribute("projCode"); 
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var callback = "<%=request.getAttribute("callback")%>";
	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	window.onload = init;
	function onSelect() {
		if (callback!="") {
			var selectedSmc;
			var allUser = document.getElementsByName("rad");
			if (allUser && allUser.length > 0) {
				for (var i=0; i<allUser.length; i++) {
					if (allUser[i].checked) {
						selectedSmc = allUser[i].value;
					}
				}
			}
			if (selectedSmc) {
				eval("window.opener."+callback+"(selectedSmc)");
			}
		}
		window.close();
	}
	
</script>
</head>
<body>
<form name="selectSmc" action="<%=cp %>/dialog/select_projCode_radio.do?m=list2" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select Project Code</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=toList==null || toList.size()==0?"No result found.":"" %>&nbsp;</div>
					</td>
				</tr>
				<tr>
					<td>
					<input type="text" class="text" name="projCode" id="projCode" value="<%= projCode!=null?projCode:"" %>">
					<input type="submit" class="button" value="Search">
					</td>
				</tr>
			</table>
<%
	if(toList != null && toList.size() > 0) {
%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>&nbsp;</th>
					<th>Project Code</th>
					<th>Product Code</th>
					<th>Product Name</th>
				</tr>
				<%
					int idx = 0;
					for(ProjectTo to : toList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						if(to == null) {
							continue;
						}
						ProductDao prodDao=new ProductDao();
						String projCode2=(to.getProjCode() == null)?"":to.getProjCode();
						String prodCode=(to.getProdCodeList() == null)?"":to.getProdCodeList();
						String prodName="";
						if(!"".equals(prodCode)) {
							String [] s = prodCode.split(",");
							prodCode="";
							for(int i=0;i<s.length;i++) {
								if(s[i]!=null && s[i].length()>0){
									prodCode += ("".equals(prodCode))? s[i] : "/" + s[i];
									ProductTo productTo = prodDao.findByProdCode(s[i]);
									String name = " ";
									if(productTo!=null){
										name= productTo.getProdName() == null? " " : productTo.getProdName();
									}	
									prodName += ("".equals(prodName))? name : "/" + name;
								}
							}
						}			
						String trim=projCode2 +"|"+prodCode+"|"+prodName;	
						
						%>
				<tr>
					<td <%=tdcss %>><input type="radio" value="<%=trim %>" name=rad></td>
					<td <%=tdcss %>><%=projCode2 %></td>
					<td <%=tdcss %>><%=prodCode %></td>
					<td <%=tdcss %>><%=prodName %></td>
				</tr>
						<%
					}
				%>
				</tbody>
			</table></div>
	</td>
</tr>
</table>
<%
	}
%>
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div align="right">
					  <input
						name="okBtn" type="button" class="button" id="okBtn"
						value="OK" onclick="onSelect()">
					  <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="window.close()">
					</div>
					</td>
				</tr>
			</table>
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
				src="<%=cp %>/images/shadow-2.gif" width="585" border="0"></td>
		</tr>
	</tbody>
</table>
<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
