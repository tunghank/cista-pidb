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
import com.cista.pidb.md.dao.ZsUsbramDao;
import com.cista.pidb.md.to.ZsUsbramQueryTo;
import com.cista.pidb.md.to.ZsUsbramTo;

public class ZsUsbramQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ZsUsbramDao usbramDao = new ZsUsbramDao();
		//
		List<ZsUsbramTo> usbramToList = usbramDao.findAll();
		List<String> materialNumList = new ArrayList<String>();
		List<String> memorySizeList = new ArrayList<String>();
		List<String> operationVoltageList = new ArrayList<String>();
		List<String> vendorCodeList = new ArrayList<String>();
		List<String> ioList = new ArrayList<String>();
		List<String> maxResolutionList = new ArrayList<String>();

		if (usbramToList != null && usbramToList.size() > 0) {
			for (int i = 0; i < usbramToList.size(); i++) {
				ZsUsbramTo usbTo = usbramToList.get(i);
				materialNumList.add(usbTo.getMaterialNum());
				memorySizeList.add(usbTo.getMemorySize());
				operationVoltageList.add(usbTo.getOperationVoltage());
				vendorCodeList.add(usbTo.getVendorCode());
				ioList.add(usbTo.getIo());
				maxResolutionList.add(usbTo.getMaxResolution());
			}
		}
		if (materialNumList != null && materialNumList.size() > 0) {
			materialNumList = getUniqueList(materialNumList);
		}
		if (memorySizeList != null && memorySizeList.size() > 0) {
			memorySizeList = getUniqueList(memorySizeList);
		}
		if (operationVoltageList != null && operationVoltageList.size() > 0) {
			operationVoltageList = getUniqueList(operationVoltageList);
		}
		if (vendorCodeList != null && vendorCodeList.size() > 0) {
			vendorCodeList = getUniqueList(vendorCodeList);
		}
		if (ioList != null && ioList.size() > 0) {
			ioList = getUniqueList(ioList);
		}
		if (maxResolutionList != null && maxResolutionList.size() > 0) {
			maxResolutionList = getUniqueList(maxResolutionList);
		}

		if (request.getParameter("ref") != null) {
			// Create with reference
			String key = request.getParameter("ref");
			if (key.indexOf(",") >= 0) {
				String[] keys = key.split(",");
				if (keys != null && keys.length == 2) {
					request.setAttribute("ref", new ZsUsbramDao()
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
		request.setAttribute("operationVoltageList", operationVoltageList);
		request.setAttribute("vendorCodeList", vendorCodeList);
		request.setAttribute("ioList", ioList);
		request.setAttribute("maxResolutionList", maxResolutionList);

		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ZsUsbramDao newDao = new ZsUsbramDao();
		ZsUsbramQueryTo newTo = (ZsUsbramQueryTo) HttpHelper.pickupForm(
				ZsUsbramQueryTo.class, request, true);

		List<ZsUsbramTo> result = newDao.findBySearchPage(newTo);
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
		ZsUsbramDao usbDao = new ZsUsbramDao();
		ZsUsbramQueryTo usbQueryTo = (ZsUsbramQueryTo) HttpHelper.pickupForm(
				ZsUsbramQueryTo.class, request, true);
		request.setAttribute("result", usbDao.findBySearchPage(usbQueryTo));
		request.setAttribute("criteria", usbQueryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		ZsUsbramDao usbDao = new ZsUsbramDao();
		ZsUsbramQueryTo usbQueryTo = (ZsUsbramQueryTo) HttpHelper.pickupForm(
				ZsUsbramQueryTo.class, request, true);
		usbQueryTo.setPageNo(-1);
		List<ZsUsbramTo> result = usbDao.findBySearchPage(usbQueryTo);

		for (ZsUsbramTo usb : result) {
			String tapeVendor = usb.getVendorCode();
			tapeVendor = null != tapeVendor ? tapeVendor : "N/A";

			List<String> tapeVendorName = usbDao.findTapeShortName(tapeVendor);
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

			usb.setVendorCode(tapeVendorTmp);

		}

		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_22_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_22_SEQ"));
		return mapping.findForward("report");
	}

}
