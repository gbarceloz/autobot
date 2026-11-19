package br.edu.chaosshop.estoque;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.edu.chaosshop.exception.EstoqueInsuficienteException;

class EstoqueTest {

    private Estoque estoque;

    @BeforeEach
    void setUp() {
        estoque = new Estoque();
    }

    @Test
    @DisplayName("reserva quando há unidades suficientes")
    void reservaComSucesso() throws EstoqueInsuficienteException {
        estoque.reservar("caneta", 3);   // 10 -> 7
        estoque.reservar("caneta", 7);   // 7  -> 0
    }

    @Test
    @DisplayName("lança exceção quando o produto não existe")
    void produtoInexistente() {
        assertThatThrownBy(() -> estoque.reservar("peixe", 1))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("peixe");
    }

    @Test
    @DisplayName("lança exceção quando a quantidade excede o disponível")
    void quantidadeExcedeEstoque() {
        assertThatThrownBy(() -> estoque.reservar("mochila", 3))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("2");
    }

    @Test
    @DisplayName("não deixa reservar além do que sobrou")
    void naoPassaDoDisponivel() throws EstoqueInsuficienteException {
        estoque.reservar("mochila", 2);
        assertThatThrownBy(() -> estoque.reservar("mochila", 1))
                .isInstanceOf(EstoqueInsuficienteException.class);
    }
}