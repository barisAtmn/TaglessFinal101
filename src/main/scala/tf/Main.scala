package tf

import cats.effect.Sync

trait Console[F[_]] {
  def info(in: Any): F[Unit]
  def error(in: Any): F[Unit]
}

object Console {
  def make[F[_]: Sync]: Console[F] =
    new Console[F] {
      import scala.Console._

      override def info(in: Any): F[Unit] = F.delay(println(GREEN + in + RESET))

      override def error(in: Any): F[Unit] = F.delay(println(RED + in + RESET))
    }
}
object Main extends App {}
