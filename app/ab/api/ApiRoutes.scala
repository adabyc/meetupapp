package ab.api

import org.apache.pekko.stream.Materializer
import play.api.routing.Router.Routes
import javax.inject.{Inject, Singleton}
import sttp.tapir.server.play.PlayServerInterpreter

@Singleton
class ApiRoutes @Inject() (
    serverEndpoints: ServerEndpoints,
    implicit val materializer: Materializer
) extends Endpoints {

  val tapirRoutes: Routes = {
    PlayServerInterpreter().toRoutes(serverEndpoints.all)
  }
}
