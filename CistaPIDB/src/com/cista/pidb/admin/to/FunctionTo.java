package com.cista.pidb.admin.to;

public class FunctionTo {
    private int id;
    private String funcName;
    private String uriPattern;
    private int position;
    private boolean isMenu;
    public String getFuncName() {
        return funcName;
    }
    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean getIsMenu() {
        return isMenu;
    }
    public void setIsMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public String getUriPattern() {
        return uriPattern;
    }
    public void setUriPattern(String uriPattern) {
        this.uriPattern = uriPattern;
    }
}
