package com.cista.pidb.md.action;


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
import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.to.CogTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class CogCreateAction extends DispatchAction {
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
        
        if (request.getParameter("ref") != null) {
            String getParams = request.getParameter("ref");
            String[]params = getParams.split(",");
            String prodCode = params[0];
            String pkgCode = params[1];
            request.setAttribute("ref", new CogDao().find(prodCode, pkgCode));
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
        CogDao cogDao = new CogDao();
        CogTo cogTo = (CogTo) HttpHelper.pickupForm(CogTo.class, request);
        UserTo loginUser = PIDBContext.getLoginUser(request);
        cogTo.setCreatedBy(loginUser.getUserId());

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
        cogTo.setAssignTo(assigns);
        cogTo.setAssignEmail(emails);
        
        cogDao.insert(cogTo, "PIDB_COG");
        
        //send mail
        SendMailDispatch.sendMailByCreate("MD-11", 
                cogTo.getPkgCode() + "(" + cogTo.getProdCode() + ")", 
                emails, 
                SendMailDispatch.getUrl("MD_11_EDIT", request.getContextPath(), cogTo));          
       
        String mes = "Save Successfully";    
        request.setAttribute("error", mes);        
        request.setAttribute("ref", cogTo);
        return mapping.findForward(forward);
    }
}
