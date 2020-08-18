// TODO тут вообще страшно. Надо изучить

const val VALID_VAR_NAMES = "abcdefghijklmnoqprstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
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
                } else if (c == ')' && i < expr.length - 1) {
                    if ((expr[i + 1].isLetterOrDigit() || expr[i + 1] == '(') && !Operation.isOperator(expr[i + 1])) {
                        expr = expr.replaceRange(i, i + 1, ")*")
                        bracketsResolved = false
                        break
                    }
                }
            }

            if (bracketsResolved) {
                break
            }
        }

        val v = Value(expr) // class Value!!!
        v.resolve()

        if (varname != null) {
            print("$varname=")
            putVariable(varname, v.resolvedValue)
        }

        println(v.resolvedValue)
    }

}