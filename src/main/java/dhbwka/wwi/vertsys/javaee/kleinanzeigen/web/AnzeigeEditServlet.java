/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.kleinanzeigen.web;

import dhbwka.wwi.vertsys.javaee.kleinanzeigen.ejb.CategoryBean;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.ejb.AnzeigeBean;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.AnzeigePreistyp;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Anzeige.
 */
@WebServlet(urlPatterns = "/app/anzeige/*")
public class AnzeigeEditServlet extends HttpServlet {

    @EJB
    AnzeigeBean anzeigeBean;

    @EJB
    CategoryBean categoryBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Arten für die Suchfelder ermitteln
        request.setAttribute("categories", this.categoryBean.findAllSorted());
        request.setAttribute("statuses", AnzeigeArt.values());
        request.setAttribute("pricetypes", AnzeigePreistyp.values());

        Anzeige anzeigetaker = this.getRequestedAnzeigen(request);
        Object anzeige_ownerforlist = anzeigetaker.getOwner();
        request.setAttribute("owner_list", anzeige_ownerforlist);

        // Zu bearbeitende Anzeige einlesen
        HttpSession session = request.getSession();

        Anzeige anzeige = this.getRequestedAnzeigen(request);
        request.setAttribute("edit", anzeige.getId() != 0);

        if (session.getAttribute("anzeige_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("anzeige_form", this.createAnzeigeForm(anzeige));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/app/anzeige_edit.jsp").forward(request, response);

        session.removeAttribute("anzeige_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        request.setCharacterEncoding("utf-8");

        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "save":
                this.saveAnzeige(request, response);
                break;
            case "delete":
                this.deleteAnzeige(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Anzeige speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveAnzeige(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String anzeige_Category = request.getParameter("anzeige_category");
        String anzeige_createDate = request.getParameter("anzeige_due_date");
        String anzeige_createTime = request.getParameter("anzeige_due_time");
        String anzeige_Status = request.getParameter("anzeige_status");
        String anziege_price = request.getParameter("anzeige_price");
        String anzeige_pricetype = request.getParameter("anzeige_pricetype");
        String anzeige_ShortText = request.getParameter("anzeige_short_text");
        String anzeige_LongText = request.getParameter("anzeige_long_text");

        Anzeige anzeige = this.getRequestedAnzeigen(request);
        System.out.print(anzeige_Status);

        if (anzeige_Category != null && !anzeige_Category.trim().isEmpty()) {
            try {
                anzeige.setCategory(this.categoryBean.findById(Long.parseLong(anzeige_Category)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date anzeigecreateDate = WebUtils.parseDate(anzeige_createDate);
        Time anzeigecreateTime = WebUtils.parseTime(anzeige_createTime);

        if (anzeigecreateDate != null) {
            anzeige.setAnzeige_erstelle_Datum(anzeigecreateDate);
        } else {
            errors.add("Das Datum muss dem Format dd.mm.yyyy entsprechen.");
        }

        if (anzeigecreateTime != null) {
            anzeige.setAnzeige_erstelle_Zeit(anzeigecreateTime);
        } else {
            errors.add("Die Uhrzeit muss dem Format hh:mm:ss entsprechen.");
        }

        try {
            anzeige.setStatus(AnzeigeArt.valueOf(anzeige_Status));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        try {
            anzeige.setAnzeige_preistyp(AnzeigePreistyp.valueOf(anzeige_pricetype));
        } catch (IllegalArgumentException ex) {
            errors.add("Die ausgewählte Preisart ist nicht vorhanden.");
        }

        anzeige.setShortText(anzeige_ShortText);
        anzeige.setLongText(anzeige_LongText);
        anzeige.setAnzeige_preis(anziege_price);

        this.validationBean.validate(anzeige, errors);

        // aktuellen User ermitteln und mit Eigentümer der Anzeige vergleichen
        Anzeige anzeigetaker = this.getRequestedAnzeigen(request);
        String owner = anzeigetaker.getOwner().getUsername();
        String user_aktuell = this.userBean.getCurrentUser().getUsername();

        if (!owner.equals(user_aktuell)) {
            errors.add("Sie sind nicht der Eigentümer!");
        }

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.anzeigeBean.update(anzeige);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeigen/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("anzeige_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Anzeige löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteAnzeige(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        Anzeige anzeigetaker = this.getRequestedAnzeigen(request);
        String owner = anzeigetaker.getOwner().getUsername();
        String useduser = this.userBean.getCurrentUser().getUsername();

        if (!owner.equals(useduser)) {
            errors.add("Sie dürfen diesen Artikel nicht löschen! Sie sind nicht der Eigentümer!");
        }

        // Datensatz löschen
        if (errors.isEmpty()) {
            Anzeige anzeige = this.getRequestedAnzeigen(request);
            this.anzeigeBean.delete(anzeige);

            // Zurück zur Übersicht
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeige/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("anzeige_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Zu bearbeitende Anzeige aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Anzeige
     */
    private Anzeige getRequestedAnzeigen(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Anzeige anzeige = new Anzeige();
        anzeige.setOwner(this.userBean.getCurrentUser());
        anzeige.setAnzeige_erstelle_Datum(new Date(System.currentTimeMillis()));
        anzeige.setAnzeige_erstelle_Zeit(new Time(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String anzeigeId = request.getPathInfo();

        if (anzeigeId == null) {
            anzeigeId = "";
        }

        anzeigeId = anzeigeId.substring(1);

        if (anzeigeId.endsWith("/")) {
            anzeigeId = anzeigeId.substring(0, anzeigeId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            anzeige = this.anzeigeBean.findById(Long.parseLong(anzeigeId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return anzeige;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param anzeige Die zu bearbeitende Anzeige
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createAnzeigeForm(Anzeige anzeige) {
        Map<String, String[]> values = new HashMap<>();

        values.put("anzeige_owner", new String[]{
            anzeige.getOwner().getUsername()
        });

        if (anzeige.getCategory() != null) {
            values.put("anzeige_category", new String[]{
                anzeige.getCategory().toString()
            });
        }

        values.put("anzeige_due_date", new String[]{
            WebUtils.formatDate(anzeige.getAnzeige_erstelle_Datum())
        });

        values.put("anzeige_due_time", new String[]{
            WebUtils.formatTime(anzeige.getAnzeige_erstelle_Zeit())
        });

        values.put("anzeige_status", new String[]{
            anzeige.getStatus().toString()
        });

        values.put("anzeige_price", new String[]{
            anzeige.getAnzeige_preis()
        });

        values.put("anzeige_pricetype", new String[]{
            anzeige.getAnzeige_preistyp().toString()
        });

        values.put("anzeige_short_text", new String[]{
            anzeige.getShortText()
        });

        values.put("anzeige_long_text", new String[]{
            anzeige.getLongText()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
