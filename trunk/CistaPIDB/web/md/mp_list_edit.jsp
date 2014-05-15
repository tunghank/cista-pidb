<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.cista.pidb.md.to.MpListTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterVendorDao"%>
<%@ page import="com.cista.pidb.code.dao.SapMasterCustomerDao"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterVendorTo"%>
<%@ page import="com.cista.pidb.md.to.MpListEolCustTo"%>
<%@ page import="com.cista.pidb.md.to.MpListCustomerMapTo"%>
<%@ page import="com.cista.pidb.code.to.SapMasterCustomerTo"%>

<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.RoleDao"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.code.to.FunctionParameterTo" %>

<%
	MpListTo ref = (MpListTo) request.getAttribute("ref");
	List<MpListEolCustTo> eolCustList = (List)request.getAttribute("eolCustList");
	List<SapMasterCustomerTo> smcList = (List)request.getAttribute("custList");
	List<SapMasterVendorTo> smcVendorList = (List)request.getAttribute("vendorList");
	//For Customer Map Vendor
	List<MpListCustomerMapTo> tapeCustList = (List)request.getAttribute("tapeCustMapList");
	List<MpListCustomerMapTo> bumpCustList = (List)request.getAttribute("bumpCustMapList");
	List<MpListCustomerMapTo> cpCustList = (List)request.getAttribute("cpCustMapList");
	List<MpListCustomerMapTo> assyCustList = (List)request.getAttribute("assyCustMapList");
	List<MpListCustomerMapTo> ftTestCustList = (List)request.getAttribute("ftTestCustMapList");
	List<MpListCustomerMapTo> polishCustList = (List)request.getAttribute("polishCustMapList");
	List<MpListCustomerMapTo> cfCustList = (List)request.getAttribute("cfCustMapList");
	List<MpListCustomerMapTo> wcfCustList = (List)request.getAttribute("wcfCustMapList");
	List<MpListCustomerMapTo> cspCustList = (List)request.getAttribute("cspCustMapList");
	//FCG1
	List<FunctionParameterTo> custTrayAlarmList = (List)request.getAttribute("trayCustAlarmMapList");
	
	
	String isGuest="No";
	RoleDao roleDao = new RoleDao();
	UserTo currentUser = PIDBContext.getLoginUser(request);
	List<RoleTo> checkedRoles = roleDao.findRoleByUserId(currentUser.getId());
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<!-- CSS Config -->
<link href="<%=cp %>/css/resources/css/ext-all.css" rel="stylesheet" type="text/css"/>
<!-- JS Config -->
<script type="text/javascript" src="<%=cp%>/js/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=cp%>/js/ext-all.js"></script>
<script type="text/javascript">

/*Ext.onReady(function(){
	var win;
	var button = Ext.get('btnModifyEOLCust');

	button.on('click', function(){
		// create the window on the first click and reuse on subsequent clicks
		if(!win){
			win = new Ext.Window({
				applyTo     : 'eolCust-win',
				layout      : 'fit',
				width       : screen.width*0.75,
				height      : screen.height*0.68,
				closeAction :'hide',
				plain       : true,
				//listeners:{"hide":function(obj){
					//document.self.reload();
					//Ext.Msg.alert("close");
					//return false;
				//}},
				items       : new Ext.TabPanel({
					autoTabs       : true,
					activeTab      : 0,
					html:'<iframe width="100%" height="100%" frameborder="0" src=<%=cp%>/dialog/select_mpListEolCustomer.do?m=list&partNum='+$F('partNum')+
			"&icFgMaterialNum="+$F('icFgMaterialNum')+"&projCodeWVersion="+$F('projCodeWVersion')+
			"&tapeName="+$F('tapeName')+"&pkgCode="+$F('pkgCode')+' scrolling="auto" ></iframe>'
				}),

				buttons: [{
					text     : 'Close Window',
					handler  : function(){
						win.hide();
					}
				}]
			});


		}
		win.on( "hide", function(obj){
			window.location.href='<%=cp%>/md/mp_list_edit.do?m=pre&partNum='+$F('partNum')+
			"&icFgMaterialNum="+$F('icFgMaterialNum')+"&projCodeWVersion="+$F('projCodeWVersion')+
			"&tapeName="+$F('tapeName')+"&pkgCode="+$F('pkgCode');
			return false;
		}); 
		win.show(button);
	});
});
*/

function doReleaseToERP(s)
{
  doSave(s);
}

function doSave(s) 
{
		if (s && s=="erp") {
			$('toErp').value = 1;
		} else {
			$('toErp').value = 0;
		}
	
		if ($F('icFgMaterialNum') == "") {
			setMessage("error", "IC FG Material Number is must field.");
			return;
		}
	
		if ($F('prodCode') == "") {
			setMessage("error", "Product Code is must field.");
			return;
		}
		
		if ($F('tapeName') == "") {
			$('tapeName').value = "NA";
		}		
		
		if ($F('pkgCode') == "") {
			$('pkgCode').value = "NA";
		}			
	
    	var incompleted = "";
		if ($F('prodCode') == "") {
			incompleted += ", Product Code";
		}
		if ($F('pkgCode') == "") {
			incompleted += ", Package Code(for COG and TRD PKG)";
		}
		if ($F('projCode') == "") {
			incompleted += ", Project Code";
		}

		if ($F('projCodeWVersion') == "") {
			incompleted += ", Project Code w Version";
		}
		if ($F('tapeName') == "") {
			incompleted += ", Tape Name";
		}
		
		if ($F('mpStatus') == "") {
			incompleted += ", MP Status";
		}
		if ($F('mpReleaseDate') == "") {
			incompleted += ", MP Release Date";
		}
		
		if ($F('assignTo') == "") {
			incompleted += ", AssignTo";
		}
		
		//FCG1 取出customer, 判斷欄位
		var submitForm = true;
		var checkPartNum = $('partNum').value;
		var answer = true;
		var alertMsg = "";
		var i =1;
		var approvCustList = "";
		//取得所有屬於 approveCust 的 <option> 子元素
		var custOptions = document.getElementById('approveCust').getElementsByTagName('option');  
		for (i = 0; i < custOptions.length; i++) //迴圈 
		{ 
			approvCustList = approvCustList + "," + custOptions[i].value;
		}
		for(i=1;i<=4;i++)
		{
			   var checkTrayFlag = false;
				var trayDrawNo1 = $('mpTrayDrawingNo' + i).value;
				<%
				for(FunctionParameterTo custObj:custTrayAlarmList)
				{
					String custName = custObj.getFieldValue().toUpperCase();
				%>
					if(approvCustList.indexOf('<%=custName%>') != -1)
					{
						checkTrayFlag = true;
					}
				<%
				}
				%>
				//check rule
				if(checkTrayFlag && checkPartNum.indexOf('PD')!=-1 )
				{
					//取PD後三碼
					var partNumTraySize =checkPartNum.substr(checkPartNum.indexOf('PD')+2, 3);
					//取12,13碼
					var tray12To13 = trayDrawNo1.substr(11,2);
					
					//250-14, 300-16, 400-20
					if(partNumTraySize =="250")
					{
						 if(tray12To13 !="" && tray12To13 !="14")
						 {
							 alertMsg = alertMsg + "Tray Drawing No.(" + i + "),";  
						 }
					}
					else if(partNumTraySize =="300")
					{
						if(tray12To13 !="" && tray12To13 !="16")
						 {
							 alertMsg = alertMsg + "Tray Drawing No.(" + i + "),";  	  
						 }
					}
					else if(partNumTraySize =="400")
					{
						if(tray12To13 !="" && tray12To13 !="20")
						 {
							 alertMsg = alertMsg + "Tray Drawing No.(" + i + "),";  	  
						 }
					}
				}//end if check rule							
		}
		//有不符合的資料
		if(alertMsg.length >0)
		{
			answer= confirm('IC 厚度與Tray 深度之搭配不符合CMO之規格[' + alertMsg + '] 是否確定Release to ERP?');	  
			submitForm = answer;
		}	
		
		//var submitForm = false;
//		if (incompleted != "") {
//			if (confirm("The following required fields is incomplete, the mpList will be saved as draft:\r\n" + incompleted.substring(2))) {
//			submitForm = true;
//		} else {
//			$('saveBtn').disabled = false;
//			$('resetBtn').disabled = false;
//			setMessage("error", "User cancel.");
//		}
//	} else {
		//submitForm = true;
//	}
	
		if (submitForm ) 
		{
			   setMessage("error", "Saving mpList...");
				selectAllOptions($('approveCust'));
				selectAllOptions($('approveTapeVendor'));
				selectAllOptions($('approveBpVendor'));
				selectAllOptions($('approveCpHouse'));
				selectAllOptions($('approveAssyHouse'));
				selectAllOptions($('approveFtHouse'));
				selectAllOptions($('approvePolishVendor'));
				selectAllOptions($('assignTo'));
				selectAllOptions($('eolCust'));
				selectAllOptions($('approveColorFilterVendor'));
				selectAllOptions($('approveWaferCfVendor'));
				selectAllOptions($('approveCpCspVendor'));
				selectAllOptions($('approveCpTsvVendor'));

			   $('mpListEditForm').submit();	
		}
}


var trays = new Array();
function fetchCogComplete(r) 
{
    var cogs = r.responseXML.getElementsByTagName("CogTo");
    if (!cogs || cogs.length == 0) {
        $('mpTrayDrawingNo1').length = 1;
        $('mpTrayDrawingNoVer1').value = "";
        $('mpColor1').value = "";
        $('mpCustomerName1').value = "";
        
        $('mpTrayDrawingNo2').length = 1;
        $('mpTrayDrawingNoVer2').value = "";
        $('mpColor2').value = "";
        $('mpCustomerName2').value = "";
        
        $('mpTrayDrawingNo3').length = 1;
        $('mpTrayDrawingNoVer3').value = "";
        $('mpColor3').value = "";
        $('mpCustomerName3').value = "";
        
        $('mpTrayDrawingNo4').length = 1;
        $('mpTrayDrawingNoVer4').value = "";
        $('mpColor4').value = "";
        $('mpCustomerName4').value = "";                                                                                                
        return;
    }
    trays.length = 0;   
    trays[0] = new Array();
    trays[1] = new Array();
    trays[2] = new Array();
    trays[3] = new Array();
    for (var i = 1; i < 7; i++) { 
        var obj1 = cogs[0].getElementsByTagName("trayDrawingNo" + i);
        if (obj1.length > 0) {
            trays[0][i - 1] = obj1[0].firstChild.nodeValue;
        }
        
        var obj2 = cogs[0].getElementsByTagName("trayDrawingNoVer" + i);
        if (obj2.length > 0) {
            trays[1][i - 1] = obj2[0].firstChild.nodeValue;
        } else {
        	trays[1][i - 1] = "";
        }

        var obj3 = cogs[0].getElementsByTagName("color" + i);
        if (obj3.length > 0) {
            trays[2][i - 1] = obj3[0].firstChild.nodeValue;
        } else {
        	trays[2][i - 1] = "";
        }
        
        var obj4 = cogs[0].getElementsByTagName("cogCustName" + i);
        if (obj4.length > 0) {
            trays[3][i - 1] = obj4[0].firstChild.nodeValue;
        } else {
        	trays[3][i - 1] = "";                                
        }                                
    }

    if (trays.length >0) {
        $('mpTrayDrawingNo1').options.length = 1;
        $('mpTrayDrawingNo2').options.length = 1;
        $('mpTrayDrawingNo3').options.length = 1;
        $('mpTrayDrawingNo4').options.length = 1;
        for (var i = 0; i < 6; i++) {
            var value = trays[0][i];
            if (typeof value != "undefined") {
                addOption($('mpTrayDrawingNo1'), value, value);
                addOption($('mpTrayDrawingNo2'), value, value);
                addOption($('mpTrayDrawingNo3'), value, value);
                addOption($('mpTrayDrawingNo4'), value, value);
            }
        }
    }
    
	$('mpTrayDrawingNo1').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO1") %>";
	$('mpTrayDrawingNo2').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO2") %>";
	$('mpTrayDrawingNo3').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO3") %>";
	$('mpTrayDrawingNo4').value = "<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO4") %>";
   /*        
        onTrayNo1Change();
        onTrayNo2Change();
        onTrayNo3Change();
        onTrayNo4Change();        
   */
}



function selectSapMasterCustomer() {
	var target = "<%=cp%>/dialog/select_sapMasterCustomer.do?m=list&callback=selectSapMasterCustomerComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterCustomerComplete(selectedProds) {
	//$('approveCust').length=0;
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCust'), prod[1], prod[0]);
		}
	}
}


//Add Form Hank 2008/10/31
function modifyEOLCust() {

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListEolCustomer.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode');

	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}
//For Tape Vendor Map
function modifyTapeVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=TAPE";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}


//For Bump Vendor Map
function modifyBumpVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=BUMP";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For CP House Vendor Map
function modifyCpVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=CP";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For Assembly House Vendor Map
function modifyAssyVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=ASSY";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For FT TEST House Vendor Map
function modifyFtVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=FT";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For Polish Vendor Map
function modifyPolishVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=POLISH";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For Color Filter Vendor Map
function modifyCFVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=CF";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For TurnKey(Wafer+Color Filter) Vendor Map
function modifyWCFVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=WCF";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

//For TurnKey(Wafer+Color Filter) Vendor Map
function modifyCspVendorCust(){

	if ($F('partNum') == "") {	
		setMessage("error", "Part Number is must field.");
		return;
	}
	if ($F('icFgMaterialNum') == "") {
		setMessage("error", "IC FG Material Number is must field.");
		return;
	}

	if ($F('projCodeWVersion') == "") {
		setMessage("error", "Product Code is must field.");
		return;
	}
	
	if ($F('tapeName') == "") {
		setMessage("error", "Tape Name is must field.");
		return;
	}	
	
	if ($F('pkgCode') == "") {
		setMessage("error", "Package Code is must field.");
		return;
	}
	var target ="<%=cp%>/dialog/select_mpListCustomerMap.do?m=list&partNum="+$F('partNum')+
		"&icFgMaterialNum="+$F('icFgMaterialNum')+
		"&projCodeWVersion="+$F('projCodeWVersion')+
		"&tapeName="+$F('tapeName')+
		"&pkgCode="+$F('pkgCode')+"&process=CSP";

	var width = 1000;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_mpListCustomerMap","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendor() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveTapeVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor2() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete2";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete2(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveBpVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor3() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete3";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete3(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCpHouse'), prod[1], prod[0]);
		}
	}
}
function selectSapMasterVendor4() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete4";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendor5() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete5";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete4(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveAssyHouse'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendorComplete5(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveFtHouse'), prod[1], prod[0]);
		}
	}
}


function selectSapMasterVendor6() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete6";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete6(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approvePolishVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor7() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete7";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete7(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveColorFilterVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor8() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete8";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete8(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveWaferCfVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor9() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete9";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete9(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCpCspVendor'), prod[1], prod[0]);
		}
	}
}

function selectSapMasterVendor10() {
	var target = "<%=cp%>/dialog/select_sapMasterVendor.do?m=list&callback=selectSapMasterVendorComplete10";
	var width = 600;
	var height = 400;
	var left = (screen.width-width)/2;
	var top = (screen.height-height)/2;
	var x = window.open(target,"select_sapMasterCustomer","scrollbar=auto, help=false, resizable=false, status=false,height="+height+"px,width="+width+"px,left="+left+"px,top="+top+"px");
	x.focus();
}

function selectSapMasterVendorComplete10(selectedProds) {
	if (selectedProds && selectedProds.length>0) {
		for(var i=0; i<selectedProds.length; i++) {
			var prod = selectedProds[i].split('|');
			addOption($('approveCpTsvVendor'), prod[1], prod[0]);
		}
	}
}

function retrieveEmail() {
	$('retrieveBtn').disabled = true;
	setMessage("error", "Retrieving assign emails...");
	new Ajax.Request(
		'<%=cp%>/ajax/fetch_email.do',
		{
			method: 'get',
			parameters: 'users='+ $F('assignTo'),
			onComplete: retrieveEmailComplete
		}
	);
}
function retrieveEmailComplete(r) {
	var returnValue = r.responseText.split("|");
	var errorItem = "";
	var successItem = "";
	if (returnValue.length == 1) {
		if (r.responseText.startWith("|")) {
			successItem = returnValue[0];
		} else {
			errorItem = returnValue[0];
		}
	} else {
		errorItem = returnValue[0];
		successItem = returnValue[1];
	}
	if (errorItem.length > 0) {
		setMessage("error", "The following user's email not found: " + errorItem + ".");
	} else {
		setMessage("error", "Retrieve user's email success. ");
	}
	
	$('assignEmail').value = successItem;
	$('retrieveBtn').disabled = false;
}

function doInit(){
}
</script>
<script type="text/javascript">
// EN APP 


</script>
</head>
<body onLoad="doInit()">
<!-- Header---------------------------------------------------------------------
* 2010.03.16/FCG1 @Jere Huang - 新增傳入customer及增加判斷alarm.
----------------------------------------------------------------------------- -->
<div id="eolCust-win" class="x-hidden">
		<div class="x-window-header">EOL Customer Detail</div>
</div> 


<form name="mpListEditForm" action="<%=cp %>/md/mp_list_edit.do?m=save" method="post">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->

			<jsp:include page="/common/banner.jsp" flush="true" />

			<!-- Content start -->
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
             <tr>
		          <td class="pageTitle">Master Data :: Modify MP List</td>
             </tr>
           </table>
	
			<div class="content">
			  <table class="formErrorAndButton">
				<tr>
					<td>
					<div id="error" class="formErrorMsg"><html:errors />
                         <%=request.getAttribute("error")!=null?request.getAttribute("error"):""%>
                         &nbsp;					
					</div>
					<!--
					<div id="error" class="formErrorMsg"><html:errors />&nbsp;</div>
					-->
					</td>
					<td>
					<div align="right">
					Created by 
					<input name="createdBy" id="createdBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CREATED_BY") %>">&nbsp;&nbsp;
					Modified by 
					<input name="modifiedBy" id="modifiedBy" type="text" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MODIFIED_BY") %>">&nbsp;&nbsp;</div>
					</td>
				</tr>
			</table>
			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
						<th width="20%"><span class="star">*</span>Part Number</th>
					    <td width="30%" colspan="3"><input class="text_protected" readonly
							name="partNum" id="partNum"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "PART_NUM") %>" > 
					      </td>
					    <th width="20%">Tray Drawing No. (1)</th>
					  <td width="30%"><select name="mpTrayDrawingNo1" id="mpTrayDrawingNo1" class="select_w130" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO1") %>" onChange="onTrayNo1Change()">
					  <script type="text/javascript">
					  function onTrayNo1Change() {
					      var obj = $('mpTrayDrawingNo1');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer1').value = "";
				              $('mpColor1').value = "";
				              $('mpCustomerName1').value = "";			      
					      }
					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer1').value = trays[1][i];
					              $('mpColor1').value = trays[2][i];
					              $('mpCustomerName1').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>
					</tr>
					<tr>
					  <th width="20%"><span class="star">*</span>IC FG Material Number</th>
					  <td width="30%" colspan="3">
					  <input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "IC_FG_MATERIAL_NUM") %>"
							name="icFgMaterialNum" id="icFgMaterialNum">
					  </td>
                      	<th>Tray Drawing No. Ver. (1)</th>
						<td><input class="text_protected" readonly type="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER1") %>"
							name="mpTrayDrawingNoVer1" id="mpTrayDrawingNoVer1"></td>					  
					</tr>
					<tr>
						<th width="20%"><span class="star">*</span>Product Code</th>
					  <td width="30%" colspan="3"><input class="text_protected" readonly
							name="prodCode" id="prodCode"   value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROD_CODE") %>"></td>
						<th>Color / Pocket Qty (1)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR1") %>"
							name="mpColor1" id="mpColor1"></td>							
                    </tr>
					<tr>
						<th width="20%"><span class="star">*</span>Package Code(for COG and TRD PKG)</th>
						<td width="30%" colspan="3"><input class="text_protected" readonly
							name="pkgCode" id="pkgCode" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PKG_CODE") %>">
							<input type="hidden" name="icFgPkgCode" id="icFgPkgCode" value="<%=request.getAttribute("icFgPkgCode") %>">	
							</td>					                         
						<th>COG Customer (1)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME1") %>"
							name="mpCustomerName1" id="mpCustomerName1"></td>	
					</tr>
					<tr>
					  <th width="20%">Project Code</th>
					  <td width="30%" colspan="3"><input class="text_protected" readonly
							name="projCode" id="projCode" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE") %>"> 
					   <!-- <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeSSBtn",
                                inputField:"projCode",
                                name:"ProjectCode",
                                autoSearch:false,
                                mode:0
							});
						
							</script>-->
						</td>
					  <th width="20%">Tray Drawing No. (2)</th>
					  <td width="30%"><select name="mpTrayDrawingNo2" id="mpTrayDrawingNo2" class="select_w130" onChange="onTrayNo2Change()">
					  <script type="text/javascript">
					  function onTrayNo2Change() {
					  	  
					      var obj = $('mpTrayDrawingNo2');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer2').value = "";
				              $('mpColor2').value = "";
				              $('mpCustomerName2').value = "";			      
					      }
					      					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer2').value = trays[1][i];
					              $('mpColor2').value = trays[2][i];
					              $('mpCustomerName2').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>						
				    </tr>
					<tr>
					  <th width="20%"><span class="star">*</span>Project Code w Version</th>
					  <td width="30%" colspan="2"><input class="text_protected" readonly
							name="projCodeWVersion" id="projCodeWVersion" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROJ_CODE_W_VERSION") %>" >
					     <!-- <img src="<%=cp%>/images/lov.gif" alt="LOV" id="projCodeWVersionSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"projCodeWVersionSSBtn",
                                inputField:"projCodeWVersion",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"PROJ_CODE_W_VERSION",
                                title:"Project Code w Version",
                                autoSearch:false,
                                mode:0
							});
						
							</script>-->
						</td>
						<td>
							WF Material Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_WF") %>"
							name="matWf" id="matWf">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matWfLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matWfLov",
                                inputField:"matWf",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"MATERIAL_NUM",
                                whereCause:" PROJ_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
						</script>

						</td> 
                      	<th>Tray Drawing No. Ver. (2)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER2") %>"
							name="mpTrayDrawingNoVer2" id="mpTrayDrawingNoVer2"></td>								
				    </tr>
					<tr>
					  <th width="20%">Tape Name</th>
					  <td width="30%" colspan="3">
					  <input class="text_protected" readonly name="tapeName" id="tapeName"  value="<%=BeanHelper.getHtmlValueByColumn(ref, "TAPE_NAME") %>">
					   <!--   <img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeNameLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeNameLov",
                                inputField:"tapeName",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"TAPE_NAME",
                                whereCause:" PKG_CODE = {icFgPkgCode} ",
                                title:"Tape Name",
                                autoSearch:false,
                                mode:0
							});
						
							</script>-->
						</td>
						<th>Color / Pocket Qty (2)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR2") %>"
							name="mpColor2" id="mpColor2"></td>							

					</tr>
					<tr>
					  <th width="20%">MP Status</th>
					  <td width="30%" colspan="3"><select name="mpStatus" id="mpStatus" class="select_w130">
					      <option value="">--Select--</option>
                          <option value="1" <%="1".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Active</option>
                          <option value="0" <%="0".equals(BeanHelper.getHtmlValueByColumn(ref, "MP_STATUS"))?"selected":"" %>>Inactive</option>
                      </select></td>
						<th>COG Customer (2)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME2") %>"
							name="mpCustomerName2" id="mpCustomerName2"></td>	                      
				    </tr>
					<tr>
					 <th width="20%">MP Release Date</th>
					  <td width="30%" colspan="3"><label>
					  <input class="text" maxlength="20" size="20" readonly
							name="mpReleaseDate" id="mpReleaseDate" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_RELEASE_DATE", "yyyy/MM/dd") %>">
					  </label>
					   <img src="../images/calendar.gif" alt="Calendar" width="19" height="20" id="mpReleaseDateBtn">
						<script type="text/javascript">
							Calendar.setup({
								inputField:"mpReleaseDate",
								ifFormat:"%Y/%m/%d",
								button:"mpReleaseDateBtn"
							});
						</script>
						</td>
					  <th width="20%">Tray Drawing No. (3)</th>
					  <td width="30%"><select name="mpTrayDrawingNo3" id="mpTrayDrawingNo3" class="select_w130" onChange="onTrayNo3Change()">
					  <script type="text/javascript">
					  function onTrayNo3Change() {
					      var obj = $('mpTrayDrawingNo3');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer3').value = "";
				              $('mpColor3').value = "";
				              $('mpCustomerName3').value = "";			      
					      }
					      					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer3').value = trays[1][i];
					              $('mpColor3').value = trays[2][i];
					              $('mpCustomerName3').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>											
				    </tr>
					  <tr>
					  <th rowspan="2">REVISION ITEM</th>
					  <td rowspan="2" colspan="3">
					  <input type="text" readonly name="revisionItem" id="revisionItem" value="<%=BeanHelper.getHtmlValueByColumn(ref, "REVISION_ITEM") %>" class="text_200"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="revisionItemSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"revisionItemSSBtn",
                                inputField:"revisionItem",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='REVISION_ITEM' and fun_name='MP_LIST'",
								orderBy:"ITEM",
                                title:"REVISION_ITEM",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"revisionItemCallback"
							});

							function revisionItemCallback(inputField, columns, value) {
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
							</script></td>
                      	<th>Tray Drawing No. Ver. (3)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER3") %>"
							name="mpTrayDrawingNoVer3" id="mpTrayDrawingNoVer3"></td>	
					</tr>
					<tr>
						<th>Color / Pocket Qty (3)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR3") %>"
							name="mpColor3" id="mpColor3"></td>		
				    </tr>
					<tr>
					    <th width="20%" rowspan="3" style="vertical-align:top">Approve Customer</th>
						<td width="30%" colspan="3" rowspan="3" style="vertical-align:top"><input name="button" type="button" class="button" onClick="selectSapMasterCustomer()" value="Add">
                          <input name="button3" type="button" class="button" onClick="removeSelectedOptions($('approveCust'))" value="Remove">
                          <br>
                          <select name="approveCust" size="3" multiple class="select_w130" id="approveCust">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CUST") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CUST");
					    			String[] list = listStr.split(",");
					    			SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			    SapMasterCustomerTo to = sapMasterCustomerDao.findByVendorCode(s);
						    			    if (to != null) {
						    			%>
						    			<option value="<%=to.getCustomerCode() %>"><%=to.getShortName() %></option>
						    			<%
						    			    }
						    			}
						    		}
					    		}
					    	%>		                               
                          </select></td>					
						<th>COG Customer (3)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME3") %>"
							name="mpCustomerName3" id="mpCustomerName3"></td>	 	
				    </tr>			
				    <tr>
					  <th width="20%">Tray Drawing No. (4)</th>
					  <td width="30%"><select name="mpTrayDrawingNo4" id="mpTrayDrawingNo4" class="select_w130" onChange="onTrayNo4Change()">
					  <script type="text/javascript">
					  function onTrayNo4Change() {
					  	  
					      var obj = $('mpTrayDrawingNo4');
					      
					      if (obj.value == null || obj.value == "") {
				              $('mpTrayDrawingNoVer4').value = "";
				              $('mpColor4').value = "";
				              $('mpCustomerName4').value = "";			      
					      }
					      					      
					      for(var i = 0; i < trays[0].length; i++) {
					          if (trays[0][i] == obj.value) {
					              $('mpTrayDrawingNoVer4').value = trays[1][i];
					              $('mpColor4').value = trays[2][i];
					              $('mpCustomerName4').value = trays[3][i];
					          }
					      }
					  }
					  </script>
                        <option value="">--Select--</option>
                      </select></td>	
                    </tr>
                    <tr>  
                      	<th>Tray Drawing No. Ver. (4)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_TRAY_DRAWING_NO_VER4") %>"
							name="mpTrayDrawingNoVer4" id="mpTrayDrawingNoVer4"></td>	                      							    
				    </tr>	    
				    <tr>
                          <th width="20%" rowspan="3" style="vertical-align:top">Approve Tape Vendor</th>
					     <td width="30%" rowspan="3" style="vertical-align:top"><input name="button2" type="button" class="button" onClick="selectSapMasterVendor()" value="Add">
                           <input name="button2" type="button" class="button" onClick="removeSelectedOptions($('approveTapeVendor'))" value="Remove">
                           <br>
                           <select size="3" multiple class="select_w130" name="approveTapeVendor" id="approveTapeVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_TAPE_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_TAPE_VENDOR");
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
                           </select></td>
						  <td rowspan="3">
						  <% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						  <input name="btnModifyTapeVendorCust" id="btnModifyTapeVendorCust" type="button" class="button" value="Modify" onClick="modifyTapeVendorCust()">
						  <%}%>
						  <!--Tape Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (tapeCustList != null && tapeCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo tapeCust : tapeCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(tapeCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(tapeCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (tapeCust.getTremark() != null ){
													tremark = tapeCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(tapeCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(tapeCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(tapeCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--Tape Vendor Map List Table-->
						  </td>
						<td width="15%"  rowspan="3" style="vertical-align:top">
							Tape Material Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_TAPE") %>"
							name="matTape" id="matWf">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="tapeNameLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"tapeNameLov",
                                inputField:"matTape",
                                table:"PIDB_IC_TAPE",
                                keyColumn:"MATERIAL_NUM",
                                whereCause:" TAPE_NAME = {tapeName} ",
                                title:"Tape Name",
                                autoSearch:false,
                                mode:0
							});
							</script>
						</td>
						<th>Color / Pocket Qty (4)</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_COLOR4") %>"
							name="mpColor4" id="mpColor4"></td>		                           				    
				    </tr>
				    <tr>
						<th>COG Customer (4)</th>
						<td><input class="text" value="<%=BeanHelper.getHtmlValueByColumn(ref, "MP_CUSTOMER_NAME4") %>"
							name="mpCustomerName4" id="mpCustomerName4"></td>	
				    </tr>
				    <tr>
						<th>MCP Die Quantity</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_DIE_QTY") %>"
							name="mcpDieQty" id="mcpDieQty"></td>				    
				    </tr>
				    <tr>
					  <th width="20%" rowspan="3" style="vertical-align:top">Approve Bumping Vendor</th>
					  <td width="30%" rowspan="3" style="vertical-align:top"><input name="button22" type="button" class="button" onClick="selectSapMasterVendor2()" value="Add">
                        <input name="button22" type="button" class="button" onClick="removeSelectedOptions($('approveBpVendor'))" value="Remove">
                        <br>
                        <select size="3" multiple class="select_w130" name="approveBpVendor" id="approveBpVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_BP_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_BP_VENDOR");
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
                        </select></td>
						<td rowspan="3">
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyBumpVendorCust" id="btnModifyBumpVendorCust" type="button" class="button" value="Modify" onClick="modifyBumpVendorCust()">
						<%}%>
						 <!--Bump Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (bumpCustList != null && bumpCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo bumpCust : bumpCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(bumpCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(bumpCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (bumpCust.getTremark() != null ){
													tremark = bumpCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(bumpCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(bumpCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(bumpCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--Bump Vendor Map List Table-->
						</td>
						<td rowspan="3">
						Bump Material Num<P>
						<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_BP") %>"
							name="matBp" id="matBp">
						<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matBpLov">
						<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matBpLov",
                                inputField:"matBp",
                                table:"PIDB_IC_WAFER",
                                keyColumn:"BP",
                                whereCause:" PROJ_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"Bump Material Num",
                                autoSearch:false,
                                mode:0
							});
						</script>
						</td>
						<th>MCP Package</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PKG") %>"
							name="mcpPkg" id="mcpPkg"></td> 
											
				    </tr>
					<tr>
						<th>MCP Product1</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_1") %>"
							name="mcpProd1" id="mcpProd1"></td>	
					</tr>
					<tr>
						<th>MCP Product2</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_2") %>"
							name="mcpProd2" id="mcpProd2"></td>
					</tr>
	
				    <tr>
					  <th width="20%" rowspan="3" style="vertical-align:top">Approve CP House</th>
					     <td width="30%" rowspan="3" style="vertical-align:top"><input name="button222" type="button" class="button" onClick="selectSapMasterVendor3()" value="Add">
                           <input name="button222" type="button" class="button" onClick="removeSelectedOptions($('approveCpHouse'))" value="Remove">
                           <br>
                           <select size="3" multiple class="select_w130" name="approveCpHouse" id="approveCpHouse">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_HOUSE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_HOUSE");
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
                           </select></td>
						   <td rowspan="3">
						   <% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						   <input name="btnModifyCpVendorCust" id="btnModifyCpVendorCust" type="button" class="button" value="Modify" onClick="modifyCpVendorCust()">
						   <%}%>
						    <!--CP House Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (cpCustList != null && cpCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo cpCust : cpCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(cpCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(cpCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (cpCust.getTremark() != null ){
													tremark = cpCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cpCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cpCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cpCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--CP House Vendor Map List Table-->
						   </td>
						<td rowspan="3">
							CP Material Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_CP") %>"
							name="matCp" id="matCp">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matCpLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matCpLov",
                                inputField:"matCp",
                                table:"PIDB_CP_MATERIAL",
                                keyColumn:"CP_MATERIAL_NUM",
                                whereCause:" PROJECT_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"CP_MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
						</script>

						</td>
						<th>MCP Product3</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_3") %>"
							name="mcpProd3" id="mcpProd3"></td>						
				    </tr>
					<tr>
						<th>MCP Product4</th>
						<td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MCP_PROD_4") %>"
							name="mcpProd4" id="mcpProd4"></td>
					</tr>
					<tr>
						<th>Lead Frame Tool</th>
						<td><input class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "LF_TOOL") %>"
							name="lfTool" id="lfTool"></td>
					</tr>
				    <tr>
						<th width="20%" rowspan="2"  style="vertical-align:top">Approve Assembly House</th>
						<td width="30%" rowspan="2"><span style="vertical-align:top">
						  <input name="button2222" type="button" class="button" onClick="selectSapMasterVendor4()" value="Add">
                          <input name="button2222" type="button" class="button" onClick="removeSelectedOptions($('approveAssyHouse'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveAssyHouse" id="approveAssyHouse">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_ASSY_HOUSE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_ASSY_HOUSE");
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
						</span></td>
						<td rowspan="2">
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyAssyVendorCust" id="btnModifyAssyVendorCust" type="button" class="button" value="Modify" onClick="modifyAssyVendorCust()">
						<%}%>
						 <!--Assembly House Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (assyCustList != null && assyCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo assyCust : assyCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(assyCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(assyCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (assyCust.getTremark() != null ){
													tremark = assyCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(assyCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(assyCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(assyCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--Assembly House Vendor Map List Table-->
						</td>
						<td rowspan="2">
							Assy Material Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_AS") %>"
							name="matAs" id="matAs">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matAsLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matAsLov",
                                inputField:"matAs",
                                table:"PIDB_IC_FG_VIEW",
								keyColumn:"A_MATERIAL_NUM",
                                whereCause:" PART_NUM = {partNum} ",
                                title:"Assy Material Num",
                                autoSearch:false,
                                mode:0
							});
						</script>

						</td> 
						<th>Close Lead Frame Name</th>
					  <td><input class="text_200_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "CLOSE_LF_NAME") %>"
							name="closeLfName" id="closeLfName"></td>							
				    </tr>

				    <tr>
						<th>MP Process Flow</th>
						<td width="23%"><input type="text" cols="40" readonly name="processFlow" id="processFlow" value="<%=BeanHelper.getHtmlValueByColumn(ref, "PROCESS_FLOW") %>" class="text_120"> 
					      <img
							src="<%=cp%>/images/lov.gif" alt="LOV" id="MpProcessFlowSSBtn">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"MpProcessFlowSSBtn",
                                inputField:"processFlow",
                                table:"PIDB_FUN_PARAMETER_VALUE",
								keyColumn:"FIELD_VALUE",
                                columns:"FIELD_VALUE,FIELD_SHOW_NAME,ITEM",
								whereCause:"FUN_FIELD_NAME='MP_PROCESS_FLOW' and fun_name='MP_LIST'",
								orderBy:"ITEM",
                                title:"MP_PROCESS_FLOW",
                                mode:0,
                                autoSearch:false,
								callbackHandle:"processFlowCallback"
							});

							function processFlowCallback(inputField, columns, value) {
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
					
					<!-- Add 2008/03/03 for FT TEST House -->
					<tr>
						<th width="20%" style="vertical-align:top">Approve FT TEST<br>(WLM , WLO) House</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor5()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveFtHouse'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveFtHouse" id="approveFtHouse">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_FT_HOUSE") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_FT_HOUSE");
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
						</span></td> 
						<td>
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyFtVendorCust" id="btnModifyFtVendorCust" type="button" class="button" value="Modify" onClick="modifyFtVendorCust()">
						<%}%>
						 <!--FT TEST House Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (ftTestCustList != null && ftTestCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo ftTestCust : ftTestCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(ftTestCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(ftTestCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (ftTestCust.getTremark() != null ){
													tremark = ftTestCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(ftTestCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(ftTestCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(ftTestCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--FT TEST House Vendor Map List Table-->
						</td>
						<td>&nbsp;</td> 
						<th width="20%"  style="vertical-align:top">EOL Customer</th>
						<td width="30%"  style="vertical-align:top">
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyEOLCust" id="btnModifyEOLCust" type="button" class="button" value="Modify" onClick="modifyEOLCust()">
						<%}%>
						<P>
							<!--EOL List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th align="left">Customer</th>
										<th align="left">EOL Date</th>
										<th align="left">Remark</th>
									</tr>
									</tbody>
								<%if (eolCustList != null && eolCustList.size() > 0) {%>
									<%
											int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

											for(MpListEolCustTo eolCust : eolCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(eolCust.getEolCust());
														smc =null!=smc?smc:new SapMasterCustomerTo();
		
														custShortName = smc.getShortName();
														custShortName =null!=custShortName?custShortName:"";

														custShortName = eolCust.getEolCust() + "|" +  custShortName;
													}
												idx ++;
												String tdcss = "class=\"c" + idx % 2+"\"";
									%>
										<tr>
										<td <%=tdcss %>><%=custShortName%>&nbsp;
											<input name='eolCust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(eolCust, "EOL_CUST") %>'>
										</td>
										<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(eolCust, "EOL_DATE", "yyyy/MM/dd") %>&nbsp;
											<input name='eolDate' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(eolCust, "EOL_DATE", "yyyy/MM/dd") %>'>
										</td>
										<td <%=tdcss %>><%=BeanHelper.getHtmlValueByColumn(eolCust, "REMARK") %>&nbsp;

										</td>
										</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--End EOL List Table-->
						  
						  </td>	 				    
				    </tr>
					<!-- Add 2009/02/18 for CP Polish House -->
					<tr>
						<th width="20%" style="vertical-align:top">Approve CP Polish Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor6()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approvePolishVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approvePolishVendor" id="approvePolishVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_POLISH_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_POLISH_VENDOR");
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
						</span></td> 
						<td>
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyPolishVendorCust" id="btnModifyPolishVendorCust" type="button" class="button" value="Modify" onClick="modifyPolishVendorCust()">
						<%}%>
						 <!--Polish Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (polishCustList != null && polishCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo polishCust : polishCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(polishCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(polishCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (polishCust.getTremark() != null ){
													tremark = polishCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(polishCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(polishCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(polishCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--Polish Vendor Map List Table-->
						</td>
						<td>
							Polish Material Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_Polish") %>"
							name="matPolish" id="matPolish">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matPolishLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matPolishLov",
                                inputField:"matPolish",
                                table:"PIDB_CP_POLISH_MATERIAL",
                                keyColumn:"CP_POLISH_MATERIAL_NUM",
                                whereCause:" PROJECT_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"CP_POLISH_MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
						</script>
						</td>
						<th width="20%" style="vertical-align:top">CP BIN(Maximum 256 Char.)</th>
						<td width="30%" style="vertical-align:top"><textarea id="cpBin" name="cpBin" cols="40" rows="3" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "CP_BIN") %></textarea></td> 				    
				    </tr>
					<tr>
						<th width="20%" style="vertical-align:top">Approve Color Filter Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor7()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveColorFilterVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveColorFilterVendor" id="approveColorFilterVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_COLOR_FILTER_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_COLOR_FILTER_VENDOR");
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
						</span></td> 
						<td>
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyCFVendorCust" id="btnModifyCFVendorCust" type="button" class="button" value="Modify" onClick="modifyCFVendorCust()">
						<%}%>
						 <!--Color Filter Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (cfCustList != null && cfCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo cfCust : cfCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(cfCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(cfCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (cfCust.getTremark() != null ){
													tremark = cfCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cfCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cfCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cfCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--Color Fiter Vendor Map List Table-->
						</td>
						<td>
							Color Filter Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_CF") %>"
							name="matCf" id="matCf">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matCfLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matCfLov",
                                inputField:"matCf",
                                table:"PIDB_COLOR_FILTER_MATERIAL",
                                keyColumn:"COLOR_FILTER_MATERIAL_NUM",
                                whereCause:" PROJECT_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"COLOR_FILTER_MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
							</script>
						</td>
						<th width="20%" style="vertical-align:top">Remark</th>
						<td width="30%" style="vertical-align:top"><textarea id="remark" name="remark" cols="40" rows="4" class="text"><%=BeanHelper.getHtmlValueByColumn(ref, "REMARK") %></textarea></td> 			    
				    </tr>
					<tr>
						<th width="20%" style="vertical-align:top">Approve Wafer CF Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor8()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveWaferCfVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveWaferCfVendor" id="approveWaferCfVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_WAFER_CF_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_WAFER_CF_VENDOR");
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
						</span></td> 
						<td>
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="btnModifyWCFVendorCust" id="btnModifyWCFVendorCust" type="button" class="button" value="Modify" onClick="modifyWCFVendorCust()">
						<%}%>
						 <!--TurnKey(WF+Color Filter) Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (wcfCustList != null && wcfCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo wcfCust : wcfCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(wcfCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(wcfCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (wcfCust.getTremark() != null ){
													tremark = wcfCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(wcfCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(wcfCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(wcfCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--TurnKey(WF+Color Filter) Vendor Map List Table-->
						</td>
						<td>
							Wafer CF Num<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_WAFERCF") %>"
							name="matWafercf" id="matWafercf">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matWafercfLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matWafercfLov",
                                inputField:"matWafercf",
                                table:"PIDB_WAFER_CF_MATERIAL",
                                keyColumn:"WAFER_CF_MATERIAL_NUM",
                                whereCause:" PROJECT_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"WAFER_CF_MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
							</script>
						</td>
						<th>AssignTo</th>
						<td colspan="2"><table border="0" cellspace="0" cellpadding="0" margin="0">
                        <tr>
                          <td rowspan="2">
					  	<select size="2" multiple class="text_two_line" name="assignTo" id="assignTo" >
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "ASSIGN_TO");
					    			String[] list = listStr.split(",");
						    		for (String s : list) {
						    			if (s != null && s.length() > 0) {
						    			%>
						    			<option value="<%=s %>"><%=s %></option>
						    			<%
						    			}
						    		}
					    		}
					    	%>					  	
					  	</select>	
                          </td>
                          <td><input name="button4" type="button" class="button" id="userBtn" value="User" >
                              <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"userBtn",
                                inputField:"assignTo",
								name:"AssignToUser",
                                autoSearch:false,
                                callbackHandle:"selectUserCallback" 
							});
							function selectUserCallback(selectField, columns, value) {
								if ($(selectField) && value != null && value.length > 0) {
									for(var i = 0; i < value.length; i++) {
										addOption($(selectField), value[i][columns[0]], value[i][columns[0]]);
									}
								}
							}						
							</script>
                          </td>
                          <td><input id="userBtn" type="button" class="button" value="Remove" onclick="removeSelectedOptions($('assignTo'))"></td>
                        </tr>
                        <tr>
                          <td><input name="button4" type="button" class="button" id="roleBtn" value="Role" >
                              <script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"roleBtn",
                                inputField:"assignTo",
								name:"AssignToRole",
                                autoSearch:false,
                                callbackHandle:"selectRoleCallback" 
							});
							function selectRoleCallback(selectField, columns, value) {
								if ($(selectField) && value != null && value.length > 0) {
									for(var i = 0; i < value.length; i++) {
										addOption($(selectField), "(R)"+value[i][columns[0]], "(R)"+value[i][columns[0]]);
									}
								}
							}						
							</script>
                          </td>
                        </tr>
                      </table></td> 				    
			    
				    </tr>
					<tr>
						<th>Approve CP CSP Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor9()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveCpCspVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveCpCspVendor" id="approveCpCspVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_CSP_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_Cp_CSP_VENDOR");
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
						</span></td> 
						 <td>
						 <% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						 <input name="btnModifyCspVendorCust" id="btnModifyCspVendorCust" type="button" class="button" value="Modify" onClick="modifyCspVendorCust()">
						 <%}%>
						  <!--CSP Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (cspCustList != null && cspCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo cspCust : cspCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(cspCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(cspCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (cspCust.getTremark() != null ){
													tremark = cspCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										<input name='vendor' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cspCust, "VENDOR") %>'>
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										<input name='cust' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cspCust, "CUST") %>'>
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										<input name='tremark' type='text' style="display: none;" value='<%=BeanHelper.getHtmlValueByColumn(cspCust, "TREMARK") %>'>
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--TurnKey(WF+Color Filter) Vendor Map List Table-->
						 </td>
						<td>
							CSP Material<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_CSP") %>"
							name="matCsp" id="matCsp">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matCspLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matCspLov",
                                inputField:"matCsp",
                                table:"PIDB_CP_CSP_MATERIAL",
                                keyColumn:"CP_CSP_MATERIAL_NUM",
                                whereCause:" PROJECT_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"CP_CSP_MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
							</script>
						</td>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						
					</tr>
					<!-- TSV -->
					<tr>
						<th>Approve CP TSV Vendor</th>
						<td width="30%"><span style="vertical-align:top">
						  <input name="buttonFT2222" type="button" class="button" onClick="selectSapMasterVendor10()" value="Add">
                          <input name="buttonFT3333" type="button" class="button" onClick="removeSelectedOptions($('approveCpTsvVendor'))" value="Remove">
                          <br>
                          <select size="3" multiple class="select_w130" name="approveCpTsvVendor" id="approveCpTsvVendor">
					    	<%
					    		if ( BeanHelper.getHtmlValueByColumn(ref, "APPROVE_CP_TSV_VENDOR") != null) {
					    			String listStr = BeanHelper.getHtmlValueByColumn(ref, "APPROVE_Cp_TSV_VENDOR");
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
						</span></td> 
						 <td>
						 <% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						 <input name="btnModifyTsvVendorCust" id="btnModifyTsvVendorCust" type="button" class="button" value="Modify" onClick="modifyTsvVendorCust()">
						 <%}%>
						  <!--CSP Vendor Map List Table-->
							<table border=0 cellpadding=0 cellspacing=0 >
							<tr><td>
								<table id="TbEOLCustList" class="grid" border="0" cellpadding="1" cellspacing="1">
									<tbody>
									<tr>
										<th>VENDOR</th>
										<th>CUSTOMER</th>
										<th>TREMARK</th>
									</tr>
									</tbody>
								<%if (cspCustList != null && cspCustList.size() > 0) {%>
									<%
										int idx = 0;
											String custShortName = "";
											SapMasterCustomerDao dao = new SapMasterCustomerDao();

												for(MpListCustomerMapTo cspCust : cspCustList) {
													for(SapMasterCustomerTo smc : smcList) {
														smc= dao.findByVendorCode(cspCust.getCust());
														custShortName = smc.getCustomerCode() + "|" + smc.getShortName();
													}
											
											String vendorShortName = "";
											SapMasterVendorDao vendorDao = new SapMasterVendorDao();
													for(SapMasterVendorTo smcVendor : smcVendorList) {
														smcVendor= vendorDao.findByVendorCode(cspCust.getVendor());
														vendorShortName = smcVendor.getVendorCode() + "|" + smcVendor.getShortName();
													}
												
												String tremark = "";
												if (cspCust.getTremark() != null ){
													tremark = cspCust.getTremark();
												}else{
													tremark = "";
												}
													
										idx ++;
										String tdcss = "class=\"c" + idx % 2+"\"";
									%>
									<tr>
									<td <%=tdcss %>><%=vendorShortName%>&nbsp;
										
									</td>
									<td <%=tdcss %>><%=custShortName%>&nbsp;
										
									</td>
									<td <%=tdcss %>><%=tremark%>&nbsp;
										
									</td>
									</tr>
									<%
										}
									%>
								<%}%>
								</table>
								</td>
							</tr>
							</table>
						    <!--TurnKey(WF+Color Filter) Vendor Map List Table-->
						 </td>
						<td>
							TSV Material<P>
							<input readonly value="<%=BeanHelper.getHtmlValueByColumn(ref, "MAT_TSV") %>"
							name="matTsv" id="matTsv">
							<img src="<%=cp%>/images/lov.gif" alt="LOV" id="matTsvLov">
							<script type="text/javascript">
							SmartSearch.setup({
                                cp:"<%=cp%>",
                                button:"matTsvLov",
                                inputField:"matTsv",
                                table:"PIDB_CP_TSV_MATERIAL",
                                keyColumn:"CP_TSV_MATERIAL_NUM",
                                whereCause:" PROJECT_CODE_W_VERSION = {projCodeWVersion} ",
                                title:"CP_TSV_MATERIAL_NUM",
                                autoSearch:false,
                                mode:0
							});
							</script>
						</td>
						<th>&nbsp;</th>
						<td>&nbsp;</td>
						
					</tr>
				</tbody>
			</table>
			</div>
			<table border="0" cellpadding="0" cellspacing="0"
				width="100%">
				<tr>
					<td>
					<div align="right">
					<input type="hidden" id="toErp" name="toErp">
						<% //Added on 3/9
												
						if (checkedRoles != null) {
							for (RoleTo roleTo : checkedRoles) {
								  if (roleTo.getRoleName().equals("Guest") )  {
									isGuest="Yes";
									} 
								}
							  } 
						if ( isGuest.equals("No"))  {
						   %>
						<input name="releaseBtn" type="button"
						class="erp_button" id="releaseBtn" value="Release To ERP"
						onclick="doReleaseToERP('erp')">
						&nbsp;&nbsp;
					        <!-- <input
						name="saveBtn" type="button" class="button" id="saveBtn"
						value="Save" onClick="doSave()"> 
						   <%
						   }     
						%>
						-->
						<input
						name="resetBtn" type="reset" class="button" id="resetBtn"
						value="Reset"></div>
					</td>
				</tr>
			</table>	
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
<script type="text/javascript">
new Ajax.Request( 
'<%=cp%>/ajax/fetch_cog.do',
                  {
	method: 'post',
	parameters: 'prodCode='+ $F('prodCode') + '&pkgCode=' + $F('pkgCode') + '&icFgMaterialNum=' +$F('icFgMaterialNum'),
	onComplete: fetchCogComplete
} );
</script>