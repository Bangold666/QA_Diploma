package netology.ru.Page;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import netology.ru.Data.DataHelper;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class CreditPage {
    final private static SelenideElement fieldCardNumber = $x("//*[.='Номер карты'] //input");
    final private static SelenideElement headingBuy = $(byText("Оплата по карте"));
    final private static SelenideElement headingCredit = $(byText("Кредит по данным карты"));
    final private static SelenideElement fieldCardMonth = $x("//*[.='Месяц'] //input");
    final private static SelenideElement fieldCardYear = $x("//*[.='Год'] //input");
    final private static SelenideElement fieldCardOwner = $x("//*[.='Владелец'] //input");
    final private static SelenideElement fieldCardCVC = $x("//*[.='CVC/CVV'] //input");
    final private static SelenideElement buttonContinue = $(".form-field button");


    public CreditPage() {
        headingCredit.shouldBe(visible);
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


    public void successfulPaymentCreditCard() {
        $(".notification_status_ok")
                .shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    public void invalidPaymentCreditCard() {
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

    public void checkAllFieldsAreRequired() {
        $$(".input__sub").shouldHave(CollectionCondition.size(1))
                .shouldHave(CollectionCondition.texts("Поле обязательно для заполнения"));
    }

}
