package ab.db

import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database
import javax.inject.{Inject, Singleton}

@Singleton
class DatabaseProvider @Inject() {
  lazy val db: JdbcBackend.Database = Database.forConfig("postgres")
}
