package com.cista.pidb.md.action;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapDefaultSingleValueDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ZsSdramDao;
import com.cista.pidb.md.dao.ZsSeqDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.erp.ZsReleaseERP;
import com.cista.pidb.md.to.ZsSdramTo;
import com.cista.pidb.md.to.ZsSeqTo;

public class ZsSdramCreateAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "create";
		if (request.getParameter("ref") != null) {
			// Create with reference
			String materialNum = (String) request.getParameter("ref");
			ZsSdramTo sdramTo = new ZsSdramDao().findByPrimaryKey(materialNum);
			request.setAttribute("ref", sdramTo);
		}
		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save";
		ZsSdramDao sdramDao = new ZsSdramDao();
		ZsSdramTo sdramTo = (ZsSdramTo) HttpHelper.pickupForm(ZsSdramTo.class,
				request, true);

		// for materialNum.
		String variableName = "zs_company_code";
		SapDefaultSingleValueDao sapDefaultSingleValueDao = new SapDefaultSingleValueDao();
		String zsCompanyCode = sapDefaultSingleValueDao
				.findDefaultValue(variableName);
		String seqNum = "";
		String Zs = "S";
		String Type = "D";
		String seqname = "0";
		String companyCode = zsCompanyCode;
		ZsSeqDao seqDao = new ZsSeqDao();
		ZsSeqTo seqTo = new ZsSeqTo();
		seqTo.setSeqZs(Zs);
		seqTo.setSeqType(Type);
		DecimalFormat df = new DecimalFormat("000000000");
		try {
			if (!seqDao.isSeqNumExist(Zs, Type)) {
				seqTo.setSeqNum("1");
				seqDao.insertSeq(seqTo);
				seqNum = seqDao.getSeqNum(Zs, Type);
				seqNum = df.format(Long.parseLong(seqNum)).toString();
			} else {
				seqNum = seqDao.getSeqNum(Zs, Type);
				int num = (Integer.parseInt(seqNum) + 1);
				String sNum = Integer.toString(num);
				seqTo.setSeqNum(sNum);
				seqDao.updateSeqNum(seqTo);
				seqNum = seqDao.getSeqNum(Zs, Type);
				seqNum = df.format(Long.parseLong(seqNum)).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String materialNum = Zs + Type + seqname + companyCode + seqNum;
		sdramTo.setMaterialNum(materialNum);

		// Transfer product code
		String[] prodCodes = request.getParameterValues("prodCodeList");
		String allProdCode = "";
		if (prodCodes != null && prodCodes.length > 0) {
			for (String prodCode : prodCodes) {
				allProdCode += "," + prodCode;
			}
		}

		if (allProdCode != null && allProdCode.length() > 0) {
			sdramTo.setApplicationProduct(allProdCode.substring(1));
		}

		// Transfer Vendor code
		String[] vendorList = request.getParameterValues("sdramVendor");
		String allVendor = "";
		if (vendorList != null && vendorList.length > 0) {
			for (String vendor : vendorList) {
				allVendor += "," + vendor;
			}
		}

		if (allVendor != null && allVendor.length() > 0) {
			sdramTo.setVendorCode(allVendor.substring(1));
		}
		UserDao userDao = new UserDao();

		UserTo loginUser = PIDBContext.getLoginUser(request);
		sdramTo.setCreatedBy(loginUser.getUserId());

		sdramDao.insertSdram(sdramTo);

		/***********************************************************************
		 * Release to ERP Add 2008/06/16 Hank Added
		 **********************************************************************/
		final UserTo user = loginUser;
		final ZsSdramTo zsSdram = sdramTo;
		String toErp = request.getParameter("toErp");

		if ("1".equals(toErp.trim())) {

			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = null;
					result = ZsReleaseERP.releaseSDRAM(zsSdram, user);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
				}
			};

			try {
				new FunctionDao().doInTransaction(callback);
			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));
				String targetForward = mapping.findForward("release_fail")
						.getPath();
				if (targetForward.indexOf('?') == -1) {
					targetForward = targetForward + "?";
				} else {
					targetForward = targetForward + "&";
				}
				ActionForward action = new ActionForward(targetForward
						+ "materialNum=" + sdramTo.getMaterialNum());
				return action;
			}
		} else {

		}

		String mes = "";
		if (toErp.equals("1")) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);

		String targetForward = mapping.findForward("viewEditJsp").getPath();
		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}
		ActionForward action = new ActionForward(targetForward + "materialNum="
				+ sdramTo.getMaterialNum());
		return action;
	}
}
