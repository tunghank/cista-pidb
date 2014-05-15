/*
 * 2010.03.01/FCG1 @Jere Huang - Initial Version.
 */
package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.RptProdTapeoutPeriodQueryTo;
import com.cista.pidb.md.to.RptProdTapeoutRevisionListTo;
import com.cista.pidb.md.to.RptProdTapeoutRevisionQueryTo;
import com.cista.pidb.md.to.RptTapeoutFamilyLineTo;

public class RptProdTapeoutDao extends PIDBDaoSupport
{
	
	public List<RptTapeoutFamilyLineTo> queryProdFamilyLineByPeriod(RptProdTapeoutPeriodQueryTo queryTo) 
	{
		String sql="";
		StringBuffer sbSql;
		String sYearMonthFrom;
		String sYearMonthTo;
		
		
		sbSql = new StringBuffer();
		sbSql.append("SELECT distinct prod_family, prod_line "); 
		sbSql.append("FROM pidb_project  "); 
		sbSql.append("WHERE proj_name IN "); 
		sbSql.append("( "); 
		sbSql.append("  SELECT a.proj_name "); 
		sbSql.append("  FROM pidb_project_code a where proj_code IN "); 
		sbSql.append("  ( "); 
		sbSql.append("     SELECT  a.proj_code "); 
		sbSql.append("     FROM pidb_ic_wafer a "); 
		sbSql.append("     WHERE tape_out_date >= to_date(?,'yyyy/MM/dd') and tape_out_date < add_months(to_date(?,'yyyy/MM/dd'),1 ) "); 
		sbSql.append("  ) "); 
		sbSql.append(") AND NOT (prod_family is null AND prod_line is null) "); 
		
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
		
		sql = sbSql.toString();
		logger.debug(sql);
		//condition
		sYearMonthFrom = queryTo.getTapeOutYearFrom() + "/" + queryTo.getTapeOutMonthFrom()+ "/01";
		sYearMonthTo = queryTo.getTapeOutYearTo() + "/" + queryTo.getTapeOutMonthTo()+ "/01";
		
		
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<RptTapeoutFamilyLineTo> rm = new GenericRowMapper<RptTapeoutFamilyLineTo>(RptTapeoutFamilyLineTo.class);
		List<RptTapeoutFamilyLineTo> result = sjt.query(sql, rm, new Object[] {sYearMonthFrom,sYearMonthTo });
		return result;
	}
	
	
	public List<RptProdTapeoutRevisionListTo> queryRevision(RptProdTapeoutRevisionQueryTo queryTo) 
	{
		String sql="";
		StringBuffer sbSql;
		String sYearMonthFrom;
		String sYearMonthTo;
		
		sbSql = new StringBuffer();
		
		sbSql.append(" SELECT to_char(tape_out_date,'yyyy/MM') as Year_Month, REVISION_ITEM "); 
		sbSql.append(" FROM pidb_ic_wafer "); 
		sbSql.append(" WHERE proj_code IN "); 
		sbSql.append(" ( "); 
		sbSql.append("    SELECT proj_code "); 
		sbSql.append("    FROM pidb_project_code "); 
		sbSql.append("    WHERE proj_name IN  "); 
		sbSql.append("    ( "); 
		sbSql.append("       select PROJ_NAME from pidb_project where PROD_FAMILY=? and  PROD_LINE =? "); 
		sbSql.append("    ) "); 
		sbSql.append(" ) "); 
		sbSql.append(" AND tape_out_date >= to_date(?, 'yyyy/MM/dd') and tape_out_date < add_months(to_date(?,'yyyy/MM/dd'),1 ) "); 
		sbSql.append(" ORDER BY tape_out_date "); 
		
		sql = sbSql.toString();
		//logger.debug(sql);
		
		//condition
		sYearMonthFrom = queryTo.getTapeOutYearFrom() + "/" + queryTo.getTapeOutMonthFrom() + "/01";
		sYearMonthTo = queryTo.getTapeOutYearTo() + "/" + queryTo.getTapeOutMonthTo() + "/01";
		
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<RptProdTapeoutRevisionListTo> rm = new GenericRowMapper<RptProdTapeoutRevisionListTo>(RptProdTapeoutRevisionListTo.class);
		List<RptProdTapeoutRevisionListTo> result = sjt.query(sql, rm, new Object[] {queryTo.getProdFamily(), queryTo.getProdLine(),sYearMonthFrom,sYearMonthTo });
		
		return result;
	}
}
