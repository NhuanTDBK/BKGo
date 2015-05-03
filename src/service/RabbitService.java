package service;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

public class RabbitService extends EndPoint {

	public RabbitService(String endPointName, String queueName) {
		super(endPointName, queueName);
	}
	public void sendMessage(Serializable object) throws IOException {
	    channel.basicPublish("",this.getQueueName(), null, SerializationUtils.serialize(object));
	}	
	public void sendMessage(String object) throws IOException {
	    channel.basicPublish("",this.getQueueName(), null, object.getBytes());
	}	
}
