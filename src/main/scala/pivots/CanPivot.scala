package pivots

trait CanPivot[T] {
	
	def pivot(predicate: T => String): Seq[T] => Seq[(String, Seq[T])] = _.groupBy(predicate).toSeq
	
}


/*
trait CanBreakdown[T] {
	def breakdown[T](implicit cp: CanPivot[T]) = cp.pivot
}
*/