<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.HtolRaTo"%>
<%@ page import="com.cista.pidb.md.dao.HtolRaDao"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="java.util.List" %>
<%@ page import="com.cista.pidb.admin.to.ParameterTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
HtolRaTo ref = (HtolRaTo) request.getAttribute("ref");
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>

<script type="text/javascript">
var oldProjCodeWVersion = '<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE_W_VERSION")%>';

function doSave() {
	if ($F('raTestItem') == "") {
		setMessage("error", "RA TEST ITEM is must field.");
		return;
	}
	if ($F('prodCode') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Project Code w Version is must field.");
		return;
	}
	
	
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking Htol Ra exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_htolRa_exist.do',
		{
			method: 'get',
			parameters: 'projCodeWVersion='+ $F('projCodeWVersion'),
			onComplete: checkHtolRaExistComplete
		}
	);
}

function checkHtolRaExistComplete(r) {
	var result = r.responseText;
	if(result=="true" && $F('projCodeWVersion')!= oldProjCodeWVersion) {
		setMessage("error", "Htol Ra is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		setMessage("error", "Saving Htol Ra...");
		
		selectAllOptions($('assignTo'));
		
		$('updateForm').submit();
	}
}

function doPlanStartDate() {
	
	if($('raRealStartDate').value ==""){
		$('testTime168Hr').value='';
		$('testTime500Hr').value='';
		$('testTime1000Hr').value='';
		$('rptCompleteTime').value='';
  }
	$('raPlanStartDate').value='';
	$('raPlanFinishDate').value='';
}

function doPlanStartDate2() {
	$('raRealStartDate').value='';
	$('raRealFinishDate').value='';
}

function doRaRealStartDate() {
	$('raRealStartDate').value='';
	$('raRealFinishDate').value='';
	$('testTime168Hr').value='';
	$('testTime500Hr').value='';
	$('testTime1000Hr').value='';
	$('rptCompleteTime').value='';
}


function doClearDate(obj) {
	$(obj).value='';
}

function selectProdCode() {
	var target = "<%=cp%>/dialog/select_product_radio.do?m=list&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
			$('prodCode').value = selectedProds;
	}
}

function selectProjCodeWVersion() {
	var target = "<%=cp%>/dialog/select_icWafer_projCodeWVersion_radio.do?m=pre&callback=selectProjCodeWVersionComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icWafer_projCodeWVersion","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeWVersionComplete(selectedItems) {
			$('projCodeWVersion').value = selectedItems;
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


function addDay (dadd,dd) {
	var a = new Date(dd);
	a = a.valueOf();
	a = a + dadd * 24 * 60 * 60 * 1000;
	a = new Date(a);
	
	m = (a.getMonth() + 1) + "";
	d = (a.getDate()) + "";
	if (m.length < 2) {
		m = "0" + m;
	}
	if (d.length < 2) {
		d = "0" + d;
	}
	return a.getFullYear() + "/" + m + "/" + d;
}

function addDate (dest,num) {
	var realDate;
	if ($('raPlanStartDate').value != "") {
		realDate =  addDay(num,$('raPlanStartDate').value);
	}	
	
	if ($('raRealStartDate').value != ""){
		realDate =  addDay(num,$('raRealStartDate').value);
	}
	dest.value = realDate;
}

function dateChange1(raDate) {
	if ($('savedate').value !="" && $('savedate').value != null){
		if (raDate != $('savedate').value){		
			if (confirm("This movement can change other dates,  Do you want to continue?")) {
				addDate ($('testTime168Hr'),10);
				addDate ($('testTime500Hr'),27);
				addDate ($('testTime1000Hr'),51);
				addDate ($('rptCompleteTime'),54);
				addDate ($('raRealFinishDate'),54);
				$('savedate').value = raDate;
			}else{
				  $('raRealStartDate').value = $('savedate').value;
				  addDate ($('testTime168Hr'),10);
				  addDate ($('testTime500Hr'),27);
				  addDate ($('testTime1000Hr'),51);
				  addDate ($('rptCompleteTime'),54);
				  addDate ($('raRealFinishDate'),54);
			    }
			}
	}else {
				$('savedate').value = $('raRealStartDate').value ;
				addDate ($('testTime168Hr'),10);
				addDate ($('testTime500Hr'),27);
				addDate ($('testTime1000Hr'),51);
				addDate ($('rptCompleteTime'),54);
				addDate ($('raRealFinishDate'),54);
			}
}


 function dateChange2(){

	if($('raRealStartDate').value!=""){
		if (confirm("This movement can change other dates,  Do you want to continue?")) {
			$('raRealStartDate').value='';
			$('raRealFinishDate').value='';
			if ($('raPlanStartDate').value != '') {
					addDate ($('testTime168Hr'),10);
					addDate ($('testTime500Hr'),27);
					addDate ($('testTime1000Hr'),51);
					addDate ($('rptCompleteTime'),54);
				$('savedate').value = $('raRealStartDate').value;
			}else{
				$('testTime168Hr').value='';
				$('testTime500Hr').value='';
				$('testTime1000Hr').value='';
				$('rptCompleteTime').value='';
				$('savedate').value = $('raRealStartDate').value;
				}
		}else{
				$('raRealStartDate').value=	$('savedate').value;
		}
	 }else{
		if ($('raPlanStartDate').value != '') {
					addDate ($('testTime168Hr'),10);
					addDate ($('testTime500Hr'),27);
					addDate ($('testTime1000Hr'),51);
					addDate ($('rptCompleteTime'),54);
				$('savedate').value = $('raRealStartDate').value;
			}else{
				$('testTime168Hr').value='';
				$('testTime500Hr').value='';
				$('testTime1000Hr').value='';
				$('rptCompleteTime').value='';
				$('savedate').value = $('raRealStartDate').value;
				}	 
	 }
 }
</script>

<form name="updateForm" action="<%=cp %>/md/htol_ra_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Modify HTOL RA</td>
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
					<input name="createdBy" id="createdBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY", "yyyy/MM/dd") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY", "yyyy/MM/dd") %>">&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div><span class="star">*</span> Product Code</div></th>
					 	<td width="30%"><input class="text_protected" type="text" readonly="true" id="prodCode" name="prodCode" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_CODE")%>" width="150px"></td>
						<th width="20%"><div>RA sample ready date</div></th>
					  	<td width="30%"><input class="text" type="text" readonly id="pmProvidedSampleTime" name="pmProvidedSampleTime"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "PM_PROVIDED_SAMPLE_TIME") %>"  width="150px">
					  	<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="pmProvidedSampleTimeBtn" style="vertical-align:middle" >
							<script type="text/javascript">
							Calendar.setup({
								inputField:"pmProvidedSampleTime",
								ifFormat:"%Y/%m/%d",
								button:"pmProvidedSampleTimeBtn"
							});
						</script>
						&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="doClearDate('pmProvidedSampleTime')">
					  	</td>
					</tr>
					<tr>
						<th width="20%"><div><span class="star">*</span> Project Code w Version</div></th>
					  	<td width="30%"><input class="text_protected" type="text" readonly id="projCodeWVersion" name="projCodeWVersion"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE_W_VERSION")%>"  width="150px"></td>
						<th width="20%"><div>RA plan start date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="raPlanStartDate" id="raPlanStartDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_PLAN_START_DATE", "yyyy/MM/dd") %>" 
					  		width="150px"onchange="addDate ($('testTime168Hr'),10);addDate ($('testTime500Hr'),27);addDate ($('testTime1000Hr'),51);addDate ($('rptCompleteTime'),54);addDate ($('raPlanFinishDate'),54);">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="raPlanStartDateBtn" style="vertical-align:middle" >
							<script type="text/javascript">
							Calendar.setup({
								inputField:"raPlanStartDate",
								ifFormat:"%Y/%m/%d",
								button:"raPlanStartDateBtn"
							});
						</script>
						&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="doPlanStartDate()">
						</td>
					</tr>
					<tr>
						<th width="20%"><div>Priority</div></th>
					  	<td width="30%">
							<select class="select_w130" name="priority" id="priority" width="150px">
								<option value="">--Select--</option>
								<option value="Super Hot Run" <%="Super Hot Run".equals(BeanHelper.getHtmlValueByColumn(ref, "PRIORITY"))?"selected":"" %>>Super Hot Run</option>
								<option value="Hot Run" <%="Hot Run".equals(BeanHelper.getHtmlValueByColumn(ref, "PRIORITY"))?"selected":"" %>>Hot Run</option>
								<option value="Normal Run" <%="Normal Run".equals(BeanHelper.getHtmlValueByColumn(ref, "PRIORITY"))?"selected":"" %>>Normal Run</option>
							</select>					  	
					  	</td>
						<th width="20%"><div>RA plan finish date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="raPlanFinishDate" id="raPlanFinishDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_PLAN_FINISH_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="raPlanFinishDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"raPlanFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"raPlanFinishDateBtn"
							});
						</script>
						&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="doClearDate('raPlanFinishDate')">
						</td>
					</tr>	
					<tr>
						<th width="20%"><div>Customer</div></th>
						<% 
							SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
							String cust = BeanHelper.getHtmlValueByColumn(ref, "CUST");
							String realCusts = "";
							if (cust != null && cust.length() > 0) {
							    String[] custs = cust.split("/");
							    if (custs != null && custs.length > 0) {
							        for (String s : custs) {
							            SapMasterCustomerTo to = sapMasterCustomerDao.findByVendorCode(s);
							            if (to != null) {
							                realCusts += "," + to.getShortName();
							            }
							        }
							    }
							}
							
							if (realCusts.length() > 0) {
							    realCusts = realCusts.substring(1);
							}
						%>
						<td width="30%"><textarea type="text" class="text_protected" cols="49" rows="1" readonly name="cust" id="cust" width="150px"><%=realCusts %></textarea></td>
						<th width="20%"><div>RA real start date</div></th>
					 	<td width="30%"><input type="text" class="text" readonly name="raRealStartDate" id="raRealStartDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_REAL_START_DATE", "yyyy/MM/dd") %>" 
					 		width="150px" onchange="dateChange1(raRealStartDate.value)">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="raRealStartDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"raRealStartDate",
								ifFormat:"%Y/%m/%d",
								button:"raRealStartDateBtn"
							});
							
						</script>
						&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="dateChange2()">
						</td>
					</tr>			
					<tr>
						<th width="20%"><div>Sample Size</div></th>
					  	<td width="30%"><input type="text" class="text" name="sampleSize" id="sampleSize" value="<%=BeanHelper.getHtmlValueByColumn(ref, "SAMPLE_SIZE") %>" width="150px"></td>
						<th width="20%"><div>RA real finish date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="raRealFinishDate" id="raRealFinishDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_REAL_FINISH_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="raRealFinishDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"raRealFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"raRealFinishDateBtn"
							});
						</script>
						&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="doClearDate('raRealFinishDate')">						
						</td>
					</tr>		
					<tr>
						<th width="20%"><div><span class="star">*</span>RA Test Item</div></th>
					  	<td width="30%">
							<select class="select_w130" name="raTestItem" id="raTestItem" width="150px">
								<option value="">--Select--</option>
								<option value="HTOL1" <%="HTOL1".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>HTOL1</option>
								<option value="HTOL2" <%="HTOL2".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>HTOL2</option>
								<option value="HTOL3" <%="HTOL3".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>HTOL3</option>
								<option value="HTOL4" <%="HTOL4".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>HTOL4</option>
								<option value="HTOL5" <%="HTOL5".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>HTOL5</option>

								<option value="LTOL1" <%="LTOL1".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>LTOL1</option>
								<option value="LTOL2" <%="LTOL2".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>LTOL2</option>
								<option value="LTOL3" <%="LTOL3".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>LTOL3</option>

								<option value="THB1" <%="THB1".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>THB1</option>
								<option value="THB2" <%="THB2".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>THB2</option>
								<option value="THB3" <%="THB3".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>THB3</option>
								<option value="Others" <%="Others".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_ITEM"))?"selected":"" %>>Others</option>
							</select>					  	
						</td>
						<th width="20%"><div>Readout point 168hrs</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="testTime168Hr" readonly id="testTime168Hr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TEST_TIME168_HR", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="testTime168HrBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"testTime168Hr",
								ifFormat:"%Y/%m/%d",
								button:"testTime168HrBtn"
							});
						</script></td>
					</tr>			
					<tr>
						<th width="20%"><div align="left" class="redWord">RA Test purpose (Tick all that apply)</div></th>
					  	<td width="30%">&nbsp;</td>
						<th width="20%"><div>Readout point 500hrs</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="testTime500Hr" readonly id="testTime500Hr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TEST_TIME500_HR", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="testTime500HrBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"testTime500Hr",
								ifFormat:"%Y/%m/%d",
								button:"testTime500HrBtn"
							});
						</script></td>			  					
					</tr>
					<tr>
						<th width="20%"><div align="left"><input type="checkbox" id="raTestPurposeCb1" name="raTestPurposeCb1" value="1" <%=ref!=null && ref.getRaTestPurposeCb1()?"checked":"" %>>1.Original new design product</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText1" name="raTestPurposeText1" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT1") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT1") %></textarea></td>
						<th width="20%"><div>Readout point 1000hrs</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="testTime1000Hr" readonly id="testTime1000Hr" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TEST_TIME1000_HR", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="testTime1000HrBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"testTime1000Hr",
								ifFormat:"%Y/%m/%d",
								button:"testTime1000HrBtn"
							});
						</script></td>			  						
					</tr>
					<tr>
						<th width="20%" align="left"><div align="left"><input type="checkbox" id="raTestPurposeCb2" name="raTestPurposeCb2" value="1" <%=ref!=null && ref.getRaTestPurposeCb2()?"checked":"" %>>2.MP in new Fab<br>(Please note Fab name)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText2" name="raTestPurposeText2" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT2") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT2") %></textarea></td>
						<th width="20%"><div>RA report ready date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="rptCompleteTime" readonly id="rptCompleteTime" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RPT_COMPLETE_TIME", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="rptCompleteTimeBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"rptCompleteTime",
								ifFormat:"%Y/%m/%d",
								button:"rptCompleteTimeBtn"
							});
						</script></td>
					</tr>		
					<tr>
						<th width="20%" align="left"><div align="left"><input type="checkbox" id="raTestPurposeCb3" name="raTestPurposeCb3" value="1" <%=ref!=null && ref.getRaTestPurposeCb3()?"checked":"" %>>3.Process generation change(Please note <br>
						 old/new gener ation and original project code)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText3" name="raTestPurposeText3" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT3") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT3") %></textarea></td>
						<th width="20%"><div>RA Request Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="raRequestDate" readonly id="raRequestDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_REQUEST_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="raRequestDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"raRequestDate",
								ifFormat:"%Y/%m/%d",
								button:"raRequestDateBtn"
							});
						</script>
						&nbsp;
						<input name="clearDateBtn" type="button" class="clrDate_button" id="clearDateBtn" value="Clear" 
						onClick="doClearDate('raRequestDate')">	
						</td>	
					</tr>		
					<tr>
						<th width="20%"><div align="left"><input type="checkbox" id="raTestPurposeCb4" name="raTestPurposeCb4" value="1" <%=ref!=null && ref.getRaTestPurposeCb4()?"checked":"" %>>4.Die shrinkage(Please note <br>
						 shrink percentage and original project code)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText4" name="raTestPurposeText4" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT4") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT4") %></textarea></td>
						<th width="20%"><div>RA Test Result</div></th>
					  	<td width="30%">
							<select class="select_w130" name="raTestResult" id="raTestResult" width="150px">
								<option value="">--Select--</option>
								<option value="OnGoing" <%="OnGoing".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_RESULT"))?"selected":"" %>>OnGoing</option>
								<option value="Pass" <%="Pass".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_RESULT"))?"selected":"" %>>Pass</option>
								<option value="Fail" <%="Fail".equals(BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_RESULT"))?"selected":"" %>>Fail</option>
							</select>					  	
					  	</td>	
					</tr>	
					<tr>
						<th width="20%"><div align="left"><input type="checkbox" id="raTestPurposeCb5" name="raTestPurposeCb5" value="1" <%=ref!=null && ref.getRaTestPurposeCb5()?"checked":"" %>>5.Fab transfer(Please note <br>
						 old/new Fab name and original project code)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText5" name="raTestPurposeText5" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT5") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT5") %></textarea></td>
						<th width="20%"><div>RA Test Report Doc</div></th>
					  	<td width="30%"><input type="text" class="text_200" name="raTestRptDoc" id="raTestRptDoc" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_RPT_DOC") %>"></td>	
					</tr>				
					<tr>
						<th width="20%"><div align="left"><input type="checkbox" id="raTestPurposeCb6" name="raTestPurposeCb6" value="1" <%=ref!=null && ref.getRaTestPurposeCb6()?"checked":"" %>>6.Re-Design(Please note <br>
						 original project code)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText6" name="raTestPurposeText6" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT6") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT6") %></textarea></td>
						<th width="20%"><div>Report Version</div></th>
					  	<td width="30%">
							<select class="select_w130" name="rptVersion" id="rptVersion"  width="150px">
								<option value="">--Select--</option>
								<option value="01" <%="01".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>01</option>
								<option value="02" <%="02".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>02</option>
								<option value="03" <%="03".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>03</option>
								<option value="04" <%="04".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>04</option>
								<option value="05" <%="05".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>05</option>
								<option value="06" <%="06".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>06</option>
								<option value="07" <%="07".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>07</option>
								<option value="08" <%="08".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>08</option>
								<option value="09" <%="09".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>09</option>
								<option value="10" <%="10".equals(BeanHelper.getHtmlValueByColumn(ref, "RPT_VERSION"))?"selected":"" %>>10</option>
							</select>					
						</td>							
						
					</tr>		
					<tr>
						<th width="20%"><div align="left"><input type="checkbox" id="raTestPurposeCb7" name="raTestPurposeCb7" value="1" <%=ref!=null && ref.getRaTestPurposeCb7()?"checked":"" %>>7.Fab process change(Please note <br>
						 change item/condition)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText7" name="raTestPurposeText7" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT7") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT7") %></textarea></td>
						<th width="20%"><div>Owner</div></th>
						<td width="30%">
						<select class="select_w130" name="owner" id="owner">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> ownerList = (List<FunctionParameterTo>) request.getAttribute("ownerList");
									for (FunctionParameterTo owner : ownerList) {
										String v = owner.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "OWNER"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=owner.getFieldShowName() %></option>
										<%
									}
								%>				
						</select></td>
					</tr>		
					<tr>
						<th width="20%"><div align="left"><input type="checkbox" id="raTestPurposeCb8" name="raTestPurposeCb8" value="1" <%=ref!=null && ref.getRaTestPurposeCb8()?"checked":"" %>>8.Others(Please note <br>
						 information)</div></th>
					  	<td width="30%">
						<textarea class="textarea" rows="2" id="raTestPurposeText8" name="raTestPurposeText8" cols="49" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT8") %>" ><%=BeanHelper.getHtmlValueByColumn(ref, "RA_TEST_PURPOSE_TEXT8") %></textarea>
						</td>
						<th width="20%"><div>Remark</div></th>
					  	<td width="30%"><textarea type="text" class="textarea" cols="49" rows="2" name="remark" id="remark" width="150px"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					</tr>		
					<tr>
						<th width="20%"><div>&nbsp;</div></th>
					  	<td width="30%">&nbsp;</td>
						<th width="20%" rowspan="2"><div>AssignTo</div></th>
					  	<td width="30%" rowspan="2">
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
										addOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
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
										addOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
									}
								}
							}						
							</script>	
							</td>		
							</tr>
						</table>												  	
					  	</td>	
					</tr>		
					<tr>
						<th width="20%"><div>&nbsp;</div></th>
					  	<td width="30%"><input type="hidden" class="text" readonly name="savedate" id="savedate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "RA_REAL_START_DATE", "yyyy/MM/dd") %>" ></td>															
					</tr>
				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right">
					
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
