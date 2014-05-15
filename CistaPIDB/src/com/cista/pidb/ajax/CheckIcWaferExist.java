package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;

public class CheckIcWaferExist extends Action {
    @Override
    public ActionForward execute(final ActionMapping arg0, final ActionForm arg1,
            final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception {

        String projCode = arg2.getParameter("projCode");
        String bobyVer = arg2.getParameter("bodyVer");
        String optionVer = arg2.getParameter("optionVer");
        PrintWriter out = arg3.getWriter();
        ProjectDao projDao = new ProjectDao();
        ProjectCodeDao projectCodeDao = new ProjectCodeDao();

        ProjectCodeTo projCodeTo = projectCodeDao.findByProjectCode(projCode);
        String projName = projCodeTo.getProjName();
        String optionFor = projCodeTo.getProjOption();
        ProjectTo projectTo = projDao.find(projName); 
        if (projectTo == null) {
           out.print("project_null");
           return null;
        }
        if (projectTo.getFab() == null) {
        	out.print("fab_null");
        	return null;
        }
        
        if (optionFor == null) {
        	optionFor = "00";
        }
        if (optionFor.equals("00")) {
        	optionVer = "0";
        } else if (StringUtils.isEmpty(optionVer)) {
            out.print("optionVer_null");
            return null;            
        }
        
        //project_code_w_version
        String projectCodeWVersion;
        if (projCode.length() == 6) {
            projectCodeWVersion = projCode + bobyVer;
        } else {
            projectCodeWVersion = projName + bobyVer + "-" + optionFor + optionVer;
        }
        if (projectCodeWVersion.length() > 50) {
            out.print("wversion_outmax");
            return null;
        }

        //variant
        String variant = "";
    	IcWaferDao icWaferDao = new IcWaferDao();
    	String maxVar = icWaferDao.findMaxVar(projectCodeWVersion, projectTo.getFab());
    	if (maxVar != null) {
            String preVar = maxVar;
            if (preVar != null && !preVar.equals("")) {
                char c = (char) ((int) preVar.charAt(0) + 1); 
                if (c > 57 && c < 65) {
                	variant = new String(new char[]{65});
                } else if (c > 90) {
                	variant = new String(new char[]{48});
                } else {
                	variant = new String(new char[]{c});
                }
            }
        } else {
        	variant = "0";
        }    	
    	
        //for materialNum.
        int len = projName.length();
        String projNameFor = projName;
        if (len > 4) {
            projNameFor = projName.substring(len - 4, len);
        }
        String materialNum = "W" + projNameFor + bobyVer + optionFor;
        materialNum += optionVer + projectTo.getFab();
        materialNum += "00" + variant;
        if (materialNum.length() > 50) {
            out.print("matnum_outmax");
            return null;
        }

        IcWaferTo exist = new IcWaferDao().findByPrimaryKey(materialNum);
        if (exist != null) {
            out.print("true");
            return null;
        }
        return null;
       
    }
}
