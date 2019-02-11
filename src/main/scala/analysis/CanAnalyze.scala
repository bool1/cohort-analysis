
package analysis

import cohorts.{PivotedCollection, CohortWithBehaviors, Behavior}

case class BehaviorMetric[T](label: String, metric: T) extends PivotedCollection
case class CohortWithBehaviorMetrics[T](label: String, size: Int, behaviors: Seq[BehaviorMetric[T]]) 
	extends PivotedCollection



trait CanAnalyze[T] {
	def analysis[U](metric: (Int, Seq[T]) => U): Seq[CohortWithBehaviors[T]] => Seq[CohortWithBehaviorMetrics[U]] 
}

class CohortAnalyzer[T]
	extends CanAnalyze[T] {

		def analysis[U](metric: (Int, Seq[T]) => U): Seq[CohortWithBehaviors[T]] => Seq[CohortWithBehaviorMetrics[U]] 
			= cohorts => cohorts map {
				case CohortWithBehaviors(label, size, behaviors) => CohortWithBehaviorMetrics(
					label,
					size,
					behaviors map {
						case Behavior(behaviorLabel, segments) => 
							BehaviorMetric[U](behaviorLabel, metric(size, segments))

					}
				)
			}
	}
