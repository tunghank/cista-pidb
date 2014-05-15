package com.cista.pidb.ajax;

/********************************************************************
 * Modify History:
 * Date Time  |  Modifier  |Description
 * 2007/09/09 |  Hank_Tang |加入可以讀取多個AD Function.   
 ********************************************************************/

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.LdapHelper;

public class FetchUserFromAD extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
    	response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        String userId = request.getParameter("userId");
        
        UserTo oUuser = LdapHelper.getOUUser(userId);
        //UserTo user = LdapHelper.getUser(userId); // Mark for Hank_Tang 2007/09/09
        PrintWriter out = response.getWriter();
        //out.print(AjaxHelper.bean2XmlString(user)); // Mark for Hank_Tang 2007/09/09
        out.print(AjaxHelper.bean2XmlString(oUuser));
        return super.execute(mapping, form, request, response);
    }
}
