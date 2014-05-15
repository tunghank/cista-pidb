/*
 * 2010.03.05/FCG1 @Jere Huang - Initial Version.
 */
package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ProjectQueryTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.RptProdTapeoutPeriodQueryTo;
import com.cista.pidb.md.to.RptTapeOutIcWaferListTo;
import com.cista.pidb.md.to.RptTapeOutMasksetListTo;
import com.cista.pidb.md.to.RptTapeOutMasksetQueryTo;

public class RptTapeoutDetailDao extends PIDBDaoSupport
{
	public List<RptTapeOutIcWaferListTo> queryTapeOutDetailByPeriod(RptProdTapeoutPeriodQueryTo queryTo) 
	{
		String sql="";
		StringBuffer sbSql;
		String sYearMonthFrom;
		String sYearMonthTo;
		
		
		sbSql = new StringBuffer();
		sbSql.append(" SELECT nvl((SELECT proj_name FROM pidb_project_code where proj_code = pidb_ic_wafer.proj_code),'no proj name' ) as proj_name,  "); 
		sbSql.append(" to_char(tape_out_date,'yyyy/mm') as year_month, nvl(revision_item, ' ') as revision_item, proj_code_w_version, mask_layer_com ,to_char(tape_out_date,'yyyy/MM/dd') as tape_out_date "); 
		sbSql.append(" FROM PIDB_IC_WAFER "); 
		sbSql.append(" WHERE  tape_out_date >= to_date(?,'yyyy/MM/dd') and tape_out_date < add_months(to_date(?,'yyyy/MM/dd'),1 ) "); 
		sbSql.append(" ORDER BY  tape_out_date, proj_name, proj_code_w_version "); 
		
		sql = sbSql.toString();
		//logger.debug(sql);
		//condition
		sYearMonthFrom = queryTo.getTapeOutYearFrom() + "/" + queryTo.getTapeOutMonthFrom()+ "/01";
		sYearMonthTo = queryTo.getTapeOutYearTo() + "/" + queryTo.getTapeOutMonthTo()+ "/01";
		
		
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<RptTapeOutIcWaferListTo> rm = new GenericRowMapper<RptTapeOutIcWaferListTo>(RptTapeOutIcWaferListTo.class);
		List<RptTapeOutIcWaferListTo> result = sjt.query(sql, rm, new Object[] {sYearMonthFrom,sYearMonthTo });
		return result;
	}
	
	public List<ProjectTo> queryTapeOutProject(ProjectQueryTo queryTo)
	{
		StringBuffer sbSql;
		
		sbSql = new StringBuffer();
		sbSql.append(" SELECT wafer_inch, prod_family, prod_line "); 
		sbSql.append(" FROM PIDB_PROJECT  "); 
		sbSql.append(" WHERE proj_name= ? ");
		
		if (queryTo.getProdFamily() != null && !queryTo.getProdFamily().equals("")) 			
		{
			sbSql.append("and prod_family ='");
			sbSql.append(queryTo.getProdFamily());
			sbSql.append("'");
		}
		if (queryTo.getProdLine() != null && !queryTo.getProdLine().equals("")) 			
		{
			sbSql.append("and prod_line ='");
			sbSql.append(queryTo.getProdLine());
			sbSql.append("'");
		}
		
		logger.debug(sbSql.toString());
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<ProjectTo> rm = new GenericRowMapper<ProjectTo>(ProjectTo.class);
		List<ProjectTo> result = sjt.query(sbSql.toString(), rm, new Object[] {queryTo.getProjName()} );
		return result;
	}
	
	public List<RptTapeOutMasksetListTo> queryTapeOutBeforeMaskset(RptTapeOutMasksetQueryTo queryTo)
	{
		StringBuffer sbSql;
		sbSql = new StringBuffer();
		sbSql.append(" SELECT   MASK_LAYER_COM "); 
		sbSql.append(" FROM  PIDB_IC_WAFER "); 
		sbSql.append(" WHERE proj_code IN (SELECT proj_code "); 
		sbSql.append("                          FROM pidb_project_code "); 
		sbSql.append("                         WHERE proj_name = ?) "); 
		sbSql.append("      AND tape_out_date <= TO_DATE (?, 'yyyy/MM/dd') "); 
		sbSql.append("      AND proj_code_w_version < ? "); 
		
		//logger.debug(sbSql.toString());
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<RptTapeOutMasksetListTo> rm = new GenericRowMapper<RptTapeOutMasksetListTo>(RptTapeOutMasksetListTo.class);
		List<RptTapeOutMasksetListTo> result = sjt.query(sbSql.toString(), rm, new Object[] {queryTo.getProjName(), queryTo.getTapeOutDate(), queryTo.getProjCodeWVersion()   } );
		return result;
	}
}
