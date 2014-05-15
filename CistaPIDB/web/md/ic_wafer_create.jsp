
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.IcWaferTo"%>
<%@ page import="com.cista.pidb.code.dao.FunctionParameterDao"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.md.dao.IcWaferDao"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.md.dao.ProjectDao"%>
<%@ page import="java.util.List"%>

<%
	//FCG1
	String HIMAX_SHUTTLE = "HimaxShuttle";
	FunctionParameterDao funcDao = new FunctionParameterDao(); 
	List<FunctionParameterTo> funcTapeOutListTo = funcDao.findValueList("IC_WAFER","TAPE_OUT_TYPE");
	ProjectDao projectDao = new ProjectDao();
	List<String> projectNameList = projectDao.findProjName();
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

	function doSave() {
		if ($F('materialDesc') == "") {
			setMessage("error", "Material Description is must field.");
			return;
		}

		if ($F('projCode') == "") {
			setMessage("error", "Project code is must field.");
			$('isRelease').value='0';
			return;
		}
		if ($F('bodyVer') == "") {
			setMessage("error", "Wafer Body Version is must field.");
			$('isRelease').value='0';
			return;
		}
		if ($('routingCsp').checked == true) {
			if ($('cspVersion').value ==""){
			
			setMessage("error", "CSP Version is must choose.");
			
			return;
			}
		}
		if ($('routingTsv').checked == true) {
			if ($('tsvVersion').value ==""){
			
			setMessage("error", "TSV Version is must choose.");
			
			return;
			}
		}
		//FCG1
		var tapeOutType = $('tapeOutType').value;
		var tapeOutProjName = $('tapeOutProjName').value;
	   var himaxShuttle = "<%=HIMAX_SHUTTLE%>";
	   if(tapeOutType == himaxShuttle && tapeOutProjName=='')
	   {
		   setMessage("error", "Himax shuttle must have project name.");
			return;
	   }

	   
		$('releaseBtn').disabled = true;
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_icwafer_exist.do',
			{
				method: 'get',
				parameters: 'projCode='+ $F('projCode') + '&bodyVer=' + $F('bodyVer') + '&optionVer=' + $F('optionVer'),
				onComplete: checkIcWaferExistComplete
			}
		);
	}	
	
	function checkIcWaferExistComplete(r) {
	var result = r.responseText;	
	if(result == "project_null") {
		setMessage("error", "Project with the Project Code is not found.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else if (result == "optionVer_null") {
		setMessage("error", "Wafer Option Version is null.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else if (result == "fab_null") {
		setMessage("error", "Project fab is null.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else  if(result == "wversion_outmax") {
		setMessage("error", "Length of Project Code w Version is beyond the max(50).");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else if(result == "variant_outmax") {
		setMessage("error", "Length of Variant is beyond the max(50).");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else if(result == "matnum_outmax") {
		setMessage("error", "Length of Material Number is beyond the max(50).");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else if(result=="true") {
		setMessage("error", "The ic wafer is already exist.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('isRelease').value='0';
	} else {
		var incompleted = "";
		if (incompleted != "") {
			if (confirm("The following required fields is incomplete, the ic wafer will be saved as draft:\r\n" + incompleted.substring(2))) {
				setMessage("error", "Saving ic wafer...");
				selectAllOptions($('assignTo'));
				selectAllOptions($('revisionItem'));
				
				$('createForm').action = $('createForm').action + "?m=save";
				$('createForm').submit();
			} else {
				$('releaseBtn').disabled = false;				
				$('saveBtn').disabled = false;
				$('resetBtn').disabled = false;	
				$('isRelease').value='0';			
				setMessage("error", "User cancel.");
			}
		} else {
			setMessage("error", "Saving ic wafer...");	
			selectAllOptions($('assignTo'));
			selectAllOptions($('revisionItem'));
												
			$('createForm').action = $('createForm').action + "?m=save";
			$('createForm').submit();
		}
	}
}

function doRelease() {
	$('isRelease').value='1';
	doSave();
}

function doClearDate() {
	$('tapeOutDate').value='';
}

function setMaskLayerCom() {
	setMessage("error", "Fetch Mask Mask Layer Combination ...");	
	new Ajax.Request(
			'<%=cp%>/ajax/auto_fetch_maskLayerCom.do',
			{
				method: 'get',
				parameters: 'projCode='+ $F('projCode'),
				onComplete: fetchMaskLayerComplete
			}
		);
}
function fetchMaskLayerComplete(r) {
	var result = r.responseText;
	
	if (result == "") {
	  	setMessage("error", "Can not fetch Mask Layer Combination.");		  	
	} else {
		setMessage("error","");
		setMessage("fetchedMaskLayer", result);	
	}
}

function createWithReference(){		
		new Ajax.Request(
			'<%=cp%>/ajax/fetch_projWVersion.do',
			{
				method: 'get',
				parameters: 'projCode='+ $F('projCode') + '&optionVer=' + $F('optionVer') + '&bodyVer=' +$F('bodyVer'),
				onComplete: fetchsetProjWVersionComplete
			}
		);
}

//Added Hank 2008/03/11 for get Project Code With Version

function setProjWVersion(inputField, columns, value) {
	$('projCode').value = value[0]["PROJ_CODE"];
	new Ajax.Request(
			'<%=cp%>/ajax/fetch_projWVersion.do',
			{
				method: 'get',
				parameters: 'projCode='+ $F('projCode') + '&optionVer=' + $F('optionVer') + '&bodyVer=' +$F('bodyVer'),
				onComplete: fetchsetProjWVersionComplete
			}
		);
}

function setProjWVersionNoParameter() {
	new Ajax.Request(
			'<%=cp%>/ajax/fetch_projWVersion.do',
			{
				method: 'get',
				parameters: 'projCode='+ $F('projCode') + '&optionVer=' + $F('optionVer') + '&bodyVer=' +$F('bodyVer'),
				onComplete: fetchsetProjWVersionComplete
			}
		);
}

function fetchsetProjWVersionComplete(r) {
	var result = r.responseText.split("|");
	if (result == "") {
	  		setMessage("error", "Can not fetch Project Code With Version.");		  	
		} else {
			if (result[1] != null && result[2] != null){
				$('projCodeWVersion').value = result[0]
				$('materialDesc').value = result[0]
				$('variant').value = result[1]
				$('materialNum').value = result[2]
			}else{
				$('projCodeWVersion').value = result[0]
				$('materialDesc').value = result[0]
			}
		}	
}

function selectFunctionParameter() {
	var target = "<%=cp%>/dialog/select_functionParameter.do?m=list&callback=selectFunctionParameterComplete&funName=IC_WAFER&funFieldName=REVISION_ITEM";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_functionParameter","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectFunctionParameterComplete(selectedProds) {
	//$('approveCust').length=0;
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('revisionItem'), prod[0], prod[0]);
		}
	}
}

function onSelectPSAll() {
	var cp = document.getElementById("routingCp");
	var polish = document.getElementById("routingPolish");
	var csp = document.getElementById("routingCsp");
	var tsv = document.getElementById("routingTsv");

	if(polish.checked  |csp.checked | tsv.checked ) {
		cp.checked = true;
	}
		
}

function onSelect2() {
	var polish = document.getElementById("routingPolish");	
	var csp = document.getElementById("routingCsp");
	if(!$('routingCp').checked) {
		if (polish.checked){
			polish.checked = false;
		}
		if (csp.checked){
			csp.checked= false;
		}
	}
	
}

function onSelect3() {
	var cf = document.getElementById("routingColorFilter");	
	
	if($('routingWaferCf').checked) {
		if (cf.checked){
			cf.checked = false;
		}
	}
}

function onSelect4() {
	var turnKey = document.getElementById("routingWaferCf");	
	
	if($('routingColorFilter').checked) 
	{
		if (turnKey.checked)
		{
			turnKey.checked = false;
		}		
	}
	
}
//FCG1
function tapeOutSelect()
{
	var tapeOutType = $('tapeOutType').value;
	var himaxShuttle = "<%=HIMAX_SHUTTLE%>";
	if(tapeOutType == himaxShuttle)
	{
		$('tapeOutProjName').disabled = false;
	}
	else
	{
		$('tapeOutProjName').disabled = true;
		$('tapeOutProjName').value ='';
	}
}
</script>

</head>
<%
	IcWaferTo ref = (IcWaferTo) request.getAttribute("ref");
	if ( ref != null ) {
%>

<body onLoad="createWithReference()">
<%}else{%>
<body >
<%}%>

<!-- Header---------------------------------------------------------------------
* 2010.05.05/FCG1 @Jere Huang - 新增Tape out type.
------------------------------------------------------------------------------->
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: Create IC Wafer </td>
  </tr>
</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/ic_wafer_create.do" method="post">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg">
					<%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
					&nbsp;</div>
					</td>
					<td>
					<div align="right">
					Created by 
					<input name="createBy" id="createBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY") %>">
					&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div class="erp_label">Material Number</div></th>
					  	<td width="25%" colspan="3">
					  	<input type="text"  class="text_protected" name="materialNum" id="materialNum"
					  	 value="" readonly></td>
					  	 <th width="12%" rowspan=3>Remark</th>
					 	<td width="30%" rowspan=3>
					 	<textarea id="remark" name="remark" cols="20" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>					  	
					</tr>
					<tr>
					   <th width="20%">Variant</th>
					  	<td width="25%" colspan="3">
					  	<input type="text" class="text_protected" name="variant" id="variant" 
					  	value="" readonly></td>

					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Project Code</div></th>
					  	<td width="25%" colspan="3">					  	
					  	<input type="text" class="text" name="projCode" id="projCode" readonly
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE") %>">
					  	<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
					  	 <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeSSBtn",
                                inputField:"projCode",                                
                                name:"ProjectCode",
                                autoSearch:false,
                                mode:0,
								callbackHandle:"setProjWVersion"
							});
						</script>					  	</td>

					</tr>
					<tr>  	
						<th width="20%"><span class="star">*</span> Wafer Body Version</th>
					  	<td width="25%" colspan="3">						
						<input type="text" class="text" name="bodyVer" id="bodyVer" maxlength="1"
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"BODY_VER") %>" onKeyUp="setProjWVersionNoParameter()"></td>
					  <th width="12%">Status</th>
					 	<td width="30%">
					 	<input type="text" class="text_protected" name="status" id="status" 
					 	value="" readonly>
					 	&nbsp;</td>						
				    </tr>
					<tr>
					<th width="20%">Wafer Option Version</th>
						<td width="25%" colspan="3">						
						<input type="text" class="text"  name="optionVer" id="optionVer" maxlength="1"
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"OPTION_VER") %>" onKeyUp="setProjWVersionNoParameter()"></td>
					  	
					  	 <th rowspan="6">Fetched Mask Layer Combination </th>
					 	 <td rowspan="6"><textarea id="fetchedMaskLayer" readonly cols="20" rows="7" class="text"></textarea></td>						
				    </tr>
					<tr>					
						<th width="20%"><div class="erp_label">Project Code w Version</div></th>
					  	<td width="25%" colspan="3">
						<input type="text" class="text_protected" name="projCodeWVersion" id="projCodeWVersion" 
						value="" readonly></td>
				    </tr>
					<tr>					
						<th width="20%"><div class="erp_label">Material Description</div></th>
					  	<td width="25%" colspan="3">
						<input type="text" class="text"  name="materialDesc" id="materialDesc" 
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"MATERIAL_DESC") %>"></td>
				    </tr>
					<tr>				 
						<th width="20%">Routing (WF)</th>
					 	<td width="25%" colspan="3">
					 	<%if( ref != null && ref.isRoutingWf()) {%>
						<input id="routingWf" name="routingWf" type="checkbox" value="Y" checked/>
						<%	} else { %>
						<input id="routingWf" name="routingWf" type="checkbox" value="Y"/>
						<%} %>						</td>
				    </tr>
					<tr>
						<th width="20%">Routing (BP)</th>
					  	<td width="25%" colspan="3">
					  	<%if(ref != null && ref.isRoutingBp()) { %>
					  	<input id="routingBp" name="routingBp" type="checkbox" value="Y" checked/>
					  	<%}else { %>
					    <input id="routingBp" name="routingBp" type="checkbox" value="Y"/>
					    <%} %>
						<input type="text" class="text_protected"  name="bp" id="bp" 
									value="" readOnly>					    
					    </td>
					</tr>
					<tr>
						<th width="20%">Routing (CP)</th>
					  	<td width="25%" colspan="3">
					  	<%if(ref != null && ref.isRoutingCp()) { %>
					  	 <input id="routingCp" name="routingCp" type="checkbox" value="Y" checked onClick="onSelect2()" />
					  	 <%} else { %>
					    <input id="routingCp" name="routingCp" type="checkbox" value="Y" onClick="onSelect2()" />
					    <%} %>
						<input type="text" class="text_protected"  name="cp" id="cp" 
									value="" readOnly>						    
					    </td>
					</tr>
					<tr>
						<th width="20%">Routing Polish</th>
						<td width="25%" colspan="3">
					  	<%if(ref != null && ref.isRoutingPolish()) { %>
					  	 <input id="routingPolish" name="routingPolish" type="checkbox" value="Y" checked onClick="onSelectPSAll()"/>
					  	 <%} else { %>
					    <input id="routingPolish" name="routingPolish" type="checkbox" value="Y" onClick="onSelectPSAll()"/>
					    <%} %>
						<input type="text" class="text_protected"  name="cpPolishMaterialNum" 	
							id="cpPolishMaterialNum" value="" readOnly>
					    </td>
						<th width="12%">MP_Status</th>
					  	<td width="30%">
					  	<input id="mpStatus" name="mpStatus" class="text_protected" type="text"  
					  	value="" readonly></td>
					</tr>
					<tr>
						<th width="20%">DS</th>
					  	<td width="25%" colspan="3">
					  	<input id="ds" name="ds" type="text" class="text_protected" 
					  	readonly value=""></td>
						<th  rowspan="2"><div>AssignTo</div></th>
					  	<td  rowspan="2">
					  	<table border="0" cellspace="0" cellpadding="0" margin="0">
					  	<tr>
					  	<td rowspan="2">
					  	<select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    	
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO") != null && BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO").length() > 0) {
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
					  	</select></td>
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
							</script>							</td>
							<td><input id="userBtn" type="button" class="button" value="Remove" onClick="removeSelectedOptions($('assignTo'))"></td>
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
						</table>					  	</td>
						
					 </tr>
					<tr>
						<th width="20%">Routing Color Filter</th>
						<td width="25%" colspan="3">
					  	<%if(ref != null && ref.isRoutingColorFilter()) { %>
					  	 <input id="routingColorFilter" name="routingColorFilter" type="checkbox" value="Y" checked onClick="onSelect4()" />
					  	 <%} else { %>
					    <input id="routingColorFilter" name="routingColorFilter" type="checkbox" value="Y" onClick="onSelect4()" />
					    <%} %>
					    </td>
						 
					</tr>
					<tr>
						<th width="20%">Routing TurnKey(Wafer + ColorFilter)</th>
						<td width="25%" colspan="3">
					  	<%if(ref != null && ref.isRoutingWaferCf()) { %>
					  	 <input id="routingWaferCf" name="routingWaferCf" type="checkbox" value="Y" checked onClick="onSelect3()" />
					  	 <%} else { %>
					    <input id="routingWaferCf" name="routingWaferCf" type="checkbox" value="Y"  onClick="onSelect3()" />
					    <%} %>
					    </td>
						<th width="12%">&nbsp;</th>
						<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Routing CSP</th>
						<td width="5%">
					  	<%if(ref != null && ref.isRoutingCsp()) { %>
					  	 <input id="routingCsp" name="routingCsp" type="checkbox" value="Y" checked onClick="onSelectPSAll()" />
					  	 <%} else { %>
					    <input id="routingCsp" name="routingCsp" type="checkbox" value="Y" onClick="onSelectPSAll()" />
					    <%} %>
					    </td>
						<th width="9%">CSP Version</th>
						<td width="13%"> <select class="select_w130" name="cspVersion" id="cspVersion">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> versionList = (List<FunctionParameterTo>) request.getAttribute("cspVersionList");
									for (FunctionParameterTo version : versionList) {
										String v = version.getFieldValue();
										%>
								<option value="<%=v %>"><%=version.getFieldShowName() %></option>
										<%
									}
								%>
							</select></td>
						<th width="20%">&nbsp;</th>
						<td width="10%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">Routing TSV</th>
						<td width="5%">
					  	<%if(ref != null && ref.isRoutingTsv()) { %>
					  		<input id="routingTsv" name="routingTsv" type="checkbox" value="Y" checked onClick="onSelectPSAll()" />
					  	<%} else { %>
							<input id="routingTsv" name="routingTsv" type="checkbox" value="Y" onClick="onSelectPSAll()" />
					    <%} %>
					    </td>
						<th width="9%">TSV Version</th>
						<td width="13%"> <select class="select_w130" name="tsvVersion" id="tsvVersion">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> tsvVersionList = (List<FunctionParameterTo>) request.getAttribute("tsvVersionList");
									for (FunctionParameterTo tsvVersion : tsvVersionList) {
										String v = tsvVersion.getFieldValue();
										%>
								<option value="<%=v %>"><%=tsvVersion.getFieldShowName() %></option>
										<%
									}
								%>
							</select></td>
						<th width="20%">&nbsp;</th>
						<td width="10%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%" rowspan=2>Mask Layer Combination</th>
					  	<td width="25%" rowspan=2 colspan="3">
					  		<textarea id="maskLayerCom" name="maskLayerCom" cols="20" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "MASK_LAYER_COM") %></textarea>
							<input type="button" value="Fetch" class="button" id="fetchMask" name="fetchMask" onClick="setMaskLayerCom()">
		         		</td>
						<th width="12%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>						
					<tr>
						<th width="12%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					
					<tr>
					 	 <th width="20%">
					 	    Tape out type
					 	 </th>     
					 	 <td width="13%"><br>
					 		  <select class="select_w130" name="tapeOutType" id="tapeOutType" onchange="tapeOutSelect()">
							  <option value="">--Select--</option>
					 		   <%
					 			for(FunctionParameterTo objTo :funcTapeOutListTo)
					 			{	
					 			%>
					 			   <option value="<%=objTo.getFieldValue()%>">  <%=objTo.getFieldShowName()%> </option>
					 			<%						 		
					 			}
					 			%>
					 		  </select>   
					 	 </td>                                                              
						 <th width="10%" readonly>Project Name
						 </th>
						 <td width="13%"><br>
						    <select class="select_w130" name="tapeOutProjName" id="tapeOutProjName" disabled >
							   <option value="">--Select--</option>
								<%								
								for (String strProjectName : projectNameList) 
								{									
								%>
								   <option value="<%=strProjectName%>" ><%=strProjectName%></option>
								<%
								}
								%>
							 </select>
						 </td>
						 <th>&nbsp;</th>
					    <td>&nbsp;</td>
					</tr>	
					
					<tr>
					 	<th width="20%">Fab device ID</th>
					  	<td width="25%" colspan="3">
							<input type="text" class="text"  name="fabDeviceId" id="fabDeviceId" value="<%=BeanHelper.getHtmlValueByColumn(ref,"FAB_DEVICE_ID") %>">
						</td>
						<th width="12%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					 			 
					<tr> <th width="20%">Tape Out Date</th>
					  	<td width="25%" colspan="3">
						   <input class="text"  name="tapeOutDate" id="tapeOutDate" readonly	value="<%=BeanHelper.getHtmlValueByColumn(ref,"TAPE_OUT_DATE","yyyy/MM/dd") %>" >
					      <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="tapeOutDateBtn">
							<script type="text/javascript">
								Calendar.setup({
									inputField:"tapeOutDate",
									ifFormat:"%Y/%m/%d",
									button:"tapeOutDateBtn"
								});
							</script>&nbsp;
						   <input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear"	onClick="doClearDate()">
						
						</td>
					  	<th>&nbsp;</th>
					   <td>&nbsp;</td>							
					</tr>
					<tr>
					   <th width="20%">Mask Id</th>
					  	<td width="25%" colspan="3">
					  	<input type="text" class="text"  name="maskId" id="maskId"
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"MASK_ID") %>">					  	</td>
					  	 <th>&nbsp;</th>
					     <td>&nbsp;</td>	
					 </tr>
					 <tr>
					  	<th width="20%" rowspan="3" style="vertical-align:top">Revision Item</th>
						<td width="25%" rowspan="3" style="vertical-align:top" colspan="3"><input name="button" type="button" class="button" onClick="selectFunctionParameter()" value="Add">
                          <input name="button3" type="button" class="button" onClick="removeSelectedOptions($('revisionItem'))" value="Remove">
                          <br>
                          <select name="revisionItem" size="3" multiple class="select_w130" id="revisionItem">
							<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "REVISION_ITEM") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "REVISION_ITEM");
					    			String[] list = listStr.split(",");
					    			FunctionParameterDao functionParameterDao = new FunctionParameterDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    FunctionParameterTo to = functionParameterDao.findByFieldValue(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getFieldValue() %>"><%=to.getFieldValue()%></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>
                          </select></td>
					  	 <th>&nbsp;</th>
					     <td>&nbsp;</td>
					</tr>
					<tr>
					  	<th>&nbsp;</th>
					    <td>&nbsp;</td>
					 </tr> 
					 <tr>
						 <th>&nbsp;</th>
					    <td>&nbsp;</td>
					</tr>
					<tr>
					  	 <th width="20%">Mask House</th>
						 <td width="25%" colspan="3"><input type="text" cols="80%" readonly name="maskHouse" id="maskHouse" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_HOUSE") %>" class='text_200'> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="MaskHouseFlowSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"MaskHouseFlowSSBtn",
                                inputField:"maskHouse",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='MASK_HOUSE' and fun_name='IC_WAFER'",
								orderBy:"ITEM",
                                title:"MASK_HOUSE",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"MaskHouseCallback"
							});

							function MaskHouseCallback(inputField, columns, value) {
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
						 <th>&nbsp;</th>
						 <td>&nbsp;</td>
					 </tr>
					 <tr>
						<th width="20%">New Mask Number</th>
						 <td width="25%" colspan="3">
						 <select class="select_w130" name="maskNum" id="maskNum">
							<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> maskNumList = (List<FunctionParameterTo>) request.getAttribute("maskNumList");
									for (FunctionParameterTo maskNum : maskNumList) {
										String v = maskNum.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "MASK_NUM"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=maskNum.getFieldShowName() %></option>
										<%
									}
								%>
						</select>
						</td>			
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
					<input type="hidden" id="isRelease" name="isRelease" value="0">
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
					
					<input name="releaseBtn" type="button" class="erp_button" id="releaseBtn" value="Release To ERP" 
						onClick="doRelease()">
					 &nbsp; <input name="saveBtn" type="button" class="button" id="saveBtn" value="Save" onClick="doSave()">
					  &nbsp;
					  	   <%
						   }     
						%>
					  
					  <input name="resetBtn" type="reset" class="button" id="resetBtn" value="Reset">
					</div>
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
