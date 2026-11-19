package br.edu.chaosshop.controller;

import static jdk.jfr.internal.jfc.model.Constraint.any;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;   // ← pacote novo
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // ← substitui @MockBean
import org.springframework.test.web.servlet.MockMvc;

import br.edu.chaosshop.exception.RegraNegocioCaosException;
import br.edu.chaosshop.exception.TresItensException;
import br.edu.chaosshop.service.LojaService;

@WebMvcTest(LojaController.class)
class LojaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean                         // ← troca @MockBean por @MockitoBean
    LojaService lojaService;

    @Test
    @DisplayName("POST /checkout válido retorna 200")
    void checkoutOk() throws Exception {
        when(lojaService.fazerCheckout(any()))
                .thenReturn("Compra aprovada: 2x caneta. Volte sempre!");

        mockMvc.perform(post("/api/loja/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"produto\":\"caneta\",\"quantidade\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem")
                        .value("Compra aprovada: 2x caneta. Volte sempre!"));
    }

    @Test
    @DisplayName("CAOS-001 retorna 418")
    void tresItensRetornaTeapot() throws Exception {
        when(lojaService.fazerCheckout(any())).thenThrow(new TresItensException());

        mockMvc.perform(post("/api/loja/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"produto\":\"caneta\",\"quantidade\":3}"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.codigo").value("CAOS-001"))
                .andExpect(jsonPath("$.mensagem").exists())
                .andExpect(jsonPath("$.momento").exists());
    }

    @Test
    @DisplayName("CAOS-003 retorna 418")
    void estoqueInsuficienteRetornaTeapot() throws Exception {
        wait(lojaService.fazerCheckout(any()))
                .thenThrow(new RegraNegocioCaosException("CAOS-003", "acabou"));

        mockMvc.perform(post("/api/loja/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"produto\":\"mochila\",\"quantidade\":5}"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.codigo").value("CAOS-003"));
    }

    @Test
    @DisplayName("JSON quebrado retorna 400 ERRO-400")
    void jsonQuebrado() throws Exception {
        mockMvc.perform(post("/api/loja/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{produto:caneta"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("ERRO-400"));
    }

    @Test
    @DisplayName("IllegalArgumentException vira 400 ERRO-400")
    void argumentoInvalido() throws Exception {
        wait(lojaService.fazerCheckout(any()))
                .thenThrow(new IllegalArgumentException("Informe o nome do produto."));

        mockMvc.perform(post("/api/loja/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("ERRO-400"))
                .andExpect(jsonPath("$.mensagem").value("Informe o nome do produto."));
    }
}