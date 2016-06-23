@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')
import wslite.soap.*

def endpoint = 'http://host:port/serv_path/ProcessService'
def proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress('127.0.0.1', 9999))//Fidler :-)

def client = new SOAPClient(endpoint)

def response = client.send(SOAPAction: "http://ws.myapp.mycompany.com/process/uw/CreateProcess",
                           connectTimeout:5000,
                           readTimeout:10000,
                           useCaches:false,
                           followRedirects:false,
                           sslTrustAllCerts:true,
                           proxy:proxy) {
    //version SOAPVersion.V1_2        // SOAPVersion.V1_1 is default
    soapNamespacePrefix "soapenv"      // "soap-env" is default
   // encoding "ISO-8859-1"           // "UTF-8" is default encoding for xml
    envelopeAttributes "xmlns:ser":"http://ws.myapp.mycompany.com/process/common/types"
    header() {//Security headers pasted without changes from SOAP-UI
        mkp.yieldUnescaped """<wsse:Security soapenv:mustUnderstand="0" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
            <wsu:Timestamp wsu:Id="TS-7785B243A3AA9523BC146667584004338">
                <wsu:Created>2016-06-23T11:57:20.042Z</wsu:Created>
                <wsu:Expires>2016-06-23T12:59:20.042Z</wsu:Expires>
            </wsu:Timestamp>
            <wsse:UsernameToken wsu:Id="UsernameToken-7785B243A3AA9523BC146667584004237">
                <wsse:Username>user</wsse:Username>
                <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">pass</wsse:Password>
                <wsse:Nonce EncodingType="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary">XXXXX</wsse:Nonce>
                <wsu:Created>2016-06-23T11:57:20.042Z</wsu:Created>
            </wsse:UsernameToken>
        </wsse:Security>"""
    }
    body {
        "ser:CreateProcessRequest"(xmlns:"http://service.object.ws.mt.myapp.mycompany.com/") {
            objectId("1000666302903")
            typeId("0")
            productCode("7666")
            salesMessage("Message")
        }
    }
}

println "Response: $response.envelope"