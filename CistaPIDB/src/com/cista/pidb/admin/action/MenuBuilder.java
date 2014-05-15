package com.cista.pidb.admin.action;

import java.util.ArrayList;
import java.util.List;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.to.FunctionTo;

public class MenuBuilder {
    private String contextPath = "";
    
    public static final String FUNC_NAME_SPLITOR = "/";
    
    public static final String URI_PATTERN_SPLITOR = ";";

    // private static MenuBuilder mb = new MenuBuilder();

    public String buildMenu(String prefix, Integer userId) {
        StringBuilder sb = new StringBuilder();
        FunctionDao fd = new FunctionDao();

        if (prefix == null || prefix.equals("")) {
            List<String> ls = fd.findTopMenu(userId);

            for (String s : ls) {
                sb.append(buildMenu(s, userId));
            }
        } else {
            List<FunctionTo> ftl = fd.findMenuByPrefix(prefix + FUNC_NAME_SPLITOR, userId);

            if (ftl == null || ftl.size() <= 0) {
                List<FunctionTo> endFt = fd.findMenuByPrefix(prefix, userId);
                for (FunctionTo ft : endFt) {
                    String realPattern = ft.getUriPattern() != null
                            && ft.getUriPattern().indexOf(URI_PATTERN_SPLITOR) != -1 ? ft
                            .getUriPattern().split(URI_PATTERN_SPLITOR)[0] : ft.getUriPattern();
                    sb.append("<li><a href=\"" + contextPath + "/"
                            + realPattern + "\">"
                            + prefix.substring(prefix.lastIndexOf(FUNC_NAME_SPLITOR) + 1)
                            + "</a></li>\r\n");
                }
            } else {
                List<String> subMenus = new ArrayList<String>();
                for (FunctionTo ft : ftl) {
                    String funcName = ft.getFuncName();
                    String newPrefix = "";
                    if ((funcName.substring(prefix.length() + 1)).indexOf(FUNC_NAME_SPLITOR) != -1) {
                        newPrefix = funcName.substring(0, funcName.indexOf(FUNC_NAME_SPLITOR,
                                (prefix + FUNC_NAME_SPLITOR).length()));
                    } else {
                        newPrefix = funcName;
                    }

                    if (!subMenus.contains(newPrefix)) {
                        subMenus.add(newPrefix);
                    }
                }

                sb.append("<li>");
                
                String tempPrefix = prefix.lastIndexOf(FUNC_NAME_SPLITOR) != -1 ? prefix.substring(prefix
                        .lastIndexOf(FUNC_NAME_SPLITOR) + 1) : prefix;
                
                sb.append(tempPrefix);
                sb.append("<ul>\r\n");
                for (String sub : subMenus) {
                    sb.append(buildMenu(sub, userId));
                }
                sb.append("</ul>");
                sb.append("</li>\r\n");

            }
        }

        return sb.toString();
    }

    public void setContextPath(String cp) {
        this.contextPath = cp;
    }

    public String getContextPath() {
        return this.contextPath;
    }
    
    public static void main (String[] args) {
    }
}
