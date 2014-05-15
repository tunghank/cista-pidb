package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.RwDao;
import com.cista.pidb.md.to.RwTo;

public class RwListAction extends DispatchAction{
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
        RwDao rwDao = new RwDao();
        List<RwTo> rwToList = rwDao.findAll();
        List<String> selectList = new ArrayList<String>();
        if (rwToList != null && rwToList.size() > 0) {
            for (int i = 0; i < rwToList.size(); i++) {
            	RwTo oneRwTo = rwToList.get(i);
                if (oneRwTo.getTrayDrawingNo1() != null && oneRwTo.getTrayDrawingNo1().length() > 0) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if ((oneRwTo.getTrayDrawingNo1()).equals((String) selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneRwTo.getTrayDrawingNo1());
                    }
                }

                if (oneRwTo.getTrayDrawingNo2() != null && oneRwTo.getTrayDrawingNo2().length() > 0) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneRwTo.getTrayDrawingNo2().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneRwTo.getTrayDrawingNo2());
                    }
                }

                if (oneRwTo.getTrayDrawingNo3() != null && oneRwTo.getTrayDrawingNo3().length() > 0) {
                    int sign = 0;
                    for (int j = 0; j < selectList.size(); j++) {
                        if (oneRwTo.getTrayDrawingNo3().equals(selectList.get(j))) {
                            sign = 1;
                        }
                    }
                    if (sign == 0) {
                        selectList.add(oneRwTo.getTrayDrawingNo3());
                    }
                }
            }
        }
        request.setAttribute("selectList", selectList);
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }

}
