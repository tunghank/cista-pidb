package com.cista.pidb.dialog.action;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.ProdStdTestRefDao;

public class SelectCpProgram extends DispatchAction {
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
           String projCode = request.getParameter("projCode");
           CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
           List selectList = cpTestProgramDao.findByProjCode(projCode);
                     
           if (selectList == null || selectList.size() < 1) {
        	   
               ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
               selectList = prodStdTestRefDao.findByProjCode(projCode);
               forward = "successToStd";
           } else {
//               String cpList = "";
//               String cpMaterialNum = "";
//               String projCodeWVersion = "";
//               CpMaterialDao cpMaterialDao = new CpMaterialDao();
//               
//               for (Object ctt : selectList) {
//                   String cp = ((CpTestProgramTo) ctt).getCpTestProgName();
//                   cpList += "," + cp;
//                   
//                   if (projCodeWVersion == null || projCodeWVersion.equals("")) {
//                       projCodeWVersion = ((CpTestProgramTo) ctt).getProjCodeWVersion();
//                   }
//               }
//               
//               if (cpList != null && cpList.length() > 0) {
//                   cpList = cpList.substring(1);
//               }
//               
//               IcWaferDao icWaferDao = new IcWaferDao();
//               IcWaferTo icWaferTo = icWaferDao.findByProjCodeWVersion(projCodeWVersion);
//               
//               if (icWaferTo != null && icWaferTo.isRoutingCp()) {
//                   CpMaterialTo cpMaterialTo = cpMaterialDao.findByCpList(cpList);
//                   
//                   if (cpMaterialTo != null) {
//                       cpMaterialNum = cpMaterialTo.getCpMaterialNum();
//                   } else {
//    
//                       String icWaferMaterialNum = icWaferTo.getMaterialNum();
//                       cpMaterialTo = new CpMaterialTo();
//                       
//                       //Create a new variant
//                       String maxVar = (String) cpMaterialDao.findMaxVar(projCodeWVersion);
//                       if (maxVar != null) {
//                           String preVar = maxVar.toString();
//                           if (preVar != null && !preVar.equals("")) {
//                               char c = (char) ((int) preVar.charAt(0) + 1);
//                               String realVar = "";
//                               if (c > 57 && c < 65) {
//                                   realVar = new String(new char[]{65});
//                               } else if (c > 90) {
//                                   realVar = new String(new char[]{48});
//                               } else {
//                                   realVar = new String(new char[]{c});
//                               }
//                               cpMaterialTo.setCpVariant(realVar);
//                           }
//                       } else {
//                           cpMaterialTo.setCpVariant("0");
//                       }
//                       
//                       cpMaterialNum = "C" + icWaferMaterialNum.substring(1, icWaferMaterialNum.length() - 1) + cpMaterialTo.getCpVariant();
//                       
//                       cpMaterialTo.setCpMaterialNum(cpMaterialNum);
//                       cpMaterialTo.setProjectCodeWVersion(projCodeWVersion);
//                       cpMaterialTo.setCpTestProgramNameList(cpList);
//                       
//                       cpMaterialDao.insert(cpMaterialTo, "PIDB_CP_MATERIAL");
//                   }
//               }
//               
//               request.setAttribute("cpMaterialNum", cpMaterialNum);
           }
           
           
           request.setAttribute("selectList", selectList);
           request.setAttribute("callback", callback);
           return mapping.findForward(forward);
       }
}
