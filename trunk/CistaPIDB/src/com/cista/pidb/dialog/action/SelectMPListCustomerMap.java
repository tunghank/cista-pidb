package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.md.dao.MpListCustomerMapDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.to.MpListTo;

public class SelectMPListCustomerMap extends DispatchAction {

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
		String condition = request.getParameter("condition");

		String partNum = request.getParameter("partNum");
		String icFgMaterialNum = request.getParameter("icFgMaterialNum");
		String projCodeWVersion = request.getParameter("projCodeWVersion");
		String tapeName = request.getParameter("tapeName");
		String pkgCode = request.getParameter("pkgCode");
		String process = request.getParameter("process");
		MpListCustomerMapDao CustDao = new MpListCustomerMapDao();
		SapMasterCustomerDao smcDao = new SapMasterCustomerDao();
		MpListTo to = new MpListDao().findByPrimaryKey(partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode);
		SapMasterCustomerTo custTo = new SapMasterCustomerTo();
		SapMasterVendorDao smcVendorDao = new SapMasterVendorDao();
		SapMasterVendorTo vendorTo = new SapMasterVendorTo();
		// 取出Approve Customer List
		List<SapMasterCustomerTo> sapCustomerTo = new ArrayList<SapMasterCustomerTo>();
		if (to.getApproveCust() != null) {
			String[] custs = (to.getApproveCust().split(","));
			for (String cust : custs) {
				if (cust != null && !cust.equals("")) {
					custTo = smcDao.findByVendorCode(cust);
					sapCustomerTo.add(custTo);

				}
				// System.out.println(sapCustomerTo);
			}
			

		}
		// 取出Tape Approve Customer List
		List<SapMasterVendorTo> sapVendorTo = new ArrayList<SapMasterVendorTo>();
		// For Tape Vendor Map
		if (process.equals("TAPE")) {
			if (to.getApproveTapeVendor() != null) {
				String[] vendors = (to.getApproveTapeVendor().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For Bumping Vendor Map
		} else if (process.equals("BUMP")) {
			if (to.getApproveBpVendor() != null) {
				String[] vendors = (to.getApproveBpVendor().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For CP House Vendor Map
		} else if (process.equals("CP")) {
			if (to.getApproveCpHouse() != null) {
				String[] vendors = (to.getApproveCpHouse().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For Assembly House Vendor Map
		} else if (process.equals("ASSY")) {
			if (to.getApproveAssyHouse() != null) {
				String[] vendors = (to.getApproveAssyHouse().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For FT Test Vendor Map
		} else if (process.equals("FT")) {
			if (to.getApproveFtHouse() != null) {
				String[] vendors = (to.getApproveFtHouse().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For Polish Vendor Map
		} else if (process.equals("POLISH")) {
			if (to.getApprovePolishVendor() != null) {
				String[] vendors = (to.getApprovePolishVendor().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For Color Filter Vendor Map
		} else if (process.equals("CF")) {
			if (to.getApproveColorFilterVendor() != null) {
				String[] vendors = (to.getApproveColorFilterVendor().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For Turnkey(Wafer + Color Filter) Vendor Map
		} else if (process.equals("WCF")) {
			if (to.getApproveWaferCfVendor() != null) {
				String[] vendors = (to.getApproveWaferCfVendor().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
			// For CSP Vendor Map
		} else if (process.equals("CSP")) {
			if (to.getApproveCpCspVendor() != null) {
				String[] vendors = (to.getApproveCpCspVendor().split(","));
				for (String vendor : vendors) {
					if (vendor != null && !vendor.equals("")) {
						vendorTo = smcVendorDao.findByVendorCode(vendor);
						sapVendorTo.add(vendorTo);

					}
				}

			}
		}

		request.setAttribute("selectList", sapCustomerTo);
		request.setAttribute("selectVendorList", sapVendorTo);
		request.setAttribute("custMap", CustDao.findByPrimaryKey(partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode, process));
		request.setAttribute("callback", callback);
		request.setAttribute("condition", condition);

		request.setAttribute("custMapList", CustDao.findByPrimaryKey(partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode, process));

		request.setAttribute("partNum", partNum);
		request.setAttribute("icFgMaterialNum", icFgMaterialNum);
		request.setAttribute("projCodeWVersion", projCodeWVersion);
		request.setAttribute("tapeName", tapeName);
		request.setAttribute("pkgCode", pkgCode);
		request.setAttribute("process", process);

		return mapping.findForward(forward);
	}

}
