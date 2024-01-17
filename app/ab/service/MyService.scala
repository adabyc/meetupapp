package ab.service

import ab.ApplicationException
import ab.db.ItemDAO
import ab.model.{ErrorInfo, Item, NewItem}
import cats.implicits._
import com.google.inject.ImplementedBy
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[MyServiceImpl])
trait MyService {
  def getAllItems(): Future[Either[ErrorInfo, Seq[Item]]]

  def addNewItem(newItem: NewItem): Future[Either[ErrorInfo, Item]]
}

@Singleton
class MyServiceImpl @Inject() (dao: ItemDAO)(implicit ec: ExecutionContext) extends MyService {

  override def getAllItems(): Future[Either[ErrorInfo, Seq[Item]]] =
    dao.all().map(_.reverse.asRight[ErrorInfo])

  override def addNewItem(newItem: NewItem): Future[Either[ErrorInfo, Item]] = {
    for {
      _ <- validate(newItem)
      processedNewItem <- processRequest(newItem)
      item <- persistNewItem(processedNewItem)
    } yield item.asRight[ErrorInfo]
  }.recoverWith { case e: ApplicationException =>
    Future(ErrorInfo(e.getMessage).asLeft[Item])
  }

  private def validate(newItem: NewItem): Future[Unit] =
    (validateName(newItem), validateFlag(newItem)) match {
      case (Left(e1), Left(e2)) => Future.failed(new ApplicationException(s"$e1 $e2"))
      case (Left(err), _)       => Future.failed(new ApplicationException(err))
      case (_, Left(err))       => Future.failed(new ApplicationException(err))
      case _                    => Future.unit
    }

  private def validateName(newItem: NewItem): Either[String, Unit] =
    Either.cond(newItem.name.length > 3, (), "Name too short")
  private def validateFlag(newItem: NewItem): Either[String, Unit] =
    Either.cond(newItem.flag, (), "Flag has to be set")

  private def processRequest(newItem: NewItem): Future[NewItem] = Future.successful {
    newItem.copy(name = newItem.name.capitalize)
  }

  private def persistNewItem(newItem: NewItem): Future[Item] = dao.add(newItem)
}
