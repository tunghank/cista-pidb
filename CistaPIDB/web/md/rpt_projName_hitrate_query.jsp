<!-- Header---------------------------------------------------------------------
* 2010.03.09/FCG1 @Jere Huang - Initial Version.
* 2010.04.21/FCG2 @Jere Huang - 修改reset功能, 修改Tape out summary > TO count
------------------------------------------------------------------------------->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
	String gridData = "";
	String prodFamily = "";
	String projName = "";
	String projCode = "";
	String prodLine = "";
	
	String reqGridData = (String) request.getAttribute("gridData");
	String reqProdFamily = (String) request.getAttribute("reqProdFamily");
	String reqProjName = (String) request.getAttribute("reqProjName");
	String reqProdLine = (String) request.getAttribute("reqProdLine");
	
	//download 權限
	boolean downloadable = false;
   String isGuest="No";
	RoleDao roleDao = new RoleDao();
	UserTo currentUser = PIDBContext.getLoginUser(request);
	List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());
	if (checkedRoles != null) 
	{
      for (RoleTo roleTo : checkedRoles)
      {
            if (roleTo.getRoleName().equalsIgnoreCase("downloadable") )  
            {
                downloadable = true;
            } 
      }
   } 
	
	if(reqGridData != null )
	{
		gridData = reqGridData;
	}
	if(reqProdFamily != null )
	{
		prodFamily = reqProdFamily;
	}
	if(reqProjName != null )
	{
		projName = reqProjName;
	}
	if(reqProdLine != null )
	{
		prodLine = reqProdLine;
	}
%>

<html>

<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp%>/css/portal.css" rel="stylesheet">

<link href="<%=cp%>/css/resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=cp%>/js/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=cp %>/js/ext-all.js"></script>
<script type="text/javascript" src="<%=cp%>/js/gridToExcel.js"></script>

<script language="javascript">

function resetAll()
{
	$('prodLine').value = '';
	$('prodFamily').value = '';
	$('projName').value = '';
}

function onQuery()
{
	var forma = document.forms(0);
	//至少要選一個條件
	var prodFamily = $("prodFamily").value;
	var projName= $("projName").value;
	var prodLine = $("prodLine").value;
	
	if(prodFamily=="" && projName == "" && prodLine=="")
	{
		Ext.MessageBox.alert('System Alert','Please select one condtion.')
		return;
	}
	else
	{	
		Ext.MessageBox.wait('送交中','資料處理中');
   	forma.submit();
	}
}

Ext.onReady(function()
{
		var resolution = screen.width+' x '+screen.height;
		var divWidth;
		var divHeight;
		if (resolution == "1024 x 768")
		{
			divWidth = screen.width * 0.84;
			divHeight = screen.height * 0.47;
		}
		else if(resolution == "1280 x 1024")
		{
			divWidth = screen.width * 0.875;
			divHeight = screen.height * 0.6;
		}
		else 
		{
			divWidth = screen.width * 0.84;
			divHeight = screen.height * 0.5;
		}
	   Ext.QuickTips.init();
	
	   var hitRateCM = new Ext.grid.ColumnModel([
	     	new Ext.grid.RowNumberer(),
			{
	          id:'family',
	          header: "Family",
	          dataIndex: 'family',
	          width: 100
	  	   },{
			   id:'proj_name',
		      header: "Project Name",
		          dataIndex: 'proj_name',
		          width: 100,
		          align: 'right' 
		   },{
			   id:'to_count',
		      header: "TO COUNT",
		          dataIndex: 'to_count',
		          width: 100,
		          align: 'right' 
		   },{
			   id:'mp',
		      header: "MP",
		          dataIndex: 'mp',
		          width: 100,
		          align: 'right' 
		   },{
			   id:'nto',
		      header: "NTO",
		          dataIndex: 'nto',
		          width: 100,
		          align: 'right' 
		   },{
			   id:'to_project',
		      header: "IC Family",
		          dataIndex: 'to_project',
		          width: 150,
		          align: 'right' 							   			
		   }
		]);
	
	   // by default columns are sortable
	   hitRateCM.defaultSortable = true;
	
	   var hitRateData = [		
	      <%=gridData%>	
	 	];
		
		// create the Data Store
	   var hitRateStore = new Ext.data.Store({
		   proxy:new Ext.data.MemoryProxy(hitRateData),
		   reader:new Ext.data.ArrayReader({},[
				{name:'family'}, 
				{name:'proj_name'},  
				{name:'to_count', type: 'int'},				 
				{name:'mp'}, 
				{name:'nto'},
				{name:'to_project'}
		   ]),
			sortInfo:{ field: "family", direction: "ASC" }
	 	});
			
		//trigger the data store load
		hitRateStore.load();
		
		//export excel
		var excelbutton = new Ext.Button({
			 text: 'Excel',
			 icon: '../images/excel.png',
			 cls: 'x-btn-text-icon',
			 handler: function() {
		    var vExportContent = hitRateGrid.getExcelXml();
			    if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2 || Ext.isSafari3) {
			          var dataURL = '/md/exportexcel.jsp';
			          params =[{
			               name: 'ex',
			               value: vExportContent 
			          },{
			              name: 'FileName',
			              value: 'exportExcel.xls'
			          }];
			          post_to_url(dataURL, params, 'post');
			    } else {
			         document.location = 'data:application/vnd.ms-excel;base64,' + Base64.encode(vExportContent);
			    } //end if
		    } //end handler
		}); //end newbutton
		
		<%if(!downloadable){%>
	  	  excelbutton.hidden = true;
	   <%}%>
	   
		// create the editor grid
		var hitRateGrid = new Ext.grid.GridPanel(
		{
		  	  ds: hitRateStore,
		     cm: hitRateCM,        	      
		     renderTo: 'data-grid',
		     title:'Hit Rate List.',
		     frame:true,
			  loadMask: true,
			  stripeRows: true,  //斑馬線
			  iconCls:'icon-grid',
			  columnLines: true,
			  autoHeight: true,  //最大頁面
		     enableRowBody: true,	     
		     tbar: new Ext.Toolbar({
		           buttons: [excelbutton]
		     })
		});
   //------------------------------------------------------end grid

	   
}); //end Ext.onReady

</script>
<style type="text/css">
   .x-grid3-row{background-color: #E3EEF8 ; } 
   .x-grid3-row-alt{background-color:#FFFFFF;}
   .x-grid3-row-selected{background:#87CEEB!important;border:1px dotted #a3bae9;}
</style>

</head>

<body>
<form name="projNameHitrateForm" id="projNameHitrateForm" action="<%=cp %>/md/rpt_projName_hitrate_query.do?m=query" method="post">
	<table width="99%" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
		  <td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->		  
			  <jsp:include page="/common/banner.jsp" flush="true" /> 
			  <!-- Content start -->
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			     <tr>
				     <td class="pageTitle">Project Name HitRate Data :: Project Name HitRate Query</td>				  
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
						  <th width="50"><div>Product Line </div></th>
						  <td width="150">
						    <input type="text" class="text" name="prodLine" id="prodLine" value="<%=prodLine%>" > 
							 <img id="prodLineBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
							 <script type="text/javascript">
							   SmartSearch.setup({cp:"<%=cp%>", button:"prodLineBtn", inputField:"prodLine", name:"prodLine", 
								      whereCause:"fun_name='PROJECT' and FUN_FIELD_NAME='PRODUCT_LINE'", mode:0});
				          </script>				  
						  </td>
						  
						  <th width="50"><div>Product Family&#13;</div></th>
						  <td width="150">
						     <input type="text" class="text" name="prodFamily" id="prodFamily" value="<%=prodFamily%>"> 
							  <img id="prodFamilyBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
							  <script type="text/javascript">
							     SmartSearch.setup({cp:"<%=cp%>", button:"prodFamilyBtn", inputField:"prodFamily", name:"prodFamily", autoSearch:false, mode:0});
				           </script>    
						  </td>
						  
						  <th width="80"><div>Project Name</div></th>
						  <td width="150">
							 <input type="text" class="text" name="projName" id="projName" value="<%=projName%>"> 
							 <img id="projNameBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
							 <script type="text/javascript">
							   SmartSearch.setup({cp:"<%=cp%>", button:"projNameBtn", inputField:"projName", name:"ProjectName", mode:1});
				          </script>  	  							
						  </td>					 						  
			         </tr>
			         <tr>
			         	<td colspan="6">
								<div align="left">
								   <input name="button1" type="button" class="button" id="button1" value="Search" onclick="onQuery()" >
								   &nbsp;&nbsp;&nbsp;
									<input name="button2" type="button" class="button" id="button2" value="Reset" onClick="resetAll()">
									&nbsp;&nbsp;&nbsp;
								</div>
						   </td>
			         </tr>					
				  </tbody>
				  </table>
								
				  <table border="0" cellpadding="0" cellspacing="0" width="100%">
				     <tr>
						  
				     </tr>
				  </table>
			  </div>
			  
			  <table  id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
			  	  <tbody>					
					   <tr>
						   <td>
						   	<div id="data-grid"></div>
						   </td>						   
					   </tr>	
										   			   							   
				  </tbody>
			  </table>
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
