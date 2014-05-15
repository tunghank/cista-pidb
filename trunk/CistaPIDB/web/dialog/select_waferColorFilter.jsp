<!-- Header---------------------------------------------------------------------
* 2010.03.22/FCG1 @Jere Huang - 修改沒有值時也要秀出來可以修改, 將判斷size拿掉,多傳入mp_status .
------------------------------------------------------------------------------->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.WaferColorFilterTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%
	List<WaferColorFilterTo> smcList = (List)request.getAttribute("waferCFList");
    String condition = (String) request.getAttribute("condition");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	var callback = "<%=request.getAttribute("callback")%>";
	window.onload = init;

	function init() {
		autoFitBottomArea('resultPanel', 80, 400);
	}
	
	/*function onModify() {
		var tbl = document.getElementById('TbColorFilterList');
		var row = tbl.rows.length;
		if (row <= 1 ){
			var response = window.confirm("'No Any Customer In List' , Do you want submit this action");
			 if (response) {
				  	$('waferCFCreateForm').request({
						parameters: 'remark=' + $('remark').value
							+'&newVariant='+$('newVariant').value
								+'&newDesc='+$('newDesc').value,
						onComplete: modifyCFMaterialComplete
					})
			 }
		}else{
			$('waferCFCreateForm').request({
				parameters: 'remark=' +$('remark').value
					+'&newVariant='+$('newVariant').value
					+'&newDesc='+$('newDesc').value,
				onComplete: modifyCFMaterialComplete
				
			})
		}
	
	}
	function modifyCFMaterialComplete(r) {
		var result = r.responseText;
		if(result=="false") {
			alert("Modify Save Error");
		} 
	}*/

	//Insert Upload File function
	var row = 3000;

	function fetchMaxVariantCFMaterialComplete(r) {
		var returnValue = r.responseText.split("|");

	//Show CP List Data & Insert AJAX Return Value.

	row = row + 1;

	var tbl = document.getElementById('TbWaferCFList');
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
	//FCG1 
	$('addBtn').disabled = "disabled";
	$('error').innerHTML = "";  
}

function getMaxCFMaterialNum(){
	new Ajax.Request( 
	'<%=cp%>/ajax/fetch_maxVariantWaferCF.do?materialNum='+$('materialNum').value,
	{
		method: 'post',
		parameters: 'projCodeWVersion=' + $('projCodeWVersion').value+'&remark='+$F('remark')+'&waferCfDesc='+$F('waferCfDesc')+'&mpStatus='+$F('mpStatus'),
		onComplete: fetchMaxVariantCFMaterialComplete
	} );
}

function onExit(){
	window.close();
}
</script>
<script type="text/javascript" src="<%=cp %>/js/prototype163.js"></script>
</head>
<body onContextMenu="return false" onunload="if(history.length>0)history.go(+1);window.opener.location.reload()"> 

<form id="waferCFCreateForm" name="selectEOLCust" action="<%=cp %>/md/cp_material_edit.do?m=save" method="post">
<input type="hidden" name="callback" value="<%=request.getAttribute("callback")%>">
<input type="hidden" name="remark" value="<%=request.getAttribute("remark")%>">
<input type="hidden" name="materialNum" value="<%=request.getAttribute("materialNum")%>">
<input type="hidden" name="projCodeWVersion" value="<%=request.getAttribute("projCodeWVersion")%>">
<input type="hidden" name="mpStatus" value="<%=request.getAttribute("mpStatus")%>">
<input type="hidden" name="c" value="">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start-->
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Dialog :: Wafer CF Material Modify</td>
				</tr>
			</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					   <div class="formErrorMsg" id="error"><html:errors/><!--ErrorMessage--><%=smcList==null || smcList.size()==0?"No Wafer Color Filter Data.":"" %>&nbsp;</div>
					</td>
				</tr>
			</table>
			<table border=0 cellpadding=0 cellspacing=0 width="100%">
			<tr>
			<td>
			<div id="resultPanel" style="overflow:auto;width:100px;">
				<table class="grid" border="0" cellpadding="1" cellspacing="1">
					<tbody>
					<tr>
						<th align="left">ProjectCode W version:
							<input type="text" class="text_protected" name="projectWVersion" id="projectWVersion" readonly value="<%=request.getAttribute("projCodeWVersion")%>">
						</th>
						<th>
						   Wafer CF Material Desc: <input type="text" class="text" name="waferCfDesc" id="waferCfDesc" value="">
						</th>
						<th>
						<!-- //FCG1 -->
						<%if(smcList != null && smcList.size() > 0){ %>
							<input name="addBtn" type="button" class="add_button" id="addBtn"value="Add" onclick="onAddDesc()" disabled>
						<%}else{ %>
						   <input name="addBtn" type="button" class="add_button" id="addBtn"value="Add" onclick="onAddDesc()">
						<%} %>
						</th>
					</tr>
					</tbody>
				</table>
				<P>
				<!--EOL List Table-->
				<table border=0 cellpadding=0 cellspacing=0 width="100%">
				<tr>
				   <td>
					<table id="TbWaferCFList" class="grid" border="0" cellpadding="1" cellspacing="1">
						<tbody>
						<tr>
							<th>Color Filter Material Num</th>
							<th>ProjectCode W version</th>
							<th>Color Filter Material Desc</th>
						</tr>
						</tbody>
					   <%if (smcList != null && smcList.size() > 0) 
					     {
							 int idx = 0;
							 String custShortName = "";
							 for(WaferColorFilterTo material : smcList) 
							 {
								if (material != null)
								{
									idx ++;
									String tdcss = "class=\"c" + idx % 2+"\"";
						%>
						<tr>
						<td <%=tdcss %>><%=material.getWaferCfMaterialNum()%>&nbsp;
							<input name='waferCfMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "WAFER_CF_MATERIAL_NUM") %>'>
						</td>
						<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(material, "PROJECT_CODE_W_VERSION") %>&nbsp;
							<input name='projectCodWVersion' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "PROJECT_CODE_W_VERSION") %>'>
						</td>
						<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(material, "DESCRIPTION") %>&nbsp;
							<input name='description' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "DESCRIPTION") %>'>
						</td>
						</tr>
						<%
							   }
						    }
						  }
						%>
				   </table>
				   </td>
				</tr>
				</table>
			</div>
			
			</td>
			</tr>
			</table>

			<table class="formErrorAndButton">
				<tr>
					<td>
					<div align="right">
					  <input name="okBtn" type="button" class="button" id="okBtn" value="OK" onclick="onExit()">
					  <!-- <input name="cancelBtn" type="button" class="button" id="cancelBtn" value="Cancel" onclick="window.close()">-->
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
			<td colspan="2"><img height="2" alt="" src="<%=cp %>/images/shadow-2.gif" width="585" border="0">
			</td>
		</tr>
	</tbody>
</table>

<script language="javascript">

function onAddDesc(){
		
	if ($('waferCfDesc').value==""){
		alert("DESCRIPTION must field");
		return;
	}
	
	getMaxCFMaterialNum();
	$('waferCfDesc').value="";
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
