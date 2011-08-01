package conx

object Conx extends App {
  //val game = Game((7,6), "ai", "ai")
  //val game = Game((7,6), "ai", "random")
  val game = Game((7, 6), "ai", "ai")
  //val game = Game((7,6), "console", "random")
  //val game = Game((7,6), "random", "random")
  do {
    println(game)
    game.next()
  } while (!game.isFinished)
  println(game)
}
