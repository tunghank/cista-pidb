package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ZsFlashramDao;
import com.cista.pidb.md.to.ZsFlashramTo;

public class CheckZsFlashramExist extends Action{
	public ActionForward execute(ActionMapping arg0, ActionForm arg1,
			HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
		// arg3.setContentType("text/plain; charset=UTF-8");
		ZsFlashramDao flashDao = new ZsFlashramDao();
		String materialNum = arg2.getParameter("materialNum");
		String description = arg2.getParameter("description");
		ZsFlashramTo flashTo = flashDao.findData(materialNum, description);
		PrintWriter out = arg3.getWriter();
		if (flashTo != null) {
			out.print("true");
			return null;
		}else {
			out.print("false");
			return null;
		}
	}

}
