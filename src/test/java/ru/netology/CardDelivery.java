package ru.netology;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class CardDelivery {

    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    public void open() {
        Selenide.open("http://localhost:9999/");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input").sendKeys(Keys.DELETE);
    }

    @Test
    public void shouldTestValidData() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[class=notification__title]").shouldHave(text("Успешно!"),
                Duration.ofSeconds(15));
        $("[class=notification__content]").shouldHave(text("Встреча успешно забронирована на " + newDate.format(formatter)));
    }

    @Test
    public void shouldTestNotAdministrativeCenter() {
        $("[data-test-id=city] input").setValue("Шерегеш");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $("[class=button__content]").click();
        $("[data-test-id=city]").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldSendFormWithInvalidCity() {
        $("[data-test-id=city] input").setValue("Kemerovo");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city]").shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldSendFormWithoutCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=city]").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldSendFormWithInvalidName() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Nikolay Potapov");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldSendFormWithoutName() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=name] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldSendFormWithInvalidDate() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys("10.01.2021");
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=date] .input__sub").shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldSendFormWithoutDate() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=date] .input__sub").shouldHave(text("Неверно введена дата"));
    }

    @Test
    public void shouldTestNotValidPhone() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("89238889911");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone] .input__sub").shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));


    }

    @Test
    public void shouldSendFormWithoutPhone() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=phone] .input__sub").shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldSendFormWithoutCheckbox() {
        $("[data-test-id=city] input").setValue("Кемерово");
        $("[data-test-id=date] input").sendKeys(formatter.format(newDate));
        $("[data-test-id=name] input").setValue("Николай Потапов");
        $("[data-test-id=phone] input").setValue("+79238889911");
        $("[class=button__content]").click();
        $("[data-test-id=agreement] .checkbox__text").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
