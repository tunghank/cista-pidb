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
import com.cista.pidb.md.dao.ZsSdramDao;
import com.cista.pidb.md.to.ZsSdramQueryTo;
import com.cista.pidb.md.to.ZsSdramTo;

public class ZsSdramQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ZsSdramDao zsSdramDao = new ZsSdramDao();
		//
		List<ZsSdramTo> zsSdramToList = zsSdramDao.findAll();
		List<String> materialNumList = new ArrayList<String>();
		List<String> memorySizeList = new ArrayList<String>();
		List<String> speedList = new ArrayList<String>();
		List<String> operationVoltageList = new ArrayList<String>();
		List<String> vendorCodeList = new ArrayList<String>();

		if (zsSdramToList != null && zsSdramToList.size() > 0) {
			for (int i = 0; i < zsSdramToList.size(); i++) {
				ZsSdramTo sdramTo = zsSdramToList.get(i);
				materialNumList.add(sdramTo.getMaterialNum());
				memorySizeList.add(sdramTo.getMemorySize());
				speedList.add(sdramTo.getSpeed());
				operationVoltageList.add(sdramTo.getOperationVoltage());
				vendorCodeList.add(sdramTo.getVendorCode());
			}
		}
		if (materialNumList != null && materialNumList.size() > 0) {
			materialNumList = getUniqueList(materialNumList);
		}
		if (memorySizeList != null && memorySizeList.size() > 0) {
			memorySizeList = getUniqueList(memorySizeList);
		}
		if (speedList != null && speedList.size() > 0) {
			speedList = getUniqueList(speedList);
		}
		if (operationVoltageList != null && operationVoltageList.size() > 0) {
			operationVoltageList = getUniqueList(operationVoltageList);
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
					request.setAttribute("ref", new ZsSdramDao()
							.findByPrimaryKey(keys[0]));
				}
			}
		}

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("materialNumList", materialNumList);
		request.setAttribute("memorySizeList", memorySizeList);
		request.setAttribute("speedList", speedList);
		request.setAttribute("operationVoltageList", operationVoltageList);
		request.setAttribute("vendorCodeList", vendorCodeList);
		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ZsSdramDao newDao = new ZsSdramDao();
		ZsSdramQueryTo newTo = (ZsSdramQueryTo) HttpHelper.pickupForm(
				ZsSdramQueryTo.class, request, true);

		List<ZsSdramTo> result = newDao.findBySearchPage(newTo);
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
		ZsSdramDao sdramDao = new ZsSdramDao();
		ZsSdramQueryTo sdramQueryTo = (ZsSdramQueryTo) HttpHelper.pickupForm(
				ZsSdramQueryTo.class, request, true);
		request.setAttribute("result", sdramDao.findBySearchPage(sdramQueryTo));
		request.setAttribute("criteria", sdramQueryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		ZsSdramDao sdramDao = new ZsSdramDao();
		ZsSdramQueryTo sdramQueryTo = (ZsSdramQueryTo) HttpHelper.pickupForm(
				ZsSdramQueryTo.class, request, true);
		sdramQueryTo.setPageNo(-1);
		List<ZsSdramTo> result = sdramDao.findBySearchPage(sdramQueryTo);

		for (ZsSdramTo sdram : result) {
			String tapeVendor = sdram.getVendorCode();
			tapeVendor = null != tapeVendor ? tapeVendor : "N/A";

			List<String> tapeVendorName = sdramDao
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

			sdram.setVendorCode(tapeVendorTmp);

		}

		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_18_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_18_SEQ"));
		return mapping.findForward("report");
	}
}