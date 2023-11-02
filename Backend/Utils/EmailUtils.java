package com.spring.CMS.Utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailUtils {
	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String text, List<String> list) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("skoul098@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		if (list != null && list.size() > 0)
			message.setCc(getCc(list));
		emailSender.send(message);
	}

	private String[] getCc(List<String> list) {
		String[] cc = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			cc[i] = list.get(i);
		}
		return cc;
	}

	public void forgotMail(String to, String subject, String password) throws MessagingException {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("skoul098@gmail.com");
		message.setTo(to);
		message.setSubject(subject);
		message.setText("<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> " + to
				+ " <br><b>Password: </b> " + password
				+ "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>");
		emailSender.send(message);
		/*
		 * MimeMessage message = emailSender.createMimeMessage(); MimeMessageHelper
		 * helper = new MimeMessageHelper(message, true);
		 * helper.setFrom("skoul098@gmail.com"); helper.setTo(to);
		 * helper.setSubject(subject); String htmlMsg =
		 * "<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> "
		 * + to + " <br><b>Password: </b> " + password +
		 * "<br><a href=\"http://localhost:4200/\">Click here to login</a></p>";
		 * message.setContent(htmlMsg, "text/html"); emailSender.send(message);
		 */
	}
}
