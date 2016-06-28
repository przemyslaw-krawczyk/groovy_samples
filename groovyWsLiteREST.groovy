@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')
import wslite.rest.*

def client = new RESTClient("http://host:666/drools-guvnor/rest/")
def response = client.get(
        path:'/packages/pl.mycompany.mypackage/assets/APPLICATION',
        accept: ContentType.XML,
        query:[param1:'xxx', param2:true],
        headers:[
                "X-Foo":"bar",
                Authorization: 'Basic XXXX'
        ],
        connectTimeout: 5000,
        readTimeout: 10000,
        followRedirects: false,
        useCaches: false,
        sslTrustAllCerts: true
)
println response.xml.metadata.disabled