package org.lyming.first;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @ClassName JMSQueueConsumer
 * @Description TODO
 * @Author lyming
 * @Date 2020/2/21 5:28 下午
 **/
public class JMSQueueConsumer1 {
    public static void main(String[] args) {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.2.100:61616");
        Connection connection = null;
        try {
            //连接AMQ
            connection = connectionFactory.createConnection();
            connection.start();
            //创建Session
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //创建目的地
            Destination destination = session.createQueue("myQueue");
            //创建消费者
            MessageConsumer consumer = session.createConsumer(destination);
            //接收消息(阻塞的方式接收)
            TextMessage message = (TextMessage) consumer.receive();
            System.out.println(message.getText());
            session.commit();
            session.close();
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
