<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.IcTapeTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">

function selectPartNum() {
	var target = "<%=cp%>/dialog/select_partNum_checkbox.do?m=list&callback=selectPartNumComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_partNum","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
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

function selectProjName() {
	var target = "<%=cp%>/dialog/select_proj_name.do?m=pre&callback=selectProjNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_proj_name","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('projName').value = selects.substring(1);
		}
	}
}

function selectProjCodeWVersion() {
	var target = "<%=cp%>/dialog/select_icWafer_projCodeWVersion.do?m=pre&callback=selectProjCodeWVersionComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icWafer_projCodeWVersion","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeWVersionComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('projCodeWVersion').value = selects.substring(1);
		}
	}
}
</script>
</head>
<body>
<form id="mpListQueryForm" action="<%=cp %>/md/mp_list_query.do?m=query" method="post">			  
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: MP List Query</td>
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
						<th width="180">Part Number</th>
					  <td><input class="text" maxlength="20" size="20"
							name="partNum" id="partNum"> 
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="partNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"partNumSSBtn",
                                inputField:"partNum",
                                table:"PIDB_MP_LIST ",
                                keyColumn:"PART_NUM",
                                title:"Part Number",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Product Code</th>
					  <td><label>
					  <input class="text" maxlength="20" size="20"
							name="prodCode" id="prodCode">
					 <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodCodeSSBtn",
                                inputField:"prodCode",
                                table:"PIDB_MP_LIST ",
                                keyColumn:"PROD_CODE",
                                title:"Product Code",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Project Code</th>
					  <td><input class="text" maxlength="20" size="20"
							name="projCode" id="projCode">
				      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeSSBtn",
                                inputField:"projCode",
                                table:"PIDB_MP_LIST ",
                                keyColumn:"PROJ_CODE",
                                title:"Project Code",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Project Code w Version</th>
						<td><input class="text" maxlength="20" size="20"
							name="projCodeWVersion" id="projCodeWVersion">
					     <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeWVersionSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_MP_LIST ",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                title:"Project Code w Version",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">Package Code(for COG and TRD PKG)</th>
						<td><input class="text" maxlength="20" size="20"
							name="pkgCode" id="pkgCode">
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="pkgCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"pkgCodeSSBtn",
                                inputField:"pkgCode",
                                table:"PIDB_MP_LIST ",
                                keyColumn:"PKG_CODE",
                                title:"Package Code(for COG and TRD PKG)",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
					  <th width="180">Tape Name</th>
					  <td><select name="tapeName" class="select_w130" id="tapeName">
  						    <option value="">--Select--</option>
							<%
                               List<String> retList = (List<String>) request.getAttribute("selectList");
                                for(String tapeName : retList) {
                                	if(tapeName==null || "".equals(tapeName)){
                                		break;
                                	}

							%>
							<option value="<%=tapeName %>"><%=tapeName %></option>
							<%
							    }
							%>
                          </select>
					  </td>
					</tr>
					<tr>
						<th width="180">MP Release Date</th>
					  <td>
					  <label> 
					  <input class="text" maxlength="20" size="20" readonly
							name="mpReleaseDateFrom" id="mpReleaseDateFrom">
					  </label>
					   <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="mpReleaseDateFromBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"mpReleaseDateFrom",
								ifFormat:"%Y/%m/%d",
								button:"mpReleaseDateFromBtn"
							});
						</script>
						~
					 <label> 
					  <input class="text" maxlength="20" size="20" readonly
							name="mpReleaseDateTo" id="mpReleaseDateTo">
					  </label>
					   <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="mpReleaseDateToBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"mpReleaseDateTo",
								ifFormat:"%Y/%m/%d",
								button:"mpReleaseDateToBtn"
							});
						</script>
					  </td>
					</tr>
					<tr>
						<th width="180"><div>FG Material</div></th>
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
						<th width="180"><div>Revision Item</div></th>
					  	<td><input class="text_200" type="text" id="revisionItem" name="revisionItem" width="150px">
 							<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="revisionItemSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"revisionItemSSBtn",
                                inputField:"revisionItem",
                                title:"Revision Item",                                
                                table:"PIDB_FUN_PARAMETER_VALUE",
                                keyColumn:"FIELD_VALUE",
								columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='REVISION_ITEM' and fun_name='MP_LIST'",
								orderBy:"ITEM",
                                mode:0
							});
						</script>	  	
						</td>
					</tr>
					<tr>
						<th width="180">Approve Customer</th>
						<td><input class="text" type="text" readonly id="approveCust" name="approveCust" width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="approveCustSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"approveCustSSBtn",
                                inputField:"approveCust",
                                table:"WM_SAP_MASTER_CUSTOMER",
                                keyColumn:"SHORT_NAME",
                                columns:"SHORT_NAME,CUSTOMER_CODE",
                                title:"Customer",
								orderBy:"CUSTOMER_CODE",
                                mode:1
							});
							</script>
					   </td>
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
