/*
 * 2010.03.05/FCG1 @Jere Huang - Initial Version.
 */
package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.RptProjCodeHitRateListTo;
import com.cista.pidb.md.to.RptProjCodeHitRateQueryTo;

public class RptProjCodeHitRateDao extends PIDBDaoSupport
{
	

	public List<RptProjCodeHitRateListTo> queryProjCodeHitRate(RptProjCodeHitRateQueryTo queryTo) 
	{
		String sql="";
		StringBuffer sbSql;
		
		sbSql = new StringBuffer();
		sbSql.append(" SELECT ic_wafer.proj_code, ic_wafer.proj_code_w_version, ic_wafer.MP_STATUS,ic_wafer.proj_name "); 
		sbSql.append(" , project.prod_line, to_char(ic_wafer.tape_out_date,'yyyy/MM/dd') as tape_out_date ");
		sbSql.append(" ,( SELECT description FROM wm_sap_master_product_family  where product_family=project.prod_family) as prod_family, ic_wafer.revision_item ");
		sbSql.append(" FROM  "); 
		sbSql.append("   ( "); 
		sbSql.append("     SELECT tape_out_date, revision_item, proj_code, proj_code_w_version, MP_STATUS,(select  proj_name from pidb_project_code where proj_code = pidb_ic_wafer.proj_code) as proj_name "); 
		sbSql.append("     FROM pidb_ic_wafer  "); 
		sbSql.append("   ) ic_wafer, pidb_project project "); 
		sbSql.append(" WHERE ic_wafer.proj_name = project.proj_name "); 
		sbSql.append(generateWhereCause(queryTo));
		
		sbSql.append(" AND ic_wafer.tape_out_date is not null ");
		sbSql.append(" order by proj_code ");
		sql = sbSql.toString();
		
		logger.debug(sql);

		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<RptProjCodeHitRateListTo> rm = new GenericRowMapper<RptProjCodeHitRateListTo>(RptProjCodeHitRateListTo.class);
		List<RptProjCodeHitRateListTo> result = sjt.query(sql, rm, new Object[] { });
		return result;
	}
	
	private String generateWhereCause(final RptProjCodeHitRateQueryTo queryTo) 
	{
		StringBuilder sb = new StringBuilder();
		String 	sProjCode;
		String  sProjName;
		String  sFamily;
		String  sProdLine;
		
		if (queryTo.getProjCode() != null && !queryTo.getProjCode().equals("")) 
		{
			sb.append(" and ic_wafer.proj_code in(");
			sProjCode = queryTo.getProjCode();

			String[] subProjCode = sProjCode.split(",");
			for(String subObj:subProjCode)
			{
				sb.append("'");
				sb.append(subObj.trim());
				sb.append("',");
			}
			//去除 , 
			sb.setLength(sb.length()-1);
			
			sb.append(" ) ");
		}
		
		if (queryTo.getProjName() != null && !queryTo.getProjName().equals("")) 
		{
			sb.append(" and project.proj_name in(");
			sProjName = queryTo.getProjName();

			String[] subProjName = sProjName.split(",");
			for(String subObj:subProjName)
			{
				sb.append("'");
				sb.append(subObj.trim());
				sb.append("',");
			}
			//去除 , 
			sb.setLength(sb.length()-1);
			
			sb.append(" ) ");
		}
		
		if (queryTo.getProdLine() != null && !queryTo.getProdLine().equals("")) 
		{
			sb.append(" and project.prod_line in(");
			sProdLine = queryTo.getProdLine();

			String[] subProdLine = sProdLine.split(",");
			for(String subObj:subProdLine)
			{
				sb.append("'");
				sb.append(subObj.trim());
				sb.append("',");
			}
			//去除 , 
			sb.setLength(sb.length()-1);
			
			sb.append(" ) ");
		}
		
		if (queryTo.getProdFamily() != null && !queryTo.getProdFamily().equals("")) 
		{
			sb.append(" and project.prod_family in(");
			sFamily = queryTo.getProdFamily();

			String[] subFamily = sFamily.split(",");
			for(String subObj:subFamily)
			{
				sb.append("'");
				sb.append(subObj.trim());
				sb.append("',");
			}
			//去除 , 
			sb.setLength(sb.length()-1);
			
			sb.append(" ) ");
		}
		return sb.toString();
	}
	
}
