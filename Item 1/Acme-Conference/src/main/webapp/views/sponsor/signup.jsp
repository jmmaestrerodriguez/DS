<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="sponsor/save.do" modelAttribute="sponsor">
     
    
    <form:hidden path="id"/>
    <form:hidden path="version"/>     
	    
    <acme:textbox path="userAccountuser" code="sponsor.userAccount.username"/>
    <br/>
    <acme:textbox path="userAccountpassword" code="sponsor.userAccount.password"/>
    <br/>
    <acme:textbox path="name" code="sponsor.name"/>
    <br/>
    <acme:textbox path="middleName" code="sponsor.middleName"/>
    <br/>
    <acme:textbox path="surname" code="sponsor.surname"/>
    <br/>
    <acme:textbox path="photo" code="sponsor.photo"/>
    <br/>
    <acme:textbox path="email" code="sponsor.email"/>
    <br/>
    <acme:textbox path="phone" code="sponsor.phone"/>
    <br/>
    <acme:textbox path="address" code="sponsor.address"/>
    <br/>
    
    <jstl:if test="${not empty msgerror  }">
    	<h5 style="color: red;"><spring:message code="${msgerror}"/></h5>
    </jstl:if>
  
	
    <br/>
    <br/>


    <button name="save" type="submit" class="button2">
        <spring:message code="sponsor.save"/>
    </button>

    <input type="button" class="btn btn-danger" name="cancell"
           value="<spring:message code="sponsor.cancell" />"
           onclick="relativeRedir('sponsor/display.do?sponsorId=${sponsor.id}');"/>

</form:form>