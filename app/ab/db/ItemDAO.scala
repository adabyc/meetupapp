package ab.db

import ab.model.{Item, NewItem}
import com.google.inject.ImplementedBy
import scala.concurrent.Future

@ImplementedBy(classOf[SlickItemDao])
trait ItemDAO {

  def all(): Future[Seq[Item]]

  def add(newItem: NewItem): Future[Item]
}
