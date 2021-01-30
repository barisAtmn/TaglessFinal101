package trick

import scala.util.chaining._

/**
  * final case class Kleisli[F[_], A, B](run: A => F[B])
  *
 **/
object KleisliTest extends App {

  val mul2: Int => Int = _ * 2

  val power2: Int => Double = Math.pow(_, 2)

  val doubleToInt: Double => Int = _.toInt

  val intToString: Int => String = _.toString

  val pipeline
    : Int => String = intToString compose mul2 compose doubleToInt compose power2

  pipeline(3)
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  def stringToNonEmptyString: String => Option[String] =
    value => if (value.nonEmpty) Option(value) else None

  def stringToNumber: String => Option[Int] =
    value => if (value.matches("-?[0-9]+")) Option(value.toInt) else None

  /**
    * Kleisli
    * def compose(g: A => F[B], f: B => F[C])(implicit M: Monad[F]): A => F[C]
    *
    * A => F[B] //g
    * B => F[C] //f
    *
    * A => F[C] //f compose g
    *
    *
    * !!! Cant work
    * ----------------------
    * val pipeline2
    * : String => Option[Int] = stringToNumber compose stringToNonEmptyString
    *
    *
    *
    *
   **/
  import cats.data.Kleisli
  import cats.implicits._ //Brings in a Monadic instance for Option

  /**
    * String => Option[String]
   **/
  val stringToNonEmptyStringK = Kleisli(stringToNonEmptyString) //Kleisli[Option, String, String]

  /**
    * String => Option[Int]
   **/
  val stringToNumberK = Kleisli(stringToNumber) //Kleisli[Option, String, Int]

  val pipeline2 = stringToNumberK compose stringToNonEmptyStringK //Kleisli[Option, String, Int]

  pipeline2("3")
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  val pipeline3
    : String => Option[Int] = Option(_) >>= stringToNonEmptyString >>= stringToNumber

  pipeline3("3")
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)
}
