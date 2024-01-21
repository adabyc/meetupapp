package ab.db

import ab.model.{Item, NewItem}
import cats.effect.Async
import doobie.hikari.HikariTransactor
import doobie.implicits._

trait ItemDAO[F[_]] {
  def all(): F[Seq[Item]]
  def add(newItem: NewItem): F[Item]
}

object ItemDAO {
  def apply[F[_]: Async](xa: HikariTransactor[F]): ItemDAO[F] = new ItemDAO[F] {

    override def all(): F[Seq[Item]] =
      sql"select id, name, flag from items"
        .query[Item]
        .to[Seq]
        .transact(xa)

    override def add(newItem: NewItem): F[Item] = {

      sql"insert into items(name, flag) values(${newItem.name}, ${newItem.flag})".update
        .withUniqueGeneratedKeys[Item]("id", "name", "flag")
        .transact(xa)
    }
  }
}
