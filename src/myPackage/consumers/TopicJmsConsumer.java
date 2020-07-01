package myPackage.consumers;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * <p>This is a very simple example of a JMS comsumer.  This is a simplified version of the quickstarts
 * provided by JBoss.</p>
 *
 */
public class TopicJmsConsumer {
	private static final String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String QUEUE_DESTINATION = "jms/topic/testTopic";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
    
    public static void main( String[] args ) throws NamingException {
    	Context namingContext = null;
        JMSContext context = null;

        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
        env.put(Context.SECURITY_PRINCIPAL, args[0]);     // username
        env.put(Context.SECURITY_CREDENTIALS, "guest");   // password
        
        try {
	        namingContext = new InitialContext(env);
	        // Use JNDI to look up the connection factory and queue
	        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(CONNECTION_FACTORY);
	        Destination destination = (Destination) namingContext.lookup(QUEUE_DESTINATION);
	        // Create a JMS context to use to create consumers
	        context = connectionFactory.createContext(args[0], "guest"); // again, don't do this in production
	        MyListener listener=new MyListener();  
	        // Read a message.  If nothing is there, this will return null
	        JMSConsumer consumer = context.createConsumer(destination);
	        consumer.setMessageListener(listener);
//	        String text = consumer.receiveBodyNoWait(String.class);
	        System.out.println(args[0] + " is ready, waiting for messages...");
	        System.out.println("press Ctrl+c to shutdown...");  
            while(true){                  
                Thread.sleep(1000);  
            }  
            
        } catch(Exception e){System.out.println(e);}  
        finally {
        	if (namingContext != null) {
        		namingContext.close();
        	}
        	if (context != null) {
        		context.close();
        		
        	}
        }
    }
}
