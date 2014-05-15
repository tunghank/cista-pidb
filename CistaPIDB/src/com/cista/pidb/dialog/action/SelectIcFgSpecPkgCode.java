package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.dao.CspDao;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.RwDao;
import com.cista.pidb.md.dao.TradPkgDao;
import com.cista.pidb.md.dao.TsvDao;

public class SelectIcFgSpecPkgCode extends DispatchAction {
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
		String pkgType = request.getParameter("pkgType") != null ? request
				.getParameter("pkgType") : "";
		String prodCode = request.getParameter("prodCode");
		String projCode = request.getParameter("projCode");
		List<Map<String, Object>> selectList = new ArrayList<Map<String, Object>>();

		if (pkgType.equalsIgnoreCase("303")) {
			CogDao cogDao = new CogDao();
			selectList = cogDao.findBy(prodCode);
			//System.out.println(selectList);
		} else if (pkgType.equalsIgnoreCase("304")) {
			TradPkgDao tradPkgDao = new TradPkgDao();
			selectList = tradPkgDao.findByProjCode(projCode);
			//System.out.println(selectList);
		} else if (pkgType.equalsIgnoreCase("305")) {
			// add new packType
			CspDao cspDao = new CspDao();
			selectList = cspDao.findByProjCode(projCode);
			//System.out.println(selectList);
		} else if (pkgType.equalsIgnoreCase("306")) {
			RwDao rwDao = new RwDao();
			selectList = rwDao.findBy(prodCode);
			//System.out.println(selectList);
		} else if (pkgType.equalsIgnoreCase("314")) {
			TsvDao tsvDao = new TsvDao();
			selectList = tsvDao.findPkgCode(prodCode);
			//System.out.println(selectList);
		} else {
			IcTapeDao icTapeDao = new IcTapeDao();
			selectList = icTapeDao.findByProdCode(prodCode);
			//System.out.println(selectList);
		}

		request.setAttribute("selectList", selectList);
		request.setAttribute("callback", callback);
		return mapping.findForward(forward);

	}
}
