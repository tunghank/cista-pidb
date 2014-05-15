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
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.dao.WlmDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.erp.WlxPkgERP;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.WlmTo;

public class WlmEditAction extends DispatchAction{
	protected final Log logger = LogFactory.getLog(getClass());
	
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
            String forward = "pre_success";
            WlmDao wlmDao = new WlmDao();
            String materialNum = (String) request.getParameter("materialNum");
            logger.debug("materialNum " + materialNum);
            if (materialNum == null || materialNum.length() <= 0) {
            	materialNum = (String)request.getAttribute("materialNum");
            }
            WlmTo wlmTo = (WlmTo) wlmDao.findByPrimaryKey(materialNum);
            if (wlmTo != null) {
                request.setAttribute("wlmTo", wlmTo);
            }
            
    		String fundName = "TSV";
    		String funFieldName = "VERSION";
    		List<FunctionParameterTo> tsvVersionList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		fundName = "CSP";
    		funFieldName = "VERSION";
    		List<FunctionParameterTo> cspVersionList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		List<FunctionParameterTo> wlmVersionList = new ArrayList<FunctionParameterTo>();
    		wlmVersionList.addAll(tsvVersionList);
    		wlmVersionList.addAll(cspVersionList);
    		
    		fundName = "WLM";
    		funFieldName = "LENS";
    		List<FunctionParameterTo> lensList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);

    		funFieldName = "PERSPECTIVE";
    		List<FunctionParameterTo> perspectiveList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		funFieldName = "FNO";
    		List<FunctionParameterTo> fnoList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		funFieldName = "IR_COATING";
    		List<FunctionParameterTo> irCoatingList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		funFieldName = "GOL_COATING";
    		List<FunctionParameterTo> golCoatingList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
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
    		
    		funFieldName = "BARREL_TYPE";
    		List<FunctionParameterTo> barrelTypeList = new FunctionParameterDao()
			.findValueList(fundName, funFieldName);
    		
    		request.setAttribute("wlmVersionList", wlmVersionList);
    		request.setAttribute("lensList", lensList);
    		request.setAttribute("perspectiveList", perspectiveList);
    		request.setAttribute("fnoList", fnoList);
    		request.setAttribute("irCoatingList", irCoatingList);
    		request.setAttribute("golCoatingList", golCoatingList);
    		request.setAttribute("prodTypeList", prodTypeList);
    		request.setAttribute("vendorCodeList", vendorCodeList);
    		request.setAttribute("serialNumList", serialNumList);
    		request.setAttribute("mpStatusList", mpStatusList);
    		request.setAttribute("packingTypeList", packingTypeList);
    		request.setAttribute("barrelTypeList", barrelTypeList);
    		
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
		WlmDao wlmDao = new WlmDao();
		WlmTo wlmTo = (WlmTo) HttpHelper.pickupForm(WlmTo.class, request);
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
		wlmTo.setModifiedBy(loginUser.getUserId());

		wlmTo.setUdt(df.format(Calendar.getInstance().getTime()));
		wlmDao.updateWlm(wlmTo);
		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("WLM", "UPDATE", AjaxHelper.bean2String(wlmTo) , loginUser.getUserId());
		
		wlmTo = new WlmDao().findByPrimaryKey(wlmTo.getMaterialNum());
		final WlmTo wlmErp = wlmTo;
		final UserTo user = loginUser;
		final IcFgDao icFgDao = new IcFgDao();
		
		if (isRelease.equals("1")) {
			logger.debug(" isRelease.equals = 1 ");
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = WlxPkgERP.releaseWLM(wlmErp, user);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
					
					//2.0 Insert Record to IC FG for MP List used
					IcFgTo icFgTo = new IcFgTo();
					icFgTo.setMaterialNum(wlmErp.getMaterialNum());
					icFgTo.setProdCode(wlmErp.getProdCode());
					icFgTo.setVariant(wlmErp.getSerialNum());
					icFgTo.setProjCode(wlmErp.getProjCode());
					icFgTo.setPkgType("313");
					icFgTo.setPkgCode(wlmErp.getPackageCode());
					icFgTo.setPartNum(wlmErp.getPartNum());
					icFgTo.setRoutingFg(true);
					icFgTo.setRoutingAs(false);					
					// add mpStatus
					
					if (wlmErp.equals("1")) {
						icFgTo.setMpStatus("MP");
					} else {
						icFgTo.setMpStatus("Non-MP");
					}
					icFgTo.setAppCategory(wlmErp.getAppCategory());
					icFgTo.setMcpDieQty("N/A");
					icFgTo.setMcpPkg("N/A");
					icFgTo.setMcpProd1("N/A");
					icFgTo.setMcpProd2("N/A");
					icFgTo.setMcpProd3("N/A");
					icFgTo.setMcpProd4("N/A");
					icFgTo.setMcpPkg("0");
					icFgTo.setCreatedBy(wlmErp.getCreatedBy());
					icFgTo.setModifiedBy(wlmErp.getModifiedBy());
					icFgTo.setStatus("Released");
					icFgTo.setVendorDevice(wlmErp.getVendorDevice());
					icFgDao.delete(icFgTo);
					icFgDao.insert(icFgTo);
					
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
		request.setAttribute("ref", wlmTo);
		return mapping.findForward(forward);
    }

}
