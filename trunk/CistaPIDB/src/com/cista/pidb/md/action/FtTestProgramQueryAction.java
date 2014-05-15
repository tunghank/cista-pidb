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
import com.cista.pidb.md.dao.FtTestProgramDao;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.to.FtTestProgramQueryTo;
import com.cista.pidb.md.to.FtTestProgramTo;
import com.cista.pidb.md.to.ProductTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class FtTestProgramQueryAction extends DispatchAction {
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
        FtTestProgramDao ftpDao = new FtTestProgramDao();
        request.setAttribute("ftTestProgRevisionList", ftpDao.findFtTestProgRevision());
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
          FtTestProgramDao ftpDao = new FtTestProgramDao();
          FtTestProgramQueryTo queryTo = (FtTestProgramQueryTo) HttpHelper.pickupForm(FtTestProgramQueryTo.class,
                  request, true);
          
          //added by fumingjie
          List<FtTestProgramQueryTo> list = ftpDao.query(queryTo);
          List<String> prodNameList = new ArrayList<String>();
          ProductDao prodDao = new ProductDao();
          for (FtTestProgramQueryTo to : list) {
        	  String prodCode = to.getProdCode();
              ProductTo pct = prodDao.findByProdCode(prodCode);
              if (pct != null) {
                  prodNameList.add(pct.getProdName());
              }
          }
          
          queryTo.setTotalResult(ftpDao.countResult(queryTo));
          request.setAttribute("result", list);
          request.setAttribute("queryTo", queryTo);
          request.setAttribute("prodNameList", prodNameList);
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
        FtTestProgramDao ftpDao = new FtTestProgramDao();
        FtTestProgramQueryTo queryTo = (FtTestProgramQueryTo) HttpHelper.pickupForm(FtTestProgramQueryTo.class,
                request, true);
        List<FtTestProgramQueryTo> list = ftpDao.query(queryTo);
        List<String> prodNameList = new ArrayList<String>();
        ProductDao prodDao = new ProductDao();
        for (FtTestProgramQueryTo to : list) {
      	  String prodCode = to.getProdCode();
            ProductTo pct = prodDao.findByProdCode(prodCode);
            if (pct != null) {
                prodNameList.add(pct.getProdName());
            }
        }
        request.setAttribute("result", list);
        request.setAttribute("queryTo", queryTo);
        request.setAttribute("prodNameList", prodNameList);
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
        FtTestProgramDao ftpDao = new FtTestProgramDao();
        FtTestProgramQueryTo queryTo = (FtTestProgramQueryTo) HttpHelper.pickupForm(FtTestProgramQueryTo.class,
                request, true);
        queryTo.setPageNo(-1);
        List<FtTestProgramTo> result = ftpDao.queryTo(queryTo);
        request.setAttribute("reportTitle", PIDBContext.getConfig("MD_14_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_14_SEQ"));
        return mapping.findForward("report");
    }
}
