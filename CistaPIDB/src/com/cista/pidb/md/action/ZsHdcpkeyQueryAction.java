package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ZsHdcpKeyDao;
import com.cista.pidb.md.to.ZsHdcpKeyQueryTo;
import com.cista.pidb.md.to.ZsHdcpKeyTo;

public class ZsHdcpkeyQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ZsHdcpKeyDao hdcpkeyDao = new ZsHdcpKeyDao();
		//
		List<ZsHdcpKeyTo> hdcpkeyToList = hdcpkeyDao.findAll();
		List<String> materialNumList = new ArrayList<String>();
		List<String> vendorCodeList = new ArrayList<String>();

		if (hdcpkeyToList != null && hdcpkeyToList.size() > 0) {
			for (int i = 0; i < hdcpkeyToList.size(); i++) {
				ZsHdcpKeyTo hdcpkeyTo = hdcpkeyToList.get(i);
				materialNumList.add(hdcpkeyTo.getMaterialNum());
				vendorCodeList.add(hdcpkeyTo.getVendorCode());
			}
		}
		if (materialNumList != null && materialNumList.size() > 0) {
			materialNumList = getUniqueList(materialNumList);
		}
		if (vendorCodeList != null && vendorCodeList.size() > 0) {
			vendorCodeList = getUniqueList(vendorCodeList);
		}

		if (request.getParameter("ref") != null) {
			// Create with reference
			String key = request.getParameter("ref");
			if (key.indexOf(",") >= 0) {
				String[] keys = key.split(",");
				if (keys != null && keys.length == 2) {
					request.setAttribute("ref", new ZsHdcpKeyDao()
							.findByPrimaryKey(keys[0]));
				}
			}
		}

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("materialNumList", materialNumList);
		request.setAttribute("vendorCodeList", vendorCodeList);
		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ZsHdcpKeyDao newDao = new ZsHdcpKeyDao();
		//
		ZsHdcpKeyQueryTo newTo = (ZsHdcpKeyQueryTo) HttpHelper.pickupForm(
				ZsHdcpKeyQueryTo.class, request, true);

		List<ZsHdcpKeyTo> result = newDao.findBySearchPage(newTo);
		newTo.setTotalResult(newDao.countResult(newTo));
		//
		request.setAttribute("criteria", newTo);
		request.setAttribute("result", result);
		return mapping.findForward(forward);
	}

	public List<String> getUniqueList(List<String> list) {
		List<String> lreturn = new ArrayList<String>();
		for (String l : list) {
			if (!lreturn.contains(l) && !StringUtils.isEmpty(l)) {
				lreturn.add(l);
			}
		}

		return lreturn;
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ZsHdcpKeyDao sdramDao = new ZsHdcpKeyDao();
		ZsHdcpKeyQueryTo hdcpkeyQueryTo = (ZsHdcpKeyQueryTo) HttpHelper
				.pickupForm(ZsHdcpKeyQueryTo.class, request, true);
		request.setAttribute("result", sdramDao
				.findBySearchPage(hdcpkeyQueryTo));
		request.setAttribute("criteria", hdcpkeyQueryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		ZsHdcpKeyDao hdcpKeyDao = new ZsHdcpKeyDao();
		ZsHdcpKeyQueryTo hdcpKeyQueryTo = (ZsHdcpKeyQueryTo) HttpHelper
				.pickupForm(ZsHdcpKeyQueryTo.class, request, true);
		hdcpKeyQueryTo.setPageNo(-1);
		List<ZsHdcpKeyTo> result = hdcpKeyDao.findBySearchPage(hdcpKeyQueryTo);

		for (ZsHdcpKeyTo hdcpkey : result) {
			String tapeVendor = hdcpkey.getVendorCode();
			tapeVendor = null != tapeVendor ? tapeVendor : "N/A";

			List<String> tapeVendorName = hdcpKeyDao
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

			hdcpkey.setVendorCode(tapeVendorTmp);

		}

		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_20_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_20_SEQ"));
		return mapping.findForward("report");
	}
}