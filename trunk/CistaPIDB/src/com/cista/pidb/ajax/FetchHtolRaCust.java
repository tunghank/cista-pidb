package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.core.Escape;
import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.dao.IcTapeDao;

public class FetchHtolRaCust extends Action {

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

        IcTapeDao icTapeDao = new IcTapeDao();
        CogDao cogDao = new CogDao();
        SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
        
        List<String> custList = new ArrayList<String>();
        List<String> extCustList = new ArrayList<String>();
        String custs = "";
        custList.addAll(icTapeDao.findCustByProdCode(prodCode));
        //custList.addAll(cogDao.findCustByProdCode(prodCode));

        for (String s : custList) {
            if (!extCustList.contains(s) && StringUtils.isNotEmpty(s)) {
                extCustList.add(s);
            }
        }

        for (String s : extCustList) {
            if (s != null && !s.equals("") && !s.equals("null")) {
                SapMasterCustomerTo sapMasterCustomerTo =sapMasterCustomerDao.findByVendorCode(s);
                if (sapMasterCustomerTo != null && StringUtils.isNotEmpty(sapMasterCustomerTo.getShortName())) {
                        custs += "/" + sapMasterCustomerTo.getShortName();
                }
            }
        }

        if (custs.length() > 0) {
            custs = custs.substring(1);
        }
        
        List<String> cogCust = cogDao.findCustByProdCode(prodCode);
        extCustList = new ArrayList<String>();
        if (cogCust != null && cogCust.size() > 0) {
            for (String s : cogCust) {
                if (!extCustList.contains(s) && StringUtils.isNotEmpty(s)) {
                    extCustList.add(s);
                }
            }            
        }
        
        for (String s : extCustList) {
            custs += "/" + s;
        }
        
        if (custs.endsWith("/")) {
            custs = custs.substring(0, custs.length());
        }
        
        PrintWriter out = response.getWriter();
        out.print(Escape.escape(custs));

        return null;
    }
}