<%-- 
    Document   : user_edit
    Created on : 01.03.2018, 10:38:46
    Author     : thoma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<template:base>
    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">

                    <%-- Eingabefelder --%>
                    <label for="login_username">
                        Benutzername:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_username" value="${user_aktuell.username}">
                    </div>

                    <label for="user_firstname">
                        Vorname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_firstname" value="${user_aktuell.user_firstname}">
                    </div>

                    <label for="user_lastname">
                        Nachname:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_lastname" value="${user_aktuell.user_lastname}">
                    </div>

                    <label for="user_email">
                        E-Mail-Adresse:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_email" value="${user_aktuelle.user_email}">
                    </div>

                    <label for="user_phonenumber">
                        Telefonnummer:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_phonenumber" value="${user_aktuell.user_phonenumber}">
                    </div>

                    <label for="user_adress">
                        Adresse:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_adress" value="${user_aktuell.user_adress}">
                    </div>

                    <label for="user_city">
                        Stadt:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_city" value="${user_aktuell.user_city}">
                    </div>

                    <label for="user_postcode">
                        PLZ:
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                        <input type="text" name="login_user_postcode" value="${user_aktuell.user_psotcode}">
                    </div>

                    <%-- Button zum Abschicken --%>
                    <div class="side-by-side">
                        <button class="icon-pencil" type="submit">
                            Speichern
                        </button>
                    </div>
                </div>

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty login_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${login_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
        </div>
    </jsp:attribute>
</template:base>

