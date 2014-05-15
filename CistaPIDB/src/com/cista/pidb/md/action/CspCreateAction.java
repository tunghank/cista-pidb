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
import com.cista.pidb.md.dao.CspDao;
import com.cista.pidb.md.to.CspTo;


public class CspCreateAction extends DispatchAction{
	 public ActionForward pre(final ActionMapping mapping,
	            final ActionForm form, final HttpServletRequest request,
	            final HttpServletResponse response) {
	            String forward = "pre_success";
	            if (request.getParameter("ref") != null) {
	                //Create with reference
	            String pkgName = request.getParameter("ref");
	            CspTo cspTo = new CspDao().findByPkgName(pkgName);
	            request.setAttribute("ref", cspTo);
	            }
	            
	    		String fundName = "CSP";
	    		String funFieldName = "VERSION";

	    		request.setAttribute("versionList", new FunctionParameterDao()
	    				.findValueList(fundName, funFieldName));
	    		
	            return mapping.findForward(forward);
	    }

	     public ActionForward save(final ActionMapping mapping,
	            final ActionForm form, final HttpServletRequest request,
	            final HttpServletResponse response) {
	             String forward = "save_success";
	             CspDao cspDao = new CspDao();
	             CspTo cspTo = (CspTo) HttpHelper.pickupForm(CspTo.class, request);

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
	             cspTo.setAssignTo(assigns);
	             cspTo.setAssignEmail(emails);
	             
	             //created by
	             UserTo loginUser = PIDBContext.getLoginUser(request);
	             cspTo.setCreatedBy(loginUser.getUserId());
	             cspDao.insertCsp(cspTo);
	             
	             //Get Create Data
	             cspTo = cspDao.findByPkgName(cspTo.getPkgName());
	             String mes = "Save Successfully";
	             
	             
	             request.setAttribute("error", mes);        
	             request.setAttribute("ref", cspTo);
	             return mapping.findForward("release_fail");
	     }

}
