import javax.management.openmbean.CompositeData
@Grab(group='org.apache.activemq',module = 'activemq-all', version='5.12.0')

import javax.management.remote.rmi.RMIConnector
import javax.management.remote.JMXConnector
import javax.management.*
import groovy.jmx.builder.*
import org.apache.activemq.broker.jmx.QueueViewMBean;

String[] credendials = ["user", "pass"]
def env = [(JMXConnector.CREDENTIALS): credendials]

RMIConnector connection = new JmxBuilder().client(port: 2011, host: 'activemqserwerJmx')
connection.connect(env)

MBeanServerConnection mBeanServerConnection = connection.MBeanServerConnection

def sourceQueueNamePattern = "NotifyDocument_*Q"
def destinationQueueName   = 'NotifyDocument_Q'

def wantedObjectName = "org.apache.activemq:type=Broker,brokerName=*,destinationType=Queue,destinationName=$sourceQueueNamePattern"
ObjectName[] queuesObjectNames = mBeanServerConnection.queryNames(new ObjectName(wantedObjectName), null);

queuesObjectNames.each {
    ObjectName queueObjName -> println "Queue name: $queueObjName"

    def queueViewMBean = new GroovyMBean(mBeanServerConnection, queueObjName)
    //QueueViewMBean queueViewMBean = JMX.newMBeanProxy(mBeanServerConnection, queueObjName, QueueViewMBean.class, false);

    println "Messages in queue ${queueViewMBean.cursorSize()}"
    //queueViewMBean.moveMatchingMessagesTo("JMSPriority=4", destinationQueueName, 1)

    for (CompositeData message : queueViewMBean.browse()) {
        def messageXmlText =  queueViewMBean.getMessage(message.get("JMSMessageID").toString()).get("Text")
        def messageXml = new XmlSlurper().parseText(messageXmlText)

        def xmlAttrsToPrint = [:]
        def xmlAttrNamesToPrint = [
                "Xml atr 1",
                "Xml atr 2",
                "Xml atr 3"
        ]
        xmlAttrNamesToPrint.each {name ->
            def attr = messageXml.Document.DocumentIndices.Attribute.find { it.AttributeName == name}
            xmlAttrsToPrint.put(attr.AttributeName, attr.AttributeValue)
        }
        println "Document: $messageXml.DocumentId.SourceSystemId; $xmlAttrsToPrint"
    }
}