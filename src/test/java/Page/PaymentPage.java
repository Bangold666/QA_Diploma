package Page;

import Data.DataHelper;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class PaymentPage {

    final private static SelenideElement fieldCardMonth = $x("//*[.='Месяц'] //input");
    final private static SelenideElement fieldCardYear = $x("//*[.='Год'] //input");
    final private static SelenideElement fieldCardOwner = $x("//*[.='Владелец'] //input");
    final private static SelenideElement fieldCardCVC = $x("//*[.='CVC/CVV'] //input");
    final private static SelenideElement fieldCardNumber = $x("//*[.='Номер карты'] //input");
    private final SelenideElement headingPayment = $$("h3.heading").find(exactText("Оплата по карте"));
    private final SelenideElement buttonContinue = $$(".button").find(exactText("Продолжить"));

    public PaymentPage() {
        headingPayment.shouldBe(visible);
    }

    public final void enterCardData(DataHelper.CardInfo info) {
        fieldCardNumber.setValue(info.getCardNumber());
        fieldCardMonth.setValue(info.getCardMonth());
        fieldCardYear.setValue(info.getCardYear());
        fieldCardOwner.setValue(info.getCardOwner());
        fieldCardCVC.setValue(info.getCardCVV());
        buttonContinue.click();
    }

    public final void cleanAllFields() {
        fieldCardNumber.sendKeys(Keys.LEFT_CONTROL + "A");
        fieldCardNumber.sendKeys(Keys.DELETE);
        fieldCardMonth.sendKeys(Keys.LEFT_CONTROL + "A");
        fieldCardMonth.sendKeys(Keys.DELETE);
        fieldCardYear.sendKeys(Keys.LEFT_CONTROL + "A");
        fieldCardYear.sendKeys(Keys.DELETE);
        fieldCardOwner.sendKeys(Keys.LEFT_CONTROL + "A");
        fieldCardOwner.sendKeys(Keys.DELETE);
        fieldCardCVC.sendKeys(Keys.LEFT_CONTROL + "A");
        fieldCardCVC.sendKeys(Keys.DELETE);
    }


    public void successfulPaymentDebitCard() {
        $(".notification_status_ok")
                .shouldHave(text("Успешно Операция одобрена Банком."), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void invalidPaymentDebitCard() {
        $(".notification_status_error")
                .shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    public void checkInvalidFormat() {
        $(".input__sub").shouldBe(visible).shouldHave(text("Неверный формат"), Duration.ofSeconds(15));
    }

    public void checkInvalidCardValidityPeriod() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Неверно указан срок действия карты"), Duration.ofSeconds(15));
    }

    public void checkCardExpired() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Истёк срок действия карты"), Duration.ofSeconds(15));
    }

    public void checkInvalidOwner() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Введите имя и фамилию, указанные на карте"), Duration.ofSeconds(15));
    }

    public void checkEmptyField() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"), Duration.ofSeconds(15));
    }

    public void incorrectOwner() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Значение поля может содержать только латинские буквы и дефис"), Duration.ofSeconds(15));
    }

    public void checkAllFieldsAreRequired() {
        $$(".input__sub").shouldHave(CollectionCondition.size(5))
                .shouldHave(CollectionCondition.texts("Поле обязательно для заполнения"));
    }

}