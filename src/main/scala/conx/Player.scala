package conx

abstract class Player {
  val name: String

  def play(grid: Grid, number: Int): Int

  override def toString: String = name
}

object Player {
  def apply(style: String): Player = style match {
    case "random" => new RandomPlayer
    case "console" => new ConsolePlayer
    case "ai" => new AiPlayer
  }
}

class AiPlayer extends Player {
  val name = "AI"
  def play(grid: Grid, number: Int): Int = ConxAi.play(grid, number)
}

class RandomPlayer extends Player {
  val name = "Random"

  import scala.util.Random
  def play(grid: Grid, number: Int): Int =
    grid.openCols(Random.nextInt(grid.openCols.size))
}

class ConsolePlayer extends Player {
  val name = "Console"
  def play(grid: Grid, number: Int): Int = Console.readInt match {
    case x if (grid.openCols contains x) => x
    case x if ((0 until grid.width) contains x) => play(grid, number)
    case 0 => sys.error("terminate")
    case _ => play(grid, number)
  }
}
