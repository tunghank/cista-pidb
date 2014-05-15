package com.cista.pidb.md.action;


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
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.erp.BumpMaskERP;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;
/**
 * .
 * @author Hu Meixia
 */
public class BumpMaskCreateAction extends DispatchAction {
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "viewJsp";
        if (request.getParameter("ref") != null) {
            //Create with reference
            String key = request.getParameter("ref");
            if (key.indexOf(",") >= 0) {
                String[] keys = key.split(",");
                if (keys != null && keys.length == 2) {
                    request.setAttribute("ref", new BumpMaskDao().findByPrimaryKey(keys[0], keys[1]));
                }
            }
        }
        return mapping.findForward(forward);
    }
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        BumpMaskDao bumpMaskDao = new BumpMaskDao();
        final BumpMaskTo bumpMaskTo = (BumpMaskTo) HttpHelper.pickupForm(BumpMaskTo.class, request);
        final UserTo loginUser = PIDBContext.getLoginUser(request);
        bumpMaskTo.setCreatedBy(loginUser.getUserId());
        
//      add assignTo and assignEmail
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
        bumpMaskTo.setAssignTo(assigns);
        bumpMaskTo.setAssignEmail(emails);
        
        bumpMaskDao.insert(bumpMaskTo, "PIDB_BUMPING_MASK");
        
        //send mail
        SendMailDispatch.sendMailByCreate("MD-3", bumpMaskTo.getMaskName(), emails, SendMailDispatch.getUrl("MD_3_EDIT", request.getContextPath(), bumpMaskTo));
        
        String toErp = request.getParameter("toErp");        
        if("1".equals(toErp.trim())) {
        	
        	TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
        		String result = null;
                public void doInTransactionWithoutResult(final TransactionStatus status) {
                	 result = BumpMaskERP.release(bumpMaskTo,loginUser);
                	if (result != null) {
            			throw new ReleaseERPException(result); 
            		} 
                }
            };
            
            try {
    			new BumpMaskDao().doInTransaction(callback);
    			
    		} catch (ReleaseERPException e) {
    			request.setAttribute("error", ERPHelper.getErrorMessage(e.getMessage()));
    			
    	        request.setAttribute("ref", bumpMaskTo);
    			return mapping.findForward("viewEditJsp");
    		}
            
            //send mail
            SendMailDispatch.sendMailByErp("MD-3", bumpMaskTo.getMaskName(), emails, "");            
    	}
        
        
        String mes = "";
        if ("1".equals(toErp.trim())) {
            mes = "Save and Release to ERP Successfully";
        } else {
            mes = "Save Successfully";
        }
        request.setAttribute("error", mes);
        request.setAttribute("ref", bumpMaskTo);

        return mapping.findForward("viewEditJsp");  
    }
}
