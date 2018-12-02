import scala.concurrent.{Future, blocking}
object FutureTest extends App {
  import scala.concurrent.ExecutionContext.Implicits.global

  //implicit val ec : ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3000))
  (1 to 1000000).foreach({
    i => Future {
      println("start")
      blocking {
        Thread.sleep(99999999)
      }
      println("end")
    }
  })

  //Await.result(Future.sequence(fs), 1.minutes)
  readLine()
}
