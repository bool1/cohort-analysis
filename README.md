# cohort-analysis

#### Notes

- Intention is to showcase modularity, composability, testability, extensibility and other key characterstics for the work (including documentation) done in less than 5 hours. It is a tall order, I guess. :)
- This is a first iteration as opposed to a 'complete' product. It is nowhere near complete if it is evaluated like a University assignment.
- Data processing is a nearly perfect candidate for "side-effect free" application(e.g. Spark) as computing is inherently spatial. This program doesn't use any computing cluster or data processing library.
- To make it more readable to "foreign" eyes, program uses only basic functional programming features such as currying, higher order functions and morphisms and lays foundation for creating DSL but doesn't use constructs from category theory such as Monoids and Semigroups.
- Some Scala specific features such as implicit, ad-hoc polymorphism, for comprehension for monads and library like cats aren't used. This makes code look little bit verbose than needed but at least it is readable to foreign eyes or those new to Scala.

#### Dependencies
- Scala 2.12.*
- sbt 0.13(Scala Build tool)
- Java 8+

#### Build
- Clone repo, go to the base directory and run
- `sbt compile`

#### Run
- **Production:** Run executable produced by sbt
- **Development:** `sbt ~run`(~ enables hot reloading)

**Sample output**(input capped to 5000 lines for brevity)
  Please note that it doesn't format output as table as the program is dynamic and can produce behavior groups of arbitrary size depending upon provided pivot function. Unless capped, table layout could be very wide and leave "holes", especially when pivoting on a more granular time slice(e.g. hourly) or on an attribute where values aren't distributed evenly across the domain. Besides implementing fluid layout requires more time.
```
Lines found: 5000
result: Vector(CohortWithBehaviorMetrics(2015-Jan,5000,Vector(BehaviorMetric(2015-Apr,CustomerRetention(0.1506,0.0146)), BehaviorMetric(2015-Jul,CustomerRetention(0.0288,0.002)), BehaviorMetric(2015-Jan,CustomerRetention(0.204,0.1396)), BehaviorMetric(2015-Jun,CustomerRetention(0.1286,0.0076)), BehaviorMetric(2015-Feb,CustomerRetention(0.1736,0.0412)), BehaviorMetric(2015-May,CustomerRetention(0.1342,0.01)), BehaviorMetric(2015-Mar,CustomerRetention(0.1802,0.0282)))))
```
#### Tests
- `sbt test`
- Only CohortSpec.scala has meaningful has tests with relevant test data. Other test cases are yet to be implemented. 
**Sample output**
```
[info] AnalysisSpec:
[info] Analyzer
[info] - should analyze cohort behaviors
[info] CohortSpec:
[info] Cohort Producer
[info] - should breakdown collection into cohorts by month
[info] Cohort Producer
[info] - should breakdown collection into cohorts by year
[info] Cohort Producer
[info] - should breakdown each cohorts(by year) into behaviors(by month)
[info] Run completed in 771 milliseconds.
[info] Total number of tests run: 4
[info] Suites: completed 2, aborted 0
[info] Tests: succeeded 4, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 13 s, completed Feb 11, 2019 10:47:48 AM
```

## Design
Three major components: (Cohort)Producer, (Cohort)Analyzer, Client. Producer and Analyzer are type agnostic(type parameterized) and have higher order functions that accepts predicates.They don't share wiring except data models that can be improved easily in next iteration.

**Producer:** Breaksdowns any collection `Seq[T]` into `Cohort[T]` and then further pivots on a behavior resulting in
`CohortWithBehaviors[T`. Producer accepts arbitrary pivot predicates. For instance, you can provide a predicate on DateTime type that groups Cohorts by week, month, year or anything. It doesn't have to be time. It can be any type.

**Analyzer:** Applies client provided metric function on `CohortWithBehaviors[T]` and yield behavior metrics. Again this is higher order function and can take any arbitrary metric function.

**Client** Non-parameterized(type-aware) component that defines predicates and apply necessary 'wiring'.


## Implementation Style
1. DSL(Domain Specific Language) - have plumbing for creating necessary DSL. Final implementation could look similar to this:

`data pivotOn signupByMonth into[Cohort[T]] pivotOn orderCreatedByMonth into[Cohort[T]]`

2. (Almost)Purely functional - Except IO side effects. No loops, reassignments, shared state or destructive mutations

3. Algebraic - Levarages abstract algebra: currying, mondas and higher-order functions


## Design Philosophy
Inspired by Unix, in one word - composability. For instance, combining(cartesian product) input files *"customers.csv"*
and *"orders.csv"* can be easily done in a program. However, it is handled with Unix tool chain, for two reasons:

1. Reusability - Why not to reuse efficient and optimized component instead of rewriting? For instance, sort command can leverage disk while programming language sort is restrcited by available main memory.

2. Composability - Unix pipe and std-in/out redirection allows data to move freely from one component to another. And components can be arranged in different order creating several permutations and combinations.

Something like:

`join -t , -a 1 -1 1 -2 3 -e '' -o 0,1.2,2.1,2.2,2.4 <(tail -n +2 customers.csv | tr -d '\r' | sort -k 1 -t ,) <(tail -n +2 orders.csv | tr -d '\r' | sort -k 3 -t ,) | sort -n`

Above command:
1. Removes headers from csv files before processing
2. Removes CR(Carriage return) if files are created on Windows
3. Performs non-numerical Sort
4. Performs join on two columns
5. Sorts numerically

## Implementation Details

- Program doesn't implement all requested functionalities due to time constraints.Many edge cases aren't handled and even simplified to keep focus on core features, again due to derth of time.

- Program reads "CustomerOrders.csv" from resources("/src/main/resources"). This file is produced by:
`join -t , -a 1 -1 1 -2 3 <(tail -n +2 customers.csv | tr -d '\r' | sort -k 1 -t ,) <(tail -n +2 orders.csv | tr -d '\r' | sort -k 3 -t ,) | sort -n`

- It performs inner join to avoid dealing with handling parsing errors and wrapping them in `Option[T]` monad.

## Identified improvements

1. Pipe output of above command straight to cohort-nalysis and avoid reading from source
2. Improve test coverage
3. Create DSL
4. Left outer join on customers and orders and handle parsing errors by wrapping `Customer_X_Order[T]` into `Option[T]`
5. CsvParser should be generic to parse any CSV/TSV/DSV files
6. Create show template/layout for displaying outcome
7. Introduce higher order function to eliminate redundant mapping 
