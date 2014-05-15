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
import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.to.CogQueryTo;
import com.cista.pidb.md.to.CogTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class CogQueryAction extends DispatchAction {
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
        CogDao cogDao = new CogDao();
        List<CogTo> cogToList = cogDao.findAll();
        List<String> selectList = new ArrayList<String>();
        if (cogToList != null && cogToList.size() > 0) {
            for (int i = 0; i < cogToList.size(); i++) {
                CogTo oneCogTo = cogToList.get(i);
                if (oneCogTo.getTrayDrawingNoVer1() != null && !"".equals(oneCogTo.getTrayDrawingNoVer1().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if ((oneCogTo.getTrayDrawingNoVer1()).equals((String) selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNoVer1());
                    }
                }

                if (oneCogTo.getTrayDrawingNoVer2() != null && !"".equals(oneCogTo.getTrayDrawingNoVer2().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNoVer2().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNoVer2());
                    }
                }

                if (oneCogTo.getTrayDrawingNoVer3() != null && !"".equals(oneCogTo.getTrayDrawingNoVer3().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNoVer3().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNoVer3());
                    }
                }
                
                if (oneCogTo.getTrayDrawingNoVer4() != null && !"".equals(oneCogTo.getTrayDrawingNoVer4().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNoVer4().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNoVer4());
                    }
                }
                
                if (oneCogTo.getTrayDrawingNoVer5() != null && !"".equals(oneCogTo.getTrayDrawingNoVer5().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNoVer5().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNoVer5());
                    }
                }
                if (oneCogTo.getTrayDrawingNoVer6() != null && !"".equals(oneCogTo.getTrayDrawingNoVer6().trim())) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNoVer6().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNoVer6());
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
          CogDao cogDao = new CogDao();
          CogQueryTo queryTo = (CogQueryTo) HttpHelper.pickupForm(CogQueryTo.class, request, true);
          queryTo.setTotalResult(cogDao.countResult(queryTo));
          request.setAttribute("result", cogDao.query(queryTo));
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
        CogDao cogDao = new CogDao();
        CogQueryTo queryTo = (CogQueryTo) HttpHelper.pickupForm(CogQueryTo.class, request, true);
        request.setAttribute("result", cogDao.query(queryTo));
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
        CogDao cogDao = new CogDao();
        CogQueryTo queryTo = (CogQueryTo) HttpHelper.pickupForm(CogQueryTo.class, request, true);
        queryTo.setPageNo(-1);
        List<CogTo> result = cogDao.query(queryTo);
        request.setAttribute("reportTitle", PIDBContext.getConfig("MD_11_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_11_SEQ"));
        return mapping.findForward("report");
    }
}
