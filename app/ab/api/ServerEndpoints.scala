package ab.api

import ab.service.MyService
import sttp.tapir.server.ServerEndpoint
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ServerEndpoints @Inject() (service: MyService) extends Endpoints {

  val all: List[ServerEndpoint[Any, Future]] =
    addNewItemEndpoint.serverLogic(service.addNewItem) ::
      getAllItemsEndpoint.serverLogic(_ => service.getAllItems()) ::
      Nil
}
