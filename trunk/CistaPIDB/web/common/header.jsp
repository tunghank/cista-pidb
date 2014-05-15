<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String cp = request.getContextPath();
%>
<title>Cista Product Information Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<!-- import the calendar script -->
<script type="text/javascript" src="<%=cp %>/js/jscalendar-1.0/calendar.js"></script>
<script type="text/javascript" src="<%=cp %>/js/jscalendar-1.0/calendar-setup.js"></script>

<!-- import the language module -->
<script type="text/javascript" src="<%=cp %>/js/jscalendar-1.0/lang/calendar-en.js"></script>
<link rel="stylesheet" type="text/css" media="all" href="<%=cp %>/js/jscalendar-1.0/calendar-blue2.css" title="aqua" />

<script type="text/javascript" src="<%=cp %>/js/form.js"></script>
<script type="text/javascript" src="<%=cp %>/js/prototype163.js"></script>
<script type="text/javascript" src="<%=cp %>/js/dynamicTable.js"></script>
<script type="text/javascript" src="<%=cp %>/js/zpmenu/utils/zapatec.js"></script>
<script type="text/javascript" src="<%=cp %>/js/zpmenu/src/zpmenu.js"></script>
<script type="text/javascript" src="<%=cp %>/js/general.js"></script>
<script type="text/javascript" src="<%=cp %>/js/ss.js"></script>
<script type="text/javascript" src="<%=cp %>/js/sortable.js"></script>
<script type="text/javascript" src="<%=cp %>/js/StringEnhance.js"></script>

<script type="text/javascript">
<!--
var globalHandler = {
	onCreate:function() {
		if($('inProcessing')) {
			Element.show('inProcessing');
		}
	},
	
	onComplete:function() {
		if(Ajax.activeRequestCount == 0) {
			if($('inProcessing')) {
				Element.hide('inProcessing');
			}
		}
	}
}

Ajax.Responders.register(globalHandler);
//-->
</script>