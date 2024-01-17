package ab.db

import ab.model.{Item, NewItem}
import javax.inject.{Inject, Singleton}
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SlickItemDao @Inject() (dbProvider: DatabaseProvider)(implicit
    ec: ExecutionContext
) extends ItemDAO {

  import dbProvider._
  import SlickTables.Items

  override def all(): Future[Seq[Item]] = {
    db.run(Items.result)
  }

  override def add(newItem: NewItem): Future[Item] = {
    val insertAction =
      Items.returning(Items.map(_.id)) += Item(0, newItem.name, newItem.flag)
    db.run(insertAction).map(id => Item(id, newItem.name, newItem.flag))
  }
}
