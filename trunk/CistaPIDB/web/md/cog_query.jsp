<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CogTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">

function selectPkgCode() {
	var target = "<%=cp%>/dialog/select_pkg_code.do?m=list&callback=selectPkgCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_pkg_code","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectPkgCodeComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('pkgCode').value = selects.substring(1);
		}
	}
}

function selectProdCode() {
	var target = "<%=cp%>/dialog/select_product2.do?m=pre&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product2","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		var prods = "";
		for(var i=0; i<selectedProds.length; i++) {
			prods += "," + selectedProds[i];
		}
		if (prods.length > 0) {
			$('prodCode').value = prods.substring(1);
		}
	}
}
function selectProdName() {
   
	var target = "<%=cp%>/dialog/select_prod_name.do?m=list&callback=selectProdNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_prod_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdNameComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		var prods = "";
		for(var i=0; i<selectedProds.length; i++) {
			prods += "," + selectedProds[i];
		}
		if (prods.length > 0) {
			$('prodName').value = prods.substring(1);
		}
	}
}

function selectPocketQua() {
   
	var target = "<%=cp%>/dialog/select_cog_pocketQua.do?m=list&callback=selectPocketQuaComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cog_pocketQua","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectPocketQuaComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		var prods = "";
		for(var i=0; i<selectedProds.length; i++) {
			prods += "," + selectedProds[i];
		}
		if (prods.length > 0) {
			$('pocketQty').value = prods.substring(1);
		}
	}
}

function selectTrayDrawingNo() {
   
	var target = "<%=cp%>/dialog/select_cog_trayDrawingNo.do?m=list&callback=selectTrayDrawingNoComplete&trayDrawingNo="+$F('trayDrawingNo');
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cog_trayDrawingNo","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectTrayDrawingNoComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		var prods = "";
		for(var i=0; i<selectedProds.length; i++) {
			prods += "," + selectedProds[i];
		}
		if (prods.length > 0) {
			$('trayDrawingNo').value = prods.substring(1);
		}
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
    <td class="pageTitle">Master Data :: COG Query</td>
  </tr>
</table>
			<div class="content">
			<form id="cogQueryForm" action="<%=cp %>/md/cog_query.do?m=query" method="post">
			 <table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				    <tr>
						<th width="180">Product Name</th>
					  <td><input  type="text" class="text" name="prodName" id="prodName" >
				      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                name:"ProductName",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
				    <tr>
					  <th>Product Code</th>
					  <td>
					  <input type="text" class="text" name="prodCode" id="prodCode" >
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
                                table:"PIDB_COG",
                                keyColumn:"PROD_CODE",
                                title:"Product Code",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th>Package Code</th>
					  <td><input type="text"  class="text"
							name="pkgCode" id="pkgCode">
				      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="pkgCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pkgCodeSSBtn",
                                inputField:"pkgCode",
                                table:"PIDB_COG",
                                keyColumn:"PKG_CODE",
                                title:"Package Code",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th>Tray Size</th>
						<td><select name="traySize" id="traySize" class="select_w130">
						    <option value="">--Select--</option>
							<option value="2''">2''</option>
							<option value="3''">3''</option>
							<option value="4''">4''</option>
						</select></td>
					</tr>
					<tr>
						<th>Tray Drawing No.</th>
					  <td><label>
					  <input type="text" class="text" name="trayDrawingNo" id="trayDrawingNo"  >
                      <img src="<%=cp%>/images/lov.gif" alt="LOV" onclick="selectTrayDrawingNo()"></label></td>
					</tr>
					<tr>
						<th>Tray Drawing No. Ver.</th>
					  <td>
					  
                      <select name="trayDrawingNoVer" id="trayDrawingNoVer" class="select_w130">
							<option value="">--Select--</option>
							<%
								List<String> selectList = (List<String>) request.getAttribute("selectList");
							    for(String cog : selectList) {
							%>
							<option value="<%=cog %>"><%=cog %></option>
							<% 
								}
							%>
						</select>
					  </td>
					</tr>
					</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right"><input name="button1" type="submit"
						class="button" id="button1" value="Search"> <input
						name="button2" type="Reset" class="button" id="button2"
						value="Reset"></div>
					</td>
				</tr>
			</table>
			</form>		
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
