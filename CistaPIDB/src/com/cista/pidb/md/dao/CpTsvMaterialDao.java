package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CpTsvMaterialTo;


public class CpTsvMaterialDao extends PIDBDaoSupport {

	/**
	 * create an CP Polish Material object.
	 * 
	 * @param newInstance
	 */
	public void insertTsvMaterial(final CpTsvMaterialTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new TSV Material object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_CP_TSV_MATERIAL "
						+ "(CP_TSV_MATERIAL_NUM," + "PROJECT_CODE_W_VERSION,"
						+ "DESCRIPTION," + "REMARK," + "UPDATE_DATE,"
						+ "CREATED_BY," + "MODIFIED_BY," + "MP_STATUS ,"
						+ "CP_TSV_VARIANT, VERSION)"
						+ " values(?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt
						.update(sql, newInstance.getCpTsvMaterialNum(),
								newInstance.getProjectCodeWVersion(),
								newInstance.getDescription(), newInstance
										.getRemark(), newInstance
										.getUpdateDate(), newInstance
										.getCreatedBy(), newInstance
										.getModifiedBy(), newInstance
										.getMpStatus(), newInstance
										.getCpTsvVariant(), newInstance
										.getVersion());

			}
		};
		doInTransaction(callback);
	}

	public CpTsvMaterialTo findTsvMaterial(final String cpTsvMaterial) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "Select * From PIDB_CP_TSV_MATERIAL where CP_TSV_MATERIAL_NUM = ?";
		logger.debug(sql);
		List<CpTsvMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpTsvMaterialTo>(CpTsvMaterialTo.class),
				new Object[] { cpTsvMaterial });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.debug("User with cpTsvMaterial = " + cpTsvMaterial
					+ " is not found.");
			return null;
		}
	}

	public List<CpTsvMaterialTo> findByProjectCode(String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "Select * from PIDB_CP_TSV_MATERIAL Where PROJECT_CODE_W_VERSION=? " +
				" Order By CP_TSV_MATERIAL_NUM ";
		logger.debug(sql);
		List<CpTsvMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpTsvMaterialTo>(CpTsvMaterialTo.class),
				new Object[] { projectCodeWVersion });

		return result;
	}
}
