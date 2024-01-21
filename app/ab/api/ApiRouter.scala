package ab.api

import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import javax.inject.Inject

class ApiRouter @Inject() (routesProvider: ApiRoutes) extends SimpleRouter {
  override def routes: Routes = {
    routesProvider.tapirRoutes
  }
}
