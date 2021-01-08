
==> In TF, DSLs are trait.

==> F[_] => F means effect.

==> Interpreters have all the power.

==> Functor can change type categories. Like moving from Int category to List[Int] category.

==> Applicative can change value categories. Like moving 5 to Option[5].

==> Monad Transformers are slow!!!
- EitherT[F[_], A, B] is a lightweight wrapper for F[Either[A, B]]

==> Helper for TF --> addCompilerPlugin("org.augustjune" %% "context-applied" % "x")

==> cats-effect for concurrency

==> Cats / Sync ==>  suspend is useful for trampolining

==> flatMap is stack safe!!!

==> EitherT[IO, NonEmptyChain[String], Int] ==> IO[Either[NonEmptyChain[String], Int]]

* Apply                 = Functor with InvariantSemigroupal with ApplyArityFunctions
* Applicative           = Apply + Pure
* Monad                 = Applicative + FlatMap
* ApplicativeError      => ApplicativeError[F[_], E] extends Applicative[F]
* MonadError            = ApplicativeError[F,E] with Monad[F]
* MonadError[F, String] = "division by zero".raiseError[F, Int]
  ** For Context Bound ==> ( F[_] : MonadError[*[_],String])
  