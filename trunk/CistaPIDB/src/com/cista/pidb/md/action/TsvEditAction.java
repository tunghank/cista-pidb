package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;


import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.TsvDao;
import com.cista.pidb.md.to.TsvTo;

public class TsvEditAction extends DispatchAction{
	
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
            String forward = "pre_success";
            TsvDao tsvDao = new TsvDao();
            String pkgName = (String) request.getParameter("pkgName");
            if (pkgName == null || pkgName.length() <= 0) {
            	pkgName = (String)request.getAttribute("pkgName");
            }
            TsvTo tsvTo = (TsvTo) tsvDao.findByPkgName(pkgName);
            if (tsvTo != null) {
                request.setAttribute("tsvTo", tsvTo);
            }
            
    		String fundName = "TSV";
    		String funFieldName = "VERSION";

    		request.setAttribute("tsvVersionList", new FunctionParameterDao()
    				.findValueList(fundName, funFieldName));
    		
            return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
             String forward = "save_success";
             TsvDao tsvDao = new TsvDao();
             TsvTo tsvTo = (TsvTo) HttpHelper.pickupForm(TsvTo.class, request);
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
             tsvTo.setAssignTo(assigns);
             tsvTo.setAssignEmail(emails);
             
             //set modified by
             UserTo loginUser = PIDBContext.getLoginUser(request);
             tsvTo.setModifiedBy(loginUser.getUserId());
             tsvDao.updateTsv(tsvTo);
                          
             tsvTo = tsvDao.findByPkgName(tsvTo.getPkgName());
             String mes = "Save Successfully";

             
             request.setAttribute("error", mes);        
             request.setAttribute("ref", tsvTo);
             return mapping.findForward(forward);
    }

}
