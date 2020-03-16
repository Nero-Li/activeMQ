package org.lyming.first;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * @ClassName JMSTopicPublisher
 * @Description TODO
 * @Author lyming
 * @Date 2020/2/21 11:18 下午
 **/
public class JMSTopicPublisher {
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
            Destination destination = session.createTopic("myTopic");
            //创建生产者
            MessageProducer producer = session.createProducer(destination);
            //如果想持久化,尽量设置成这样
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //创建消息
            TextMessage message = session.createTextMessage("公告:道路千万条,安全第一条");
            producer.send(message);
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
