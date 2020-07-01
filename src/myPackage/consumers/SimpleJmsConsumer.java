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
public class SimpleJmsConsumer {
	private static final String CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String QUEUE_DESTINATION = "jms/queue/testQueue";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
    
    public static void main( String[] args ) throws NamingException {
    	Context namingContext = null;
        JMSContext context = null;

        final Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
        env.put(Context.SECURITY_PRINCIPAL, "guest1");     // username
        env.put(Context.SECURITY_CREDENTIALS, "guest1");   // password
        
        try {
	        namingContext = new InitialContext(env);        
	        // Use JNDI to look up the connection factory and queue
	        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup(CONNECTION_FACTORY);
	        Destination destination = (Destination) namingContext.lookup(QUEUE_DESTINATION);        
	        // Create a JMS context to use to create consumers
	        context = connectionFactory.createContext("guest1", "guest1"); // again, don't do this in production        
	        // Read a message.  If nothing is there, this will return null
	        JMSConsumer consumer = context.createConsumer(destination);
	        String text = consumer.receiveBodyNoWait(String.class);
	        System.out.println("Received message: " + text );
        } finally {
        	if (namingContext != null) {
        		namingContext.close();
        	}
        	if (context != null) {
        		context.close();
        	}
        }
    }
}
