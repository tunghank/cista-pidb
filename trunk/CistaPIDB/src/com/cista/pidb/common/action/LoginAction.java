package com.cista.pidb.common.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.admin.action.MenuBuilder;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.PIDBContext;

public class LoginAction extends Action {
    /** Logger. */
    private Log logger = LogFactory.getLog(getClass());

    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        try {
            UserDao userDao = new UserDao();
            String userId = arg2.getParameter("userId");
            UserTo userTo = userDao.find(userId);
            PIDBContext.setLoginUser(arg2, userTo);
            List<String> authentication = new ArrayList<String>();
            List<String> uris = userDao.findAuthenByUser(userTo.getId());
            if (uris != null && uris.size() > 0) {
                for (String s : uris) {
                    if (s != null && s.length() > 0) {
                        String[] ss = s.split(";");
                        if (ss != null) {
                            for (String sss : ss) {
                                authentication.add(sss);
                            }
                        }
                    }
                }
            }
            PIDBContext.setUserAuth(arg2, authentication);

            MenuBuilder mb = new MenuBuilder();
            mb.setContextPath(arg2.getContextPath());
            PIDBContext.setUserMenu(arg2, mb.buildMenu(null, userTo.getId()));

            logger.info("User login: " + userTo.getUserId() + ".");
        } catch (RuntimeException e) {
            logger.debug("Login fail.", e);
            return arg0.findForward("fail");
        }

        
        if (PIDBContext.getUserSavedUrl(arg2) != null) {
            String targetUrl = PIDBContext.getUserSavedUrl(arg2);
            
            PIDBContext.removeUserSavedUrl(arg2);

            return new ActionForward(targetUrl, true);
        }

        return arg0.findForward("success");
    }

}
