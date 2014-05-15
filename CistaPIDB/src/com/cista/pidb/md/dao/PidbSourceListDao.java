package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.to.PidbSourceListTo;

public class PidbSourceListDao extends PIDBDaoSupport {

	public void batchInsert(List<PidbSourceListTo> list, String tableName) {
		if (list != null) {
			for (PidbSourceListTo to : list) {
				super.insert(to, tableName);
			}
		}
	}

	public void insert(final PidbSourceListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}

		int newId = SequenceSupport
				.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST);

		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "insert into PIDB_SOURCE_LIST(MATERIAL_NUM, VENDOR, MP_STATUS, PLANT_CODE,"
				+ "SAP_STATUS, INFO_MESSAGE, TIME_STAMP, RELEASED_BY,"
				+ "RELEASE_TO) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		logger.debug(sql);
		sjt.update(sql, newId, newInstance.getMaterialNum(), newInstance
				.getVendor(), newInstance.getMpStatus(), newInstance
				.getPlantCode(), newInstance.getSapStatus(), newInstance
				.getInfoMessage(), newInstance.getTimeStamp(), newInstance
				.getReleasedBy(), newInstance.getReleaseTo());
	}

}