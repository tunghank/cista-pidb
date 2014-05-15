package com.cista.pidb.md.action;

import com.cista.pidb.core.BeanHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.core.mail.MailManager;
import com.cista.pidb.core.mail.MailTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SendMailDispatch {
    
    
    private static final List<String> CREATE_PARTITION = new ArrayList<String>();

    private static final List<String> MODIFY_PARTITION = new ArrayList<String>();

    private static final List<String> ERP_PARTITION = new ArrayList<String>();

    private static final String EMAIL_SPLITER = ",";

    static {
        CREATE_PARTITION.add("MD-1");
        CREATE_PARTITION.add("MD-4");
        CREATE_PARTITION.add("MD-9");
        CREATE_PARTITION.add("MD-13");

        MODIFY_PARTITION.add("MD-1");
        MODIFY_PARTITION.add("MD-4");
        MODIFY_PARTITION.add("MD-9");
        MODIFY_PARTITION.add("MD-13");

        ERP_PARTITION.add("MD-4");
        ERP_PARTITION.add("MD-9");
    }
    
    public static void sendMailDefault(String subject, String text, String assignEmail) {
        MailManager mailManager = PIDBContext.getPIDBMailManager();
        String[] emailList = assignEmail.split(EMAIL_SPLITER);
        MailTo mailTo = new MailTo();
        mailTo.setSubject(subject);
        mailTo.setText(text);
        mailTo.setAssignEmail(emailList);
        mailManager.place(mailTo);
    }
    
    public static void sendMailByCreate(String masterId, String key,
            String emails, String link) {
        String masterName = PIDBContext.getConfig(masterId);
        if (emails == null || emails.length() <= 0) {
            return;
        }
        String[] emailList = emails.split(EMAIL_SPLITER);
        MailManager mailManager = PIDBContext.getPIDBMailManager();
        MailTo mailTo = new MailTo();
        String subject = "";

        if (CREATE_PARTITION.contains(masterId)) {
            subject = masterName + key + "已完成儲存,請 PE/TE 維護相關資料後通知PM";
            
        } else {
            subject = masterName + key + "已完成儲存,請相關人員維護相關資料";
        }
        
        mailTo.setSubject(subject);
        mailTo.setText("請依以下Link進入維護 " + link);
        mailTo.setAssignEmail(emailList);
        mailManager.place(mailTo);
    }

    public static void sendMailByModify(String masterId, String key,
            String emails, String link) {
        String masterName = PIDBContext.getConfig(masterId);
        if (emails == null || emails.length() <= 0) {
            return;
        }
        String[] emailList = emails.split(EMAIL_SPLITER);
        MailManager mailManager = PIDBContext.getPIDBMailManager();
        MailTo mailTo = new MailTo();
        String subject = "";

        if (MODIFY_PARTITION.contains(masterId)) {
            subject = masterName + key + "已進行修改,請 PE/TE 確認相關資料後通知PM";
        } else {
            subject = masterName + key + "已進行修改,請相關人員確認相關資料";
        }
        mailTo.setSubject(subject);
        mailTo.setText("請依以下Link進入維護 " + link);
        mailTo.setAssignEmail(emailList);
        mailManager.place(mailTo);
    }

    public static void sendMailByErp(String masterId, String key,
            String emails, String link) {
        String masterName = PIDBContext.getConfig(masterId);
        if (emails == null || emails.length() <= 0) {
            return;
        }
        String[] emailList = emails.split(EMAIL_SPLITER);
        MailManager mailManager = PIDBContext.getPIDBMailManager();
        MailTo mailTo = new MailTo();
        String subject = "";

        if (ERP_PARTITION.contains(masterId)) {
            subject = masterName + key + "PM 已執行Release To ERP,請相關人員維護/確認Item master資料";
        } else {
            subject = masterName + key + "已執行Release To ERP,請相關人員確認相關Item Master資料";
            
        }
        mailTo.setSubject(subject);
        mailTo.setText(" ");
        mailTo.setAssignEmail(emailList);
        mailManager.place(mailTo);
    }
    
//    private static void sendMail() {
//        
//    }
    
    public static String getUrl (String masterId, String cp, Object o, Map<String, String>... ml) {
        String editUrl = PIDBContext.getConfig(masterId);
        //editUrl = "Kelvin Li and Kelvin Chan #areDDs# both working #in# Kelvin Chen's KelvinSoftShop company";
        StringBuffer sb = new StringBuffer();
        if (editUrl.indexOf("#") >= 0) {
            Pattern p = Pattern.compile("#[a-zA-Z]*?#");
            
            Matcher m = p.matcher(editUrl);
            
            boolean result = true;
            
            
            while (result) {
                result = m.find();
                
                if (result) {
                    String block = m.group();
                    block = block.replaceAll("#", "");
                    
                    String value = (String) BeanHelper.getPropertyValue(o, block);
                    if ((value == null || value.length() < 0) && ml != null) {
                        value = ml[0].get(block);
                    }
                    m.appendReplacement(sb, value);
                }
            }
            
            m.appendTail(sb);
            
        }
        
        return PIDBContext.getConfig("SERVER_HOST") + cp + sb.toString();
    }
    
    /*public static void main (String[] args) {
        SendMailDispatch.getUrl("", new Object());
        //sendMailByCreate("MD-1", "1100", "wangx@cavell.com.tw", "http://localhost:8080/PIDB/");
//      生成Pattern对象并且编译一个简单的正则表达式"Kelvin"
        Pattern p = Pattern.compile("#[a-z]*?[A-Z]*?#");
//        用Pattern类的matcher()方法生成一个Matcher对象
        Matcher m = p.matcher("Kelvin Li and Kelvin Chan #are# both working #in# Kelvin Chen's KelvinSoftShop company");
        StringBuffer sb = new StringBuffer();
        int i=0;
//        使用find()方法查找第一个匹配的对象
         
        boolean result = true;
//        使用循环将句子里所有的kelvin找出并替换再将内容加到sb里
        

      
        for (int j = 0; j < m.groupCount() ;j++) {
            System.out.println("j="+m.group(j));
        }
        while(result) {
        i++;
        result = m.find();
        //m.appendReplacement(sb, "Kevin");
        //System.out.println("第"+i+"次匹配后sb的内容是："+sb);
//        继续查找下一个匹配对象
        if (result) {
            //System.out.println("m.group()="+m.group());
            //System.out.println("m.groupCount()="+m.groupCount());
        }

        }
//        最后调用appendTail()方法将最后一次匹配后的剩余字符串加到sb里；
        //m.appendTail(sb);
        //System.out.println("调用m.appendTail(sb)后sb的最终内容是:"+ sb.toString());           
    }*/
}
