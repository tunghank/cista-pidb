package com.cista.pidb.md.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.to.ProductTo;

public class ProductEditAction extends DispatchAction {
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "edit";
        String prodCode = request.getParameter("prodCode");
        ProductDao productDao = new ProductDao();
        ProductTo productTo = productDao.findByProdCode(prodCode);
        
        request.setAttribute("productTo", productTo);
        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        ProductTo productTo = (ProductTo) HttpHelper.pickupForm(ProductTo.class, request);
        
        ProductDao productDao = new ProductDao();
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("PROD_CODE", productTo.getProdCode());
        productDao.update(productTo, "PIDB_PRODUCT", m);

        request.setAttribute("productList", productDao.findAll());
        return mapping.findForward(forward);
    }
}
