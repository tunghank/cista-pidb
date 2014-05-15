<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.ZsHdcpKeyTo" %>
<%@ page import="com.cista.pidb.md.to.ProductTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>

<% 
	ZsHdcpKeyTo ref = (ZsHdcpKeyTo)request.getAttribute("ref");
%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function doSave(s) {
		if (s && s=="erp") {
			$('toErp').value = 1;
		} else {
			$('toErp').value = 0;
		}
		if ($F('materialNum') == "") {
			setMessage("error", "Material Num is must field.");
			return;
		}

		if ($F('DESCRIPTION') == "") {
			setMessage("error", "DESCRIPTION is must field.");
			return;
		}
		if ($F('releaseTo') == "") {
		setMessage("error", "Company is must field.");
		return;		
		}
		
		$('releaseBtn').disabled = true;
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		
		var incompleted = "";
		if (incompleted != "") {
			if (confirm("The following required fields is incomplete, the ic wafer will be saved as draft:\r\n" + incompleted.substring(2))) {
				setMessage("error", "Updating Hdcp Key...");
				selectAllOptions($('hdcpKeyVendor'));
				selectAllOptions($('prodCodeList'));								
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
			setMessage("error", "Updating Hdcp Key...");	
			selectAllOptions($('hdcpKeyVendor'));
			selectAllOptions($('prodCodeList'));						
			$('createForm').action = $('createForm').action + "?m=save";
			$('createForm').submit();
		}
		
	}
function doRelease(s) {
	doSave(s);
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
			addOption($('hdcpKeyVendor'), test[1], test[0]);
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

</head>
<body>

<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: Modify Hdcp Key </td>
  </tr>
</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/zs_hdcpkey_edit.do" method="post">
						
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
					 	<td width="30%"><input type="text" class="text_protected" readonly id="materialNum" name="materialNum" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MATERIAL_NUM") %>" width="150px"></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>DESCRIPTION</div></th>
					  	<td width="30%"><input type="text" class="text" name="description" id="description" value="<%=BeanHelper.getHtmlValueByColumn(ref, "DESCRIPTION") %>" width="150px"></td>					
					</tr>
					<tr>
						<th width="20%"  style="vertical-align:top"><div>HDCP Key Vendor</div></th>
					  	<td width="30%"  style="vertical-align:top">
					  	<input type="button" class="button" value="Add" onclick="selectVendor()"><input type="button" class="button" value="Remove" onclick="removeSelectedOptions($('hdcpKeyVendor'))"><br>
					  	<select size="5" multiple style="width:50%" name="hdcpKeyVendor" id="hdcpKeyVendor">
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
						<th width="20%"  style="vertical-align:top">Remark</th>
						<td width="30%"  style="vertical-align:top"><textarea id="remark" name="remark" cols="40" rows="6" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td>
					</tr>	
					<tr>
						<th width="20%" rowspan="7" style="vertical-align:top"><div>APPLICATION PRODUCT</div></th>
					  	<td width="23%" rowspan="7" style="vertical-align:top">
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
					    </select>
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
					 <td><input type="hidden" id="createdBy" name="createdBy" 
					 	value="<%=BeanHelper.getHtmlValueByColumn(ref,"CREATED_BY") %>">
					 	<input type="hidden" id="assignEmail" name="assignEmail" 
					 	value="<%=BeanHelper.getHtmlValueByColumn(ref,"ASSIGN_EMAIL") %>">
					</td>
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
						   
					<input name="releaseBtn" type="button" class="erp_button" id="releaseBtn" value="Release To ERP"
						onclick="doRelease('erp')"> 
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
