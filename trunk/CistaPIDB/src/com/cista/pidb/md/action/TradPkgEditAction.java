package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.TradPkgDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.erp.TradPkgERP;
import com.cista.pidb.md.to.TradPkgTo;

public class TradPkgEditAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";
		TradPkgDao tradPkgDao = new TradPkgDao();
		String pkgName = (String) request.getParameter("pkgName");
		if (pkgName == null || pkgName.length() <= 0) {
			pkgName = (String) request.getAttribute("pkgName");
		}
		TradPkgTo tradPkgTo = (TradPkgTo) tradPkgDao.findByPkgName(pkgName);
		if (tradPkgTo != null) {
			request.setAttribute("tradPkgTo", tradPkgTo);
		}

		String fundName = "TRAD_PKG";
		String funFieldName = "ASSEMBLY_HOUSE";
		request.setAttribute("assemblyHouseList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		String funFieldName2 = "LEAD_FRAME_TOOL";
		request.setAttribute("leadFrameToolList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName2));
		
		String funFieldName3 = "CLOSE_LEAD_FRAME_NAME";
		request.setAttribute("closeLeadFrameNameList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName3));

		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save_success";
		TradPkgDao tradPkgDao = new TradPkgDao();
		TradPkgTo tradPkgTo = (TradPkgTo) HttpHelper.pickupForm(
				TradPkgTo.class, request);
		// assignEmail
		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		String emails = userDao.fetchEmail(assignTo);
		tradPkgTo.setAssignTo(assigns);
		tradPkgTo.setAssignEmail(emails);

		// set modified by
		UserTo loginUser = PIDBContext.getLoginUser(request);
		tradPkgTo.setModifiedBy(loginUser.getUserId());
		tradPkgDao.updateTradPkg(tradPkgTo);

		// send mail
		SendMailDispatch.sendMailByModify("MD-12", tradPkgTo.getPkgName(),
				emails, SendMailDispatch.getUrl("MD_12_EDIT", request
						.getContextPath(), tradPkgTo));

		final TradPkgTo tradTo = tradPkgTo;
		final UserTo user = loginUser;

		// Start Release to SAP
		String isRelease = (String) request.getParameter("isRelease");
		if (isRelease.equals("1")) {

			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {

					String result = TradPkgERP.release(tradTo, user);

					if (result != null) {
						throw new ReleaseERPException(result);
					}
				}
			};

			try {
				new FunctionDao().doInTransaction(callback);
			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));
				request.setAttribute("pkgName", tradTo.getPkgName());
				return mapping.findForward("release_fail");
			}

			// send mail
			// SendMailDispatch.sendMailByErp("MD-12", tradPkgTo.getPkgName(),
			// emails, "");
		} else {

		}
		// End Release to SAP

		String mes = "";
		if (isRelease.equals("1")) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);
		request.setAttribute("ref", tradPkgTo);
		return mapping.findForward("release_fail");
	}

}
