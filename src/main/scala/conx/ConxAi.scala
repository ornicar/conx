package conx

import scala.util.Random

private final class ConxAi private (initialGrid: Grid, me: Int) {

  val maxDepth = 6
  var count: Int = 0
  val opponent: Int = if (me == 2) 1 else 2

  def play: Int =
    if (initialGrid.count < 4) initialGrid.width / 2
    else bestMove(initialGrid.openCols map (x =>
      new Move(x, me, 1, initialGrid.add(x, me))
    ))

  def bestMove(moves: Seq[Move]): Int = {
    analyze(moves)
    val dec = (
      moves filter (_.forceWins) sortWith (_.winDist < _.winDist),
      moves filter (_.forceLoses) sortWith (_.lossDist < _.lossDist)
    ) match {
      // nothing interresting
      case (Nil, Nil) => moves
      // some forced wins, choose the fastest
      case (ws, Nil) => ws.head
      // all moves are forced losses, choose the slowest
      case (Nil, ls) if (ls.size == moves.size) => ls.last
      // choose a move that does not loose
      case (Nil, ls) => moves diff ls
      // some forced wins do not loose before, pick one
      case (ws, ls) if !(ws diff ls).isEmpty => (ws diff ls).head
      // fastest win is faster than fastest loss, pick it
      case (ws, ls) if (ws.head.winDist < ls.head.lossDist) => ws.head
      // choose the slowest death
      case (ws, ls) if (ls.size == moves.size) => ls.last
      // choose a move that does not loose
      case (ws, ls) => moves diff ls
    }
    dec match {
      case m: Move => m.x
      case ms: Seq[Move] => ms(Random.nextInt(ms.size)).x
    }
  }

  def analyze(moves: Seq[Move]) {
    val t1 = time()
    for (move <- moves) {
      move.forceWins
      move.forceLoses
    }
    println("%d moves examinated in %d ms.".format(
      count, time() - t1
    ))
  }

  case class Move(val x: Int, p: Int, val depth: Int, val grid: Grid) {
    count += 1

    val o: Int = if (p == 2) 1 else 2

    lazy val answers: Seq[Move] =
      if (wins) Nil
      else if (depth >= maxDepth) Nil
      else grid.openCols map(ax =>
        new Move(ax, o, depth+1, grid.add(ax, o))
      )

    lazy val wins: Boolean = grid.isWonBy(p)

    lazy val winDist: Int =
      if (wins) depth
      else if (answers.isEmpty) -1
      else {
        val lossDists = answers map (_.lossDist)
        if (lossDists forall (_ != -1)) lossDists.max
        else -1
      }

    lazy val lossDist: Int =
      if (answers exists (_.wins)) depth + 1
      else if (answers.isEmpty) -1
      else {
        val winDists = answers map (_.winDist) filter (_ != -1)
        if (winDists.isEmpty) -1
        else winDists.max
      }

    def forceWins: Boolean = -1 != winDist
    def forceLoses: Boolean = -1 != lossDist

    override def toString = x.toString
  }

  def time(): Long = System.currentTimeMillis()
}

object ConxAi {
  def play(grid: Grid, n: Int): Int = new ConxAi(grid, n).play
}
