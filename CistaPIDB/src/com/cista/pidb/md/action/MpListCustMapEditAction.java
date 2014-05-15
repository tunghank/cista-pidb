package com.cista.pidb.md.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.MpListCustomerMapDao;
import com.cista.pidb.md.to.MpListCustomerMapTo;

/**
 * .
 * 
 * @author Hu Meixia
 */

public class MpListCustMapEditAction extends DispatchAction {

	/** Logger. */
	private static final Log LOGGER = LogFactory.getLog(HttpHelper.class);

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
	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "success";
		MpListCustomerMapDao custMapDao = new MpListCustomerMapDao();
		MpListCustomerMapTo custMapTo = (MpListCustomerMapTo) HttpHelper
				.pickupForm(MpListCustomerMapTo.class, request);
		String[] custArray = request.getParameterValues("cust");
		String[] vendorArray = request.getParameterValues("vendor");
		String[] remarkArray = request.getParameterValues("tremark");
		String process = request.getParameter("selProcess");
		//System.out.println("remarkArray length= " + remarkArray.length);

		UserTo loginUser = PIDBContext.getLoginUser(request);
		custMapTo.setModifiedBy(loginUser.getUserId());
		// Before Insert , Delete original record
		custMapDao.deleteByPrimaryKey(custMapTo.getPartNum(), custMapTo
				.getIcFgMaterialNum(), custMapTo.getProjCodeWVersion(),
				custMapTo.getTapeName(), custMapTo.getPkgCode(), process);
		

		try {
			for (int i = 1; i <= custArray.length - 1; i++) {
				custMapTo.setPartNum(custMapTo.getPartNum());
				custMapTo.setIcFgMaterialNum(custMapTo.getIcFgMaterialNum());
				custMapTo.setPkgCode(custMapTo.getPkgCode());
				custMapTo.setProjCodeWVersion(custMapTo.getProjCodeWVersion());
				custMapTo.setTapeName(custMapTo.getTapeName());
				custMapTo.setProcess(process);
				custMapTo.setCust(custArray[i]);
				// System.out.println(eolCustArray[i]);
				custMapTo.setVendor(vendorArray[i]);
				custMapTo.setTremark(remarkArray[i]);
				// Before Insert , Delete original record
				custMapDao.deleteCustomer(custMapTo.getPartNum(), custMapTo
						.getIcFgMaterialNum(), custMapTo.getProjCodeWVersion(),
						custMapTo.getTapeName(), custMapTo.getPkgCode(),
						custMapTo.getProcess(), vendorArray[i],custArray[i]);
				custMapDao.insert(custMapTo, "PIDB_MP_LIST_MAP_CUSTOMER");
			}
			// System.out.println()"";
			PrintWriter out = response.getWriter();
			out.print("true");

		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		}

		return null;

	}
}
