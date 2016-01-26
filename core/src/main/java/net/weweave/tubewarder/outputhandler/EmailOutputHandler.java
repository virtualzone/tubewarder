package net.weweave.tubewarder.outputhandler;

import net.weweave.tubewarder.outputhandler.config.*;
import net.weweave.tubewarder.service.model.AttachmentModel;
import net.weweave.tubewarder.util.Address;
import org.apache.commons.validator.GenericValidator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class EmailOutputHandler extends OutputHandler {
    public static final String ID = "EMAIL";

    public EmailOutputHandler(Map<String, Object> config) {
        super(config);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Email";
    }

    @Override
    public void process(Address sender, Address recipient, String subject, String content, List<AttachmentModel> attachments) {
        Session session = getSession();
        try {
            MimeMessage message = createMimeMessage(session, sender);
            MimeMultipart multipart = prepareMessage(message, recipient, subject, content);
            appendAttachments(multipart, attachments);
            message.setContent(multipart);
            sendMail(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<OutputHandlerConfigOption> getConfigOptions() {
        List<OutputHandlerConfigOption> options = new ArrayList<>();
        options.add(new StringConfigOption("smtpServer", "SMTP Server", true, ""));
        options.add(new IntConfigOption("port", "Port", true, 25));
        options.add(new BoolConfigOption("auth", "Authentication required", false, false));
        options.add(new StringConfigOption("username", "Username", true, ""));
        options.add(new StringConfigOption("password", "Password", true, ""));
        SelectConfigOption security = new SelectConfigOption("security", "Security", true);
        security.addOption("NONE", "None");
        security.addOption("SSL", "SSL");
        security.addOption("TLS", "TLS");
        options.add(security);
        options.add(new StringConfigOption("contentType", "Content Type", true, "text/plain"));
        return options;
    }

    private Session getSession() {
        Session session = Session.getDefaultInstance(getProperties(), null);
        return session;
    }

    private Properties getProperties() {
        Map<String, Object> config = getConfig();
        Properties props = new Properties();
        if ("TLS".equals(config.getOrDefault("security", ""))) {
            props.put("mail.smtp.starttls.enable", "true");
        } else if ("SSL".equals(config.getOrDefault("security", ""))) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        return props;
    }

    private MimeMessage createMimeMessage(Session session, Address sender) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender.getAddress(), sender.getName()));
        return message;
    }

    private MimeMultipart prepareMessage(MimeMessage message, Address recipient, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        Map<String, Object> config = getConfig();
        String contentType = config.getOrDefault("contentType", "") + "; charset=\"utf-8\"";

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

        return multipart;
    }

    private void appendAttachments(MimeMultipart multipart, List<AttachmentModel> attachments) throws MessagingException {
        for (AttachmentModel attachment : attachments) {
            appendAttachment(multipart, attachment);
        }
    }

    private void appendAttachment(MimeMultipart multipart, AttachmentModel attachment) throws MessagingException {
        MimeBodyPart messageBodyPart = new PreencodedMimeBodyPart("base64");
        if (!GenericValidator.isBlankOrNull(attachment.contentType)) {
            messageBodyPart.setHeader("Content-Type", attachment.contentType);
        }
        messageBodyPart.setFileName(attachment.filename);
        messageBodyPart.setText(attachment.payload);
        multipart.addBodyPart(messageBodyPart);
    }

    private void sendMail(Session session, MimeMessage message) throws MessagingException {
        Transport tr = session.getTransport("smtp");
        Map<String, Object> config = getConfig();
        if (!(Boolean)config.getOrDefault("auth", false)) {
            tr.connect(
                    (String)config.getOrDefault("smtpServer", ""),
                    (Integer)config.getOrDefault("port", 25),
                    null,
                    null
            );
        } else {
            tr.connect(
                    (String)config.getOrDefault("smtpServer", ""),
                    (Integer)config.getOrDefault("port", 25),
                    (String)config.getOrDefault("username", ""),
                    (String)config.getOrDefault("password", "")
            );
        }
        message.saveChanges();
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}
