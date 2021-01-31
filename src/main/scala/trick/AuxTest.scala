package trick

import cats.effect.{IO, Sync}
import cats.{Functor, Monoid}
import monix.eval.Task

import scala.language.existentials
import scala.util.chaining._

object AuxTest extends App {

  trait Apart[F] {
    type T
    type W[X]

    def apply(f: F): W[T]
  }

  object Apart {
    def apply[F](implicit apart: Apart[F]) = apart

    type Aux[FA, F[_], A] = Apart[FA] { type T = A; type W[X] = F[X] }

    implicit def mk[F[_], R]: Aux[F[R], F, R] = new Apart[F[R]] {
      type T = R
      type W[X] = F[X]

      def apply(f: F[R]): W[T] = f
    }
  }

  def mapZero[Thing, F[_], A](
    thing: Thing
  )(implicit apart: Apart.Aux[Thing, F, A], f: Functor[F], m: Monoid[A]): F[A] =
    f.map(apart(thing))(_ => m.empty)

  mapZero(Option(3))
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  trait Boo[A] {
    type B
    def value: B
  }

  object Boo {
    type Aux[A0, B0] = Boo[A0] { type B = B0 }

    implicit def fi = new Boo[Int] {
      type B = String
      val value = "Hello"
    }
    implicit def fs = new Boo[String] {
      type B = Boolean
      val value = false
    }

  }

  def ciao[T, R](t: T)(implicit f: Boo.Aux[T, R]): R = f.value

  ciao(2)
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  /**
    * It doesnt compile
    * why ?
   **/
  trait Bar[A[_]] {
    type B[_]
    def value: B[_]
  }

  object Bar extends Bar[IO] {
    import monix.eval._

    type Aux[A0[_], B0[_]] = Bar[A0] { type B = B0[_] }
    override type B = Task[String]
    override def value = Task { "Foo" }

  }

  Bar
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

}
