package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.to.IfMaterialCharacterTo;

public class IfMaterialCharacterDao extends PIDBDaoSupport {

	public void batchInsert(List<IfMaterialCharacterTo> list, String tableName) {
		if (list != null) {
			for (IfMaterialCharacterTo to : list) {
				super.insert(to, tableName);
			}
		}
	}

	public void insert(final IfMaterialCharacterTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		int newId = SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER);

		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "Insert into PIDB_IF_MATERIAL_CHARACTER( " +
				" MATERIAL_NUM, MATERIAL_TYPE, CH_TECH_NAME, CH_VALUE, SAP_STATUS," +
				" INFO_MESSAGE, TIME_STAMP, ID, RELEASED_BY,RELEASE_TO )  " +
				" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		logger.debug(sql);
		sjt.update(sql, newInstance.getMaterialNum(), newInstance
				.getMaterialType(), newInstance.getChTechName(), newInstance
				.getChValue(), newInstance.getSapStatus(), newInstance
				.getInfoMessage(), newInstance.getTimeStamp(), newId,
				newInstance.getReleasedBy(),newInstance.getReleaseTo());
	}

}