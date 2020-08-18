fun main() {
    println("Определение переменных !x = 3")
    println("Определение функций !f(x) = x^2")
    println("Поддерживаемые операторы: % ! ^ √ * / + - > < = & |")
    println("Логические операторы буду считать любое число больше 1 - true, меньше - false")
    println("Логические операторы возвращают 1 - true, 0 - false")
    println("Обозначать отрицательные числа: _, а не - ")

    val env = ExpressionEnvironment()
    println("Константы:")

    env.parseAndPrint("!e = 3")
    env.parseAndPrint("!p = 3.14")

    while (true) {
        println("Введите выражение:")
        val exprRaw = readLine() ?: continue

        env.parseAndPrint(exprRaw)
    }
}

/*
!x = 3 - задаём переменную
!f(x)=3x - функцию
f(x) - вызов

Ну или просто (2-5)(5×7)
*/