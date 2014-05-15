package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.to.PidbAPLTo;

public class PidbAplDao extends PIDBDaoSupport {

	public void batchInsert(List<PidbAPLTo> list, String tableName) {
		if (list != null) {
			for (PidbAPLTo to : list) {
				super.insert(to, tableName);
			}
		}
	}

	public void insert(final PidbAPLTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}

		int newId = SequenceSupport.nextValue(SequenceSupport.SEQ_PIDB_APL);

		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "insert into PIDB_ASL (MATERIAL_NUM, CUSTOMER, VALID_FROM, VALID_TO,"
				+ "SAP_STATUS, INFO_MESSAGE, TIME_STAMP, RELEASED_BY, "
				+ "RELEASE_TO) values(?, ?, ?, ?, ?, ?, ?, ?, ? )";
		logger.debug(sql);
		System.out.println(newInstance.getMaterialNum());
		System.out.println(newInstance.getCustomer());
		System.out.println(newInstance.getValidFrom());
		System.out.println(newInstance.getValidTo());
		System.out.println(newInstance.getSapStatus());
		System.out.println(newInstance.getInfoMessage());
		System.out.println(newInstance.getTimeStamp());
		System.out.println(newInstance.getReleasedBy());

		sjt.update(sql, newId, newInstance.getMaterialNum(), newInstance
				.getCustomer(), newInstance.getValidFrom(), newInstance
				.getValidTo(), newInstance.getSapStatus(), newInstance
				.getInfoMessage(), newInstance.getTimeStamp(), newInstance
				.getReleasedBy(),newInstance.getReleaseTo());
	}

}