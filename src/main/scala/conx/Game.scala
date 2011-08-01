package conx

class Game(var grid: Grid, p1: Player, p2: Player) {
  val players = Map(1 -> p1, 2 -> p2)
  var turn = 1

  def next() {
    grid = grid add (player.play(grid, playerNumber), playerNumber)
    turn += 1
  }

  def playerNumber: Int = if (1 == turn % 2) 1 else 2

  def player: Player = players(playerNumber)

  def isFinished: Boolean = isFull || 0 != winnerNumber

  def winnerNumber: Int = grid.winnerNumber

  def isFull: Boolean = grid.isFull

  override def toString: String = {
    //val repr = "1:%s vs 2:%s, turn %d\n%s" format (players(1), players(2), turn, grid)
    val repr = "-------------------\n" + grid.toString
    isFinished match {
      case false => repr
      case true => winnerNumber match {
        case 0 => repr + "\nDRAW"
        case n => "%s\n%d:%s WINS" format (repr, n, players(n))
      }
    }
  }
}

object Game {
  def apply(size: Pair[Int, Int], p1Style: String, p2Style: String) =
    new Game(Grid(size), Player(p1Style), Player(p2Style))
}
