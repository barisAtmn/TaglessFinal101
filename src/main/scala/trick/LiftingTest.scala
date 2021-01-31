package trick

import scala.util.chaining._

object LiftingTest extends App {

  import cats.data.EitherT
  import cats.implicits._

  EitherT
    .liftF("a".some)
    .tap(println)
  //EitherT(Some(Right(a)))

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  EitherT
    .liftF(none[String])
    .tap(println)
  //EitherT(None)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  val intMatcher: PartialFunction[Int, String] = {
    case 1 => "Hello World!"
  }

  val liftedIntMatcher: Int => Option[String] = intMatcher.lift

  liftedIntMatcher(1)
    .pipe(data => Console.BLUE + data + Console.RESET)
    .tap(println)

  liftedIntMatcher(0)
    .pipe(data => Console.BLUE + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  ("a".some |+| "b".some)
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("a".some, "b".some)
    .mapN(_ ++ _)
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

}
