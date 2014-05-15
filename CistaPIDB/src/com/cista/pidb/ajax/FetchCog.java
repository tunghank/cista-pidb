package com.cista.pidb.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.to.CogTo;
import com.cista.pidb.md.to.IcFgTo;

/**
 * Fetching cog.
 * @author fumingjie
 */
public class FetchCog extends Action {

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

        String prodCode = request.getParameter("prodCode");
        String pkgCode = request.getParameter("pkgCode");
        String icFgMaterialNum = request.getParameter("icFgMaterialNum");
        
        if (StringUtils.isNotEmpty(icFgMaterialNum)) {
            IcFgDao icFgDao = new IcFgDao();
            IcFgTo icFgTo = icFgDao.findByMaterialNum(icFgMaterialNum);
            
            if (icFgTo != null) {
                pkgCode = icFgTo.getPkgCode();
            }
        }
        
        CogDao cogDao = new CogDao();
        CogTo cogTo = cogDao.findBy(prodCode, pkgCode);
        
        String xml = AjaxHelper.bean2XmlString(cogTo);
        
        response.setContentType("text/xml;charset=UTF-8");
        response.getWriter().write(xml == null ? "" : xml);

        return null;
    }
}
