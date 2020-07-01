package myPackage.consumers;

import javax.jms.*;  
public class MyListener implements MessageListener {  
  
    public void onMessage(Message m) {  
        try{  
        TextMessage msg=(TextMessage)m;  
      
        System.out.println("Jaja following message is received:"+msg.getText());  
        }catch(JMSException e){System.out.println(e);}  
    }  
}  