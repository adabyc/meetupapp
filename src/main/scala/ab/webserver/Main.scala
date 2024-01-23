package ab.webserver

import ab.api.ServerEndpoints
import ab.config.Cfg
import ab.db.ItemDAO
import ab.service.MyService
import cats.effect.kernel.Resource
import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.implicits.catsSyntaxApplicativeId
import doobie.hikari.HikariTransactor
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import scala.util.control.NonFatal

object Main extends IOApp {

  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- Logger[IO].info("Starting MeetUp App...")
      cfg <- loadConfig()
      _ <- buildAndRun(cfg)
    } yield ExitCode.Success
  }.recoverWith { case NonFatal(e) =>
    Logger[IO].error(e)(s"Application has terminated with error: ${e.getMessage}") *> ExitCode.Error.pure[IO]
  }

  private def loadConfig(): IO[Cfg] =
    ConfigSource.default.load[Cfg] match {
      case Left(failures) =>
        IO.raiseError(new Exception(failures.prettyPrint()))
      case Right(cfg) =>
        Logger[IO].info(s"CONFIGURATION:\n$cfg") *> cfg.pure[IO]
    }

  private def buildAndRun(cfg: Cfg): IO[Unit] =
    mkDbTransactor(cfg).use { xa =>
      val dao = ItemDAO[IO](xa)
      val service = MyService[IO](dao)
      new EmberServer(new ServerEndpoints[IO](service).all).run().void
    }

  private def mkDbTransactor(cfg: Cfg): Resource[IO, HikariTransactor[IO]] = {
    HikariTransactor.fromHikariConfig[IO](cfg.dbConfig.toDbConfig)
  }
}
