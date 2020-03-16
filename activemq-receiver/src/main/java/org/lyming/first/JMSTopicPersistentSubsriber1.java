package org.lyming.first;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * @ClassName JMSTopicSubsriber1
 * @Description TODO
 * @Author lyming
 * @Date 2020/2/21 11:31 下午
 **/
public class JMSTopicPersistentSubsriber1 {
    public static void main(String[] args) {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.2.100:61616");
        Connection connection = null;
        try {
            //连接AMQ
            connection = connectionFactory.createConnection();
            connection.setClientID("lyming-01");
            connection.start();
            //创建Session
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            //创建目的地
            Topic destination = session.createTopic("myTopic");
            //创建消费者
            MessageConsumer consumer = session.createDurableSubscriber(destination,"lyming-01");
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
