import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.json.JsonObject;


public class TestRunner extends Verticle {

  public void start() {
    JsonObject config = new JsonObject();
	config.putString("foo", "wibble");
	config.putBoolean("bar", false);

	container.deployVerticle("SenderTest.java", config);
	container.deployVerticle("TCPServer.java", config);
  }
}
