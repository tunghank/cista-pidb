package com.cista.pidb.admin.action;

import java.util.List;

import javax.servlet.http.*;  


import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.to.FunctionTo;

public class FunctionListAction extends DispatchAction implements HttpSessionListener  {
	private static int activeSessions = 0;  

    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        FunctionDao functionDao = new FunctionDao();
        
        List<FunctionTo> functions = functionDao.findAll();
        request.setAttribute("allFunctions", functions);
        return mapping.findForward(forward);
    }

    public ActionForward delete(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        String statusMsg = "";
        FunctionDao functionDao = new FunctionDao();
        int selectedFunction = Integer.parseInt(request.getParameter("selectedFunction"));
        FunctionTo deleteFunction = functionDao.find(selectedFunction);
        functionDao.delete(selectedFunction);
        statusMsg = "User '" + deleteFunction.getFuncName() + "' is deleted.";

        List<FunctionTo> functions = functionDao.findAll();
        request.setAttribute("allFunctions", functions);
        request.setAttribute("statusMsg", statusMsg);
        return mapping.findForward(forward);
    }
    /*
     *Add By 900it
     *User Online Count 
     */
    public void sessionCreated(HttpSessionEvent se) {  
    	activeSessions++;  
    	}  
    	public void sessionDestroyed(HttpSessionEvent se) {  
    	if(activeSessions > 0)  
    	activeSessions--;  
    	}  
    	public static int getActiveSessions() {  
    	return activeSessions;  
    	}  
    	
}