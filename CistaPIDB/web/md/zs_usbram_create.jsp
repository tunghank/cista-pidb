<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.md.to.ZsUsbramTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.md.to.ProductTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="java.util.List" %>


<%
	ZsUsbramTo ref = (ZsUsbramTo) request.getAttribute("ref");
	
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>

<script type="text/javascript">
function doRelease(s) {
	doSave(s);
}
function doSave(s) {
	if (s && s=="erp") {
		$('toErp').value = 1;
	} else {
		$('toErp').value = 0;
	}
	
	if ($F('description') == "") {
		setMessage("error", "Description is must field.");
		return;
	}
	if ($F('releaseTo') == "") {
		setMessage("error", "Company is must field.");
		return;		
	}
		
	$('releaseBtn').disabled = true;
	$('saveBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Checking ZS_USBRAM exist...");
	new Ajax.Request(
		'<%=cp%>/ajax/check_zsUsbram_exist.do',
		{
			method: 'get',
			parameters: 'materialNum='+ $F('materialNum') + '&description=' + $F('description'),
			onComplete: checkZsUsbramExistComplete
		}
	);
}

function checkZsUsbramExistComplete(r) {
	var result = r.responseText;
	if(result == "materialNum") {
		setMessage("error", "This Data is already exist.");
		$('releaseBtn').disabled = false;
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
	} else {
		var incompleted = "";
		
		if ($F('operationVoltage') == "") {
			incompleted += ", OPERATION_VOLTAGE";
		}
		
		if (incompleted != "") {
			if (confirm("The following required fields is incomplete, the USBRAM will be saved as draft:\r\n" + incompleted.substring(2))) {
				setMessage("error", "Saving USBRAM...");
				selectAllOptions($('usbramVendor'));
				selectAllOptions($('prodCodeList'));
				$('zsUsbramForm').action = $('zsUsbramForm').action + "&toErp=" + $F('toErp');
				$('zsUsbramForm').submit();
			} else {
				$('releaseBtn').disabled = false;
				$('saveBtn').disabled = false;
				$('resetBtn').disabled = false;
				setMessage("error", "User cancel.");
			}
		} else {
			setMessage("error", "Saving USB-RAM...");
			selectAllOptions($('usbramVendor'));
			 selectAllOptions($('prodCodeList'));
			$('zsUsbramForm').action = $('zsUsbramForm').action + "&toErp=" + $F('toErp');
			$('zsUsbramForm').submit();
		}
	}
}

function selectVendor() {
	var target = "<%=cp%>/dialog/select_sapMasterVendorByName.do?m=list&callback=selectVendorComplete";
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
			addOption($('usbramVendor'), test[1], test[0]);
		}
	}
}

function selectProdCode() {
	var target = "<%=cp%>/dialog/select_product1.do?m=list&callback=selectProdCodeComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_product1","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProdCodeComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i];
			addOption($('prodCodeList'), prod, prod);
		}
	}
}


</script>

<form name="zsUsbramForm" action="<%=cp %>/md/zs_usbram_create.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td class="pageTitle">Master Data :: Create ZS_USBRAM</td>
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
					 	<td width="30%"><input type="text" class="text_protected" readonly id="materialNum" name="materialNum" value="" width="150px"></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>DESCRIPTION</div></th>
					  	<td width="30%"><input type="text" class="text" name="description" id="description" value="<%=BeanHelper.getHtmlValueByColumn(ref, "DESCRIPTION") %>" width="150px"></td>					
					</tr>
					<tr>
						<th width="20%"><div>MEMORY SIZE</div></th>
					  	<td width="30%"><input class="text" type="text" id="memorySize" name="memorySize"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "MEMORY_SIZE") %>"  width="150px"></td>

						<th width="20%"><div>THICKNESS</div></th>
					  	<td width="30%"><input type="text" class="text"  name="thickness" id="thickness" value="<%=BeanHelper.getHtmlValueByColumn(ref, "THICKNESS") %>" width="150px">
						</td>					
					</tr>
					<tr>
						<th width="20%"><div>OPERATION VOLTAGE</div></th>
					  	<td width="30%"><input class="text_required" type="text" id="operationVoltage" name="operationVoltage" value="<%=BeanHelper.getHtmlValueByColumn(ref, "OPERATION_ VOLTAGE") %>"  width="150px"></td>
						<th width="20%"><div>WAFER INCH</div></th>
						 <td width="30%"><label><input type="text" readonly name="waferInch" id="waferInch" value="<%=BeanHelper.getHtmlValueByColumn(ref, "WAFER_INCH") %>" class="text_required"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="waferInchSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"waferInchSSBtn",
                                inputField:"waferInch",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='WAFER_INCH' and fun_name='ZS'",
								orderBy:"ITEM",
                                title:"WAFER_INCH",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"waferInchCallback"
							});

							function waferInchCallback(inputField, columns, value) {
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
					</tr>	
					<tr>
						<th width="20%"><div>GROSS_DIE</div></th>
					  	<td width="30%"><input class="text_required" type="text" id="grossDie" name="grossDie"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "GROSS_DIE") %>"  width="150px" ></td>
						<th width="20%"><div>Input/Output</div></th>
					  	<td width="30%"><input type="text" class="text"  name="io" id="io" value="<%=BeanHelper.getHtmlValueByColumn(ref, "IO") %>" width="150px"></td>
					</tr>
					<tr>
						<th width="20%">Support max resolution</th>
						<td width="20%"><input class="text_required" type="text" id="maxResolution" name="maxResolution"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAX_RESOLUTION") %>"  width="150px" ></td>
						<th width="20%" rowspan="2" style="vertical-align:top"><div>Remark</div>
						<p>&nbsp;</th>
					  	<td width="30%" rowspan="2" style="vertical-align:top">
					  	<textarea id="remark" name="remark" cols="40" rows="6" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					</tr>
					<tr>
						<th width="20%" style="vertical-align:top">USBRAM Vendor</th>
						<td width="20%" style="vertical-align:top">
					  	<input type="button" class="button" value="Add" onclick="selectVendor()"><input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('usbramVendor'))"><br>
					  	<select size="4" multiple style="width:50%" name="usbramVendor" id="usbramVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "VENDOR_CODE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "VENDOR_CODE");
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
						</tr>
					<tr>
						<th width="20%" rowspan="7" style="vertical-align:top"><div>APPLICATION PRODUCT</div></th>
						<td width="20%" rowspan="7" style="vertical-align:top">
					    <input type="button" class="button" value="Add" onclick="selectProdCode()"><input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('prodCodeList'))"><br>
						<select size="8" multiple style="width:100%" name="prodCodeList" id="prodCodeList">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPLICATION_PRODUCT") != null) {
					    			String prodCodeListStr = BeanHelper.getHtmlValueByColumn(ref, "APPLICATION_PRODUCT");
					    			String[] prodCodeList = prodCodeListStr.split(",");
						    		for (String prodCode : prodCodeList) {
						    			if (prodCode != null && prodCode.length() > 0) {
						    			%>
						    			<option value="<%=prodCode %>"><%=prodCode %></option>
						    			<%
						    			}
						    		}
					    		}
					    	%>
					    </select></td>
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
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>
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
						onclick="doRelease('erp')"> <input
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
