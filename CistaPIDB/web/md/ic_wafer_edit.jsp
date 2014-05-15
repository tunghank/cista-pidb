<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.dao.IcWaferDao"%>
<%@ page import="com.cista.pidb.md.to.IcWaferTo" %>
<%@ page import="com.cista.pidb.code.dao.FunctionParameterDao"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.md.to.CpMaterialTo"%>
<%@ page import="com.cista.pidb.md.dao.CpPolishMaterialDao"%>
<%@ page import="com.cista.pidb.md.to.CpPolishMaterialTo"%>
<%@ page import="com.cista.pidb.md.dao.ColorFilterMaterialDao"%>
<%@ page import="com.cista.pidb.md.to.ColorFilterMaterialTo"%>
<%@ page import="com.cista.pidb.md.dao.WaferColorFilterDao"%>
<%@ page import="com.cista.pidb.md.to.WaferColorFilterTo"%>
<%@ page import="com.cista.pidb.md.dao.CpCspMaterialDao"%>
<%@ page import="com.cista.pidb.md.to.CpCspMaterialTo"%>
<%@ page import="com.cista.pidb.md.to.CpTsvMaterialTo"%>
<%@ page import="com.cista.pidb.md.dao.ProjectDao"%>
<%@ page import="java.util.List"%>

<%
	List<CpMaterialTo> materialList = (List)request.getAttribute("materialList");
	List<CpPolishMaterialTo> polishList = (List)request.getAttribute("polishList");
	List<ColorFilterMaterialTo> cfList = (List)request.getAttribute("cfList");
	List<WaferColorFilterTo> waferCFList = (List)request.getAttribute("waferCFList");
	List<CpCspMaterialTo> cspList = (List)request.getAttribute("cspMaterialList");
	List<CpTsvMaterialTo> tsvMaterialList = (List)request.getAttribute("tsvMaterialList");
	String cspVersion = (String)request.getAttribute("cspVersion");
	RoleDao roleDao = new RoleDao();
	UserTo currentUser = PIDBContext.getLoginUser(request);
	List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());
	String isGuest="No";
	//FCG4 取的tape out type list 資料
	FunctionParameterDao funcDao = new FunctionParameterDao(); 
	List<FunctionParameterTo> funcTapeOutListTo = funcDao.findValueList("IC_WAFER","TAPE_OUT_TYPE");
	ProjectDao projectDao = new ProjectDao();
	List<String> projectNameList = projectDao.findProjName();
	String HIMAX_SHUTTLE = "HimaxShuttle";
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
			return;
		}
		if ($F('bodyVer') == "") {
			setMessage("error", "Wafer Body Version is must field.");
			return;
		}

		//FCG4
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
		
		var incompleted = "";
		if (incompleted != "") {
			if (confirm("The following required fields is incomplete, the ic wafer will be saved as draft:\r\n" + incompleted.substring(2))) {
				setMessage("error", "Updating ic wafer...");
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
			setMessage("error", "Updating ic wafer...");	
			selectAllOptions($('assignTo'));
			selectAllOptions($('revisionItem'));

			//FCG1
			$('routingColorFilter').disabled = false;
			$('routingPolish').disabled = false;
			$('routingWaferCf').disabled = false;
					
			$('createForm').action = $('createForm').action + "?m=save";
			$('createForm').submit();
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
	setMessage("error", "Fetchs Mask Layer Combination ...");	
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

//Added Hank 2008/01/09 for getMaxVariantCpMaterial 
function getMaxVariantCpMaterial() {
	new Ajax.Request( 
	'<%=cp%>/ajax/fetch_maxVariantCpMateriaNum.do?materialNum='+$('materialNum').value,
	{
		method: 'post',
		parameters: 'projCodeWVersion=' + $('projCodeWVersion').value,
		onComplete: fetchMaxVariantCpMaterialComplete
	} );
}

function fetchMaxVariantCpMaterialComplete(r) {
	var returnValue = r.responseText.split("|");
	 $('cpWithVariant').value = returnValue;
	 
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

//Add Form Frank 2009/01/20
function modifyCpMaterial() {

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Project Code W Verison is must field.");
		return;
	}
	
	var target ="<%=cp%>/dialog/select_cpMaterial.do?m=list&projCodeWVersion="+$F('projCodeWVersion')+"&remark="+$F('remark')+"&materialNum="+$F('materialNum');

	var width = 800;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cpMaterial","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
	
	//window.location.reload();
}

function modifyCpPolish() {

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Project Code W Verison is must field.");
		return;
	}

	var target ="<%=cp%>/dialog/select_cpPolishMaterial.do?m=list&projCodeWVersion="+$F('projCodeWVersion')+"&remark="+$F('remark')+'&status='+$F('mpStatus');

	var width = 900;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cpPolishMaterial","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

// Add Color Filter
function modifyCF() {

	
	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Project Code W Verison is must field.");
		return;
	}
	//FCG2 多加判斷是否有選, 有選才可加
	if ($('routingColorFilter').checked == false) 
	{
		setMessage("error", "Routing Color Filter must be checked.");
		return;
	}
	
	//FCG2 material, mp_status傳入
	var target ="<%=cp%>/dialog/select_colorFilterMaterial.do?m=list&projCodeWVersion="+$F('projCodeWVersion')+"&remark="+$F('remark')+ "&status="+$F('status')+ "&materialNum="+$('materialNum').value + "&mpStatus="+$F('mpStatus');

	var width = 900;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_colorFilterMaterial","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
	
}

// Add Color Filter + Wafer
function modifyWaferCF() {

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Project Code W Verison is must field.");
		return;
	}

	//FCG2 多加判斷是否有選, 有選才可加
	if ($('routingWaferCf').checked == false) 
	{
		setMessage("error", "Routing TurnKey(Wafer + ColorFilter) must be checked.");
		return;
	}
	
	//FCG2 material傳入
	var target ="<%=cp%>/dialog/select_waferColorFilter.do?m=list&projCodeWVersion="+$F('projCodeWVersion')+"&remark="+$F('remark')+"&mpStatus="+$F('mpStatus')+ "&materialNum="+$('materialNum').value;

	var width = 900;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_waferColorFilter","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

// Add Cp Csp 
function modifyCpCsp() {
	if ($('routingCsp').checked == false){
		alert("Routing CSP is must Checked.");
		return;
	}
	if ($F('projCodeWVersion') == "") {
		alert("Project Code W Verison is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_cpCspMaterialNum.do?m=list&projCodeWVersion="+$F('projCodeWVersion');

	var width = 900;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cpCspMaterialNum","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

// Add Cp Tsv 
function modifyCpTsv() {
	
	if ($('routingTsv').checked == false){
		alert("Routing Tsv is must Checked.");
		return;
	}
	if ($F('projCodeWVersion') == "") {
		alert("Project Code W Verison is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_cpTsvMaterialNum.do?m=list&projCodeWVersion="+$F('projCodeWVersion');

	var width = 900;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_cpTsvMaterialNum","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
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
	//FCG2
	/*
	if($('routingWaferCf').checked) {
		if (cf.checked){
			cf.checked = false;
		}
	}
	*/
}

function onSelect4() {
	var turnKey = document.getElementById("routingWaferCf");	

	//FCG2
	/*
	if($('routingColorFilter').checked) {
		if (turnKey.checked){
			turnKey.checked = false;
		}		
	}
	*/
}

function onSelect5() {
	var csp = document.getElementById("routingCsp");		
	if(!$('routingCp').checked) {
		if (csp.checked){
			csp.checked = false;
		}
	}
	
}
//FCG4
function tapeOutSelect()
{
	var tapeOutType = $('tapeOutType').value;
	var himaxShuttle = "<%=HIMAX_SHUTTLE%>";
	//alert(himaxShuttle);
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

<% 
	IcWaferTo ref = (IcWaferTo)request.getAttribute("icWaferTo");
	//FCG4
	String showProjecNameTag = "";						   
   String strTapeOutType= BeanHelper.getHtmlValueByColumn(ref, "TAPE_OUT_TYPE");
   String strTapeOutProjName= BeanHelper.getHtmlValueByColumn(ref, "TAPE_OUT_PROJ_NAME");
   if(!HIMAX_SHUTTLE.equals(strTapeOutType))
   {
	   showProjecNameTag = "disabled";
   }
%>
</head>
<body>
<!-- Header---------------------------------------------------------------------
* 2010.02.25/FCG1 @Jere Huang - 修改routing polish disable, 及Save時未將值正確更新.
* 2010.03.22/FCG2 @Jere Huang - 修改color filter傳入mp_status, 及未傳入material錯誤, 拿掉color filter,routing turnkey disable .
* 2010.04.21/FCG3 @Jere Huang - 新增cp material remark
* 2010.05.05/FCG4 @Jere Huang - 新增Tape out type.
----------------------------------------------------------------------------- -->
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			  <tr>
			    <td class="pageTitle">Master Data :: Modify IC Wafer </td>
			  </tr>
			</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/ic_wafer_edit.do" method="post">
			<input type="hidden" id="bp" name="bp" value="<%=BeanHelper.getHtmlValueByColumn(ref, "BP") %>">
			<input type="hidden" id="cp" name="cp" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CP") %>">
			<input type="hidden" id="ds" name="ds" value="<%=BeanHelper.getHtmlValueByColumn(ref, "DS") %>">			
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
					  	<td width="30%" colspan="3">
					  	<input type="text" class="text_protected" name="materialNum" id="materialNum" 
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"MATERIAL_NUM") %>" readonly></td>
					  	 <th width="20%" rowspan=3>Remark</th>
					 	<td width="30%" rowspan=3>
					 	<textarea id="remark" name="remark" cols="20" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>	
					 </tr>
					 <tr>
					  	<th width="20%">Variant</th>
					  	<td width="30%" colspan="3">
					  	<input type="text"  class="text_protected" name="variant" id="variant" 
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"VARIANT") %>" readonly>					  	</td>
					</tr>
					<tr><th width="20%"><div class="erp_label"><span class="star">*</span> Project Code</div></th>
					  	<td width="30%" colspan="3">
					  	<input type="text" class="text_protected" name="projCode" id="projCode" 
					  	value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE") %>" readonly>					  	</td>

					</tr>
					 <tr>					
						<th width="20%"><span class="star">*</span> Wafer Body Version</th>
					  	<td width="30%" colspan="3">
						<input type="text" class="text_protected" name="bodyVer" id="bodyVer" 
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"BODY_VER") %>" readonly>						</td>
					  	 <th width="20%">Status</th>
					 	<td width="30%">
				 	<input type="text"  class="text_protected" name="status" id="status" 
					 	value="<%=BeanHelper.getHtmlValueByColumn(ref,"STATUS") %>" readonly>	</td>						
				    </tr>
					<tr><th width="20%">Wafer Option Version</th>
						<td width="30%" colspan="3">
						<input type="text" class="text_protected" name="optionVer" id="optionVer" 
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"OPTION_VER") %>" readonly>						</td>	
					  	<th rowspan="6">Fetched Mask Layer Combination </th>
					 <td rowspan="6"><textarea id="fetchedMaskLayer" readonly cols="20" rows="7" class="text"></textarea></td>						
				    </tr>
					 <tr>			
						<th width="20%"><div class="erp_label">Project Code w Version</div></th>
					  	<td width="30%" colspan="3">
						<input type="text" class="text_protected" name="projCodeWVersion" id="projCodeWVersion" 
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE_W_VERSION") %>" readonly>						</td>
				    </tr>
					<tr>					
						<th width="20%"><div class="erp_label">Material Description</div></th>
					  	<td width="30%" colspan="3">
						<input type="text" class="text"  name="materialDesc" id="materialDesc" 
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"MATERIAL_DESC") %>"></td>
				    </tr>
					<tr>				 
						<th width="20%">Routing (WF)</th>
					 	<td width="30%" colspan="3">
					 		<%if (ref.isRoutingWf() == true) { %>
							<input id="routingWf" name="routingWf" type="checkbox" checked value="Y">
							<%} else { %>
							<input id="routingWf" name="routingWf" type="checkbox"  value="Y">
							<%} %>							</td>
				    </tr>
					 <tr>
						<th width="20%">Routing (BP)</th>
					  	<td width="30%" colspan="3">
					  		<%if (ref.isRoutingBp() == true) { %>
							<input id="routingBp" name="routingBp" type="checkbox" checked value="Y">
							<%} else { %>
							<input id="routingBp" name="routingBp" type="checkbox" value="Y">
							<%} %>							
						<input type="text" class="text_protected"  name="bp" id="bp" 
									value="<%=BeanHelper.getHtmlValueByColumn(ref,"BP") %>" readOnly>								
							</td>
					</tr>
					<tr>
						<th width="20%">Routing (CP)</th>
					  	<td width="30%" colspan="3">
					  		<%if(ref.isRoutingCp() == true) { %>
							<input id="routingCp" name="routingCp" type="checkbox"  checked value="Y" onClick="onSelect2()">
							<%} else { %>
							<input id="routingCp" name="routingCp" type="checkbox"  value="Y" onClick="onSelect2()">
							<%} %>							
						<input type="text" class="text_protected"  name="cp" id="cp" 
									value="<%=BeanHelper.getHtmlValueByColumn(ref,"CP") %>" readOnly>								
							</td>
					</tr>
					 <tr>
						<th width="20%">DS</th>
					  	<td width="30%" colspan="3">
					  	<input id="ds" name="ds" type="text" class="text_protected" 
					  	readonly value="<%=BeanHelper.getHtmlValueByColumn(ref,"DS") %>">					  	</td>					 
						<th width="20%">MP_Status</th>
					  	<td width="30%">
					  	<input id="mpStatus" name="mpStatus" type="text" class="text_protected" id="MP_Status" 
					  	readonly value="<%=BeanHelper.getHtmlValueByColumn(ref,"MP_STATUS") %>">					  	</td>
					 </tr>
					 <tr>
						<th width="20%">Routing Polish</th>
						<td width="30%" readonly colspan="3"><br>
					  	<%if(ref != null && ref.isRoutingPolish()) { %>
					  		  <%//FCG1 %>
					  	     <input id="routingPolish" name="routingPolish" type="checkbox" value="Y" checked onClick="onSelectPSAll()" disabled="true" / >
					  	 <%} else { %>
					        <input id="routingPolish" name="routingPolish"  type="checkbox" value="Y" onClick="onSelectPSAll()" disabled="true" />
					    <%} %>
						<input type="text" class="text_protected"  name="cpPolishMaterialNum" id="cpPolishMaterialNum" value="<%=BeanHelper.getHtmlValueByColumn(ref,"CP_POLISH_MATERIAL_NUM") %>" readOnly>
					    </td>
						<th>CP Polish Material Create</th>
						 <td>
						 <% //Added on 9/18
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						 <input name="btnModifyPolish" id="btnModifyPolish" type="button" class="button" onClick="modifyCpPolish()" value="Polish Modify">
						 <%
							}
						   %>
						 <P>
							<table border=0 cellpadding=0 cellspacing=0 width="100%">
						<tr><td>
						<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
							<tbody>
							<tr>
								<th>Cp Polish Material Num</th>
								<th>ProjectCode W version</th>
								<th>Cp Polish Material Desc</th>
							</tr>
							</tbody>
						<%if (polishList != null && polishList.size() > 0) {%>
							<%
								int idx = 0;
								for(CpPolishMaterialTo polish : polishList) {
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
						<%}%>
						</table>
				</td>
			</tr>
			</table>
							</td>
					</tr>
					<tr>
						<th width="20%">Routing Color Filter</th>
						<td width="30%" readonly colspan="3"><br>
					  	<%if(ref != null && ref.isRoutingColorFilter()) { %>
					  	   <% //FCG1, FCG2 %>
					  	 	<input id="routingColorFilter" name="routingColorFilter" type="checkbox" value="Y" checked   onClick="onSelect4()" / > 																		  	
					  	 <%} else { %>
					       <input id="routingColorFilter" name="routingColorFilter"  type="checkbox" value="Y"  onClick="onSelect4()" />					       
					    <%} %>
					   </td>
						<th>Color Filter Material Create</th>
						 <td>
							  <% //Added on 9/18
							  if (checkedRoles != null) 
							  {
								  for (RoleTo roleTo : checkedRoles) 
								  {
									  if (roleTo.getRoleName().equals("Guest") )  
									  {
										  isGuest="Yes";
									  } 
								  }
							  } 							  
							  %>
							 <P>
							 <table border=0 cellpadding=0 cellspacing=0 width="100%">
							 <tr>
							    <td>
							    <table id="TbColorFilterList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>Color Filter Material Num</th>
										<th>ProjectCode W version</th>
										<th>Color Filter Material Desc</th>
									</tr>
									</tbody>
									<%if (cfList != null && cfList.size() > 0) 
									  {
											int idx = 0;
											for(ColorFilterMaterialTo cf : cfList) 
											{
												if (cf != null)
												{
													idx ++;
													String tdcss = "class=\"c" + idx % 2+"\"";
								   %>
									<tr>
									<td <%=tdcss %>><%=cf.getColorFilterMaterialNum()%>&nbsp;
										<input name='cpPolishMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cf, "COLOR_FILTER_MATERIAL_NUM") %>'>
									</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(cf, "PROJECT_CODE_W_VERSION") %>&nbsp;
										<input name='projectCodeWVersion' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cf, "PROJECT_CODE_W_VERSION") %>'>
									</td>
									<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(cf, "DESCRIPTION") %>&nbsp;
										<input name='description' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cf, "DESCRIPTION") %>'>
									</td>
									</tr>
								   <%
									         }
								         }
									   }%>
							    </table>
							    </td>
					       </tr>
				          </table>
					    </td>
					</tr>
					<tr>
						<th width="20%">Routing TurnKey(Wafer + ColorFilter)</th>
						<td width="30%" readonly colspan="3"><br>
					  	<%if(ref != null && ref.isRoutingWaferCf()) { %>
					  		  <% //FCG1, FCG2 %>
					  	     <input id="routingWaferCf" name="routingWaferCf" type="checkbox" value="Y" checked onClick="onSelect3()" / >
					  	 <%} else { %>
					        <input id="routingWaferCf" name="routingWaferCf"  type="checkbox" value="Y"  onClick="onSelect3()" />
					    <%} %>
					    </td>
						<th>Wafer Color Filter Create</th>
						 <td>
						  <% //Added on 9/18
							if (checkedRoles != null) 
							{
								for (RoleTo roleTo : checkedRoles) 
								{
								   if (roleTo.getRoleName().equals("Guest") )  
								   {
									  isGuest="Yes";
								   } 
								}
						   } 
						   //FCG2
							if ( isGuest.equals("No") )  
							{
						   %>
						 		<input name="btnModifyCF" id="btnModifyCF" type="button" class="button" onClick="modifyWaferCF()" value="Wafer CF Modify">
						 <%}%>
						 <P>
						 <table border=0 cellpadding=0 cellspacing=0 width="100%">
						   <tr>
						   <td>
							<table id="TbWaferCFList" class="grid" border="0" cellpadding="1" cellspacing="1">
								<tbody>
								<tr>
									<th>Wafer Color Filter Num</th>
									<th>ProjectCode W version</th>
									<th>Wafer Color Filter Desc</th>
								</tr>
								</tbody>
								<%if (waferCFList != null && waferCFList.size() > 0) 
								{
									int idx = 0;
									for(WaferColorFilterTo waferCf : waferCFList) 
									{
										if (waferCf != null)
										{
											idx ++;
											String tdcss = "class=\"c" + idx % 2+"\"";
							   %>
								<tr>
								<td <%=tdcss %>><%=waferCf.getWaferCfMaterialNum()%>&nbsp;
									<input name='waferCfMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(waferCf, "WAFER_CF_MATERIAL_NUM") %>'>
								</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(waferCf, "PROJECT_CODE_W_VERSION") %>&nbsp;
									<input name='projectCodWVersion' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(waferCf, "PROJECT_CODE_W_VERSION") %>'>
								</td>
								<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(waferCf, "DESCRIPTION") %>&nbsp;
									<input name='description' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(waferCf, "DESCRIPTION") %>'>
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
					    </td>
					</tr>
					
					<tr>
						<th width="20%">Routing CSP</th>
						<td width="10%" readonly colspan="3"><br>
						  	<%if(ref != null && ref.isRoutingCsp()) { %>
						  	  <input id="routingCsp" name="routingCsp" type="checkbox" value="Y" checked onClick="onSelectPSAll()" / >
						  	<%} else { %>
						     <input id="routingCsp" name="routingCsp"  type="checkbox" value="Y" onClick="onSelectPSAll()" />
						   <%} %>
					   </td>

						<th>CSP Material Create</th>
						<td>
							<% //Added on 9/18
						   if (checkedRoles != null) 
						   {
								for (RoleTo roleTo : checkedRoles) 
								{
								   if (roleTo.getRoleName().equals("Guest") )  
								   {
									 isGuest="Yes";
								   } 
								}
							} 
							if ( isGuest.equals("No"))  
							{
							%>
						     <input name="btnModifyCsp" id="btnModifyCsp" type="button" class="button" onClick="modifyCpCsp()" value="CSP Modify">
						   <%
						   }
						   %>
						   <P>
							<table border=0 cellpadding=0 cellspacing=0 width="100%">
							<tr>
								<td>
								<table id="TbCpcspList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									  <tr>
									     <th>CSP MaterialNum</th>
										  <th>ProjectCode W version</th>
										  <th>CP CSP Desc</th>
									  </tr>
									</tbody>
						 		   <%
						 		   if (cspList != null && cspList.size() > 0) 
						    		{
										int idx = 0;
										for(CpCspMaterialTo cspMaterial : cspList) 
								 		{
										  if (cspMaterial != null)
										  {
										    idx ++;
										    String tdcss = "class=\"c" + idx % 2+"\"";
						  			%>
										    <tr>
											   <td <%=tdcss %>><%=cspMaterial.getCpCspMaterialNum()%>&nbsp;
												   <input name='cpCspMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cspMaterial, "CP_CSP_MATERIAL_NUM") %>'>
											   </td>
									  			<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(cspMaterial, "PROJECT_CODE_W_VERSION") %>&nbsp;
										  			<input name='projectCodWVersion' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cspMaterial, "PROJECT_CODE_W_VERSION") %>'>
									  			</td>
									  		   <td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(cspMaterial, "DESCRIPTION") %>&nbsp;
										  			 <input name='description' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cspMaterial, "DESCRIPTION") %>'>
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
						</td>
					</tr>
					<!-- TSV -->
					<tr>
						<th width="20%">Routing TSV</th>
						<td width="10%" readonly colspan="3"><br>
						  	<%if(ref != null && ref.isRoutingTsv()) { %>
						  	  <input id="routingTsv" name="routingTsv" type="checkbox" value="Y" checked onClick="onSelectPSAll()" / >
						  	<%} else { %>
						     <input id="routingTsv" name="routingTsv"  type="checkbox" value="Y" onClick="onSelectPSAll()" />
						   <%} %>
					   </td>

						<th>TSV Material Create</th>
						<td>
							<% //Added on 9/18
						   if (checkedRoles != null) 
						   {
								for (RoleTo roleTo : checkedRoles) 
								{
								   if (roleTo.getRoleName().equals("Guest") )  
								   {
									 isGuest="Yes";
								   } 
								}
							} 
							if ( isGuest.equals("No"))  
							{
							%>
						     <input name="btnModifyTsv" id="btnModifyTsv" type="button" class="button" onClick="modifyCpTsv()" value="TSV Modify">
						   <%
						   }
						   %>
						   <P>
							<table border=0 cellpadding=0 cellspacing=0 width="100%">
							<tr>
								<td>
								<table id="TbCpTsvList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									  <tr>
									     <th>TSV MaterialNum</th>
										  <th>ProjectCode W version</th>
										  <th>CP TSV Desc</th>
									  </tr>
									</tbody>
						 		   <%
						 		   if (tsvMaterialList != null && tsvMaterialList.size() > 0) 
						    		{
										int idx = 0;
										for(CpTsvMaterialTo tsvMaterial : tsvMaterialList) 
								 		{
										  if (tsvMaterial != null)
										  {
										    idx ++;
										    String tdcss = "class=\"c" + idx % 2+"\"";
						  			%>
										    <tr>
											   <td <%=tdcss %>>
											   <%=tsvMaterial.getCpTsvMaterialNum()%>&nbsp;

											   </td>
									  			<td <%=tdcss %>>
												<%=tsvMaterial.getProjectCodeWVersion()%>&nbsp;
									  			</td>
									  		   <td <%=tdcss %>>
											   <%=tsvMaterial.getProjectCodeWVersion()%>&nbsp;
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
						</td>
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
					 				if(objTo.getFieldValue().equals(strTapeOutType) )
					 				{
					 			%>
					 				   <option value="<%=objTo.getFieldValue()%>" selected>  <%=objTo.getFieldShowName()%> </option>
					 			<% }
					 				else
					 				{
					 			%>
					 				   <option value="<%=objTo.getFieldValue()%>">  <%=objTo.getFieldShowName()%> </option>
					 			<%	
					 				}
					 			}
					 			%>
					 		</select>   
					 	</td>                                                              
						<th width="10%" readonly>Project Name</th>
						<td width="13%"><br>
						    <select class="select_w130" name="tapeOutProjName" id="tapeOutProjName" <%=showProjecNameTag%> >
								<option value="">--Select--</option>
								<%								
								for (String strProjectName : projectNameList) 
								{
									if(strProjectName.equals(strTapeOutProjName) )
									{
								%>
								      <option value="<%=strProjectName%>" selected><%=strProjectName%></option>
								<%
									}
									else
									{
								%>
										<option value="<%=strProjectName%>" ><%=strProjectName%></option>
								<%
									}
								}
								%>
							 </select>
						</td>
                                  						
						<th rowspan="3">
						   <div>CP Material Create</div>
						</th>
					  	<td rowspan="3">
							<% //Added on 9/18
							if (checkedRoles != null) 
							{
								for (RoleTo roleTo : checkedRoles) 
								{
								   if (roleTo.getRoleName().equals("Guest") )  
								   {
										isGuest="Yes";
									} 
								}
							} 
							if ( isGuest.equals("No"))  
							{
							%>
						  	<input name="btnModifyCpMaterial" id="btnModifyCpMaterial" type="button" class="button" onClick="modifyCpMaterial()" value="CP Modify">
							<%}%>
							<P>
							
							<table border="0" cellpadding="0" cellspacing="0" width="100%" id="table2">
							<tr>
							<td>
							   <table id="table3" class="grid" border="0" cellpadding="1" cellspacing="1">
								<tbody>
								  <tr>
									  <th>Cp Material Num</th>
									  <th>ProjectCode W version</th>
									  <th>Cp Material Desc</th>
									  <th>Cp Material Remark</th> <!-- FCG3 -->
								  </tr>
								</tbody>
							   <%if (materialList != null && materialList.size() > 0) 
							     {
									  int idx = 0;
									  String custShortName = "";
									  for(CpMaterialTo material : materialList) 
									  {
										  if (material != null)
										  {
									        idx ++;
									        String tdcss = "class=\"c" + idx % 2+"\"";
								%>
											  <tr>
											  <td <%=tdcss %>> <%=material.getCpMaterialNum()%>&nbsp;
												  <input name='cpMaterialNum' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "CP_MATERIAL_NUM") %>'>
											  </td>
											  <td <%=tdcss %>> <%=BeanHelper.getHtmlValueByColumn(material, "PROJECT_CODE_W_VERSION") %>&nbsp;
												  <input name='projectCodeWVersion1' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "PROJECT_CODE_W_VERSION") %>' size="20">
											  </td>
											  <td <%=tdcss %>> <%=BeanHelper.getHtmlValueByColumn(material, "DESCRIPTION") %>&nbsp;
												  <input name='description1' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "DESCRIPTION") %>' size="20">
											  </td>
											  <td <%=tdcss %>> <%=BeanHelper.getHtmlValueByColumn(material, "REMARK") %>&nbsp;
												  <input name='remark1' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(material, "REMARK") %>' size="10">
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
							<p>&nbsp;
							
					   </td>
					 </tr>
					 					 					  
					 <tr>					 	 
                   <th width="20%">Fab device ID</th>					   
					  	 <td width="30%" colspan="3">					  	   					  	
						    <input type="text" class="text" name="fabDeviceId" id="fabDeviceId"	value="<%=BeanHelper.getHtmlValueByColumn(ref,"FAB_DEVICE_ID") %>">
						 </td>							 						 
					 </tr>
					 
					 <tr>
						<th width="20%" rowspan=2>Mask Layer Combination</th>
					  	<td width="30%" rowspan=2 colspan="3">
					  		<textarea id="maskLayerCom" name="maskLayerCom" cols="50" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "MASK_LAYER_COM") %></textarea>
							<input type="button" value="Fetch" class="button" id="fetchMask" name="fetchMask" onClick="setMaskLayerCom()">
		         	</td>
					 </tr>
					 <tr>
						<th>AssignTo</th>
						   <td>
					  	   <table border="0" cellspace="0" cellpadding="0" margin="0" id="table4">
					  	   <tr>
					  	      <td rowspan="2">
					  	      <select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    	   <%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO") != null) 
					    		{
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO");
					    			String[] list = listStr.split(",");
						    		for (String s : list) 
						    		{
						    			if (s != null && s.length() > 0) 
						    			{
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
									SmartSearch.setup(
									{
		                        cp:"<%=cp%>",
		                        button:"userBtn",
		                        inputField:"assignTo",
										name:"AssignToUser",
		                        autoSearch:false,
		                        callbackHandle:"selectUserCallback" 
									});
									function selectUserCallback(selectField, columns, value) 
									{
										if ($(selectField) && value != null && value.length > 0) 
										{
											for(var i = 0; i < value.length; i++) 
											{
												addDifferOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
											}
										}
									}							
							      </script>
							   </td>
							   <td>
							      <input id="userBtn" type="button" class="button" value="Remove" onClick="removeSelectedOptions($('assignTo'))">
							   </td>
						   </tr>
							<tr>
							   <td>
					  			   <input id="roleBtn" type="button" class="button" value="Role" >
									<script type="text/javascript">
									SmartSearch.setup(
									{
                               cp:"<%=cp%>",
                               button:"roleBtn",
                               inputField:"assignTo",
								       name:"AssignToRole",
                               autoSearch:false,
                               callbackHandle:"selectRoleCallback" 
							      });
							      function selectRoleCallback(selectField, columns, value) 
							      {
								      if ($(selectField) && value != null && value.length > 0) 
									   {
									       for(var i = 0; i < value.length; i++) 
										    {
										        addDifferOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
									       }
								       }
							      }							
							      </script>
						      </td>		
					      </tr>
						   </table>
							</td>							
					 </tr>
					 	
					 <tr> <th width="20%">Tape Out Date</th>
					  	<td width="30%" colspan="3">
						<input type="text" class="text"  name="tapeOutDate" id="tapeOutDate" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"TAPE_OUT_DATE","yyyy/MM/dd") %>">
					    <img src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19" height="20" id="tapeOutDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"tapeOutDate",
								ifFormat:"%Y/%m/%d",
								button:"tapeOutDateBtn"
							});
						</script>&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="doClearDate()">
						</td>
					    <th>&nbsp;</th>
						<td>&nbsp;</td>
					  </tr>
					 <tr>
					    <th width="20%">Mask Id</th>
					  	<td width="30%" colspan="3"><input type="text" class="text" name="maskId" id="maskId" 
					  	     value="<%=BeanHelper.getHtmlValueByColumn(ref,"MASK_ID") %>"></td>
					  	<th>&nbsp;</th>
						<td>&nbsp;</td>
					 </tr>
					 <tr>
					  	<th width="20%" rowspan="3" style="vertical-align:top">Revision Item</th>
						<td width="30%" rowspan="3" style="vertical-align:top" colspan="3"><input name="button" type="button" class="button" onClick="selectFunctionParameter()" value="Add">
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
					 <td><input type="hidden" id="createdBy" name="createdBy" 
					 	value="<%=BeanHelper.getHtmlValueByColumn(ref,"CREATED_BY") %>">
					 	<input type="hidden" id="assignEmail" name="assignEmail" 
					 	value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSIGN_EMAIL") %>">
					 </td>		 	
					 </tr>
					 <tr>
					  	 <th width="20%">Mask House</th>
						 <td width="23%" colspan="3"><input type="text" cols="80%" readonly name="maskHouse" id="maskHouse" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MASK_HOUSE") %>" class='text_200'> 
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
						 <td width="22%" colspan="3">
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
						/*String isGuest="No";
						RoleDao roleDao = new RoleDao();
						UserTo currentUser = PIDBContext.getLoginUser(request);
						List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());*/
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
						onclick="doRelease()"> 
					&nbsp;<input name="saveBtn" type="button" class="button" id="saveBtn"
						value="Save" onClick="doSave()"> 
					&nbsp;
						   <%
						   }     
						%>
					<input name="resetBtn" type="reset" class="button" id="resetBtn"
						value="Reset"></div>
					</td>
				</tr>
			</table>
			</form>
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
