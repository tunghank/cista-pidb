package com.cista.pidb.md.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.FtTestProgramDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.FtTestProgramERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.FtTestProgramTo;
import com.cista.pidb.md.to.IcFgTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class FtTestProgramCreateAction extends DispatchAction {
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
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "viewJsp";
        SapMasterVendorDao vendorDao = new SapMasterVendorDao();
        request.setAttribute("vendorList", vendorDao.findAll());
        
        if (request.getParameter("ref") != null) {
        	String[] keys = request.getParameter("ref").split(",");
            // Remove Hank 2007/12/21
        	if (keys != null && keys.length == 2) {
            request.setAttribute("ref", new FtTestProgramDao().findByPrimaryKey(keys[0], keys[1]));
        }
       }
		String fundName = "FT_TEST_PROG";
		String funFieldName = "CONTACT_DIE_QTY";
		List<FunctionParameterTo> contactDieQtyList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		request.setAttribute("contactDieQtyList", contactDieQtyList);
        return mapping.findForward(forward);
    }

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
		FtTestProgramDao ftpDao = new FtTestProgramDao();
		final FtTestProgramTo ftpTo = (FtTestProgramTo) HttpHelper.pickupForm(
				FtTestProgramTo.class, request);
		final UserTo loginUser = PIDBContext.getLoginUser(request);
		ftpTo.setCreatedBy(loginUser.getUserId());

		// add assignTo and assignEmail
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
		ftpTo.setAssignTo(assigns);
		ftpTo.setAssignEmail(emails);

		ftpDao.insert(ftpTo, "PIDB_FT_TEST_PROGRAM");

		// 1.0 2008/02/13 add for Hank Update IC_FG
		System.out.println("Update IC FG");
		String ftMaterialNum = ftpTo.getFtMaterialNum();
		ftMaterialNum = null != ftMaterialNum ? ftMaterialNum : "";
		ftMaterialNum = ftMaterialNum.trim();

		IcFgDao updateIcFgDao = new IcFgDao();
		IcFgTo updateIcFgTo = new IcFgTo();
		FtTestProgramTo chkFtTestProgramTo = ftpDao
				.findByFtMaterialNum(ftMaterialNum);

		// 1.0 Add 2007/12/20 New Rule
		updateIcFgTo = updateIcFgDao.findByMaterialNum(ftMaterialNum);

		if (chkFtTestProgramTo == null) {
			// 1.1 Multiple NULL
			// Add 2007/12/20 New Rule
			System.out.println("chkFtTestProgramTo Is NULL");

			// 1.2 Select FT Program form PIDB_FT_TEST_PROGRAM
			String testProgName = ftpTo.getFtTestProgName();
			testProgName = null != testProgName ? testProgName : "";
			FtTestProgramTo tmpFtTestProgramTo = ftpDao.findByPrimaryKey(
					ftMaterialNum, testProgName);

			System.out.println("testProgName " + testProgName);
			// 1.3 Update IC_FG FT_TEST_PROGRAM_LIST
			updateIcFgTo.setFtTestProgList(testProgName);
			Map<String, Object> key1 = new HashMap<String, Object>();
			key1.put("MATERIAL_NUM", ftMaterialNum);
			updateIcFgDao.update(updateIcFgTo);

		} else {
			// 1.1 Multiple NOT NULL -->To Merage Some Character Value
			// Add 2007/12/20 New Rule
			System.out.println("chkFtTestProgramTo NOT NULL");

			// 1.2 Select FT Program form PIDB_FT_TEST_PROGRAM
			List ftTestProgramList = (List) ftpDao
					.findListByFtMaterialNum(ftMaterialNum);
			String testProgName = "";
			for (int i = 0; i < ftTestProgramList.size(); i++) {
				if (i == (ftTestProgramList.size() - 1)) {
					testProgName = testProgName + ftTestProgramList.get(i);
				} else {
					testProgName = testProgName + ftTestProgramList.get(i)
							+ ",";
				}
			}
			System.out.println("testProgName " + testProgName);
			// 1.3 Update IC_FG FT_TEST_PROGRAM_LIST
			updateIcFgTo.setFtTestProgList(testProgName);
			Map<String, Object> key1 = new HashMap<String, Object>();
			key1.put("MATERIAL_NUM", ftMaterialNum);
			updateIcFgDao.update(updateIcFgTo);

		}

		// send mail
		SendMailDispatch.sendMailByCreate("MD-14", ftpTo.getFtTestProgName(),
				emails, SendMailDispatch.getUrl("MD_14_EDIT", request
						.getContextPath(), ftpTo));

		String toErp = request.getParameter("toErp");
		if ("1".equals(toErp.trim())) {
			System.out.println("Release to SAP");
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				String result = null;

				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					result = FtTestProgramERP.release(ftpTo, loginUser);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
				}
			};

			try {
				new FtTestProgramDao().doInTransaction(callback);

			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));

				SapMasterVendorDao vendorDao = new SapMasterVendorDao();
				request.setAttribute("vendorList", vendorDao.findAll());
				request.setAttribute("ref", ftpTo);
				return mapping.findForward("viewEditJsp");
			}
			// send mail
			SendMailDispatch.sendMailByErp("MD-14", ftpTo.getFtTestProgName(),
					emails, "");
		}

		String mes = "";
		if ("1".equals(toErp.trim())) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);
		SapMasterVendorDao vendorDao = new SapMasterVendorDao();
		request.setAttribute("vendorList", vendorDao.findAll());
		request.setAttribute("ref", ftpTo);
		return mapping.findForward("viewEditJsp");
	}
}
