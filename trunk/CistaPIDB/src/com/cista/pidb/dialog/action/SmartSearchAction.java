package com.cista.pidb.dialog.action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.dialog.dao.SmartSearchDao;

public class SmartSearchAction extends DispatchAction {
    private final Log logger = LogFactory.getLog(getClass());
    public ActionForward search(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1);
        }
        String forward = "success";
        SmartSearch ss = new SmartSearch();
        if (request.getParameter("name") != null && request.getParameter("name").length() > 0) {
            ss = SmartSearchLoader.getSmartSearch(request.getParameter("name"));
            //request.setAttribute("name", request.getParameter("name"));
        } else {
            ss.setTable(request.getParameter("table"));
            //request.setAttribute("table", request.getParameter("table"));
            ss.setKeyColumn(request.getParameter("keyColumn"));
            //request.setAttribute("keyColumn", request.getParameter("keyColumn"));
            String columnsStr = request.getParameter("columns");
            if (columnsStr != null && columnsStr.length() > 0) {
                String[] columns = columnsStr.split(",");
                for (String column : columns) {
                    ss.addColumn(column.trim());
                }
            }
        }

        if (ss.getColumns() == null || ss.getColumns().size() == 0) {
            ss.addColumn(ss.getKeyColumn());
        }

        if (request.getParameter("title") != null && request.getParameter("title").length() > 0) {
            ss.setTitle(request.getParameter("title"));
            //request.setAttribute("title", request.getParameter("title"));
        }
        if (request.getParameter("mode") != null && request.getParameter("mode").length() > 0) {
            ss.setMode(Integer.parseInt(request.getParameter("mode")));
            //request.setAttribute("mode", request.getParameter("mode"));
        }

        if (request.getParameter("whereCause") != null && request.getParameter("whereCause").length() > 0) {
            ss.setWhereCause(request.getParameter("whereCause"));
            //request.setAttribute("whereCause", request.getParameter("whereCause"));
        }

        if (request.getParameter("orderBy") != null && request.getParameter("orderBy").length() > 0) {
            ss.setOrderBy(request.getParameter("orderBy"));
            //request.setAttribute("orderBy", request.getParameter("orderBy"));
        }

        request.setAttribute("inputField", request.getParameter("inputField"));
        request.setAttribute("inputFieldValue", request.getParameter("inputFieldValue"));
        request.setAttribute("callbackHandle", request.getParameter("callbackHandle"));

        try {
            request.setAttribute("result", new SmartSearchDao().find(ss, request.getParameter("inputFieldValue")));
        } catch (RuntimeException e) {
            logger.error("Smart search query exception.", e);
        }
        request.setAttribute("SmartSearch", ss);

        return mapping.findForward(forward);
    }
}
