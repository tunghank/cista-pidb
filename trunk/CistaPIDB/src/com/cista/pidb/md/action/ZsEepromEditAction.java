package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.cista.pidb.md.dao.ZsEepromDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.erp.ZsReleaseERP;
import com.cista.pidb.md.to.ZsEepromTo;

public class ZsEepromEditAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "edit";
		String materialNumber = (String) request.getParameter("materialNum");
		request.setAttribute("ref", new ZsEepromDao()
				.findByPrimaryKey(materialNumber));
		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "save";
		String materialNumber = (String) request.getParameter("materiaNum");
		ZsEepromDao eepromDao = new ZsEepromDao();
		ZsEepromTo eepromTo = (ZsEepromTo) HttpHelper.pickupForm(
				ZsEepromTo.class, request, true);

		// transfer Application Product
		String[] prodCodes = request.getParameterValues("prodCodeList");
		String allProdCode = "";
		if (prodCodes != null && prodCodes.length > 0) {
			for (String prodCode : prodCodes) {
				allProdCode += "," + prodCode;
			}
		}

		if (allProdCode != null && allProdCode.length() > 0) {
			eepromTo.setApplicationProduct(allProdCode.substring(1));
		}

		String[] vendorList = request.getParameterValues("eepromVendor");
		String allVendor = "";
		if (vendorList != null && vendorList.length > 0) {
			for (String vendor : vendorList) {
				allVendor += "," + vendor;
			}
		}

		if (allVendor != null && allVendor.length() > 0) {
			eepromTo.setVendorCode(allVendor.substring(1));
		}

		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");

		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		} else {
			assignTo = new String[] {};
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		// emailList
		List<String> emailList = new ArrayList<String>(Arrays.asList(assignTo));
		emailList.add("(R)default_wf_save");

		String emails = userDao.fetchEmail(emailList);
		eepromTo.setAssignEmail(emails);

		UserTo loginUser = PIDBContext.getLoginUser(request);
		eepromTo.setModifiedBy(loginUser.getUserId());
		eepromDao.updateSdram(eepromTo);

		/***********************************************************************
		 * Release to ERP Add 2008/06/12 Hank Added
		 **********************************************************************/
		final UserTo user = loginUser;
		final ZsEepromTo eepRom = eepromTo;
		String toErp = request.getParameter("toErp");
		if ("1".equals(toErp.trim())) {
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = null;
					result = ZsReleaseERP.releaseEEPRom(eepRom, user);
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
				return mapping.findForward("release_fail");
			}
		}

		String mes = "";
		if (toErp.equals("1")) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);

		String targetForward = mapping.findForward("viewEditJsp").getPath();

		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}

		ActionForward actionForward = new ActionForward(targetForward
				+ "materialNum=" + eepromTo.getMaterialNum());
		return actionForward;
	}
}
