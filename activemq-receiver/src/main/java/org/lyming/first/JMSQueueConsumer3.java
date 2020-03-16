package org.lyming.first;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Enumeration;

/**
 * @ClassName JMSQueueConsumer
 * @Description TODO
 * @Author lyming
 * @Date 2020/2/21 5:28 下午
 **/
public class JMSQueueConsumer3 {
    public static void main(String[] args) {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.2.100:61616");
        Connection connection = null;
        try {
            //连接AMQ
            connection = connectionFactory.createConnection();
            connection.start();
            //创建Session
            Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);
            //创建目的地
            Destination destination = session.createQueue("myQueue");
            //创建消费者
            MessageConsumer consumer = session.createConsumer(destination);
            //接收消息(阻塞的方式接收)
//            TextMessage message = (TextMessage) consumer.receive();
//            Enumeration jmsxPropertyNames = connection.getMetaData().getJMSXPropertyNames();
//            while (jmsxPropertyNames.hasMoreElements()) {
//                System.out.println("JMSXProperty:"+jmsxPropertyNames.nextElement());
//            }
            //创建监听
            MessageListener messageListener = new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println(((TextMessage)message).getText());
                        message.acknowledge();
                        Thread.sleep(1000);
                        Enumeration enumeration = message.getPropertyNames();
                        while (enumeration.hasMoreElements()) {
                            String name = (String) enumeration.nextElement();
                            System.out.println("name:"+name+"==>"+message.getStringProperty(name));
                        }

                    } catch (JMSException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
            //一直监听
            while (true) {
                consumer.setMessageListener(messageListener);
//                session.commit();
//                session.rollback();
            }


//            System.out.println(message.getText());

//            session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
