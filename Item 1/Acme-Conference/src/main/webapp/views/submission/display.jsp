<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h3><spring:message code="submission.general.information" /></h3>

<acme:display code="submission.ticker" value="${submission.ticker}" />

<acme:display code="submission.moment" value="${submission.moment}" />



<jstl:choose>
	<jstl:when test="${lang eq 'es' }">
		<jstl:choose>
		<jstl:when test="${submission.status eq 'ACCEPTED' }">
			<acme:display code="submission.status" value="ACEPTADA" />
		</jstl:when>
		<jstl:when test="${submission.status eq 'REJECTED' }">
			<acme:display code="submission.status" value="RECHAZADA" />
		</jstl:when>
		<jstl:when test="${submission.status eq 'UNDER-REVIEWED' }">
			<acme:display code="submission.status" value="BAJO REVISI�N" />
		</jstl:when>
		</jstl:choose>
	</jstl:when>
	<jstl:otherwise>
		<acme:display code="submission.status" value="${submission.status}" />
	</jstl:otherwise>
</jstl:choose>

<acme:display code="submission.author" value="${submission.author.name}" />

<acme:display code="submission.conference" value="${submission.conference.title}" />

<br>
<br>

<h3><spring:message code="submission.review.paper" /></h3>

<acme:display code="submission.reviewPaper.title" value="${submission.reviewPaper.title}" />
<acme:display code="submission.reviewPaper.summary" value="${submission.reviewPaper.summary}" />
<acme:display code="submission.reviewPaper.document" value="${submission.reviewPaper.document}" url="${submission.reviewPaper.document}" />
<spring:message code="submission.reviewPaper.authors" />:
<ul>
	<jstl:forEach items="${submission.reviewPaper.authors}" var="author">
		<li><jstl:out value="${author}" /></li>
	</jstl:forEach>
</ul>

<br>
<br>

<h3><spring:message code="submission.camera.ready.paper" /></h3>

<jstl:if test="${not empty submission.cameraReadyPaper }">

<acme:display code="submission.cameraReadyPaper.title" value="${submission.cameraReadyPaper.title}" />
<acme:display code="submission.cameraReadyPaper.summary" value="${submission.cameraReadyPaper.summary}" />
<acme:display code="submission.cameraReadyPaper.document" value="${submission.cameraReadyPaper.document}" />
<spring:message code="submission.cameraReadyPaper.authors" />:
<ul>
	<jstl:forEach items="${submission.cameraReadyPaper.authors}" var="author">
		<li><jstl:out value="${author}" /></li>
	</jstl:forEach>
</ul>

<br>
<jstl:if test="${availableCameraReadyDeadline eq true }">
<acme:button url="submission/author/editPaper.do?submissionId=${submission.id }&paperId=${submission.cameraReadyPaper.id }" name="back" code="submission.edit.paper" />
</jstl:if>
</jstl:if>

<jstl:if test="${empty submission.cameraReadyPaper }">

<jstl:choose>
<jstl:when test="${availableSubmissionStatus eq false }">
<h4 style="color: red;"><spring:message code="submission.no.status" /></h4>
</jstl:when>
<jstl:when test="${availableSubmissionStatus eq true and availableCameraReadyDeadline eq false  }">
<h4 style="color: red;"><spring:message code="submission.elapsed.camera.deadline" /></h4>
</jstl:when>
<jstl:when test="${availableSubmissionStatus eq true and availableCameraReadyDeadline eq true }">
<acme:button url="submission/author/createPaper.do?submissionId=${submission.id }" name="back" code="submission.create.paper" />
</jstl:when>
</jstl:choose>


</jstl:if>
<br>
<br>
<br>

<jstl:choose>
	<jstl:when test="${isAdministrator eq true }">
		<acme:button url="submission/administrator/submissions.do" name="back" code="submission.back" />
		<br>
		<acme:button url="conference/administrator/display.do?conferenceId=${submission.conference.id }" name="back" code="submission.back.conference" />
	</jstl:when>
	<jstl:when test="${isAuthor eq true }">
		<acme:button url="submission/author/mySubmissions.do" name="back" code="submission.back" />
	</jstl:when>
</jstl:choose>
<security:authorize access="hasRole('AUTHOR')">
<jstl:if test="${submission.isNotified}">
	<acme:button code="submission.reports" name="list" url="report/author/listBySubmission.do?submissionId=${submission.id}"/>
</jstl:if>
</security:authorize>