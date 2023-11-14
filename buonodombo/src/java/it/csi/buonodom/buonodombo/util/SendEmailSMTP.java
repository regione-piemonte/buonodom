/*******************************************************************************

* Copyright Regione Piemonte - 2023

* SPDX-License-Identifier: EUPL-1.2

******************************************************************************/
package it.csi.buonodom.buonodombo.util;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.mail.smtp.SMTPTransport;

@Component
public class SendEmailSMTP extends LoggerUtil {

	@Value("${nameServer}")
	private String host;

	@Value("${portServer}")
	private String port;

	@Value("${indirizzoFrom}")
	private String from;

	@Value("${usernameEmail}")
	private String username;

	@Value("${passEmail}")
	private String password;

	// FIXME fare refactoring bisogna concordare dove prelevare tutte le variabili
	// statiche !!!!!!!

	// private static final String SMTP_SERVER = "mailfarm-app.csi.it";

	// private static final String EMAIL_TO = "tommaso.ruggiero@csi.it,
	// tommaso.ruggiero@gmail.com";
	private static final String EMAIL_TO_CC = "";

	private static final String EMAIL_SUBJECT_CALL_VERIFICA = "Errore nel raggiungere end point: ";
	private static final String EMAIL_TEXT_CALL_VERIFICA = "Si è verificato un errore nel raggiungere l'end-point in oggetto \n  callVerificaServizio non risponde";
	private static final String EMAIL_SUBJECT_CALL_ACQ_REVOCA = "Errore nel raggiungere end point: ";
	private static final String EMAIL_TEXT_CALL_ACQ_REVOCA = "Si è verificato un errore mentre si stava inoltrando un consenso verso l'end-point in oggetto: ";
	private static final String EMAIL_SUBJECT_DEFAULT = "Errore non codificato ";
	private static final String EMAIL_TEXT_DEFAULT = "Si è verificato un errore non codificato nella classe SendEmailSMTP";

	// eliminato parametro Map<String, String> mailProperties
	public void send(String to, String bodyMessage, String oggetto) throws AddressException, MessagingException {
		logInfo("Send mail", "Entro in SendEmailSMTP metodo send");

		Properties prop = System.getProperties();
		prop.put("mail.smtp.host", host); // optional, defined in SMTPTransport
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.smtp.port", port); // default port 25

		// System.out.println("MailFrom="+getMailFrom);
		Session session = Session.getInstance(prop, null);
		Message msg = new MimeMessage(session);

		// from (String)mailProperties.get("spring.mail.from")
		String ff = from;
		msg.setFrom(new InternetAddress(ff));

		// to
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

//			// cc
//            msg.setRecipients(Message.RecipientType.CC,
//                    InternetAddress.parse(EMAIL_TO_CC, false));

//            switch(tipoErrore) {
//            case 1:
//            	// subject
//            	msg.setSubject(EMAIL_SUBJECT_CALL_VERIFICA+urlEndPoint);		
//            	// content 
//            	msg.setText(EMAIL_TEXT_CALL_VERIFICA+"\n"+eccezione);
//            	break;
//            case 2:
//            	//errore nella fase di invio massivo
//            	// subject
//            	msg.setSubject(EMAIL_SUBJECT_CALL_ACQ_REVOCA+urlEndPoint+" cons_id:"+cons_id);		
//            	// content 
//            	msg.setText(EMAIL_TEXT_CALL_ACQ_REVOCA+"\n"+eccezione);
//            	break;
//            case 3:
//            	 msg.setRecipients(Message.RecipientType.TO,
//                         InternetAddress.parse(mailTO, false));
//            	//errore durante notifica al cittadino
//            	// subject
//            	msg.setSubject(oggettoMail);		
//            	// content 
//            	msg.setText(bodyMail+urlEndPoint+"\n"+eccezione);
//            	break;
//            default:
//            	// subject
//            	msg.setSubject(EMAIL_SUBJECT_DEFAULT);		
//            	// content 
//            	msg.setText(EMAIL_TEXT_DEFAULT+"\n"+eccezione);    	
//            }

		msg.setSubject(oggetto);
		// content
		msg.setText(bodyMessage);
		msg.setSentDate(new Date());

		// Get SMTPTransport
		SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

		// connect
		t.connect(host, username, password);

		// send
		t.sendMessage(msg, msg.getAllRecipients());

		t.close();

		logInfo("Send mail", "Esco da SendEmailSMTP metodo send");
	}

}
