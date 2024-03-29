package org.naivs.perimeter.metro.assistant.data.enums;

import lombok.Getter;

@Getter
public enum NotificationType {

    /*
        === out/return for one ===
        "Хлопья овсяные NORDIC, 500г" больше нет в продаже.
        ("<productName>" больше нет в продаже.)
        "Хлопья овсяные NORDIC, 500г" снова в продаже по цене "130.50 р." за "1 шт.".
        ("<productName>" снова в продаже по цене "<regularPrice> р." за "<package>".)

        === out/return for > one ===
        "Кефир ПРОСТОКВАШИНО, 2,5% 930г" больше не продается по "3 шт".
        ("<productName>" больше не продается по "<priceLevel> шт".)
        "Кефир ПРОСТОКВАШИНО, 2,5% 930г" снова продается по "3 шт". Цена "47.20 р." за "1 шт.".
        ("<productName>" снова продается по "<priceLevel> шт". Цена "<price> р." за "<package>".)

        === price increased/decreased for some proposal ===
        Цена выросла на "3.10 р." на "Творог АГУША классический, 4,5% 100г" при покупке от "3 шт.". Новая цена "32.50 р.".
        (Цена выросла на "<deltaPrice> р." на "<productName>" при покупке от "<priceLevel> шт.". Новая цена "<newPrice> р.".)
        Цена снизилась на "1.00 р." на "Творог АГУША классический, 4,5% 100г" при покупке от "1 шт.". Новая цена "34.00 р.".
        (Цена снизилась на "<deltaPrice> р." на "<productName>" при покупке от "<priceLevel> шт.". Новая цена "<newPrice> р.".)

        === best price for some proposal ===
        Лучшая цена на "Биолакт ТЕМА 3,2%, 208г" "28.40 р." при покупке от "3 шт.". Выгода "11.40 р." от розничной стоимости.
        (Лучшая цена на "<productName>" "<price> р." при покупке от "<priceLevel> шт.". Выгода "<deltaPrice> р." от розничной стоимости.)

        === price < 50% of average ===
        Цена более 50% ниже средней стоимости на "Творог ПРОСТОКВАШИНО 2%, 220г" "84.00 р." при покупке от "15 шт.". Выгода "21.00 р.".
        (Цена более 50% ниже средней стоимости на "<productName>" "<price> р." при покупке от "<priceLevel> шт.". Выгода "<deltaPrice> р.".)

        === price < average and > 50% of average ===
        Цена ниже средней стоимости на "Творог ПРОСТОКВАШИНО 2%, 220г" "84.00 р." при покупке от "1 шт.". Выгода "14.00 р.".
        (Цена ниже средней стоимости на "<productName>" "<price> р." при покупке от "<priceLevel> шт.". Выгода "<deltaPrice> р.".)
     */
    PRODUCT_ONE_OUT("\"%s\" больше нет в продаже.", 1),
    PRODUCT_ONE_RETURN("\"%s\" снова в продаже по цене \"%.2f р.\" за \"%s\".", 3),

    PRODUCT_SALE_OUT("\"%s\" больше не продается по \"%s\".", 2),
    PRODUCT_SALE_RETURN("\"%s\" снова продается по \"%s\". Цена \"%.2f р.\" за \"%s\".", 4),

    PRICE_INCREASED("Цена выросла на \"%.2f р.\" на \"%s\" при покупке от \"%s\". Новая цена \"%.2f р.", 4),
    PRICE_DECREASED("Цена снизилась на \"%.2f р.\" на \"%s\" при покупке от \"%s\". Новая цена \"%.2f р.\".", 4),

    PRICE_LEVEL_ONE("Лучшая цена на \"%s\" \"%.2f р.\" при покупке от \"%s\". Выгода \"%.2f р.\" от розничной стоимости.", 4),
    PRICE_LEVEL_TWO("Цена более 50% ниже средней стоимости на \"%s\" \"%.2f р.\" при покупке от \"%s\". Выгода \"%.2f р.\".", 4),
    PRICE_LEVEL_THREE("Цена ниже средней стоимости на \"%s\" \"%.2f р.\" при покупке от \"%s\". Выгода \"%.2f р.\".", 4),

    ERROR("%s", 1);

    private String template;
    private int args;

    NotificationType(String template, int args) {
        this.template = template;
        this.args = args;
    }
}
