package service;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EndPoint {
	private String queueName;
	private String host = "localhost";
	private String endpointName;
	private ConnectionFactory factory;
	private Connection connection;
	protected Channel channel; 
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getEndpointName() {
		return endpointName;
	}

	public void setEndpointName(String endpointName) {
		this.endpointName = endpointName;
	}

	public ConnectionFactory getFactory() {
		return factory;
	}

	public void setFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public EndPoint(String endPointName, String queueName) {
	
		this(endPointName,queueName,"localhost");
	}
	public EndPoint(String endPointName,String queueName, String host)
	{
		try {
			this.queueName = queueName;
			this.host = host;
			this.endpointName = endPointName;
			factory = new ConnectionFactory();
			factory.setHost(host);
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(queueName, false, false, false, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    public void close() throws IOException{
        this.channel.close();
        this.connection.close();
    }
	
}
