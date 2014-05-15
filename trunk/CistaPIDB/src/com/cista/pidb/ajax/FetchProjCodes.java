package com.cista.pidb.ajax;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ProjectCodeDao;

/**
 * Ajax Action for fetching project codes by project name.
 * @author fumingjie
 */
public class FetchProjCodes extends Action {

    /**
     * Do action perform.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception Exception
     * @return ActionForward instance.
     */
    public ActionForward execute(
            final ActionMapping mapping,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response)
            throws Exception {
        String projName = request.getParameter("projName");

        ProjectCodeDao projCodeDao = new ProjectCodeDao();
        List<String> projCodes = projCodeDao.findProjCode(projName);

        String result = "";
        for (String code : projCodes) {
            result += "," + code;
        }

        if (result.length() > 0) {
            result = result.substring(1);
        }

        response.getWriter().print(result);

        return null;
    }
}
