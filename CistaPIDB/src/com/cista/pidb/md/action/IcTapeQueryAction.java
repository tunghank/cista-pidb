package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.to.IcTapeQueryTo;
import com.cista.pidb.md.to.IcTapeTo;

public class IcTapeQueryAction extends DispatchAction {
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		IcTapeDao icTapeDao = new IcTapeDao();
		List<IcTapeTo> ittl = icTapeDao.findAll();
		List<String> pkgCodeList = new ArrayList<String>();
		List<String> pkgVersionList = new ArrayList<String>();
		List<String> tapeWidthList = new ArrayList<String>();

		if (ittl != null) {
			for (IcTapeTo itt : ittl) {
				if (itt.getPkgCode() != null && itt.getPkgCode().length() > 0
						&& !pkgCodeList.contains(itt.getPkgCode())) {
					pkgCodeList.add(itt.getPkgCode());
				}
				if (itt.getPkgVersion() != null
						&& itt.getPkgVersion().length() > 0
						&& !pkgVersionList.contains(itt.getPkgVersion())) {
					pkgVersionList.add(itt.getPkgVersion());
				}
				if (itt.getTapeWidth() != null
						&& itt.getTapeWidth().length() > 0
						&& !tapeWidthList.contains(itt.getTapeWidth())) {
					tapeWidthList.add(itt.getTapeWidth());
				}

			}
		}

		if (request.getParameter("ref") != null) {
			// Create with reference
			String key = request.getParameter("ref");
			if (key.indexOf(",") >= 0) {
				String[] keys = key.split(",");
				if (keys != null && keys.length == 2) {
					request.setAttribute("ref", new IcTapeDao()
							.findByPrimaryKey(keys[0], keys[1]));
				}
			}
		}

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("pkgCodeList", pkgCodeList);
		request.setAttribute("pkgVersionList", pkgVersionList);
		request.setAttribute("tapeWidthList", tapeWidthList);

		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		IcTapeDao icTapeDao = new IcTapeDao();
		IcTapeQueryTo queryTo = (IcTapeQueryTo) HttpHelper.pickupForm(
				IcTapeQueryTo.class, request, true);
		queryTo.setTotalResult(icTapeDao.countResult(queryTo));

		request.setAttribute("result", icTapeDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		IcTapeDao icTapeDao = new IcTapeDao();
		IcTapeQueryTo queryTo = (IcTapeQueryTo) HttpHelper.pickupForm(
				IcTapeQueryTo.class, request, true);
		queryTo.setTotalResult(icTapeDao.countResult(queryTo));
		request.setAttribute("result", icTapeDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		IcTapeDao icTapeDao = new IcTapeDao();
		IcTapeQueryTo queryTo = (IcTapeQueryTo) HttpHelper.pickupForm(
				IcTapeQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<IcTapeTo> result = icTapeDao.queryForDomain(queryTo);
		for (IcTapeTo icTape : result) {
			String tapeVendor = icTape.getTapeVendor();
			tapeVendor = null != tapeVendor ? tapeVendor : "N/A";

			List<String> tapeVendorName = icTapeDao
					.findTapeShortName(tapeVendor);
			String tapeVendorTmp = "";
			for (int i = 0; i < tapeVendorName.size(); i++) {
				if (i != (tapeVendorName.size() - 1)) {
					tapeVendorTmp = tapeVendorTmp
							+ (String) tapeVendorName.get(i) + ",";
				} else {
					tapeVendorTmp = tapeVendorTmp
							+ (String) tapeVendorName.get(i);
				}
			}

			icTape.setTapeVendor(tapeVendorTmp);

		}

		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_10_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_10_SEQ"));
		return mapping.findForward("report");
	}
}