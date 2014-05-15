package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.MpListEolCustDao;
import com.cista.pidb.md.to.MpListTo;

public class SelectMPListEolCustomer extends DispatchAction {
	/**
	 * .
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */

	public ActionForward list(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		String callback = request.getParameter("callback");
		String condition = request.getParameter("condition");

		String partNum = request.getParameter("partNum");
		String icFgMaterialNum = request.getParameter("icFgMaterialNum");
		String projCodeWVersion = request.getParameter("projCodeWVersion");
		String tapeName = request.getParameter("tapeName");
		String pkgCode = request.getParameter("pkgCode");
		MpListEolCustDao eolCustDao = new MpListEolCustDao();
		SapMasterCustomerDao smcDao = new SapMasterCustomerDao();
		MpListTo to = new MpListDao().findByPrimaryKey(partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode);
		SapMasterCustomerTo custTo = new SapMasterCustomerTo();
		List<SapMasterCustomerTo> sapCustomerTo = new ArrayList<SapMasterCustomerTo>();
		if (to.getApproveCust() != null) {
			String[] custs = (to.getApproveCust().split(","));
			for (String cust : custs) {
				if(cust != null && !cust.equals("")){
				custTo = smcDao.findByVendorCode(cust);
				sapCustomerTo.add(custTo);
				
				}
				//System.out.println(sapCustomerTo);
			}

		}
		request.setAttribute("selectList", sapCustomerTo);
		request.setAttribute("custList", smcDao.findAll());
		request.setAttribute("eolList", eolCustDao.findByPrimaryKey(partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode));
		request.setAttribute("callback", callback);
		request.setAttribute("condition", condition);

		request.setAttribute("eolCustList", eolCustDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode));

		request.setAttribute("partNum", partNum);
		request.setAttribute("icFgMaterialNum", icFgMaterialNum);
		request.setAttribute("projCodeWVersion", projCodeWVersion);
		request.setAttribute("tapeName", tapeName);
		request.setAttribute("pkgCode", pkgCode);

		return mapping.findForward(forward);
	}

}
