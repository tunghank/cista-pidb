<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<html>
<head>
<!-- This is header -->
<jsp:include page="common/header.jsp" flush="true" />
<link type="text/css" href="<%=cp %>/css/portal.css" rel="stylesheet">
</head>
<body>
<table width="99%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<td valign="top" bgcolor="#FFFFFF">
			<div id="banner">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				style="border-collapse: collapse">
				<tr>
					<td valign="bottom">
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						style="border-collapse: collapse">
						<tr>
							<td width="750" valign="middle">
							<table width="98%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="20%"><a href="http://www.himax.com.tw"><img
										src="<%=cp %>/images/logo.jpg" width="146" height="42" border="0"
										alt=""></a></td>
									<td width="80%" valign="middle"><span class="pageHeader"><br>
									<%=appName %></span></td>
								</tr>
							</table>
							</td>
							<td height="40" valign="top" style="vertical-align: top">&nbsp;</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</div>
			<!--Html Start-->
			<div class="content">
			<table width="100%" cellpadding="0" cellspacing="8" border="0"
				align="center" background="<%=cp %>/images/table-bgg03(deep).jpg">
				<tr>
					<td height="100">&nbsp;</td>
				</tr>
				<tr>
					<td width="100%">
					<table width="100%" height="200" cellpadding="0" cellspacing="0"
						border="0">
						<tr>
							<td width="20%">&nbsp;</td>
							<td width="28%">
							<div align="right"><img src="<%=cp %>/images/login-02.gif"
								width="149" height="119" alt=""></div>
							</td>
							<td width="72%">
							<table width="99%" cellpadding="0" cellspacing="0" align="left">
								<tr>
									<td>
									<table width="59%" cellpadding="0" cellspacing="0" border="0">
										<tr>
											<td>
											<form name="loginForm" action="<%=cp %>/common/login.do" method="post" onsubmit="doAuthentication();return false;">
											<table width="100%" border="0" cellspacing="2"
												cellpadding="0" class="login-border" align="center">
												<tr>
													<td colspan="2" height="13">
													<table border="0" cellspacing="0"
														cellpadding="0">
														<tr>
															<td height="19"><img
																src="<%=cp %>/images/login.gif" width="21" height="18" alt="">
															<font face="Arial, Helvetica, sans-serif" size="2"></font><font
																face="Arial, Helvetica, sans-serif" size="2"><b>PIDB
															Login </b></font></td>
														</tr>
														<tr>
															<td height="19">
															<div id="error" class="formErrorMsg"></div></td>
														</tr>
													</table>
													</td>
												</tr>
												<tr>
													<td width="24%" class="word">
													<div align="right" class="item">User ID:</div>
													</td>
													<td width="76%"><input type="text" name="userId"
														size="20"></td>
												</tr>
												<tr>
													<td width="24%" class="word">
													<div align="right" class="text"><span class="item">Password</span>:</div>
													</td>
													<td width="76%"><input type="password" name="password"
														size="20"></td>
												</tr>
												<tr>
													<td width="24%" class="word">&nbsp;</td>
													<td width="76%"><input type="submit" class="button"
														value="login" name="loginBtn">
														<input type="reset" class="button"
														value="reset" name="resetBtn"></td>
												</tr>
											</table>
											</form>
											</td>
										</tr>
									</table>
									</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</div>
			<!--Html End--></td>
			<td width="5" valign="bottom" background="<%=cp %>/images/shadow-1.gif">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				background="<%=cp %>/images/bgs.gif">
				<tr>
					<td height="15"><img src="<%=cp %>/images/spacer.gif" width="1"
						height="1" alt=""></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="2"><img height="2" alt="" src="<%=cp %>/images/shadow-2.gif"
				width="100%" border="0"></td>
		</tr>
	</tbody>
</table>
<!-- This is footer -->
<jsp:include page="/common/footer.jsp" flush="true" />
<script type="text/javascript">
function doAuthentication() {

	if ($F('userId')=="" || $F('password')=="") {
		setMessage("error", "Incorrect user id or password.");
		$('userId').select();
		$('userId').focus();
		return false;
	}
	$('loginBtn').disabled = true;
	$('resetBtn').disabled = true;
	setMessage("error", "Check user id exist...");
	var checkUser = new Ajax.Request(
		'<%=cp%>/ajax/check_user_exist.do',
		{
			method: 'get',
			parameters: 'status=active&userId=' + $F('userId'),
			onComplete: doAuthentication2
		}
	);

}

function doAuthentication2(r) {

	var result = r.responseText;
	if(result == 'false' ) {
		$('loginBtn').disabled = false;
		$('resetBtn').disabled = false;
		setMessage("error", "User not exist.");
		$('userId').select();
		$('userId').focus();
	} else {
		setMessage("error", "Authenticate user's password via Active Directory...");
		var checkUser = new Ajax.Request(
			'<%=cp%>/ajax/check_user_on_ad.do',
			{
				method: 'get',
				parameters: 'userId='+ $F('userId') + '&password=' + $F('password'),
				onComplete: doAuthentication3
			}
		);
	}

}

function doAuthentication3(r) {

	var result = r.responseText;
	
	if(result == 'false' ) {
		$('loginBtn').disabled = false;
		$('resetBtn').disabled = false;
		setMessage("error", "Incorrect user id or password.");
		$('userId').select();
		$('userId').focus();
	} else {
		setMessage("error", "Authentication success, process login.");
		$('loginForm').submit();
	}

}
</script>
</body>
</html>
