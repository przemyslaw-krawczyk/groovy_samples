import groovy.json.*

def jsonText = '''\
{
   "id": 6000502935,
   "name": "Dorota Nowak",
   "firstName": "Dorota",
   "surname": "Nowak",
   "pesel": "666666666",
   "grantedPrivileges":    [
            {
         "id": 12,
         "name": "ProductMenuX"
      },
            {
         "id": 14,
         "name": "FeatureX"
      },
            {
         "id": 15,
         "name": "AnnexingFeature"
      },
            {
         "id": 30,
         "name": "PaymentWayCash"
      }
   ],
   "deniedPrivileges": []
}      
'''
class User {
   String id
   String name
   String firstName
   String surname
   String pesel
   List<Privilege> grantedPrivileges
   List<Privilege> deniedPrivileges 
}
class Privilege {
   def id
   def name
}
def user = new JsonSlurper().parseText(jsonText)
User u = new User(user)
User u2 = new User(user)
u.grantedPrivileges.find {it.id == 12}.name = "XXX"
u2.grantedPrivileges.find {it.id == 14}.name = "XXX"
//user.grantedPrivileges.each {priv -> println "id: ${priv.id} name:${priv.name}"}
println "U1"
u.grantedPrivileges.each {priv -> println "id: ${priv.id} name:${priv.name}"}
println "U2"
u2.grantedPrivileges.each {priv -> println "id: ${priv.id} name:${priv.name}"}