<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.MpListCustomerMapTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<SapMasterCustomerTo> smcList = (List)request.getAttribute("selectList");
	List<SapMasterVendorTo> smcVendorList = (List)request.getAttribute("selectVendorList");
    String condition = (String) request.getAttribute("condition");

	List<MpListCustomerMapTo> custMapList = (List)request.getAttribute("custMapList");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript" src="<%=cp%>/js/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=cp%>/js/ext-all.js"></script>
<script type="text/javascript">
	var callback = "<%=request.getAttribute("callback")%>";
	window.onload = init;

	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	
	function onModify() {
		var tbl = document.getElementById('TbEOLCustList');
		var row = tbl.rows.length;
		if (row <= 1 ){
			var response = window.confirm("'No Any Customer In List' , Do you want submit this action");
			 if (response) {
				  	$('formEOLCust').request({
							onComplete: modifyEOLCustComplete
					})
			 }
		}else{
			$('formEOLCust').request({
				onComplete: modifyEOLCustComplete
				
			})
		}
	
	}

	function modifyEOLCustComplete(r) {
		var result = r.responseText;
		//alert("result= "+result);
		if(result=="true") {
			window.close();
		} else {
			alert("Modify Save Error");
		}
	}
</script>
</head>
<body onContextMenu="return false" onunload="if(history.length>0)history.go(+1);window.opener.location.reload()">

<form id="formEOLCust" name="selectEOLCust" action="<%=cp %>/md/mp_list_cust_map_edit.do?m=save" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">

<input type="hidden" name="partNum" value="<%=request.getAttribute("partNum")%>">
<input type="hidden" name="projCodeWVersion" value="<%=request.getAttribute("projCodeWVersion")%>">
<input type="hidden" name="tapeName" value="<%=request.getAttribute("tapeName")%>">
<input type="hidden" name="pkgCode" value="<%=request.getAttribute("pkgCode")%>">
<input type="hidden" name="icFgMaterialNum" value="<%=request.getAttribute("icFgMaterialNum")%>">
<input type="hidden" name="selProcess" value="<%=request.getAttribute("process")%>">

<input name='cust' type='text' style="display: none;">
<input name='vendor' type='text' style="display: none;">
<input name='tremark' type='text' style="display: none;">


<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Select Customer Map</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=smcList==null || smcList.size()==0?"No Customer Code.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
<%
	if((smcList != null && smcList.size() > 0) | (smcVendorList != null && smcVendorList.size() > 0)) {
%>


<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>Customer:
						<select name="selCust" id="selCust" class="select_w130">
						<option value="">--Select--</option>
						<%

							for(SapMasterCustomerTo smc : smcList) {
						%>
							<option value="<%=smc.getCustomerCode() %>"><%=smc.getCustomerCode() %>|<%=smc.getShortName() %></option>
						<%
							}
						%>
						</select>
					</th>

					<th>Vendor:
						<select name="selVendor" id="selVendor" class="select_w130">
						<option value="">--Select--</option>
						<%

							for(SapMasterVendorTo smcVendor : smcVendorList) {
						%>
							<option value="<%=smcVendor.getVendorCode() %>"><%=smcVendor.getVendorCode() %>|<%=smcVendor.getShortName() %></option>
						<%
							}
						
						%>
						</select>
					</th>
					<th>Remark:
					<label>
					  <input type="text" class="text" size="60"
							name="selRemark" id="selRemark">
					  </label>
					</th>
					<th>
						<input
						name="addBtn" type="button" class="add_button" id="addBtn"
						value="Add" onclick="onAddEOL()">
					</th>
				</tr>
				</tbody>
			</table>
			<P>
			<!--EOL List Table-->
			<table border=0 cellpadding=0 cellspacing=0 width="100%">
			<tr><td>
						<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
							<tbody>
							<tr>
								<th>Process</th>
								<th>Customer</th>
								<th>Vendor</th>
								<th>Remark</th>
								<th>Function</th>
							</tr>
							</tbody>
						<%if (custMapList != null && custMapList.size() > 0) {%>
							<%
								int idx = 0;
								String custShortName = "";
								String vendorShortName = "";
								for(MpListCustomerMapTo custMap : custMapList) {
									custShortName = "";
									for(SapMasterCustomerTo smc : smcList) {
										if ( smc.getCustomerCode().equals(BeanHelper.getHtmlValueByColumn(custMap, "CUST")) ){
											custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
											break;
										}
									}
									for(SapMasterVendorTo smcVendor : smcVendorList) {
										vendorShortName = "";
										if ( smcVendor.getVendorCode().equals(BeanHelper.getHtmlValueByColumn(custMap, "VENDOR")) ){
											vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
											break;
										}

									}
								idx ++;
								String tdcss = "class=\"c" + idx % 2+"\"";
							%>
							<tr>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(custMap, "PROCESS") %>&nbsp;
								<input name='process' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(custMap, "PROCESS") %>'>
							</td>
							<td <%=tdcss %>><%=custShortName%>&nbsp;
								<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(custMap, "CUST") %>'>
							</td>
							<td <%=tdcss %>><%=vendorShortName%>&nbsp;
								<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(custMap, "VENDOR") %>'>
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(custMap, "TREMARK") %>&nbsp;
								<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(custMap, "TREMARK") %>'>
							</td>
							<td <%=tdcss %>><input type='button' class='button' onclick='deleteRows(this)' value='Cancel'>&nbsp;</td>
							</tr>
							<%
								}
							%>
						<%}
							%>
						</table>
				</td>
			</tr>
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
						value="OK" onclick="onModify()">
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
<script language="javascript">

//Insert Upload File function
var row = 3000;

function onAddEOL(){
	if ($('selCust').value==""){
		alert("Customer must be choice");
		return;
	}
	if ($('selVendor').value==""){
		alert("Vendor must be choice");
		return;
	}
	//var fileUID = new UUID();


	row = row + 1;

	var tbl = document.getElementById('TbEOLCustList');
	var lastRow = tbl.rows.length;
	// if there's no header row in the table, then iteration = lastRow + 1
	var iteration = lastRow;

	addRow  = tbl.insertRow(iteration);
	addRow.bgColor = "#CCFF99";
	// cell 2 - input button
	var cell0 = addRow.insertCell(0);
	//cell0.innerHTML = lastRow ;

	cell0.innerHTML = $('selProcess').value  + "<input type='text' style='display: none;' class='text_protected' readonly  name='process' value='" + $('selProcess').value + "' /> "  ;

	var cell1 = addRow.insertCell(1);   
	
	cell1.innerHTML = $('selCust').options[$('selCust').selectedIndex].text + "<input type='text' style='display: none;' class='text_protected' readonly  name='cust' value='" + $('selCust').value + "' /> " ;
	//alert( cell1.innerHTML );

	var cell2 = addRow.insertCell(2);   
	
	cell2.innerHTML = $('selVendor').options[$('selVendor').selectedIndex].text + "<input type='text' style='display: none;' class='text_protected' readonly  name='vendor' value='" + $('selVendor').value + "' /> " ;
	//alert( cell1.innerHTML );

	var cell3 = addRow.insertCell(3);   
	
	cell3.innerHTML = $('selRemark').value + "<input type='text' style='display: none;' class='text_protected' readonly  name='tremark' value='" + $('selRemark').value + "' /> " ;
	//alert( cell1.innerHTML );

	var cell4 = addRow.insertCell(4);   
	cell4.innerHTML = " <input type='button' class='button' " +
					  " onclick='deleteRows(this)' value='Cancel'>";

	$('selRemark').value="";
	$('selCust').options[$('selCust').selectedIndex]
	$('selVendor').options[$('selVendor').selectedIndex]
	//alert( tbl.innerHTML ) ;
}

function deleteRows(obj){

	var delRow = obj.parentNode.parentNode;
	var tbl = delRow.parentNode.parentNode;
	var rIndex = delRow.sectionRowIndex;
	var rowObjArray = new Array(delRow);

	for (var i=0; i<rowObjArray.length; i++) {
		var rIndex = rowObjArray[i].sectionRowIndex;
		rowObjArray[i].parentNode.deleteRow(rIndex);
	}
}
</script>
<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
