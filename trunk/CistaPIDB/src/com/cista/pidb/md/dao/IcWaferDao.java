/*
 * 2010.05.07/FCG1 @Jere Huang - 新增tape out type, tape out project name.
 */
package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.to.IcWaferQueryTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;

/**
 * search IcWafer.
 * 
 * @author smilly
 * 
 */
public class IcWaferDao extends PIDBDaoSupport {

	public List<IcWaferTo> findBySearchPage(final IcWaferQueryTo findInstance) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "SELECT "
				+ " a.material_num, a.variant, a.proj_code, a.body_ver, a.option_ver,"
				+ " a.proj_code_w_version, a.routing_wf, a.routing_bp, a.routing_cp,"
				+ " a.mp_status, a.remark, a.fab_device_id, a.mask_layer_com,"
				+ " a.tape_out_date, a.mask_id, a.revision_item, a.assign_to,"
				+ " a.assign_email, a.status, a.created_by, a.modified_by, a.cp,"
				+ " a.bp, a.ds, a.material_desc, a.mask_house, a.mask_num,"
				+ " a.routing_polish,b.cp_polish_material_num , " 
				+ " a.routing_color_filter, a.routing_wafer_cf, " 
				+ " a.routing_csp, c.release_to, a.routing_tsv "
				+ " FROM pidb_ic_wafer a,pidb_cp_polish_material b,PIDB_PROJECT_CODE c "
				+ " where a.proj_code_w_version = b.project_code_w_version(+) "
				+ " and a.variant = b.cp_polish_variant(+)"
				+ " and a.PROJ_CODE = c.PROJ_CODE(+) ";
		if (findInstance.getReleaseTo() != null
				&& !findInstance.getReleaseTo().equals("")) {
			if (findInstance.getReleaseTo().equals("HX")
					|| findInstance.getReleaseTo().equals("WP")) {
				sql += " AND (c.RELEASE_TO ='" + findInstance.getReleaseTo()
						+ "'" + " or c.RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(findInstance);
		sql = sql + where;
		sql += " order by a.PROJ_CODE_W_VERSION, a.MATERIAL_NUM";
		logger.debug(sql);

		if (findInstance.getPageNo() > 0) {
			int cursorFrom = (findInstance.getPageNo() - 1)
					* findInstance.getPageSize() + 1;
			int cursorTo = (findInstance.getPageNo())
					* findInstance.getPageSize();
			return stj.query(getPagingSql(sql, cursorFrom, cursorTo),
					new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
					new Object[] {});
		} else {
			return stj.query(sql, new GenericRowMapper<IcWaferTo>(
					IcWaferTo.class), new Object[] {});
		}

	}

	public List<IcWaferTo> findAll() {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] {});
	}

	public IcWaferTo findByPrimaryKey(final String _meterialNum) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		//FCG1
		String sql = "SELECT "
				+ " a.material_num, a.variant, a.proj_code, a.body_ver, a.option_ver,"
				+ " a.proj_code_w_version, a.routing_wf, a.routing_bp, a.routing_cp,"
				+ " a.mp_status, a.remark, a.fab_device_id, a.mask_layer_com,"
				+ " a.tape_out_date, a.mask_id, a.revision_item, a.assign_to,"
				+ " a.assign_email, a.status, a.created_by, a.modified_by, a.cp,"
				+ " a.bp, a.ds, a.material_desc, a.mask_house, a.mask_num,"
				+ " a.routing_polish,b.cp_polish_material_num , a.routing_color_filter, a.routing_wafer_cf, a.routing_csp,c.VERSION, "
				+ " a.tape_out_type, a.tape_out_proj_name, a.routing_tsv "
				+ " FROM pidb_ic_wafer a,pidb_cp_polish_material b,PIDB_CP_CSP_MATERIAL c "
				+ " where a.proj_code_w_version = b.project_code_w_version(+) "
				+ " and a.variant = b.cp_polish_variant(+)"
				+ " and a.proj_code_w_version = c.project_code_w_version(+) "
				+ " and MATERIAL_NUM = ?";
		logger.debug(sql);
		List<IcWaferTo> result = stj.query(sql,
				new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { _meterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with meterialNum = " + _meterialNum
					+ " is not found.");
			return null;
		}
	}

	/**
	 * create an IcWafer object.
	 * 
	 * @param newInstance
	 */
	public void insertIcWafer(final IcWaferTo newInstance) {
		if (null == newInstance) 
		{
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) 
			{
				int newIcWaferId = SequenceSupport.nextValue(SequenceSupport.SEQ_IC_WAFER);
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				//FCG1
				/*
				String sql = "insert into PIDB_IC_WAFER "
						+ "(MATERIAL_NUM,"
						+ "VARIANT,"
						+ "PROJ_CODE,"
						+ "BODY_VER,"
						+ "OPTION_VER,"
						+ "PROJ_CODE_W_VERSION,"
						+ "ROUTING_WF,"
						+ "ROUTING_BP,"
						+ "ROUTING_CP,"
						+ "MP_STATUS,"
						+ "REMARK,"
						+ "FAB_DEVICE_ID,"
						+ "MASK_LAYER_COM,"
						+ "TAPE_OUT_DATE,"
						+ "MASK_ID,"
						+ "REVISION_ITEM,"
						+ "ASSIGN_TO,"
						+ "ASSIGN_EMAIL,"
						+ "STATUS,"
						+ "CREATED_BY,"
						+ "MODIFIED_BY,"
						+ "BP,"
						+ "CP,"
						+ "DS,"
						+ "MATERIAL_DESC,"
						+ "MASK_HOUSE,"
						+ "MASK_NUM,"
						+ "ROUTING_POLISH,"
						+ "ROUTING_COLOR_FILTER,"
						+ "ROUTING_WAFER_CF,"
						+ "ROUTING_CSP)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				*/
				StringBuffer sbSql = new StringBuffer();
				sbSql.append("INSERT INTO PIDB_IC_WAFER ");
				sbSql.append("(material_num, variant, proj_code, body_ver, option_ver, ");
				sbSql.append(" proj_code_w_version, routing_wf, routing_bp, routing_cp, mp_status, ");
				sbSql.append(" remark, fab_device_id, mask_layer_com, tape_out_date, mask_id, ");
				sbSql.append(" revision_item, assign_to, assign_email, status, created_by, ");
				sbSql.append(" modified_by, bp, cp, ds, material_desc,  ");
				sbSql.append(" mask_house, mask_num, routing_polish, routing_color_filter, routing_wafer_cf, ");
				sbSql.append(" routing_csp, tape_out_type, tape_out_proj_name, routing_tsv) ");
				sbSql.append("VALUES( ");
				sbSql.append(" ?, ?, ?, ?, ?,  ");
				sbSql.append(" ?, ?, ?, ?, ?, ");
				sbSql.append(" ?, ?, ?, ?, ?, ");
				sbSql.append(" ?, ?, ?, ?, ?, ");
				sbSql.append(" ?, ?, ?, ?, ?, ");
				sbSql.append(" ?, ?, ?, ?, ?, ");
				sbSql.append(" ?, ?, ?, ?)");
				
				logger.debug(sbSql.toString());
				sjt.update(sbSql.toString(), 
					newInstance.getMaterialNum(), newInstance.getVariant(), newInstance.getProjCode(), newInstance.getBodyVer(), newInstance.getOptionVer(),
					newInstance.getProjCodeWVersion(), newInstance.isRoutingWf(), newInstance.isRoutingBp(), newInstance.isRoutingCp(), newInstance.getMpStatus(),
					newInstance.getRemark(), newInstance.getFabDeviceId(), newInstance.getMaskLayerCom(), newInstance.getTapeOutDate(), newInstance.getMaskId(),
					newInstance.getRevisionItem(), newInstance.getAssignTo(), newInstance.getAssignEmail(), newInstance.getStatus(), newInstance.getCreatedBy(),
					newInstance.getModifiedBy(), newInstance.getBp(), newInstance.getCp(), newInstance.getDs(),	newInstance.getMaterialDesc(), 
					newInstance.getMaskHouse(), newInstance.getMaskNum(), newInstance.isRoutingPolish(), newInstance.isRoutingColorFilter(), newInstance.isRoutingWaferCf(),
					newInstance.isRoutingCsp(), newInstance.getTapeOutType(), newInstance.getTapeOutProjName(), newInstance.isRoutingTsv() );
			}
		};
		doInTransaction(callback);
	}

	public void deleteByPrimaryKey(final String _meterialNum,
			final String _projCodeWVersion) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_IC_WAFER where MATERIAL_NUM =? and PROJ_CODE_W_VERSION = ?";
				logger.debug(sql);
				sjt.update(sql, _meterialNum, _projCodeWVersion);
			}
		};
		doInTransaction(callback);
	}

	public void updateIcWafer(final IcWaferTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				//FCG1
				/*
				String sql = "update PIDB_IC_WAFER set "
						+ "VARIANT=?,"
						+ "PROJ_CODE=?,"
						+ "BODY_VER=?,"
						+ "OPTION_VER=?,"
						+ "ROUTING_WF=?,"
						+ "ROUTING_BP=?,"
						+ "ROUTING_CP=?,"
						+ "MP_STATUS=?,"
						+ "REMARK=?,"
						+ "FAB_DEVICE_ID=?,"
						+ "MASK_LAYER_COM=?,"
						+ "TAPE_OUT_DATE=?,"
						+ "MASK_ID=?,"
						+ "REVISION_ITEM=?,"
						+ "ASSIGN_TO=?,"
						+ "ASSIGN_EMAIL=?,"
						+ "STATUS=?,"
						+ "CREATED_BY=?,"
						+ "MODIFIED_BY=?,"
						+ "BP=?,"
						+ "CP=?,"
						+ "DS=?,"
						+ "MATERIAL_DESC=?,"
						+ "MASK_HOUSE=?,"
						+ "MASK_NUM=?,"
						+ "ROUTING_POLISH=?,ROUTING_COLOR_FILTER=?,ROUTING_WAFER_CF =?,ROUTING_CSP=? "
						+ "where MATERIAL_NUM=? and PROJ_CODE_W_VERSION =?";
				*/
				StringBuffer sbSql = new StringBuffer();
				sbSql.append("UPDATE PIDB_IC_WAFER "); 
				sbSql.append("SET variant=?, proj_code=?, body_ver=?, option_ver=?, routing_wf=?, "); 
				sbSql.append("  routing_bp=?, routing_cp=?, mp_status=?, remark=?, fab_device_id=?, "); 
				sbSql.append("  mask_layer_com=?, tape_out_date=?, mask_id=?, revision_item=?, assign_to=?, "); 
				sbSql.append("  assign_email=?, status=?, created_by=?, modified_by=?, bp=?, "); 
				sbSql.append("  cp=?, ds=?, material_desc=?, mask_house=?, mask_num=?,"); 
				sbSql.append("  routing_polish=?, routing_color_filter=?, routing_wafer_cf=?, routing_csp=?, tape_out_type=?, "); 
				sbSql.append("  tape_out_proj_name =?, ROUTING_TSV = ? ");
				sbSql.append("WHERE material_num=? and proj_code_w_version =? ");
				
				logger.debug(sbSql.toString());
				sjt.update(
						sbSql.toString(), 
						newInstance.getVariant(), newInstance.getProjCode(), newInstance.getBodyVer(), newInstance.getOptionVer(), newInstance.isRoutingWf(),
						newInstance.isRoutingBp(), newInstance.isRoutingCp(), newInstance.getMpStatus(), newInstance.getRemark(), newInstance.getFabDeviceId(),
						newInstance.getMaskLayerCom(), newInstance.getTapeOutDate(), newInstance.getMaskId(), newInstance.getRevisionItem(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(), newInstance.getCreatedBy(), newInstance.getModifiedBy(), newInstance.getBp(),
						newInstance.getCp(), newInstance.getDs(), newInstance.getMaterialDesc(), newInstance.getMaskHouse(), newInstance.getMaskNum(),
						newInstance.isRoutingPolish(), newInstance.isRoutingColorFilter(), newInstance.isRoutingWaferCf(), newInstance.isRoutingCsp(), newInstance.getTapeOutType(), 
						newInstance.getTapeOutProjName(),newInstance.getRoutingTsv(),
						newInstance.getMaterialNum(), newInstance.getProjCodeWVersion()
				);
			}
		};
		doInTransaction(callback);
	}

	public int countResult(IcWaferQueryTo icWaferQueryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) FROM pidb_ic_wafer a,pidb_cp_polish_material b,PIDB_PROJECT_CODE c "
				+ " where a.proj_code_w_version = b.project_code_w_version(+) "
				+ " and a.variant = b.cp_polish_variant(+)"
				+ " and a.PROJ_CODE = c.PROJ_CODE(+) ";
		if (icWaferQueryTo.getReleaseTo() != null && !icWaferQueryTo.getReleaseTo().equals("")) 
		{
			if (icWaferQueryTo.getReleaseTo().equals("HX")|| icWaferQueryTo.getReleaseTo().equals("WP"))
			{
				sql += " AND (c.RELEASE_TO ='" + icWaferQueryTo.getReleaseTo()
						+ "'" + " or c.RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(icWaferQueryTo);
		sql = sql + where;
		return sjt.queryForInt(sql, new Object[] {});
	}

	private String generateWhereCause(final IcWaferQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getMaterialNum() != null && !queryTo.getMaterialNum().equals("")) 
		{
			String materialNumQueryString = getSmartSearchQueryString("MATERIAL_NUM", queryTo.getMaterialNum());
			if (materialNumQueryString != null) 
			{
				sb.append(" and (" + materialNumQueryString + " )");
			}
		}

		if (queryTo.getProjCode() != null && !queryTo.getProjCode().equals("")) 
		{
			String projCodeQueryString = getSmartSearchQueryAllLike("a.PROJ_CODE", queryTo.getProjCode());
			if (projCodeQueryString != null) 
			{
				sb.append(" and (" + projCodeQueryString + " )");
			}
		}

		if (queryTo.getProjCodeWVersion() != null && !queryTo.getProjCodeWVersion().equals("")) 
		{
			String projCodeWVQueryString = getSmartSearchQueryString("PROJ_CODE_W_VERSION", queryTo.getProjCodeWVersion());
			if (projCodeWVQueryString != null) 
			{
				sb.append(" and (" + projCodeWVQueryString + " )");
			}
		}
		if (queryTo.getBodyVer() != null && !(queryTo.getBodyVer().trim().equals(""))) 
		{
			sb.append(" and BODY_VER = '" + queryTo.getBodyVer().trim() + "'");
		}
		if (queryTo.getOptionVer() != null && !(queryTo.getOptionVer().trim().equals(""))) 
		{
			sb.append(" and OPTION_VER = '" + queryTo.getOptionVer().trim()	+ "'");
		}

		if (queryTo.getRoutingBp() != null && !queryTo.getRoutingBp().equals("")) 
		{
			sb.append(" and ROUTING_BP = " + getSQLString(queryTo.getRoutingBp()) + " ");
		}
		if (queryTo.getRoutingWf() != null && !queryTo.getRoutingWf().equals("")) 
		{
			sb.append(" and ROUTING_WF = " + getSQLString(queryTo.getRoutingWf()) + " ");
		}
		if (queryTo.getRoutingCp() != null && !queryTo.getRoutingCp().equals("")) 
		{
			sb.append(" and ROUTING_CP = " + getSQLString(queryTo.getRoutingCp()) + " ");
		}

		if (queryTo.getTapeOutDateFrom() != null
				&& !queryTo.getTapeOutDateFrom().equals("")
				&& queryTo.getTapeOutDateTo() != null
				&& !queryTo.getTapeOutDateTo().equals("")) 
		{

			String dateFrom = getSQLDateString(queryTo.getTapeOutDateFrom()	+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			String dateTo = getSQLDateString(queryTo.getTapeOutDateTo()	+ " 23:59:59", "yyyy/MM/dd HH:mm:ss");
			sb.append(" and TAPE_OUT_DATE between " + dateFrom + " and " + dateTo + " ");
		}
		
		if (queryTo.getStatus() != null) 
		{
			if (queryTo.getStatus().equals("Draft")) 
			{
				sb.append(" and STATUS = 'Draft'");
			} 
			else if (queryTo.getStatus().equals("Completed")) 
			{
				sb.append(" and STATUS = 'Completed'");
			} 
			else if (queryTo.getStatus().equals("Released")) 
			{
				sb.append(" and STATUS = 'Released'");
			}
		}
		
		if (queryTo.getFabDeviceId() != null && !queryTo.getFabDeviceId().equals("")) 
		{
			String fabDeviceIdQueryString = getSmartSearchQueryString("FAB_DEVICE_ID", queryTo.getFabDeviceId());
			if (fabDeviceIdQueryString != null) 
			{
				sb.append(" and (" + fabDeviceIdQueryString + " )");
			}
		}
		
		if (queryTo.getRevisionItem() != null && !queryTo.getRevisionItem().equals("")) 
		{
			sb.append(" and REVISION_ITEM = " + getSQLString(queryTo.getRevisionItem()) + " ");
		}
		
		if (queryTo.getMaskHouse() != null && !queryTo.getMaskHouse().equals("")) 
		{
			String maskHouseQueryString = getSmartSearchQueryString("MASK_HOUSE", queryTo.getMaskHouse());
			if (maskHouseQueryString != null) 
			{
				sb.append(" and (" + maskHouseQueryString + " )");
			}
		}

		/*
		 * if (queryTo.getReleaseTo() != null &&
		 * !queryTo.getReleaseTo().equals("")) { String ReleaseToQueryString =
		 * getSmartSearchQueryString( "c.RELEASE_TO", queryTo.getReleaseTo());
		 * if (ReleaseToQueryString != null) { sb.append(" and (" +
		 * ReleaseToQueryString + " )"); } }
		 */

		return sb.toString();
	}

	public List<String> findMaterialNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct MATERIAL_NUM from PIDB_IC_WAFER order by MATERIAL_NUM";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> materialNums = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				materialNums.add((String) item.get("MATERIAL_NUM"));
			}
		}
		return materialNums;
	}

	public List<String> findProjCodeWVersion() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_CODE_W_VERSION from PIDB_IC_WAFER order by PROJ_CODE_W_VERSION";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projCodeWVersions = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projCodeWVersions.add((String) item.get("PROJ_CODE_W_VERSION"));
			}
		}
		return projCodeWVersions;
	}

	public List<String> findProjCodeWVersion(String projCode) {
		// if (projCode == null && projCode.length() <= 0) {
		// logger.warn("Project Code is employ,it is must field.");
		// }
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_CODE_W_VERSION from PIDB_IC_WAFER where PROJ_CODE=? "
				+ "and ROUTING_CP='1' order by PROJ_CODE_W_VERSION";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] { projCode });
		List<String> projCodeWVersions = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projCodeWVersions.add((String) item.get("PROJ_CODE_W_VERSION"));
			}
		}
		return projCodeWVersions;
	}

	public List<IcWaferTo> findByProjectCode(String projCode) {
		if (projCode == null && projCode.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where PROJ_CODE=? "
				+ "and ROUTING_BP='1' order by MATERIAL_NUM";
		logger.debug(sql);
		List<IcWaferTo> result = stj.query(sql,
				new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { projCode });

		return result;
	}

	public IcWaferTo findByProjCodeWVersion(final String _projCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where PROJ_CODE_W_VERSION = ?";
		logger.debug(sql);
		List<IcWaferTo> result = stj.query(sql,
				new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { _projCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public String findMaxVar(final String projCWVersion, final String fab) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where PROJ_CODE_W_VERSION = ?";
		List<IcWaferTo> waferList = sjt.query(sql,
				new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { projCWVersion });
		if (waferList != null && waferList.size() > 0) {
			ProjectCodeDao projCodeDao = new ProjectCodeDao();
			ProjectCodeTo projCodeTo = new ProjectCodeTo();
			ProjectDao projDao = new ProjectDao();
			ProjectTo projectTo = new ProjectTo();
			String cmpFab = "";
			String varReturn = null;
			for (IcWaferTo to : waferList) {
				projCodeTo = projCodeDao.findByProjectCode(to.getProjCode());
				if (projCodeTo != null) {
					projectTo = projDao.find(projCodeTo.getProjName());
					if (projectTo == null) {
						cmpFab = null;
					} else {
						cmpFab = projectTo.getFab();
					}
					if (cmpFab != null && cmpFab.equals(fab)) {
						if (varReturn != null) {
							if (varReturn.compareToIgnoreCase(to.getVariant()) < 0) {
								varReturn = to.getVariant();
							}
						} else {
							varReturn = to.getVariant();
						}
					}
				}
			}
			return varReturn;
		} else {
			return null;
		}
	}

	public List<String> findMaskLayComByProjCode(final String projCode, int n) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct MASK_LAYER_COM from PIDB_IC_WAFER where PROJ_CODE = ? order by PROJ_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] { projCode });
		List<String> projCodes = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				String maskTemp = (String) item.get("MASK_LAYER_COM");
				if (maskTemp != null) {
					String[] tempList = maskTemp.split("/");
					for (String t : tempList) {
						if (t != null && !projCodes.contains(t)
								&& t.length() == n) {
							projCodes.add(t);
						}
					}
				}
			}
		}
		return projCodes;
	}

	public List<String> findMaskLayComByProjCode(final String projCode,
			final String... type) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct MASK_LAYER_COM from PIDB_IC_WAFER where PROJ_CODE = ? order by PROJ_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] { projCode });
		List<String> projCodes = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				String maskTemp = (String) item.get("MASK_LAYER_COM");
				if (maskTemp != null) {
					String[] tempList = maskTemp.split("/");
					for (String t : tempList) {
						if (t != null && t.length() > 3) {
							if (type != null && type.length > 0) {
								if (!projCodes.contains(t)
										&& t.indexOf("-") >= 0) {
									projCodes.add(t);
								}
							} else {
								if (!projCodes.contains(t)
										&& t.indexOf("-") < 0) {
									projCodes.add(t);
								}
							}
						}
					}
				}
			}
		}
		return projCodes;
	}

	public List<ProjectTo> findProjByProjCodeWVersion(String projCodeWVersion) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select pp.*,ppc.* from PIDB_IC_WAFER piw ,PIDB_PROJECT pp, PIDB_PROJECT_CODE ppc where pp.PROJ_NAME=ppc.PROJ_NAME and ppc.PROJ_CODE=piw.PROJ_CODE and piw.PROJ_CODE_W_VERSION=?";
		logger.debug(sql);

		return stj.query(sql, new GenericRowMapper<ProjectTo>(ProjectTo.class),
				new Object[] { projCodeWVersion });
	}

	public List<IcWaferTo> findByProjCode(String projCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where PROJ_CODE=? ";
		logger.debug(sql);
		List<IcWaferTo> result = stj.query(sql,
				new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { projCode });

		return result;
	}

	public List<IcWaferTo> findListByProjCodeWVersion(
			final String _projCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where PROJ_CODE_W_VERSION = ?";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { _projCodeWVersion });
	}

	public List<IcWaferTo> findListMaterialDesc(final String materialNum,
			final String _projCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where MATERIAL_NUM=? and PROJ_CODE_W_VERSION = ?";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<IcWaferTo>(IcWaferTo.class),
				new Object[] { materialNum, _projCodeWVersion });
	}

	public IcWaferTo findMaterialDesc(final String materialNum,
			final String _projCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_WAFER where MATERIAL_NUM=? and PROJ_CODE_W_VERSION = ?";
		logger.debug(sql);
		List<IcWaferTo> result = stj.query(sql,
				new GenericRowMapper<IcWaferTo>(IcWaferTo.class), new Object[] {
						materialNum, _projCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<FunctionParameterTo> findMaskNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select item,field_value,field_show_name from pidb_fun_parameter_value where "
				+ " fun_field_name='NEW_MASK_NUMBER' and fun_name='IC_WAFER' order by item";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj.query(sql,
				new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] {});
		return result;
	}
	
    public List<IcWaferTo> findByProjWver(String projWver) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = " Select 'W'||substr(A.MATERIAL_NUM,2,13) MATERIAL_NUM " +
        			" From PIDB_IC_WAFER A " +
        			" Where A.PROJ_CODE_W_VERSION = ? ";
        logger.debug(sql);
        GenericRowMapper<IcWaferTo> rm = new GenericRowMapper<IcWaferTo>(IcWaferTo.class);
        
        return sjt.query(sql, rm, new Object[] { projWver });
        
    }    
}
