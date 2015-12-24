package eio.util.output;

import eio.domain.EmailOutputHandlerConfiguration;
import eio.domain.MailSecurity;
import eio.util.Address;
import org.apache.commons.validator.GenericValidator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailOutputHandler extends AbstractOutputHandler<EmailOutputHandlerConfiguration> {
    @Override
    public void process(EmailOutputHandlerConfiguration config, Address sender, Address recipient, String subject, String content) {
        Session session = getSession(config);
        try {
            MimeMessage message = createMimeMessage(session, sender);
            prepareMessage(config, message, recipient, subject, content);
            sendMail(session, config, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Session getSession(EmailOutputHandlerConfiguration config) {
        Session session = Session.getDefaultInstance(getProperties(config), null);
        return session;
    }

    private Properties getProperties(EmailOutputHandlerConfiguration config) {
        Properties props = new Properties();
        if (MailSecurity.TLS.equals(config.getSecurity())) {
            props.put("mail.smtp.starttls.enable", "true");
        } else if (MailSecurity.SSL.equals(config.getSecurity())) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        return props;
    }

    private MimeMessage createMimeMessage(Session session, Address sender) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender.getAddress(), sender.getName()));
        return message;
    }

    private void prepareMessage(EmailOutputHandlerConfiguration config, MimeMessage message, Address recipient, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        String contentType = config.getContentType() + "; charset=\"utf-8\"";

        if (GenericValidator.isBlankOrNull(recipient.getName())) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getAddress()));
        } else {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.getAddress(), recipient.getName()));
        }

        message.setHeader("Content-Type", contentType);

        message.setSubject(subject, "UTF-8");

        MimeMultipart multipart = new MimeMultipart();
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setHeader("Content-Type", contentType);
        messageBodyPart.setContent(content, contentType);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
    }

    private void sendMail(Session session, EmailOutputHandlerConfiguration config, MimeMessage message) throws MessagingException {
        Transport tr = session.getTransport("smtp");
        if (!config.getAuthorization()) {
            tr.connect(
                    config.getSmtpServer(),
                    config.getPort(),
                    null,
                    null
            );
        } else {
            tr.connect(
                    config.getSmtpServer(),
                    config.getPort(),
                    config.getUsername(),
                    config.getPassword()
            );
        }
        message.saveChanges();
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}
