package Page;

import Data.DataHelper;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage {
    final private static SelenideElement fieldCardNumber = $x("//*[.='Номер карты'] //input");
    final private static SelenideElement headingBuy = $(byText("Оплата по карте"));
    final private static SelenideElement headingCredit = $(byText("Кредит по данным карты"));
    final private SelenideElement notificationStatusOk = $(".notification_status_ok .notification__content");
    final private SelenideElement buttonCloseNotificationStatusOk = $(".notification_status_ok button");
    final private SelenideElement notificationStatusError = $(".notification_status_error .notification__content");
    final private SelenideElement buttonCloseNotificationStatusError = $(".notification_status_error button");
    final private static SelenideElement fieldCardMonth = $x("//*[.='Месяц'] //input");
    final private static SelenideElement fieldCardYear = $x("//*[.='Год'] //input");
    final private static SelenideElement fieldCardOwner = $x("//*[.='Владелец'] //input");
    final private static SelenideElement fieldCardCVC = $x("//*[.='CVC/CVV'] //input");
    final private static SelenideElement buttonContinue = $(".form-field button");
    final private static SelenideElement fieldWrongFormat = $(byText("Неверный формат"));
    final private static SelenideElement fieldWrongCardDate = $(byText("Неверно указан срок действия карты"));
    final private static SelenideElement fieldCardDateExpired = $(byText("Истёк срок действия карты"));
    final private static SelenideElement fieldRequired = $(byText("Поле обязательно для заполнения"));

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

    public void successfulPaymentCreditCard() {
        $(".notification_status_ok")
                .shouldHave(text("Успешно Операция одобрена Банком."), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public void invalidPaymentCreditCard() {
        $(".notification_status_error")
                .shouldBe(visible).shouldHave(text("Ошибка Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(15));
    }

    public void checkInvalidFormat() {
        $(".input__sub").shouldBe(visible).shouldHave(text("Неверный формат"),Duration.ofSeconds(15));
    }

    public void checkInvalidCardValidityPeriod() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Неверно указан срок действия карты"),Duration.ofSeconds(15));
    }

    public void checkCardExpired () {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Истёк срок действия карты"),Duration.ofSeconds(15));
    }

    public void checkInvalidOwner() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Введите имя и фамилию, указанные на карте"),Duration.ofSeconds(15));
    }

    public void checkEmptyField() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Поле обязательно для заполнения"),Duration.ofSeconds(15));
    }

    public void incorrectOwner() {
        $(".input__sub").shouldBe(visible)
                .shouldHave(text("Значение поля может содержать только латинские буквы и дефис"),Duration.ofSeconds(15));
    }

    public void checkAllFieldsAreRequired() {
        $$(".input__sub").shouldHave(CollectionCondition.size(5))
                .shouldHave(CollectionCondition.texts("Поле обязательно для заполнения"));
    }

}
