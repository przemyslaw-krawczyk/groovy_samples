@Grab(group='org.apache.activemq',module = 'activemq-client', version='5.12.0')
@Grab(group='pl.mycompany',module = 'my-lib', version='04.05.07-SNAPSHOT', changing=true)

import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.*
import pl.mycompany.model.DocHandlerRequest

def brokerUrl = 'tcp://localhost:9999'
def userName = 's_xxs'
def password = 's_xxs12345'
def queue = 'pl.mycompany.jms.queue.DocumentHandler'

new ActiveMQConnectionFactory(brokerURL: brokerUrl, userName: userName, password: password).createConnection().with {
  start()
  createSession(false, Session.AUTO_ACKNOWLEDGE).with {
   for (i = 1; i <= 100; i++) {
       def docHandlerRequest = new DocHandlerRequest()
       docHandlerRequest.with {
           claimId = 20700003915
           claimRequestId = 1
           alfrescoDir = 'CLAIMS'
           fileName = '333' + i
       }
       def message = createObjectMessage(docHandlerRequest)
       createProducer().send(createQueue(queue), message)
   }
  }
  close()
}