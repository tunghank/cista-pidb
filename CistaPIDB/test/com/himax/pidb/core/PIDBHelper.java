package com.himax.pidb.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PIDBHelper {

    static Map<String, String> fields = new HashMap<String, String>();

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            String newKeyin = br.readLine();
            if(newKeyin.equals("!")) {
                printFields("\r\n");
            } else if(newKeyin.equals("#")) {
            	printMembers();
            } else if(newKeyin.equals("\r\n") || newKeyin.trim().equals("")) {
                continue;
            } else if(newKeyin.equals("~")) {
                System.out.println("System exit!");
                System.exit(0);
            } else if(newKeyin.equals("@")) {
                System.out.println("Buffer cleared!");
                fields = new HashMap<String, String>();
            } else {
                newKeyin = newKeyin.trim().toUpperCase();
                String[] spilited = null;
                if(newKeyin.indexOf(",")>0) {
                    spilited = newKeyin.split(",");
                } else if(newKeyin.indexOf("\r\n")>0) {
                    spilited = newKeyin.split("\r\n");
                } else if(newKeyin.indexOf("\t")>0) {
                    spilited = newKeyin.split("\t");
                }

                if(spilited!=null) {
                    for(int i=0; i<spilited.length; i++) {
                        //fields.put(spilited[i].trim(), spilited[i].trim());
                        put(spilited[i].trim());
                    }
                } else {
                    //fields.put(newKeyin, newKeyin);
                    put(newKeyin.trim());
                }

            }
        }
    }

    private static void put(String src) {
//        if (src != null) {
//            String[] sp = src.split("_");
//            for(String s : sp) {
//                fields.put(s, s);
//            }
//        }
    	fields.put(src.trim(), src.trim());
    }
    public static void printFields(String dim) {
        List<String> tempList = new ArrayList<String>();
        Iterator iter = fields.keySet().iterator();
        while(iter.hasNext()) {
            tempList.add(iter.next().toString());
        }

        Collections.sort(tempList);
        for(int i=0; i<tempList.size(); i++) {
            System.out.print(tempList.get(i));
            System.out.print(dim);
        }
        System.out.println();
    }

    public static void printMembers() {
    	NameConverter d = new DefaultNameConverter();
    	for(String s : fields.keySet()) {
    		String memberName = d.db2java(s);
    		System.out.println("private String " + memberName + ";");
    	}
    }

}
