import groovy.xml.*
def xmlText = '''\
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns2:SearchUUIDResponse xmlns:ns2="http://proama.pl/integrationbus/ws/">
         <uuid>117054</uuid>
         <uuid>117055</uuid>
      </ns2:SearchUUIDResponse>
   </soap:Body>
</soap:Envelope>
'''

def response = new XmlSlurper().parseText(xmlText)
def responseXml = new XmlParser().parseText(xmlText)
def uuid = response.Body.SearchUUIDResponse.uuid
response.'**'.findAll{ it.name() == 'uuid'}.each {println it}
println uuid
def xml = new XmlNodePrinter().print(responseXml)