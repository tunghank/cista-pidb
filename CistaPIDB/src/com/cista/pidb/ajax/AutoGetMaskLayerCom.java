package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.ProjectCodeTo;

public class AutoGetMaskLayerCom extends Action {
    /*public static void main (String[] args) {
        //String projCode = arg2.getParameter("projCode");
        String projCode = "PA1110-01";
        //PrintWriter out = arg3.getWriter();
//        if (projCode == null || projCode.length() <= 0) {
//            out.print("");
//            return null;
//        }

        IcWaferDao icWaferDao = new IcWaferDao();
        ProjectCodeDao projectCodeDao = new ProjectCodeDao();
        ProjectCodeTo projCodeTo = projectCodeDao.findByProjectCode(projCode);
//        if (projCodeTo == null) {
//            out.print("");
//            return null;
//        }
        // create Mask_Layer_COM
        String noBarString = "";
        String barString = "";
        List<String> projCodeMList = projectCodeDao.findProjCode(projCodeTo
                .getProjName());
        if (projCodeMList != null && projCodeMList.size() > 0) {
            List<String> noBarResultList = new ArrayList<String>();

            for (String temp : projCodeMList) {
                if (temp != null) {
                    // 取没有-的maskLayer
                    List<String> maskLayComList = icWaferDao
                            .findMaskLayComByProjCode(temp);
                    if (maskLayComList != null && maskLayComList.size() > 0) {
                        for (String addTemp : maskLayComList) {
                            if (!noBarResultList.contains(addTemp)) {
                                noBarResultList.add(addTemp);
                            }
                        }
                    }
                }
            }

            noBarString = fetchMaskLayer(noBarResultList);
        }

        List<String> barTempList = icWaferDao.findMaskLayComByProjCode(
                projCode, "-");
        
        if (barTempList != null && barTempList.size() > 0) {
            barString = fetchMaskLayer(barTempList);
        }
        
        System.out.print(noBarString + "/" + barString);

    }*/
    
    private static String fetchMaskLayer (List<String> resultList) {
        String retString = "";
        if (resultList != null && resultList.size() > 0) {

            Map<String, List<String>> noBarMap = new HashMap<String, List<String>>();
            for (String temp : resultList) {
                String key = temp.length() + "";
                if (noBarMap.containsKey(key)) {
                    noBarMap.get(key).add(temp);
                } else {
                    List<String> l = new ArrayList<String>();
                    l.add(temp);
                    noBarMap.put(key, l);
                }
            }

            List<Map<String, String>> noBarList = new ArrayList<Map<String, String>>();
            for (String k : noBarMap.keySet()) {
                Map<String, String> map = new HashMap<String, String>();
                List<String> l = noBarMap.get(k);
                for (String s : l) {
                    
                    String tempKey = s.substring(0, 3)
                            + (s.length() > 4 ? s.substring(4) : "");
                    tempKey = tempKey.toLowerCase();
                    
                    if (map.containsKey(tempKey)) {
                        char tempChar = (map.get(tempKey).toLowerCase()).charAt(3);
                        
                        if (tempChar < s.toLowerCase().charAt(3)) {
                            map.put(tempKey, s);
                        }
                    } else {
                        map.put(tempKey, s);
                    }
                }
                
                noBarList.add(map);
            }

            for (Map<String, String> m : noBarList) {
                for (String k : m.keySet()) {
                    retString += "/" + m.get(k);
                }
            }
            
            if (retString.length() > 0) {
                retString = retString.substring(1);
            }
        }        
        return retString;
    }
    
    public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        String projCode = arg2.getParameter("projCode");
        PrintWriter out = arg3.getWriter();
        if (projCode == null || projCode.length() <= 0) {
            out.print("");
            return null;
        }

        IcWaferDao icWaferDao = new IcWaferDao();
        ProjectCodeDao projectCodeDao = new ProjectCodeDao();
        ProjectCodeTo projCodeTo = projectCodeDao.findByProjectCode(projCode);
        if (projCodeTo == null) {
            out.print("");
            return null;
        }
        // create Mask_Layer_COM
        String noBarString = "";
        String barString = "";
        List<String> projCodeMList = projectCodeDao.findProjCode(projCodeTo
                .getProjName());
        if (projCodeMList != null && projCodeMList.size() > 0) {
            List<String> noBarResultList = new ArrayList<String>();

            for (String temp : projCodeMList) {
                if (temp != null) {
                    // 取没有-的maskLayer
                    List<String> maskLayComList = icWaferDao
                            .findMaskLayComByProjCode(temp);
                    if (maskLayComList != null && maskLayComList.size() > 0) {
                        for (String addTemp : maskLayComList) {
                            if (!noBarResultList.contains(addTemp)) {
                                noBarResultList.add(addTemp);
                            }
                        }
                    }
                }
            }

            noBarString = fetchMaskLayer(noBarResultList);
        }

        List<String> barTempList = icWaferDao.findMaskLayComByProjCode(
                projCode, "-");
        
        if (barTempList != null && barTempList.size() > 0) {
            barString = fetchMaskLayer(barTempList);
        }
        
        if (StringUtils.isEmpty(noBarString) && StringUtils.isEmpty(barString)) {
            out.print("");
            return null;            
        }
        
        out.print(noBarString + "/" + barString);

        return null;
    }

    /*public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {

        String projCode = arg2.getParameter("projCode");
        PrintWriter out = arg3.getWriter();
        if (projCode == null || projCode.length() <= 0) {
            out.print("");
            return null;
        }

        IcWaferDao icWaferDao = new IcWaferDao();
        ProjectCodeDao projectCodeDao = new ProjectCodeDao();
        ProjectCodeTo projCodeTo = projectCodeDao.findByProjectCode(projCode);
        if (projCodeTo == null) {
            out.print("");
            return null;
        }
        // create Mask_Layer_COM
        String fourString = "";
        String nineString = "";
        List<String> projCodeMList = projectCodeDao.findProjCode(projCodeTo
                .getProjName());
        if (projCodeMList != null && projCodeMList.size() > 0) {
            List<String> fourResultList = new ArrayList<String>();

            for (String temp : projCodeMList) {
                if (temp != null) {
                    List<String> maskLayComList = icWaferDao
                            .findMaskLayComByProjCode(temp, 4);
                    if (maskLayComList != null && maskLayComList.size() > 0) {
                        for (String addTemp : maskLayComList) {
                            if (!fourResultList.contains(addTemp)) {
                                fourResultList.add(addTemp);
                            }
                        }
                    }
                }
            }

            if (fourResultList != null && fourResultList.size() > 0) {
                Map<String, Object> map = new HashMap<String, Object>();
                List<String> keyList = new ArrayList<String>();
                for (String temp : fourResultList) {
                    String a = (String) map.get(temp.substring(0, 3));
                    if (a == null) {
                        map.put(temp.substring(0, 3), temp.substring(3));
                        keyList.add(temp.substring(0, 3));
                    } else {
                        if (a.compareToIgnoreCase(temp.substring(3)) < 0) {
                            map.put(temp.substring(0, 3), temp.substring(3));
                        }
                    }
                }
                String midTemp;
                for (int i = 0; i < keyList.size() - 1; i++) {
                    for (int j = i + 1; j < keyList.size(); j++) {
                        if (keyList.get(i).compareToIgnoreCase(keyList.get(j)) < 0) {
                            midTemp = keyList.get(i);
                            keyList.set(i, keyList.get(j));
                            keyList.set(j, midTemp);
                        }
                    }
                }
                fourString = keyList.get(0) + (String) map.get(keyList.get(0));
                for (int i = 1; i < keyList.size(); i++) {
                    fourString += "/" + keyList.get(i)
                            + map.get(keyList.get(i));
                }
            }
        }

        List<String> nineTempList = icWaferDao.findMaskLayComByProjCode(
                projCode, 9);

        if (nineTempList != null && nineTempList.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<String> keyList = new ArrayList<String>();
            for (String temp : nineTempList) {
                String a = (String) map.get(temp.substring(0, 3)
                        + temp.substring(4, 9));
                if (a == null) {
                    map.put(temp.substring(0, 3) + temp.substring(4, 9), temp
                            .substring(3, 4));
                    keyList.add(temp.substring(0, 3) + temp.substring(4, 9));
                } else {
                    if (a.compareToIgnoreCase(temp.substring(3)) < 0) {
                        map.put(temp.substring(0, 3) + temp.substring(4, 9),
                                temp.substring(3, 4));
                    }
                }
            }
            String midTemp;
            for (int i = 0; i < keyList.size() - 1; i++) {
                for (int j = i + 1; j < keyList.size(); j++) {
                    if (keyList.get(i).compareToIgnoreCase(keyList.get(j)) < 0) {
                        midTemp = keyList.get(i);
                        keyList.set(i, keyList.get(j));
                        keyList.set(j, midTemp);
                    }
                }
            }
            nineString = keyList.get(0) + (String) map.get(keyList.get(0));
            for (int i = 1; i < keyList.size(); i++) {
                nineString += "/" + keyList.get(i).substring(0, 3)
                        + map.get(keyList.get(i))
                        + keyList.get(i).substring(3, 8);
            }
        }

        String resultString = "";
        resultString = fourString == null ? "" : fourString;
        if (nineString != null && nineString.length() > 0) {
            resultString = resultString + "/" + nineString;
        }
        out.print(resultString);
        // if (fourString != null && fourString.length() > 0) {
        // if (nineString != null && nineString.length() > 0) {
        // fourString += "/" + nineString;
        // out.print("fourString");
        // return null;
        // }
        //        	
        // } else {
        // if (nineString != null && nineString.length() > 0) {
        // out.print("nineString");
        // return null;
        // }
        // }
        return null;
    }*/
}
