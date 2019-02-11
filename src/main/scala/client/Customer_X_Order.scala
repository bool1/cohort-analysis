package client

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import pivots.{CanPivot}
import cohorts.{Cohort}

case class Customer_X_Order(customerId: Int, signUpDate: LocalDate,
	orderId: Integer, orderNumber: Integer, orderCreated: LocalDate)







object Customer_X_Order {

	implicit class CustomerOrdersUtils(val customerOrders: Seq[Customer_X_Order]) 
		extends CanPivot[Customer_X_Order] {
		def on(f: Customer_X_Order => String) = pivot(f)(customerOrders)
	}

/*
	type CollectionWithLabel =  (String, Seq[Customer_X_Order])

	implicit class CustomerOrdersBreakdownUtils(val customerOrders: Seq[CollectionWithLabel]) {
		def into[T](implicit f: CollectionWithLabel => T)  = customerOrders map {
			tuple => f(tuple)
		}
	}

	implicit val toCohort: CollectionWithLabel => Cohort[Customer_X_Order] = tuple => tuple match {
		case (label, segments) => Cohort(label, segments)
	}
	*/
}


/*
class CustomerOrders {
	def breakdown(implicit cp: CanPivot)
}*/

/*
	implicit class CustomerOrdersBreakdownUtils(val customerOrders: Seq[(String, Seq[Customer_X_Order])]) {
		def to[]
	}
}


object Pivot extends CanPivot[Customer_X_Order] {

}*/
/*implicits
object CustomerOrders {
	implicit object PivotOnSignupDate extends CanPivot[Customer_X_Order] {
		val on = _.signUpDate.getMonth.toString
	}

}
*/

/*
object CustomerOrders {
	implicit object MonthlyCustomerCohort extends CanDefineCohort[Customer_X_Order] {
			
			def breakdown: Seq[Customer_X_Order] => Seq[Cohort[Customer_X_Order]] = 
				data => data.groupBy(_.signUpDate.getMonth.toString).map {
						case (month, values) => Cohort(month, values)
					}.toSeq
			
	}

	def breakdown[T](data: Seq[T])(implicit c: CanDefineCohort[T]) = c.breakdown(data)
}
*/



/*
object CustomersCohort extends CanPivot[Customer_X_Order] {

	def groupByMonth(f: Customer_X_Order => String) = _.signUpDate.getMonth.toString

	def bring
}*/
/*
object Customer_X_Order {
	implicit class CustomerOrders(customerOrders: Seq[Customer_X_Order]) extends CanPivot[Customer_X_Order] {
		def pivot = bpivot(customerOrders)
	})
}
*/


/*
object CustomerOrderCohort extends CanBdown[Seq[Customer_X_Order], String] {

	def breakdown(predicate: Customer_X_Order => String): Seq[Customer_X_Order] => Seq[(String, Seq[Customer_X_Order])] = 
		data => data.groupBy(predicate).toSeq
}

trait CustomerOrderCohort extends CanBreakdownWithGroupBy[Customer_X_Order, String] {

	def breakdown(predicate: Customer_X_Order => String): Seq[Customer_X_Order] => Seq[(String, Seq[Customer_X_Order])] = 
		data => data.groupBy(predicate).toSeq
}
*/

/*
object Customer_X_Order {
	
	implicit class CustomerOrders(customerOrders: Seq[Customer_X_Order]) {
		def groupInto[Cohort] = customerOrders.groupBy()

	}
}
*/




