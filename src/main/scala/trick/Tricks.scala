package trick

import cats.{Applicative, Bifunctor, Functor, Monad, Monoid, Traverse}

import scala.util.chaining._

object Tricks extends App {

  def fn[F[_]: Monad: Traverse, A](fs: F[A]) =
    F.map(fs)(_.toString.size)

  fn(List("3487", "87687"))
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  def f[F[_]: Applicative, B[_, _]: Bifunctor, A: Monoid] = {
    val fa: F[A] = F.pure(A.empty)
    val rf: Functor[B[A, *]] = B.rightFunctor[A]
  }

}
