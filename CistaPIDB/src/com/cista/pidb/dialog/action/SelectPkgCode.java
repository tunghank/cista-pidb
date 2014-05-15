package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.TradPkgDao;

public class SelectPkgCode extends DispatchAction {
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward list(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "success";
           String callback = request.getParameter("callback");
           IcFgDao icFgDao = new IcFgDao();
           request.setAttribute("selectList", icFgDao.findPkgCode());
           request.setAttribute("callback", callback);
           return mapping.findForward(forward);
       }
    
    public ActionForward lpc(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "list_success";
        String callback = request.getParameter("callback");
        String inputValue= request.getParameter("in");
        
        IcTapeDao icTapeDao = new IcTapeDao();
        List<String> pkgCodeListOne = new ArrayList<String>();
        TradPkgDao tradDao = new TradPkgDao();
        List<String> pkgCodeListTwo = new ArrayList<String>();
        CogDao cogDao = new CogDao();
        List<String> pkgCodeListThr = new ArrayList<String>();
        
        if (inputValue != null && inputValue.length() > 0) {
        	pkgCodeListOne = icTapeDao.findPkgCodeByPkgCode(inputValue);
        	pkgCodeListTwo = tradDao.findPkgCodeByPkgCode(inputValue);
        	pkgCodeListThr = cogDao.findAllPkgCodeByPkgCode(inputValue);
        } else {
        	pkgCodeListOne = icTapeDao.findAllPkgCode();
        	pkgCodeListTwo = tradDao.findPkgCode();
        	pkgCodeListThr = cogDao.findAllPkgCode();
        }
        
        
        List<String> allPkgCode = new ArrayList<String>();
        for (String temp : pkgCodeListOne) {
        	if (pkgCodeListTwo.contains(temp)) {
        		pkgCodeListTwo.remove(temp);
        	}
        	allPkgCode.add(temp);
        }
        for (String tempTwo : pkgCodeListTwo) {
        	allPkgCode.add(tempTwo);
        }
        for (String temp: pkgCodeListThr) {
        	if (!allPkgCode.contains(temp)) {
        		allPkgCode.add(temp);
        	}
        }    
        request.setAttribute("inputValue", inputValue);
        request.setAttribute("selectList", allPkgCode);
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}
