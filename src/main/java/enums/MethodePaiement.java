package enums;

public enum MethodePaiement {
    CARTE("carte"),
    ESPECE("espèce");

    private final String libelle;

    MethodePaiement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static String[] getAllLibelles() {
        MethodePaiement[] values = MethodePaiement.values();
        String[] libelles = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            libelles[i] = values[i].getLibelle();
        }

        return libelles;
    }

    public static MethodePaiement fromLibelle(String libelle) {
        for (MethodePaiement method : MethodePaiement.values()) {
            if (method.getLibelle().equals(libelle)) {
                return method;
            }
        }
        return CARTE; // Default value
    }
}
