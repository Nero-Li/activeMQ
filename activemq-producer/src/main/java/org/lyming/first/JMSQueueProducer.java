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
 * @ClassName JMSQueueProducer
 * @Description TODO
 * @Author lyming
 * @Date 2020/2/21 4:41 下午
 **/
public class JMSQueueProducer {
    public static void main(String[] args) {

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://192.168.2.100:61616");
        Connection connection = null;
        try {
            //连接AMQ
            connection = connectionFactory.createConnection();
            connection.start();
            //创建Session
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //创建目的地
            Destination destination = session.createQueue("myQueue");
            //创建生产者
            MessageProducer producer = session.createProducer(destination);

            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //简单实例
            for (int i = 0; i < 1030; i++) {

                simpleDemoWithNo(session, producer,i);
            }
            //应用设置的属性
//            appProperty(session, producer);
//            session.commit();
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

    /**
     * 添加自定义属性
     *
     * @param session
     * @param producer
     * @throws JMSException
     */
    private static void appProperty(Session session, MessageProducer producer) throws JMSException {
        TextMessage message = session.createTextMessage("appProperty");
        message.setStringProperty("lyming", "hello world");
        producer.send(message);
    }

    /**
     * 简单发送示例
     *
     * @param session
     * @param producer
     * @throws JMSException
     */
    private static void simpleDemo(Session session, MessageProducer producer) throws JMSException {
        //创建消息
        TextMessage message = session.createTextMessage("Hello");
        //发送消息
        producer.send(message);
    }

    private static void simpleDemoWithNo(Session session, MessageProducer producer,int i) throws JMSException {
        //创建消息
        TextMessage message = session.createTextMessage("Hello  "+i);
        //发送消息
        producer.send(message);
    }


}
