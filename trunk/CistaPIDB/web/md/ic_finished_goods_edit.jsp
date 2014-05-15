<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.IcFgQueryTo"%>
<%@ page import="com.cista.pidb.md.to.IcFgTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.code.to.SapAppCategoryTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterPackageTypeTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	IcFgTo icFgTo = (IcFgTo) request.getAttribute("ref");
	String lfTool = (String) request.getAttribute("lfTool");
	lfTool = null != lfTool ? lfTool : "";

	String closeLfName = (String) request.getAttribute("closeLfName");
	closeLfName = null != closeLfName ? closeLfName : "";
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body onload="doInit()">

<script>
var projFab = "<%=request.getAttribute("fab") != null ? request.getAttribute("fab") : ""%>";
var cpType;

var oldProdCode = '<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PROD_CODE")%>';
var oldprojCode = '<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PROJ_CODE")%>';
var oldPkgCode = '<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PKG_CODE")%>';
var oldVariant = '<%=BeanHelper.getHtmlValueByColumn(icFgTo, "VARIANT")%>';
var oldMaterialNum = '<%=BeanHelper.getHtmlValueByColumn(icFgTo, "MATERIAL_NUM")%>';
function doReleaseToERP(s) {
  doSave(s);
}
function doSave(s) {
	if (s && s=="erp") {
		$('toErp').value = 1;
	} else {
		$('toErp').value = 0;
	}
	
	if ($F('prodCode') == "") {
		setMessage("error", "Product code is must field.");
		return;
	}
	if ($F('projCode') == "") {
		setMessage("error", "Project code is must field.");
		return;
	}
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	if ($F('partNum') == "") {
		setMessage("error", "Part Number is must field.");
		return;
	}		
	//2010/05/25 by jere, modify value
	if ($F('mcpPkg') == "") {
		setMessage("error", "MCP PKG is must have value.");
		return;
	}	
	
	if ($F('pkgType') == "303") {
		tempPkg = $F('pkgCode');
		if (tempPkg.length > 2 && tempPkg.substring(0,2) == "PD") {
			//$('pkgCode').value = "G" + tempPkg.substring(2);
		} else {
			setMessage("error", "Package Code is error.");
			return;			
		}
	}
	
	$('releaseBtn').disabled = true;
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking ic finished goods exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_icFg_exist.do',
		{
			method: 'get',
			parameters: 'materialNum='+ $F('materialNum'),
			onComplete: checkIcFgExistComplete
		}
	);
}

function checkIcFgExistComplete(r) {
	var result = r.responseText;
	if(result == "pkgcodeerror") {
		setMessage("error", "Package code is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "faberror") {
		setMessage("error", "Fab is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "optionerror") {
		setMessage("error", "Option is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "prodnameerror") {
		setMessage("error", "Product name is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "varianterror") {
		setMessage("error", "Variant is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result=="true" && $F('materialNum')!= oldMaterialNum) {
		setMessage("error", "Ic finished goods is already exist.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		var incompleted = "";
		
		if ($F('appCategory') == "") {
			incompleted += ", Application Category";
		}
		if ($F('pkgType') == "") {
			incompleted += ", Package Type";
		}
		if ($F('mcpPkg') == "") {
			incompleted += ", MCP Package";
		}
		
		if (incompleted != "") {
			if (confirm("The following required fields is incomplete, the ic finished goods will be saved as draft:\r\n" + incompleted.substring(2))) {
				setMessage("error", "Saving ic finished goods...");
				selectAllOptions($('cpTestProgNameList'));
				selectAllOptions($('ftTestProgNameList'));
				selectAllOptions($('assignTo'));
				
				
				$('icFgForm').action = $('icFgForm').action + "&toErp=" + $F('toErp');
				$('icFgForm').submit();
			} else {
				$('releaseBtn').disabled = false;
				$('saveBtn').disabled = false;
				$('resetBtn').disabled = false;
				setMessage("error", "User cancel.");
			}
		} else {
			setMessage("error", "Saving ic finished goods...");
			selectAllOptions($('cpTestProgNameList'));
			selectAllOptions($('ftTestProgNameList'));
			selectAllOptions($('assignTo'));
			
			$('icFgForm').action = $('icFgForm').action + "&toErp=" + $F('toErp');
			$('icFgForm').submit();
		}	
	}
}


function selectCP(projCode) {
	var target = "<%=cp%>/dialog/select_cpProgram.do?m=list&callback=selectCPComplete&projCode="+projCode;
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cpTest","scrollbars=yes, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectCPComplete(selectedTests, cpMaterialNum) {

	cpType = cpMaterialNum;
	
	$('cpTestProgNameList').length = 0;
	/*if (cpMaterialNum) {
		if (cpMaterialNum == "1") {
			$('cpTestProgNameList').length = 0;
		}
	}*/
	
	var testStr = "";
	var projCodeWVersion = "";
	if (selectedTests && selectedTests.length>0) {
		for(var i=0; i<selectedTests.length; i++) {
		
			if (selectedTests[i].indexOf("|") > -1) {
				var arr = selectedTests[i].split("|");
				addOption($('cpTestProgNameList'), arr[1], arr[1]);
				testStr += "," + arr[1];	
				
				projCodeWVersion = arr[0];			
				$('projCodeWVersion').value = arr[0];
			} else {
				addOption($('cpTestProgNameList'), selectedTests[i], selectedTests[i]);
				testStr += "," + selectedTests[i];					
			}
		}
	}
	
	if (testStr.length > 0) {
		testStr = testStr.substring(1);
	}
	
	//Remove By Hank 2008/01/07
	/*if (cpMaterialNum != "1") {
		
		new Ajax.Request(
			'<%=cp%>/ajax/fetch_cpMateriaNum.do?projCodeWVersion='+projCodeWVersion,
			{
				method: 'get',
				parameters: 'cpTestProgramNameList='+ testStr,
				onComplete: fetchCpMateriaNumComplete
			}
		);		
	}*/
}

//Remove By Hank 2008/01/07
/*function fetchCpMateriaNumComplete(r) {
	var result = r.responseText;
	$('cpMaterialNum').value=result;
}*/

function removeCp (opt) {
	removeSelectedOptions(opt);
	
	
	
	var testStr = "";
	if ($('cpTestProgNameList').options.length>0) {
		for(var i=0; i<$('cpTestProgNameList').options.length; i++) {
			testStr += "," + $('cpTestProgNameList').options[i].value;
		}
	}
	//Remove By Hank 2008/01/07
	/*if (testStr.length > 0) {
		testStr = testStr.substring(1);
		
		if (cpType != "1") {
			new Ajax.Request(
				'<%=cp%>/ajax/fetch_cpMateriaNum.do',
				{
					method: 'get',
					parameters: 'cpTestProgramNameList='+ testStr,
					onComplete: fetchCpMateriaNumComplete
				}
			);	
		} else {
			$('cpMaterialNum').value="";
		}
	} else {
		$('cpMaterialNum').value="";
	}*/
		
}

 
function selectFT(partNum,projCode) {
	var target = "<%=cp%>/dialog/select_ftProgram.do?m=list&callback=selectFTComplete&partNum="+partNum+"&projCode="+projCode;
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_ftProgram","scrollbars=yes, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectFTComplete(selectedTests,type) {
	if (type) {
		if (type == "1") {
			$('ftTestProgNameList').length = 0;
		}
	}
	if (selectedTests && selectedTests.length>0) {
		for(var i=0; i<selectedTests.length; i++) {
			addOption($('ftTestProgNameList'), selectedTests[i], selectedTests[i]);
		}
	}
	/*
	if (selectedTests && selectedTests.length>0) {
		for(var i=0; i<selectedTests.length; i++) {
			var test = selectedTests[i].split("|");
			addOption($('ftTestProgList'), test[1], test[1]);
		}
	}*/
}

function selectProdCode() {
	var target = "<%=cp%>/dialog/select_product_radio.do?m=list&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product_radio","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		$('prodCode').value = selectedProds;
	}
	
	setPartNum ()
}

function selectProjCode() {
	var target = "<%=cp%>/dialog/select_projCode_radio.do?m=list&callback=selectProjCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_projCode_radio","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		$('projCode').value = selectedItems;
	}
	
	new Ajax.Request(
		'<%=cp%>/ajax/fetch_proj_fab.do',
		{
			method: 'get',
			parameters: 'projCode=' + $F('projCode'),
			onComplete: setProjFab
		}
	);	
	
}

function selectSapMasterCustomer() {
	var target = "<%=cp%>/dialog/select_sapMasterCustomer.do?m=list&callback=selectSapMasterCustomerComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterCustomerComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			var test = selectedItems[i].split("|");
			selects += "," + test[1];
		}
		if (selects.length > 0) {
			$('custName').value = selects.substring(1);
		}		
	}	
}

function setProjFab (r) {
	var result = r.responseText;
	projFab=result;
	setPartNum ();
}

function setPartNum () {
	$('partNum').value = $('prodCode').value + projFab + $('pkgCode').value;
}

function selectPkgCode(pkgType, prodCode, projCode) {
	var target = "<%=cp%>/dialog/select_icFgSpecPkgCode.do?m=list&callback=selectPkgCodeComplete&pkgType="+pkgType+"&prodCode="+prodCode+"&projCode="+projCode;
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icFgSpecPkgCode","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectPkgCodeComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		if (selectedItems[0].indexOf("|") > -1) {
			var arr = selectedItems[0].split("|");
			$('pkgCode').value = arr[0];
			$('cust').value = arr[1];
			$('apModel').value = unescape(arr[2]);
			//$('custName').value = arr[2];
		} else {
			$('pkgCode').value = selectedItems;
			$('cust').value = "";
			$('apModel').value = "";	
			//$('custName').value = "";		
		}
	}
	setPartNum ();
}
//Add Hank 2008/01/29
function doInit(){
	removeOptions($('cpMaterialNum'));
	new Ajax.Request( 
		'<%=cp%>/ajax/fetch_icCpMaterialNumByProjCode.do',
		{
			method: 'post',
			parameters: 'projCode=' + $('projCode').value,
			onComplete: fetchIcCpMaterialNumCompleteInit
	} );


}

function fetchIcCpMaterialNumCompleteInit(r) {
	
	var returnValue = r.responseText.split("|");
	addOption($('cpMaterialNum'), "--Select--", "");
	if (returnValue.length > 0) {
		
		for (i = 0 ; i < returnValue.length; i++) {
			if ( returnValue[i].length > 1 ) {
				addOption($('cpMaterialNum'), returnValue[i], returnValue[i]);
			}
		}
		
		if ( $('cpMaterialNum').length == 2 ){
			removeOption($('cpMaterialNum'),0);
		}else if ( $('cpMaterialNum').length > 2 ) {
			
		}else{
			removeOption($('cpMaterialNum'));
		}

		
	}
	
	SelectItem_Combo_Value($('cpMaterialNum'),'<%=BeanHelper.getHtmlValueByColumn(icFgTo, "CP_MATERIAL_NUM") %>' );
}

//Add Hank 2008/01/29
function selectCPTestProg() {
	if ($F('cpMaterialNum')!=""){
		
		new Ajax.Request( 
			'<%=cp%>/ajax/fetch_cpTestProgByCPMaterialNum.do',
			{
				method: 'post',
				parameters: 'cpMaterialNum=' + $('cpMaterialNum').value,
				onComplete: fetchCpTestProgComplete
		} );
	}else{
		removeOptions($('cpTestProgNameList'));
		//alert( "NO CP MaterialNum " );
	}
}

function fetchCpTestProgComplete(r) {
	removeOptions($('cpTestProgNameList'));
	var returnValue = r.responseText.split("|");
	if (returnValue.length > 0) {
		
		for (i = 0 ; i < returnValue.length; i++) {
			if ( returnValue[i].length > 1 ) {
				addOption($('cpTestProgNameList'), returnValue[i], returnValue[i]);
			}
		}
	}
}
</script>

<form name="icFgForm" action="<%=cp %>/md/ic_fg_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Modify IC Finished Goods</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />
                         <%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
                         &nbsp;					
					</div>
					</td>
					<td>
					<div align="right">
					Created by 
					<input name="createdBy" id="createdBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "CREATED_BY") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "MODIFIED_BY") %>">&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div class="erp_label">Material Number</div></th>
					 	<td width="30%"><input class="text_protected" type="text" readonly="true" id="materialNum" name="materialNum" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "MATERIAL_NUM") %>" width="150px"></td>
						<th width="20%"><div class="erp_label">MCP Product 1</div></th>
					 	<td width="30%"><input class="text" type="text" id="mcpProd1" name="mcpProd1"  
					 		value="<%
								String mcpProd1 = BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PROD_1");
								out.print(mcpProd1 != null && !mcpProd1.equals("") ? mcpProd1 : "N/A");
								%>" width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div><span class="star">*</span> Product Code</div></th>
					  	<td width="30%"><input class="text_protected" readonly="true" id="prodCode" name="prodCode"  value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PROD_CODE") %>"  width="150px">
							</td>
						<th width="20%"><div class="erp_label">MCP Product 2</div></th>
					  	<td width="30%"><input class="text" type="text" id="mcpProd2" name="mcpProd2" 
					  		value="<%
								String mcpProd2 = BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PROD_2");
								out.print(mcpProd2 != null && !mcpProd2.equals("") ? mcpProd2 : "N/A");								
								%>" width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div>Variant</div></th>
					  	<td width="30%"><input class="text_protected" type="text" readonly="true" id="variant" name="variant" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "VARIANT") %>" width="150px"></td>
						<th width="20%"><div class="erp_label">MCP Product 3</div></th>
					  	<td width="30%"><input class="text" type="text" id="mcpProd3" name="mcpProd3" 
					  		value="<%
								String mcpProd3 = BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PROD_3");
								out.print(mcpProd3 != null && !mcpProd3.equals("") ? mcpProd3 : "N/A");		
								%>"  width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Project Code</div></th>
					  	<td width="30%"><input class="text_required" type="text" readonly id="projCode" name="projCode" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PROJ_CODE") %>"  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
							<script type="text/javascript">
							function resetSS() {
								if ($F('prodCode') != null && $F('prodCode') != "") {
									SmartSearch.setup({
		                                cp:"<%=cp%>",
		                                button:"projCodeSSBtn",
		                                inputField:"projCode",
		                                whereCause:" PROJ_NAME in (select PROJ_NAME from PIDB_PROJECT where pidb_include(PROD_CODE_LIST, ',', {prodCode})>=1) ",
		                                name:"ProjectCode",
		                                autoSearch:false,
		                                callbackHandle:"selectProjCodeCallback"                                
									});					
								} else {
									SmartSearch.setup({
		                                cp:"<%=cp%>",
		                                button:"projCodeSSBtn",
		                                inputField:"projCode",
		                                name:"ProjectCode",
		                                autoSearch:false,
		                                callbackHandle:"selectProjCodeCallback"                                
									});				
								}								
							}
							
							resetSS();
							function selectProjCodeCallback(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";
							
									for(var i = 0; i < value.length; i++) {
										tempValue += "," + value[i][columns[0]];
									}
							
									if(tempValue != "") {
										$(inputField).value = tempValue.substring(1);
									}
								}
								
								new Ajax.Request(
									'<%=cp%>/ajax/fetch_proj_fab.do',
									{
										method: 'get',
										parameters: 'projCode=' + $F('projCode'),
										onComplete: setProjFab
									}
								);

								removeOptions($('cpMaterialNum'));
								new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icCpMaterialNumByProjCode.do',
									{
										method: 'post',
										parameters: 'projCode=' + $('projCode').value,
										onComplete: fetchIcCpMaterialNumComplete
								} );
							}


							function fetchIcCpMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('cpMaterialNum'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('cpMaterialNum'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('cpMaterialNum').length == 2 ){
										removeOption($('cpMaterialNum'),0);
									}else if ( $('cpMaterialNum').length > 2 ) {
										
									}else{
										removeOption($('cpMaterialNum'));
									}
								}
							}
							</script>												
							</td>					
						<th width="20%"><div class="erp_label">MCP Product 4</div></th>
					  	<td width="30%"><input class="text" type="text" id="mcpProd4" name="mcpProd4" 
					  		value="<%
							String mcpProd4 = BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PROD_4");
							out.print(mcpProd4 != null && !mcpProd4.equals("") ? mcpProd4 : "N/A");									
					  		%>" width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label">Package Type</div></th>
					  	<td width="30%"><select id="pkgType" name="pkgType" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PKG_TYPE") %>" style="width:130px">
					  	<option value="">--Select--</option>
					  	<%
					  		String realPkgType = BeanHelper.getHtmlValueByColumn(icFgTo, "PKG_TYPE");
					  		List pkgTypeList = (List) request.getAttribute("pkgTypeList");
					  		for (int i = 0; i < pkgTypeList.size(); i ++) {
					  		    out.print("<option value=\""+((SapMasterPackageTypeTo) pkgTypeList.get(i)).getPackageType()+"\"");
					  		    if (realPkgType != null && realPkgType.equals(((SapMasterPackageTypeTo) pkgTypeList.get(i)).getPackageType())) {
					  		        out.print(" selected ");
					  		    }
					  		  	out.print(">"+((SapMasterPackageTypeTo) pkgTypeList.get(i)).getDescription()+"</option>");
					  		}
					  	%>							  	
					  	</select></td>
						<th>Lead Frame Tool</th>
						<td><input class="text_protected" type="text" readonly id="leadFrameTool" name="leadFrameTool" value="<%=lfTool%>"  width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Package Code</div></th>
					  	<td width="30%"><input class="text_protected" type="text" readonly="true" id="pkgCode" name="pkgCode" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PKG_CODE") %>"  width="150px">
					  	</td>				
						<th>Close Lead Frame Name</th>
						<td><input class="text_200_protected" type="text" readonly id="closeLeadFrameName" name="closeLeadFrameName" value="<%=closeLfName%>"  width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Part Number</div></th>
					  	<td width="30%"><input class="text_200" type="text" id="partNum" name="partNum" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "PART_NUM") %>"  width="150px"></td>					
						<th width="20%"><div>Remark</div></th>
					  	<td width="30%"><input class="text" type="text" id="remark" name="remark" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "REMARK") %>" width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label">Routing (FG)</div></th>
					  	<td width="30%"><input type="checkbox" id="routingFg" name="routingFg" width="150px" value="1"
							<%=icFgTo!=null && icFgTo.getRoutingFg()?"checked":"" %>></td>
						<th width="20%" rowspan="2"><div>AssignTo</div></th>
					  	<td width="30%" rowspan="2">
					  	<table border="0" cellspace="0" cellpadding="0" margin="0">
					  	<tr>
					  	<td rowspan="2">
					  	<select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(icFgTo, "ASSIGN_TO") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(icFgTo, "ASSIGN_TO");
					    			String[] list = listStr.split(",");
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			%>
						    			<option value="<%=s %>"><%=s %></option>
						    			<%
						    			}
						    		}
					    		}
					    	%>					  	
					  	</select>	
					  	</td>
					  	<td>
					  	<input id="userBtn" type="button" class="button" value="User" >
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"userBtn",
                                inputField:"assignTo",
								name:"AssignToUser",
                                autoSearch:false,
                                callbackHandle:"selectUserCallback" 
							});
							function selectUserCallback(selectField, columns, value) {
								if ($(selectField) && value != null && value.length > 0) {
									for(var i = 0; i < value.length; i++) {
										addDifferOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
									}
								}
							}							
							</script>
							</td>
							<td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
							</tr>
							<tr>
							<td>
					  	<input id="roleBtn" type="button" class="button" value="Role" >
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"roleBtn",
                                inputField:"assignTo",
								name:"AssignToRole",
                                autoSearch:false,
                                callbackHandle:"selectRoleCallback" 
							});
							function selectRoleCallback(selectField, columns, value) {
								if ($(selectField) && value != null && value.length > 0) {
									for(var i = 0; i < value.length; i++) {
										addDifferOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
									}
								}
							}							
							</script>	
							</td>		
							</tr>
						</table>	
						<input type="hidden" id="assignEmail" name="assignEmail" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "ASSIGN_EMAIL") %>" />											  	
					  	</td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label">Routing (AS)</div></th>
					  	<td width="30%"><input type="checkbox" id="routingAs" name="routingAs" width="150px" value="1"
							<%=icFgTo!=null && icFgTo.getRoutingAs()?"checked":"" %>></td>				
					</tr>
					<tr>
						<th width="20%"><div>MP Status</div></th>
					  	<td width="30%">
					  	<input class="text_protected" type="text" readonly="true" id="mpStatus" name="mpStatus" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "MP_STATUS")%>"  width="150px">
					  	</td>
						<th width="20%"><div class="erp_label">CP Material Number</div></th>
					  	<td width="30%"><!--remove Hank 2008/01/29 <input class="text_protected" type="text" readonly id="cpMaterialNum" name="cpMaterialNum" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "CP_MATERIAL_NUM") %>" width="150px"> -->
						<!--Add Hank 2008/01/29 -->
						<select name="cpMaterialNum" id="cpMaterialNum" class="select_w130" onChange="selectCPTestProg()">
								<option value="">--Select--</option>
						</select>
					  	<input type="hidden" id="projCodeWVersion" name="projCodeWVersion" value="" >
					  	</td>
					  	
					</tr>	
					<tr>
						<th width="20%"><div>Customer</div></th>
						<% 
							SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
							SapMasterCustomerTo sapMasterCustomerTo = sapMasterCustomerDao.findByVendorCode(BeanHelper.getHtmlValueByColumn(icFgTo, "CUST"));			
						%>
					  	<td width="30%">
					  	<input class="text" type="text" readonly id="cust" name="cust" value="<%=BeanHelper.getHtmlValueByColumn(sapMasterCustomerTo, "SHORT_NAME")%>"  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="custSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"custSSBtn",
                                inputField:"cust",
                                table:"WM_SAP_MASTER_CUSTOMER",
                                keyColumn:"SHORT_NAME",
                                columns:"SHORT_NAME,CUSTOMER_CODE",
                                autoSearch:false,
                                title:"Customer"
							});
							</script>
					  	</td>
						<th width="20%" rowspan="4" style="vertical-align:top"><div class="erp_label">CP Test Program Name List</div></th>
					  	<td width="30%" rowspan="4" style="vertical-align:top">
					  	<!--remove Hank 2008/01/29 <input type="button" class="button" value="Add" onclick="selectCP($('projCode').value)"><input type="button" class="button" value="Remove" onclick="removeCp($('cpTestProgNameList'))"><br> -->
					  	<select size="6" multiple style="width:100%" name="cpTestProgNameList" id="cpTestProgNameList" >
					  	
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(icFgTo, "CP_TEST_PROG_NAME_LIST") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(icFgTo, "CP_TEST_PROG_NAME_LIST");
					    			String[] list = listStr.split(",");
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			%>
						    			<option value="<%=s %>"><%=s %></option>
						    			<%
						    			}
						    		}
					    		}
					    	%>							  	
					  	</select>
					  	</td>
					</tr>	
					<tr>
						<th width="20%"><div>AP Model</div></th>
					  	<td width="30%"><input class="text_120" type="text" readonly="true" id="apModel" name="apModel" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "AP_MODEL") %>"  width="150px"><img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="apModelSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"apModelSSBtn",
                                inputField:"apModel",
                                table:"PIDB_IC_TAPE ic",
                                keyColumn:"TAPE_CUST_PROJ_NAME",
                                columns:"TAPE_CUST_PROJ_NAME",
								whereCause:"ic.PROD_NAME in(select b.prod_name from pidb_product b where b.prod_code={prodCode})",
                                autoSearch:false,
                                title:"AP Model"
							});
							</script>
						</td>
					</tr>	
					<tr>
						<th width="20%"><div class="erp_label">Application Category</div></th>
					  	<td width="30%"><select id="appCategory" name="appCategory" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "APP_CATEGORY") %>" style="width:130px">
					  	<option value="">--Select--</option>
					  	<%
					  		String realCat = BeanHelper.getHtmlValueByColumn(icFgTo, "APP_CATEGORY");
					  		List appCateGoryList = (List) request.getAttribute("appCateGoryList");
					  		for (int i = 0; i < appCateGoryList.size(); i ++) {
					  		    out.print("<option value=\""+((SapAppCategoryTo) appCateGoryList.get(i)).getApplicationCategory() + "\"");
					  		    if (realCat != null && realCat.equals(((SapAppCategoryTo) appCateGoryList.get(i)).getApplicationCategory())) {
					  		        out.print(" selected ");
					  		    }
					  		    out.print(">"+((SapAppCategoryTo) appCateGoryList.get(i)).getDescription()+"</option>");
					  		}
					  	%>					  	
					  	</select></td>		  	
					</tr>	
					<tr>
						<th width="20%"><div class="erp_label">MCP Die Quantity</div></th>
					  	<td width="30%"><select id="mcpDieQty" name="mcpDieQty" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY") %>" style="width:130px">
					  	<!-- <option value="">--Select--</option> -->
					  	<option value="N/A" <%="N/A".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY"))?"selected":"" %>>N/A</option>
					  	<option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY"))?"selected":"" %>>1</option>
					  	<option value="2" <%="2".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY"))?"selected":"" %>>2</option>
					  	<option value="3" <%="3".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY"))?"selected":"" %>>3</option>
					  	<option value="4" <%="4".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY"))?"selected":"" %>>4</option>
					  	<option value="5" <%="5".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_DIE_QTY"))?"selected":"" %>>5</option>
					  	</select></td>						
					</tr>	
					<tr>
						<th width="20%"><div class="erp_label">MCP Package</div></th>
					  	<td width="30%">
					  	   <select id="mcpPkg" name="mcpPkg" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PKG") %>" style="width:130px">
					  	   <!-- <option value="">--Select--</option> -->
					  		<!-- <option value="N/A" <%="N/A".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PKG"))?"selected":"" %>>N/A</option> -->
						  	<option value="" <%="N/A".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PKG"))?"selected":"" %>>---</option>
						  	<option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PKG"))?"selected":"" %>>Yes</option>
						  	<option value="0" <%="0".equals(BeanHelper.getHtmlValueByColumn(icFgTo, "MCP_PKG"))?"selected":"" %>>No</option>
					  		</select>
					  	</td>						
						<th width="20%" rowspan="4" style="vertical-align:top" ><div class="erp_label">FT Test Program List</div></th>
					  	<td width="30%" rowspan="4" style="vertical-align:top">
					  	<!--remove Hank 2008/01/29 <input type="button" class="button" value="Add" onclick="selectFT($('partNum').value,$('projCode').value)"><input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('ftTestProgNameList'))"><br> -->
					  	<select size="6" multiple style="width:100%" name="ftTestProgNameList" id="ftTestProgNameList">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(icFgTo, "FT_TEST_PROG_LIST") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(icFgTo, "FT_TEST_PROG_LIST");
					    			String[] list = listStr.split(",");
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			%>
						    			<option value="<%=s %>"><%=s %></option>
						    			<%
						    			}
						    		}
					    		}
					    	%>						  	
					  	</select>
					  	</td>					  	
					</tr>	
					<tr>
						<th width="20%"><div>Status</div></th>
					  	<td width="30%"><input class="text_protected" type="text" readonly="true" id="status" name="status" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "STATUS") %>"  width="150px"></td>	
					</tr>	
					<tr>
						<th width="20%"><div>Vendor Device</div></th>
					  	<td width="30%"><input class="text" type="text" id="vendorDevice" name="vendorDevice" value="<%=BeanHelper.getHtmlValueByColumn(icFgTo, "VENDOR_DEVICE") %>"  width="150px"></td>
					</tr>		
					<tr>
						<th>&nbsp;</th>
						<td>&nbsp;</td>							
					</tr>																																		
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right">
					<input type="hidden" id="toErp" name="toErp">
					
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
					           <input name="releaseBtn" type="button"
						   class="erp_button" id="releaseBtn" value="Release To ERP"
						   onclick="doReleaseToERP('erp')"> <input
						   name="saveBtn" type="button" class="button" id="saveBtn"
						   value="Save" onClick="doSave()"> 
						   <%
						   }     
				        %>
						<input
						name="resetBtn" type="reset" class="button" id="resetBtn"
						value="Reset"></div>
					</td>
				</tr>
			</table>
						
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

<script>

jumpUrl = '<%=request.getAttribute("jumpUrl")%>';
if (jumpUrl&&jumpUrl!='null') {
	
	window.open(jumpUrl,"");
}

if (<%=request.getAttribute("reminder")%>) {
	alert("                  ** Reminder ** \r\n 1. Please confirm CP and FT Test Program are correct \r\n 2. Please remember to Release FG to ERP if CP has been updated ");
}
</script>
<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>

