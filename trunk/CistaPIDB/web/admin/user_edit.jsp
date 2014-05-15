<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.cista.pidb.admin.to.UserTo"%>
<%@ page import="com.cista.pidb.admin.to.RoleTo"%>
<%@ page import="com.cista.pidb.core.BeanHelper"%>
<%@ include file="/common/global.jsp"%>
<%! 
	boolean checkRole(UserTo userTo, int roleId) {
    	for(RoleTo role : userTo.getRoles()) {
    	    if (role.getId() == roleId) {
    	        return true;
    	    }
    	}
    	return false;
	}
%>
<%
	UserTo userTo = (UserTo)request.getAttribute("userTo");
	List<RoleTo> allRoles = (List<RoleTo>)request.getAttribute("allRoles");
%>
<html>
<head>
<!-- This is header -->
<jsp:include page="/common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
<script type="text/javascript">
	function fetchUserFromAD() {
		if ($F('userId')=="") {
			setMessage("error", "Please fill in user id.");
		} else {
			$('createBtn').disabled = true;
			$('cancelBtn').disabled = true;
			$('fetchUserBtn').disabled = true;
			setMessage("error", "Synchronizing user's information from active directory server...");
			var checkUser = new Ajax.Request(
				'<%=cp%>/ajax/fetch_user_from_ad.do',
				{
					method: 'get',
					parameters: 'userId='+ $F('userId'),
					onComplete: fetchUserComplete
				}
			);
		}
	}
	
	function fetchUserComplete(r) {
		var user = r.responseXML;
		var userTxt = r.responseText;
		if(userTxt!="null") {
			var node;
			node = user.getElementsByTagName("firstName");
			if(node && node.length>0) {
				$('firstName').value = node[0].firstChild.nodeValue;
			} else {
				$('firstName').value = "";
			}
			node = user.getElementsByTagName("lastName");
			if(node && node.length>0) {
				$('lastName').value = node[0].firstChild.nodeValue;
			} else {
				$('lastName').value = "";
			}

			node = user.getElementsByTagName("email");
			if(node && node.length>0) {
				$('email').value = node[0].firstChild.nodeValue;
			} else {
				$('email').value = "";
			}

			setMessage("error", "Fetch user's information success.");
		} else {
			$('firstName').value = "";
			$('lastName').value = "";
			$('email').value = "";
			setMessage("error", "Fetch user's information from active directory server fail, maybe server down or user not exist.");
		}
		$('createBtn').disabled = false;
		$('cancelBtn').disabled = false;
		$('fetchUserBtn').disabled = false;
	}

	function saveUser() {
		if($F('firstName')=="") {
			setMessage("error", "First name is must field.");
			return
		}
		
		if($F('lastName')=="") {
			setMessage("error", "Last name is must field.");
			return
		}

		$('createBtn').disabled = true;
		$('cancelBtn').disabled = true;
		$('fetchUserBtn').disabled = true;
		
		setMessage("error", "Save user...");
		$('userUpdate').submit();
	}
</script>
</head>
<body>
<form name="userUpdate" action="<%=cp %>/admin/user_edit.do?m=save" method="post">
<input type="hidden" name="id" id="id" value="<%=BeanHelper.getHtmlValueByColumn(userTo, "ID")%>">
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF"><!--Html Start--> <!-- This is header -->
			<jsp:include page="/common/banner.jsp" flush="true" />
			<!-- Content start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="pageTitle">Admin :: User management</td>
  </tr>
</table>
			<div class="content">
			<div class="segmentHeader">User Update </div>
			<table class="formErrorAndButton">
				<tr>
					<td width="75%">
					<div class="formErrorMsg" id="error"><html:errors/></div>
					</td>
					<td width="25%">
					<div align="right">
					  <input
						name="createBtn" type="button" class="button" id="createBtn"
						value="Update" onclick="saveUser()">
					  <input
						name="cancelBtn" type="button" class="button" id="cancelBtn"
						value="Cancel" onclick="document.location.href='<%=cp %>/admin/user_list.do?m=pre'">
					</div>
					</td>
				</tr>
			</table>


			<table class="formFull" border="0" cellpadding="1" cellspacing="1">
				<tbody>
					<tr>
					  <th width="180">User ID </th>
					  <td>
							<input name="userId" id="userId" type="text" size="20" class="text" readonly value="<%=BeanHelper.getHtmlValueByColumn(userTo, "USER_ID")%>">
							<input name="fetchUserBtn" id="fetchUserBtn" type="button" class="button" class="button" value="Retrieve" onclick="fetchUserFromAD()">
					  </td>
					</tr>
					<tr>
						<th width="180">First Name</th>
					  <td>
							<input name="firstName" id="firstName" type="text" size="20" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(userTo, "FIRST_NAME")%>">
					  </td>
					</tr>
					<tr>
					  <th width="180">Last Name </th>
					  <td>
							<input name="lastName" id="lastName" type="text" size="20" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(userTo, "LAST_NAME")%>">
					  </td>
					</tr>
					<tr>
					  <th width="180">Email </th>
					  <td>
							<input name="email" id="email" type="text" size="20" class="text_protected" readonly value="<%=BeanHelper.getHtmlValueByColumn(userTo, "EMAIL")%>">
					  </td>
					</tr>
					<tr>
					  <th width="180">Status </th>
					  <td>
							<input name="active" id=active type="checkbox" value="1" <%=userTo.getActive()?"checked":"" %>> Active
					  </td>
					</tr>
					<tr>
					  <th width="180" style="vertical-align: top;">Roles Available</th>
					  <td>
							<% 
								if (allRoles != null && allRoles.size() > 0) {
								    boolean firstRole = true;
								    for(RoleTo role : allRoles) {
								        %>
								        <%
								        	if (firstRole) {
								        	    firstRole = false;
								        	} else {
											%>
											<br>
											<%								        	    
								        	}
								        %>

								        <input name="roles" id="roles" type="checkbox" value="<%=BeanHelper.getHtmlValueByColumn(role, "ID") %>" <%=checkRole(userTo, role.getId())?"checked":"" %>> <%=BeanHelper.getHtmlValueByColumn(role, "ROLE_NAME") %>
								        <%
								    }
								}
							%>
							&nbsp;
					  </td>
					</tr>
				</tbody>
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
