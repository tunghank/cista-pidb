<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.md.to.WlmTo"%>
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
		}else{
			if ($F('reserved').length != 1 ) {
				setMessage("error", "Reserved Length must be 1");
				$('isRelease').value='0';	
				return;
			}
		}

		if ($F('packageCode') == "") {
			setMessage("error", "Package Code is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('materialNum') == "") {
			setMessage("error", "Material Number is must field.");
			$('isRelease').value='0';	
			return;
		}else if ($F('materialNum').length < 14 ){
			setMessage("error", "WLM Material Number length < 14 ");
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

		if ($F('lens') == "") {
			setMessage("error", "Lens Structure is must field.");
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

		if ($F('perspective') == "") {
			setMessage("error", "Perspective/FOV is must field.");
			$('isRelease').value='0';	
			return;
		}else{
			if ( $F('perspective').isNumber() ){
				if(!$F('perspective').isDecimal(6,2) ){
					setMessage("error", "Perspective/FOV be like '88.22' decimal number!");
					return;
				}
			}else{
				setMessage("error", "Perspective/FOV must be number!");
				return;
			}
		}



		if ($F('fno') == "") {
			setMessage("error", "F No. is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('irCoating') == "") {
			setMessage("error", "IR Coating is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('emi') == "") {
			setMessage("error", "EMI is must field.");
			$('isRelease').value='0';	
			return;
		}
		
		if ($F('packingType') == "") {
			setMessage("error", "Packing Type is must field.");
			$('isRelease').value='0';	
			return;
		}

		if ($F('barrelType') == "") {
			setMessage("error", "Barrel Type is must field.");
			$('isRelease').value='0';	
			return;
		}



		$('releaseBtn').disabled = true;
		$('saveBtn').disabled = true;
		$('resetBtn').disabled = true;
		setMessage("error", "Checking ...");
		new Ajax.Request(
			'<%=cp%>/ajax/check_wlm_exist.do',
			{
				method: 'get',
				parameters: 'materialNum='+ $F('materialNum'),
				onComplete: checkWlmExistComplete
			}
		);
	}	
	
 function checkWlmExistComplete(r) {
	var result = r.responseText;
	if(result=="true") {
		setMessage("error", "The WLM Material is already exist.");
		$('saveBtn').disabled = false;
		$('resetBtn').disabled = false;
		$('releaseBtn').disabled = false;
		$('isRelease').value='0';	
	} else {
		
			setMessage("error", "Saving WLM Material package...");		
			selectAllOptions($('assignTo'));
									
			$('createForm').action = $('createForm').action + "?m=save";
			$('createForm').submit();
	}
}

function fetchWlmMaterialNum() {
	new Ajax.Request(
			'<%=cp%>/ajax/fetch_WlmMaterialNum.do',
			{
				method: 'post',
				parameters: 'prodName='+ $F('prodName') + 
							'&packageCode=' + $F('packageCode') + 
							'&prodType=' + $F('prodType')+ 
							'&vendorCode=' + $F('vendorCode')+ 
							'&serialNum=' + $F('serialNum')+ 
							'&reserved=' + $F('reserved')+ 
							'&variant=' + $F('variant')+
							'&packingType=' + $F('packingType'),
				onComplete: fetchWlmMaterialNumComplete
			}
		);
}

function fetchWlmMaterialNumComplete(r) {
	var result = r.responseText;
	var rt  = result.split("|");
	var material = rt[0];
	var partNum = rt[1];

	if (result == "") {
	  	setMessage("error", "Can not fetch WLM Material Number.");		  	
	} else {
		$('materialNum').value = material;
		$('partNum').value = partNum;
	}
	$('prodCode').value =  $F('prodName') + $F('packageCode');
}

function doRelease() {
	$('isRelease').value='1';
	doSave();
}

</script>
</head>
<body>
<%
	WlmTo ref = (WlmTo) request.getAttribute("ref");
%>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
		<td class="pageTitle">Master Data :: Create WLM Material</td>
  </tr>
</table>
			<div class="content">
			<form id="createForm" action="<%=cp%>/md/wlm_create.do" method="post">
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
					<input name="createBy" id="createBy" type="text" class="text_protected" readonly>&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly>
					&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Material Number</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="materialNum" id="materialNum" readonly></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Lens Structure</div></th>
					    <td width="30%">
						  <select class="select_w130" name="lens" id="lens">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> lensList = (List<FunctionParameterTo>) request.getAttribute("lensList");
										for (FunctionParameterTo version : lensList) {
											String v = version.getFieldValue();
											String selected = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"LENS")))
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
						<th width="20%"><div class="erp_label"><span class="star">*</span>Part Num</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="partNum" id="partNum" readonly size='25'></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Perspective/FOV</div></th>
					    <td width="30%"><input class="text" type="text" name="perspective" id="perspective" 
						 size='10' value="<%=BeanHelper.getHtmlValueByColumn(ref,"PERSPECTIVE") %>">
						</td>

					</tr>

					<tr>
						<th width="20%">Description</th>
					    <td width="30%">
						<input class="text" type="text" name="description" id="description" size='35'></td>

						<th width="20%"><div class="erp_label"><span class="star">*</span>F No.</div></th>
					    <td width="30%">
						  <select class="select_w130" name="fno" id="fno">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> fnoList = (List<FunctionParameterTo>) request.getAttribute("fnoList");
										for (FunctionParameterTo version : fnoList) {
											String v = version.getFieldValue();
											String selected = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"FNO")))
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
						<th width="20%"><div class="erp_label"><span class="star">*</span>Product Code</div></th>
					    <td width="30%">
						<input class="text_protected" type="text" name="prodCode" id="prodCode" readonly></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>EMI</div></th>
					    <td width="30%">
						  <select class="select_w130" name="emi" id="emi">
								<option value="">--Select--</option>
								<option value="With EMI" <%="With EMI".equals(BeanHelper.getHtmlValueByColumn(ref, "EMI"))?"selected":"" %>>With EMI</option>
								<option value="Without EMI" <%="Without EMI".equals(BeanHelper.getHtmlValueByColumn(ref, "EMI"))?"selected":"" %>>Without EMI</option>
						  </select>
						</td>

					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Project Code</div></th>
					    <td width="30%">
						<input class="text" type="text" name="projCode" id="projCode" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROJ_CODE") %>" >
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeBtn",
                                inputField:"projCode",
                                title:"Product Code",
                                table:"PIDB_PROJECT_CODE",
                                keyColumn:"PROJ_CODE",
								autoSearch:false,
                                mode:0
							});
						</script></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>IR Coating</div></th>
					    <td width="30%">
						  <select class="select_w130" name="irCoating" id="irCoating">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> irCoatingList = (List<FunctionParameterTo>) request.getAttribute("irCoatingList");
										for (FunctionParameterTo version : irCoatingList) {
											String v = version.getFieldValue();
											String selected = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"IR_COATING")))
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
						<th width="20%"><div class="erp_label"><span class="star">*</span>Product Name</div></th>
					    <td width="30%">
						<input class="text" type="text" name="prodName" id="prodName" readonly
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"PROD_NAME") %>" >
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameBtn">
					    <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodNameBtn",
                                inputField:"prodName",
                                title:"Product Name",
                                table:"PIDB_PRODUCT",
                                keyColumn:"PROD_NAME",                              
                                mode:0,
								autoSearch:false,
								callbackHandle:"prodNameCallback"
							});

							function prodNameCallback(inputField, columns, value) {
								if ($(inputField) && value != null && value.length > 0) {
									var tempValue = "";

									tempValue = value[0][columns[0]];
									if(tempValue != "") {
										$(inputField).value = tempValue;
									}
								}
								fetchWlmMaterialNum();
							}
						</script></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Barrel Type</div></th>
					    <td width="30%">
						  <select class="select_w150" name="barrelType" id="barrelType">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> barrelTypeList = (List<FunctionParameterTo>) request.getAttribute("barrelTypeList");
										for (FunctionParameterTo version : barrelTypeList) {
											String v = version.getFieldValue();
											String selected = "";
											if (v.equals(BeanHelper.getHtmlValueByColumn(ref,"BARREL_TYPE")))
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
						<th width="20%"><div class="erp_label"><span class="star">*</span>Product type</div></th>
					    <td width="30%"><select class="select_w130" name="prodType" id="prodType" onChange="fetchWlmMaterialNum()">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> prodTypeList = (List<FunctionParameterTo>) request.getAttribute("prodTypeList");
										for (FunctionParameterTo version : prodTypeList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Packing Type</div></th>
					    <td width="30%">
						  <select class="select_w130" name="packingType" id="packingType" onChange="fetchWlmMaterialNum()">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> packingTypeList = (List<FunctionParameterTo>) request.getAttribute("packingTypeList");
										for (FunctionParameterTo version : packingTypeList) {
											String v = version.getFieldValue();
											%>
											<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
								%>
						  </select>
						</td>

					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Vendor Code</div></th>
					    <td width="30%"><select class="select_w130" name="vendorCode" id="vendorCode" onChange="fetchWlmMaterialNum()">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> vendorCodeList = (List<FunctionParameterTo>) request.getAttribute("vendorCodeList");
										for (FunctionParameterTo version : vendorCodeList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select></td>
						<th width="20%"><div class="erp_label"><span class="star">*</span>MP Status</div></th>
					    <td width="30%">
						  <select class="select_w130" name="mpStatus" id="mpStatus">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> mpStatusList = (List<FunctionParameterTo>) request.getAttribute("mpStatusList");
										for (FunctionParameterTo version : mpStatusList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select>
						</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Serial number</div></th>
					    <td width="30%"><select class="select_w130" name="serialNum" id="serialNum" onChange="fetchWlmMaterialNum()">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> serialNumList = (List<FunctionParameterTo>) request.getAttribute("serialNumList");
										for (FunctionParameterTo version : serialNumList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select></td>
						<th width="20%">Remark</th>
					    <td width="30%">
						<input class="text" type="text" name="remark" id="remark"  size='40'
						value="<%=BeanHelper.getHtmlValueByColumn(ref,"REMARK") %>"></td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Variant</div></th>
					    <td width="30%"><input class="text" type="text" name="variant" id="variant" 
						 size='1' maxlength="2" onChange="fetchWlmMaterialNum()" onBlur="fetchWlmMaterialNum()"></td>
						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
					</tr>
					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Reserved</div></th>
					    <td width="30%"><input class="text" type="text" name="reserved" id="reserved" 
						value="0" onChange="fetchWlmMaterialNum()" onBlur="fetchWlmMaterialNum()" size='1' maxlength="1"></td>
						<th width="20%">&nbsp;</th>
					    <td width="30%">&nbsp;</td>
					</tr>

					<tr>
						<th width="20%"><div class="erp_label"><span class="star">*</span>Package Code</div></th>
					    <td width="30%">
						  <select class="select_w130" name="packageCode" id="packageCode" onChange="fetchWlmMaterialNum()">
								<option value="">--Select--</option>
								<%
										List<FunctionParameterTo> wlmVersionList = (List<FunctionParameterTo>) request.getAttribute("wlmVersionList");
										for (FunctionParameterTo version : wlmVersionList) {
											String v = version.getFieldValue();
											%>
									<option value="<%=v %>"><%=version.getFieldShowName() %></option>
											<%
										}
									%>
						 </select>
						</td>
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
					  
					  <input name="resetBtn" type="button" class="button" id="resetBtn" value="Reset">
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

