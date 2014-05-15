package com.cista.pidb.core.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cista.pidb.core.PIDBContext;

public class MailManagerImpl implements MailManager {
    protected final Log logger = LogFactory.getLog(getClass());

    private MailSender mailSender;

    private SimpleMailMessage message;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setMessage(SimpleMailMessage message) {
        this.message = message;
    }

    public void place(MailTo mailTo) {

//        SimpleMailMessage msg = new SimpleMailMessage(this.message);
//        msg.setTo(mailTo.getAssignEmail());
//        //msg.setTo(new String[]{"wangx@cavell.com.tw"});
//        msg.setText(mailTo.getText());
//        msg.setSubject(mailTo.getSubject());
        
        try {
//        	mailSender.send(msg);
        	JavaMailSenderImpl sender = (JavaMailSenderImpl)mailSender;
        	MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper mimeHelper = new MimeMessageHelper(message, true,
                    "UTF-8");
            mimeHelper.setFrom(PIDBContext.getConfig("pidb.mail.from"));
        	mimeHelper.setSubject(mailTo.getSubject());
            mimeHelper.setText(mailTo.getText());
            mimeHelper.setTo(mailTo.getAssignEmail());
            sender.send(message);
        } catch (MessagingException ex0) {
            logger.error(ex0);
        } catch (MailException ex) {
            logger.error(ex);
        }
    }
}
