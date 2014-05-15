<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.dao.UserDao"%>
<%@ page import="com.cista.pidb.admin.action.MenuBuilder"%>
<%@ page import="com.cista.pidb.core.PIDBContext"%>
<%@ page import="java.util.List"%>
<%
	response.addHeader("Cache-Control", "no-cache");
	response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
	String cp = request.getContextPath();
%>
<!-- The HTML for the menu-->
<ul id="designMenu">
	<%
		//UserDao ud = new UserDao();
		//UserTo loginUser = PIDBContext.getLoginUser(request);
		//MenuBuilder mb = new MenuBuilder();
		//mb.setContextPath(cp);
		//String s = mb.buildMenu(null, loginUser.getId());
		//out.print(s);
		
		out.print(PIDBContext.getUserMenu(request));
	%>
</ul>
        