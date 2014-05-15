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
import com.cista.pidb.md.dao.ZsFlashramDao;
import com.cista.pidb.md.to.ZsFlashramQueryTo;
import com.cista.pidb.md.to.ZsFlashramTo;

public class ZsFlashramQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ZsFlashramDao flashramDao = new ZsFlashramDao();
		//
		List<ZsFlashramTo> flashramToList = flashramDao.findAll();
		List<String> materialNumList = new ArrayList<String>();
		List<String> memorySizeList = new ArrayList<String>();
		List<String> speedList = new ArrayList<String>();
		List<String> operationVoltageList = new ArrayList<String>();
		List<String> vendorCodeList = new ArrayList<String>();

		if (flashramToList != null && flashramToList.size() > 0) {
			for (int i = 0; i < flashramToList.size(); i++) {
				ZsFlashramTo flashTo = flashramToList.get(i);
				materialNumList.add(flashTo.getMaterialNum());
				memorySizeList.add(flashTo.getMemorySize());
				speedList.add(flashTo.getSpeed());
				operationVoltageList.add(flashTo.getOperationVoltage());
				vendorCodeList.add(flashTo.getVendorCode());
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
					request.setAttribute("ref", new ZsFlashramDao()
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
		ZsFlashramDao newDao = new ZsFlashramDao();
		ZsFlashramQueryTo newTo = (ZsFlashramQueryTo) HttpHelper.pickupForm(
				ZsFlashramQueryTo.class, request, true);

		List<ZsFlashramTo> result = newDao.findBySearchPage(newTo);
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
		ZsFlashramDao flashDao = new ZsFlashramDao();
		ZsFlashramQueryTo flashQueryTo = (ZsFlashramQueryTo) HttpHelper
				.pickupForm(ZsFlashramQueryTo.class, request, true);
		request.setAttribute("result", flashDao.findBySearchPage(flashQueryTo));
		request.setAttribute("criteria", flashQueryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		ZsFlashramDao flashDao = new ZsFlashramDao();
		ZsFlashramQueryTo flashQueryTo = (ZsFlashramQueryTo) HttpHelper
				.pickupForm(ZsFlashramQueryTo.class, request, true);
		flashQueryTo.setPageNo(-1);
		List<ZsFlashramTo> result = flashDao.findBySearchPage(flashQueryTo);

		for (ZsFlashramTo flash : result) {
			String tapeVendor = flash.getVendorCode();
			tapeVendor = null != tapeVendor ? tapeVendor : "N/A";

			List<String> tapeVendorName = flashDao
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

			flash.setVendorCode(tapeVendorTmp);

		}

		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_21_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_21_SEQ"));
		return mapping.findForward("report");
	}

}
