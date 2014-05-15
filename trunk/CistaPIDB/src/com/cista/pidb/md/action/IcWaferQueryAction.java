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
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.to.IcWaferQueryTo;
import com.cista.pidb.md.to.IcWaferTo;

public class IcWaferQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";
		IcWaferDao icWaferDao = new IcWaferDao();
		//
		List<IcWaferTo> icWaferToList = icWaferDao.findAll();
		List<String> bodyVersionList = new ArrayList<String>();
		List<String> optionVersionList = new ArrayList<String>();
		List<String> fabDeviceIdList = new ArrayList<String>();
		List<String> revisionItemList = new ArrayList<String>();

		if (icWaferToList != null && icWaferToList.size() > 0) {
			for (int i = 0; i < icWaferToList.size(); i++) {
				IcWaferTo oneIcWaferTo = icWaferToList.get(i);
				bodyVersionList.add(oneIcWaferTo.getBodyVer());
				optionVersionList.add(oneIcWaferTo.getOptionVer());
				fabDeviceIdList.add(oneIcWaferTo.getFabDeviceId());
				revisionItemList.add(oneIcWaferTo.getRevisionItem());
			}
		}
		if (bodyVersionList != null && bodyVersionList.size() > 0) {
			bodyVersionList = getUniqueList(bodyVersionList);
		}
		if (optionVersionList != null && optionVersionList.size() > 0) {
			optionVersionList = getUniqueList(optionVersionList);
		}
		if (fabDeviceIdList != null && fabDeviceIdList.size() > 0) {
			fabDeviceIdList = getUniqueList(fabDeviceIdList);
		}
		if (revisionItemList != null && revisionItemList.size() > 0) {
			revisionItemList = getUniqueList(revisionItemList);
		}
		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));
		//
		request.setAttribute("bodyVersionList", bodyVersionList);
		request.setAttribute("optionVersionList", optionVersionList);
		request.setAttribute("fabDeviceIdList", fabDeviceIdList);
		request.setAttribute("revisionItemList", revisionItemList);
		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "query_success";
		IcWaferDao newDao = new IcWaferDao();
		//
		IcWaferQueryTo newTo = (IcWaferQueryTo) HttpHelper.pickupForm(
				IcWaferQueryTo.class, request, true);

		List<IcWaferTo> result = newDao.findBySearchPage(newTo);
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
		IcWaferDao icWaferDao = new IcWaferDao();
		IcWaferQueryTo icWaferQueryTo = (IcWaferQueryTo) HttpHelper.pickupForm(
				IcWaferQueryTo.class, request, true);
		request.setAttribute("result", icWaferDao
				.findBySearchPage(icWaferQueryTo));
		request.setAttribute("criteria", icWaferQueryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		IcWaferDao icWaferDao = new IcWaferDao();
		IcWaferQueryTo icWaferQueryTo = (IcWaferQueryTo) HttpHelper.pickupForm(
				IcWaferQueryTo.class, request, true);
		icWaferQueryTo.setPageNo(-1);
		List<IcWaferTo> result = icWaferDao.findBySearchPage(icWaferQueryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_4_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_4_SEQ"));
		return mapping.findForward("report");
	}
}
