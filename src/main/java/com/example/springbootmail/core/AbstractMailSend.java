package com.example.springbootmail.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractMailSend {

    private static final Logger log = LoggerFactory.getLogger(AbstractMailSend.class);

    private JavaMailSender mailSender;

    public AbstractMailSend(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }


    public void sendSimpleText(String to, String from, String subject, String text) {
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text, false);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("sendSimpleText..mail send simple text failure:【{}】", e);
        }
    }

    /**
     * 发送带附件的Mail
     *
     * @param to
     * @param subject
     * @param text
     * @param file
     * @throws MessagingException
     */
    public void sendAttachmentMail(String to, String from, String subject, String text, File file) throws MessagingException {
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text, false);
            message.addAttachment("我是一个附件", file);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("sendAttachmentMail..mail send simple text failure:【{}】", e);
        }
    }


    public void sendHtmlAttachment(String to, String from, String subject) throws IOException, TemplateException, MessagingException {
        try {
            //HTML 文本
            String htmlText = "";

            //获取模板
            //创建配置实例
            Configuration configuration = new Configuration();
            //设置编码
            configuration.setDefaultEncoding("UTF-8");

            configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
            //获取模板
            Template template = configuration.getTemplate("email-send.ftl");
            //FreeMarker通过Map传递动态数据
            Map map = new HashMap();
            //注意动态数据的key和模板标签中指定的属性相匹配
            map.put("name", "kim");
            //注意动态数据的key和模板标签中指定的属性相匹配
            map.put("time", LocalDate.now());
            //解析模板并替换动态数据，最终username将替换模板文件中的${username}标签。
            htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();

            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(htmlText, true);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("sendAttachmentMail..mail send simple text failure:【{}】", e);
        }
    }


}
