// TODO Считай самое главное - надо добавить тесты на все возможные случаи. Будет гораздо удобнее
// Сейчам почему-то 1+1 выражение он поймёт, а (1+1) нет, говорит, что скобки расставлены не верно, хотя это не так

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

// TODO пока не смотрел - надо разобраться

/**
 * Сглаживает токены, указанные в списке, и упрощает их.
 */
fun flatten(tokens: ArrayList<Any>, vararg ops: Operation) {
    val tokenSwap = ArrayList<Any>()

    tokenSwap.addAll(tokens)

    while (!ops.none { tokens.contains(it) }) {
        println(tokens.joinToString(separator = " "))

        for (i in tokens.indices) {
            var topBreak = false
            for (o in ops) {
                if (tokens[i] == o) {
                    // левый и правый токен должен быть Double
                    if (i == 0 && o.requireLeftValue()) {
                        throw IllegalArgumentException("Выражение не может начинаться с оператора: $o")
                    } else if (i == tokens.size - 1 && o.requireRightValue()) {
                        throw IllegalArgumentException("Выражение не может заканчиваться оператором: $o")
                    }

                    if (o.requireRightValue() && o.requireLeftValue()) {
                        val a = tokens[i - 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i - 1]}")
                        val b = tokens[i + 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i + 1]}")

                        val v = o.execute(a, b)

                        val localIndex = findIndex(tokens[i - 1], tokens[i], tokens[i + 1], list = tokenSwap)
                        for (j in 0..2) {
                            tokenSwap.removeAt(localIndex)
                        }

                        tokenSwap.add(localIndex, v)
                        topBreak = true
                        break
                    } else if (o.requireRightValue()) {
                        val b = tokens[i + 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i + 1]}")

                        val v = o.execute(0.0, b)

                        val localIndex = findIndex(tokens[i], tokens[i + 1], list = tokenSwap)
                        for (j in 0..1) {
                            tokenSwap.removeAt(localIndex)
                        }

                        tokenSwap.add(localIndex, v)
                        topBreak = true
                        break
                    } else if (o.requireLeftValue()) {
                        val a = tokens[i - 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i - 1]}")

                        val v = o.execute(a, 0.0)

                        val localIndex = findIndex(tokens[i - 1], tokens[i], list = tokenSwap)
                        for (j in 0..1) {
                            tokenSwap.removeAt(localIndex)
                        }

                        tokenSwap.add(localIndex, v)
                        topBreak = true
                        break
                    }
                }
            }

            if (topBreak)
                break
        }

        tokens.clear()
        tokens.addAll(tokenSwap)
    }
}

/**
 * находит ииндекс в заданном списке ArrayList
 */
fun findIndex(vararg objs: Any, list: ArrayList<Any>) =
    list.indices.firstOrNull { i -> list[i] == objs[0] && objs.indices.none { list[i + it] != objs[it] } } ?: -1
