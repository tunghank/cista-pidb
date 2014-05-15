<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.ProductTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<ProductTo> productList = (List)request.getAttribute("productList");
	String condition = request.getParameter("condition");
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
			var selectedProd = new Array();
			var allUser = document.getElementsByName("ProdCheckbox");
			if (allUser && allUser.length > 0) {
				for (var i=0; i<allUser.length; i++) {
					if (allUser[i].checked) {
						selectedProd.push(allUser[i].value);
					}
				}
			}
			if (selectedProd.length > 0) {
				eval("window.opener."+callback+"(selectedProd)");
			}
		}
		window.close();
	}
	
	function newProduct() {
		var newProductCode = $F('prodCode');
		if (newProductCode.length != 8 && newProductCode.length != 10) {
			setMessage("error", "Product code length must 8 or 10.");
			return;
		/*}else if ($F('releaseTo') ==""){
			setMessage("error", "For WP  must Choice.");
			return;*/
		} else {
			var action = $('selectForm').action;
			if (action.indexOf('?') >= 0) {
				$('selectForm').action = action.substring(0, action.indexOf('?')) + "?m=create";
			} else {
				$('selectForm').action = action +  "?m=create";
			}
			
			$('selectForm').submit();
		}
	}
	
	function onSearch() {
			var action = $('selectForm').action;
			if (action.indexOf('?') >= 0) {
				$('selectForm').action = action.substring(0, action.indexOf('?')) + "?m=search";
			} else {
				$('selectForm').action = action +  "?m=search";
			}
			
			$('selectForm').submit();
	}
</script>
</head>
<body>
<form name="selectForm" action="<%=cp %>/dialog/select_product1.do?m=create" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select Product Code</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=productList==null || productList.size()==0?"No product.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th width="120" nowrap><div align="right">New Product Code:</div></th>
					<td class="c0"><input type="text" class="text" name="prodCode"></td>
				</tr>
				<!--<tr>
					<th style="text-align:right"><div class="erp_label"><span class="star">*</span>For WP</div></th>
					<td class="c0">
						<select class="select_w130" name="releaseTo">
							<option value="">--Select--</option>
							<option value="HX">Cista</option>
							<option value="WP" >WP</option>
							<option value="All">All</option>
						</select>
					</td>
				</tr>-->
				<tr>
				    <th style="text-align:right">Remark:</th>
				    <td class="c0"><input type="text" class="text" name="remark"><input type="button" class="button" value="Create" onclick="newProduct()"></td>
				</tr>
				<tr>
					<th style="text-align:right">&nbsp;</th>
					<td class="c0">
					<input type="text" class="text" name="condition" value="<%=condition!=null?condition:"" %>"><input type="button" class="button" value="Search" onclick="onSearch()">
					</td>
				</tr>				
				</tbody>
			</table>
<br>
<%
	if(productList != null && productList.size() > 0) {
%>
<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;height:100px">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>&nbsp;</th>
					<th>Product Code</th>
					<th>Product Name</th>
					<th>Option</th>
					<th>Remark</th>
				</tr>
				<%
					int idx = 0;
					for(ProductTo product : productList) {
						idx ++;
						String tdcss = "class=\"c" + idx % 2+"\"";
						if(product==null){
							break;
						}
						%>
				<tr>
					<td <%=tdcss %> width="20"><input type="checkbox" value="<%=product.getProdCode() %>" name="ProdCheckbox"></td>
			    	<td <%=tdcss %>><a href="<%=cp %>/dialog/product_edit.do?m=pre&prodCode=<%=product.getProdCode() %>"><%=(product.getProdCode()==null)? "" : product.getProdCode() %></a></td>
					<td <%=tdcss %>><%=(product.getProdName()==null)? "" : product.getProdName() %></td>
					<td <%=tdcss %>><%=(product.getProdOption()==null)? "" : product.getProdOption() %></td>
					<td <%=tdcss %>><%=(product.getRemark()==null)? "" : product.getRemark() %></td>
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
