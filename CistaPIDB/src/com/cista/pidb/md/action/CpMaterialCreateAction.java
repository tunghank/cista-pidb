package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CpMaterialMantainDao;
import com.cista.pidb.md.to.CpMaterialMatainTo;

public class CpMaterialCreateAction extends DispatchAction{
	public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "viewJsp";
        
        if (request.getParameter("ref") != null) {
            String[] keys = request.getParameter("ref").split(",");
            
            if (keys != null && keys.length == 2) {
                request.setAttribute("ref", new CpMaterialMantainDao().findCpMaterialNum(keys[0], keys[1]));
            }
        }
        return mapping.findForward(forward);
    }
	
	public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        CpMaterialMantainDao ctpDao = new CpMaterialMantainDao();
        final CpMaterialMatainTo ctpTo = (CpMaterialMatainTo) HttpHelper.pickupForm(CpMaterialMatainTo.class, request);
        final UserTo loginUser = PIDBContext.getLoginUser(request);
        ctpTo.setCreatedBy(loginUser.getUserId());
  
        
        ctpDao.insertCpMaterial(ctpTo);
        
        String mes = "";

		mes = "Save Successfully";
		
		request.setAttribute("error", mes);
        request.setAttribute("ref", ctpTo);
        
        return mapping.findForward("viewEditJsp");
    }
	

}
