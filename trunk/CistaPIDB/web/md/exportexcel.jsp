<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% response.setHeader("Pragma","public");
 response.setHeader("Expires","0");
 response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
 response.setHeader("Content-Type","application/force-download");
 response.setHeader("Content-Type","application/vnd.ms-excel");
 response.setHeader("Content-Disposition","attachment;filename="+request.getParameter("FileName"));
 
 //modify by jere , 修正中文錯誤
 String var = new String(request.getParameter("ex").getBytes("iso8859-1"), "UTF-8");
 out.print(var);
 //out.print(request.getParameter("ex"));
 %>