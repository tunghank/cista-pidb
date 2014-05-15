<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpMaterialTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.MpListEolCustTo"%>
<%@ include file="/common/global.jsp"%>
<%
	List<CpMaterialTo> smcList = (List)request.getAttribute("cpMaterialList");
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
	
	function onModify() {
		var tbl = document.getElementById('TbEOLCustList');
		var row = tbl.rows.length;
		if (row <= 1 ){
			var response = window.confirm("'No Any Customer In List' , Do you want submit this action");
			 if (response) {
				  	$('cpMaterialCreateForm').request({
						parameters: 'remark=' + $('remark').value
							+'&newVariant='+$('newVariant').value
								+'&newDesc='+$('newDesc').value,
						onComplete: modifyCpMaterialComplete
					})
			 }
		}else{
			$('cpMaterialCreateForm').request({
				parameters: 'remark=' +$('remark').value
					+'&newVariant='+$('newVariant').value
					+'&newDesc='+$('newDesc').value,
				onComplete: modifyCpMaterialComplete
				
			})
		}
	
	}
	function modifyCpMaterialComplete(r) {
		var result = r.responseText;
		if(result=="false") {
			alert("Modify Save Error");
		} 
	}

	//Insert Upload File function
	var row = 3000;

	function fetchMaxVariantCpMaterialComplete(r) {
		var returnValue = r.responseText.split("|");

	//Show CP List Data & Insert AJAX Return Value.

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

function getMaxCpMaterialNum(){
	new Ajax.Request( 
	'<%=cp%>/ajax/fetch_maxVariantCpMaterial.do?materialNum='+$F('materialNum').value+'&projCodeWVersion=' + $('projCodeWVersion').value+'&remark='+$F('remark')+'&cpMaterialDesc='+$F('materialDesc'),
	{
		method: 'post',
		//parameters: 'projCodeWVersion=' + $('projCodeWVersion').value+'&remark='+$F('remark')+'&cpMaterialDesc='+$F('materialDesc'),
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
<input type="hidden" name="materialNum" value="<%=request.getAttribute("materialNum")%>">
<input type="hidden" name="projCodeWVersion" value="<%=request.getAttribute("projCodeWVersion")%>">
<input type="hidden" name="c" value="">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: CP Material Modify</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=smcList==null || smcList.size()==0?"No Cp Material Data.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
<%
	if(smcList != null && smcList.size() > 0) {
%>


<table border=0 cellpadding=0 cellspacing=0 width="100%"><tr><td>
<div id="resultPanel" style="overflow:auto;width:100px;">
			<table class="grid" border="0" cellpadding="1" cellspacing="1">
				<tbody>
				<tr>
					</th>
					<th align="left">ProjectCode W version:
						<input type="text" class="text_protected" name="projectWVersion" id="projectWVersion" readonly value="<%=request.getAttribute("projCodeWVersion")%>">
					</th>
					<th>Cp Material Desc:
					  <input type="text" class="text" name="materialDesc" id="materialDesc" value="">
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
								<th>Cp Material Num</th>
								<th>ProjectCode W version</th>
								<th>Cp Material Desc</th>
							</tr>
							</tbody>
						<%if (smcList != null && smcList.size() > 0) {%>
							<%
								int idx = 0;
								String custShortName = "";
								for(CpMaterialTo material : smcList) {
									if (material != null){
								idx ++;
								String tdcss = "class=\"c" + idx % 2+"\"";
							%>
							<tr>
							<td <%=tdcss %>><%=material.getCpMaterialNum()%>&nbsp;
								<input name='cpMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "CP_MATERIAL_NUM") %>'>
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(material, "PROJECT_CODE_W_VERSION") %>&nbsp;
								<input name='projectCodeWVersion' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "PROJECT_CODE_W_VERSION") %>'>
							</td>
							<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(material, "DESCRIPTION") %>&nbsp;
								<input name='description' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "DESCRIPTION") %>'>
							</td>
							</tr>
							<%
								}
							}
							%>
						<%}%>
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
		
	if ($('materialDesc').value==""){
		alert("materialDesc must field");
		return;
	}
	
	getMaxCpMaterialNum();
	$('materialDesc').value="";
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
