package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.PkgRaDao;
import com.cista.pidb.md.to.PkgRaTo;

public class PkgRaCreateAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";
		if (request.getParameter("ref") != null) {
			// Create with reference
			String getParams = request.getParameter("ref");
			String[] params = getParams.split(",");
			String prodName = params[0];
			String pkgCode = params[1];
			String pkgType = params[2];
			String worksheetNumber = params[3];
			PkgRaTo pkgRaTo = new PkgRaDao().findByPrimaryKey(prodName,
					pkgCode, pkgType, worksheetNumber);
			request.setAttribute("ref", pkgRaTo);
		}

		String fundName = "RA";
		String funFieldName = "OWNER";

		//String fundName2 = "IC_TAPE";
		//String funFieldName2 = "ASSEMBLY_SITE";

		String fundName3 = "PKG_RA";
		String funFieldName3 = "RPT_VERSION_LIST";

		/*request.setAttribute("assemblySiteList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));*/

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		request.setAttribute("reportVersionList", new FunctionParameterDao()
				.findValueList(fundName3, funFieldName3));

		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save_success";
		PkgRaDao dao = new PkgRaDao();
		PkgRaTo newInstance = (PkgRaTo) HttpHelper.pickupForm(PkgRaTo.class,
				request, true);
		if (newInstance == null) {
			forward = "fail";
			request.setAttribute("error", "new PkgRaTo object can not found");
			return mapping.findForward(forward);
		}
		// differ with same primary key PkgRaTo is or not exist
		PkgRaTo exist = dao.findByPrimaryKey(newInstance.getProdName(),
				newInstance.getPkgCode(), newInstance.getPkgType(), newInstance
						.getWorksheetNumber());
		if (exist != null) {
			forward = "fail";
			request.setAttribute("error",
					"Primary Key (Product Name and Package Code)conflict!");
			return mapping.findForward(forward);
		}

		

		// assignEmail
		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		String emails = userDao.fetchEmail(assignTo);
		newInstance.setAssignTo(assigns);
		newInstance.setAssignEmail(emails);

		// convert PkgType
		// SapMasterPackageTypeDao sapMasterPackageTypeDao = new
		// SapMasterPackageTypeDao();
		// SapMasterPackageTypeTo sapMasterPackageTypeTo =
		// sapMasterPackageTypeDao.findByDesc(newInstance.getPkgType());
		// if (sapMasterPackageTypeTo != null) {
		// newInstance.setPkgType(sapMasterPackageTypeTo.getPackageType());
		// }

		// convert VendorCode
		if (newInstance.getTapeVendor() != null
				&& !newInstance.getTapeVendor().equals("")) {
			SapMasterVendorDao SapMasterVendorDao = new SapMasterVendorDao();
			SapMasterVendorTo sapMasterVendorTo = SapMasterVendorDao
					.findByShortName(newInstance.getTapeVendor());
			if (sapMasterVendorTo != null) {
				newInstance.setTapeVendor(sapMasterVendorTo.getVendorCode());
			}
		} 
		

		// set created by
		UserTo loginUser = PIDBContext.getLoginUser(request);
		newInstance.setCreatedBy(loginUser.getUserId());
		dao.insertPkgRa(newInstance);

		// send mail
		SendMailDispatch.sendMailByCreate("MD-15", newInstance.getPkgCode()
				+ "(" + newInstance.getProdName() + ")", emails,
				SendMailDispatch.getUrl("MD_15_EDIT", request.getContextPath(),
						newInstance));

		String mes = "Save Successfully";

		request.setAttribute("error", mes);
		String targetForward = mapping.findForward(forward).getPath();

		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}

		ActionForward actionForward = new ActionForward(targetForward + "pdn="
				+ newInstance.getProdName() + "&pgc="
				+ newInstance.getPkgCode() + "&wsn="
				+ newInstance.getWorksheetNumber() + "&pgt="
				+ newInstance.getPkgType());

		return actionForward;
	}
}
