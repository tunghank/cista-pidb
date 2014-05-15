package com.cista.pidb.md.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.MaskLayerMappingDao;
import com.cista.pidb.md.to.MaskLayerMappingTo;

public class MaskLayerMappingEditAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "edit";
        
        String maskNum = request.getParameter("maskNum");
        MaskLayerMappingTo to = new MaskLayerMappingDao()
                .findByMaskNum(maskNum);
        if (to != null) {
            request.setAttribute("ref", to);
        }
        

        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";

        MaskLayerMappingDao maskLayerMappingDao = new MaskLayerMappingDao();
        MaskLayerMappingTo maskLayerMappingTo = (MaskLayerMappingTo) HttpHelper.pickupForm(MaskLayerMappingTo.class, request);
        
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("MASK_NUM", maskLayerMappingTo.getMaskNum());
        maskLayerMappingDao.update(maskLayerMappingTo, "PIDB_MASK_LAYER_MAPPING", m);

        String mes = "Save Successfully";
        
        request.setAttribute("error", mes);        
        request.setAttribute("ref", maskLayerMappingTo);
        return mapping.findForward(forward);
    }
}
