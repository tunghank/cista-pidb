<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.IcTapeTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%@ page import="java.util.List" %>


<%
	IcTapeTo ref = (IcTapeTo) request.getAttribute("ref");
	
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>

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
	
	if ($F('prodName') == "") {
		setMessage("error", "Product Name is must field.");
		return;
	}
	if ($F('pkgCode') == "" ) {
		setMessage("error", "Package Code is must field");
		return;
	}
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name (請加版本) is must field.");
		return;
	}	
	if ($F('pkgVersion') == "" || /[^A-Z]/g.test($F('pkgVersion'))) {
		setMessage("error", "IC Tape Version is must field and entry A~Z.");
		return;
	}
	if ($F('minPitch') != "" && !IsNumber($F('minPitch'))) {
		setMessage("error", "Min. Pitch / IL pitch(UM) is must number.");
		return;		
	}

	if ($F('releaseTo') == "") {
		setMessage("error", "Company is must field.");
		return;		
	}
	
	$('releaseBtn').disabled = true;
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking IC-Tape exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_icTape_exist.do',
		{
			method: 'get',
			parameters: 'prodName='+ $F('prodName') + '&pkgCode=' + $F('pkgCode') + '&pkgVersion=' + $F('pkgVersion') + '&tapeName=' + $F('tapeName'),
			onComplete: checkIcTapeExistComplete
		}
	);
}

function checkIcTapeExistComplete(r) {
	var result = r.responseText;
	if(result == "pkgcodeerror") {
		setMessage("error", "Package code is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "pkgversionerror") {
		setMessage("error", "Package version is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "prodnameerror") {
		setMessage("error", "Product Name is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result == "varianterror") {
		setMessage("error", "Variant is error.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else if(result=="true") {
		setMessage("error", "IC-Tape is already exist.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		var incompleted = "";
		
		if ($F('tapeType') == "") {
			incompleted += ", Tape Type";
		}
		if ($F('tapeWidth') == "") {
			incompleted += ", Tape Width";
		}
		if ($F('sprocketHoleNum') == "") {
			incompleted += ", Sprocket Hole Number";
		}
		if ($F('minPitch') == "") {
			incompleted += ", Min. Pitch / IL pitch(UM)";
		}
		
		if (incompleted != "") {
			if (confirm("The following required fields is incomplete, the ic tape will be saved as draft:\r\n" + incompleted.substring(2))) {
				setMessage("error", "Saving IC-Tape...");
				selectAllOptions($('tapeVendor'));
				selectAllOptions($('assySite'));
				selectAllOptions($('assignTo'));
				
				$('icTapeForm').action = $('icTapeForm').action + "&toErp=" + $F('toErp');
				$('icTapeForm').submit();
			} else {
				$('releaseBtn').disabled = false;
				$('saveBtn').disabled = false;
				$('resetBtn').disabled = false;
				setMessage("error", "User cancel.");
			}
		} else {
			setMessage("error", "Saving IC-Tape...");
			selectAllOptions($('tapeVendor'));
			selectAllOptions($('assySite'));
			selectAllOptions($('assignTo'));
			
			$('icTapeForm').action = $('icTapeForm').action + "&toErp=" + $F('toErp');
			$('icTapeForm').submit();
		}
	}
}

function selectProdName() {
   
	var target = "<%=cp%>/dialog/select_prodName_radio.do?m=pre&callback=selectProdNameComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_prodName_radio","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdNameComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		
			$('prodName').value = selectedItems;
		
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

function selectSapMasterCustomerComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			var test = selectedItems[i].split("|");
			selects += "," + test[1];
		}
		if (selects.length > 0) {
			$('tapeCust').value = selects.substring(1);
		}		
	}	
}

function selectVendor() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectVendorComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterVendor","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectVendorComplete(selectedTests) {
	if (selectedTests && selectedTests.length>0) {
		for(var i=0; i<selectedTests.length; i++) {
			var test = selectedTests[i].split("|");
			addOption($('tapeVendor'), test[1], test[0]);
		}
	}
}

function selectAssySite() {
	var target = "<%=cp%>/dialog/select_assySite.do?m=list&callback=selectAssySiteComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_assySite","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectAssySiteComplete(selectedTests) {
	if (selectedTests && selectedTests.length>0) {
		for(var i=0; i<selectedTests.length; i++) {
			var prod = selectedTests[i].split('|');
			addOption($('assySite'), prod[0], prod[0]);
		}
	}
}

function createTapeName(prodName,pkgCode) {
	$('tapeName').value=prodName + pkgCode;
	
	/*if (prodName.length >= 8) {
		$('tapeName').value=prodName.substring(2, 6) + prodName.charAt(7) + pkgCode;
	} else if (prodName.length >= 6) {
		$('tapeName').value=prodName.substring(2, 6) + "0" + pkgCode;
	} else {
		$('tapeName').value=prodName + pkgCode;
	}*/
	
}
</script>

<form name="icTapeForm" action="<%=cp %>/md/ic_tape_create.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Create IC-Tape</td>
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
					<input name="createdBy" id="createdBy" type="text" class="text_protected" readonly value="">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="">&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>

			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div class="erp_label">Material Number</div></th>
					 	<td width="30%"><input class="text_protected" type="text" readonly="true" id="materialNum" name="materialNum" value="" width="150px"></td>
						<!--//modify by 900it add Revision_Reason 2008/04/15-->
						<th width="20%"><div>Revision reason</div></th>
					  	<td width="30%"><label> <select
							name="revisionReason" id="revisionReason" value="<%=BeanHelper.getHtmlValueByColumn(ref, "Revision_Reason") %>" class="select_w130">
							<option value="">--Select--</option>
							<option value="3001" <%="3001".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>EMI</option>
							<option value="3002" <%="3002".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>Modify SR</option>
							<option value="3003"
							<%="3003".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>Cu free</option>
							<option value="3004"
							<%="3004".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>Stress release</option>
							<option value="3005"
							<%="3005".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>Cu thickness change</option>
							<option value="3006"
							<%="3006".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>layout error</option>
							<option value="3007"
							<%="3007".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>film material</option>
							<option value="3008"
							<%="3008".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>Modify PI</option>
							<option value="3009"
							<%="3009".equals(BeanHelper.getHtmlValueByColumn(ref, "revision_Reason"))?"selected":"" %>>Others</option>
						</select></label>
					  	</td>				
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Tape Name (請加版本)</div></th>
					  	<td width="30%"><input class="text_required" type="text" id="tapeName" name="tapeName"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_NAME") %>"  width="150px"></td>
						<th width="20%"><div>Tape Approve Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="tapeApproveDate" id="tapeApproveDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_APPROVE_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="tapeApproveDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"tapeApproveDate",
								ifFormat:"%Y/%m/%d",
								button:"tapeApproveDateBtn"
							});
						</script></td>
					</tr>
					<tr>
						<th width="20%"><div>Tape Variant</div></th>
					  	<td width="30%"><input class="text_protected" type="text"  readonly="true" id="tapeVariant" name="tapeVariant"  width="150px"></td>
						<th width="20%"><div>Customer Drawing Issue Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="custDrawingIssueDate" id="custDrawingIssueDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CUST_DRAWING_ISSUE_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="custDrawingIssueDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"custDrawingIssueDate",
								ifFormat:"%Y/%m/%d",
								button:"custDrawingIssueDateBtn"
							});
						</script></td>											
					</tr>	
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span> Product Name</div></th>
					  	<td width="30%"><input class="text_required" type="text" readonly id="prodName" name="prodName"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_NAME") %>"  width="150px" > <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameSSBtn",
                                inputField:"prodName",
                                name:"ProductName",
                                autoSearch:false,
                                callbackHandle:"icTapeCallbackHandle"
							});
							function icTapeCallbackHandle(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";
							
									for(var i = 0; i < value.length; i++) {
										tempValue += "," + value[i][columns[0]];
									}
							
									if(tempValue != "") {
										$(inputField).value = tempValue.substring(1);
									}
								}
								createTapeName($('prodName').value,$('pkgCode').value);
							}
							</script></td>
							<th width="20%"><div>TCP Drawing Finish Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="tcpDrawingFinishDate" id="tcpDrawingFinishDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TCP_DRAWING_FINISH_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="tcpDrawingFinishDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"tcpDrawingFinishDate",
								ifFormat:"%Y/%m/%d",
								button:"tcpDrawingFinishDateBtn"
							});
						</script></td>
					</tr>			
					<tr>
						<th width="20%"><div><span class="star">*</span> Package Code</div></th>
					  	<td width="30%"><input class="text_required" type="text" id="pkgCode" name="pkgCode"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "PKG_CODE") %>" maxlength="4"  width="150px" onblur="createTapeName($('prodName').value,$('pkgCode').value)"></td>
						<th width="20%"><div>Tape Maker Confirm: Done Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="tapeMakerConfirmDoneDate" id="tapeMakerConfirmDoneDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_MAKER_CONFIRM_DONE_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="tapeMakerConfirmDoneDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"tapeMakerConfirmDoneDate",
								ifFormat:"%Y/%m/%d",
								button:"tapeMakerConfirmDoneDateBtn"
							});
						</script></td>
					</tr>				
					<tr>
						<th width="20%"><div><span class="star">*</span> Tape Version</div></th>
					  	<td width="30%"><input class="text_required" type="text" id="pkgVersion" name="pkgVersion"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "PKG_VERSION") %>" maxlength="1"  width="150px"></td>
						<th width="20%"><div>Customer Approval: Done Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="custApprovalDoneDate" id="custApprovalDoneDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CUST_APPROVAL_DONE_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="custApprovalDoneDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"custApprovalDoneDate",
								ifFormat:"%Y/%m/%d",
								button:"custApprovalDoneDateBtn"
							});
						</script></td>												  	
					</tr>		
					<tr>
						<th width="20%"><div class="erp_label">Tape Type</div></th>
					  	<td width="30%"><label> <select
							name="tapeType" id="tapeType" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_TYPE") %>" class="select_w130">
							<option value="">--Select--</option>
							<option value="301" <%="301".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_TYPE"))?"selected":"" %>>TCP</option>
							<option value="302" <%="302".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_TYPE"))?"selected":"" %>>COF</option>
						</select></label></td>
						<th width="20%"><div>Tape Making Finish: Done Date</div></th>
					  	<td width="30%"><input type="text" class="text" readonly name="tapeMakingFinishDoneDate" id="tapeMakingFinishDoneDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_MAKING_FINISH_DONE_DATE", "yyyy/MM/dd") %>" width="150px">
							<img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="tapeMakingFinishDoneDateBtn" style="vertical-align:middle">
							<script type="text/javascript">
							Calendar.setup({
								inputField:"tapeMakingFinishDoneDate",
								ifFormat:"%Y/%m/%d",
								button:"tapeMakingFinishDoneDateBtn"
							});
						</script></td>						
					</tr>																		
					<tr>
						<th width="20%" rowspan="4" style="vertical-align:top"><div class="erp_label">Tape Vendor</div></th>
					  	<td width="30%" rowspan="4" style="vertical-align:top">
					  	<input type="button" class="button" value="Add" onclick="selectVendor()"><input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('tapeVendor'))"><br>
					  	<select size="6" multiple style="width:100%" name="tapeVendor" id="tapeVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "TAPE_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "TAPE_VENDOR");
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
						<th width="20%"><div>Remark</div></th>
					  	<td width="30%"><input class="text" type="text" id="remark" name="remark"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %>"  width="150px"></td>	  	
					</tr>				
					<tr>
						<th width="20%" ><div>AssignTo</div></th>
					  	<td width="30%" >
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
						<th width="20%"><div>Status</div></th>
					  	<td width="30%"><input class="text_protected" type="text" id="status" name="status" value="<%=BeanHelper.getHtmlValueByColumn(ref, "STATUS") %>" readonly width="150px">
					  	</td>							
					</tr>			
					<tr>
						<!--//modify by 900it 2008/04/15-->
						<th width="20%"><div>Tape Customer</div></th>
					  	<% 
							SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
							SapMasterCustomerTo sapMasterCustomerTo = sapMasterCustomerDao.findByVendorCode(BeanHelper.getHtmlValueByColumn(ref, "TAPE_CUST"));
						%>
					  	<td width="30%"><input class="text" type="text" readonly id="tapeCust" name="tapeCust"  value="<%=BeanHelper.getHtmlValueByColumn(sapMasterCustomerTo, "SHORT_NAME")%>"  width="150px"> <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeCustSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeCustSSBtn",
                                inputField:"tapeCust",
                                table:"WM_SAP_MASTER_CUSTOMER",
                                keyColumn:"SHORT_NAME",
                                columns:"SHORT_NAME,CUSTOMER_CODE",
                                autoSearch:false,
                                title:"Tape Customer"
							});
							</script></td>							
					</tr>												
					<tr>
						<th width="20%"><div class="erp_label">Tape Width</div></th>
					  	<td width="30%"><label> <select
							name="tapeWidth" id="tapeWidth"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_WIDTH") %>" class="select_w130">
							<option value="">--Select--</option>
							<option value="35W" <%="35W".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_WIDTH"))?"selected":"" %>>35W</option>
							<option value="35SW" <%="35SW".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_WIDTH"))?"selected":"" %>>35SW</option>
							<option value="48W" <%="48W".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_WIDTH"))?"selected":"" %>>48W</option>
							<option value="48SW" <%="48SW".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_WIDTH"))?"selected":"" %>>48SW</option>
							<option value="70W" <%="70W".equals(BeanHelper.getHtmlValueByColumn(ref, "TAPE_WIDTH"))?"selected":"" %>>70W</option>
						</select></label></td>
						<!--//modify by 900it 2008/04/15-->
						<th width="20%"><div>Tape Customer Project Name</div></th>
					  	<td width="30%"><input class="text" type="text" id="tapeCustProjName" name="tapeCustProjName"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_CUST_PROJ_NAME") %>"  width="150px">
					  	</td>
					</tr>						
					<tr>
						<th width="20%"><div class="erp_label">Sprocket Hole No.</div></th>
					  	<td width="30%">
							<label> <select
								name="sprocketHoleNum" id="sprocketHoleNum"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM") %>" class="select_w130">
								<option value="">--Select--</option>
								<option value="2" <%="2".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>2</option>
								<option value="2.5" <%="2.5".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>2.5</option>
								<option value="3" <%="3".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>3</option>
								<option value="3.5" <%="3.5".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>3.5</option>
								<option value="4" <%="4".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>4</option>
								<option value="4.5" <%="4.5".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>4.5</option>
								<option value="5" <%="5".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>5</option>
								<option value="5.5" <%="5.5".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>5.5</option>
								<option value="6" <%="6".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>6</option>
								<option value="6.5" <%="6.5".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>6.5</option>
								<option value="7" <%="7".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>7</option>
								<option value="8" <%="8".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>8</option>
								<option value="9" <%="9".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>9</option>
								<option value="10" <%="10".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>10</option>
								<option value="11" <%="11".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>11</option>
								<option value="12" <%="12".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>12</option>
								<option value="13" <%="13".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>13</option>
								<option value="14" <%="14".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>14</option>
								<option value="15" <%="15".equals(BeanHelper.getHtmlValueByColumn(ref, "SPROCKET_HOLE_NUM"))?"selected":"" %>>15</option>
							</select></label>					  	
					  	</td>
						<!--//modify by 900it 2008/04/15-->
						<th width="20%" rowspan="5" style="vertical-align:top"><div>Assembly Site</div></th>
					  	<td width="30%" rowspan="5" style="vertical-align:top">
					  	<input type="button" class="button" value="Add" onclick="selectAssySite()"><input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assySite'))"><br>
					  	<select size="6" multiple style="width:100%" name="assySite" id="assySite" >
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "ASSY_SITE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "ASSY_SITE");
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
						<th width="20%"><div class="erp_label">Min. Pitch</div></th>
					  	<td width="30%"><input class="text" type="text" id="minPitch" name="minPitch"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "MIN_PITCH") %>"  width="150px"></td>				  	
					</tr>		
					<tr>
						<th width="20%">IL Pitch (UM)</th>
					  	<td width="30%"><input class="text_protected" readonly type="text" id="ilPitch" name="ilPitch"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "IL_PITCH") %>"  width="150px"></td>						  	
					</tr>								
					<tr>
						<th width="20%"><div>OLB Cross Section TOP</div></th>
					  	<td width="30%"><input class="text" type="text" id="olbCrossTop" name="olbCrossTop"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "OLB_CROSS_TOP") %>"  width="150px"></td>
	
					</tr>	
					<tr>
						<th width="20%"><div>OLB Cross Section Bottom</div></th>
					  	<td width="30%"><input class="text" type="text" id="olbCrossBottom" name="olbCrossBottom"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "OLB_CROSS_BOTTOM") %>"  width="150px"></td>
	
					</tr>	
					<tr>
						<th width="20%"><div>Tape Process</div></th>
					  	<td width="30%"><label> <select
							name="tapeProcess" id="tapeProcess" value="<%=BeanHelper.getHtmlValueByColumn(ref, "Tape_Process") %>" class="select_w130">
							<option value="">--Select--</option>
							<option value="Normal-Etching" <%="Normal-Etching".equals(BeanHelper.getHtmlValueByColumn(ref, "Tape_Process"))?"selected":"" %>>Normal Etching</option>
							<option value="New-Etching" <%="New-Etching".equals(BeanHelper.getHtmlValueByColumn(ref, "Tape_Process"))?"selected":"" %>>New Etching</option>
							<option value="Semi-additive" <%="Semi-additive".equals(BeanHelper.getHtmlValueByColumn(ref, "Tape_Process"))?"selected":"" %>>Semi-additive</option>
							</select></label>
							</td>
							<th width="11%"><div class="erp_label"><span class="star">*</span>Company</div></th>
							<td width="22%">
							<select class="select_w130" name="releaseTo" id="releaseTo">
								<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> companyList = (List<FunctionParameterTo>) request.getAttribute("companyNameList");
									for (FunctionParameterTo company : companyList) {
										String v = company.getFieldValue();
										String selected = "";
										if (v.equals(BeanHelper.getHtmlValueByColumn(ref, "RELEASE_TO"))) {
											selected = "selected";
										}
										%>
								<option value="<%=v %>" <%=selected %>><%=company.getFieldShowName() %></option>
										<%
									}
								%>
						</select>
						</td>		
					</tr>
					<tr>
						<!--//modify by 900it add Cu_Layer 2008/04/15-->
						<th width="20%"><div>Cu layer</div></th>
					  	<td width="30%"><label> <select
							name="cuLayer" id="cuLayer" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CU_LAYER") %>" class="select_w130">
							<option value="">--Select--</option>
							<option value="1L" <%="1L".equals(BeanHelper.getHtmlValueByColumn(ref, "CU_LAYER"))?"selected":"" %>>1L</option>
							<option value="2L" <%="2L".equals(BeanHelper.getHtmlValueByColumn(ref, "CU_LAYER"))?"selected":"" %>>2L</option>
						</select></label></td>	
						<th width="20%"><div>Tape Material</div></th>
					  	<td width="30%"><input class="text" type="text" id="tapeMaterial" name="tapeMaterial"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_MATERIAL") %>"  width="150px"></td>
					</tr>		
					<tr>
						<!--//modify by 900it add Cu_Thickness_Pattern_site 2008/04/15-->
						<th width="20%"><div>Cu thickness-Pattern site (um)</div></th>
					  	<td width="30%"><label> <input type="text" readonly name="cuThicknessPattern" id="cuThicknessPattern" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CU_THICKNESS_PATTERN") %>" class="text_required"><img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="cuThicknessPatternSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"cuThicknessPatternSSBtn",
                                inputField:"cuThicknessPattern",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='CU_THICKNESS' and fun_name='IC_TAPE'",
								orderBy:"ITEM",
                                title:"CU_THICKNESS",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"cuThicknessCallback"
							});

							function cuThicknessCallback(inputField, columns, value) {
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
							</label>
						 </td>
						<th width="20%"><div>Chip Size (mm)</div></th>
					  	<td width="30%"><input class="text" type="text" id="chipSize" name="chipSize"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "CHIP_SIZE") %>"  width="150px"></td>
					</tr>							
					<tr>
						<!--//modify by 900it add Cu_Thickness_Back_site 2008/04/15-->
						<th width="20%"><div>Cu thickness-back site (um)</div></th>
					  	<td width="30%"><label><input type="text" readonly name="cuThicknessBack" id="cuThicknessBack" value="<%=BeanHelper.getHtmlValueByColumn(ref, "CU_THICKNESS_BACK") %>" class="text_required"><img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="cuThicknessBackSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"cuThicknessBackSSBtn",
                                inputField:"cuThicknessBack",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='CU_THICKNESS' and fun_name='IC_TAPE'",
								orderBy:"ITEM",
                                title:"CU_THICKNESS",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"cuThicknessCallback"
							});
							</script>
						  </label>
						</td>
						<th width="20%"><div>Reel Size (mm)</div></th>
					  	<td width="30%"><input class="text" type="text" id="reelSize" name="reelSize"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "REEL_SIZE") %>"  width="150px"></td>
					</tr>		
					<tr>
						<th width="20%"><div>Tape New Process 原因</div></th>
					  	<td width="30%"><input class="text" type="text" id="newProcessReason" name="newProcessReason"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "NEW_PROCESS_REASON") %>"  width="150px"></td>

						<th width="20%"><div>S/R Material</div></th>
					  	<td width="30%"><input class="text" type="text" id="srMaterial" name="srMaterial"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "SR_MATERIAL") %>"  width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div>OL Output Side Total Pitch (mm)</div></th>
					  	<td width="30%"><input class="text" type="text" id="olOsTotalPitch" name="olOsTotalPitch"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "OL_OS_TOTAL_PITCH") %>"  width="150px"></td>

						<th width="20%"><div>Spacer Type</div></th>
					  	<td width="30%"><input class="text" type="text" id="spacerType" name="spacerType"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "SPACER_TYPE") %>"  width="150px"></td>
					</tr>
					<tr>
						<th width="20%"><div>OL Input Side Total Pitch (mm)</div></th>
					  	<td width="30%"><input class="text" type="text" id="olIsTotalPitch" name="olIsTotalPitch"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "OL_IS_TOTAL_PITCH") %>"  width="150px"></td>

						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><div>Output Channel</div></th>
					  	<td width="30%"><input class="text" type="text" id="outputChannel" name="outputChannel"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "OUTPUT_CHANNEL") %>"  width="150px"></td>

						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
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

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
