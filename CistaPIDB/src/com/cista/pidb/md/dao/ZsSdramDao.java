package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ZsSdramQueryTo;
import com.cista.pidb.md.to.ZsSdramTo;

/**
 * search IcWafer.
 * 
 * @author smilly
 * 
 */
public class ZsSdramDao extends PIDBDaoSupport {

	public List<ZsSdramTo> findBySearchPage(final ZsSdramQueryTo findInstance) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_SDRAM where 1=1";
		if (findInstance.getReleaseTo() != null
				&& !findInstance.getReleaseTo().equals("")) {
			if (findInstance.getReleaseTo().equals("HX")
					|| findInstance.getReleaseTo().equals("WP")) {
				sql += " AND (RELEASE_TO ='" + findInstance.getReleaseTo() + "'"
						+ " or RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(findInstance);
		sql = sql + where;
		sql += " order by MATERIAL_NUM";
		logger.debug(sql);

		if (findInstance.getPageNo() > 0) {
			int cursorFrom = (findInstance.getPageNo() - 1)
					* findInstance.getPageSize() + 1;
			int cursorTo = (findInstance.getPageNo())
					* findInstance.getPageSize();
			return stj.query(getPagingSql(sql, cursorFrom, cursorTo),
					new GenericRowMapper<ZsSdramTo>(ZsSdramTo.class),
					new Object[] {});
		} else {
			return stj.query(sql, new GenericRowMapper<ZsSdramTo>(
					ZsSdramTo.class), new Object[] {});
		}

	}

	public List<ZsSdramTo> findAll() {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_SDRAM";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ZsSdramTo>(ZsSdramTo.class),
				new Object[] {});
	}

	public ZsSdramTo findByPrimaryKey(final String _meterialNum) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_SDRAM where MATERIAL_NUM = ?";
		logger.debug(sql);
		List<ZsSdramTo> result = stj.query(sql,
				new GenericRowMapper<ZsSdramTo>(ZsSdramTo.class),
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
	public void insertSdram(final ZsSdramTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				/*
				 * int newSdramId = SequenceSupport
				 * .nextValue(SequenceSupport.SEQ_PIDB_SDRAM);
				 */

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_SDRAM " + "(MATERIAL_NUM,"
						+ "DESCRIPTION," + "MEMORY_SIZE," + "SPEED,"
						+ "OPERATION_VOLTAGE," + "THICKNESS,"
						+ "APPLICATION_PRODUCT," + "WAFER_INCH," + "REMARK,"
						+ "GROSS_DIE," + "CREATED_BY," + "MODIFIED_BY,"
						+ "VENDOR_CODE, RELEASE_TO)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getDescription(), newInstance.getMemorySize(),
						newInstance.getSpeed(), newInstance
								.getOperationVoltage(), newInstance
								.getThickness(), newInstance
								.getApplicationProduct(), newInstance
								.getWaferInch(), newInstance.getRemark(),
						newInstance.getGrossDie(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getVendorCode(), newInstance.getReleaseTo());

			}
		};
		doInTransaction(callback);
	}

	public void deleteByPrimaryKey(final String meterialNum) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_SDRAM where MATERIAL_NUM =? ";
				logger.debug(sql);
				sjt.update(sql, meterialNum);
			}
		};
		doInTransaction(callback);
	}

	public void updateSdram(final ZsSdramTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_SDRAM set " + "MATERIAL_NUM=?,"
						+ "DESCRIPTION=?," + "MEMORY_SIZE=?," + "SPEED=?,"
						+ "OPERATION_VOLTAGE=?," + "THICKNESS=?,"
						+ "APPLICATION_PRODUCT=?," + "WAFER_INCH=?,"
						+ "REMARK=?," + "GROSS_DIE=?," + "CREATED_BY=?,"
						+ "MODIFIED_BY=?," + "VENDOR_CODE=? , RELEASE_TO=?"
						+ "where MATERIAL_NUM=? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getDescription(), newInstance.getMemorySize(),
						newInstance.getSpeed(), newInstance
								.getOperationVoltage(), newInstance
								.getThickness(), newInstance
								.getApplicationProduct(), newInstance
								.getWaferInch(), newInstance.getRemark(),
						newInstance.getGrossDie(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getVendorCode(), newInstance.getReleaseTo(),
						newInstance.getMaterialNum());
			}
		};
		doInTransaction(callback);
	}

	public int countResult(ZsSdramQueryTo zsSdramQueryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_SDRAM where 1=1";
		if (zsSdramQueryTo.getReleaseTo() != null
				&& !zsSdramQueryTo.getReleaseTo().equals("")) {
			if (zsSdramQueryTo.getReleaseTo().equals("HX")
					|| zsSdramQueryTo.getReleaseTo().equals("WP")) {
				sql += " AND (RELEASE_TO ='" + zsSdramQueryTo.getReleaseTo() + "'"
						+ " or RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(zsSdramQueryTo);
		sql = sql + where;
		return sjt.queryForInt(sql, new Object[] {});
	}

	private String generateWhereCause(final ZsSdramQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getMaterialNum() != null
				&& !queryTo.getMaterialNum().equals("")) {
			String materialNumQueryString = getSmartSearchQueryString(
					"MATERIAL_NUM", queryTo.getMaterialNum());
			if (materialNumQueryString != null) {
				sb.append(" and (" + materialNumQueryString + " )");
			}
		}

		if (queryTo.getMemorySize() != null
				&& !queryTo.getMemorySize().equals("")) {
			String memorySizeQueryString = getSmartSearchQueryString(
					"MEMORY_SIZE", queryTo.getMemorySize());
			if (memorySizeQueryString != null) {
				sb.append(" and (" + memorySizeQueryString + " )");
			}
		}

		if (queryTo.getSpeed() != null && !queryTo.getSpeed().equals("")) {
			String speedQueryString = getSmartSearchQueryString("SPEED",
					queryTo.getSpeed());
			if (speedQueryString != null) {
				sb.append(" and (" + speedQueryString + " )");
			}
		}
		if (queryTo.getOperationVoltage() != null
				&& !queryTo.getOperationVoltage().equals("")) {
			String OperationVoltageQueryString = getSmartSearchQueryString(
					"OPERATION_VOLTAGE", queryTo.getOperationVoltage());
			if (OperationVoltageQueryString != null) {
				sb.append(" and (" + OperationVoltageQueryString + " )");
			}
		}
		if (queryTo.getVendorCode() != null
				&& !queryTo.getVendorCode().equals("")) {
			String vendorCodeQueryString = getSmartSearchQueryString(
					"VENDOR_CODE", queryTo.getSpeed());
			if (vendorCodeQueryString != null) {
				sb.append(" and (" + vendorCodeQueryString + " )");
			}
		}
		if (queryTo.getApplicationProduct() != null
				&& !queryTo.getApplicationProduct().equals("")) {
			String applicationProduct = getSmartSearchQueryAllLike(
					"APPLICATION_PRODUCT", queryTo.getApplicationProduct());
			if (applicationProduct != null) {
				sb.append("and (" + applicationProduct + ")");
			}
		}

		/*if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			String releaseTo = getSmartSearchQueryString("RELEASE_TO", queryTo
					.getReleaseTo());
			if (releaseTo != null) {
				sb.append(" and (" + releaseTo + " )");
			}
		}*/

		return sb.toString();
	}

	public List<String> findMaterialNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct MATERIAL_NUM from PIDB_SDRAM order by MATERIAL_NUM";
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

	public List<String> findTapeShortName(final String vendorCode) {

		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "Select SHORT_NAME TAPE_VENDOR from WM_SAP_MASTER_VENDOR where ("
				+ super.getSmartSearchQueryString("VENDOR_CODE", vendorCode)
				+ ") order by VENDOR_CODE";
		logger.debug(sql);

		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> sdram = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				sdram.add((String) item.get("TAPE_VENDOR"));
			}
		}
		return sdram;
	}
}
