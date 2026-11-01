package app.pintabien.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

/**
 * Hash SHA-256 (hex) del documento, usado en claves de cache y en registros de consultas.
 * Nunca se persiste ni se loguea el DNI en claro.
 */
public final class DocumentHasher {
    private DocumentHasher() {}

    public static String sha256Hex(String... parts) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            for (String p : parts) {
                md.update((p == null ? "" : p.trim().toUpperCase(Locale.ROOT)).getBytes(StandardCharsets.UTF_8));
                md.update((byte) 0x1F);
            }
            return HexFormat.of().formatHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    /** Hash estable por (proveedor + tipo + numero de documento). */
    public static String documentKey(String provider, String documentType, String documentNumber) {
        return sha256Hex(provider, documentType, documentNumber);
    }
}
