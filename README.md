# Описание   
***
В рамках данного проекта необходимо автоматизировать тестирование комплексного сервиса покупки тура, взаимодействующего с СУБД и API Банка.  

База данных хранит информацию о заказах, платежах, статусах карт, способах оплаты.  

Покупка тура возможна с помощью карты и в кредит. Данные по картам обрабатываются отдельными сервисами (Payment Gate, Credit Gate)  

Полные условия и исходные данные описанного кейса можно посмотреть [здесь](https://github.com/netology-code/qa-diploma)   

# Документация
* [План автоматизации тестирования веб-формы сервиса покупки туров интернет-банка](https://github.com/Bangold666/QA_Diploma/blob/master/documents/Plan.md)  
* [Отчёт о проведённом тестировании](https://github.com/Bangold666/QA_Diploma/blob/master/documents/Report.md)
* [Комплексный отчёт о проведённой автоматизации тестирования]()

# Инструкция по запуску SUT и авто-тестов
Для запуска тестов, в системе необходимо иметь установленным IntelliJ IDEA, JDK 11, Docker Desktop.

По умолчанию используется база данных MySQL. Для изменения базы данных, необходимо переключить (перекомментировать) в классе [DataHelper.jar](https://github.com/Bangold666/QA_Diploma/blob/master/src/test/java/netology/ru/Data/DataHelper.java) строки `16, 17`, а также переключить (перекомментировать) в файле [application.properties](https://github.com/Bangold666/QA_Diploma/blob/master/application.properties) `строки 3, 4`.

Для запуска тестов необходимо:

1. Запустить gate-simulator и базу данных командой: `docker-compose up` (для запуска в фоновом режиме использовать флаг -d)
2. Запустить приложение командой: `java -jar ./artifacts/aqa-shop.jar`
3. Запустить тесты командой `./gradlew clean test --info -Dselenide.headless=true`(флаг --info позволяет выводить больше информации, флаг -Dselenide.headless=true запуск selenide в headless-режиме)
4. Для просмотра отчета в Allure, ввести команду: `./gradlew allureServe`
