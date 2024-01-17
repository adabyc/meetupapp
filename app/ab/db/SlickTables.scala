package ab.db

import ab.model.Item
import slick.jdbc.PostgresProfile

object SlickTables {

  protected val profile = PostgresProfile

  import profile.api._

  class ItemsTable(tag: Tag) extends Table[Item](tag, Some("public"), "items") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def flag = column[Boolean]("flag")

    override def * = (id, name, flag) <> (Item.tupled, Item.unapply)
  }

  lazy val Items: TableQuery[ItemsTable] = TableQuery[ItemsTable]
}
