<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-nested.tld" prefix="nested"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.core.PIDBContext"%>
<%
	//Context path.
	String cp = request.getContextPath();

	String loginUserDesc = "Not login.";
	UserTo loginUser = PIDBContext.getLoginUser(request);
	if (loginUser != null) {
	    loginUserDesc = loginUser.getUserId();
	}
	
	String appName = PIDBContext.getMessage("App.Name");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
