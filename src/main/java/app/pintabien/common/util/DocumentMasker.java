package app.pintabien.common.util;

/** Enmascara numeros de documento para logs y snapshots: 44123445 -> 44****45. */
public final class DocumentMasker {
    private DocumentMasker() {}

    public static String mask(String document) {
        if (document == null) {
            return null;
        }
        String d = document.trim();
        if (d.length() <= 4) {
            return "*".repeat(d.length());
        }
        int middle = d.length() - 4;
        return d.substring(0, 2) + "*".repeat(middle) + d.substring(d.length() - 2);
    }
}
