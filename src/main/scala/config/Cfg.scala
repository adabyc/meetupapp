package config

case class Cfg(dbConfig: DbConfig)
case class DbConfig(driver: String, dbConnection: String, username: String, password: String)
