package ab.webserver

import cats.effect.{ExitCode, IO, Resource}
import com.comcast.ip4s._
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import sttp.capabilities.fs2.Fs2Streams
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter

class EmberServer(serverEndpoints: List[ServerEndpoint[Any, IO]]) {

  def run(): IO[ExitCode] = {

    val httpApp: HttpApp[IO] = mkHttpApp(serverEndpoints)

    val serverBuilder: EmberServerBuilder[IO] = EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"9000")
      .withHttpApp(httpApp)

    val serverAsResource: Resource[IO, Server] = serverBuilder.build
    serverAsResource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

  private def mkHttpApp(serverEndpoints: List[ServerEndpoint[Fs2Streams[IO], IO]]): HttpApp[IO] =
    Http4sServerInterpreter[IO]().toRoutes(serverEndpoints).orNotFound
}
