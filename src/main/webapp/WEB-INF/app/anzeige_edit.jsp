<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        <c:choose>
            <c:when test="${edit}">
                Anzeige bearbeiten
            </c:when>
            <c:otherwise>
                Anzeige anlegen
            </c:otherwise>
        </c:choose>
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/anzeige_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/anzeigen/"/>">Übersicht</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="anzeige_owner">Eigentümer:</label>
                <div class="side-by-side">
                    <input type="text" name="anzeige_owner" value="${anzeige_form.values["anzeige_owner"][0]}" readonly="readonly">
                </div>

                <div>
                    <ul>
                        <li>
                            <b> Vorname: </b> ${owner_list.user_firstname}
                        </li>
                        <li>
                            <b> Nachname: </b> ${owner_list.user_lastname}
                        </li>
                        <li>
                            <b> Adresse: </b> ${owner_list.user_adress}
                        </li>
                        <li>
                            <b> Stadt: </b> ${owner_list.user_city}
                        </li>
                        <li>
                            <b> PLZ: </b> ${owner_list.user_psotcode}
                        </li>
                        <li>
                            <b> Telefonnummer: </b> ${owner_list.user_phonenumber}
                        </li>
                        <li>
                            <b> E-Mail: </b> ${owner_list.user_email}
                        </li>
                    </ul>

                </div>


                <label for="anzeige_category">Kategorie:</label>
                <div class="side-by-side">
                    <select name="anzeige_category">
                        <option value="">Keine Kategorie</option>

                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}" ${anzeige_form.values["anzeige_category"][0] == category.id ? 'selected' : ''}>
                                <c:out value="${category.name}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="anzeige_due_date">
                    Erstellt am:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="anzeige_due_date" value="${anzeige_form.values["anzeige_due_date"][0]}">
                    <input type="text" name="anzeige_due_time" value="${anzeige_form.values["anzeige_due_time"][0]}">
                </div>

                <label for="anzeige_status">
                    Anzeigenstatus:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="anzeige_status">
                        <c:forEach items="${statuses}" var="status">
                            <option value="${status}" ${anzeige_form.values["anzeige_status"][0] == status ? 'selected' : ''}>
                                <c:out value="${status.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="anzeige_price">
                    Preis:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="anzeige_price" value="${anzeige_form.values["anzeige_price"][0]}">
                </div>

                <label for="anzeige_pricetype">
                    Preisart:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side margin">
                    <select name="anzeige_pricetype">
                        <c:forEach items="${pricetypes}" var="pricetype">
                            <option value="${pricetype}" ${anzeige_form.values["anzeige_pricetype"][0] == pricetype ? 'selected' : ''}>
                                <c:out value="${pricetype.label}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="anzeige_short_text">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input type="text" name="anzeige_short_text" value="${anzeige_form.values["anzeige_short_text"][0]}">
                </div>

                <label for="anzeige_long_text">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="anzeige_long_text"><c:out value="${anzeige_form.values['anzeige_long_text'][0]}"/></textarea>
                </div>

                <%-- Button zum Abschicken --%>
                <div class="side-by-side">
                    <button class="icon-pencil" type="submit" name="action" value="save">
                        Sichern
                    </button>

                    <c:if test="${edit}">
                        <button class="icon-trash" type="submit" name="action" value="delete">
                            Löschen
                        </button>
                    </c:if>
                </div>
            </div>

            <%-- Fehlermeldungen --%>
            <c:if test="${!empty anzeige_form.errors}">
                <ul class="errors">
                    <c:forEach items="${anzeige_form.errors}" var="error">
                        <li>${error}</li>
                        </c:forEach>
                </ul>
            </c:if>
        </form>
    </jsp:attribute>
</template:base>