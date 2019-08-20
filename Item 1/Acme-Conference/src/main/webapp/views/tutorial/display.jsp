<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<acme:showActivity activity="${tutorial}" lang="${lang}" />

<!-- Lista de sections dentro del tutorial -->
<jstl:out value="${lang}"/>
<display:table name="${tutorial.sections}" id="row"
	requestURI="section/list.do?tutorialId=${tutorial.id}" pagesize="5"
	class="displaytag">

	<display:column property="title" titleKey="tutorial.section.title" />

	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="section/edit.do?sectionId=${row.id}&tutorialId=${tutorial.id}"
				name="edit" code="tutorial.section.edit" />
		</security:authorize>
	</display:column>
	<display:column>
		<security:authorize access="hasRole('ADMIN')">
			<acme:button
				url="section/edit.do?sectionId=${row.id}&tutorialId=${tutorial.id}"
				name="edit" code="tutorial.section.edit" />
		</security:authorize>
	</display:column>
</display:table>
<security:authorize access="hasRole('ADMIN')">
	<acme:button url="section/create.do?tutorialId=${tutorial.id}"
		name="edit" code="tutorial.section.create" />
</security:authorize>

<acme:button url="tutorial/list.do?conferenceId=${conferenceId}" name="back" code="activity.back" />


