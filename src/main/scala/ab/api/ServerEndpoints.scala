package ab.api

import ab.service.MyService
import sttp.tapir.server.ServerEndpoint

class ServerEndpoints[F[_]](service: MyService[F]) extends Endpoints {

  val all: List[ServerEndpoint[Any, F]] =
    addNewItemEndpoint.serverLogic(service.addNewItem) ::
      getAllItemsEndpoint.serverLogic(_ => service.getAllItems()) ::
      Nil
}
