package app.pintabien.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DocumentMaskerTest {

    @Test
    void masksMiddleDigits() {
        assertThat(DocumentMasker.mask("44123445")).isEqualTo("44****45");
        assertThat(DocumentMasker.mask("001234567")).isEqualTo("00*****67");
    }

    @Test
    void handlesShortAndNullValues() {
        assertThat(DocumentMasker.mask("1234")).isEqualTo("****");
        assertThat(DocumentMasker.mask(null)).isNull();
    }

    @Test
    void hashIsStableAndNeverContainsDocument() {
        String h1 = DocumentHasher.documentKey("SBS", "DNI", "44123445");
        String h2 = DocumentHasher.documentKey("sbs", "dni", " 44123445 ");
        assertThat(h1).isEqualTo(h2).hasSize(64).doesNotContain("44123445");
        assertThat(DocumentHasher.documentKey("INFOCORP", "DNI", "44123445")).isNotEqualTo(h1);
    }

    @Test
    void tenantContextHoldsAndClears() {
        var tenant = java.util.UUID.randomUUID();
        TenantContext.set(tenant, "corr-x");
        assertThat(TenantContext.requireTenantId()).isEqualTo(tenant);
        assertThat(TenantContext.correlationId()).isEqualTo("corr-x");
        TenantContext.clear();
        assertThat(TenantContext.isPresent()).isFalse();
        assertThat(TenantContext.tenantId()).isEmpty();
    }
}
