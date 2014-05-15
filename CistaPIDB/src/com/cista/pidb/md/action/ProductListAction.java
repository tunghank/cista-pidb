package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.to.ProductTo;

public class ProductListAction extends DispatchAction {
    public ActionForward list(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        String callback = request.getParameter("callback");
        ProductDao productDao = new ProductDao();
        request.setAttribute("productList", productDao.findAll());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }

    public ActionForward create(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        ProductDao productDao = new ProductDao();
        String callback = request.getParameter("callback");
        String prodCode = request.getParameter("prodCode");
        String remark = request.getParameter("remark");
        ProductTo productTo = new ProductTo();
        productTo.setProdCode(prodCode);
        productTo.setRemark(remark);
        productTo.setProdName(prodCode.substring(0, 8));
        
        if (prodCode.length() == 10) {
            productTo.setProdOption(prodCode.substring(8, 10));
        } else {
            productTo.setProdOption("00");
        }

        productDao.insert(productTo);

        request.setAttribute("productList", productDao.findAll());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
    
    public ActionForward search(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        String callback = request.getParameter("callback");
        String condition = request.getParameter("condition");
        ProductDao productDao = new ProductDao();
        request.setAttribute("productList", productDao.queryByProdCode(condition));
        request.setAttribute("callback", callback);
        request.setAttribute("condition", condition);
        return mapping.findForward(forward);
    }    
}
