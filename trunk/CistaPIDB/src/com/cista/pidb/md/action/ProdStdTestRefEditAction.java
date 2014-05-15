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
public class ProdStdTestRefEditAction extends DispatchAction {
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
            String testReferenceId = request.getParameter("testReferenceId");
            ProdStdTestRefTo to = new ProdStdTestRefDao().findByTestReferenceId(testReferenceId);
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
        ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
        ProdStdTestRefTo prodStdTestRefTo = (ProdStdTestRefTo) HttpHelper.pickupForm(ProdStdTestRefTo.class, request);
        UserTo loginUser = PIDBContext.getLoginUser(request);
        prodStdTestRefTo.setModifiedBy(loginUser.getUserId());

        Map<String, Object> key = new HashMap<String, Object>();
        key.put("TEST_REFERENCE_ID", request.getParameter("testReferenceId"));

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
        
        prodStdTestRefDao.update(prodStdTestRefTo, "PIDB_PROD_STD_TEST_REF", key);
        
        //send mail
        SendMailDispatch.sendMailByModify("MD-17", 
                prodStdTestRefTo.getProductLine() + "-" + prodStdTestRefTo.getProductFamily() + "-" +prodStdTestRefTo.getSubClassification(), 
                emails, 
                SendMailDispatch.getUrl("MD_17_EDIT", request.getContextPath(), prodStdTestRefTo));      
        
        String mes = "Save Successfully";
        
        request.setAttribute("error", mes);        
        request.setAttribute("ref", prodStdTestRefTo);
        return mapping.findForward(forward);
    }
}
