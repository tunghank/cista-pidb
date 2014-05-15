<!-- Header---------------------------------------------------------------------
* 2010.01.11/FCG1 @Jere Huang - Initial Version.
------------------------------------------------------------------------------->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List"%>

<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.to.FabCodeTo"%>

<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>

<body>
<form id="partnoLinkQueryForm" action="<%=cp %>/md/rpt_link_query.do?m=query" method="post">	
	<table width="99%" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
		  <td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
		  
		  <jsp:include page="/common/banner.jsp" flush="true" /> 
		  <!-- Content start -->
		  <table width="100%" border="0" cellspacing="0" cellpadding="0">
		     <tr>
			     <td class="pageTitle">Link Data :: Link Query</td>
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
					  <th width="180"><div>Part No</div></th>
					  <td>
						   <input type="text" class="text" name="partNo" id="partNo" value=""> 
							<img id="partNoBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
							<script type="text/javascript">
							    SmartSearch.setup({cp:"<%=cp%>", button:"partNoBtn", inputField:"partNo", name:"PartNo", mode:1});
							</script>
					  </td>
		         </tr>
		         
					<tr>
					  <th width="180"><div>Project Code</div></th>
					  <td>
							<input type="text" class="text" name="projCode" id="projCode"> 
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
							<script type="text/javascript">
							    SmartSearch.setup({cp:"<%=cp%>", button:"projCodeSSBtn", inputField:"projCode", name:"ProjectCode", mode:1});
							</script>
					  </td>
					</tr>
						         
					<tr>
					  <th width="180"><div>FAB</div></th>
					  <td>
						 <select class="select_w130" name="fab" id="fab">
							  <option value="">--Select--</option>
							  <%
							  List<FabCodeTo> fabCodeList = (List<FabCodeTo>) request.getAttribute("fabCodeList");
							  if(fabCodeList != null)
							  {
							    for(FabCodeTo fab : fabCodeList) 
							    {
							  %>							  
							  	  <option value="<%=fab.getFab() %>"><%=fab.getFab() %>,<%=fab.getFabDescr() %></option>
							  <%
							    }
							  }
							  %>
						 </select>
						</td>
				   </tr>
				   
				   <tr>
					  <th width="180"><div>Tape Name</div></th>
					  <td>
							<input type="text" class="text" name="tapeName" id="tapeName"> 
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeNameSSBtn">
							<script type="text/javascript">
							    SmartSearch.setup({cp:"<%=cp%>", button:"tapeNameSSBtn", inputField:"tapeName", name:"TapeName", mode:1});
							</script>
					  </td>
					</tr>
					
					<tr>
					  <th width="180"><div>CP Tester</div></th>
					  <td>
							<input type="text" class="text" name="cpTester" id="cpTester"> 
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="cpTesterSSBtn">
							<script type="text/javascript">
							    SmartSearch.setup({cp:"<%=cp%>", button:"cpTesterSSBtn", inputField:"cpTester", name:"CpTester", mode:1});
							</script>
					  </td>
					</tr>
					
					<tr>
					  <th width="180"><div>FT Tester</div></th>
					  <td>
							<input type="text" class="text" name="ftTester" id="ftTester"> 
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="ftTesterSSBtn">
							<script type="text/javascript">
							    SmartSearch.setup({cp:"<%=cp%>", button:"ftTesterSSBtn", inputField:"ftTester", name:"FtTester", mode:1});
							</script>
					  </td>
					</tr>
										
			  </tbody>
			  </table>
							
			  <table border="0" cellpadding="0" cellspacing="0" width="100%">
			     <tr>
					   <td>
						<div align="right"><input name="button1" type="submit"
							class="button" id="button1" value="Search"> <input
							name="button2" type="Reset" class="button" id="button2"
							value="Reset">
						</div>
					   </td>
			     </tr>
			  </table>
		  </div>
		  <!-- Content end -->
	     </td>
		  <td width="5" valign="bottom"	background="<%=cp %>/images/shadow-1.gif">
			  <table width="100%" border="0" cellpadding="0" cellspacing="0"	background="<%=cp %>/images/bgs.gif">
			  <tr>
				  <td height="15">
				     <img src="<%=cp %>/images/spacer.gif"	width="1" height="1" alt="">
				  </td>
			  </tr>
			  </table>
		  </td>
		  
	   </tr>
		<tr>
			<td colspan="2"><img height="2" alt="" src="<%=cp %>/images/shadow-2.gif" width="100%" border="0"></td>
		</tr>
		</tbody>
	</table>


<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
</form>


</body>
</html>