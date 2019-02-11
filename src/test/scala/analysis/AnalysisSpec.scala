package analysis

import org.scalatest._
import client._
import java.time.{LocalDate, Month}
import java.time.format.DateTimeFormatter
import cohorts._


class AnalysisSpec extends FlatSpec with Matchers {


	
	//TODO: Create Test Data
	
	val metric: (Int, Seq[Customer_X_Order]) => CustomerRetention
		=  (size, customerOrders) => CustomerRetention(
				customerOrders.length.toFloat / size, 
				customerOrders.filter(_.orderNumber == 1).length.toFloat / size
			)

	"Analyzer" should "analyze cohort behaviors" in {

		val analyzer = new CohortAnalyzer[Customer_X_Order]()
		//val result = analyzer.analysis(metric)(cohorts)
		//println(result)
	  }

}
