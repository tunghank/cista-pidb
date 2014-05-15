package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.to.CpMaterialTo;

public class SelectCpMaterial extends DispatchAction {
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

		String projCodeWVersion = request.getParameter("projCodeWVersion");
		String remark = request.getParameter("remark");
		String materialNum = request.getParameter("materialNum");

		List<CpMaterialTo> to = new ArrayList<CpMaterialTo>();
		CpMaterialDao dao = new CpMaterialDao();
		to = dao.getByProjCodeWVersion(projCodeWVersion);

		/*CpMaterialTo cpMaterialTo = dao
				.getByProjCodeWVersionMaxVariant(projCodeWVersion);
		if (cpMaterialTo != null) {
			String var = cpMaterialTo.getCpMaterialNum().substring(10, 13);
			int newVar = (Integer.parseInt(var) + 1);
			String varNum = String.valueOf(newVar);
			if (varNum.length() == 1) {
				String variant = cpMaterialTo.getCpMaterialNum().substring(0,
						10)
						+ "00" + varNum;
				request.setAttribute("variant", variant);
			} else if (varNum.length() == 2) {
				String variant = cpMaterialTo.getCpMaterialNum().substring(0,
						10)
						+ "0" + varNum;
				request.setAttribute("variant", variant);
			} else {
				String variant = cpMaterialTo.getCpMaterialNum().substring(0,
						10)
						+ varNum;
				request.setAttribute("variant", variant);
			}
		}*/

		request.setAttribute("cpMaterialList", to);
		request.setAttribute("callback", callback);
		request.setAttribute("condition", condition);
		request.setAttribute("remark", remark);
		request.setAttribute("materialNum", materialNum);
		request.setAttribute("projCodeWVersion", projCodeWVersion);

		return mapping.findForward(forward);
	}

}
