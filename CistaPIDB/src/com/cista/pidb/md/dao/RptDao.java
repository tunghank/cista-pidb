package com.cista.pidb.md.dao;

/*------- Header---------------------------------
* 2010.01.11/FCG1 @Jere Huang - Initial Version.
*
*/

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.RptLinkQueryTo;
import com.cista.pidb.md.to.RptLinkTo;



public class RptDao extends PIDBDaoSupport
{
	public List<RptLinkTo> query(RptLinkQueryTo queryTo) 
	{
		String sql="";
		StringBuffer sbSql;
		
		sbSql = new StringBuffer();
		sbSql.append("  SELECT distinct icFG.part_num, icFG.proj_code, pjCode.proj_name, pjCode.FUNC_REMARK, icFG.MP_STATUS, "); 
		sbSql.append("       (select fab_descr from T_FAB_CODE  where fab=pj.fab) as fab, "); 
		sbSql.append("       icWafer.fab_device_id, mp.proj_code_w_version, pj.VOLTAGE, pj.poly_metal_layers, pj.PROC_TECH,pj.PROC_LAYER_NO, "); 
		sbSql.append("       pj.MASK_LAYERS_NO,pj.PITCH, pj.WAFER_INCH, pj.GROSS_DIE, pj.WAFER_THICKNESS, pj.X_SIZE, pj.Y_SIZE,pj.SCRIBE_LINE, "); 
		sbSql.append("       pj.SEALRING,  mask.BUMP_HOUSE1, mask.BUMP_HOUSE2, mask.BUMP_HOUSE3,mask.BUMP_HOUSE4, mask.BUMP_HOUSE5, "); 
		sbSql.append("       mask.bump_height, mask.BUMP_HARDNESS, icTape.TAPE_NAME, icTape.TAPE_CUST_PROJ_NAME, "); 
		sbSql.append("       icTape.tape_vendor,icTape.ASSY_SITE, icTape.TAPE_WIDTH, icTape.SPROCKET_HOLE_NUM, icTape.MIN_PITCH, "); 
		sbSql.append("       (select short_name from WM_SAP_MASTER_CUSTOMER where CUSTOMER_CODE=icTape.TAPE_CUST) as Tape_cust,  "); 
		sbSql.append("       cog.TRAY_DRAWING_NO1, cog.TRAY_DRAWING_NO_VER1, cog.COLOR1, cog.TRAY_DRAWING_NO2, "); 
		sbSql.append("       cog.TRAY_DRAWING_NO_VER2, cog.COLOR2, cog.TRAY_DRAWING_NO3, cog.TRAY_DRAWING_NO_VER3, cog.COLOR3, "); 
		sbSql.append("       cog.TRAY_DRAWING_NO4, cog.TRAY_DRAWING_NO_VER4, cog.COLOR4,  "); 
		sbSql.append("       pkg.TRAD_PKG_TYPE, pkg.PIN_COUNT, pkg.LEAD_FRAME_TYPE, pkg.body_size, pkg.mcp_die_qty, pkg.mcp_pkg,  "); 
		sbSql.append("       pkg.subtract_layer, pkg.assy_house1, pkg.assy_house2, pkg.assy_house3, pkg.assy_house4, "); 
		sbSql.append("       cpTest.CP_TEST_PROG_NAME, cpTest.MULTIPLE_STAGE, "); 
		sbSql.append("       cpTest.CP_TEST_PROG_REVISION, cpTest.CP_TEST_PROG_RELEASE_DATE, cpTest.CP_CPU_TIME, cpTest.CP_INDEX_TIME, "); 
		sbSql.append("       cpTest.CONTACT_DIE_QTY as CP_CONTACT_DIE_QTY, cpTest.TESTER as CP_TESTER, cpTest.TESTER_CONFIG as CP_TESTER_CONFIG, "); 
		sbSql.append("       (SELECT short_name FROM wm_sap_master_vendor  WHERE vendor_code=cpTest.FIRST_CP_TEST_HOUSE) as FIRST_CP_TEST_HOUSE,  "); 
		sbSql.append("       ftTest.FT_TEST_PROG_NAME, ftTest.FT_TEST_PROG_REVISION, ftTest.FT_TEST_PROG_RELEASE_DATE, "); 
		sbSql.append("       ftTest.FT_CPU_TIME, ftTest.FT_INDEX_TIME, ftTest.CONTACT_DIE_QTY as FT_CONTACT_DIE_QTY, ftTest.TESTER as FT_TESTER, ftTest.TESTER_CONFIG as FT_TESTER_CONFIG, "); 
		sbSql.append("       icFG.pkg_code,  "); 
		sbSql.append("       (SELECT DESCRIPTION from WM_SAP_MASTER_PACKAGE_TYPE where package_type=icFG.pkg_type) AS package_type, "); 
		sbSql.append("       icFG.material_num as IC_FG_MATERIAL_NUM, ");  
		sbSql.append("       mp.MP_RELEASE_DATE, mp.APPROVE_CUST, ");  
		sbSql.append("       mp.APPROVE_TAPE_VENDOR, mp.APPROVE_BP_VENDOR, mp.APPROVE_CP_HOUSE, ");  
		sbSql.append("       mp.APPROVE_ASSY_HOUSE,mp.ASSIGN_TO, mp.ASSIGN_EMAIL, ");  
		sbSql.append("       mp.CREATED_BY,mp.MODIFIED_BY, mp.MP_TRAY_DRAWING_NO1, ");  
		sbSql.append("       mp.MP_TRAY_DRAWING_NO_VER1, mp.MP_COLOR1, mp.MP_CUSTOMER_NAME1, ");  
		sbSql.append("       mp.MP_TRAY_DRAWING_NO2, mp.MP_TRAY_DRAWING_NO_VER2, ");  
		sbSql.append("       mp.MP_COLOR2,mp.MP_CUSTOMER_NAME2, ");  
		sbSql.append("       mp.MP_TRAY_DRAWING_NO3, mp.MP_TRAY_DRAWING_NO_VER3, ");  
		sbSql.append("       mp.MP_COLOR3, mp.MP_CUSTOMER_NAME3, ");  
		sbSql.append("       mp.MP_TRAY_DRAWING_NO4, mp.MP_TRAY_DRAWING_NO_VER4, ");  
		sbSql.append("       mp.MP_COLOR4, mp.MP_CUSTOMER_NAME4, ");  
		sbSql.append("       mp.PROCESS_FLOW, mp.MAT_TAPE, mp.MAT_BP, ");  
		sbSql.append("       mp.MAT_CP, mp.MAT_AS, mp.MAT_WF, mp.REMARK, ");  
		sbSql.append("       mp.APPROVE_FT_HOUSE, mp.REVISION_ITEM, ");  
		sbSql.append("       mp.UPDATE_TIME, mp.CDT, ");  
		sbSql.append("       mp.APPROVE_POLISH_VENDOR, mp.MAT_POLISH,  ");  
		sbSql.append("       mp.APPROVE_COLOR_FILTER_VENDOR, mp.MAT_CF, ");  
		sbSql.append("       mp.APPROVE_WAFER_CF_VENDOR, mp.MAT_WAFERCF,  ");  
		sbSql.append("       mp.MAT_CSP, mp.APPROVE_CP_CSP_VENDOR,  ");  
		sbSql.append("       pjCode.RELEASE_TO, mp.CP_BIN, ");    
		sbSql.append("       (SELECT icFg.MCP_PROD1 FROM PIDB_IC_FG icFg Where mp.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD1,  ");  
		sbSql.append("       (SELECT icFg.MCP_PROD2 FROM PIDB_IC_FG icFg Where mp.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD2,  ");  
		sbSql.append("       (SELECT icFg.MCP_PROD3 FROM PIDB_IC_FG icFg Where mp.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD3,  ");  
		sbSql.append("       (SELECT icFg.MCP_PROD4 FROM PIDB_IC_FG icFg Where mp.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD4,  ");  
		sbSql.append("       (SELECT tradPkg.LF_TOOL FROM PIDB_TRADITIONAL_PKG tradPkg WHERE tradPkg.PKG_NAME =mp.PART_NUM) LF_TOOL,  ");  
		sbSql.append("       (SELECT tradPkg.CLOSE_LF_NAME FROM PIDB_TRADITIONAL_PKG tradPkg WHERE tradPkg.PKG_NAME =mp.PART_NUM) CLOSE_LF_NAME  "); 		
		sbSql.append("FROM "); 
		sbSql.append("  PIDB_PROJECT_CODE pjCode, PIDB_MP_LIST mp, PIDB_PROJECT pj, PIDB_IC_WAFER icWafer, PIDB_BUMPING_MASK mask "); 
		sbSql.append("  ,(select * from PIDB_IC_TAPE where tape_variant='0') icTape, PIDB_COG cog, PIDB_CP_TEST_PROGRAM cpTest, PIDB_FT_TEST_PROGRAM ftTest "); 
		sbSql.append("  ,(select fg.*, (select proj_name from PIDB_PROJECT_CODE where proj_code=fg.proj_code ) as proj_name, 'W'|| substr(CP_MATERIAL_NUM,2,length(CP_MATERIAL_NUM)-1) as w_materila_num from PIDB_IC_FG fg)  icFG "); 
		sbSql.append("  ,PIDB_TRADITIONAL_PKG pkg "); 
		sbSql.append("WHERE  (mp.part_num(+)=icFG.part_num and mp.IC_FG_MATERIAL_NUM(+)=icFG.MATERIAL_NUM )  and pjCode.proj_code = icFG.proj_code "); 
		sbSql.append("  and pj.proj_name = pjCode.proj_name and  icFG.w_materila_num = icWafer.MATERIAL_NUM(+) "); 
		sbSql.append("  and mask.proj_code(+) = icFG.proj_code  and icTape.tape_name(+)=mp.tape_name   "); 
		sbSql.append("  and (cog.prod_code(+) =icFG.prod_code and cog.pkg_code(+) = icFG.pkg_code)   "); 
		sbSql.append("  and (cpTest.CP_MATERIAL_NUM(+)=icFG.CP_MATERIAL_NUM and cpTest.CP_TEST_PROG_NAME(+)=icFG.CP_TEST_PROG_NAME_LIST and cpTest.proj_code(+)=icFG.proj_code ) "); 
		sbSql.append("  and (ftTest.PART_NUM(+) =icFG.PART_NUM and ftTest.FT_TEST_PROG_NAME(+)= icFG.FT_TEST_PROG_LIST) "); 
		sbSql.append("  and (pkg.proj_name(+)=icFG.proj_name and pkg.PKG_CODE(+)=icFG.PKG_CODE ) "); 
		sbSql.append( generateWhereCause(queryTo) );
		
		sql = sbSql.toString();
		
		logger.debug(sql);
		
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		
		GenericRowMapper<RptLinkTo> rm = new GenericRowMapper<RptLinkTo>(RptLinkTo.class);
		
		List<RptLinkTo> result = sjt.query(sql, rm, new Object[] {});
		queryTo.setTotalResult(result.size());
		

		if (queryTo.getPageNo() > 0)
		{
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize();
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			if(cursorTo > queryTo.getTotalResult())
			{
				cursorTo=queryTo.getTotalResult();
			}
			List<RptLinkTo> subResutl = result.subList(cursorFrom, cursorTo);

			//return subResutl;
			return transferResult(subResutl);
		}
		else
		{
			//return result;
			return transferResult(result);
		}

	}
	
	private List<RptLinkTo> transferResult(List<RptLinkTo> result)
	{
		
		
		for(RptLinkTo obj:result)
		{
			String sTapeVendor= obj.getTapeVendor();
			String sApproveCust = obj.getApproveCust();
			String sApproveTapeVendor = obj.getApproveTapeVendor();
			String sApproveBPVendor = obj.getApproveBpVendor();
			String sApproveCpHose = obj.getApproveCpHouse();
			String sApproveAssyHouse = obj.getApproveAssyHouse();
			String sApproveFtHouse = obj.getApproveFtHouse();
			
			
			String sResutl = "";
	
			//change Tape name code to desc
			if(sTapeVendor != null)
			{
				sResutl = "";
				String[] list = sTapeVendor.split(",");
				SapMasterVendorDao vendorDao = new SapMasterVendorDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setTapeVendor(sResutl);				
			}
			//approve cust
			if(sApproveCust != null)
			{
				sResutl = "";
				String[] list = sApproveCust.split(",");
				SapMasterCustomerDao custDao = new SapMasterCustomerDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterCustomerTo to = custDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setApproveCust(sResutl);				
			}
			//approve Tape Vendor
			if(sApproveTapeVendor != null)
			{
				sResutl = "";
				String[] list = sApproveTapeVendor.split(",");
				SapMasterVendorDao vendorDao = new SapMasterVendorDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setApproveTapeVendor(sResutl);				
			}
			
			//approve BP Vendor
			if(sApproveBPVendor != null)
			{
				sResutl = "";
				String[] list = sApproveBPVendor.split(",");
				SapMasterVendorDao vendorDao = new SapMasterVendorDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setApproveBpVendor(sResutl);				
			}
			
			//approve Cp Hose
			if(sApproveCpHose != null)
			{
				sResutl = "";
				String[] list = sApproveCpHose.split(",");
				SapMasterVendorDao vendorDao = new SapMasterVendorDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setApproveCpHouse(sResutl);				
			}
			
			//approve Assy Hose
			if(sApproveAssyHouse != null)
			{
				sResutl = "";
				String[] list = sApproveAssyHouse.split(",");
				SapMasterVendorDao vendorDao = new SapMasterVendorDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setApproveAssyHouse(sResutl);				
			}
			
			//approve FT Hose
			if(sApproveFtHouse != null)
			{
				sResutl = "";
				String[] list = sApproveFtHouse.split(",");
				SapMasterVendorDao vendorDao = new SapMasterVendorDao();				
				for (String s : list) 
	    		{
	    			if (s != null && s.length() > 0) 	    			
	    			{
	    			    SapMasterVendorTo to = vendorDao.findByVendorCode(s);	    				
	    			    if (to != null) 
	    			    {
	    			    	sResutl+= to.getShortName() + ",";
	    			    }
	    			}
	    		}
	    		//�h���<ƪ�,
	    		if(sResutl.length()>0)
	    		{
	    			sResutl = sResutl.substring(0, sResutl.length()-1);
	    		}
				obj.setApproveFtHouse(sResutl);				
			}
			
		}
		return result;
	}
	
	private String generateWhereCause(RptLinkQueryTo queryTo)
	{
		StringBuffer sb = new StringBuffer();
		
		//part no
		if(!isParameterNull(queryTo.getPartNo()))
		{
			String sPartNum = getSmartSearchQueryString("icFG.part_num", queryTo.getPartNo());
			logger.debug("sPartNum=" + sPartNum);
			if (sPartNum != null) {
				sb.append(" and (" + sPartNum + " )");
			}
		}
		//project code
		if(!isParameterNull(queryTo.getProjCode()) )
		{
			String sProjectCode = getSmartSearchQueryAllLike("icFG.proj_code", queryTo.getProjCode());
			if (sProjectCode != null) {
				sb.append(" and (" + sProjectCode + " )");
			}
		}
		//fab
		if (!isParameterNull(queryTo.getFab()) ) 
		{
			sb.append(" and pj.fab = " + getSQLString(queryTo.getFab()) + " ");
		}
		
		//tape name
		if(!isParameterNull(queryTo.getTapeName()) )
		{
			String sTapeName = getSmartSearchQueryString("icTape.TAPE_NAME", queryTo.getTapeName());
			if (sTapeName != null) {
				sb.append(" and (" + sTapeName + " )");
			}
		}
		
		//cp tester
		if(!isParameterNull(queryTo.getCpTester() ) )
		{
			String sCpTester = getSmartSearchQueryString("cpTest.TESTER", queryTo.getCpTester());
			if (sCpTester != null) {
				sb.append(" and (" + sCpTester + " )");
			}
		}
		
		//ft tester
		if(!isParameterNull(queryTo.getFtTester()) )
		{
			String sFtTester = getSmartSearchQueryString("ftTest.TESTER", queryTo.getFtTester());
			if (sFtTester != null) {
				sb.append(" and (" + sFtTester + " )");
			}
		}
		logger.debug("WHERE condition=" + sb.toString());
		return sb.toString();
	
	}
	
	
	public  boolean isParameterNull(String inputParameter)
    {
        int zeroLength = 0;
        boolean isParameterNull = (inputParameter == null ||
                                   inputParameter.length() == zeroLength);
        return isParameterNull;
    }
}
