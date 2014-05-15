<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.WlhTo" %>
<%@ page import="com.cista.pidb.code.to.SapAppCategoryTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="java.util.List"%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
function doSave() {
		if ($F('prodName') == "") {
			setMessage("error", "Product Name is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('prodType') == "") {
			setMessage("error", "Product type is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('vendorCode') == "") {
			setMessage("error", "Vendor Code is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('serialNum') == "") {
			setMessage("error", "Serial number is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('variant') == "") {
			setMessage("error", "Variant is must field.");
			$('isRelease').value='0';	
			return;
		}else{
			if ($F('variant').length != 2 ) {
				setMessage("error", "Variant Length must be 2");
				$('isRelease').value='0';	
				return;
			}
		}

		if ($F('reserved') == "") {
			setMessage("error", "Reserved is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('materialNum') == "") {
			setMessage("error", "Material Number is must field.");
			$('isRelease').value='0';	
			return;
		}else if ($F('materialNum').length < 14 ){
			setMessage("error", "WLO Material Number length < 14 ");
			$('isRelease').value='0';	
			return;
		}

		if ($F('partNum') == "") {
			setMessage("error", "Part Num is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('projCode') == "") {
			setMessage("error", "Project Code is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('prodCode') == "") {
			setMessage("error", "Product Code is must field.");
			$('isRelease').value='0';	
			return;
		}



		if ($F('appCategory') == "") {
			setMessage("error", "Application Category is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('mpStatus') == "") {
			setMessage("error", "MP Status is must field.");
			$('isRelease').value='0';	
			return;
		}

		/*if ($F('xDim') == "") {
			setMessage("error", "X Dimension (mm) is must field.");
			$('isRelease').value='0';	
			return;
		}else{*/
			if ( $F('xDim').isNumber() ){
				if(!$F('xDim').isDecimal(10,2) ){
					setMessage("error", "X Dimension be like '88.22' decimal number!");
					return;
				}
			}else{
				setMessage("error", "X Dimension must be number!");
				return;
			}
		//}

		/*if ($F('yDim') == "") {
			setMessage("error", "Y Dimension (mm) is must field.");
			$('isRelease').value='0';	
			return;
		}else{*/
			if ( $F('yDim').isNumber() ){
				if(!$F('yDim').isDecimal(10,2) ){
					setMessage("error", "Y Dimension be like '88.22' decimal number!");
					return;
				}
			}else{
				setMessage("error", "Y Dimension must be number!");
				return;
			}
		//}

		/*if ($F('zDim') == "") {
			setMessage("error", "Z Dimension (mm) is must field.");
			$('isRelease').value='0';	
			return;
		}else{*/
			if ( $F('zDim').isNumber() ){
				if(!$F('zDim').isDecimal(10,2) ){
					setMessage("error", "Z Dimension be like '88.22' decimal number!");
					return;
				}
			}else{
				setMessage("error", "Z Dimension must be number!");
				return;
			}
		//}

		if ($F('packingType') == "") {
			setMessage("error", "Packing Type is must field.");
			$('isRelease').value='0';	
			return;
		}

	
		setMessage("error", "Updating WLH Material...");	
		selectAllOptions($('assignTo'));
									
		$('createForm').action = $('createForm').action + "?m=save";
		$('createForm').submit();
	}



function doRelease() {
	$('isRelease').value='1';
	doSave();
}	
</script>
</head>
<body>
<% 
	WlhTo ref = (WlhTo)request.getAttribute("wlhTo");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
		<td class="pageTitle">Master Data :: Modify WLH Material</td>
		
  </tr>
</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/wlh_edit.do" method="post">
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
						<th width="20%"><div class="erp_label"><span class="star">*</span>Material Number</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="materialNum" id="materialNum" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"MATERIAL_NUM") %>"></td>

						<th width="20%">X Dimension (mm)</th>
					    <td width="30%"><input class="text" type="text" name="xDim" id="xDim" 
						 size='10' value="<%=BeanHelper.getHtmlValueByColumn(ref,"X_DIM") %>">(mm)
						</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Part Num</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="partNum" id="partNum" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PART_NUM") %>" size='25'></td>

						<th width="20%">Y Dimension (mm)</th>
					    <td width="30%"><input class="text" type="text" name="yDim" id="yDim" 
						 size='10' value="<%=BeanHelper.getHtmlValueByColumn(ref,"Y_DIM") %>">(mm)
						</td>
					</tr>

					<tr>
						<th width="20%">Description</th>
					    <td width="30%">
						<input class="text" type="text" name="description" id="description" 
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"DESCRIPTION") %>" size='35'></td>

						<th width="20%">Y Dimension (mm)</th>
					    <td width="30%"><input class="text" type="text" name="zDim" id="zDim" 
						 size='10' value="<%=BeanHelper.getHtmlValueByColumn(ref,"Z_DIM") %>">(mm)
						</td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Reference WLO</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="refWloMaterial" id="refWloMaterial" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"REF_WLO_MATERIAL") %>">
					      <img src="<%=cp%>/images/lov.gif" alt="LOV" id="refWloMaterialLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"refWloMaterialLov",
                                inputField:"refWloMaterial",
                                table:"PIDB_WLO",
                                keyColumn:"MATERIAL_NUM",
                                title:"Wlo Material",
                                autoSearch:false,
                                mode:0,
								callbackHandle:"refWloMaterialCallback"
							});

							function refWloMaterialCallback(inputField, columns, value) {
								if (value != null && value.length > 0) {
									$('refWloMaterial').value = value[0]["MATERIAL_NUM"];
									new Ajax.Request( '<%=cp%>/ajax/get_WloMaterialNum.do',
										{
											method: 'post',
											parameters: 'material=' + $('refWloMaterial').value,
											onComplete: getWloMaterialNumComplete
										} 
									);
								}

							}
							
							function getWloMaterialNumComplete(r) {
								var returnValue = r.responseText.split("|");
								if (returnValue.length > 0) {
									$('prodCode').value = returnValue[0];
									$('projCode').value = returnValue[1];
									$('prodName').value = returnValue[2];
									$('serialNum').value = returnValue[3];
									$('variant').value = returnValue[4];
									$('reserved').value = returnValue[5];
									fetchWlhMaterialNum();
								}
							}

							</script>




						</td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Packing Type</div></th>
					    <td width="30%"><input class="text_protected" type="text" name="packingType" id="packingType" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PACKING_TYPE") %>" size='5'>

								<%
										List<FunctionParameterTo> packingTypeList = (List<FunctionParameterTo>) request.getAttribute("packingTypeList");
										for (FunctionParameterTo version : packingTypeList) {
											String v = version.getFieldValue();
											String showName = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"PACKING_TYPE"))){
												showName = version.getFieldShowName();
												out.println(showName);
												break;
											}
								%>
											
								<%
										}
								%>
						</td>

					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Product Code</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="prodCode" id="prodCode" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROD_CODE") %>">
						</td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>MP Status</div></th>
					    <td width="30%">
						  <select class="select_w130" name="mpStatus" id="mpStatus">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> mpStatusList = (List<FunctionParameterTo>) request.getAttribute("mpStatusList");
										for (FunctionParameterTo version : mpStatusList) {
											String v = version.getFieldValue();
											String selected = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"MP_STATUS")))
											{
												selected = "selected";
											}
								%>
											<option value="<%=v %>" <%=selected%>><%=version.getFieldShowName() %></option>
								<%
										}
								%>
						 </select>
						</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Project Code</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="projCode" id="projCode" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE") %>">
						</td>
						<th width="20%">Remark</th>
					    <td width="30%">
						<input class="text" type="text" name="remark" id="remark"  size='40'
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"REMARK") %>"></td>

					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Product Name</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="prodName" id="prodName" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROD_NAME") %>">
						</td>
						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>

					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Product type</div></th>
					    <td width="30%"><input class="text_protected" type="text" name="prodType" id="prodType" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROD_TYPE") %>">
								<%
										List<FunctionParameterTo> prodTypeList = (List<FunctionParameterTo>) request.getAttribute("prodTypeList");
										for (FunctionParameterTo version : prodTypeList) {
											String v = version.getFieldValue();
											String showName = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"PROD_TYPE"))){
												showName = version.getFieldShowName();
												out.println(showName);
												break;
											}
								%>
											
								<%
										}
								%>
								
						</td>

						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>

					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Vendor Code</div></th>
					    <td width="30%"><input class="text_protected" type="text" name="vendorCode" id="vendorCode" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"VENDOR_CODE") %>">
								<%
										List<FunctionParameterTo> vendorCodeList = (List<FunctionParameterTo>) request.getAttribute("vendorCodeList");
										for (FunctionParameterTo version : vendorCodeList) {
											String v = version.getFieldValue();
											String showName = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"VENDOR_CODE"))){
												showName = version.getFieldShowName();
												out.println(showName);
												break;
											}
								%>
											
								<%
										}
								%>
						
						</td>
						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Serial number</div></th>
					    <td width="30%"><input class="text_protected" type="text" name="serialNum" id="serialNum" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"SERIAL_NUM") %>"></td>
						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Variant</div></th>
					    <td width="30%"><input class="text_protected" type="text" name="variant" id="variant" 
						  readonly value="<%=BeanHelper.getHtmlValueByColumn(ref,"VARIANT") %>" size='1'></td>
						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Reserved</div></th>
					    <td width="30%"><input class="text_protected" type="text" name="reserved" id="reserved" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"RESERVED") %>" size='1' maxlength="1"></td>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Application Category</div></th>
					  	<td width="30%"><select id="appCategory" name="appCategory" value="<%=BeanHelper.getHtmlValueByColumn(ref, "APP_CATEGORY") %>" style="width:130px">
					  	<option value="">--Select--</option>
					  	<%
					  		String realCat = BeanHelper.getHtmlValueByColumn(ref, "APP_CATEGORY");
					  		List appCateGoryList = (List) request.getAttribute("appCateGoryList");
					  		for (int i = 0; i < appCateGoryList.size(); i ++) {
					  		    out.print("<option value=\""+((SapAppCategoryTo) appCateGoryList.get(i)).getApplicationCategory() + "\"");
					  		    if (realCat != null && realCat.equals(((SapAppCategoryTo) appCateGoryList.get(i)).getApplicationCategory())) {
					  		        out.print(" selected ");
					  		    }
					  		    out.print(">"+((SapAppCategoryTo) appCateGoryList.get(i)).getDescription()+"</option>");
					  		}
					  	%>					  	
					  	</select></td>
						<th width="20%">&nbsp;</th>
					  	<td width="30%">&nbsp;</td>

					</tr>

					<tr>
						<th width="20%"><div>Vendor Device</div></th>
					  	<td width="30%"><input class="text" type="text" id="vendorDevice" name="vendorDevice" value="<%=BeanHelper.getHtmlValueByColumn(ref, "VENDOR_DEVICE") %>"  width="150px"></td>
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
						onclick="doRelease()">
					&nbsp;
					 <input name="saveBtn" type="button" class="button" id="saveBtn" value="Save" onClick="doSave()">
					  &nbsp;
					  
					           <%
						   }     
						%>
					  <input name="button" type="reset" class="button" id="resetBtn" value="Reset">
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
