package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CornerLotCharDao;
import com.cista.pidb.md.to.CornerLotCharTo;

public class CornerLotCharEditAction extends DispatchAction {

    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
            String forward = "pre_success";
            CornerLotCharDao cornerDao = new CornerLotCharDao();
            //
            String projCodeWVersion = (String) request.getParameter("wversion");
            CornerLotCharTo cornerTo = (CornerLotCharTo) cornerDao.findByProjCodeWVersion(projCodeWVersion);
            //
            if (cornerTo != null) {
                request.setAttribute("ref", cornerTo);
            }
            
            return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
         String forward = "save_success";
         CornerLotCharDao cornerDao = new CornerLotCharDao();
         CornerLotCharTo cornerTo = (CornerLotCharTo) HttpHelper.pickupForm(CornerLotCharTo.class, request, true);
         
         //assignEmail
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
         cornerTo.setAssignTo(assigns);
         cornerTo.setAssignEmail(emails);
         
         UserTo loginUser = PIDBContext.getLoginUser(request);
         cornerTo.setModifiedBy(loginUser.getUserId());
         cornerDao.updateCornerLotChar(cornerTo);
         
         //send mail
         SendMailDispatch.sendMailByModify("MD-6", 
                 cornerTo.getProjCodeWVersion(), 
                 emails, 
                 SendMailDispatch.getUrl("MD_6_EDIT", request.getContextPath(), cornerTo));  
         
         String mes = "Save Successfully";
         
         request.setAttribute("error", mes);        
         request.setAttribute("ref", cornerTo);
         return mapping.findForward(forward);
 }
}
