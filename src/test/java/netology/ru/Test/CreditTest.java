package netology.ru.Test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import netology.ru.Data.DataHelper;
import netology.ru.Page.StartPage;
import org.junit.jupiter.api.*;

import static com.codeborne.selenide.Selenide.open;

public class CreditTest {

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

    @DisplayName("Покупка тура в кредит по «APPROVED» карте с валидным реквизитами")
    @Test
    void shouldCreditWithValidCard() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.successfulPaymentCreditCard();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        String expected = DataHelper.getIdFromCreditRequestEntity();
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Покупка тура в кредит по «DECLINED» карте с валидными реквизитами")
    @Test
    void shouldNotPayWithInValidCard() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setInvalidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.invalidPaymentCreditCard();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Оплата с незарегистрированной картой")
    @Test
    void shouldGetErrorPayNotificationWithRandomCard() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                RandomCardNumber = "4444 4444 4444 4443";

        var cardInfo = DataHelper.setCard(RandomCardNumber, month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.invalidPaymentCreditCard();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при пустом поле `Номер карты`")
    @Test
    void shouldGetErrorNotificationWithEmptyCardNumber() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                EmptyCardNumber = "";

        var cardInfo = DataHelper.setCard(EmptyCardNumber, month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе нуля в поле `Номер карты`")
    @Test
    void shouldGetErrorNotificationWithShortCardNumber() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                shortCardNumber = "4444 4444 4444";
        var cardInfo = DataHelper.setCard(shortCardNumber, month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе короткого номера в поле `Номер карты`")
    @Test
    void shouldGetErrorNotificationWithZeroAsCardNumber() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                zeroCardNumber = "0";

        var cardInfo = DataHelper.setCard(zeroCardNumber, month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при просроченным поле `Месяц`")
    @Test
    void shouldGetErrorNotificationWithPastValidityPeriod() {
        String[] date = DataHelper.generateDate(-30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123",
                cardNumber = "4444 4444 4444 4441";

        var cardInfo = DataHelper.setCard(cardNumber, month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidCardValidityPeriod();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе 00 в поле `Месяц`")
    @Test
    void shouldGetErrorNotificationWithDoubleZeroAsMonth() {
        String[] date = DataHelper.generateDate(30);
        String month = "00",
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidCardValidityPeriod();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе 13 в поле `Год`")
    @Test
    void shouldGetErrorNotificationWithCardExpiredPeriod() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = "13",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkCardExpired();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при пустом поле `Месяц`")
    @Test
    void shouldGetErrorNotificationWithEmptyMonth() {
        String[] date = DataHelper.generateDate(30);
        String month = " ",
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе месяца не верного формата в поле `Месяц`")
    @Test
    void shouldGetErrorNotificationWithInvalidFormatMonth() {
        String[] date = DataHelper.generateDate(30);
        String month = "3",
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при не заполнении поля `Год`")
    @Test
    void shouldGetErrorNotificationWithEmptyYear() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = " ",
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе прошлого года в поле `Год`")
    @Test
    void shouldGetErrorNotificationWithPastYear() {
        String[] date = DataHelper.generateDate(-365);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkCardExpired();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе на 6 лет дальше настоящего года в поле `Год`")
    @Test
    void shouldGetErrorNotificationWithFutureYear() {
        String[] date = DataHelper.generateDate(2190);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidCardValidityPeriod();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при пустом поле `Владелец`")
    @Test
    void shouldGetErrorNotificationWithEmptyOwnerField() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = "",
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkAllFieldsAreRequired();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе кириллицы в поле`Владелец`")
    @Test
    void shouldGetErrorNotificationWithCyrillicOwner() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("Ru"),
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе спецсимволов в поле`Владелец`")
    @Test
    void shouldGetErrorNotificationWithSpecialSymbolAsOwner() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = "!\"№;?\"%",
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе цифр в поле`Владелец`")
    @Test
    void shouldGetErrorNotificationWithNumbersAsOwner() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = "12345",
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе нуля в поле`Владелец`")
    @Test
    void shouldGetErrorNotificationWithZeroAsOwner() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = " 0 ",
                cvv = "123";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при пустом поле`CVV`")
    @Test
    void shouldGetErrorNotificationWithEmptyCvv() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = " ";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе одной цифры в поле`CVV`")
    @Test
    void shouldGetErrorNotificationWithOneAsCvv() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "1";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе нуля в поле`CVV`")
    @Test
    void shouldGetErrorNotificationWithZeroAsCvv() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "0";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

    @DisplayName("Проверка покупки тура при вводе двух цифр в поле`CVV`")
    @Test
    void shouldGetErrorNotificationWithTwoDigitAsCvv() {
        String[] date = DataHelper.generateDate(30);
        String month = date[1],
                year = date[2],
                owner = DataHelper.generateOwner("En"),
                cvv = "12";

        var cardInfo = DataHelper.setValidCard(month, year, owner, cvv);
        var paymentPage = StartPage.paymentOnCredit();

        paymentPage.cleanAllFields();
        paymentPage.enterCardData(cardInfo);

        paymentPage.checkInvalidFormat();

        String actual = DataHelper.getCreditIdFromOrderEntity();
        Assertions.assertNull(actual);
    }

}
