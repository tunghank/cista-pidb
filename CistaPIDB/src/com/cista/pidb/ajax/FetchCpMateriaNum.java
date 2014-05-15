package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.CpTestProgramTo;
import com.cista.pidb.md.to.IcWaferTo;

public class FetchCpMateriaNum extends Action {
    /**
     * Do action performance.
     * 
     * @param mapping
     *            ActionMapping
     * @param form
     *            ActionForm
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return ActionForward
     * @throws Exception
     *             exception
     */
    public ActionForward execute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {

        String cpTestProgramNameList = request.getParameter("cpTestProgramNameList");
        
        String projCodeWVersion = request.getParameter("projCodeWVersion");
        CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
        
        
        if (projCodeWVersion == null || projCodeWVersion.equals("")) {
            if (cpTestProgramNameList != null && cpTestProgramNameList.length() > 0) {
                String[] cpTestProgramName = cpTestProgramNameList.split(",");
                for (String s : cpTestProgramName) {
                    CpTestProgramTo cpTo = cpTestProgramDao.findByCpTestProgName(s);
                    if (cpTo !=null) {
                        projCodeWVersion = cpTo.getProjCodeWVersion();
                        break;
                    }
                }
            }
        }
        
        String cpMaterialNum = "";
        CpMaterialDao cpMaterialDao = new CpMaterialDao();



        IcWaferDao icWaferDao = new IcWaferDao();
        IcWaferTo icWaferTo = icWaferDao
                .findByProjCodeWVersion(projCodeWVersion);

        if (icWaferTo != null && icWaferTo.isRoutingCp()) {
            CpMaterialTo cpMaterialTo = cpMaterialDao.findByCpList(cpTestProgramNameList, projCodeWVersion);

            if (cpMaterialTo != null) {
                cpMaterialNum = cpMaterialTo.getCpMaterialNum();
            } else {

                String icWaferMaterialNum = icWaferTo.getMaterialNum();
                cpMaterialTo = new CpMaterialTo();

                // Create a new variant
                String maxVar = (String) cpMaterialDao
                        .findMaxVar(projCodeWVersion);
                if (maxVar != null) {
                    String preVar = maxVar.toString();
                    if (preVar != null && !preVar.equals("")) {
                        char c = (char) ((int) preVar.charAt(0) + 1);
                        String realVar = "";
                        if (c > 57 && c < 65) {
                            realVar = new String(new char[] { 65 });
                        } else if (c > 90) {
                            realVar = new String(new char[] { 48 });
                        } else {
                            realVar = new String(new char[] { c });
                        }
                        cpMaterialTo.setCpVariant(realVar);
                    }
                } else {
                    cpMaterialTo.setCpVariant("0");
                }

                cpMaterialNum = "C"
                        + icWaferMaterialNum.substring(1, icWaferMaterialNum
                                .length() - 1) + cpMaterialTo.getCpVariant();

//                cpMaterialTo.setCpMaterialNum(cpMaterialNum);
//                cpMaterialTo.setProjectCodeWVersion(projCodeWVersion);
//                cpMaterialTo.setCpTestProgramNameList(cpTestProgramNameList);
//
//                cpMaterialDao.insert(cpMaterialTo, "PIDB_CP_MATERIAL");
            }
        }
        
        PrintWriter out = response.getWriter();
        out.print(cpMaterialNum);


        return null;
    }
}