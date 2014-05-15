package com.cista.pidb.admin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cista.pidb.admin.to.FunctionTo;

public class FunctionTable {
    public static String draw(List<FunctionTo> all, List<FunctionTo> checkedList) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1' cellpadding='0' cellspacing='0'>");

        Map<Integer, Integer> depth = new HashMap<Integer, Integer>();

        int maxDepth = 0;
        for (FunctionTo f : all) {
            int dep = countSpilter(f.getFuncName(), '/') + 1;
            depth.put(f.getId(), dep);
            if (dep > maxDepth) {
               maxDepth = dep;
            }
        }

        for (FunctionTo f : all) {
            sb.append("<tr>\r\n");
            int dep = depth.get(f.getId());
            String[] nodes = f.getFuncName().split("/");
            String lastNode = "";
            for (int i = 0; i < dep; i++) {
                String currentNode = lastNode + nodes[i];

                if (!duplicatedItem(all, f, currentNode)) {
                    int rowSpan = countSameHeader(all, f, currentNode);
                    int colSpan = 1;
                    if (i == dep - 1) {
                        colSpan = maxDepth - dep + 1;
                    }
                    sb.append("\t<td rowSpan=\"" + rowSpan + "\" colSpan=\""
                            + colSpan + "\" style='vertical-align: middle;'>"
                            + currentNode
                            + "</td>\r\n");
                }
                lastNode = currentNode + "/";
            }

            if (checkedList != null) {
                boolean checked = false;
                for (FunctionTo cf : checkedList) {
                    if (cf.getId() == f.getId()) {
                        checked = true;
                        break;
                    }
                }
                sb.append("<td><input type='checkbox' name='functions' id='functions' value='" + f.getId() + "' ");
                if (checked) {
                     sb.append("checked");
                }
                sb.append("></td>");
            }
            sb.append("</tr>\r\n");
        }
        sb.append("</table>");
        return sb.toString();
    }

    private static int countSpilter(String src, char d) {
        int c = 0;
        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == d) {
                c++;
            }
        }

        return c;
    }

    private static boolean duplicatedItem(List<FunctionTo> all, FunctionTo c, String header) {
        for (FunctionTo f : all) {
            if (f == c) {
                break;
            }

            if (f.getFuncName().startsWith(header + "/")) {
                return true;
            }
        }

        return false;
    }

    private static int countSameHeader(List<FunctionTo> all, FunctionTo c, String header) {
        boolean startCount = false;
        int result = 0;
        for (FunctionTo f : all) {
            if (f == c) {
                startCount = true;
                result++;
                continue;
            }

            if (startCount) {
                if (f.getFuncName().startsWith(header + "/")) {
                    result++;
                }
            }
        }

        return result;
    }
}
