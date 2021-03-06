package ru.knasys.springinactioon5.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.knasys.springinactioon5.db.IngredientRepository;
import ru.knasys.springinactioon5.db.TacoRepository;
import ru.knasys.springinactioon5.entities.Ingredient;
import ru.knasys.springinactioon5.entities.Ingredient.Type;
import ru.knasys.springinactioon5.entities.Order;
import ru.knasys.springinactioon5.entities.Taco;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j /* генерирует объект logger для класса */
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoContriller {
    private IngredientRepository ingredientRepository;
    private TacoRepository tacoRepository;

    @Autowired
    public DesignTacoContriller(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
        this.ingredientRepository = ingredientRepository;
        this.tacoRepository = tacoRepository;
    }

    @GetMapping
    public String showDesignForm(Model model){ /* Поставляется уже с вызванным на ней методом addIngredientsToModel из-за аннотации @ModelAttribute */

        model.addAttribute("taco", new Taco()); /* Блин, нужно называть как класс, иначе будет что-то не понятное О_о */

        Taco paco = new Taco();
        paco.setName("Paco");
        return "design";
    }

    @PostMapping
    public String processDesign(@Valid Taco taco, Errors errors, Model model) {  /* выполнить проверку на предоставленном объекте тако */
        if(errors.hasErrors()){
            String test = "Имеются ошибки"; /* Пример того как пропихнуть в представление переменную */
            model.addAttribute("test", test);
            return "design";
        }
        log.info("Процессинг типа дизайн, но на самом деле тако " + taco);

        Taco saved = tacoRepository.save(taco);
        Order order = (Order) model.getAttribute("order");
        order.getTacos().add(saved);

        return "redirect:/orders/current";
//        return "";
    }


    @ModelAttribute
    private void addIngredientsToModel(Model model){
        List<Ingredient> ingredients = new ArrayList<>(); /*Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );*/
        ingredientRepository.findAll().forEach(ingredient -> ingredients.add(ingredient));
        Type[] types = Type.values();
        for (Type type : types) {
            model.addAttribute(type.toString().toLowerCase(), ingredients.stream().filter(ingredient -> ingredient.getType() == type).collect(Collectors.toList()));
        }
    }

    @ModelAttribute("order")
    private Order order() {
        return new Order();
    }

    @ModelAttribute("taco")
    private Taco taco() {
        return new Taco();
    }
}
