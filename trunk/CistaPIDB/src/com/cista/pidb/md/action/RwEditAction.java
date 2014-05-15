package com.cista.pidb.md.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.RwDao;
import com.cista.pidb.md.to.RwTo;

public class RwEditAction extends DispatchAction{
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

        SapMasterVendorDao vendorDao = new SapMasterVendorDao();
        request.setAttribute("vendorList", vendorDao.findAll());
        String prodCode = request.getParameter("prodCode");
        String pkgCode = request.getParameter("pkgCode");
        RwTo to = new RwDao().find(prodCode, pkgCode);
        if (to != null) {
            request.setAttribute("ref", to);
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
        RwDao rwDao = new RwDao();
        RwTo rwTo = (RwTo) HttpHelper.pickupForm(RwTo.class, request);
        UserTo loginUser = PIDBContext.getLoginUser(request);
        rwTo.setModifiedBy(loginUser.getUserId());

        Map<String, Object> key = new HashMap<String, Object>();
        key.put("PROD_CODE", request.getParameter("prodCode"));
        key.put("PKG_CODE", request.getParameter("pkgCode"));

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
        rwTo.setAssignTo(assigns);
        rwTo.setAssignEmail(emails);
        
        rwDao.update(rwTo, "PIDB_RW", key);
        
        //send mail
        SendMailDispatch.sendMailByModify("MD-27", 
        		rwTo.getPkgCode() + "(" + rwTo.getProdCode() + ")", 
                emails, 
                SendMailDispatch.getUrl("MD_27_EDIT", request.getContextPath(), rwTo));              
        
        String mes = "Save Successfully";    
        request.setAttribute("error", mes);        
        request.setAttribute("ref", rwTo);
        return mapping.findForward(forward);
    }

}
