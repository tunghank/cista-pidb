package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ZsEepromQueryTo;
import com.cista.pidb.md.to.ZsEepromTo;

/**
 * search IcWafer.
 * 
 * @author smilly
 * 
 */
public class ZsEepromDao extends PIDBDaoSupport {

	public List<ZsEepromTo> findBySearchPage(final ZsEepromQueryTo findInstance) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_EEPROM where 1=1";
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
					new GenericRowMapper<ZsEepromTo>(ZsEepromTo.class),
					new Object[] {});
		} else {
			return stj.query(sql, new GenericRowMapper<ZsEepromTo>(
					ZsEepromTo.class), new Object[] {});
		}

	}

	public List<ZsEepromTo> findAll() {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_EEPROM";
		logger.debug(sql);
		return stj.query(sql,
				new GenericRowMapper<ZsEepromTo>(ZsEepromTo.class),
				new Object[] {});
	}

	public ZsEepromTo findByPrimaryKey(final String _meterialNum) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_EEPROM where MATERIAL_NUM = ?";
		logger.debug(sql);
		List<ZsEepromTo> result = stj.query(sql,
				new GenericRowMapper<ZsEepromTo>(ZsEepromTo.class),
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
	public void insertSdram(final ZsEepromTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new EEPROM object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				/*
				 * int newSdramId = SequenceSupport
				 * .nextValue(SequenceSupport.SEQ_PIDB_SDRAM);
				 */

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_EEPROM " + "(MATERIAL_NUM,"
						+ "DESCRIPTION," + "SPEED," + "DENSITY,"
						+ "OPERATION_VOLTAGE," + "THICKNESS,"
						+ "APPLICATION_PRODUCT," + "WAFER_INCH," + "REMARK,"
						+ "GROSS_DIE," + "CODE_BIND," + "CREATED_BY,"
						+ "MODIFIED_BY," + "VENDOR_CODE, RELEASE_TO)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getDescription(), newInstance.getSpeed(), newInstance
						.getDensity(), newInstance.getOperationVoltage(),
						newInstance.getThickness(), newInstance
								.getApplicationProduct(), newInstance
								.getWaferInch(), newInstance.getRemark(),
						newInstance.getGrossDie(), newInstance.getCodeBind(),
						newInstance.getCreatedBy(),
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
				String sql = "delete from PIDB_EEPROM where MATERIAL_NUM =? ";
				logger.debug(sql);
				sjt.update(sql, meterialNum);
			}
		};
		doInTransaction(callback);
	}

	public void updateSdram(final ZsEepromTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_EEPROM set " + "MATERIAL_NUM=?,"
						+ "DESCRIPTION=?," + "SPEED=?," + " DENSITY=?,"
						+ "OPERATION_VOLTAGE=?," + "THICKNESS=?,"
						+ "APPLICATION_PRODUCT=?," + "WAFER_INCH=?,"
						+ "REMARK=?," + "GROSS_DIE=?," + "CODE_BIND=?,"
						+ "CREATED_BY=?," + "MODIFIED_BY=?,"
						+ "VENDOR_CODE=?, RELEASE_TO=?"
						+ "where MATERIAL_NUM=? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getDescription(), newInstance.getSpeed(), newInstance
						.getDensity(), newInstance.getOperationVoltage(),
						newInstance.getThickness(), newInstance
								.getApplicationProduct(), newInstance
								.getWaferInch(), newInstance.getRemark(),
						newInstance.getGrossDie(), newInstance.getCodeBind(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getVendorCode(), newInstance.getReleaseTo(),
						newInstance.getMaterialNum());
			}
		};
		doInTransaction(callback);
	}

	public int countResult(ZsEepromQueryTo zsEepromQueryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_EEPROM where 1=1";
		if (zsEepromQueryTo.getReleaseTo() != null
				&& !zsEepromQueryTo.getReleaseTo().equals("")) {
			if (zsEepromQueryTo.getReleaseTo().equals("HX")
					|| zsEepromQueryTo.getReleaseTo().equals("WP")) {
				sql += " AND (RELEASE_TO ='" + zsEepromQueryTo.getReleaseTo() + "'"
						+ " or RELEASE_TO ='ALL')";
			}
		}
		String where = generateWhereCause(zsEepromQueryTo);
		sql = sql + where;
		return sjt.queryForInt(sql, new Object[] {});
	}

	private String generateWhereCause(final ZsEepromQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getMaterialNum() != null
				&& !queryTo.getMaterialNum().equals("")) {
			String materialNumQueryString = getSmartSearchQueryString(
					"MATERIAL_NUM", queryTo.getMaterialNum());
			if (materialNumQueryString != null) {
				sb.append(" and (" + materialNumQueryString + " )");
			}
		}

		if (queryTo.getSpeed() != null && !queryTo.getSpeed().equals("")) {
			String speedQueryString = getSmartSearchQueryString("SPEED",
					queryTo.getSpeed());
			if (speedQueryString != null) {
				sb.append(" and (" + speedQueryString + " )");
			}
		}
		if (queryTo.getDensity() != null && !queryTo.getDensity().equals("")) {
			String memorySizeQueryString = getSmartSearchQueryString(
					"MEMORY_SIZE", queryTo.getDensity());
			if (memorySizeQueryString != null) {
				sb.append(" and (" + memorySizeQueryString + " )");
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
		String sql = "select distinct MATERIAL_NUM from PIDB_EEPROM order by MATERIAL_NUM";
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
		List<String> eeprom = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				eeprom.add((String) item.get("TAPE_VENDOR"));
			}
		}
		return eeprom;
	}

}
