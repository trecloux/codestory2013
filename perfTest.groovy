@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.6')
import groovyx.net.http.HTTPBuilder

import static groovyx.net.http.ContentType.URLENC
import static java.lang.Integer.parseInt

def url = urlParam()
println("Url : ${url}")
def orderSize = orderSizeParam()
println("Order size : ${orderSize}" )
def payload = buildJsonRequest(orderSize)
callServer(url, payload)


private void callServer(String url, String requestPayload) {
    def http = new HTTPBuilder(url)
    def start = System.currentTimeMillis();
    http.post(path: 'optimize', body: requestPayload.toString(),
            requestContentType: URLENC) { resp, jsonResp ->

        def duration = System.currentTimeMillis() - start
        println "Response time ${duration} ms"
        println "Response status: ${resp.statusLine}"
        //println "Response data : ${jsonResp}"
        assert resp.statusLine.statusCode == 200
    }
}

private String buildJsonRequest(int size) {
    def random = new Random();
    def json = new StringBuffer('[')
    for (int i=0; i < size; i++) {
        def duration = random.nextInt(10) +1
        def price = random.nextInt(25) +1
        json.append('{"VOL":"custom-' + i + '","DEPART":' + i + ',"DUREE":' + duration + ',"PRIX": '+ price +'}')
        if (i < (size-1)) {
            json.append(',')
        }
    }
    json.append(']')
    jsonAsString = json.toString();
    println("JSon payload is ready. Size of the payload: ${jsonAsString.length()}")
    jsonAsString
}

private int orderSizeParam() {
    if (args.length == 0) {
        return 100000
    } else if (args.length == 1) {
        return parseInt(args[0])
    } else {
        return parseInt(args[1])
    }
}

private String urlParam() {
    if (args.length < 2 || args[0] == 'heroku') {
        return 'http://codestory2013.unchticafe.fr/jajascript/'
    } else if (args[0] == "local") {
        return "http://localhost:5000/jajascript/"
    } else if (args[0] == "test") {
        return 'http://code-story-test.herokuapp.com/jajascript/'
    } else if (args[0] == "cloudfoundry") {
        return 'http://codestory-tometjerem.cloudfoundry.com/jajascript/'
    }
}



