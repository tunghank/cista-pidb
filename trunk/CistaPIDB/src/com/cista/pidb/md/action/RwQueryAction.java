package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.RwDao;
import com.cista.pidb.md.to.RwQueryTo;
import com.cista.pidb.md.to.RwTo;

public class RwQueryAction extends DispatchAction{
	/**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "query";
        RwDao rwDao = new RwDao();
        List<RwTo> rwToList = rwDao.findAll();
        List<String> selectList = new ArrayList<String>();
        if (rwToList != null && rwToList.size() > 0) {
            for (int i = 0; i < rwToList.size(); i++) {
            	RwTo onerwTo = rwToList.get(i);
                if (onerwTo.getTrayDrawingNoVer1() != null && !"".equals(onerwTo.getTrayDrawingNoVer1().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if ((onerwTo.getTrayDrawingNoVer1()).equals((String) selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(onerwTo.getTrayDrawingNoVer1());
                    }
                }

                if (onerwTo.getTrayDrawingNoVer2() != null && !"".equals(onerwTo.getTrayDrawingNoVer2().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (onerwTo.getTrayDrawingNoVer2().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(onerwTo.getTrayDrawingNoVer2());
                    }
                }

                if (onerwTo.getTrayDrawingNoVer3() != null && !"".equals(onerwTo.getTrayDrawingNoVer3().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (onerwTo.getTrayDrawingNoVer3().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(onerwTo.getTrayDrawingNoVer3());
                    }
                }
                
                if (onerwTo.getTrayDrawingNoVer4() != null && !"".equals(onerwTo.getTrayDrawingNoVer4().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (onerwTo.getTrayDrawingNoVer4().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(onerwTo.getTrayDrawingNoVer4());
                    }
                }
                
                if (onerwTo.getTrayDrawingNoVer5() != null && !"".equals(onerwTo.getTrayDrawingNoVer5().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (onerwTo.getTrayDrawingNoVer5().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(onerwTo.getTrayDrawingNoVer5());
                    }
                }
                if (onerwTo.getTrayDrawingNoVer6() != null && !"".equals(onerwTo.getTrayDrawingNoVer6().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (onerwTo.getTrayDrawingNoVer6().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(onerwTo.getTrayDrawingNoVer6());
                    }
                }
            }
        }
        request.setAttribute("selectList", selectList);
        return mapping.findForward(forward);
    }
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward query(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
          String forward = "result";
          RwDao rwDao = new RwDao();
          RwQueryTo queryTo = (RwQueryTo) HttpHelper.pickupForm(RwQueryTo.class, request, true);
          queryTo.setTotalResult(rwDao.countResult(queryTo));
          request.setAttribute("result", rwDao.query(queryTo));
          request.setAttribute("queryTo", queryTo);
          return mapping.findForward(forward);
    }
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward paging(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "result";
        RwDao rwDao = new RwDao();
        RwQueryTo queryTo = (RwQueryTo) HttpHelper.pickupForm(RwQueryTo.class, request, true);
        request.setAttribute("result", rwDao.query(queryTo));
        request.setAttribute("queryTo", queryTo);
        return mapping.findForward(forward);
    }
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward download(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
    	RwDao rwDao = new RwDao();
    	RwQueryTo queryTo = (RwQueryTo) HttpHelper.pickupForm(RwQueryTo.class, request, true);
        queryTo.setPageNo(-1);
        List<RwTo> result = rwDao.query(queryTo);
        request.setAttribute("reportTitle", PIDBContext.getConfig("MD_27_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_27_SEQ"));
        return mapping.findForward("report");
    }

}
