package com.cista.pidb.md.dao;

import java.util.List;

import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IfMaterialMasterTo;

public class IfMaterialMasterDao extends PIDBDaoSupport {
    public void batchInsert (List<IfMaterialMasterTo> list, String tableName) {
        if (list != null) {
            for (IfMaterialMasterTo to : list) {
                super.insert(to, tableName);
            }
        }
    }    
}
