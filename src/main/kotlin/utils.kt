import org.mariuszgromada.math.mxparser.Expression
import kotlin.math.abs
<<<<<<< HEAD
import kotlin.math.min
=======
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276

/* Как работают функции типа String. или любой другой объект Int.
Допустим, у тебя есть какой-то объект из "чужого" API, методы которого ты не сможешь изменить.
Но тебе очень хочется. Ты можешь таким образом "добавить" нужный метод.
Например, у тебя есть строка String, ты хочешь чтобы был метод "calculate",
который что-то делает. В итоге, ты объявляешь функцию вот так:
fun String.calculate() ....
Это означает, что новый метод будет существовать для всех строк.
Такие методы удобные, но их лучше использовать, когда нужно сделать что-то небольшое.
 */

/**
 * Это вспомогательная функция, чтобы можно было писать как-то так:
 * <code>"1+1".calculate()</code>
 */
fun String.calculate(): Double = Expression(this).calculate()

/**
 * Производная
 * @param expression это само выражение
 * @param argument это значение аргумента
 */
fun der(expression: String, argument: Double) = "der($expression, x, $argument)".calculate()

<<<<<<< HEAD
//TODO добавить расчетное время работы функций, графики, выгрузить теорию и тесты
//TODO разобраться в каких случаях работает итерация, она работает с примером с сайта
//TODO попробовать выводить случаи если корень посчитан точно, оформить выводы методов
//TODO тесты, разобраться с тестами
=======
//TODO функция второй производной, добавить расчетное время работы функции, доделать итарацию,убрать дичь на 50-й строке
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276

/**
 * Вторая производная. Функция dern работает с ошибкой, поэтому используем der(der()).
 * Не факт что это работает, стоит проверить.
 * @param expression это само выражение
 * @param argument это значение аргумента
 */
fun secDer(expression: String, argument: Double) = "der(der($expression, x, x), x, $argument)".calculate()

<<<<<<< HEAD
//fun SecDer(expression: String, argument: Double) = "der($expression,x,$argument + 0.000001) - "
=======
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
//получаем x-3x
// расширение класса  без наследования, https://tproger.ru/articles/kotlin-magic-functions/
fun String.replaceAndCount(value: Double): Double = replace("x", "$value").calculate()

<<<<<<< HEAD
//
=======
// TODO сделать вывод первых трех уточнений корня, погрешности, итерации,если корень посчитан точно, то сообщить и т.д.
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276

fun bisection(left: Double, right: Double, accuracy: Double, function: String): Double {
    var newLeftEdge: Double = left
    var newRightEdge: Double = right
    var result: Double = newLeftEdge
<<<<<<< HEAD
    var iter = 0

    while (newRightEdge - newLeftEdge >= accuracy) {
        iter++
        result = (newLeftEdge + newRightEdge) / 2.0
=======

    while (newRightEdge - newLeftEdge >= accuracy) {
        result = (newLeftEdge + newRightEdge) / 2
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276

        val fResult = function.replaceAndCount(result)
        val fLeft = function.replaceAndCount(newLeftEdge)

<<<<<<< HEAD

=======
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
        if (fResult == 0.0) {
            break
        }
        if (fResult * fLeft < 0) {
            newRightEdge = result
        } else {
            newLeftEdge = result
        }
<<<<<<< HEAD
        println("$iter . left - $newLeftEdge | right - $newRightEdge | x - $result")
    }

=======
    }

    println(secDer(function, 1.0))

>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
    return result
}

fun combination(left: Double, right: Double, accuracy: Double, function: String): Double {
<<<<<<< HEAD
    //http://cyclowiki.org/wiki/%D0%9A%D0%BE%D0%BC%D0%B1%D0%B8%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D1%8B%D0%B9_%D0%BC%D0%B5%D1%82%D0%BE%D0%B4
    //
    fun startTimer() {}

    var a: Double = left //левый край
    var b: Double = right // правый край
    var iter = 0

    while (abs(b - a) > 2 * accuracy) {

        val fA = function.replaceAndCount(a)
        val fB = function.replaceAndCount(b)



        if (fA * secDer(function, a) < 0) {
            a -= (fA * (b - a) / (fB - fA))
            b -= (fB / der(function, b))
        }
        if (fA * secDer(function, a) > 0) {
            a -= fA / der(function, a)
            b -= fB * (b - a) / (fB - fA)
        }
        if (secDer(function, a) * secDer(function, b) == 0.0) {
            println("Вторая производная = 0, нельзя решить данным методом ")
            break
        }
    }
        return ((a + b) / 2.0)

}

fun iteration(left: Double,right: Double,accuracy: Double,function: String):Double{


    var iter = 0
    var x1 = 2.0
do {
    println("$iter. x1 = $x1")
    var x0 = x1
    x1 = function.replaceAndCount(x0)
    iter++
    if ((x0 < left) or (x0 > right)) {
        println("Ошибка")
        break
    }
} while (abs(x0 - x1) > accuracy)

    return x1
}
=======

    //http://cyclowiki.org/wiki/%D0%9A%D0%BE%D0%BC%D0%B1%D0%B8%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D1%8B%D0%B9_%D0%BC%D0%B5%D1%82%D0%BE%D0%B4
    //

    var a: Double = left //левый край
    var b: Double = right // правый край
    val fA = function.replaceAndCount(left) //функция от a
    val fB = function.replaceAndCount(right) // функция от b

    while (abs(a - b) > 2 * accuracy) {
        when {
            fA * secDer(function, a) < 0 -> a -= (fA * (b - a)) / (fB - fA)
            fA * secDer(function, a) > 0 -> a -= fA / der(function, a)
            fB * secDer(function, b) < 0 -> b -= fB * (b - a) / (fB - fA)
            fB * secDer(function, b) > 0 -> b -= fB / der(function, b)
        }
    }
    return (a + b) / 2.0
}
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
