<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "application/json");
	
	String rootPath = application.getRealPath( "/" );
	System.out.println("++++++++++++++++++"+rootPath);

//	out.write( new ActionEnter( request, rootPath ).exec() );
	PrintWriter writer = response.getWriter();
	writer.write(new ActionEnter( request, rootPath ).exec());
	writer.flush();
	writer.close();
	
%>