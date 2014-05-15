<!-- Header---------------------------------------------------------------------
* 2010.03.04/FCG1 @Jere Huang - Initial Version.
* 2010.04.21/FCG2 @Jere Huang - 修改reset功能
------------------------------------------------------------------------------->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
		String gridColumnList = "";
		String ColumnReaderList = "";
		String gridData = "";
		String prodFamily = "";
		String prodLine = "";
		
		String reqGridColumnList = (String) request.getAttribute("gridColumn");
		String reqColumnReader = (String) request.getAttribute("gridColumnReader");
		String reqGridData = (String) request.getAttribute("gridData");
		//選單資料
		String reqYearFrom = (String) request.getAttribute("reqYearFrom");
		String reqYearTo = (String) request.getAttribute("reqYearTo");
		String reqMonthFrom = (String) request.getAttribute("reqMonthFrom");
		String reqMonthTo = (String) request.getAttribute("reqMonthTo");
		String reqProdFamily = (String) request.getAttribute("reqProdFamily");
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
		if(reqProdFamily != null )
		{
			prodFamily = reqProdFamily;
		}
		if(reqProdLine != null )
		{
			prodLine = reqProdLine;
		}
		//year list , - 10
		ArrayList<String> yearArrayList = new ArrayList<String>();
		for(int i=-10;i<=0; i++)
		{
			Calendar cal = Calendar.getInstance();	
			cal.add(Calendar.YEAR,i);
			yearArrayList.add(Integer.toString(cal.get(Calendar.YEAR)) );
		}
		//月份
		ArrayList<String> monthArrayList = new ArrayList<String>();
		NumberFormat nf = new DecimalFormat("00");
		for(int i=1;i<=12;i++)
		{
			monthArrayList.add(nf.format(i) );
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

//FCG2
function resetAll()
{
	$('prodLine').value = '';
	$('prodFamily').value = '';
	
	$('tapeOutYearFrom').value = '<%=yearArrayList.get(0)%>';
	$('tapeOutMonthFrom').value = '01';
	
	$('tapeOutYearTo').value = '<%=yearArrayList.get(0)%>';
	$('tapeOutMonthTo').value = '01';

}

function onQuery()
{
	
	var forma = document.forms(0);

	var yearMonthFrom =parseInt($("tapeOutYearFrom").value + $("tapeOutMonthFrom").value);
	var yearMonthTo = $("tapeOutYearTo").value + $("tapeOutMonthTo").value;

	
	if(yearMonthFrom > yearMonthTo)
	{
		Ext.MessageBox.alert('System Alert','The period date is not validate.')
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
	
	   var tapeOutCM = new Ext.grid.ColumnModel([
	      new Ext.grid.RowNumberer(),
			{
            id:'to_month',
            header: "To Month",
            dataIndex: 'to_month',
            width: 100
		   },{
			   id:'proj_name',
		      header: "Project Name",
            dataIndex: 'proj_name',
            width: 100,
            align: 'right' 
		   },{
			   id:'version',
		      header: "version",
            dataIndex: 'version',
            width: 80,
            align: 'right' 
		   },{
			   id:'to_mask',
		      header: "To Mask",
            dataIndex: 'to_mask',
            width: 400,
            align: 'right' 
		   },{
			   id:'ic_family',
		      header: "IC Family",
            dataIndex: 'ic_family',
            width: 100,
            align: 'right' 
		   },{
			   id:'wafer_inch',
		      header: "Wafer Inch",
            dataIndex: 'wafer_inch',
            width: 80,
            align: 'right' 
		   },{
			   id:'tape_out_date',
		      header: "Tape Out Date",
            dataIndex: 'tape_out_date',
            width: 100,
            align: 'right' 
		   },{
		      id:'revision_item',
		      header: "Revision Item",
            dataIndex: 'revision_item',
            width: 100,
            align: 'right' 
		   },{
		   	id:'tape_out_qty',
		      header: "Tape Out Qty",
            dataIndex: 'tape_out_qty',
            width: 80,
            align: 'right' 							   			
		   }
	   ]);
	
	   // by default columns are sortable
	   tapeOutCM.defaultSortable = true;
	
	   var masksetData = [		
	       <%=gridData%>	
	  	];
	
		// create the Data Store
	   var tapeOutStore = new Ext.data.Store({
		   proxy:new Ext.data.MemoryProxy(masksetData),
		   reader:new Ext.data.ArrayReader({},[
				{name:'to_month'}, 
				{name:'proj_name'}, 
				{name:'version'}, 
				{name:'to_mask'}, 
				{name:'ic_family'}, 
				{name:'wafer_inch'}, 
				{name:'tape_out_date'}, 
				{name:'revision_item'}, 
				{name:'tape_out_qty', type: 'int'}
		   ]),
			sortInfo:{ field: "to_month", direction: "ASC" }
	  	});
	 	
		// trigger the data store load
	 	tapeOutStore.load();
	
	 	//export excel
		var excelbutton = new Ext.Button({
			 text: 'Excel',
			 icon: '../images/excel.png',
			 cls: 'x-btn-text-icon',
			 handler: function() {
		    var vExportContent = tapeOutGrid.getExcelXml();
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
	  var tapeOutGrid = new Ext.grid.GridPanel(
	  {
	   	ds: tapeOutStore,
	      cm: tapeOutCM,        	      
	      renderTo: 'data-grid',
	      title:'ProdTapeOut List.',
	      frame:true,
	 		loadMask: true,
	 	   stripeRows: true,  //斑馬線
	 	   iconCls:'icon-grid',
	 	   columnLines: true,
	 	   autoHeight: true,  //最大頁面
	      //clicksToEdit: 1,
	      viewConfig: {
	      	enableRowBody: true,
	      	getRowClass: function(record, rowIndex, p, ds)
	      	             {
            					 var cls = 'totalRowCls';
            				 	 if(record.get('tape_out_qty')>0 )
            				 	 {
                				 	 return cls;
                				 }	
		    	             }
	      },
	      tbar: new Ext.Toolbar({
	            buttons: [excelbutton]
	      })
	  });
	  //------------------------------------------------------end grid
	
}); //end Ext.onReady

</script>

<style type="text/css">
   .x-grid3-row{background-color: #E3EEF8 ; } 
   .totalRowCls{background-color: #9ACD32 !important;}
   .x-grid3-row-selected{background:#87CEFA!important;border:1px dotted #a3bae9;}
</style>

</head>

<body>
<form name="prodTapeOutForm" id="prodTapeOutForm" action="<%=cp %>/md/rpt_tapeout_detail_query.do?m=query" method="post">
	<table width="99%" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
		  <td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->		  
			  <jsp:include page="/common/banner.jsp" flush="true" /> 
			  <!-- Content start -->
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			     <tr>
				     <td class="pageTitle">Tape Out Detail Data :: Tape Out Detail Query</td>
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
						  <th width="180"><div>Tape Out Date From &#13;</div></th>
						  <td width="130">
							   <select class="select_w50" name="tapeOutYearFrom" id="tapeOutYearFrom">
							   	<%for(String yearObj:yearArrayList)
							   	  { 
							   		  if(yearObj.equals(reqYearFrom))
							   		  {
							   	%>					   
							   		     <option value="<%=yearObj%>" selected><%=yearObj%></option>
							   	<%   }
							   		  else
							   		  {
							   	%>							   	
							   			 <option value="<%=yearObj%>"><%=yearObj%></option>
							   	<%   }
							   	  }
							   	%>
							   </select> 
							   <select name="tapeOutMonthFrom" id="tapeOutMonthFrom">							   
							   	<%
							   	for(String monthObj:monthArrayList)
							   	{ 
							   		if(monthObj.equals(reqMonthFrom))
							   		{
							   	%>					   
							   	      <option value="<%=monthObj%>" selected><%=monthObj%></option>
							   	<% }
							   		else
							   		{
							   	%>
							   			<option value="<%=monthObj%>"><%=monthObj%></option>
							   	<%  }
							   	}
							   	%>
							   </select>  							  
						  </td>
						  <th width="30"><div>To&#13;</div></th>
						  <td width="150">
							   <select class="select_w50" name="tapeOutYearTo" id="tapeOutYearTo">
							   	<%for(String yearObj:yearArrayList)
							   	  { 
							   		  if(yearObj.equals(reqYearTo))
							   		  {
							   	%>					   
							   		     <option value="<%=yearObj%>" selected><%=yearObj%></option>
							   	<%   }
							   		  else
							   		  {
							   	%>							   	
							   			 <option value="<%=yearObj%>"><%=yearObj%></option>
							   	<%   }
							   	  }
							   	%>
							   </select> 
							   <select name="tapeOutMonthTo" id="tapeOutMonthTo">
							   	<%
							   	for(String monthObj:monthArrayList)
							   	{ 
							   		if(monthObj.equals(reqMonthTo))
							   		{
							   	%>					   
							   	      <option value="<%=monthObj%>" selected><%=monthObj%></option>
							   	<% }
							   		else
							   		{
							   	%>
							   			<option value="<%=monthObj%>"><%=monthObj%></option>
							   	<%  }
							   	}
							   	%>
							   </select>  	  							
						  </td>
						  
						  <th width="30"><div>Product Line&#13;</div></th>
						  <td width="150">
						     <input type="text" class="text" name="prodLine" id="prodLine" value="<%=prodLine%>"> 
							  <img id="prodLineBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
							  <script type="text/javascript">
							     SmartSearch.setup({cp:"<%=cp%>", button:"prodLineBtn", inputField:"prodLine", name:"prodLine",
							    	   keyColumn:"FIELD_VALUE", whereCause:"fun_name='PROJECT' and FUN_FIELD_NAME='PRODUCT_LINE'", mode:0});
				           </script>
						  </td>
						  
						  <th width="30"><div>Product Family&#13;</div></th>
						  <td width="150">
						     <input type="text" class="text" name="prodFamily" id="prodFamily" value="<%=prodFamily%>"> 
							  <img id="prodFamilyBtn" src="<%=cp%>/images/lov.gif" alt="LOV">
							  <script type="text/javascript">
							     SmartSearch.setup({cp:"<%=cp%>", button:"prodFamilyBtn", inputField:"prodFamily", name:"prodFamily",
							    	 autoSearch:false, mode:0});
				           </script>    
						  </td>
						</tr>
					   <tr>
						  <td colspan="8">
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
