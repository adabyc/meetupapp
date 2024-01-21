package object config {

  implicit class DoobieDbConfig(val dbCfg: DbConfig) {
    import com.zaxxer.hikari.HikariConfig

    def toDbConfig: HikariConfig = {
      val config = new HikariConfig()
      config.setDriverClassName(dbCfg.driver)
      config.setJdbcUrl(dbCfg.dbConnection)
      config.setUsername(dbCfg.username)
      config.setPassword(dbCfg.password)
      config
    }
  }
}
