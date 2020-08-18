data class Value(val textualValue: String) { // получит expr строку

    var resolvedValue = Float.NaN

    /** Разрешает выражение и возвращает его, кэширует вполе resolvedValue */
    fun resolve(): Float {
        try {
            resolvedValue = textualValue.toFloat()
        } catch (e: NumberFormatException) {
        }// исключение числового формати

        val tokens = ArrayList<Any>()
        var index = 0
        var numStartIndex = -1

        while (index < textualValue.length) {
            val c = textualValue[index]

            if (c.isWhitespace()) {
                index++
                continue
            }
            // парсинг значений с плавющей точкой
            if (c.isDigit() || c == '_') {   // для чисел, положительных и отрицательных
                if (numStartIndex == -1) {
                    numStartIndex = index
                } else if (c == '_') {
                    throw IllegalArgumentException("Отрицательный операнд внутри числа")
                }
            } else if (!c.isDigit() && c != '.') {
                // перемещаем текущий число в стек
                if (numStartIndex != -1) {
                    // с numStartIndex до Index
                    val num = textualValue.substring(numStartIndex, index).replace("_", "-").toFloat()
                    tokens.add(num)
                }
                numStartIndex = -1
            }
            /** смотрим если это скобка*/
            if (c == '(') {
                // ищем подстроку в скобках
                var bracketDepth = 0

                for (seek in index + 1 until textualValue.length) { // поиск конечной закрывающей скобки
                    if (textualValue[seek] == '(') // если есть вложенные скобки
                        bracketDepth++

                    if (textualValue[seek] == ')') {
                        if (bracketDepth == 0) { //проверим что в далее нет вложенных скобок
                            val bracketContent = textualValue.substring(index + 1, seek)  // +1 к индексу
                            val subVal = Value(bracketContent)
                            tokens.add(subVal.resolve()) // пополняем стек
                            index = seek
                            bracketDepth = -1
                            break
                        } else {
                            bracketDepth--
                        }
                    }
                }
                if (bracketDepth != 1) {
                    throw IllegalArgumentException("Скобки расставлены неверно")
                }
            } else if (Operation.isOperator(c)) {
                tokens.add(Operation.getOperationForChar(c)!!)
            } else if (!c.isDigit() && c != '_' && c != '.') {
                println("Нераспознанный символ: $c. Игнорируется")
            }
            //////////

            index++
        }

        if (numStartIndex != -1) {

            val num = textualValue.substring(numStartIndex, textualValue.length).replace("_", "-").toFloat()
            tokens.add(num)
        }

        flatten(tokens, Operation.FACTORIAL)
        flatten(tokens, Operation.MODULE)
        flatten(tokens, Operation.POWER)
        flatten(tokens, Operation.ROOT)
        flatten(tokens, Operation.DIVIDE, Operation.MULTIPLY)
        flatten(tokens, Operation.PLUS, Operation.MINUS)

        // токен - 1 символ
        if (tokens.size != 1) {
            throw IllegalArgumentException("Невозможно полностью разрешить выражение: ${tokens.joinToString(separator = " ")}")
        }
        resolvedValue = tokens[0] as Float

        return resolvedValue

    }
}
/**
 * Сглаживает токены, указанные в списке, и упрощает их.
 */
fun flatten(tokens: ArrayList<Any>, vararg ops: Operation) {
    val tokenSwap = ArrayList<Any>()

    tokenSwap.addAll(tokens)

    while(!ops.none { tokens.contains(it) }) {
        println(tokens.joinToString(separator = " "))

        for (i in tokens.indices) {
            var topBreak = false
            for(o in ops) {
                if (tokens[i] == o) {
                    // левый и правый токен должен быть float
                    if(i == 0 && o.requireLeftValue()) {
                        throw IllegalArgumentException("Выражение не может начинаться с оператора: $o")
                    } else if(i == tokens.size - 1 && o.requireRightValue()) {
                        throw IllegalArgumentException("Выражение не может заканчиваться оператором: $o")
                    }

                    if(o.requireRightValue() && o.requireLeftValue()) {
                        val a = tokens[i - 1] as? Float ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i - 1]}")
                        val b = tokens[i + 1] as? Float ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i + 1]}")

                        val v = o.execute(a, b)

                        val localIndex = findIndex(tokens[i - 1], tokens[i], tokens[i + 1], list = tokenSwap)
                        for (j in 0..2) {
                            tokenSwap.removeAt(localIndex)
                        }

                        tokenSwap.add(localIndex, v)
                        topBreak = true
                        break
                    } else if(o.requireRightValue()) {
                        val b = tokens[i + 1] as? Float ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i + 1]}")

                        val v = o.execute(0f, b)

                        val localIndex = findIndex(tokens[i], tokens[i + 1], list = tokenSwap)
                        for (j in 0..1) {
                            tokenSwap.removeAt(localIndex)
                        }

                        tokenSwap.add(localIndex, v)
                        topBreak = true
                        break
                    } else if(o.requireLeftValue()) {
                        val a = tokens[i - 1] as? Float ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i - 1]}")

                        val v = o.execute(a, 0f)

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

            if(topBreak)
                break
        }

        tokens.clear()
        tokens.addAll(tokenSwap)
    }
}

/**
 * находит ииндекс в заданном списке ArrayList
 */
fun findIndex(vararg objs: Any, list: ArrayList<Any>) = list.indices.firstOrNull { i -> list[i] == objs[0] && objs.indices.none { list[i + it] != objs[it] } } ?: -1

const val VALID_VAR_NAMES = "abcdefghijklmnoqprstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" //функция

/** различные проверки, описыаем переменные и функции */
class ExpressionEnvironment {

    private val vars = HashMap<Char, Float>() // создаем переменную с ключом Char и значением Float
    private val funcs = HashMap<Pair<Char, Char>, String>() // создаем переменную с функциями с парой !!!!!!!!!!!!!!

    /** так как запись ведется через map, можем добавить новую запись через .put */
    fun putVariable(name: Char, value: Float) = vars.put(name, value) // функция c переменными
    fun putFunction(function: Pair<Char, Char>, value: String) = funcs.put(function, value) //функция с функциями..

    fun parseAndPrint(exprRaw: String) {

        var varname: Char? = null //
        var expr: String = exprRaw.replace("\\s".toRegex(), "") //входная строка для обработки без пробелов и табов
        /** проверка на функцию или переменную */
        if (expr[0] == '!') { //если есть ключевой символ
            /** если функция */
            if (expr[2] == '(' && expr[1] == ')' && expr[5] == '=') {
                val funcName = expr[1] // имя переменной
                val funcVar = expr[3] // имя переменной

                if (!VALID_VAR_NAMES.contains(funcName)) { // проверка на валидность имени аргумента
                    throw IllegalArgumentException("Некорректное имя функци $varname.")
                }
                if (!VALID_VAR_NAMES.contains(funcVar)) { // проверка на валидноть имени аргумента
                    throw IllegalArgumentException("Некорректное имя аргумента $varname.")
                }

                putFunction(funcName to funcVar, expr.substring(6)) //  пополняем функцию с функциями
                println("$funcName($funcVar)=${funcs[funcName to funcVar]}") // выводим фунцию
                return // локальный возврат
            } else {
                /** если не функция, то переменная */
                varname = expr[1] // имя переменной
                if (!VALID_VAR_NAMES.contains(varname)) {
                    throw IllegalArgumentException("Некорректное имя переменной $varname")
                }
                if (expr[2] != '=') {
                    throw IllegalArgumentException("Имя переменной - один символ, далее - '=' ")
                }
                expr = expr.substring(3) // подстрока с индекса 3

            }
        } else if (expr[0] == '?') {
            // Вывод функции по запросу
            if (expr.length >= 5 && expr[2] == '(' && expr[4] == ')') {

                val funcName = expr[1]
                val funcVar = expr[3]

                if (!VALID_VAR_NAMES.contains(funcName)) {
                    throw IllegalArgumentException("Некорректное имя функции $varname.")
                }
                if (!VALID_VAR_NAMES.contains(funcVar)) {
                    throw IllegalArgumentException("Некорректный аргумент функции $varname")
                }

                println("$varname=${vars[varname]}")
                return // локальный возврат
            }
        }
        /**вызов функции с заданной переменной*/
        for ((key, value) in funcs) { // пока в функции есть значение
            while (expr.contains("${key.first}(")) { // пока не пусто?
                val funStart = expr.indexOf("${key.first}") + 2

                var bracketDepth = 0
                for (seek in funStart until expr.length) {  //
                    if (expr[seek] == '(') {
                        bracketDepth++
                    } else if (expr[seek] == ')') {
                        if (bracketDepth == 0) {
                            val funContents = expr.substring(funStart, seek)
                            val funReplaced = "(" + value.replace(key.second.toString(), "($funContents)" + ")")
                            expr = expr.replaceRange(funStart - 2, seek + 1, funReplaced) // замена функции на обработанную
                            bracketDepth = -1
                            break
                        } else {
                            bracketDepth--
                        }
                    }
                }

                if (bracketDepth != -1) {
                    throw IllegalArgumentException("Ошибка расстановки скобок")
                }
            }
        }
        /** неявное умножение и знак числа*/
        for (c in VALID_VAR_NAMES) { // перебор всех разрешенных символов
            if (vars.containsKey(c)) { // если переменная определена

                while (expr.contains(c)) { // пока c содержится во входной строке
                    var mulLeft = false //
                    var mulRight = false

                    var index = -1

                    for (i in expr.indices) { // поиск по всей строке
                        if (expr[i] == c) { //
                            if (i > 0) { //
                                val left = expr[i - 1]
                                if (left.isLetterOrDigit()) { // проверка на букву или символ
                                    mulLeft = true
                                }
                            }
                            if (i < expr.length - 1) {
                                val right = expr[i + 1]
                                if (right.isLetterOrDigit()) {
                                    mulRight = true
                                }
                            }
                            index = i
                            break
                        }
                    }

                    val replaceWith = (if (mulLeft) "*" else "") + (vars[c].toString().replace("-", "_")) + (if (mulRight) "*" else "")
                    expr = expr.replaceRange(index, index + 1, replaceWith)
                }
            } else if (expr.contains(c)) {
                throw IllegalArgumentException("Неопределенная переменная: $c")
            }
        }

        var bracketsResolved: Boolean
        while (true) {
            bracketsResolved = true

            for (i in expr.indices) {
                val c = expr[i]
                if (c == '(' && i > 0) {
                    if ((expr[i - 1].isLetterOrDigit() || expr[i - 1] == ')') && !Operation.isOperator(expr[i - 1])) {
                        // добавляем умножение
                        expr = expr.replaceRange(i, i + 1, "*(")
                        bracketsResolved = false
                        break
                    }
                }
                else if (c == ')' && i < expr.length - 1) {
                    if ((expr[i + 1].isLetterOrDigit() || expr[i + 1] == '(') && !Operation.isOperator(expr[i + 1])) {
                        expr = expr.replaceRange(i, i + 1, ")*")
                        bracketsResolved = false
                        break
                    }
                }
            }

            if(bracketsResolved) {
                break
            }
        }

        val v = Value(expr) // class Value!!!
        v.resolve()

        if(varname != null) {
            print("$varname=")
            putVariable(varname, v.resolvedValue)
        }


        println(v.resolvedValue)
    }

}
