package com.cista.pidb.dialog.action;


import org.apache.struts.actions.DispatchAction;


public class SelectCogPocketQua extends DispatchAction {
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
//    public ActionForward list(final ActionMapping mapping,
//            final ActionForm form, final HttpServletRequest request,
//            final HttpServletResponse response) {
//        String forward = "success";
////        String callback = request.getParameter("callback");
////        CogDao cogDao = new CogDao();
////        List<CogTo> cogToList = cogDao.findAll();
////        List<String> selectList = new ArrayList<String>();
////        if (cogToList != null && cogToList.size() > 0) {
////            for (int i = 0; i < cogToList.size(); i++) {
////                CogTo oneCogTo = cogToList.get(i);
////                if (oneCogTo.getPocketQty() != null && oneCogTo.getPocketQty().length() > 0) {
////                    int sign = 0;
////                    for (int j = 0; j < selectList.size(); j++) {
////                        if ((oneCogTo.getPocketQty()).equals((String) selectList.get(j))) {
////                            sign = 1;
////                        }
////                    }
////                    if (sign == 0) {
////                        selectList.add(oneCogTo.getPocketQty());
////                    }
////                }
////            }
////        }
////        request.setAttribute("selectList", selectList);
////        request.setAttribute("callback", callback);
////        return mapping.findForward(forward);
////    }
}
