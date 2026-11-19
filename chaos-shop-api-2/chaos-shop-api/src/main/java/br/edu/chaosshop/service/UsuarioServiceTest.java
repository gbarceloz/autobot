package br.edu.chaosshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.edu.chaosshop.dto.CadastroRequest;
import br.edu.chaosshop.exception.NomeDePalindromoException;
import br.edu.chaosshop.exception.RegraNegocioCaosException;

class UsuarioServiceTest {

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService();
    }

    @Test
    void cadastroValido() {
        String msg = service.cadastrar(new CadastroRequest("maria", "abc12345"));
        assertThat(msg).contains("maria");
    }

    @Test
    void nomeNuloOuEmBranco() {
        assertThatThrownBy(() -> service.cadastrar(new CadastroRequest(null, "abc12345")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.cadastrar(new CadastroRequest("   ", "abc12345")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void senhaNulaOuEmBranco() {
        assertThatThrownBy(() -> service.cadastrar(new CadastroRequest("maria", null)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.cadastrar(new CadastroRequest("maria", "   ")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("palíndromo de 3+ letras dispara CAOS-002")
    void palindromo() {
        assertThatThrownBy(() -> service.cadastrar(new CadastroRequest("ovo", "abc12345")))
                .isInstanceOf(NomeDePalindromoException.class)
                .isInstanceOfSatisfying(RegraNegocioCaosException.class,
                        ex -> assertThat(ex.getCodigo()).isEqualTo("CAOS-002"))
                .hasMessageContaining("ovo");
    }

    @Test
    @DisplayName("case-insensitive: Arara também é palíndromo")
    void palindromoIgnoraMaiusculas() {
        assertThatThrownBy(() -> service.cadastrar(new CadastroRequest("Arara", "abc12345")))
                .isInstanceOf(NomeDePalindromoException.class);
    }

    @Test
    @DisplayName("palíndromo com menos de 3 letras é permitido")
    void palindromoCurtoEhPermitido() {
        String msg = service.cadastrar(new CadastroRequest("aa", "abc12345"));
        assertThat(msg).contains("aa");
    }
}