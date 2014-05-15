package com.cista.pidb.dialog.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CogDao;

public class SelectTrayDrawingNo extends DispatchAction {
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
        String trayDrawingNo = request.getParameter("trayDrawingNo");
        CogDao cogDao = new CogDao();
        List<String> cogToList = cogDao.findTrayDrawingNo(trayDrawingNo);
//        List<String> selectList = new ArrayList<String>();
//        if (cogToList != null && cogToList.size() > 0) {
//            for (int i = 0; i < cogToList.size(); i++) {
//                CogTo oneCogTo = cogToList.get(i);
//                if (oneCogTo.getTrayDrawingNo1() != null && oneCogTo.getTrayDrawingNo1().length() > 0) {
//                    int sign = 0;
//                    for (int j = 0; j < selectList.size(); j++) {
//                        if ((oneCogTo.getTrayDrawingNo1()).equals((String) selectList.get(j))) {
//                            sign = 1;
//                        }
//                    }
//                    if (sign == 0) {
//                        selectList.add(oneCogTo.getTrayDrawingNo1());
//                    }
//                }
//
//                if (oneCogTo.getTrayDrawingNo2() != null && oneCogTo.getTrayDrawingNo2().length() > 0) {
//                    int sign = 0;
//                    for (int j = 0; j < selectList.size(); j++) {
//                        if (oneCogTo.getTrayDrawingNo2().equals(selectList.get(j))) {
//                            sign = 1;
//                        }
//                    }
//                    if (sign == 0) {
//                        selectList.add(oneCogTo.getTrayDrawingNo2());
//                    }
//                }
//
//                if (oneCogTo.getTrayDrawingNo3() != null && oneCogTo.getTrayDrawingNo3().length() > 0) {
//                    int sign = 0;
//                    for (int j = 0; j < selectList.size(); j++) {
//                        if (oneCogTo.getTrayDrawingNo3().equals(selectList.get(j))) {
//                            sign = 1;
//                        }
//                    }
//                    if (sign == 0) {
//                        selectList.add(oneCogTo.getTrayDrawingNo3());
//                    }
//                }
//                if (oneCogTo.getTrayDrawingNo4() != null && oneCogTo.getTrayDrawingNo4().length() > 0) {
//                    int sign = 0;
//                    for (int j = 0; j < selectList.size(); j++) {
//                        if (oneCogTo.getTrayDrawingNo4().equals(selectList.get(j))) {
//                            sign = 1;
//                        }
//                    }
//                    if (sign == 0) {
//                        selectList.add(oneCogTo.getTrayDrawingNo4());
//                    }
//                }
//                if (oneCogTo.getTrayDrawingNo5() != null && oneCogTo.getTrayDrawingNo5().length() > 0) {
//                    int sign = 0;
//                    for (int j = 0; j < selectList.size(); j++) {
//                        if (oneCogTo.getTrayDrawingNo5().equals(selectList.get(j))) {
//                            sign = 1;
//                        }
//                    }
//                    if (sign == 0) {
//                        selectList.add(oneCogTo.getTrayDrawingNo5());
//                    }
//                }
//                if (oneCogTo.getTrayDrawingNo6() != null && oneCogTo.getTrayDrawingNo6().length() > 0) {
//                    int sign = 0;
//                    for (int j = 0; j < selectList.size(); j++) {
//                        if (oneCogTo.getTrayDrawingNo6().equals(selectList.get(j))) {
//                            sign = 1;
//                        }
//                    }
//                    if (sign == 0) {
//                        selectList.add(oneCogTo.getTrayDrawingNo6());
//                    }
//                }
//            }
//        }
        
        request.setAttribute("selectList", cogToList);
        request.setAttribute("trayDrawingNo", trayDrawingNo);
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}
