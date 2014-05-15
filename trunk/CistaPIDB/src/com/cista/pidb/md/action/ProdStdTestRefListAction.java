package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CogDao;
import com.cista.pidb.md.to.CogTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class ProdStdTestRefListAction extends DispatchAction {
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
        CogDao cogDao = new CogDao();
        List<CogTo> cogToList = cogDao.findAll();
        List<String> selectList = new ArrayList<String>();
        if (cogToList != null && cogToList.size() > 0) {
            for (int i = 0; i < cogToList.size(); i++) {
                CogTo oneCogTo = cogToList.get(i);
                if (oneCogTo.getTrayDrawingNo1() != null && oneCogTo.getTrayDrawingNo1().length() > 0) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if ((oneCogTo.getTrayDrawingNo1()).equals((String) selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNo1());
                    }
                }

                if (oneCogTo.getTrayDrawingNo2() != null && oneCogTo.getTrayDrawingNo2().length() > 0) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNo2().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNo2());
                    }
                }

                if (oneCogTo.getTrayDrawingNo3() != null && oneCogTo.getTrayDrawingNo3().length() > 0) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneCogTo.getTrayDrawingNo3().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneCogTo.getTrayDrawingNo3());
                    }
                }
            }
        }
        request.setAttribute("selectList", selectList);
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}
