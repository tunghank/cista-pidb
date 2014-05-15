package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.BumpMaskQueryTo;
import com.cista.pidb.md.to.BumpMaskTo;

/**
 * Data access object for table PIDB_BUMPING_MASK.
 * @author Hu Meixia
 */
public class BumpMaskDao extends PIDBDaoSupport {

    /**
     * Find all bumpMasks in the table.
     * @return List of BumpMaskTo
     */
    public List<BumpMaskTo> findAll() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_BUMPING_MASK order by MASK_NAME";
        logger.debug(sql);
        return sjt.query(sql, new GenericRowMapper<BumpMaskTo>(BumpMaskTo.class),
                new Object[] {});
    }

    /**
     * .
     * @param maskName String
     * @return BumpMaskTo
     */
    public BumpMaskTo find(final String maskName) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_BUMPING_MASK where MASK_NAME = ?";
        GenericRowMapper<BumpMaskTo> rm = new GenericRowMapper<BumpMaskTo>(BumpMaskTo.class);
        List<BumpMaskTo> result = sjt.query(sql, rm, new Object[]{maskName});
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    /**
     * .
     * @param queryTo BumpMaskQueryTo
     * @return int
     */
    public int countResult(final BumpMaskQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "";
            sql = "select count(*) from ("
            + " select distinct"
            + " p.MASK_NAME,"
            + " p.PROJ_CODE,"
            + " pd.PROJ_NAME,"
            + " pd.PROJ_OPTION,"
            + " pj.FAB,"
            + " pb.FAB_DESCR"
            + " from PIDB_BUMPING_MASK p left join (PIDB_PROJECT_CODE pd left join PIDB_PROJECT pj "
                 + "on pd.PROJ_NAME=pj.PROJ_NAME) on p.PROJ_CODE = pd.PROJ_CODE,T_FAB_CODE ,T_fab_code pb where pj.fab=pb.fab";
//        }
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause + ")";
        return sjt.queryForInt(sql, new Object[]{});
    }

    /**
     * .
     * @param queryTo BumpMaskQueryTo
     * @return List
     */
    public List<BumpMaskQueryTo> query(final BumpMaskQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "";
            sql = " select distinct"
                + " p.PROJ_CODE,"
                + " p.MASK_NAME,"
                + " pd.PROJ_NAME,"
                + " pd.PROJ_OPTION,"
                + " pj.FAB,"
                + " pb.FAB_DESCR"
                + " from PIDB_BUMPING_MASK p left join (PIDB_PROJECT_CODE pd left join PIDB_PROJECT pj"
                       + " on pd.PROJ_NAME=pj.PROJ_NAME) on p.PROJ_CODE = pd.PROJ_CODE ,T_fab_code pb where pj.fab=pb.fab ";

        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        sql += " order by pd.PROJ_NAME, p.MASK_NAME";

        GenericRowMapper<BumpMaskQueryTo> rm = new GenericRowMapper<BumpMaskQueryTo>(BumpMaskQueryTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }
    
    public List<BumpMaskTo> queryTo(final BumpMaskQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "";
            sql = " select distinct"
                + " p.MASK_NAME,"
                + " p.PROJ_CODE,"
                + " p.RDL,"
                + " p.PI,"
                + " p.BUMP_HEIGHT,"
                + " p.BUMP_HARDNESS,"
                + " p.BUMP_HOUSE1,"
                + " p.BUMP_HOUSE1_MRDP,"
                + " p.BUMP_HOUSE2,"
                + " p.BUMP_HOUSE2_MRDP,"
                + " p.BUMP_HOUSE3,"
                + " p.BUMP_HOUSE3_MRDP,"
                + " p.BUMP_HOUSE4,"
                + " p.BUMP_HOUSE4_MRDP,"
                + " p.BUMP_HOUSE5,"
                + " p.BUMP_HOUSE5_MRDP,"
                + " p.REMARK,"
                + " p.ASSIGN_TO,"
                + " p.ASSIGN_EMAIL,"
                + " p.CREATED_BY,"
                + " p.MODIFIED_BY,"
                + " pd.PROJ_NAME,"
                + " pd.PROJ_OPTION,"
                + " pj.FAB,"
                + " pb.FAB_DESCR"
                + " from PIDB_BUMPING_MASK p left join (PIDB_PROJECT_CODE pd left join PIDB_PROJECT pj"
                       + " on pd.PROJ_NAME=pj.PROJ_NAME) on p.PROJ_CODE = pd.PROJ_CODE ,T_fab_code pb where pj.fab=pb.fab ";

        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        sql += " order by pd.PROJ_NAME, p.MASK_NAME";

        GenericRowMapper<BumpMaskTo> rm = new GenericRowMapper<BumpMaskTo>(BumpMaskTo.class);
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});
        }
    }

    /**
     * .
     * @param queryTo BumpMaskQueryTo
     * @return String
     */
    private String generateWhereCause(final BumpMaskQueryTo queryTo) {
        StringBuilder sb = new StringBuilder();
        if (queryTo.getMaskName() != null && !queryTo.getMaskName().equals("")) {
            String queryString = getSmartSearchQueryString("p.MASK_NAME", queryTo.getMaskName());
            if (queryString != null) {
                sb.append(" and (" + queryString + " )");
            }
        }

        if (queryTo.getProjName() != null && !"".equals(queryTo.getProjName().trim())) {
            String queryString = getSmartSearchQueryString("pd.PROJ_NAME", queryTo.getProjName());
            if (queryString != null) {
                sb.append(" and (" + queryString + " )");
            }
        }

        if (queryTo.getFab() != null && !queryTo.getFab().equals("")) {
        	sb.append(" and pj.FAB = " + getSQLString(queryTo.getFab()) + " ");
        }

        if (queryTo.getProjOption() != null && !queryTo.getProjOption().equals("")) {
            String queryString = getSmartSearchQueryString("pd.PROJ_OPTION", queryTo.getProjOption());
            if (queryString != null) {
                sb.append(" and (" + queryString + " )");
            }
        }


        return sb.toString();
    }
    
    public List<BumpMaskTo> getByProjCode(final String projCode) {
    	SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_BUMPING_MASK where PROJ_CODE = ?";
        GenericRowMapper<BumpMaskTo> rm = new GenericRowMapper<BumpMaskTo>(BumpMaskTo.class);
        return sjt.query(sql, rm, new Object[]{projCode});       
    }
    
    public BumpMaskTo findByPrimaryKey(final String maskName, final String projCode) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_BUMPING_MASK where MASK_NAME = ? and PROJ_CODE = ?";
        GenericRowMapper<BumpMaskTo> rm = new GenericRowMapper<BumpMaskTo>(BumpMaskTo.class);
        List<BumpMaskTo> result = sjt.query(sql, rm, new Object[]{maskName,projCode});
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }    
}
