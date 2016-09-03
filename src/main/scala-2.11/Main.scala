import akka.actor.{Actor, ActorSystem, Props}
import akka.actor.Actor.Receive
import akka.event.Logging

/**
  * Created by SS on 2016/09/04.
  */
class MyActor extends Actor {
  var log = Logging(context.system, this)
  var child = context.actorOf(Props(classOf[MyActor2], "myArg3", "myArg4"), name = "myChild")
  override def receive: Receive = {
    case s: String => {
      log.info("received: %s" format s)
      child ! s
    }
    case _ => {
      // なにもしないよ。
    }
  }
}

class MyActor2(arg1: String, arg2: String) extends Actor {
  var log = Logging(context.system, this)
  override def receive: Receive = {
    case s: String => {
      log.info("args: %s, %s, received: %s" format (arg1, arg2, s))
    }
    case _ => {
      // なにもしないよ。
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("mySystem")  // 重い処理（アプリ内で１回だけやる）

    // アクターの設定
    val props1 = Props[MyActor]
    val props2 = Props(classOf[MyActor2], "myArg1", "myArg2")

    // アクターの生成（このタイミングで自動的に非同期で開始される）
    val actor1 = system.actorOf(props1, name = "myActor1")
    val actor2 = system.actorOf(props2, name = "myActor2")

    while(true){
      actor1 ! "hi actor1"
      actor2 ! "hi actor2"
      Thread.sleep(5000)
    }
  }
}
