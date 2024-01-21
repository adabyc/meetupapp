package ab.service

import ab.ApplicationException
import ab.db.ItemDAO
import ab.model.{ErrorInfo, Item, NewItem}
import cats.data.ValidatedNel
import cats.effect.{Concurrent, Sync}
import cats.implicits._
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait MyService[F[_]] {
  def getAllItems(): F[Either[ErrorInfo, Seq[Item]]]

  def addNewItem(newItem: NewItem): F[Either[ErrorInfo, Item]]
}

abstract class MyServiceInterpreter[F[_]: Sync](dao: ItemDAO[F]) extends MyService[F] {

  override def getAllItems(): F[Either[ErrorInfo, Seq[Item]]] =
    dao.all().map(_.reverse.asRight[ErrorInfo])

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

  private def persistNewItem(newItem: NewItem): F[Item] = dao.add(newItem)
}

object MyService {
  implicit def logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]

  def apply[F[_]: Sync: Concurrent](dao: ItemDAO[F]): MyService[F] = new MyServiceInterpreter[F](dao) {}
}
