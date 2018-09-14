#!groovy
import com.sun.mail.imap.*
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper

import javax.mail.*

@Grab(group='com.sun.mail', module='javax.mail', version='1.5.5')
@Grab(group='org.springframework', module='spring-context-support', version='4.3.11.RELEASE')

sender = getSender()

100.times { count ->
    message = prepareMessage(sender, "message nr: ${count + 1}")
    sender.send(message)
    println "Message sent ${count + 1}"
}

JavaMailSender getSender() {
    Properties props = new Properties()
    props.setProperty("mail.debug", "true")
    props.setProperty("mail.transport.protocol", "smtp")
    props.setProperty("mail.smtp.auth.mechanisms", "NTLM")
    props.setProperty("mail.smtp.starttls.enable", "false")
    props.setProperty("mail.smtp.starttls.required", "false")
    props.setProperty("mail.smtp.host", "poczta.pl")
    props.setProperty("mail.smtp.port", "25")
    props.setProperty("mail.smtp.ssl.trust", "*")
    props.setProperty("mail.smtp.timeout", "12000")
    props.setProperty("mail.smtp.connectiontimeout", "6000")
    props.setProperty("mail.smtp.auth", "false")
    props.setProperty("mail.smtp.auth.ntlm.domain","DOMAIN")

    def senderImpl = new JavaMailSenderImpl()
    senderImpl.setJavaMailProperties(props)
    senderImpl.setUsername("user@foo.bar")
    senderImpl.setPassword("666777666")

    senderImpl
}

def prepareMessage(JavaMailSender sender, def info) {
    def list = [
            "1addr@domain.pl",
            "2addr@domain.pl",
            "3addr@domain.pl",
    ] as String[]
    helper = new MimeMessageHelper(sender.createMimeMessage(), MimeMessageHelper.MULTIPART_MODE_MIXED)
    helper.setTo(list)
    helper.setFrom('przemyslaw.krawczyk@gmail.com')
    messageStr = "Project mailboxes test ${info}"
    helper.setSubject(messageStr)
    helper.setText(messageStr)
    file = new File($/~/dev_sandbox/atachment.msg/$)
    helper.addAttachment(file.getName(), file)
    helper.getMimeMessage()
}