package ab.api

import ab.model.{ErrorInfo, Item, NewItem}
import io.circe.generic.auto._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.{EndpointInput, EndpointOutput, PublicEndpoint, endpoint}

trait Endpoints {

  private val jsonInput: EndpointInput[NewItem] = jsonBody[NewItem]
  private val jsonOutput: EndpointOutput[Item] = jsonBody[Item]
  private val jsonListOutput: EndpointOutput[Seq[Item]] = jsonBody[Seq[Item]]

  private val errorOutput: EndpointOutput[ErrorInfo] = jsonBody[ErrorInfo]

  private lazy val apiV1Endpoint: PublicEndpoint[Unit, ErrorInfo, Unit, Any] =
    endpoint
      .in("api")
      .in("v2")
      .errorOut(errorOutput)

  protected val addNewItemEndpoint: PublicEndpoint[NewItem, ErrorInfo, Item, Any] = apiV1Endpoint
    .in("addnew")
    .in(jsonInput)
    .out(jsonOutput)

  protected val getAllItemsEndpoint: PublicEndpoint[Unit, ErrorInfo, Seq[Item], Any] = apiV1Endpoint
    .in("getall")
    .out(jsonListOutput)
}
