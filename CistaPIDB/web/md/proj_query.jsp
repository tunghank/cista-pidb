<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.ProjectTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterProductFamilyTo"%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>
<form id="masterDataQueryForm" action="<%=cp %>/md/proj_query.do?m=query" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	  <td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
	  <jsp:include page="/common/banner.jsp" flush="true" /> <!-- Content start -->
	  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		  <td class="pageTitle">Master Data :: Project Query</td>
		</tr>
	  </table>
	  <div class="content">
	  <table class="formErrorAndButton">
		<tr>
		  <td><div id="error" class="formErrorMsg"><html:errors />&nbsp;</div></td>
		</tr>
	  </table>
	  <table class="formFull" border="0" cellpadding="1" cellspacing="1">
		<tbody>
		<tr>
		  <th width="180">Project Code</th>
			<td>
			<input type="text" class="text" name="projCode" id="projCode"> 
			<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
			<script type="text/javascript">
			SmartSearch.setup({cp:"<%=cp%>", button:"projCodeSSBtn", inputField:"projCode", name:"ProjectCode", mode:1});
			</script>
			</td>
			</tr>
			<tr>
			  <th width="180">Project Name</th>
				<td>
				<input type="text" class="text" name="projName" id="projName" value=""> 
				<img id="projNameBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
				<script type="text/javascript">
				SmartSearch.setup({cp:"<%=cp%>", button:"projNameBtn", inputField:"projName", name:"ProjectName", mode:1});
				</script>
			    </td>
              </tr>
			  <tr>
				<th width="180">Product Code</th>
				<td>
				<input type="text" class="text" name="prodCode"	id="prodCode" value=""> 
				<img id="prodCodeBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
				<script type="text/javascript">
				SmartSearch.setup({cp:"<%=cp%>", button:"prodCodeBtn", inputField:"prodCode", name:"ProductCode", mode:1 });
				</script>
				</td>
				</tr>
				
				<tr>
					<th width="180">Nick Name</th>
					  <td>
						<label><input type="text" class="text" name="nickName"
							id="nickName" value="" size="30"></label>					  					  
					</td>
				</tr>	

				<tr>
				  <th width="180">FAB</th>
					<td>
					<select class="select_w130" name="fab" id="fab">
					  <option value="">--Select--</option>
					  <%
					  List<FabCodeTo> fabCodeList = (List<FabCodeTo>) request.getAttribute("fabCodeList");
					  for(FabCodeTo fab : fabCodeList) {
					  %>
					  <option value="<%=fab.getFab() %>"><%=fab.getFab() %>,<%=fab.getFabDescr() %></option>
					  <%}%>
					</select>
					</td>
					</tr>
					<!-- Remove for 2007/10/1 Hank Tang -->
					<!-- <tr>
					  <th width="180">Option</th>
					  <td>
					  <input type="text" class="text" name="projOption" id="projOption" value="">
					  <img id="projOptionBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
					  <script type="text/javascript">
					  SmartSearch.setup({cp:"<%=cp%>", button:"projOptionBtn", inputField:"projOption", table:"PIDB_PROJECT_CODE",
                                keyColumn: "PROJ_OPTION", title: "Option", mode:1});
					  </script>
					  </td>
					</tr> -->
					<tr>
						<th width="180">Panel Type</th>
						<td><label> <select class="select_w130"
							name="panelType" id="panelType">
							<option value="">--Select--</option>
							<option value="TFT-LCD">TFT-LCD</option>
							<option value="LTPS">LTPS</option>
							<option value="OLED">OLED</option>
							<option value="PDP">PDP</option>
						</select></label></td>
					</tr>
					<tr>
						<th width="180">Product Family</th>
						<td><select class="select_w130"
							name="prodFamily" id="prodFamily">
							<option value="">--Select--</option>
							<%
								List<SapMasterProductFamilyTo> productFamilyList = (List<SapMasterProductFamilyTo>) request.getAttribute("productFamilyList");
								for(SapMasterProductFamilyTo productFamily : productFamilyList) {
							%>
							<option value="<%=productFamily.getProductFamily() %>"><%=productFamily.getDescription() %></option>
							<%
								}
							%>
						</select></td>
					</tr>
					<tr>
						<th width="180">Product Line</th>
						<td><input type="text" class="text" name="prodLine" id="prodLine" value="">
						<img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="prodLineSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"prodLineSSBtn",
                                inputField:"prodLine",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='PRODUCT_LINE'",
								orderBy:"ITEM",
                                title:"Product Line",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"prodLineCallback"
							});

							function prodLineCallback(inputField, columns, value) {
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
					</tr>
					<tr>
						<th width="180">Process Technology</th>
						<td><select class="select_w130" name="procTech" id="procTech">
							<option value="">--Select--</option>
							<%
									List<FunctionParameterTo> procTech = (List<FunctionParameterTo>) request.getAttribute("processTechnology");
									for (FunctionParameterTo prodc : procTech) {
										String v = prodc.getFieldValue();
										
										%>
								<option value="<%=v %>"><%=prodc.getFieldShowName() %></option>
										<%
									}
								%>
							
						</select></td>
					</tr>
					<tr>
						<th width="180">Customer</th>
						<td><input type="text" class="text" name="cust" id="cust" value=""> 
						<img src="<%=cp %>/images/lov.gif" id="custBtn"
							alt="LOV"></td>
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"custBtn",
                                inputField:"cust",
                                table:"PIDB_PROJECT_CODE",
                                keyColumn: "CUST",
                                title: "Customer",
                                mode:1
							});
							</script>
					</tr>
					<tr>
						<th width="180">Team Member</th>
						<td><input type="text" class="text" name="teamMember"
							id="teamMember" value="" size="40"></td>
					</tr>
					<tr>
						<th>Kick-off Date</th>
						<td><input class="text" class="text"
							name="kickOffDateFrom" id=""kickOffDateFrom"" value="" readonly> <img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="kickoffdatefromBtn"> <script
							type="text/javascript">
							Calendar.setup({
								inputField:"kickOffDateFrom",
								ifFormat:"%Y/%m/%d",
								button:"kickoffdatefromBtn"
							});
						</script> ~ <input class="text" class="text" name="kickOffDateTo"
							id="kickOffDateTo" value="" readonly> <img
							src="<%=cp%>/images/calendar.gif" alt="Calendar" width="19"
							height="20" id="kickoffdatetoBtn"> <script
							type="text/javascript">
							Calendar.setup({
								inputField:"kickOffDateTo",
								ifFormat:"%Y/%m/%d",
								button:"kickoffdatetoBtn"
							});
						</script></td>
					</tr>
					<tr>
						<th width="180">Status</th>
						<td><label> <select class="select_w130" name="status"
							id="status">
							<option value="">--Select--</option>
							<option value="Draft">Draft</option>
							<option value="Completed">Completed</option>
							<option value="Released">Released</option>
						</select></label></td>
					</tr>
					<tr>
					<th width="180">Est. Wafer Gross</th>
					  <td>
						<label> <select style="width:130px" name="estimated"
							id="estimated">
							<option value="">--Select--</option>
							<option value="1">Yes</option>
							<option value="0">No</option>
							</select></label>					  					  
					  </td>
					 </tr>
					 <tr>
					<th width="180">Compay</th>
					  <td>
						<select class="select_w130" name="releaseTo" id="releaseTo">
							<option value="">--Select--</option>
								<%
									List<FunctionParameterTo> companyList = (List<FunctionParameterTo>) request.getAttribute("companyNameList");
									for (FunctionParameterTo company : companyList) {
										String v = company.getFieldValue();
										%>
								<option value="<%=v %>"><%=company.getFieldShowName() %></option>
										<%
									}
								%>
						</select>				  					  
					  </td>
					</tr>	

				</tbody>
			</table>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right"><input name="button1" type="submit"
						class="button" id="button1" value="Search"> <input
						name="button2" type="Reset" class="button" id="button2"
						value="Reset"></div>
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
				src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
	</tbody>
</table>

<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>
</body>
</html>
