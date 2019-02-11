package cohorts

abstract class PivotedCollection
case class Cohort[T](label: String, size: Int, segments: Seq[T]) extends PivotedCollection
case class Behavior[T](label: String, segments: Seq[T]) extends PivotedCollection
case class CohortWithBehaviors[T](label: String, size: Int, behaviors: Seq[Behavior[T]]) extends PivotedCollection








