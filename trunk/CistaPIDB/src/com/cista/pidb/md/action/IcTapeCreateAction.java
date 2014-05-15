package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcTapeERP;
import com.cista.pidb.md.erp.MpListAslERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.IcTapeTo;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectTo;

public class IcTapeCreateAction extends DispatchAction {

    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "create";
        
        if (request.getParameter("ref") != null) {
            //Create with reference
            String key = request.getParameter("ref");
            if (key.indexOf(",") >= 0) {
                String[] keys = key.split(",");
                if (keys != null && keys.length == 2) {
                    request.setAttribute("ref", new IcTapeDao().findByPrimaryKey(keys[0], keys[1]));
                }
            }
        }  
        
        String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
		.findValueList(fundName2, funFieldName2));

        return mapping.findForward(forward);
    }

    public ActionForward save(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        IcTapeDao icTapeDao = new IcTapeDao();
        IcTapeTo icTapeTo = (IcTapeTo) HttpHelper.pickupForm(IcTapeTo.class, request);
        UserTo loginUser = PIDBContext.getLoginUser(request);
        icTapeTo.setCreatedBy(loginUser.getUserId());

        //find ilPitch
        ProductDao productDao = new ProductDao();
        List<ProductTo> productList = productDao.findByProdName(icTapeTo.getProdName());
        String prodCodes = "";
        if (productList != null && productList.size() > 0) {
            for (ProductTo to : productList) {
                if (StringUtils.isNotEmpty(to.getProdCode())) {
                    prodCodes += "," + to.getProdCode();
                }
            }
        }
        if (prodCodes.length() > 0) {
            prodCodes = prodCodes.substring(1);
        }
        ProjectDao projectDao = new ProjectDao();
        List<ProjectTo> projectToList = projectDao.findByProdCodes(prodCodes);
        for (ProjectTo to : projectToList) {
            if (StringUtils.isNotEmpty(to.getPitch())) {
                icTapeTo.setIlPitch(to.getPitch());
                break;
            }
        }
        
        
        String prodName = icTapeTo.getProdName();
        String pkgCode = icTapeTo.getPkgCode();
        String pkgVersion = icTapeTo.getPkgVersion();
        
        //Create a new variant
        String maxVar = (String) icTapeDao.findMaxVar(prodName, pkgCode, pkgVersion);
        if (maxVar != null) {
            String preVar = maxVar.toString();
            if (preVar != null && !preVar.equals("")) {
                char c = (char) ((int) preVar.charAt(0) + 1);
                String realVar = "";
                if (c > 57 && c < 65) {
                    realVar = new String(new char[]{65});
                } else if (c > 90) {
                    realVar = new String(new char[]{48});
                } else {
                    realVar = new String(new char[]{c});
                }
                icTapeTo.setTapeVariant(realVar);
            }
        } else {
            icTapeTo.setTapeVariant("0");
        }
        
        if (prodName.length() >= 8) {
            prodName = prodName.substring(2, 6) + prodName.charAt(7);
        } else if (prodName.length() >= 6) {
            prodName = prodName.substring(2, 6) + "0";
        }
        
        String variant = icTapeTo.getTapeVariant();
        int pkgLen = pkgVersion.length();
        for (int i = 0; i < 2 - pkgLen; i++) {
            pkgVersion = pkgVersion + "0";
        }
        
        String materialNum = "P" + prodName + pkgCode + pkgVersion + variant;
        icTapeTo.setMaterialNum(materialNum);
        boolean isDraft = false;
        if (icTapeTo.getTapeType() == null
                || icTapeTo.getTapeType().equals("")) {
            isDraft = true;
        }
        if (icTapeTo.getTapeWidth() == null || icTapeTo.getTapeWidth().equals("")) {
            isDraft = true;
        }
        if (icTapeTo.getSprocketHoleNum() == null || icTapeTo.getSprocketHoleNum().equals("")) {
            isDraft = true;
        }
        if (icTapeTo.getMinPitch() == null || icTapeTo.getMinPitch().equals("")) {
            isDraft = true;
        }
        //Add Cu_Layer and Revision_Reason by 900it
        if (icTapeTo.getCuLayer() == null || icTapeTo.getCuLayer().equals("")) {
            isDraft = true;
        }
        if(icTapeTo.getRevisionReason() == null || icTapeTo.getRevisionReason().equals("")){
        	isDraft = true;
        }

        if (isDraft) {
            icTapeTo.setStatus("Draft");
        } else {
            icTapeTo.setStatus("Completed");
        }

        // add multiple select data
        String[] vendorList = request.getParameterValues("tapeVendor");
        String allVendor = "";
        if (vendorList != null && vendorList.length > 0) {
            for (String vendor : vendorList) {
                allVendor += "," + vendor;
            }
        }

        if (allVendor != null && allVendor.length() > 0) {
            icTapeTo.setTapeVendor(allVendor.substring(1));
        }

        String[] assyList = request.getParameterValues("assySite");
        String allAssy = "";
        if (assyList != null && assyList.length > 0) {
            for (String assy : assyList) {
                allAssy += "," + assy;
            }
        }

        if (allAssy != null && allAssy.length() > 0) {
            icTapeTo.setAssySite(allAssy.substring(1));
        }
        
        //add assignTo and assignEmail
        String assigns = "";
        String[] assignTo = request.getParameterValues("assignTo");
        if (assignTo != null) {
            for (String a : assignTo) {
                assigns += "," + a;
            }
        } else {
            assignTo = new String[]{};
        }

        assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

        UserDao userDao = new UserDao();
        List<String> emailList = new ArrayList<String>(Arrays.asList(assignTo));
        emailList.add("(R)default_tp_save");
        
        String emails = userDao.fetchEmail(emailList); 
        icTapeTo.setAssignTo(assigns);
        icTapeTo.setAssignEmail(emails);

        //convert Tape Customer
        if (StringUtils.isNotEmpty(icTapeTo.getTapeCust())) {
            SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
            SapMasterCustomerTo sapMasterCustomerTo = sapMasterCustomerDao.findByShortName(icTapeTo.getTapeCust());
            if (sapMasterCustomerTo != null) {
                icTapeTo.setTapeCust(sapMasterCustomerTo.getCustomerCode());
            }
        }
        
        
        icTapeDao.insert(icTapeTo);
		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("IC_TAPE", "INSET", AjaxHelper.bean2String(icTapeTo), loginUser.getUserId());
		
        //send mail
        SendMailDispatch.sendMailByCreate("MD-10", 
                icTapeTo.getTapeName() + "(" + icTapeTo.getMaterialNum() + ")", 
                emails, 
                SendMailDispatch.getUrl("MD_10_EDIT", request.getContextPath(), icTapeTo));  
        
        String toErp = request.getParameter("toErp");        
        if("1".equals(toErp.trim())) {
            final IcTapeTo erpTo = icTapeTo;
            final UserTo erpUser = loginUser;
            TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
                String result = null;
                public void doInTransactionWithoutResult(final TransactionStatus status) {
                     result = IcTapeERP.release(erpTo, erpUser);
                    if (result != null) {
                        throw new ReleaseERPException(result); 
                    }
                    //Release to ASL List
                    result = MpListAslERP.releaseFormICTape(erpTo, erpUser);
                    if (result != null) {
                        throw new ReleaseERPException(result); 
                    }
                }
            };
            
            try {
                icTapeDao.doInTransaction(callback);
                
            } catch (ReleaseERPException e) {
                request.setAttribute("error", ERPHelper.getErrorMessage(e.getMessage()));
                
                String targetForward = mapping.findForward("viewEditJsp").getPath();
                
                if (targetForward.indexOf('?') == -1) {
                    targetForward = targetForward + "?";
                } else {
                    targetForward = targetForward + "&";
                }
                
                ActionForward actionForward = new ActionForward(targetForward + "materialNum=" 
                        + materialNum + "&tapeName=" + icTapeTo.getTapeName());     
                
                return actionForward;
            }
            
            //update status Released
            icTapeTo.setStatus("Released");
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("MATERIAL_NUM", icTapeTo.getMaterialNum());
            m.put("TAPE_NAME", icTapeTo.getTapeName());
            icTapeDao.update(icTapeTo, "PIDB_IC_TAPE", m);        
            
            //send mail
            emailList.remove("(R)default_tp_save");
            emailList.add("(R)default_tp_erp");
            emails = userDao.fetchEmail(emailList);                
            SendMailDispatch.sendMailByErp("MD-10", icTapeTo.getTapeName() + " ( " + icTapeTo.getMaterialNum() + " )", emails, "");               
        }
        
        String mes = "";
        if ("1".equals(toErp.trim())) {
            mes = "Save and Release to ERP Successfully";
        } else {
            mes = "Save Successfully";
        }
        request.setAttribute("error", mes);
        
        String targetForward = mapping.findForward("viewEditJsp").getPath();
        
        if (targetForward.indexOf('?') == -1) {
            targetForward = targetForward + "?";
        } else {
            targetForward = targetForward + "&";
        }
        
        ActionForward actionForward = new ActionForward(targetForward + "materialNum=" 
                + materialNum + "&tapeName=" + icTapeTo.getTapeName());     
        
        return actionForward;        
          
    }
}
