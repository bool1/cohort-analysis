package client

import scala.io._
import java.time.{LocalDate}
import java.time.format.DateTimeFormatter

//TODO: Add configuration
//TODO: Convert into generic CSV parser
//TODO: Wrap T into Option[T] to handle non-conforming values and parse exceptions
class CsvParser(resourceName: String) {

	def parse(): Seq[Customer_X_Order] =  {
		val bufferedSource = Source.fromResource(resourceName)
	  	val data = bufferedSource.getLines.map {
	  		line => {
	  			val cells = line.split(",").map(_.trim)
	  			val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	  			Customer_X_Order(cells(0).toInt, LocalDate.parse(cells(1), dateFormatter),
	  				cells(2).toInt, cells(3).toInt, LocalDate.parse(cells(4), dateFormatter)
	  			)
	  		}
	  	}.toList
	  	
	  	bufferedSource.close
	  	data
	}
}