package conx

final class Grid private(
  val m: Grid.Matrix,
  val width: Int,
  val height: Int,
  val lastMove: Pair[Int, Int] = (0,0)
) {

  def apply(pos: Grid.Pos) = m(pos)

  def col(x: Int): Seq[Int] = (0 until height) map (m(x, _))

  def row(y: Int): Seq[Int] = (0 until width) map (m(_, y))

  def add(x: Int, p: Int): Grid = {
    val y = freeHoleHeight(x)
    new Grid(m + ((x, y) -> p), width, height, (x, y))
  }

  def freeHoleHeight(x: Int): Int = col(x).indexOf(0) match {
    case -1 => throw new IndexOutOfBoundsException
    case y => y
  }

  override def toString: String =
    (0 until height).reverse.map(y =>
      (0 until width).map(m(_, y) match {
        case 0 => '.' case 1 => 'o' case 2 => 'x'
      }).mkString(" ")
    ).mkString("\n") + "\n" + (0 until width).map(_ + " ").mkString

  lazy val openCols: Seq[Int] =
    (0 until width) filter (col(_) contains 0)

  lazy val isFull: Boolean = m forall (_._2 != 0)

  // try to use withFilter
  lazy val winnerNumber: Int = {
    val (lx, ly) = (lastMove._1, lastMove._2)
    val horzS = if (count < 8) "" else (for {
      x <- (lx-3).max(0) until (lx+4).min(width)
    } yield m(x, ly)).mkString
    val vertS = if (count < 8) "" else (for {
      y <- (ly-3).max(0) until (ly+4).min(height)
    } yield m(lx, y)).mkString
    val diagS = if (count < 10) "" else (for (dy <- -1 to 1 by 2)
      yield (for {
      d <- -3 to +3
      x = lx + d
      if (x >= 0 && x < width)
      y = ly + d*dy
      if (y >= 0 && y < height)
    } yield m(x, y)).mkString).mkString(",")
    val allS = horzS + "," + vertS + "," + diagS
    if (allS contains "1111") 1
    else if (allS contains "2222") 2
    else 0
  }

  def isWonBy(playerNumber: Int) = winnerNumber == playerNumber

  lazy val count: Int = m count (_._2 != 0)
}

object Grid {
  type Pos = Pair[Int, Int]
  type Matrix = Map[Pos, Int]
  def apply(m: Matrix, w: Int, h: Int): Grid = new Grid(m, w, h)
  def apply(g: Grid): Grid = apply(g.m, g.width, g.height)
  def apply(w: Int, h: Int): Grid = {
    val m = for (x <- 0 until w; y <- 0 until h) yield (x, y) -> 0
    apply(m.toMap, w, h)
  }
  def apply(size: Pair[Int, Int]): Grid = apply(size._1, size._2)
}
