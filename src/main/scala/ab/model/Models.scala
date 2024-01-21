package ab.model

case class Item(id: Int, name: String, flag: Boolean)
case class NewItem(name: String, flag: Boolean)
case class ErrorInfo(message: String)
