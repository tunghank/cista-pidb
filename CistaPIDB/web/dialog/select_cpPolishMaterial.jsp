<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpPolishMaterialTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.MpListEolCustTo"%>
<%@ page import="com.cista.pidb.md.to.CpMaterialTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<CpPolishMaterialTo> smcList = (List)request.getAttribute("cpPolishList");
    String condition = (String) request.getAttribute("condition");
%>
<html>
<head>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var callback = "<%=request.getAttribute("callback")%>";
	window.onload = init;

	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	
	//Insert Upload File function
	var row = 3000;

	function fetchMaxVariantCpMaterialComplete(r) {
		var returnValue = r.responseText.split("|");

	//Show CP List Data & Insert AJAX Return Value.
	if (returnValue[2]=="true"){
		alert("this Polish Material already Exist...!!!");
		return;
	}else{
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
	
	cell0.innerHTML = returnValue[0] ;

	var cell1 = addRow.insertCell(1);   

	cell1.innerHTML =$('projectWVersion').value;
	
	
	//alert( cell1.innerHTML );
	var cell2 = addRow.insertCell(2);  

	cell2.innerHTML =returnValue[1];
	}	 
}

function getMaxCpMaterialNum(){
	$('polishVariant').value = $('cpPolishVariant').value;
	
	new Ajax.Request( 
	'<%=cp%>/ajax/fetch_maxVariantCpPolish.do?polishVariant='+$('polishVariant').value,
	{
		method: 'post',
		parameters: 'projCodeWVersion=' + $('projCodeWVersion').value+'&polishDesc='+$F('polishDesc')+'&status='+$F('status'),
		onComplete: fetchMaxVariantCpMaterialComplete
	} );
}

function onExit(){
	window.close();
}
</script>
<script type="text/javascript" src="<%=cp %>/js/prototype163.js"></script>
</head>
<body onContextMenu="return false" onunload="if(history.length>0)history.go(+1);window.opener.location.reload()"> 
<form id="cpMaterialCreateForm" name="selectEOLCust" action="<%=cp %>/md/cp_material_edit.do?m=save" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<input type="hidden" name="remark" value="<%=request.getAttribute("remark")%>">
<input type="hidden" name="status" value="<%=request.getAttribute("status")%>">
<input type="hidden" name="projCodeWVersion" value="<%=request.getAttribute("projCodeWVersion")%>">
<input type="hidden" name="c" value="">
<input type="hidden" name="polishVariant" value="">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: CP Polish Modify</td>
				</tr>
			</table>

<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					<th>Polish Material Variant:
						 <select class="select_w130" name="cpPolishVariant" id="cpPolishVariant">
						 <option value="">--Select--</option>
						 <%
									List<CpMaterialTo> variantList = (List<CpMaterialTo>) request.getAttribute("variantList");
									for (CpMaterialTo variant : variantList) {
										String v = variant.getCpMaterialNum();
										/*String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(variant, "CP_MATERIAL_NUM"))) {
											selected = "selected";
										}*/
										
										%>
								<option value="<%=v %>"><%=v %></option>
										<%
									}
								%>
							
							</select>
					</th>
					<th align="left">ProjectCode W version:
						<input type="text" class="text_protected" name="projectWVersion" id="projectWVersion" readonly value="<%=request.getAttribute("projCodeWVersion")%>">
					</th>
					<th>Polish Material Desc:
					  <input type="text" class="text" name="polishDesc" id="polishDesc" value="">
					</th>
					<th>
						<input
						name="addBtn" type="button" class="add_button" id="addBtn"
						value="Add" onclick="onAddDesc()">
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
								<th>Cp Polish Material Num</th>
								<th>ProjectCode W version</th>
								<th>Cp Polsih Desc</th>
							</tr>
							</tbody>
						<%if (smcList != null && smcList.size() > 0) {%>
							<%
								int idx = 0;
								for(CpPolishMaterialTo polish : smcList) {
									if (polish != null){
								idx ++;
								String tdcss = "class=\"c" + idx % 2+"\"";
							%>
							<tr>
							<td <%=tdcss %>><%=polish.getCpPolishMaterialNum()%>&nbsp;
								<input name='cpPolishMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(polish, "CP_POLISH_MATERIAL_NUM") %>'>
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(polish, "PROJECT_CODE_W_VERSION") %>&nbsp;
								<input name='projectCodeWVersion' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(polish, "PROJECT_CODE_W_VERSION") %>'>
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(polish, "DESCRIPTION") %>&nbsp;
								<input name='description' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(polish, "DESCRIPTION") %>'>
							</td>
							</tr>
							<%
								}
							}
							%>
						
						</table>
				</td>
			</tr>
			</table>
	</td>
</tr>
</table>

<%
	}
%>
			<table class="formErrorAndButton" align="right">
				<tr>
					<td>
					<div >
					  <input
						name="okBtn" type="button" class="button" id="okBtn"
						value="OK" onclick="onExit()">
					 <!-- <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="window.close()">-->
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



function onAddDesc(){
	if ($('polishVariant').value=""){
		alert("CP Material is must choose");
		return;
	
	}	
		
	if ($('polishDesc').value==""){
		alert("polishDesc must field");
		return;
	}
	getMaxCpMaterialNum();
	$('polishDesc').value="";
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
