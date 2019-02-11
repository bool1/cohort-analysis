package client

import java.time.{LocalDate, Month}
import java.time.format.DateTimeFormatter
import cohorts.{Cohort, Behavior, CohortWithBehaviors, CohortProducer}
import analysis.{CohortAnalyzer}


object CustomerRetentionAnalysis extends App {



  	val data = new CsvParser("CustomerOrders.csv").parse.take(5000)
  	println("Lines found: " + data.length)  


	//Fragment into Cohorts & Behaviors
	val formatter = DateTimeFormatter.ofPattern("yyyy-MMM");
	val signupMonth: Customer_X_Order => String = x => formatter.format(x.signUpDate)
	val orderMonth: Customer_X_Order => String = x => formatter.format(x.orderCreated)

	//TODO: Build DSL. Refactor calls on producer to chain methods
	val producer = new CohortProducer[Customer_X_Order]()
	val cohorts = producer.withBehaviors(orderMonth)(producer.produce(signupMonth)(data))
	//println("cohorts: " +  cohorts)


	//Analyze
	val metric: (Int, Seq[Customer_X_Order]) => CustomerRetention
		=  (size, customerOrders) => CustomerRetention(
				customerOrders.length.toFloat / size, 
				customerOrders.filter(_.orderNumber == 1).length.toFloat / size
			)

	val analyzer = new CohortAnalyzer[Customer_X_Order]()
	val result = analyzer.analysis(metric)(cohorts)

	println("result: " + result)
	


}