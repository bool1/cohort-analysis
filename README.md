# cohort-analysis

## Dependencies
- Scala 2.12.*
- sbt 0.13(Scala Build tool)
- Java 8+

## Build
- Clone repo, go to the base directory
- `sbt compile`

## Run
- **Production:** Run executable produced by sbt
- **Development:** `sbt ~run`(~ enables hot reloading)

## Tests
- `sbt test`
- Only CohortSpec.scala has meaningful has tests with relevant test data. Other test cases are yet to be implemented. 

## Key Points:

- Showcase modularity, composability, testability, extensibility and other key characterstics
- This is a first iteration as opposed to 'complete' product. Hence this is not complete(or anwhere near complete) if it is evaluated like a University assignment

## Design
Three major components: (Cohort)Producer, (Cohort)Analyzer, Client. Producer and Analyzer are type agnostic(type parameterized) and have higher order functions accepts predicates.They don't share wiring except data models that can be improved easily in next iteration.

**Producer:** Breaksdown any collection `Seq[T]` into `Cohort[T]` and then further pivots on a behavior resulting in
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

2. Composability - Unix pipe and std-in/out redirection allows data to move freely from one component to another. And components can be arranged in variety of order.

Something like:

`join -t , -a 1 -1 1 -2 3 -e '' -o 0,1.2,2.1,2.2,2.4 <(tail -n +2 customers.csv | tr -d '\r' | sort -k 1 -t ,) <(tail -n +2 orders.csv | tr -d '\r' | sort -k 3 -t ,) | sort -n`

Above command:
1. Removes headers from csv files before processing
2. Remove CR(Carriage return) if files are created on Windows
3. Sort
4. Perform join on two columns
5. Sort Numerically

## Implementation Details

- Program doesn't implement all the request functionality.

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
