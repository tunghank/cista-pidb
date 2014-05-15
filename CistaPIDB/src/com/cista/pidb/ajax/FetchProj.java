package com.cista.pidb.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.ProjectTo;

/**
 * Action for fetching a project record by project name.
 * @author fumingjie
 */
public class FetchProj extends Action {

    /**
     * Do action performance.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     * @throws Exception exception
     */
    public ActionForward execute(
            final ActionMapping mapping,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response)
        throws Exception {

        String projName = request.getParameter("projName");
        ProjectDao projDao = new ProjectDao();
        ProjectTo projTo = projDao.find(projName);
        String projXml = AjaxHelper.bean2XmlString(projTo);

        response.setContentType("text/xml");
        response.getWriter().print(projXml == null ? "" : projXml);

        return null;
    }
}
