package netology.ru.Data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataHelper {
    // в зависимости от используемой базы данных, раскомментировать нужное значение переменной
    //final private static String databaseType = "postgresql";
    final private static String databaseType = "mysql";
    final private static String testDataValidCardNumber = "4444444444444441";
    final private static String testDataInvalidCardNumber = "4444444444444442";

    final static String orderCreditId = "SELECT credit_id FROM order_entity ORDER BY created DESC";
    final static String creditId = "SELECT bank_id FROM credit_request_entity ORDER BY created DESC";
    final static String orderPaymentId = "SELECT payment_id FROM order_entity ORDER BY created DESC";
    final static String paymentId = "SELECT transaction_id FROM payment_entity ORDER BY created DESC";

    @Value
    public static class CardInfo {
        private String cardNumber;
        private String cardMonth;
        private String cardYear;
        private String cardOwner;
        private String cardCVV;
    }

    public static CardInfo setCard(String setCardNumber, String setCardMonth, String setCardYear, String setCardOwner, String setCardCVV) {
        return new CardInfo(setCardNumber, setCardMonth, setCardYear, setCardOwner, setCardCVV);
    }

    public static CardInfo setValidCard(String month, String year, String owner, String cvv) {
        return setCard(testDataValidCardNumber, month, year, owner, cvv);
    }

    public static CardInfo setInvalidCard(String month, String year, String owner, String cvv) {
        return setCard(testDataInvalidCardNumber, month, year, owner, cvv);
    }

    public static String[] generateDate(int days) {
        String date = LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yy"));
        return date.split("\\.");
    }

    public static String generateOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    public static String getIdFromPaymentEntity() {
        return requestSQL(paymentId);
    }

    public static String getPaymentIdFromOrderEntity() {
        return requestSQL(orderPaymentId);
    }

    public static String getIdFromCreditRequestEntity() {
        return requestSQL(creditId);
    }

    public static String getCreditIdFromOrderEntity() {
        return requestSQL(orderCreditId);
    }

    @SneakyThrows
    private static String requestSQL(String requestSQL) {
        //ожидание обновления базы данных
        Thread.sleep(500);
        var runner = new QueryRunner();

        if (databaseType.equals("mysql")) {
            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app", "app", "pass"
                    )
            ) {
                return runner.query(conn, requestSQL, new ScalarHandler<>());
            }
        } else if (databaseType.equals("postgresql")) {
            {
                try (
                        var conn = DriverManager.getConnection(
                                "jdbc:postgresql://localhost:5432/app", "app", "pass"
                        )
                ) {
                    return runner.query(conn, requestSQL, new ScalarHandler<>());
                }
            }
        } else {
            throw new RuntimeException("databaseType in DataHelper.java should be equal 'mysql' or 'postgresql'. " +
                    "Actual value: " + databaseType);
        }
    }

    @SneakyThrows
    public static void clearSUTData() {
        var runner = new QueryRunner();
        var sqlDeleteAllOrders = "DELETE FROM order_entity;";
        var sqlDeleteAllCreditRequest = "DELETE FROM credit_request_entity;";
        var sqlDeleteAllPaymentRequest = "DELETE FROM payment_entity;";

        if (databaseType.equals("mysql")) {
            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/app", "app", "pass"
                    )
            ) {
                runner.update(conn, sqlDeleteAllOrders);
                runner.update(conn, sqlDeleteAllCreditRequest);
                runner.update(conn, sqlDeleteAllPaymentRequest);
            }
        } else if (databaseType.equals("postgresql")) {
            try (
                    var conn = DriverManager.getConnection(
                            "jdbc:postgresql://localhost:5432/app", "app", "pass"
                    )
            ) {
                runner.update(conn, sqlDeleteAllOrders);
                runner.update(conn, sqlDeleteAllCreditRequest);
                runner.update(conn, sqlDeleteAllPaymentRequest);
            }
        } else {
            throw new RuntimeException("databaseType in DataHelper.java should be equal 'mysql' or 'postgresql'. " +
                    "Actual value: " + databaseType);
        }
    }
}
