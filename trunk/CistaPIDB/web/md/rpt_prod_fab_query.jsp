<!-- Header---------------------------------------------------------------------
* 2010.02.22/FCG1 @Jere Huang - Initial Version.
------------------------------------------------------------------------------->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
		String gridColumnList = "";
		String ColumnReaderList = "";
		String gridData = "";
		String productName = "";
		String reqGridColumnList = (String) request.getAttribute("gridColumn");
		String reqColumnReader = (String) request.getAttribute("gridColumnReader");
		String reqGridData = (String) request.getAttribute("gridData");
		String reqProdName =(String) request.getAttribute("reqProdName");
		
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
		
		if(reqGridColumnList != null )
		{
			gridColumnList = reqGridColumnList;
		}
		if(reqColumnReader != null )
		{
			ColumnReaderList = reqColumnReader;
		}
		if(reqGridData != null )
		{
			gridData = reqGridData;
		}
		if(reqProdName != null )
		{
			productName = reqProdName;
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
	$('prodName').value = '';
}

function onQuery()
{
	
	var forma = document.forms(0);
	if($("prodName").value == '')
	{
		Ext.MessageBox.alert('System Alert','Please select product name.')
		return;
	}
	else
	{
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

   var prodFabCM = new Ext.grid.ColumnModel([
      <%=gridColumnList%>
   ]);

   // by default columns are sortable
   prodFabCM.defaultSortable = true;

  	var masksetData = [
		<%=gridData%>
  	];  	
  	// create the Data Store
   var prodFabStore = new Ext.data.Store({
	   proxy:new Ext.data.MemoryProxy(masksetData),
	   reader:new Ext.data.ArrayReader({},[
			<%=ColumnReaderList%> 
	   ])
  	});

  	// trigger the data store load
  	prodFabStore.load();

  //export excel
	var excelbutton = new Ext.Button({
		 text: 'Excel',
		 icon: '../images/excel.png',
		 cls: 'x-btn-text-icon',
		 handler: function() 
		 {
	    	 var vExportContent = prodFabGrid.getExcelXml();
		    if (Ext.isIE6 || Ext.isIE7 || Ext.isSafari || Ext.isSafari2 || Ext.isSafari3) 
			 {
		          var dataURL = '/md/exportexcel.jsp';
		          params =[{
		               name: 'ex',
		               value: vExportContent 
		          },{
		              name: 'FileName',
		              value: 'exportExcel.xls'
		          }];
		          post_to_url(dataURL, params, 'post');
		    } 
		    else
			 {
		         document.location = 'data:application/vnd.ms-excel;base64,' + Base64.encode(vExportContent);
		    } //end if
       } //end handler
   }); //end newbutton

	<%if(!downloadable){%>
  	excelbutton.hidden = true;
   <%}%>

   // create the editor grid
   var prodFabGrid = new Ext.grid.EditorGridPanel(
 	{
   	ds: prodFabStore,
      cm: prodFabCM,        
      //viewConfig: {forceFit:true},
      renderTo: 'data-grid',
      title:'ProdFab List.',
      frame:true,
 		loadMask: true,
 	   stripeRows: true,  //斑馬線
 	   iconCls:'icon-grid',
 	   columnLines: true,
 	   autoHeight: true,  //最大頁面
      clicksToEdit: 1,
      tbar: new Ext.Toolbar({
          buttons: [excelbutton]
        })
   });

   //------------------------------------------------------end grid
  
}); //end Ext.onReady

</script>
<!-- 自動換行 -->
<style type="text/css">
 .x-grid3-cell-inner, .x-grid3-hd-inner { white-space:normal !important; }
 .x-grid3-row, .x-grid3-hd-inner{background-color:#FFFFFF;}
 .x-grid3-row-alt, .x-grid3-hd-inner{background-color:#F0C1FF;}
</style>

</head>

<body>
<form name="prodFabForm" id="prodFabForm" action="<%=cp %>/md/rpt_prod_fab_query.do?m=query" method="post">
	<table width="99%" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
		  <td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->		  
			  <jsp:include page="/common/banner.jsp" flush="true" /> 
			  <!-- Content start -->
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			     <tr>
				     <td class="pageTitle">Fab Data :: ProdName Query</td>
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
						  <th width="180"><div>Product Name&#13;</div></th>
						  <td width="180">
							   <input type="text" class="text" name="prodName"	id="prodName" value="<%=productName%>"> 
							   <img src="<%=cp%>/images/lov.gif" alt="LOV" id="prodNameSSBtn">
								<script type="text/javascript">
									SmartSearch.setup({
		                            cp:"<%=cp%>",
		                            button:"prodNameSSBtn",
		                            inputField:"prodName",
		                            name:"ProductName",
		                            mode:0
									});
								</script>
						  </td>
						  
						   <td>
								<div align="left">
								   <input name="button1" type="button" class="button" id="button1" value="Search" onclick="onQuery()">
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
						  <br>
				     </tr>
				  </table>
			  </div>
			  
			  <table class="formFull" id="queryCriteriaPanel" border="0" cellpadding="1" cellspacing="1">
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