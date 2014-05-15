<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.dao.FabCodeDao"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>


<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>
<script>
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

function selectPartNum() {
	var target = "<%=cp%>/dialog/select_icFgPartNum.do?m=list&callback=selectPartNumComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icFgPartNum","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectPartNumComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('partNum').value = selects.substring(1);
		}
	}  	
}

function selectPkgCode() {
	var target = "<%=cp%>/dialog/select_icFgPkgCode.do?m=list&callback=selectPkgCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icFgPkgCode","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
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

function selectProjCode() {
	var target = "<%=cp%>/dialog/select_proj_code.do?m=pre&callback=selectProjCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_proj_code","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('projCode').value = selects.substring(1);
		}
	}
}

function selectMaterialNum() {
	var target = "<%=cp%>/dialog/select_ic_fg_material_num.do?m=list&callback=selectMaterialNumComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_ic_fg_material_num","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectMaterialNumComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('materialNum').value = selects.substring(1);
		}
	}
}

function selectCust() {
	var target = "<%=cp%>/dialog/select_ic_fg_cust.do?m=list&callback=selectCustComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_ic_fg_cust","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectCustComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('cust').value = selects.substring(1);
		}
	}
}

function selectApModel() {
	var target = "<%=cp%>/dialog/select_ic_fg_apModel.do?m=list&callback=selectApModelComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_ic_fg_apModel","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectApModelComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('apModel').value = selects.substring(1);
		}
	}
}
</script>
<form id="masterDataQueryForm"  method="post" action="<%=cp %>/md/ic_fg_query.do?m=query">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: IC Finished Goods Query</td>
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
			
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="180"><div>Material Number </div></th>
					  	<td><input class="text" type="text" id="materialNum" name="materialNum" width="150px">
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="materialNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"materialNumSSBtn",
                                inputField:"materialNum",
                                table:"PIDB_IC_FG",
                                keyColumn:"MATERIAL_NUM",
                                columns:"MATERIAL_NUM",
                                title:"Material Number",
                                mode:1
							});
							</script>	  	
						</td>
					</tr>
					<tr>
						<th width="180"><div>Package Code&#13;</div></th>
					  	<td><input class="text" type="text" id="pkgCode" name="pkgCode" width="150px">
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="pkgCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pkgCodeSSBtn",
                                inputField:"pkgCode",
                                table:"PIDB_IC_FG",
                                keyColumn:"PKG_CODE",
                                columns:"PKG_CODE",
                                title:"Package Code",
                                mode:1
							});
							</script>	  					  	
						</td>
					</tr>
					<tr>
						<th width="180"><div>Part Number </div></th>
					  	<td><input class="text_200" type="text" id="partNum" name="partNum" width="150px">
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="partNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"partNumSSBtn",
                                inputField:"partNum",
                                table:"PIDB_IC_FG",
                                keyColumn:"PART_NUM",
                                columns:"PART_NUM",
                                title:"Part Number",
                                mode:1
							});
							</script>
							</td>
					</tr>
					<tr>
						<th width="180"><div>Product Code </div></th>
					 	 <td><input class="text" type="text" id="prodCode" name="prodCode" width="150px">
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
                                table:"PIDB_IC_FG",
                                keyColumn:"PROD_CODE",
                                columns:"PROD_CODE",
                                title:"Product Code",
                                mode:1
							});
							</script>					 	 
							</td>
					</tr>
					<tr>
						<th width="180"><div>Fab</div></th>
					  	<td><label> <select style="width:130px" name="fab"
							id="fab">
							<option value="">--Select--</option>
						  	<%
						  		List<FabCodeTo> fabList = (List<FabCodeTo>) request.getAttribute("fabList");
						  		if (fabList != null) {
							  		for (FabCodeTo to : fabList) {
							  		  	out.print("<option value=\""+to.getFab()+"\">"+to.getFab()+","+to.getFabDescr()+"</option>");
							  		}
						  		}
						  	%>				
						</select></label></td>
					</tr>
					<tr>
						<th> <div>Option</div></th>
					  	<td><select id="projOption" name="projOption" style="width:130px">
					  	<option value="">--Select--</option>
					  	<%
					  		List<String> optionList = (List<String>) request.getAttribute("optionList");
					  		for (int i = 0; i < optionList.size(); i ++) {
					  		    out.print("<option value=\""+optionList.get(i)+"\">"+optionList.get(i)+"</option>");
					  		}
					  	%>
					  	</select></td>
					</tr>
					<tr>
						<th width="180"><div>Project Code </div></th>
					  <td><input class="text" type="text" id="projCode" name="projCode" width="150px"> 
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeSSBtn",
                                inputField:"projCode",
                                table:"PIDB_IC_FG",
                                keyColumn:"PROJ_CODE",
                                columns:"PROJ_CODE",
                                title:"Project Code",
                                mode:1
							});
							</script>
					  </td>
					</tr>
					<tr>
						<th><div>Routing (FG)</div></th>
					 	 <td><label> <select style="width:130px" name="routingFg"
							id="routingFg">
							<option value="">--Select--</option>
							<option value="1">Yes</option>
							<option value="0">No</option>
							</select></label>
						</td>
					</tr>
					<tr>
						<th><div>Routng (AS)</div></th>
					  	<td><label> <select style="width:130px" name="routingAs"
							id="routingAs">
							<option value="">--Select--</option>
							<option value="1">Yes</option>
							<option value="0">No</option>
							</select></label>
					  </td>
					</tr>
					<tr>
						<th width="180"><div>MP Status</div></th>
					 	<td><label> <select style="width:130px" name="mpStatus"
							id="mpStatus">
							<option value="">--Select--</option>
							<option value="Non-MP">Non-MP</option>
							<option value="MP">MP</option>
							</select></label></td>
					</tr>
					<tr>
						<th width="180"><div>Customer </div></th>
					  	<td><input class="text" type="text" id="cust" name="cust" width="150px"> 
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="custSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"custSSBtn",
                                inputField:"cust",
                                table:"WM_SAP_MASTER_CUSTOMER",
                                keyColumn:"CUSTOMER_CODE",
                                columns:"SHORT_NAME,CUSTOMER_CODE",
                                title:"Customer",
                                mode:1
							});
							</script>					  	
							</td>
					</tr>
					<tr>
						<th width="180"><div>AP Model </div></th>
					  	<td><input class="text" type="text" id="apModel" name="apModel"> 
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="apModelSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"apModelSSBtn",
                                inputField:"apModel",
                                table:"PIDB_IC_FG",
                                keyColumn:"AP_MODEL",
                                columns:"AP_MODEL",
                                title:"AP Model",
                                mode:1
							});
							</script>						  	
							</td>
					</tr>
					<tr>
						<th width="180"> <div>MCP Package</div></th>
					  	<td><label> <select id="mcpPkg" name="mcpPkg" style="width:130px"> 
					  		<option value="">--Select--</option>
					  		<option value="N/A">N/A</option>
							<option value="1">Yes</option>
							<option value="0">No</option>					  	
					  	</select></label></td>
					</tr>
					<tr>
						<th width="180"> <div>Status</div></th>
					 	 <td><select id="status" name="status" style="width:130px">
					  		<option value="">--Select--</option>
							<option value="Draft">Draft</option>
							<option value="Completed">Completed</option>
							<option value="Released">Released</option> 	 
					  	</select></td>
					</tr>
					<tr>
					<th width="180">Compay</th>
					  <td>
						<select class="select_w130" name="releaseTo" id="releaseTo">
							<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> companyList = (List<FunctionParameterTo>) request.getAttribute("companyNameList");
									for (FunctionParameterTo company : companyList) {
										String v = company.getFieldValue();
										
										%>
								<option value="<%=v %>"><%=company.getFieldShowName() %></option>
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
</form>
</body>
</html>
