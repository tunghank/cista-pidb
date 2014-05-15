<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ page import="com.cista.pidb.admin.action.FunctionListAction" %>
<div id="banner">
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse">
	<tr>
		<td valign="bottom">
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			style="border-collapse: collapse">
			<tr>
				<td width="700" valign="middle">
				<table width="98%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="20%"><a href="http://www.himax.com.tw"><img
							src="<%=cp %>/images/logo.jpg" width="146" height="42" border="0"
							alt=""></a></td>
						<td width="80%" valign="middle"><span class="pageHeader"><br>
						<%=appName %> </span></td>
					</tr>
				</table>
				</td>
				<td>
				<div align="right">
				<table  border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="28" valign="top" style="vertical-align: top" colspan="2">
						<div align="right"><a href="<%=cp %>/common/main.jsp">Home</a> | <a href="#">My
						Profile</a> | <a href="#">Online Help</a> | <a
							href="<%=cp %>/common/logout.do">Logout</a>&nbsp; &nbsp;</div>
						</td>
					</tr>
					<tr>
					<td valign="bottom" style="vertical-align: bottom; color:#333333">
					On line User: <%=FunctionListAction.getActiveSessions()%>
					</td>
						<td valign="bottom" style="vertical-align: bottom; color:#333333">
						 <div align=right>User: <%=loginUserDesc %>&nbsp;&nbsp;</div> 
						</td>
					</tr>
				</table>
				</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<div>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td nowrap height="20" bgcolor="#37a8de" colspan="2" width="100%"
			id="menuContainer"><!-- The HTML for the menu--> <!-- The Javascript code to initiate the menu -->
		<script type="text/javascript">
            var myMenu = new Zapatec.Menu({
                theme: "himax",
                source: "<%=cp%>/common/menu.jsp",
			    sourceType: "html/url",
			    container: "menuContainer"
				});
        </script>
		<noscript><br />
		Your browser does not support Javascript. <br />
		Either enable Javascript in your Browser or upgrade to a newer
		version.</noscript>
		</td>
	</tr>
	<tr>
		<td height="2" bgcolor="#CCCCCC" colspan="2" width="100%"></td>
	</tr>
</table>
</div>
