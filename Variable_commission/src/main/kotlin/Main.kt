package ru.netology

import kotlin.math.roundToInt

/**
 *Задача №2. Разная комиссия
 * Сумма комиссии будет зависеть ещё и от типа карты, с которой мы переводим средства.
 *
 * За переводы с карты Mastercard комиссия не взимается, пока не превышен месячный лимит в 75 000 руб. Если лимит превышен, комиссия составит 0,6% + 20 руб.
 * За переводы с карты Visa комиссия составит 0,75%, минимальная сумма комиссии 35 руб.
 * За переводы с карты Мир комиссия не взимается.
 * Кроме того, введём лимиты на суммы перевода за сутки и за месяц. Максимальная сумма перевода с одной карты:
 *
 * 150 000 руб. в сутки
 * 600 000 руб. в месяц
 * Комиссия в лимитах не учитывается.
 *
 * Т. е. если пользователь решит перевести матери 150 000 руб. с карты Mastercard впервые за месяц, то его мать получит всю сумму, а комиссия будет удержана сверх этого. Сумма комиссии составит 75 000 * 0,006 + 20 = 470 руб. (т. к. с первых 75 000 руб. комиссия не взимается).
 *
 * Напишите алгоритм расчёта в виде функции, передавая в функцию:
 * В случае превышения какого-либо из лимитов операция должна блокироваться.
 */

fun main() {
    println(calculateCommission("Mastercard", 0, 120_000))
    println(calculateCommission("Visa", 0, 50_000))
    println(calculateCommission("Visa", 0, 1_000))
    println(calculateCommission("Мир", 0, 300_000))
    println(calculateCommission("Mastercard", 0, 200_000))
    println(calculateCommission("Mastercard", 80_000, 10_000))
}


//Функция расчета комиссии в зависимости от типа карты и суммы перевода
fun calculateCommission(
    cardLevel: String = "Мир", //тип карты (по умолчанию Мир);
    sumTransferAmountToMonth: Int = 0, //сумма предыдущих переводов в этом месяце (по умолчанию 0 рублей);
    transferAmount: Int, // сумма совершаемого перевода.
): String {
    //Проверяем лимит по сумме за день и в месяц
    if (transferAmount > DAILY_LIMIT){
        println("Перевод заблокирован. Превышен суточный лимит в ${DAILY_LIMIT} руб.")
    }
    if (sumTransferAmountToMonth + transferAmount > MONTHLY_LIMIT){
        println("Перевод заблокирован. Превышен месячный лимит в ${MONTHLY_LIMIT} руб.")
    }

    //Рассчитаем комиссию
    val commission: Double = when (cardLevel) {
        "Mastercard" -> {
            val freeRemaining = maxOf(0, MONTHLY_LIMIT_MASTER - sumTransferAmountToMonth) //Остаток до назначения комиссии. Пример 75_000 - 0 = 75_000
            val limit = maxOf(0, transferAmount - freeRemaining) //Определение превышения лимита. Пример 10_000 - 75_000 = - 65_000
            if (limit == 0) 0.0 //Если limit отрицательный, значит лимит еще не превышен, в противном случае назначается комиссия
            else limit * COMMISSION_PERCENT_MASTER + COMMISSION_RUB_MASTER //комиссия составит 0,6% + 20 руб. от суммы превышения лимита
        }
        "Visa" -> maxOf(transferAmount * COMMISSION_PERCENT_VISA, COMMISSION_RUB_VISA) //комиссия составит 0,75% если она больше 35 руб.
        else -> 0.0 // "Мир" и любые другие карты
    }

    // Округление до копеек
    val rounded = (commission * 100).roundToInt() / 100.0
    return "Комиссия: $rounded руб."
}

const val MONTHLY_LIMIT_MASTER = 75_000 //Месячный лимит
const val COMMISSION_PERCENT_MASTER = 0.006 //Комиссия 0.6%
const val COMMISSION_RUB_MASTER = 20.0
const val COMMISSION_PERCENT_VISA = 0.0075 //Комиссия 0.75%
const val COMMISSION_RUB_VISA = 35.0
const val DAILY_LIMIT = 150_000
const val MONTHLY_LIMIT = 600_000