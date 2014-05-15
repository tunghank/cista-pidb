package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.MaskLayerMappingDao;
import com.cista.pidb.md.to.MaskLayerMappingTo;

public class MaskLayerMappingCreateAction extends DispatchAction {

    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "create";

        if (request.getParameter("ref") != null) {
            // Create with reference
            String maskNum = request.getParameter("ref");
            request.setAttribute("ref", new MaskLayerMappingDao()
                    .findByMaskNum(maskNum));
        }

        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        MaskLayerMappingDao maskLayerMappingDao = new MaskLayerMappingDao();
        MaskLayerMappingTo maskLayerMappingTo = (MaskLayerMappingTo) HttpHelper.pickupForm(MaskLayerMappingTo.class, request);


        maskLayerMappingDao.insert(maskLayerMappingTo, "PIDB_MASK_LAYER_MAPPING");

        
        String mes = "Save Successfully";
        
        request.setAttribute("error", mes);        
        request.setAttribute("ref", maskLayerMappingTo);
        return mapping.findForward(forward);
    }
}
