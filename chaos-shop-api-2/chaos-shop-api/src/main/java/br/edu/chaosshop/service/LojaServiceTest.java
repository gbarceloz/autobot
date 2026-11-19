package br.edu.chaosshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.edu.chaosshop.dto.CheckoutRequest;
import br.edu.chaosshop.estoque.Estoque;
import br.edu.chaosshop.exception.RegraNegocioCaosException;
import br.edu.chaosshop.exception.TresItensException;

class LojaServiceTest {

    private LojaService service;

    @BeforeEach
    void setUp() {
        service = new LojaService(new Estoque());
    }

    @Test
    void checkoutValido() {
        String msg = service.fazerCheckout(new CheckoutRequest("caneta", 2));
        assertThat(msg).contains("caneta").contains("2");
    }

    @Test
    void normalizaProdutoParaMinusculoETrim() {
        String msg = service.fazerCheckout(new CheckoutRequest("  CANETA  ", 1));
        assertThat(msg).contains("caneta");
    }

    @Test
    void produtoNulo() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest(null, 1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void produtoEmBranco() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("   ", 1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void quantidadeNula() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("caneta", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void quantidadeZeroOuNegativa() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("caneta", 0)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("caneta", -1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("quantidade 3 dispara o CAOS-001")
    void tresItens() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("caneta", 3)))
                .isInstanceOf(TresItensException.class)
                .hasMessageContaining("Três");
    }

    @Test
    @DisplayName("produto inexistente embrulha como CAOS-003")
    void produtoInexistenteGeraCaos003() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("peixe", 1)))
                .isInstanceOf(RegraNegocioCaosException.class)
                .isInstanceOfSatisfying(RegraNegocioCaosException.class,
                        ex -> assertThat(ex.getCodigo()).isEqualTo("CAOS-003"))
                .hasMessageContaining("peixe");
    }

    @Test
    @DisplayName("estoque insuficiente embrulha como CAOS-003")
    void estoqueInsuficienteGeraCaos003() {
        assertThatThrownBy(() -> service.fazerCheckout(new CheckoutRequest("mochila", 5)))
                .isInstanceOfSatisfying(RegraNegocioCaosException.class,
                        ex -> assertThat(ex.getCodigo()).isEqualTo("CAOS-003"));
    }
}