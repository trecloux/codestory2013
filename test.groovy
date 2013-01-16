@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.6')
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.URLENC

def StringBuffer json = new StringBuffer('[');
def int i=1;
for (;i<50000;i++) {
    json.append('{ "VOL": "custom-'+i+'", "DEPART": '+i+', "DUREE": 5, "PRIX": 19},');
}
json.append('{ "VOL": "custom-'+i+'", "DEPART": '+i+', "DUREE": 5, "PRIX": 19}');
json.append(']');

//def http = new HTTPBuilder('http://localhost:5000/jajascript/')
//def http = new HTTPBuilder('http://codestory2013.unchticafe.fr/jajascript/')
def http = new HTTPBuilder('http://codestory-tometjerem.cloudfoundry.com/jajascript/');
def start = System.currentTimeMillis();
http.post( path: 'optimize', body: json.toString(),
           requestContentType: URLENC ) { resp ->
 
  println "Response time " + (System.currentTimeMillis() -start) + "ms"
  println "Response status: ${resp.statusLine}"
  assert resp.statusLine.statusCode == 200
}
