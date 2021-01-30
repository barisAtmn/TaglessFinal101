//package tf
//
//import cats.data.EitherT
//import cats.{ApplicativeError, MonadError}
//import cats.effect.{IO, Sync}
//import tf.BusinessError.ErrorWithMessage
//
//import scala.util.chaining._
//
//sealed abstract class BusinessError extends Product with Serializable
//
//object BusinessError {
//  case object Error extends BusinessError
//  final case class ErrorWithMessage(message: String) extends BusinessError
//}
//
//trait Console[F[_]] {
//  def info(in: Any): F[Unit]
//  def error(in: Any): F[Unit]
//}
//
//object Console {
//  def make[F[_]: Sync]: Console[F] =
//    new Console[F] {
//      import scala.Console._
//
//      override def info(in: Any): F[Unit] = F.delay(println(GREEN + in + RESET))
//
//      override def error(in: Any): F[Unit] = F.delay(println(RED + in + RESET))
//    }
//}
//
//trait ErrorProducer[F[_]] {
//  def technicalError: F[Int]
//  def businessError: F[Int]
//}
//
//object ErrorProducer {
//  def make[F[_]](implicit F: ApplicativeError[F, Throwable],
//                 G: ApplicativeError[F, BusinessError]) =
//    new ErrorProducer[F] {
//      override def technicalError: F[Int] =
//        F.raiseError(new RuntimeException("technical"))
//
//      override def businessError: F[Int] =
//        G.raiseError(ErrorWithMessage("business"))
//    }
//}
//
//trait ErrorHandler[F[_]] {
//  def technicalHandler: F[Unit]
//  def businessHandler: F[Unit]
//}
//
//object ErrorHandler {
//  def make[F[_]](
//    errorProducer: ErrorProducer[F],
//    console: Console[F]
//  )(implicit F: MonadError[F, Throwable], G: MonadError[F, BusinessError]) =
//    new ErrorHandler[F] {
//      override def technicalHandler: F[Unit] =
//        F.handleErrorWith(
//          F.flatMap(errorProducer.technicalError)(console.error)
//        )(console.info)
//
//      override def businessHandler: F[Unit] =
//        G.handleErrorWith(
//          G.flatMap(errorProducer.businessError)(console.error)
//        )(console.info)
//    }
//}
//
//object Main2 extends App {
//  val handler: ErrorHandler[EitherT[IO, BusinessError, *]] =
//    ErrorHandler.make(ErrorProducer.make, Console.make)
//  handler.technicalHandler.value.unsafeRunSync().pipe(println)
//  println(scala.Console.YELLOW + "-" * 100)
//  handler.businessHandler.value.unsafeRunSync().pipe(println)
//}
