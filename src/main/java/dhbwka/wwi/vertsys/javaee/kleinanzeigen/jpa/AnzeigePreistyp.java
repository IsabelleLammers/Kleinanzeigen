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

public enum AnzeigePreistyp {
    FIXED, OPTION;

    /**
     * Type des Preises ermitteln
     *
     * @return Preistyp
     */
    public String getLabel() {
        switch (this) {
            case FIXED:
                return "Festpreis";
            case OPTION:
                return "Verhandlungsbasis";
            default:
                return this.toString();
        }
    }
}
