package cohorts

import org.scalatest._
import client._
import java.time.{LocalDate, Month}
import java.time.format.DateTimeFormatter


class CohortSpec extends FlatSpec with Matchers {


	val testData =  List( 
		  		Customer_X_Order(1, LocalDate.of(2015, Month.JULY,13), 101, 1, LocalDate.of(2015, Month.JULY, 15) ),
		  		Customer_X_Order(2, LocalDate.of(2015, Month.JULY,14), 102, 1, LocalDate.of(2015, Month.JULY, 17) ),
		  		Customer_X_Order(2, LocalDate.of(2015, Month.JULY,15), 103, 2, LocalDate.of(2015, Month.JULY, 28) ),
				Customer_X_Order(3, LocalDate.of(2015, Month.AUGUST,14), 104, 1, LocalDate.of(2015, Month.AUGUST, 16) ),
				Customer_X_Order(3, LocalDate.of(2015, Month.AUGUST,14), 105, 2, LocalDate.of(2015, Month.SEPTEMBER, 19) ),
				Customer_X_Order(3, LocalDate.of(2015, Month.AUGUST,14), 106, 3, LocalDate.of(2015, Month.SEPTEMBER, 22) ),
				Customer_X_Order(4, LocalDate.of(2015, Month.SEPTEMBER,14), 107, 1, LocalDate.of(2015, Month.SEPTEMBER, 14) ),
				Customer_X_Order(5, LocalDate.of(2015, Month.SEPTEMBER,14), 108, 1, LocalDate.of(2015, Month.OCTOBER, 19) ),
				Customer_X_Order(6, LocalDate.of(2015, Month.SEPTEMBER,14), 109, 1, LocalDate.of(2015, Month.OCTOBER, 2) )	
		  )

	
	val signupYear: Customer_X_Order => String 
		= x => DateTimeFormatter.ofPattern("yyyy").format(x.signUpDate)

	val signupMonth: Customer_X_Order => String 
		= x => DateTimeFormatter.ofPattern("yyyy-MMM").format(x.signUpDate)
	
	val orderMonth: Customer_X_Order => String 
		= x => DateTimeFormatter.ofPattern("yyyy-MMM").format(x.orderCreated)




  "Cohort Producer" should "breakdown collection into cohorts by month" in {

  
	val producer = new CohortProducer[Customer_X_Order]()
	val cohortsByMonth = producer.produce(signupMonth)(testData)
	
	cohortsByMonth.size should be (3)
  }

  "Cohort Producer" should "breakdown collection into cohorts by year" in {

  
	val producer = new CohortProducer[Customer_X_Order]()
	val cohortsByYear = producer.produce(signupYear)(testData)
	cohortsByYear.size should be (1)
  }

  "Cohort Producer" should "breakdown each cohorts(by year) into behaviors(by month)" in {

  
	val producer = new CohortProducer[Customer_X_Order]()
	val cohortsByYear = producer.produce(signupYear)(testData)
	val cohortsWithBehaviors = producer.withBehaviors(orderMonth)(cohortsByYear)
	println(cohortsWithBehaviors)
	cohortsWithBehaviors.size should be (1)
	val behaviors = cohortsWithBehaviors.head match {
		case CohortWithBehaviors(_, _, b) => b
		}
	
	behaviors.length should be (4)
  }


}
