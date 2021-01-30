package trick

import cats.{Id}
import cats.data.{Kleisli, Reader}
import cats.effect.{IO}
import cats.implicits._
import cats.effect.IO._
import scala.util.chaining._
import scala.util.Random

/**
  * The Reader Monad lets us encode dependencies directly into the type,
  * while providing composability(it provides map, flatMap like composable functions).
  *
  * The main concern of the Constructor Injection method is, the functions required Repo as its
  * argument(e.g getUser() and createUser() required the Repo as a parameter).
  * It warp functions with Reader where we can pass the Repo in run time. The return type of the function would be Reader[Repo, _]
  *
  * Scala offers two syntactic styles for declaring implicit dependencies: implicit parameters and context bounds.
 **/
object ReaderMonad extends App {

  case class User(id: Long, name: String, role: String)
  case class Permission(id: Long, role: String, name: String)

  case class Repo(userRepo: UserRepo[IO], permRepo: PermissionRepo[IO])

  trait UserRepo[F[_]] {
    def create(user: User): F[Long]
    def get(id: Long): F[User]
  }

  class UserRepoImpl extends UserRepo[IO] {
    override def create(user: User) = {
      println(s"user created $user")
      IO.apply(user.id)
    }

    override def get(id: Long) = {
      println(s"get user $id")
      val roles = List("admin", "manager", "legal_officer", "user")
      IO.apply(User(id, s"lambda$id", Random.shuffle(roles).head))
    }
  }

  trait PermissionRepo[F[_]] {
    def create(permission: Permission): F[Long]
    def get(id: Long): F[Permission]
    def search(role: String): F[List[Permission]]
  }

  class PermissionRepoImpl extends PermissionRepo[IO] {
    override def create(permission: Permission) = {
      println(s"permission created $permission")
      IO.apply(permission.id)
    }

    override def get(id: Long) = {
      println(s"get permission $id")
      val roles = List("admin", "manager", "legal_officer", "user")
      val perms =
        List("use_archive", "use_doc_storage", "use_schema", "use_auth")
      IO.apply(
        Permission(id, Random.shuffle(roles).head, Random.shuffle(perms).head)
      )
    }

    override def search(role: String) = {
      IO.apply(
        List(
          Permission(1001, role, "user_archive"),
          Permission(1002, role, "use_doc_storage"),
          Permission(1005, role, "use_auth")
        )
      )
    }
  }

  object UserHandlerWithMonad {
    def createUser(user: User) = {
      Reader((repo: Repo) => {
        repo.userRepo.create(user)
      })
    }

    def getUser(id: Long) = {
      Reader((repo: Repo) => {
        repo.userRepo.get(id)
      })
    }
  }

  object PermissionHandlerWithMonad {
    def createPermission(permission: Permission) = {
      Reader((repo: Repo) => {
        repo.permRepo.create(permission)
      })
    }

    def getPermission(id: Long) = {
      Reader((repo: Repo) => {
        repo.permRepo.get(id)
      })
    }

    def searchPermissions(role: String) = {
      Reader((repo: Repo) => {
        repo.permRepo.search(role)
      })
    }
  }

  object UserPermissionHandlerWithMonad {
    def getUserPermissions(id: Long): Kleisli[Id, Repo, List[Permission]] = {
      for {
        u <- UserHandlerWithMonad.getUser(id)
        p <- PermissionHandlerWithMonad.searchPermissions(
          u.unsafeRunSync().role
        )
      } yield p.unsafeRunSync()
    }
  }

  val userRepo = new UserRepoImpl
  val permRepo = new PermissionRepoImpl
  val repo = Repo(userRepo, permRepo)

  // create user
  UserHandlerWithMonad
    .createUser(User(1001, "lambda", "admin"))
    .run(repo)
    .unsafeRunSync()
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  // get user
  UserHandlerWithMonad
    .getUser(1001)
    .run(repo)
    .unsafeRunSync()
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

  ("-" * 100)
    .pipe(data => Console.YELLOW + data + Console.RESET)
    .tap(println)

  // create permission
  PermissionHandlerWithMonad
    .createPermission(Permission(2001, "admin", "use_auth"))
    .run(repo)
    .unsafeRunSync()
    .pipe(data => Console.GREEN + data + Console.RESET)
    .tap(println)

}
