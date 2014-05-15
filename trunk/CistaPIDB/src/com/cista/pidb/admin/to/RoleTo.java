package com.cista.pidb.admin.to;

import java.util.ArrayList;
import java.util.List;

/**
 * database table Role.
 * @author Administrator
 *
 */
public class RoleTo {

    /**
     * table fields.
     */
    private int id;
    private String roleName;
    private List<FunctionTo> functions;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param _id the id to set
     */
    public void setId(final int _id) {
        this.id = _id;
    }

    /**
     * @return the name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * @param _roleName the name to set
     */
    public void setRoleName(final String _roleName) {
        this.roleName = _roleName;
    }

    public List<FunctionTo> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FunctionTo> functions) {
        this.functions = functions;
    }
    
    public void addFunction(FunctionTo function) {
        if (functions == null) {
            functions = new ArrayList<FunctionTo>();
        }
        functions.add(function);
    }
}
