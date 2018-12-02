package example

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.routing.BalancingPool
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.Random

case class Order(id: Int)

class SomeService {
  val random = new Random()

  import scala.concurrent.ExecutionContext.Implicits.global

  val shortDuration: FiniteDuration = 10.millis
  val longDuration: FiniteDuration = 1.second

  def get: Future[Int] = Future {
    if (random.nextInt() % 150 == 0)
      Thread.sleep(longDuration.toMillis)
    else
      Thread.sleep(shortDuration.toMillis)
    3
  }
}


class Consumer(processor: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: String =>
      println(s"greeting from consumer ! $msg")
      processor ! Order(msg.toInt)
  }
}

class Processor(someService: SomeService) extends Actor {

  val waitDuration: FiniteDuration = 10.hours

  override def receive: Receive = {
    case order: Order =>
      println(s"greeting from processor ! ${order.id}")
      val a = Await.result(someService.get, waitDuration)
      //Thread.sleep(sleepDuration.toMillis)
      println(order.id)
  }
}

object ActorConcurrency extends App {

  val config: Config = ConfigFactory.load()
  val system = ActorSystem("my-actor-system")

  val serviceRepository = new SomeService
  val processors = system.actorOf(
    new BalancingPool(nr = 50)
      .props(Props(new Processor(serviceRepository))), name = "processors"
  )
  val consumer = system.actorOf(
    new BalancingPool(nr = 50)
      .props(Props(new Consumer(processors))), name = "consumers"
  )

  //System.in.read()
  (1 to 10000).foreach(i => {
    consumer ! i.toString
  })
  Thread.sleep(1000)

  Await.result(system.terminate(), Duration.Inf)
}
