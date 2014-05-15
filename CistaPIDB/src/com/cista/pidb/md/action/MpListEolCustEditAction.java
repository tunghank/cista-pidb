package com.cista.pidb.md.action;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.MpListEolCustDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.to.MpListEolCustTo;


/**
 * .
 * 
 * @author Hu Meixia
 */
public class MpListEolCustEditAction extends DispatchAction {
    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(HttpHelper.class);


	/**
	 * .
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
	 */
	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "success";
		MpListEolCustDao eolCustDao = new MpListEolCustDao();
		MpListEolCustTo eolCustTo = (MpListEolCustTo) HttpHelper.pickupForm(MpListEolCustTo.class,
				request);
		String[] eolCustArray = request.getParameterValues("eolCust");
		String[] eolDateArray = request.getParameterValues("eolDate");
		String[] remarkArray = request.getParameterValues("remark");
		String[] flag = request.getParameterValues("flag");
		
		UserTo loginUser = PIDBContext.getLoginUser(request);
		eolCustTo.setModifiedBy(loginUser.getUserId());
	    SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
	    //Before Insert , Delete original record
	    eolCustDao.deleteByPrimaryKey(eolCustTo.getPartNum(), eolCustTo.getIcFgMaterialNum(), 
	    		eolCustTo.getProjCodeWVersion(), eolCustTo.getTapeName(), 
	    		eolCustTo.getPkgCode());
	    try{
			for(int i = 1; i <= eolCustArray.length - 1 ; i ++ ){
				eolCustTo.setEolCust(eolCustArray[i]);
				//System.out.println(eolCustArray[i]);
				eolCustTo.setEolDate(SDF.parse(eolDateArray[i]));
				eolCustTo.setRemark(remarkArray[i]);
				eolCustTo.setFlag(flag[i]);
			    //Before Insert , Delete original record
			    eolCustDao.deleteEOLCust(eolCustTo.getPartNum(), eolCustTo.getIcFgMaterialNum(), 
			    		eolCustTo.getProjCodeWVersion(), eolCustTo.getTapeName(), 
			    		eolCustTo.getPkgCode(),eolCustArray[i]);
				eolCustDao.insert(eolCustTo, "PIDB_MP_LIST_EOL");
				//Change Log
				PidbChangeLogDao chlDao = new PidbChangeLogDao();
				chlDao.insertChange("MP_LIST_EOL", "INSET", AjaxHelper.bean2String(eolCustTo), loginUser.getUserId());
			}
			//System.out.println()"";
			PrintWriter out = response.getWriter();	
			out.print("true");

	    }catch (ParseException e) {
            LOGGER.error(e);
            e.printStackTrace();
        }catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
        }

        return null;

	}
}
