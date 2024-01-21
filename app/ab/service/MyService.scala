package ab.service

import ab.ApplicationException
import ab.db.ItemDAOAlg
import ab.model.{ErrorInfo, Item, NewItem}
import cats.MonadError
import cats.data.ValidatedNel
import cats.implicits._
import com.google.inject.ImplementedBy
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@ImplementedBy(classOf[MyServiceImpl])
trait MyService[F[_]] {
  def getAllItems(): F[Either[ErrorInfo, Seq[Item]]]

  def addNewItem(newItem: NewItem): F[Either[ErrorInfo, Item]]
}

abstract class MyServiceInterpreter[F[_]](dao: ItemDAOAlg[F])(implicit ME: MonadError[F, Throwable])
    extends MyService[F] {

  def addNewItem(newItem: NewItem): F[Either[ErrorInfo, Item]] = {
    for {
      _ <- validate(newItem)
      processedNewItem <- processRequest(newItem)
      item <- persistNewItem(processedNewItem)
    } yield item.asRight[ErrorInfo]
  }.recoverWith { case e: ApplicationException =>
    ErrorInfo(e.getMessage).asLeft[Item].pure[F]
  }

  /** For presentation purpose Validated is not being used.
    */
  private def validate(newItem: NewItem): F[Unit] =
    (validateName(newItem), validateFlag(newItem))
      .mapN((_, _) => ())
      .leftMap(errors => new ApplicationException(errors.mkString_(" & ")))
      .liftTo[F]

  private def validateName(newItem: NewItem): ValidatedNel[String, Unit] =
    if (newItem.name.length > 3) ().validNel
    else "Name too short".invalidNel

  private def validateFlag(newItem: NewItem): ValidatedNel[String, Unit] =
    if (newItem.flag) ().validNel
    else "Flag has to be set".invalidNel

  private def processRequest(newItem: NewItem): F[NewItem] =
    newItem.copy(name = newItem.name.capitalize).pure[F]

  protected def persistNewItem(newItem: NewItem): F[Item]
}

@Singleton
class MyServiceImpl @Inject() (dao: ItemDAOAlg[Future])(implicit ec: ExecutionContext)
    extends MyServiceInterpreter[Future](dao) {

  override def getAllItems(): Future[Either[ErrorInfo, Seq[Item]]] =
    dao.all().map(_.reverse.asRight[ErrorInfo])

  protected def persistNewItem(newItem: NewItem): Future[Item] = dao.add(newItem)
}
