<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.md.to.MpListTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo" %>

<%
	MpListTo ref = (MpListTo) request.getAttribute("ref");
	//FCG1
	List<FunctionParameterTo> custTrayAlarmList = (List)request.getAttribute("trayCustAlarmMapList");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function doReleaseToERP(s) {
  doSave(s);
}

function doSave(s) {
	if (s && s=="erp") {
		$('toErp').value = 1;
	} else {
		$('toErp').value = 0;
	}

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('prodCode') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('projCode') == "") {
		setMessage("error", "Project Code is must field.");
		return;
	}

	if ($F('tapeName') == "") {
		$('tapeName').value = "NA";
	}	
	
	if ($F('pkgCode') == "") {
		$('pkgCode').value = "NA";
	}
	
	//FCG1 取出customer, 判斷欄位
	var submitForm = true;
	var checkPartNum = $('partNum').value;
	var answer = true;
	var alertMsg = "";
	var i =1;
	var approvCustList = "";
	//取得所有屬於 approveCust 的 <option> 子元素
	var custOptions = document.getElementById('approveCust').getElementsByTagName('option');  
	for (i = 0; i < custOptions.length; i++) //迴圈 
	{ 
		approvCustList = approvCustList + "," + custOptions[i].value;
	}
	for(i=1;i<=4;i++)
	{
		   var checkTrayFlag = false;
			//var custNm1= $('mpCustomerName' + i).value;
			var trayDrawNo1 = $('mpTrayDrawingNo' + i).value;
			//custNm1 = custNm1.toUpperCase();
			<%
			for(FunctionParameterTo custObj:custTrayAlarmList)
			{
				String custName = custObj.getFieldValue().toUpperCase();
			%>
				if(approvCustList.indexOf('<%=custName%>') != -1)
				{
					checkTrayFlag = true;
				}
			<%
			}
			%>
			//check rule
			if(checkTrayFlag && checkPartNum.indexOf('PD')!=-1 )
			{
				//取PD後三碼
				var partNumTraySize =checkPartNum.substr(checkPartNum.indexOf('PD')+2, 3);
				//取12,13碼
				var tray12To13 = trayDrawNo1.substr(11,2);
				
				//250-14, 300-16, 400-20
				if(partNumTraySize =="250")
				{
					 if(tray12To13 !="" && tray12To13 !="14")
					 {
						 alertMsg = alertMsg + "Tray Drawing No.(" + i + "),";  
					 }
				}
				else if(partNumTraySize =="300")
				{
					if(tray12To13 !="" && tray12To13 !="16")
					 {
						 alertMsg = alertMsg + "Tray Drawing No.(" + i + "),";  	  
					 }
				}
				else if(partNumTraySize =="400")
				{
					if(tray12To13 !="" && tray12To13 !="20")
					 {
						 alertMsg = alertMsg + "Tray Drawing No.(" + i + "),";  	  
					 }
				}
			}//end if check rule							
	}
	//有不符合的資料
	if(alertMsg.length >0)
	{
		answer= confirm('IC 厚度與Tray 深度之搭配不符合CMO之規格[' + alertMsg + '] 是否確定Release to ERP?');	  
		submitForm = answer;
	}	
	//alert(submitForm);

	
	if(submitForm)
	{
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking mp_list exist...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_mpList_exist.do',
			{
				method: 'get',
				parameters: 'icFgMaterialNum=' + $F('icFgMaterialNum') + '&projCodeWVersion=' + $F('projCodeWVersion') + '&tapeName=' +$F('tapeName') + '&pkgCode=' + $F('pkgCode')+'&partNum=' + $F('partNum') ,
				onComplete: checkMpListExistComplete
			}
		);
	}
	else
	{
		return;
	}
	
}

function checkMpListExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "IC FG Material Number, Project Code w Version and Package Version is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
			setMessage("error", "Saving mp list...");
			selectAllOptions($('approveCust'));
			selectAllOptions($('approveTapeVendor'));
			selectAllOptions($('approveBpVendor'));
			selectAllOptions($('approveCpHouse'));
			selectAllOptions($('approveAssyHouse'));
			selectAllOptions($('approveFtHouse'));
			selectAllOptions($('approvePolishVendor'));
			selectAllOptions($('assignTo'));
			selectAllOptions($('eolCust'));
			selectAllOptions($('approveColorFilterVendor'));
			selectAllOptions($('approveWaferCfVendor'));
			selectAllOptions($('approveCpCspVendor'));
			selectAllOptions($('approveCpTsvVendor'));
			
			$('mpListCreate').action = $('mpListCreate').action + "?m=save";
			$('mpListCreate').submit();
	}
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

function selectSapMasterCustomerComplete(selectedProds) {
	//$('approveCust').length=0;
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCust'), prod[1], prod[0]);
		}
	}
}

function selectEOLCustomer() {
	var target ="<%=cp%>/dialog/select_sapMasterCustomer.do?m=list&callback=selectEOLCustomerComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectEOLCustomerComplete(selectedProds) {
	//$('approveCust').length=0;
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('eolCust'), prod[1], prod[0]);
		}
	}
}

//Add Form Hank 2008/10/31
function modifyEOLCust() {

	/*if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('prodCode') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}*/
	
	if ($F('tapeName') == "") {
		var answer = confirm ("Tape Name is Null?")
		if (answer)
			$('tapeName').value = "NA";
		else
			return;
		
	}	
	
	if ($F('pkgCode') == "") {
		var answer = confirm ("Package Code is Null?")
		if (answer)
			$('pkgCode').value = "NA";
		else
			return;
	}
	var target ="<%=cp%>/dialog/select_mpListEolCustomer.do?m=list&callback=selectEOLCustomerComplete&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode');

	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendor() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveTapeVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor2() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete2";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete2(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveBpVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor3() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete3";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete3(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCpHouse'), prod[1], prod[0]);
		}
	}
}
function selectSapMasterVendor4() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete4";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete4(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveAssyHouse'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor5() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete5";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete5(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveFtHouse'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor6() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete6";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete6(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approvePolishVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor7() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete7";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete7(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveColorFilterVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor8() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete8";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete8(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveWaferCfVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor9() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete9";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete9(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCpCspVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor10() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete10";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete10(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCpTsvVendor'), prod[1], prod[0]);
		}
	}
}

function retrieveEmail() {
	$('retrieveBtn').disabled = true;
	setMessage("error", "Retrieving assign emails...");
	new Ajax.Request(
		'<%=cp%>/ajax/fetch_email.do',
		{
			method: 'get',
			parameters: 'users='+ $F('assignTo'),
			onComplete: retrieveEmailComplete
		}
	);
}
function retrieveEmailComplete(r) {
	var returnValue = r.responseText.split("|");
	var errorItem = "";
	var successItem = "";
	if (returnValue.length == 1) {
		if (r.responseText.startWith("|")) {
			successItem = returnValue[0];
		} else {
			errorItem = returnValue[0];
		}
	} else {
		errorItem = returnValue[0];
		successItem = returnValue[1];
	}
	if (errorItem.length > 0) {
		setMessage("error", "The following user's email not found: " + errorItem + ".");
	} else {
		setMessage("error", "Retrieve user's email success. ");
	}
	
	$('assignEmail').value = successItem;
	$('retrieveBtn').disabled = false;
}

function createWithReference(){
	var projwv = $('projCodeWVersion').value;
	var tape = $('tapeName').value;
	
	if($F('projCodeWVersion') != ""){
		findmaterialName(projwv);
	}
	if ($('tapeName') != ""){
		findTapeNameWithRef(tape);
		
	}
}


function findTapeNameWithRef(tapeName) {
  if (tapeName != null && tapeName.length > 0) {
	removeOptions($('matTape'));
	new Ajax.Request( 
		'<%=cp%>/ajax/fetch_icTapeMaterialNum.do',
	{
		method: 'post',
		parameters: 'tapeName=' + tapeName,
		onComplete: fetchIcTapeMaterialNumComplete
	} );
	}
}

function findmaterialName(value) {
	if (value != null && value.length > 0) {
	   removeOptions($('matBp'));
	   removeOptions($('matCp'));
	   removeOptions($('matWf'));
    	new Ajax.Request( 
		'<%=cp%>/ajax/fetch_icBumpMaterialNum.do',
	{
		method: 'post',
		parameters: 'projWver=' + $('projCodeWVersion').value,
	    onComplete: fetchIcBumpMaterialNumComplete
		} );
		new Ajax.Request( 
		'<%=cp%>/ajax/fetch_icCpMaterialNum.do',
		{
		method: 'post',
    	parameters: 'projWver=' + $('projCodeWVersion').value,
				onComplete: fetchIcCpMaterialNumComplete
			} );

			new Ajax.Request( 
			'<%=cp%>/ajax/fetch_icWfMaterialNum.do',
			{
				method: 'post',
				parameters: 'projWver=' + $('projCodeWVersion').value,
				onComplete: fetchIcWfMaterialNumComplete
			} );
		}
	}

//LF Function
function fetchsetLFToolComplete(r) {
	var result = r.responseText.split("|");
		if (result == "") {
	  		setMessage("error", "Can not fetch Lead Frame Tool , Close Lead Frame Name.");		  	
		} else {
			if (result[0] != null && result[1] != null){
				$('lfTool').value = result[0];
				$('closeLfName').value = result[1];
			}else{
				$('lfTool').value = "";
				$('closeLfName').value = "";
			}
		}	
}

function setIcFgMCP(r) {
	new Ajax.Request(
			'<%=cp%>/ajax/fetch_icFgMCP.do',
			{
				method: 'get',
				parameters: 'icFgMaterialNum='+ $F('icFgMaterialNum'),
				onComplete: fetchsetIcFgMCPComplete
			}
		);
}

function fetchsetIcFgMCPComplete(r) {
	var result = r.responseText.split("|");
		if (result == "") {
	  		setMessage("error", "Can not fetch Lead Frame Tool , Close Lead Frame Name.");		  	
		} else {
			if (result[0] != null && result[1] != null){
				$('mcpDieQty').value = result[0];
				$('mcpPkg').value = result[1];
				$('mcpProd1').value = result[2];
				$('mcpProd2').value = result[3];
				$('mcpProd3').value = result[4];
				$('mcpProd4').value = result[5];
			}else{
				$('mcpDieQty').value = "";
				$('mcpPkg').value = "";
				$('mcpProd1').value = "";
				$('mcpProd2').value = "";
				$('mcpProd3').value = "";
				$('mcpProd4').value = "";
			}
		}	
}
</script>
</head>
<body onLoad="createWithReference()">
<!-- Header---------------------------------------------------------------------
* 2010.03.16/FCG1 @Jere Huang - 新增傳入customer及增加判斷alarm.
----------------------------------------------------------------------------- -->
<form name="mpListCreate" action="<%=cp %>/md/mp_list_create.do" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
		          <td class="pageTitle">Master Data :: Create MP List</td>
             </tr>
           </table>
	
			<div class="content">
			  <table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					</td>
					<td>
					<div align="right">
					Created by 
					<input name="createdBy" id="createdBy" type="text" class="text_protected" readonly>&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly>&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><span class="star">*</span>Part Number</th>
					    <td width="30%" colspan="2"><input class="text_required" readonly
							name="partNum" id="partNum"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="partNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"partNumSSBtn",
                                inputField:"partNum",
                                table:"PIDB_IC_FG ",
                                keyColumn:"PART_NUM",
                                columns:"PART_NUM",
                                title:"Part Number",
                                mode:0,
                                autoSearch:false,
                                callbackHandle:"customeCallback"
							});
							
						function customeCallback(inputField, columns, value) {
	                       if (value != null && value.length > 0) {
	                           $('partNum').value = value[0]["PART_NUM"];
	                           $('prodCode').value = "";
	                           $('pkgCode').value = "";

							   
	                           /*len = $('icFgMaterialNum').length;
	                           for (i = 0; i < len; i++) {
	                           		if ($('icFgMaterialNum').options[i].value != "") {
	                           			$('icFgMaterialNum').options[i] = null;
	                           		}
	                           }*/

							   removeOption($('matAs'),0);
	                           $('icFgMaterialNum').length = 1;
	                           
		                        new Ajax.Request( 
		                        '<%=cp%>/ajax/fetch_icFgMaterialNum.do',
		                        {
									method: 'post',
									parameters: 'partNum=' + $('partNum').value,
									onComplete: fetchIcFgMaterialNumComplete
								} );

								/*new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icAssyMaterialNum.do',
									{
										method: 'post',
										parameters: 'partNum=' + $('partNum').value,
										onComplete: fetchIcAssyMaterialNumComplete
									} );*/
								new Ajax.Request(
										'<%=cp%>/ajax/fetch_tradPkgLF.do',
										{
											method: 'get',
											parameters: 'partNum='+ $F('partNum'),
											onComplete: fetchsetLFToolComplete
										}
									);
                            }
                        }
                        
                        function fetchIcFgMaterialNumComplete(r) {
                        	var returnValue = r.responseText.split("|");
                        	if (returnValue.length > 0) {
	                        	for (i = 0 ; i < returnValue.length; i++) {
	                        		addOption($('icFgMaterialNum'), returnValue[i], returnValue[i]);
	                        	}
                        	}
                        }

						function fetchIcAssyMaterialNumComplete(r) {
							var returnValue = r.responseText.split("|");
							addOption($('matAs'), "--Select--", "");
							if (returnValue.length > 0) {
								for (i = 0 ; i < returnValue.length; i++) {
									if ( returnValue[i].length > 1 ) {
										addOption($('matAs'), returnValue[i], returnValue[i]);
									}
								}

								
								if ( $('matAs').length == 2 ){
									removeOption($('matAs'),0);
								}else if ( $('matAs').length > 2 ) {
										
								}else{
									removeOption($('matAs'));
								}
							}
						}
						
                        function changeIcFg() {
							removeOption($('matAs'));
                        	var icFgKey = $F('icFgMaterialNum');
	                        new Ajax.Request( 
	                        '<%=cp%>/ajax/fetch_icFg.do',
	                        {
								method: 'post',
								parameters: 'icFgMaterialNum=' + icFgKey,
								onComplete: fetchIcFgComplete
							} );
							
							new Ajax.Request( 
							'<%=cp%>/ajax/fetch_icAssyMaterialNum.do',
							{
								method: 'post',
								parameters: 'partNum=' + $('partNum').value,
								onComplete: fetchIcAssyMaterialNumComplete
							} );
                        }
                        
                        function fetchIcFgComplete(r) {
                        	if (r.responseText == null || r.responseText == "") {
                        		$('prodCode').value = "";
                        		$('pkgCode').value = "";
                        		return;
                        	}
                        	
                           var icFg = r.responseXML.getElementsByTagName("IcFgTo");	  
                           $('prodCode').value = icFg[0].getElementsByTagName("prodCode")[0].firstChild.nodeValue;
                           
                           $('icFgPkgCode').value = icFg[0].getElementsByTagName("pkgCode")[0].firstChild.nodeValue;
                           
                           var icFgPkgType = icFg[0].getElementsByTagName("pkgType")[0].firstChild.nodeValue;
                           if (icFgPkgType == "303" || icFgPkgType == "304") {
                           		$('pkgCode').value = icFg[0].getElementsByTagName("pkgCode")[0].firstChild.nodeValue;
                           } else {
                           		$('pkgCode').value = "NA";
                           }
                           if (icFgPkgType == "303") { //COG Not need Assy add rule 2008/05/02
							   removeOption($('matAs'));
						   }
                           
	                       var items = icFg[0].getElementsByTagName("pkgType")[0].firstChild.nodeValue;
	                       if (items=="303" || items=="304") {
								$('tapeName').value = "NA";
								//$('tapeName').disabled = true;
	                       		$('tapeNameLov').disabled = true;
	                        }
	    					else {		    					    
	     						$('tapeName').disabled = false;
	    						$('tapeNameLov').disabled = false;
	    					}
	    					
	    					var prodCode=icFg[0].getElementsByTagName("prodCode")[0].firstChild.nodeValue;
	                        var pkgCode=icFg[0].getElementsByTagName("pkgCode")[0].firstChild.nodeValue;

	                        new Ajax.Request( 
	                        '<%=cp%>/ajax/fetch_cog.do',
	                        {
								method: 'post',
								parameters: 'prodCode='+ prodCode + '&pkgCode=' + pkgCode,
								onComplete: fetchCogComplete
							} );                        	
                        }
                        
                        
                        var trays = new Array();	
                        function fetchCogComplete(r) {                  
                            var cogs = r.responseXML.getElementsByTagName("CogTo");
                            if (!cogs || cogs.length == 0) {
                                $('mpTrayDrawingNo1').length = 1;
                                $('mpTrayDrawingNoVer1').value = "";
                                $('mpColor1').value = "";
                                $('mpCustomerName1').value = "";
                                
                                $('mpTrayDrawingNo2').length = 1;
                                $('mpTrayDrawingNoVer2').value = "";
                                $('mpColor2').value = "";
                                $('mpCustomerName2').value = "";
                                
                                $('mpTrayDrawingNo3').length = 1;
                                $('mpTrayDrawingNoVer3').value = "";
                                $('mpColor3').value = "";
                                $('mpCustomerName3').value = "";
                                
                                $('mpTrayDrawingNo4').length = 1;
                                $('mpTrayDrawingNoVer4').value = "";
                                $('mpColor4').value = "";
                                $('mpCustomerName4').value = "";                                                                                                
                                return;
                            }
                            trays.length = 0;   
                            trays[0] = new Array();
                            trays[1] = new Array();
                            trays[2] = new Array();
                            trays[3] = new Array();
                            for (var i = 1; i < 7; i++) { 
                                var obj1 = cogs[0].getElementsByTagName("trayDrawingNo" + i);
                                if (obj1.length > 0) {
                                    trays[0][i - 1] = obj1[0].firstChild.nodeValue;
                                }
                                
                                var obj2 = cogs[0].getElementsByTagName("trayDrawingNoVer" + i);
                                if (obj2.length > 0) {
                                    trays[1][i - 1] = obj2[0].firstChild.nodeValue;
                                } else {
                                	trays[1][i - 1] = "";
                                }

                                var obj3 = cogs[0].getElementsByTagName("color" + i);
                                if (obj3.length > 0) {
                                    trays[2][i - 1] = obj3[0].firstChild.nodeValue;
                                } else {
                                	trays[2][i - 1] = "";
                                }
                                
                                var obj4 = cogs[0].getElementsByTagName("cogCustName" + i);

                                if (obj4.length > 0) {
                                    trays[3][i - 1] = obj4[0].firstChild.nodeValue;
                                } else {
                                	trays[3][i - 1] = "";                                
                                }                                
                            }

                            if (trays.length >0) {
                                $('mpTrayDrawingNo1').options.length = 1;
                                $('mpTrayDrawingNo2').options.length = 1;
                                $('mpTrayDrawingNo3').options.length = 1;
                                $('mpTrayDrawingNo4').options.length = 1;
                                for (var i = 0; i < 6; i++) {
                                    var value = trays[0][i];
                                    
                                    if (typeof value != "undefined") {
                                        addOption($('mpTrayDrawingNo1'), value, value);
                                        addOption($('mpTrayDrawingNo2'), value, value);
                                        addOption($('mpTrayDrawingNo3'), value, value);
                                        addOption($('mpTrayDrawingNo4'), value, value);
                                    }
                                }
                            }
                            
                            onTrayNo1Change();
                            onTrayNo2Change();
                            onTrayNo3Change();
                            onTrayNo4Change();
                        }
                       </script>							</td>
					  <th width="20%">Tray Drawing No. (1)</th>
					  <td width="30%"><select name="mpTrayDrawingNo1" id="mpTrayDrawingNo1" class="select_w130" onChange="onTrayNo1Change()">
					  <script type="text/javascript">
					  function onTrayNo1Change() {
					      var obj = $('mpTrayDrawingNo1');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer1').value = "";
				              $('mpColor1').value = "";
				              $('mpCustomerName1').value = "";			      
					      }
					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer1').value = trays[1][i];
					              $('mpColor1').value = trays[2][i];
					              $('mpCustomerName1').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>
					</tr>
					<tr>
					  <th width="20%"><span class="star">*</span>IC FG Material Number</th>
					  <td width="30%" colspan="2">
						  <select name="icFgMaterialNum" id="icFgMaterialNum" class="select_w130" onchange="changeIcFg();setIcFgMCP()">
						      <option value="">--Select--</option>
	                      </select>
					  </td>
                      	<th>Tray Drawing No. Ver. (1)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER1") %>"
							name="mpTrayDrawingNoVer1" id="mpTrayDrawingNoVer1"></td>					  
					</tr>
					<tr>
						<th width="20%"><span class="star">*</span>Product Code</th>
					  <td width="30%" colspan="2"><input class="text_protected" readonly
							name="prodCode" id="prodCode"   value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_CODE") %>"></td>
						<th>Color / Pocket Qty (1)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR1") %>"
							name="mpColor1" id="mpColor1"></td>							
                    </tr>
					<tr>
						<th width="20%"><span class="star">*</span>Package Code(for COG and TRD PKG)</th>
						<td width="30%" colspan="2"><input class="text_protected" readonly
							name="pkgCode" id="pkgCode" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PKG_CODE") %>">
							<input type="hidden" name="icFgPkgCode" id="icFgPkgCode" value="">							
							</td>					                         
						<th>COG Customer (1)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME1") %>"
							name="mpCustomerName1" id="mpCustomerName1"></td>	
					</tr>
					<tr>
					  <th width="20%"><span class="star">*</span>Project Code</th>
					  <td width="30%" colspan="2"><input class="text" readonly
							name="projCode" id="projCode" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE") %>"> 
					    <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeSSBtn",
                                inputField:"projCode",
                                name:"ProjectCode",
                                autoSearch:false,
                                mode:0
							});
						
							</script>
						</td>
					  <th width="20%">Tray Drawing No. (2)</th>
					  <td width="30%"><select name="mpTrayDrawingNo2" id="mpTrayDrawingNo2" class="select_w130" onChange="onTrayNo2Change()">
					  <script type="text/javascript">
					  function onTrayNo2Change() {
					      var obj = $('mpTrayDrawingNo2');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer2').value = "";
				              $('mpColor2').value = "";
				              $('mpCustomerName2').value = "";			      
					      }
					      					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer2').value = trays[1][i];
					              $('mpColor2').value = trays[2][i];
					              $('mpCustomerName2').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>						
				    </tr>
					<tr>
					  <th width="20%"><span class="star">*</span>Project Code w Version</th>
					  <td width="30%"><input class="text" readonly
							name="projCodeWVersion" id="projCodeWVersion" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE_W_VERSION") %>" >
					      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeWVersionSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                title:"Project Code w Version",
                                autoSearch:false,
                                mode:0,
								callbackHandle:"materialNameCallback"
							});

							function materialNameCallback(inputField, columns, value) {
							   if (value != null && value.length > 0) {
								   $('projCodeWVersion').value = value[0]["PROJ_CODE_W_VERSION"];
								   //$('matWf').length = 1;
								   //$('matBp').length = 1;
								   //$('matCp').length = 1;
								   removeOptions($('matBp'));
								   removeOptions($('matCp'));
								   removeOptions($('matWf'));
								   removeOptions($('matpolish'));
								   removeOptions($('matCf'));
								   removeOptions($('matWafercf'));
								   removeOptions($('matCsp'));
								   removeOptions($('matTsv'));

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icBumpMaterialNum.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcBumpMaterialNumComplete
									} );

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icCpMaterialNum.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcCpMaterialNumComplete
									} );

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icWfMaterialNum.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcWfMaterialNumComplete
									} );

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icWfPolish.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcWfPolishComplete
									} );

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icWfColorFilter.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcWfColorFilterComplete
									} );

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icWfWaferCf.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcWfWaferCfComplete
									} );
									
									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icWfCsp.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcWfCspComplete
									} );
									//TSV
									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icWfTsv.do',
									{
										method: 'post',
										parameters: 'projWver=' + $('projCodeWVersion').value,
										onComplete: fetchIcWfTsvComplete
									} );
								}
							}
							
							function fetchIcBumpMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matBp'), "--Select--", "");
								if (returnValue.length > 0) {
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matBp'), returnValue[i], returnValue[i]);
										}
									}

									if ( $('matBp').length == 2 ){
										removeOption($('matBp'),0);
									}else if ( $('matBp').length > 2 ) {
										
									}else{
										removeOption($('matBp'));
									}
								}
							}

							function fetchIcCpMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matCp'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matCp'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matCp').length == 2 ){
										removeOption($('matCp'),0);
									}else if ( $('matCp').length > 2 ) {
										
									}else{
										removeOption($('matCp'));
									}
								}
							}

							function fetchIcWfMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matWf'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matWf'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matWf').length == 2 ){
										removeOption($('matWf'),0);
									}else if ( $('matWf').length > 2 ) {
										
									}else{
										removeOption($('matWf'));
									}
								}
							}

							function fetchIcWfPolishComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matPolish'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matPolish'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matPolish').length == 2 ){
										removeOption($('matPolish'),0);
									}else if ( $('matPolish').length > 2 ) {
										
									}else{
										removeOption($('matPolish'));
									}
								}
							}

							function fetchIcWfColorFilterComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matCf'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matCf'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matCf').length == 2 ){
										removeOption($('matCf'),0);
									}else if ( $('matCf').length > 2 ) {
										
									}else{
										removeOption($('matCf'));
									}
								}
							}

							function fetchIcWfWaferCfComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matWafercf'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matWafercf'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matWafercf').length == 2 ){
										removeOption($('matWafercf'),0);
									}else if ( $('matWafercf').length > 2 ) {
										
									}else{
										removeOption($('matWafercf'));
									}
								}
							}

							function fetchIcWfCspComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matCsp'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matCsp'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matCsp').length == 2 ){
										removeOption($('matCsp'),0);
									}else if ( $('matCsp').length > 2 ) {
										
									}else{
										removeOption($('matCsp'));
									}
								}
							}
							//TSV
							function fetchIcWfTsvComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matTsv'), "--Select--", "");
								if (returnValue.length > 0) {
									
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matTsv'), returnValue[i], returnValue[i]);
										}
									}
									
									if ( $('matTsv').length == 2 ){
										removeOption($('matTsv'),0);
									}else if ( $('matTsv').length > 2 ) {
										
									}else{
										removeOption($('matTsv'));
									}
								}
								
							}
							</script>
						</td>
						<td>
							WF Material Num<P>
							<select name="matWf" id="matWf" class="select_w130" >
								<option value="">--Select--</option>
							</select>
						</td> 
                      	<th>Tray Drawing No. Ver. (2)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER2") %>"
							name="mpTrayDrawingNoVer2" id="mpTrayDrawingNoVer2"></td>								
				    </tr>
					<tr>
					  <th width="20%">Tape Name</th>
					  <td width="30%" colspan="2">
					  <input class="text" readonly name="tapeName" id="tapeName"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_NAME") %>">
					      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeNameLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeNameLov",
                                inputField:"tapeName",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"TAPE_NAME",
                                whereCause:" PKG_CODE = {icFgPkgCode} ",
                                title:"Tape Name",
                                autoSearch:false,
                                mode:0,
								callbackHandle:"tapeNameCallback"
							});

							function tapeNameCallback(inputField, columns, value) {
							   if (value != null && value.length > 0) {
								   $('tapeName').value = value[0]["TAPE_NAME"];
								   //$('matTape').length = 1;
								   removeOptions($('matTape'));

									new Ajax.Request( 
									'<%=cp%>/ajax/fetch_icTapeMaterialNum.do',
									{
										method: 'post',
										parameters: 'tapeName=' + $('tapeName').value,
										onComplete: fetchIcTapeMaterialNumComplete
									} );

								}
							}
							
							function fetchIcTapeMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								addOption($('matTape'), "--Select--", "");
								if (returnValue.length > 0) {
									for (i = 0 ; i < returnValue.length; i++) {
										if ( returnValue[i].length > 1 ) {
											addOption($('matTape'), returnValue[i], returnValue[i]);
										}
									}

									if ( $('matTape').length == 2 ){
										removeOption($('matTape'),0);
									}else if ( $('matTape').length > 2 ) {
										
									}else{
										removeOption($('matTape'));
									}
								}
							}
							

							</script>
						</td>
						<th>Color / Pocket Qty (2)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR2") %>"
							name="mpColor2" id="mpColor2"></td>							

					</tr>
					<tr>
					  <th width="20%">MP Status</th>
					  <td width="30%" colspan="2"><select name="mpStatus" id="mpStatus" class="select_w130">
					      <option value="">--Select--</option>
                          <option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Active</option>
                          <option value="0" <%="0".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Inactive</option>
                      </select></td>
						<th>COG Customer (2)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME2") %>"
							name="mpCustomerName2" id="mpCustomerName2"></td>	                      
				    </tr>
					<tr>
					 <th width="20%">MP Release Date</th>
					  <td width="30%" colspan="2"><label>
					  <input class="text" maxlength="20" size="20" readonly
							name="mpReleaseDate" id="mpReleaseDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_RELEASE_DATE", "yyyy/MM/dd") %>">
					  </label>
					   <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="mpReleaseDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"mpReleaseDate",
								ifFormat:"%Y/%m/%d",
								button:"mpReleaseDateBtn"
							});
						</script>
						</td>
					  <th width="20%">Tray Drawing No. (3)</th>
					  <td width="30%"><select name="mpTrayDrawingNo3" id="mpTrayDrawingNo3" class="select_w130" onChange="onTrayNo3Change()">
					  <script type="text/javascript">
					  function onTrayNo3Change() {
					      var obj = $('mpTrayDrawingNo3');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer3').value = "";
				              $('mpColor3').value = "";
				              $('mpCustomerName3').value = "";			      
					      }
					      					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer3').value = trays[1][i];
					              $('mpColor3').value = trays[2][i];
					              $('mpCustomerName3').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>											
				    </tr>
					<tr>
					  <th rowspan="2">REVISION ITEM</th>
					  <td rowspan="2" colspan="2">
					  <input type="text" readonly name="revisionItem" id="revisionItem" value="<%=BeanHelper.getHtmlValueByColumn(ref, "REVISION_ITEM") %>" class="text_200"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="revisionItemSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"revisionItemSSBtn",
                                inputField:"revisionItem",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='REVISION_ITEM' and fun_name='MP_LIST'",
								orderBy:"ITEM",
                                title:"REVISION_ITEM",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"revisionItemCallback"
							});

							function revisionItemCallback(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";
							
									for(var i = 0; i < value.length; i++) {
										tempValue += "," + value[i][columns[0]];
									}
							
									if(tempValue != "") {
										$(inputField).value = tempValue.substring(1);
									}
								}
							}
							</script></td>
                      	<th>Tray Drawing No. Ver. (3)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER3") %>"
							name="mpTrayDrawingNoVer3" id="mpTrayDrawingNoVer3"></td>	
					</tr>
					<tr>
						<th>Color / Pocket Qty (3)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR3") %>"
							name="mpColor3" id="mpColor3"></td>		
				    </tr>
					<tr>
					    <th width="20%" rowspan="3" style="vertical-align:top">Approve Customer</th>
						<td width="30%" colspan="2" rowspan="3" style="vertical-align:top"><input name="button" type="button" class="button" onClick="selectSapMasterCustomer()" value="Add">
                          <input name="button3" type="button" class="button" onClick="removeSelectedOptions($('approveCust'))" value="Remove">
                          <br>
                          <select name="approveCust" size="3" multiple class="select_w130" id="approveCust">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CUST") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CUST");
					    			String[] list = listStr.split(",");
					    			SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterCustomerTo to = sapMasterCustomerDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getCustomerGrp() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>		                               
                          </select></td>	
						
						<th>COG Customer (3)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME3") %>"
							name="mpCustomerName3" id="mpCustomerName3"></td>	 	
				    </tr>			
				    <tr>
					  <th width="20%">Tray Drawing No. (4)</th>
					  <td width="30%"><select name="mpTrayDrawingNo4" id="mpTrayDrawingNo4" class="select_w130" onChange="onTrayNo4Change()">
					  <script type="text/javascript">
					  function onTrayNo4Change() {
					      var obj = $('mpTrayDrawingNo4');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer4').value = "";
				              $('mpColor4').value = "";
				              $('mpCustomerName4').value = "";			      
					      }
					      					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer4').value = trays[1][i];
					              $('mpColor4').value = trays[2][i];
					              $('mpCustomerName4').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>	
                    </tr>
                    <tr>  
                      	<th>Tray Drawing No. Ver. (4)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER4") %>"
							name="mpTrayDrawingNoVer4" id="mpTrayDrawingNoVer4"></td>	                      							    
				    </tr>	    
				    <tr>
                          <th width="20%" rowspan="3" style="vertical-align:top">Approve Tape Vendor</th>
					     <td width="15%" rowspan="3" style="vertical-align:top"><input name="button2" type="button" class="button" onClick="selectSapMasterVendor()" value="Add">
                           <input name="button2" type="button" class="button" onClick="removeSelectedOptions($('approveTapeVendor'))" value="Remove">
                           <br>
                           <select size="3" multiple class="select_w130" name="approveTapeVendor" id="approveTapeVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_TAPE_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_TAPE_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>			  
                           </select>

						   </td>
						<td width="15%"  rowspan="3" style="vertical-align:top">
							Tape Material Num<P>
							<select name="matTape" id="matTape" class="select_w130" >
								<option value="">--Select--</option>
							</select>
							
						</td>
						<th>Color / Pocket Qty (4)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR4") %>"
							name="mpColor4" id="mpColor4"></td>		                           				    
				    </tr>
				    <tr>
						<th>COG Customer (4)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME4") %>"
							name="mpCustomerName4" id="mpCustomerName4"></td>	
				    </tr>
				    <tr>
						<th>MCP Die Quantity</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY") %>"
							name="mcpDieQty" id="mcpDieQty"></td>	    
				    </tr>		
				    <tr>
					  <th width="20%" rowspan="3" style="vertical-align:top">Approve Bumping Vendor</th>
					  <td width="30%" rowspan="3" style="vertical-align:top"><input name="button22" type="button" class="button" onClick="selectSapMasterVendor2()" value="Add">
                        <input name="button22" type="button" class="button" onClick="removeSelectedOptions($('approveBpVendor'))" value="Remove">
                        <br>
                        <select size="3" multiple class="select_w130" name="approveBpVendor" id="approveBpVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_BP_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_BP_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>			                        
                        </select></td>
						<td rowspan="3" style="vertical-align:top">
						Bump Material Num<P>
							<select name="matBp" id="matBp" class="select_w130" >
								<option value="">--Select--</option>
							</select>
						</td> 
						<th>MCP Package</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PKG") %>"
							name="mcpPkg" id="mcpPkg"></td> 												
				    </tr>
					<tr>
						<th>MCP Product1</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_1") %>"
							name="mcpProd1" id="mcpProd1"></td>	    
				    </tr>
					<tr>
						<th>MCP Product2</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_2") %>"
							name="mcpProd2" id="mcpProd2"></td>	    
				    </tr>
				    <tr>
					  <th width="20%" rowspan="3" style="vertical-align:top">Approve CP House</th>
					     <td width="30%" rowspan="3" style="vertical-align:top"><input name="button222" type="button" class="button" onClick="selectSapMasterVendor3()" value="Add">
                           <input name="button222" type="button" class="button" onClick="removeSelectedOptions($('approveCpHouse'))" value="Remove">
                           <br>
                           <select size="3" multiple class="select_w130" name="approveCpHouse" id="approveCpHouse">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_HOUSE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_HOUSE");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>							    	
                           </select></td>
						<td rowspan="3">
							CP Material Num<P>
							<select name="matCp" id="matCp" class="select_w130" >
								<option value="">--Select--</option>
							</select>
						</td> 
						<th>MCP Product3</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_3") %>"
							name="mcpProd3" id="mcpProd3"></td>
								
				    </tr>
					<tr>
						<th>MCP Product4</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_4") %>"
							name="mcpProd4" id="mcpProd4"></td>
					</tr>
					<tr>
						<th>Lead Frame Tool</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "LF_TOOL") %>"
							name="lfTool" id="lfTool"></td>
					</tr>
				    <tr>
						<th width="20%" rowspan="2"  style="vertical-align:top">Approve Assembly House</th>
						<td width="30%" rowspan="2"><span style="vertical-align:top">
						  <input name="button2222" type="button" class="button" onClick="selectSapMasterVendor4()" value="Add">
                          <input name="button2222" type="button" class="button" onClick="removeSelectedOptions($('approveAssyHouse'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveAssyHouse" id="approveAssyHouse">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_ASSY_HOUSE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_ASSY_HOUSE");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td rowspan="2">
							Assy Material Num<P>
							<select name="matAs" id="matAs" class="select_w130" >
								<option value="">--Select--</option>
							</select>
						</td> 
						<th>Close Lead Frame Name</th>
					  <td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CLOSE_LF_NAME") %>"
							name="closeLfName" id="closeLfName"></td>						
				    </tr>			
				    <tr>
						<th>MP Process Flow</th>
						<td width="23%" ><input type="text" cols="80%" readonly name="processFlow" id="processFlow" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROCESS_FLOW") %>" class="text_120"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="MpProcessFlowSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"MpProcessFlowSSBtn",
                                inputField:"processFlow",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='MP_PROCESS_FLOW' and fun_name='MP_LIST'",
								orderBy:"ITEM",
                                title:"MP_PROCESS_FLOW",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"processFlowCallback"
							});

							function processFlowCallback(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";
							
									for(var i = 0; i < value.length; i++) {
										tempValue += "," + value[i][columns[0]];
									}
							
									if(tempValue != "") {
										$(inputField).value = tempValue.substring(1);
									}
								}
							}
							</script>
						</td> 				    
				    </tr>
					
					<!-- Add 2008/03/03 for FT TEST House -->
					<tr>
						<th width="20%"  style="vertical-align:top">Approve FT TEST<br>(WLM , WLO) House</th>
						<td width="30%" ><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor5()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveFtHouse'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveFtHouse" id="approveFtHouse">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_FT_HOUSE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_FT_HOUSE");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td >&nbsp;</td> 
						<th width="20%"  style="vertical-align:top">CP BIN(Maximum 256 Char.)</th>
						<td width="30%"  style="vertical-align:top"><textarea id="cpBin" name="cpBin" cols="40" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "CP_BIN") %></textarea></td>				    
				    </tr>
					<!-- Add 2009/02/18 for CP Polish House -->
					<tr>
						<th width="20%" style="vertical-align:top">Approve CP Polish Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor6()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approvePolishVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approvePolishVendor" id="approvePolishVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_POLISH_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_POLISH_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td width="15%" style="vertical-align:top">
							Polish Material Num<P>
							<select name="matPolish" id="matPolish" class="select_w130" >
								<option value="">--Select--</option>
							</select>
							
						</td> 
						<th width="20%"  style="vertical-align:top">Remark</th>
						<td width="30%"  style="vertical-align:top"><textarea id="remark" name="remark" cols="40" rows="4" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					</tr>
					<tr>
						<th width="20%" style="vertical-align:top">Approve Color Filter Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor7()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveColorFilterVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveColorFilterVendor" id="approveColorFilterVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_COLOR_FILTER_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_COLOR_FILTER_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td width="15%" style="vertical-align:top">
							Color Filter Material Num<P>
							<select name="matCf" id="matCf" class="select_w130" >
								<option value="">--Select--</option>
							</select>
							
							<p>&nbsp;</td> 
						<th width="20%" style="vertical-align:top">AssignTo Remark</th>
						<td width="30%" style="vertical-align:top">
						<!--Start Assign To -->
						<table border="0" cellspace="0" cellpadding="0" margin="0">
                        <tr>
                          <td rowspan="2">
					  	<select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO");
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
                          <td><input name="button4" type="button" class="button" id="userBtn" value="User" >
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
										addOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
									}
								}
							}						
							</script>
                          </td>
                          <td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
                        </tr>
                        <tr>
                          <td><input name="button4" type="button" class="button" id="roleBtn" value="Role" >
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
										addOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
									}
								}
							}						
							</script>
                          </td>
                        </tr>
                      </table>
						<!--End Assign To -->
						</td> 					  				    

												    
				    </tr>
					<tr>
						<th width="20%" style="vertical-align:top">Approve Wafer CF Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor8()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveWaferCfVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveWaferCfVendor" id="approveWaferCfVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_WAFER_CF_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_WAFER_CF_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td width="15%" style="vertical-align:top">
							WAFER CF Material Num<P>
							<select name="matWafercf" id="matWafercf" class="select_w130" >
								<option value="">--Select--</option>
							</select>							
							<p>&nbsp;</td> 
						<th>&nbsp;</th>
						<td>&nbsp;</td>				    
				    </tr>
					<tr>
						<th>Approve CP_CSP Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor9()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveCpCspVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveCpCspVendor" id="approveCpCspVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_CSP_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_CSP_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td width="15%" style="vertical-align:top">
							CSP Material Num<P>
							<select name="matCsp" id="matCsp" class="select_w130" >
								<option value="">--Select--</option>
							</select>							
							<p>&nbsp;</td> 
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<th>Approve CP_TSV Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor10()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveCpTsvVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveCpTsvVendor" id="approveCpTsvVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_TSV_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_TSV_VENDOR");
					    			String[] list = listStr.split(",");
					    			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterVendorTo to = sapMasterVendorDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getVendorCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>	            
                          </select>
						</span></td> 
						<td width="15%" style="vertical-align:top">
							TSV Material Num<P>
							<select name="matTsv" id="matTsv" class="select_w130" >
								<option value="">--Select--</option>
							</select>							
							<p>&nbsp;</td> 
						<th>&nbsp;</th>
						<td>&nbsp;</td>
					</tr>
				</tbody>
			</table>
		
			</div>
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
						onclick="doReleaseToERP('erp')">
						&nbsp;&nbsp;
					        <input
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

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
<script type="text/javascript">
new Ajax.Request( 
'<%=cp%>/ajax/fetch_cog.do',
                  {
	method: 'post',
	parameters: 'prodCode='+ $F('prodCode') + '&pkgCode=' + $F('pkgCode'),
	onComplete: fetchCogComplete
} );
</script>