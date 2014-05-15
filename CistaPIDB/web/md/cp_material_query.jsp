<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.md.to.CpMaterialTo"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">


function selectProjCodeWVersion() {
	var target = "<%=cp%>/dialog/select_icWafer_projCodeWVersion.do?m=pre&callback=selectProjCodeWVersionComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_icWafer_projCodeWVersion","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectProjCodeWVersionComplete(selectedItems) {
	if (selectedItems && selectedItems.length>0) {
		var selects = "";
		for(var i=0; i<selectedItems.length; i++) {
			selects += "," + selectedItems[i];
		}
		if (selects.length > 0) {
			$('projCodeWVersion').value = selects.substring(1);
		}
	}
}

</script>
</head>
<body>
<form id="cpMaterialQueryForm" action="<%=cp %>/md/cp_material_query.do?m=query" method="post">	
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Master Data :: CP Material Query</td>
  </tr>
</table>
			<div class="content">
			<table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="180"><div>CP Material Num.</div></th>
					  <td><input class="text" type="text" id="cpMaterialNum" name="cpMaterialNum">
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="cpMaterialNumSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"cpMaterialNumSSBtn",
                                inputField:"cpMaterialNum",
								title:"CP Material Number",
                                table:"PIDB_CP_MATERIAL",
                                keyColumn:"CP_MATERIAL_NUM",
                                mode:1
							});
							</script>
							</td>
					</tr>		
					<tr>
						<th width="180">Project Code w Version</th>
					  <td><input class="text" type="text" id="projectCodeWVersion" name="projectCodeWVersion" >
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeWVersionSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projectCodeWVersion",
                                table:"PIDB_CP_MATERIAL",
                                keyColumn:"PROJECT_CODE_W_VERSION",
                                title:"Project Code w Version",
                                mode:1
							});
						
							</script>
							</td>
					</tr>
					<tr>
						<th width="180">CP VARIANT</th>
					  <td><input class="text" type="text" id="cpVariant" name="cpVariant" >
					  <img src="<%=cp%>/images/lov.gif" alt="LOV" id="cpVariantSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"cpVariantSSBtn",
                                inputField:"cpVariant",
                                table:"PIDB_CP_MATERIAL",
                                keyColumn:"CP_VARIANT",
                                title:"CP VARIANT",
                                mode:1
							});
						
							</script>
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