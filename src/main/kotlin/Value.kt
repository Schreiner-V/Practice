// TODO слишком большой класс. Функцию resolve надо бы разбить на нескольбо маленьких и простых
data class Value(val textualValue: String) { // получит expr строку

    var resolvedValue = Double.NaN

    /** Разрешает выражение и возвращает его, кэширует вполе resolvedValue */
    fun resolve(): Double {
        try {
            resolvedValue = textualValue.toDouble()
        } catch (e: NumberFormatException) {
        }// исключение числового формата

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
                    val num = textualValue.substring(numStartIndex, index).replace("_", "-").toDouble()
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

            val num = textualValue.substring(numStartIndex, textualValue.length).replace("_", "-").toDouble()
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
        resolvedValue = tokens[0] as Double

        return resolvedValue
    }
}