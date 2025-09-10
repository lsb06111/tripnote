<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/views/jspf/head.jspf" %>

</head>
<body class="index-page">
  
<%@ include file="/WEB-INF/views/jspf/header.jspf" %>
  <main class="main">
	
    <!-- Hero Section -->
    <%@ include file="/WEB-INF/views/jspf/main/welcome.jspf" %>

    <%-- <%@ include file="/WEB-INF/views/jspf/serviceinfo.jspf" %> --%>
	
    <%@ include file="/WEB-INF/views/jspf/main/featureinfo.jspf" %> 

	<%@ include file="/WEB-INF/views/jspf/main/steps.jspf" %>
	
    <%@ include file="/WEB-INF/views/jspf/main/preview.jspf" %> 


    <%@ include file="/WEB-INF/views/jspf/main/faq.jspf" %> 

    
	<%-- <%@ include file="/WEB-INF/views/jspf/teamcards.jspf" %> --%>

  </main>

<%@ include file="/WEB-INF/views/jspf/footer.jspf" %>

</body>

</html>