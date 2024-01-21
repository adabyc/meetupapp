package ab.db

import ab.model.{Item, NewItem}
import com.google.inject.ImplementedBy
import scala.concurrent.Future

@ImplementedBy(classOf[SlickItemDao])
trait ItemDAOAlg[F[_]] {

  def all(): F[Seq[Item]]

  def add(newItem: NewItem): F[Item]
}

trait ItemDAO extends ItemDAOAlg[Future] {

  def all(): Future[Seq[Item]]

  def add(newItem: NewItem): Future[Item]
}
