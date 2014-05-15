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
import com.cista.pidb.md.dao.ZsEepromDao;
import com.cista.pidb.md.to.ZsEepromQueryTo;
import com.cista.pidb.md.to.ZsEepromTo;

public class ZsEepromQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ZsEepromDao eepromDao = new ZsEepromDao();
		//
		List<ZsEepromTo> eepromToList = eepromDao.findAll();
		List<String> materialNumList = new ArrayList<String>();
		List<String> speedList = new ArrayList<String>();
		List<String> densityList = new ArrayList<String>();
		List<String> operationVoltageList = new ArrayList<String>();

		if (eepromToList != null && eepromToList.size() > 0) {
			for (int i = 0; i < eepromToList.size(); i++) {
				ZsEepromTo eepromTo = eepromToList.get(i);
				materialNumList.add(eepromTo.getMaterialNum());
				speedList.add(eepromTo.getSpeed());
				densityList.add(eepromTo.getDensity());
				operationVoltageList.add(eepromTo.getOperationVoltage());
			}
		}
		if (materialNumList != null && materialNumList.size() > 0) {
			materialNumList = getUniqueList(materialNumList);
		}
		if (speedList != null && speedList.size() > 0) {
			speedList = getUniqueList(speedList);
		}
		if (densityList != null && densityList.size() > 0) {
			densityList = getUniqueList(densityList);
		}

		if (operationVoltageList != null && operationVoltageList.size() > 0) {
			operationVoltageList = getUniqueList(operationVoltageList);
		}

		if (request.getParameter("ref") != null) {
			// Create with reference
			String key = request.getParameter("ref");
			if (key.indexOf(",") >= 0) {
				String[] keys = key.split(",");
				if (keys != null && keys.length == 2) {
					request.setAttribute("ref", new ZsEepromDao()
							.findByPrimaryKey(keys[0]));
				}
			}
		}

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("materialNumList", materialNumList);
		request.setAttribute("densityList", densityList);
		request.setAttribute("speedList", speedList);
		request.setAttribute("operationVoltageList", operationVoltageList);
		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ZsEepromDao newDao = new ZsEepromDao();
		//
		ZsEepromQueryTo newTo = (ZsEepromQueryTo) HttpHelper.pickupForm(
				ZsEepromQueryTo.class, request, true);

		List<ZsEepromTo> result = newDao.findBySearchPage(newTo);
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
		ZsEepromDao eepromDao = new ZsEepromDao();
		ZsEepromQueryTo eepromQueryTo = (ZsEepromQueryTo) HttpHelper
				.pickupForm(ZsEepromQueryTo.class, request, true);
		request.setAttribute("result", eepromDao
				.findBySearchPage(eepromQueryTo));
		request.setAttribute("criteria", eepromQueryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		ZsEepromDao eepromDao = new ZsEepromDao();
		ZsEepromQueryTo eepromQueryTo = (ZsEepromQueryTo) HttpHelper
				.pickupForm(ZsEepromQueryTo.class, request, true);
		eepromQueryTo.setPageNo(-1);
		List<ZsEepromTo> result = eepromDao.findBySearchPage(eepromQueryTo);

		for (ZsEepromTo eeprom : result) {
			String tapeVendor = eeprom.getVendorCode();
			tapeVendor = null != tapeVendor ? tapeVendor : "N/A";

			List<String> tapeVendorName = eepromDao
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

			eeprom.setVendorCode(tapeVendorTmp);

		}

		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_19_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_19_SEQ"));
		return mapping.findForward("report");
	}
}