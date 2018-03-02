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

import dhbwka.wwi.vertsys.javaee.kleinanzeigen.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.Anzeige;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.AnzeigePreistyp;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.AnzeigeArt;
import dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa.User;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = {"/app/useredit/"})
public class UserEditServlet extends HttpServlet {

    @EJB
    ValidationBean validationBean;

    @EJB
    UserBean userBean;

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // aktuellen User ermitteln
        User user_aktuell = this.userBean.getCurrentUser();
        request.setAttribute("user_aktuell", user_aktuell);

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/app/user_edit.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("login_form");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben auslesen
        request.setCharacterEncoding("utf-8");

        String username = request.getParameter("login_username");
        String user_firstname = request.getParameter("login_user_firstname");
        String user_lastname = request.getParameter("login_user_lastname");
        String user_phonenumber = request.getParameter("login_user_phonenumber");
        String user_adress = request.getParameter("login_user_adress");
        String user_email = request.getParameter("login_user_email");
        String user_city = request.getParameter("login_user_city");
        String user_postcode = request.getParameter("login_user_postcode");

        // Eingaben prüfen
        User user_aktuell = this.userBean.getCurrentUser();

        user_aktuell.setUser_firstname(user_firstname);
        user_aktuell.setUser_lastname(user_lastname);
        user_aktuell.setUser_city(user_city);
        user_aktuell.setUser_email(user_email);
        user_aktuell.setUser_psotcode(user_postcode);
        user_aktuell.setUser_phonenumber(user_phonenumber);
        user_aktuell.setUsername(username);

        List<String> errors = this.validationBean.validate(user_aktuell);

        //User aktualisieren, wenn alles richtig ist
        if (errors.isEmpty()) {
            this.userBean.update(user_aktuell);
            response.sendRedirect(WebUtils.appUrl(request, "/app/anzeigen/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("login_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }
}
