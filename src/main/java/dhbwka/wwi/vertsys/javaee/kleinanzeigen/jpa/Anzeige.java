/*
 * Copyright © 2018 Dennis Schulmeister-Zimolong
 * 
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 * 
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.kleinanzeigen.jpa;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Eine Anzeige.
 */
@Entity
public class Anzeige implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "anzeige_ids")
    @TableGenerator(name = "anzeige_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @ManyToOne
    @NotNull(message = "Die Anzeige muss einem Benutzer geordnet werden.")
    private User owner;

    @ManyToOne
    private Category category;

    @Column(length = 50)
    @NotNull(message = "Die Bezeichnung darf nicht leer sein.")
    @Size(min = 1, max = 50, message = "Die Bezeichnung muss zwischen ein und 50 Zeichen lang sein.")
    private String shortText;

    @Lob
    @NotNull
    private String longText;

    @NotNull(message = "Das Datum darf nicht leer sein.")
    private Date anzeige_erstelle_Datum;

    @NotNull(message = "Die Uhrzeit darf nicht leer sein.")
    private Time anzeige_erstelle_Zeit;

    @NotNull(message = "hier eigene Nachricht erfinden")
    private String anzeige_preis;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AnzeigeArt status = AnzeigeArt.TASK;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AnzeigePreistyp anzeige_preistyp = AnzeigePreistyp.FIXED;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Anzeige() {
    }

    public Anzeige(User owner, Category category, String shortText, String longText,
            Date anzeige_erstelle_Datum, Time anzeige_erstelle_Zeit, String anzeige_preis) {

        this.owner = owner;
        this.category = category;
        this.shortText = shortText;
        this.longText = longText;
        this.anzeige_erstelle_Datum = anzeige_erstelle_Datum;
        this.anzeige_erstelle_Zeit = anzeige_erstelle_Zeit;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    //</editor-fold>
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public Date getAnzeige_erstelle_Datum() {
        return anzeige_erstelle_Datum;
    }

    public void setAnzeige_erstelle_Datum(Date anzeige_erstelle_Datum) {
        this.anzeige_erstelle_Datum = anzeige_erstelle_Datum;
    }

    public Time getAnzeige_erstelle_Zeit() {
        return anzeige_erstelle_Zeit;
    }

    public void setAnzeige_erstelle_Zeit(Time anzeige_erstelle_Zeit) {
        this.anzeige_erstelle_Zeit = anzeige_erstelle_Zeit;
    }

    public String getAnzeige_preis() {
        return anzeige_preis;
    }

    public void setAnzeige_preis(String anzeige_preis) {
        this.anzeige_preis = anzeige_preis;
    }

    public AnzeigeArt getStatus() {
        return status;
    }

    public void setStatus(AnzeigeArt status) {
        this.status = status;
    }

    public AnzeigePreistyp getAnzeige_preistyp() {
        return anzeige_preistyp;
    }

    public void setAnzeige_preistyp(AnzeigePreistyp anzeige_preistyp) {
        this.anzeige_preistyp = anzeige_preistyp;
    }

}
