package test;

import Data.DataHelper;
import Page.StartPage;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;

public class PaymentTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldStart() {
        DataHelper.clearSUTData();
        open("http://localhost:8080/");
    }

    @AfterEach
    void afterCase() {
        DataHelper.clearSUTData();
    }

    @Test
    void shouldPayWithValidCard() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.payment();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.successfulPaymentDebitCard();

        String actual = DataHelper.getPaymentIdFromOrderEntity();
        String expected = DataHelper.getIdFromPaymentEntity();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotPayWithInValidCard() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setInvalidCard(month, year, owner, cvv);
        var paymentPage = StartPage.payment();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.invalidPaymentDebitCard();

        String actual = DataHelper.getPaymentIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @Test
    void shouldGetErrorPayNotificationWithRandomCard() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setCard("4444 4444 4444 4443", month, year, owner, cvv);
        var paymentPage = StartPage.payment();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.invalidPaymentDebitCard();

        String actual = DataHelper.getPaymentIdFromOrderEntity();
        Assertions.assertNull(actual);
    }
}
