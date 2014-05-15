<!-- Header---------------------------------------------------------------------
* 2010.02.5 /FCG1 @Jere Huang - Initial Version.
* 2010.04.21/FCG2 @Jere Huang - 修改reset功能
------------------------------------------------------------------------------->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>

<%
		String gridColumnList = "";
		String ColumnReaderList = "";
		String gridData = "";
		String reqGridColumnList = (String) request.getAttribute("gridColumn");
		String reqColumnReader = (String) request.getAttribute("gridColumnReader");
		String reqGridData = (String) request.getAttribute("gridData");
		
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
	$('projName').value = '';
}

Ext.onReady(function()
{	
		var resolution = screen.width+' x '+screen.height;
		var divWidth;
		var divHeight;
		if (resolution == "1024 x 768"){
			divWidth = screen.width * 0.84;
			divHeight = screen.height * 0.47;
		}else if(resolution == "1280 x 1024"){
			divWidth = screen.width * 0.875;
			divHeight = screen.height * 0.6;
		}else {
			divWidth = screen.width * 0.84;
			divHeight = screen.height * 0.5;
		}
      Ext.QuickTips.init();

		var masksetCM = new Ext.grid.ColumnModel([
         <%=gridColumnList%>
      ]);

      // by default columns are sortable
      masksetCM.defaultSortable = true;

     	var masksetData = [
			<%=gridData%>
     	];  	
     	// create the Data Store
      var masksetStore = new Ext.data.Store({
  		   proxy:new Ext.data.MemoryProxy(masksetData),
  		   reader:new Ext.data.ArrayReader({},[
				<%=ColumnReaderList%> 
  		   ])
		   //,sortInfo:{ field: "maskLayer", direction: "ASC" }   
     	});

     	// trigger the data store load
     	masksetStore.load();
     	//export excel
    	var excelbutton = new Ext.Button({
    		 text: 'Excel',
    		 icon: '../images/excel.png',
    		 cls: 'x-btn-text-icon',
    		 handler: function() 
    		 {
	    	    var vExportContent = masksetGrid.getExcelXml();
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
      var masksetGrid = new Ext.grid.EditorGridPanel(
    	{
      	ds: masksetStore,
         cm: masksetCM,        
         renderTo: 'data-grid',
         title:'Maskset List.',
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

      //button
  	   var checkButton = new Ext.Button({
		   text: 'Color',   		 
		   cls: 'button_align_center',
		   minWidth: 60,
		   renderTo: 'showBtn',
		   handler: function() 
		   {
		 		var columnCount= masksetCM.getColumnCount();		 		
  			   var rowCount =  masksetStore.getTotalCount();
  			   
  				for(var i = 0; i< rowCount; ++i)
  	  			{
  					var cellOldData = "";
  	  	  			for(var j=2;j<columnCount; ++j)
  	  	  			{
  	  	  	  			//取後面欄位與前面比對
  	  	  	  			var cellData = masksetGrid.getView().getCell(i,j).innerText;
  	  	  	  			if(cellOldData != cellData)
  	  	  	  			{
  							masksetGrid.getView().getCell(i,j).style.backgroundColor='#FFFF00';
  	  	  	  			}
  	  	  	  			cellOldData = cellData;
  	  	  			}
  	  			}
         } //end handler
      }); //end saveButton
      
    //------------------------------------------------------end grid

    
}); //end Ext.onReady

</script>
</head>

<body>

<form name="masksetForm" id="masksetForm" action="<%=cp %>/md/rpt_maskset_query.do?m=query" method="post">
	<table width="99%" border="0" cellpadding="0" cellspacing="0">
		<tbody>
		<tr>
		  <td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->		  
			  <jsp:include page="/common/banner.jsp" flush="true" /> 
			  <!-- Content start -->
			  <table width="100%" border="0" cellspacing="0" cellpadding="0">
			     <tr>
				     <td class="pageTitle">Maskset Data :: Maskset Query</td>
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
						  <th width="180"><div>Project Name</div></th>
						  <td width="180">
							   <select class="select_w130" name="projName" id="projName">						
								  <option value="">--Select--</option>
									  <%
									  String reqProjName =(String) request.getAttribute("reqProjName");
									  List<String> projNameList = (List<String>) request.getAttribute("projNameList");
									  if(projNameList != null)
									  {
									    for(String nameObj : projNameList) 
									    {
									    	 String sOptional = "";
									       if(nameObj.equals(reqProjName))
									       {
									    	    sOptional = "selected";
									       }
									    %>							  
									  	     <option value="<%=nameObj %>" <%=sOptional%> ><%=nameObj %></option>
									    <%
									    }
									  }
									  %>
						 		</select>
						  </td>
						  <td>
						    <div align="left">
							   <input name="button1" type="submit" class="button" id="button1" value="Search">
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
					   
					    <tr>
					   <td>
					   	<div id="showBtn"></div>
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