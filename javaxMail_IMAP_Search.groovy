#!groovy
package scripts.cm
import groovy.time.TimeCategory

@Grapes(
        @Grab(group='com.sun.mail', module='javax.mail', version='1.5.5')
)
import javax.mail.*
import javax.mail.search.*

import static java.lang.System.exit

use (TimeCategory) {
    term = new FlagTerm(new Flags(Flags.Flag.SEEN), true)
    term = new AndTerm(term, new ReceivedDateTerm(DateTerm.GT, new Date() - 3.days))
}
mailBoxes = []

mailBoxes << ['poczta.foo.pl', 'domain\\user\\mailbox1', '!pa', 'Inbox']
mailBoxes << ['poczta.foo.pl', "domain\\uset\\mailbox2", '!pa', 'Inbox']

mailBoxes.each {mailbox ->
    (host, username, password, folderName) = mailbox
    processMessages(host, username, password, folderName, term) {message, messageIndex->
        println "email: ${messageIndex + 1} SentDate: ${message.sentDate} ReceivedDate: ${message.receivedDate} Sublect: ${message.subject} From: ${message.from} Sender: ${message.sender}"
        message.setFlag(Flags.Flag.SEEN, false)
    }
}

def processMessages(host, username, password, folderName, term, doWithMessage) {
    (store, folder) = getStoreAndFolder(host, username, password, folderName)
    messages = folder.search(term)


    FetchProfile fetchProfile = new FetchProfile()
    fetchProfile.add(FetchProfile.Item.CONTENT_INFO)
    fetchProfile.add(FetchProfile.Item.FLAGS)

    messagesFetchSize = 150
    if (messages.size() > messagesFetchSize) {
        messages = Arrays.copyOf(messages, messagesFetchSize)
    }

    startDate = new Date()
    println "Start fetch: ${startDate}"
    folder.fetch(messages, fetchProfile)
    stopDate = new Date()
    println "Stop fetch: ${stopDate.time - startDate.time} ms"


    println messages.eachWithIndex { Message message, messageIndex ->
        startDate = new Date()
        println "Start message: ${startDate}"

        doWithMessage(message, messageIndex)

        stopDate = new Date()
        println "Stop message: ${stopDate.time - startDate.time} ms"

        if (messageIndex >= messagesFetchSize) {
            folder.close(true)
            store.close()
            exit(0)
        }
    }
    folder.close(true)
    store.close()
}

def getStoreAndFolder(host, username, password, emailFolderName) {

    Properties props = new Properties()
    props.setProperty("mail.debug", "true")
    props.setProperty("mail.store.protocol", "imaps")
    props.setProperty("mail.imaps.host", host)
    props.setProperty("mail.imaps.port", '993')
    props.setProperty("mail.imaps.ssl.enable", "true")
    props.setProperty("mail.imaps.ssl.trust", "*")
    props.setProperty("mail.imaps.auth.mechanisms", "LOGIN")
    props.setProperty("mail.debug", "false")

    Session session = Session.getDefaultInstance(props, null)
    def store = session.getStore("imaps")
    store.connect(host, username, password)
    def folder = store.getFolder(emailFolderName)
    folder.open(Folder.READ_WRITE)
    return [store, folder]
}