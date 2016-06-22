@Grab(group='org.grails', module = 'grails-datastore-gorm-hibernate4', version='5.0.8.RELEASE')
@Grab(group='org.springframework', module = 'spring-context-support', version='4.1.7.RELEASE')
@Grab(group='org.springframework', module = 'spring-jdbc', version='4.1.7.RELEASE')
@Grab(group='oracle.jdbc', module = 'oracle-driver', version='11.1.0.7.0')

import grails.gorm.annotation.Entity
import grails.orm.bootstrap.HibernateDatastoreSpringInitializer
import org.springframework.jdbc.datasource.DriverManagerDataSource

/*Classes*/
@Entity
class Person {
    String name
}

/*Script code*/

dataSource = new DriverManagerDataSource(url:'jdbc:oracle:thin:@xxxdb02prod:2025:in13dev', username:'XXX', password:'xxpass',
        driverClassName:'oracle.jdbc.OracleDriver')
initializer = new HibernateDatastoreSpringInitializer(['hibernate.log_sql':'true', 'hibernate.jdbc.use_get_generated_keys':'true'], Person)
initializer.configureForDataSource(dataSource)

Person.withNewSession{session ->
    println Person.count()
    person1 = new Person(name:"Przemek")
    person1.save()
}