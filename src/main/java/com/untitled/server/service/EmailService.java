package com.untitled.server.service;


import com.untitled.server.domain.auth.User;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    final Configuration configuration;
    final JavaMailSender javaMailSender;

    public EmailService(Configuration configuration, JavaMailSender javaMailSender) {
        this.configuration = configuration;
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordEmail(User user, String token) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Password Reset Token");
        helper.setTo(user.getEmail());
        String emailContent = getPasswordEmailContent(user, token);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    String getPasswordEmailContent(User user, String token) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("token", token);
        configuration.getTemplate("pwreset.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    public void sendRegEmail(User user, String token) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Please validate email");
        helper.setTo(user.getEmail());
        String emailContent = getRegEmailContent(user, token);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    String getRegEmailContent(User user, String token) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("token", token);
        configuration.getTemplate("emailvalidate.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    public void sendEmail(User user) throws MessagingException, IOException, TemplateException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Password Reset Token");
        helper.setTo(user.getEmail());
        String emailContent = getEmailContent(user);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    String getEmailContent(User user) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        configuration.getTemplate("pwreset.ftlh").process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}