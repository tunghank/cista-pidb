package com.cista.pidb.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.PIDBContext;

public class LogoutAction extends Action {
    /** Logger. */
    private Log logger = LogFactory.getLog(getClass());
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        UserTo userTo = PIDBContext.getLoginUser(arg2);
        if (userTo != null) {
            logger.info("User logout: " + userTo.getUserId() + ".");
        }
        arg2.getSession().invalidate();
        return arg0.findForward("success");
    }

}
