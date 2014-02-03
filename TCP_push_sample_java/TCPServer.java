import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.core.net.NetServer;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.VoidHandler;

public class TCPServer extends Verticle {

  public void start() {
  	
  	NetServer server = vertx.createNetServer();
  	
  	// bind connection handler
  	server.connectHandler(new Handler<NetSocket>() {
      public void handle(final NetSocket socket) {
      	 System.out.println("Client has been connected!!");

      	// after client has been connected data handler is registered
      	socket.dataHandler(new Handler<Buffer>() {
           public void handle(Buffer buffer) {
               System.out.println("I received "+buffer.toString() +" size:" + buffer.length() + " bytes of data");
           }
        }); // data handler
        
        // close handler
        // when socket is closed, this handler is called. 
        socket.closeHandler(new VoidHandler() {
            public void handle() {
               System.out.println("The socket is now closed"); 
            }
        });
        
      	// register event bus handler to get push request from HTTP Server
		    vertx.eventBus().registerHandler("subscriber-1",new Handler<Message<String>>() {
		      	 	@Override
		      	 	public void handle(Message<String> message){
		      	 		System.out.println("I got a message :"+message.body());
		      	 		// if it gets a message from eb, it sends msg to client. 
		      	 		Buffer sndBuffer = new Buffer();
		      	 		socket.write("I got a message :"+message.body());
		      	 	}
		    });  	
      	
      }
    });
    
    server.listen(1234);
  }


}
