package caldeira.pokemon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokeApi")
public class ApiController {

    private final ApiController apiController;

    public ApiController(ApiController apiController) {
        this.apiController = apiController;
    }

    @GetMapping("/pokemon/{pokemon_name}")
    public String whoIsThatPokemon (@PathVariable String pokemon_name){
        return service.getPokemonByName(pokemon_name);
    }
}
