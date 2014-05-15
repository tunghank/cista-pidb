package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.MaskLayerMappingDao;
import com.cista.pidb.md.to.MaskLayerMappingTo;

public class CheckMaskLayerMappingExist extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        MaskLayerMappingDao maskLayerMappingDao = new MaskLayerMappingDao();
        String maskNum = arg2.getParameter("maskNum");
        MaskLayerMappingTo to = maskLayerMappingDao.findByMaskNum(maskNum);
        PrintWriter out = arg3.getWriter();

        if (to == null) {
            out.print("false");
        } else {
            out.print("true");
        }

        return null;
    }
}
