package cohorts

import pivots._


trait CanProduceCohort[T] {
	def produce(pivotOn: T => String): Seq[T] => Seq[Cohort[T]] 
	def withBehaviors(pivotOn: T => String): Seq[Cohort[T]] => Seq[CohortWithBehaviors[T]]
}

//TODO: Change map into higher order functions
class CohortProducer[T] 
	extends CanProduceCohort[T] with CanPivot[T] {


	def produce(predicate: T => String) 
		= collection => pivot(predicate)(collection).map {
			case (label, segments) => Cohort(label, segments.length, segments)
		}


	def withBehaviors(predicate: T => String)
		 = cohorts => cohorts map {
			case Cohort(label, size, segments) => CohortWithBehaviors(label,
				size,
				pivot(predicate)(segments).map {
					case (l, s) => Behavior(l, s)
				})
	}


}
