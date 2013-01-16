@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.6')
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.URLENC

def url;
def size;
if (args.length == 0) {
    url = 'http://codestory2013.unchticafe.fr/jajascript/'
    size = 100000
} else if (args.length == 1) {
    url = 'http://codestory2013.unchticafe.fr/jajascript/'
    size = Integer.parseInt(args[0])
} else if (args[0] == "local") {
    url = "http://localhost:5000/jajascript/"
    size = Integer.parseInt(args[1])
} else if (args[0] == "test") {
    url = 'http://code-story-test.herokuapp.com/jajascript/'
    size = Integer.parseInt(args[1])
} else if (args[0] == "cloudfoundry") {
    url = 'http://codestory-tometjerem.cloudfoundry.com/jajascript/'
    size = Integer.parseInt(args[1])
}

println("Url : " + url)
println("Size : " + size)

def json = new StringBuffer('[')
def i=1
for (;i<size;i++) {
    json.append('{ "VOL": "custom-'+i+'", "DEPART": '+i+', "DUREE": 5, "PRIX": 19},')
}
json.append('{ "VOL": "custom-'+i+'", "DEPART": '+i+', "DUREE": 5, "PRIX": 19}')
json.append(']')



def http = new HTTPBuilder(url)
def start = System.currentTimeMillis();
http.post( path: 'optimize', body: json.toString(),
           requestContentType: URLENC ) { resp, jsonResp ->
 
  println "Response time " + (System.currentTimeMillis() -start) + "ms"
  println "Response status: ${resp.statusLine}"
  //println "Response data : ${jsonResp}"
  assert resp.statusLine.statusCode == 200
}
