package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapAppCategoryDao;
import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.dao.WlhDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.erp.WlxPkgERP;
import com.cista.pidb.md.to.WlhTo;

public class WlhEditAction extends DispatchAction{
	protected final Log logger = LogFactory.getLog(getClass());
	
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
            String forward = "pre_success";
            WlhDao wlhDao = new WlhDao();
            String materialNum = (String) request.getParameter("materialNum");
            logger.debug("materialNum " + materialNum);
            if (materialNum == null || materialNum.length() <= 0) {
            	materialNum = (String)request.getAttribute("materialNum");
            }
            WlhTo wlhTo = (WlhTo) wlhDao.findByPrimaryKey(materialNum);
            if (wlhTo != null) {
                request.setAttribute("wlhTo", wlhTo);
            }
            
    		String fundName = "TSV";
    		String funFieldName = "VERSION";
    		List<FunctionParameterTo> tsvVersionList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		fundName = "CSP";
    		funFieldName = "VERSION";
    		List<FunctionParameterTo> cspVersionList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		List<FunctionParameterTo> wlhVersionList = new ArrayList<FunctionParameterTo>();
    		wlhVersionList.addAll(tsvVersionList);
    		wlhVersionList.addAll(cspVersionList);
    		
    		fundName = "WLH";
    		funFieldName = "LENS";

    		
    		funFieldName = "PROD_TYPE";
    		List<FunctionParameterTo> prodTypeList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		funFieldName = "VENDOR_CODE";
    		List<FunctionParameterTo> vendorCodeList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		funFieldName = "SERIAL_NUM";
    		List<FunctionParameterTo> serialNumList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		funFieldName = "MP_STATUS";
    		List<FunctionParameterTo> mpStatusList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);

    		funFieldName = "PACKING_TYPE";
    		List<FunctionParameterTo> packingTypeList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		request.setAttribute("wlhVersionList", wlhVersionList);

    		request.setAttribute("prodTypeList", prodTypeList);
    		request.setAttribute("vendorCodeList", vendorCodeList);
    		request.setAttribute("serialNumList", serialNumList);
    		request.setAttribute("mpStatusList", mpStatusList);
    		request.setAttribute("packingTypeList", packingTypeList);
    		request.setAttribute("appCateGoryList", new SapAppCategoryDao()
			.findAll());
    		
            return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save_success";
		String isRelease = (String) request.getParameter("isRelease");

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		WlhDao wlhDao = new WlhDao();
		WlhTo wlhTo = (WlhTo) HttpHelper.pickupForm(WlhTo.class, request);
		//assignEmail
		/*String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
		    for (String a : assignTo) {
		        assigns += "," + a;
		    }
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		String emails = userDao.fetchEmail(assignTo);
		tsvTo.setAssignTo(assigns);
		tsvTo.setAssignEmail(emails);*/

		//set modified by
		UserTo loginUser = PIDBContext.getLoginUser(request);
		wlhTo.setModifiedBy(loginUser.getUserId());

		wlhTo.setUdt(df.format(Calendar.getInstance().getTime()));
		wlhDao.updateWlh(wlhTo);
		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("WLH", "UPDATE", AjaxHelper.bean2String(wlhTo), loginUser.getUserId());

		wlhTo = new WlhDao().findByPrimaryKey(wlhTo.getMaterialNum());
		final WlhTo wlhErp = wlhTo;
		final UserTo user = loginUser;
		final IcFgDao icFgDao = new IcFgDao();
		
		if (isRelease.equals("1")) {
			logger.debug(" isRelease.equals = 1 ");
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = WlxPkgERP.releaseWLH(wlhErp, user);
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

				return mapping.findForward("release_fail");
			}

			// send mail

			// update status Released

		} else {
			logger.debug(" isRelease.equals != 1 ");
		}

		String mes = "";
		if (isRelease.equals("1")) {
			mes = "Update and Release to ERP Successfully";
		} else {
			mes = "Update Successfully";
		}

		request.setAttribute("error", mes);
		request.setAttribute("ref", wlhTo);
		return mapping.findForward(forward);
    }

}
