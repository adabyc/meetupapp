package controllers

import ab.model._
import ab.service.MyService
import play.api.libs.json._
import play.api.mvc._
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject() (service: MyService[Future], val controllerComponents: ControllerComponents)(implicit
    ec: ExecutionContext
) extends BaseController {

  implicit val itemJsonFormat: OFormat[Item] = Json.format[Item]
  implicit val newItemJsonFormat: OFormat[NewItem] = Json.format[NewItem]

  def getAll: Action[AnyContent] = Action.async {
    service.getAllItems().map {
      case Right(items) => Ok(Json.toJson(items))
      case Left(_)      => InternalServerError
    }
  }

  def addNew(): Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson
      .map { jsonReq =>
        Json.fromJson[NewItem](jsonReq).asEither match {
          case Right(newItem) =>
            service.addNewItem(newItem).map {
              case Right(item) => Ok(Json.toJson(item))
              case Left(err)   => UnprocessableEntity(err.message)
            }
          case Left(_) => Future.successful(BadRequest("Wrong JSON"))
        }
      }
      .getOrElse(Future.successful(BadRequest("Invalid JSON")))
  }

}
