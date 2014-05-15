package com.cista.pidb.md.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.erp.CpTestProgramERP;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.CpTestProgramTo;
import com.cista.pidb.md.to.IcFgTo;
//Add for Hank 2008/02/13 Use to update IC_FG

/**
 * .
 * @author Hu Meixia
 *
 */
public class CpTestProgramCreateAction extends DispatchAction {
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
        String forward = "viewJsp";
        SapMasterVendorDao vendorDao = new SapMasterVendorDao();
        request.setAttribute("vendorList", vendorDao.findAll());
        
        if (request.getParameter("ref") != null) {
            String[] keys = request.getParameter("ref").split(",");
            
            if (keys != null && keys.length == 3) {
                request.setAttribute("ref", new CpTestProgramDao().findByPrimaryKey(keys[0], keys[1], keys[2]));
            }
        }
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
    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        CpTestProgramDao ctpDao = new CpTestProgramDao();
        final CpTestProgramTo ctpTo = (CpTestProgramTo) HttpHelper.pickupForm(CpTestProgramTo.class, request);
        final UserTo loginUser = PIDBContext.getLoginUser(request);
        ctpTo.setCreatedBy(loginUser.getUserId());
        
//      add assignTo and assignEmail
        String assigns = "";
        String[] assignTo = request.getParameterValues("assignTo");
        if (assignTo != null) {
            for (String a : assignTo) {
                assigns += "," + a;
            }
        }

        assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

        UserDao userDao = new UserDao();
        String emails = userDao.fetchEmail(assignTo);
        ctpTo.setCpTestProgName(request.getParameter("cpTestProgName").trim());
        ctpTo.setAssignTo(assigns);
        ctpTo.setAssignEmail(emails);
        
     // convert Customer
		SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
		String vendor = ctpTo.getVendorCode();
		String realVendor = "";
		if (vendor != null && vendor.length() > 0) {
			String custs = vendor.trim();
			if (custs != null) {				
					SapMasterVendorTo to = sapMasterVendorDao.findByShortName(custs);
					if (to != null) {
						realVendor =  to.getVendorCode();
					
				}
			}
		}
			ctpTo.setVendorCode(realVendor);
        
        ctpDao.insertCpTest(ctpTo);
        
        //******1.0  2008/02/13 add for Hank Update IC_FG*****//
		String cpMaterialNum = ctpTo.getCpMaterialNum();
		cpMaterialNum =null!= cpMaterialNum?cpMaterialNum:"";
        cpMaterialNum = cpMaterialNum.trim();
        
        System.out.println( "Update IC FG" );
        //1.1 Add Hank for PhaseII Release Rule
        IcFgDao updateIcFgDao = new IcFgDao();       
        //1.2 Add 2007/12/20 New Rule
        List<IcFgTo>  updateIcFgToList = updateIcFgDao.findByCpMaterialNum(cpMaterialNum);
    	//1.3 Update IC_FG CP_TEST_PROGRAM_LIST
        String cpTestProgName = ctpTo.getCpTestProgName();
        	cpTestProgName = cpTestProgName.trim();
        if (cpTestProgName == null || cpTestProgName.length() <= 0) {
        
        }else{
        	for (IcFgTo icFgTo : updateIcFgToList) {
        		icFgTo.setCpTestProgNameList(cpTestProgName);
        		Map<String, Object> key1 = new HashMap<String, Object>();
        		key1.put("CP_MATERIAL_NUM" , cpMaterialNum);
        		key1.put("MATERIAL_NUM" , icFgTo.getMaterialNum());
        		updateIcFgDao.update(icFgTo, "PIDB_IC_FG", key1);
        	}
        }  
                
        //send mail
        SendMailDispatch.sendMailByCreate("MD-8", 
                ctpTo.getCpTestProgName(), 
                emails, 
                SendMailDispatch.getUrl("MD_8_EDIT", request.getContextPath(), ctpTo));  
        
        String toErp = request.getParameter("toErp");        
        if("1".equals(toErp.trim())) {
        	
        	TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
        		String result = null;
                public void doInTransactionWithoutResult(final TransactionStatus status) {
                	 result = CpTestProgramERP.release(ctpTo,loginUser);
                	if (result != null) {
            			throw new ReleaseERPException(result); 
            		} 
                }
            };
            
            try {
    			new CpTestProgramDao().doInTransaction(callback);
    			
    		} catch (ReleaseERPException e) {
    			request.setAttribute("error", ERPHelper.getErrorMessage(e.getMessage()));
    			
    			SapMasterVendorDao vendorDao = new SapMasterVendorDao();
    	        request.setAttribute("vendorList", vendorDao.findAll());
    	        request.setAttribute("ref", ctpTo);
    			return mapping.findForward("viewEditJsp");
    		}
            
            //send mail
            SendMailDispatch.sendMailByErp("MD-8", ctpTo.getCpTestProgName(), emails, "");              
    	}
        
        String mes = "";
        if("1".equals(toErp.trim())) {
            mes = "Save and Release to ERP Successfully";
        } else {
            mes = "Save Successfully";
        }
        SapMasterVendorDao vendorDao = new SapMasterVendorDao();
        request.setAttribute("vendorList", vendorDao.findAll());        
        request.setAttribute("error", mes);        
        request.setAttribute("ref", ctpTo);
        
        return mapping.findForward("viewEditJsp");
    }
}
