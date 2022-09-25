package netology.ru.Page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class StartPage {
    private static final SelenideElement paymentButton = $$(".button").find(exactText("Купить"));
    private static final SelenideElement creditButton = $$(".button").find(exactText("Купить в кредит"));
    private final SelenideElement headingStart = $("h2.heading");

    public StartPage() {
        headingStart.shouldBe(visible);
    }

    public static PaymentPage payment() {
        paymentButton.click();
        return new PaymentPage();
    }

    public static CreditPage paymentOnCredit() {
        creditButton.click();
        return new CreditPage();
    }
}
