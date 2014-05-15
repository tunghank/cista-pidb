package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ProjectQueryTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.WmVendorTo;



public class RptProdFabDao extends PIDBDaoSupport
{
	public List<WmVendorTo> getWmFabVendorList() 
	{
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append(" SELECT short_name, vendor_code FROM WM_SAP_MASTER_VENDOR  WHERE vendor_code IN ");
		sbSql.append("   (SELECT  distinct vendor_code ");
		sbSql.append("   FROM T_FAB_CODE  WHERE vendor_code like '1%') ORDER BY VENDOR_CODE ");
		
		logger.debug(sbSql.toString());
		return stj.query(sbSql.toString(), new GenericRowMapper<WmVendorTo>(WmVendorTo.class),
				new Object[] {});
	}
	
	public List<ProjectTo> queryProject(ProjectQueryTo queryTo) 
	{
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		//新加條件, 當筆proj 要有 tape out date, 否則不顯示
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT p.proj_name, p.fab, p.pitch, p.pad_type, p.fs_date, "); 
		sbSql.append("       p.proc_tech, p.poly_metal_layers, p.voltage, p.mask_layers_no, p.proc_layer_no,  "); 
		sbSql.append("       p.wafer_type, p.wafer_inch, p.x_size, p.y_size, p.gross_die, "); 
		sbSql.append("       p.fcst_cp_yield, p.to_include_sealring, p.sealring, p.scribe_line, p.test_line, "); 
		sbSql.append("       p.wafer_thickness, p.proc_name, p.any_ip_usage, p.embedded_otp, "); 
		sbSql.append("       p.otp_size, p.proj_leader, p.design_engr, p.prod_engr, p.esd_engr,  "); 
		sbSql.append("       p.apr_engr, p.layout_engr, p.test_engr, p.assy_engr, p.app_engr, "); 
		sbSql.append("       p.pm, p.qa_engr, p.sales_engr, p.assign_to, p.assign_email, "); 
		sbSql.append("       p.status, p.prod_family, p.prod_line, p.panel_type, p.ic_type, "); 
		sbSql.append("       pc.prod_code prod_code_list, p.created_by, p.modified_by, p.estimated, p.nick_name, "); 
		sbSql.append("       p.second_foundry_project, pc.project_type, pc.proj_code, pc.proj_option, pc.func_remark,  "); 
		sbSql.append("       pc.cust, pc.remark, pc.kick_off_date, pc.release_to  "); 
		sbSql.append("FROM PIDB_PROJECT p LEFT JOIN PIDB_PROJECT_CODE pc ON p.proj_name=pc.proj_name where 1 = 1  "); 
		sbSql.append(" and exists (select 1 from pidb_ic_wafer where proj_code =pc.proj_code and TAPE_OUT_DATE is not null) ");
		
		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) 
		{
			sbSql.append(" AND (p.prod_code_list like '");
			sbSql.append(queryTo.getProdCode());
			sbSql.append(",%' OR p.prod_code_list='");
			sbSql.append(queryTo.getProdCode());
			sbSql.append("' OR p.prod_code_list like ',");
			sbSql.append(queryTo.getProdCode());
			sbSql.append("%') ");
		}
		sbSql.append(" order by pc.PROJ_CODE");

		logger.debug(sbSql.toString());

		GenericRowMapper<ProjectTo> rm = new GenericRowMapper<ProjectTo>(ProjectTo.class);
		if (queryTo.getPageNo() > 0)
		{
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			return sjt.query(getPagingSql(sbSql.toString(), cursorFrom, cursorTo), rm, new Object[] {});
		}
		else
		{
			return sjt.query(sbSql.toString(), rm, new Object[] {});
		}
	}
}
