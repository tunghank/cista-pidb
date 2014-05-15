package com.cista.pidb.md.erp;

import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.MpListDao;

public class ERPHelper {
    public static String getMaterialType(String md) {
        return PIDBContext.getMessage("ERP." + md);
    }

    /**
     * Get a message from resource.
     * @param materialType String
     * @param arg2 String
     * @return String
     */
    public static String getMaterialGroup(String materialType, String arg2) {
        return PIDBContext.getMessage("ERP." + materialType + "." + (arg2.trim()).replaceAll(" ", "_"));
    }
    
    public static String getErrorMessage(String errorCode) {
        return "Release fail: (" + errorCode + ") " + PIDBContext.getMessage(errorCode);
    }
    
    /**
     * Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
     * @param icFgMaterialNum
     * @return out
     */
    public static String judgeMP(String icFgMaterialNum){ 	
        MpListDao mpListDao = new MpListDao();
    	String out;
    	
    	out = mpListDao.findEOL(icFgMaterialNum);
    	/*if ( mpListDao.findEOL(icFgMaterialNum)!= null ){
    		out = "0"; //EOL
    	}else if ( mpListDao.findMP(icFgMaterialNum) != null ){
    		out = "1"; //MP
    	}else{
    		out = "2"; //NON-MP
    	}*/
    	
    	return out;
    }
    
    /**
     * Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
     * @param icFgMaterialNum
     * @return out
     */
    public static String judgeMPByProjWVersion(String projCodeWVersion){ 	
        MpListDao mpListDao = new MpListDao();
    	String out;
    	out = mpListDao.findEOLByProjWVersion(projCodeWVersion);
    	/*if ( mpListDao.findEOLByProjWVersion(projCodeWVersion) != null ){
    		if ( mpListDao.findEOLByProjWVersion2(projCodeWVersion) == null ){
    		//Can't find any active in MP_List to judge to EOL change rele in 2008/05/02
    			out = "0"; //EOL
    		}else{
    			if ( mpListDao.findMPByProjWVersion(projCodeWVersion) != null ){
    	    		out = "1"; //MP
    	    	}else{
    	    		out = "2"; //NON-MP
    	    	}
    		}
    	}else {
    		if ( mpListDao.findMPByProjWVersion(projCodeWVersion) != null ){
        		out = "1"; //MP
        	}else{
        		out = "2"; //NON-MP
        	}
    	}*/
    	
    	return out;
    }

    /**
     * Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
     * @param icFgMaterialNum
     * @return out
     */
    public static String judgeMPByTapeName(String TapeName){ 	
        MpListDao mpListDao = new MpListDao();
    	String out;
    	
    	out = mpListDao.findEOLByTapeName(TapeName);
    	/*if ( mpListDao.findEOLByTapeName(TapeName) != null ){
    		if ( mpListDao.findEOLByTapeName2(TapeName) == null ){
        		//Can't find any active in MP_List to judge to EOL change rele in 2008/05/02
        		out = "0"; //EOL
    		}else{
    			if ( mpListDao.findMPByTapeName(TapeName) != null ){
    	    		out = "1"; //MP
    	    	}else{
    	    		out = "2"; //NON-MP
    	    	}
    		}
    	}else{ 
	    	if ( mpListDao.findMPByTapeName(TapeName) != null ){
	    		out = "1"; //MP
	    	}else{
	    		out = "2"; //NON-MP
	    	}
    	}*/
    	
    	return out;
    }
}
