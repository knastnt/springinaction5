package ru.knasys.springinactioon5.entities;

import lombok.Data;
import org.hibernate.validator.constraints.CreditCardNumber;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class Order {
    private Long id;
    @NotBlank(message = "Заполните поле Имя")
    private String name;
    @NotBlank(message = "Заполните поле Улица")
    private String street;
    @NotBlank(message = "Заполните поле Город")
    private String city;
    @NotBlank(message = "Заполните поле Область/Край")
    private String state;
    @NotBlank(message = "Заполните поле Индекс")
    private String zip;
    @CreditCardNumber(message = "Номер карты не верный")
    private String ccNumber;
    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/]([1-9][0-9]))$", message = "Действует до должно быть в формате ММ/ГГ")
    private String ccExpiration;
//    @Digits(integer = 3, fraction = 0, message = "CVV не верный")
    @Pattern(regexp = "^\\d{3}$", message = "CVV не верный")
    private String ccCVV;
}
