package com.cista.pidb.md.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ProdStdTestRefDao;
import com.cista.pidb.md.to.ProdStdTestRefTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class ProdStdTestRefCreateAction extends DispatchAction {
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
        SapMasterProductFamilyDao smpfDao = new SapMasterProductFamilyDao();
        request.setAttribute("productFamily", smpfDao.findAll());
        if (request.getParameter("ref") != null) {
            String testReferenceId = request.getParameter("ref");
            request.setAttribute("ref", new ProdStdTestRefDao().findByTestReferenceId(testReferenceId));
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
        ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
        ProdStdTestRefTo prodStdTestRefTo = (ProdStdTestRefTo) HttpHelper.pickupForm(ProdStdTestRefTo.class, request);
        UserTo loginUser = PIDBContext.getLoginUser(request);
        prodStdTestRefTo.setCreatedBy(loginUser.getUserId());

        String productLine = prodStdTestRefTo.getProductLine();
        String productFamily = prodStdTestRefTo.getProductFamily();
        String maxVar = (String) prodStdTestRefDao.findMaxVar(productLine, productFamily);
        String variant = "0";
        if (maxVar != null) {
            String preVar = maxVar.toString();
            if (preVar != null && !preVar.equals("")) {
                char c = (char) ((int) preVar.charAt(0) + 1);
                String realVar = "";
                if (c > 57 && c < 65) {
                    realVar = new String(new char[]{65});
                } else if (c > 90) {
                    realVar = new String(new char[]{48});
                } else {
                    realVar = new String(new char[]{c});
                }
                variant = realVar;
            }
        }
        prodStdTestRefTo.setVariant(variant);
        String testReferenceId = prodStdTestRefTo.getProductLine() + prodStdTestRefTo.getProductFamily() + variant;
        prodStdTestRefTo.setTestReferenceId(testReferenceId);

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
        prodStdTestRefTo.setAssignTo(assigns);
        prodStdTestRefTo.setAssignEmail(emails);
        
        prodStdTestRefDao.insert(prodStdTestRefTo, "PIDB_PROD_STD_TEST_REF");
        
        //send mail
        SendMailDispatch.sendMailByCreate("MD-17", 
                prodStdTestRefTo.getProductLine() + "-" + prodStdTestRefTo.getProductFamily() + "-" +prodStdTestRefTo.getSubClassification(), 
                emails, 
                SendMailDispatch.getUrl("MD_17_EDIT", request.getContextPath(), prodStdTestRefTo));   
        
        String mes = "Save Successfully";
        
        request.setAttribute("error", mes);        
        request.setAttribute("ref", prodStdTestRefTo);
        return mapping.findForward(forward);
    }
}
