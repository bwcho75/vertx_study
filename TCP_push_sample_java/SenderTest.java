import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

public class SenderTest extends Verticle {
		static int counter=0;

  public void start() {
    // Send a ping message every second
    vertx.setPeriodic(2000, new Handler<Long>() {

      @Override
      public void handle(Long timerID) {
        counter = (counter+1)%5;
        String address = "subscriber-"+counter;
        String message = "send message to "+address;
        System.out.println(message);
        vertx.eventBus().send(address, message  , new Handler<Message<String>>() {
          @Override
          public void handle(Message<String> reply) {
            System.out.println("Received reply: " + reply.body());
          }
        });
      }
    });
  }
}
