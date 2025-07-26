//package com.mrt.utils;
//
//import java.io.File;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.List;
//import java.util.Properties;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//
///**
// * @author Murat.Degirmenci
// */
//
//public class SendEmail {
//
//    static String fileName = System.getProperty("use.dir") + File.separator + "target" + File.separator +
//                             ConfigReader.getProperty("CurrentPI") + "-TestCaseReport.xlsx";
//
//
//    public static void sendAttachmentInEmail(String recipient){
//        String from ="dadas4ever@hotmail.com";
//        final String username ="username";
//        final String password ="password";
//
//        Properties props =new Properties();
//        props.put("mail.smtp.auth","true");
//        props.put("mail.smtp.starttls.enable","true");
//        props.put("mail.smtp.host","smtp.gmail.com");
//        props.put("mail.smtp.port","587");
//
//        Session session =Session.getInstance(props, new javax.mail.Authenticator(){
//            protected PasswordAuthentication getPasswordAuthentication(){
//                return new PasswordAuthentication(username,password);
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress (from));
//            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(recipient));
//            message.setSubject("Test case report for " + ConfigReader.getProperty("currentPI"));
//
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setText("Test report in excell file");
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(messageBodyPart);
//            messageBodyPart = new MimeBodyPart();
//
//            DataSource source = new FileDataSource(fileName);
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(fileName);
//            multipart.addBodyPart(messageBodyPart);
//            message.setContent(multipart);
//            //SEND MESSAGE
//            Transport.send(message);
//
//        }catch (MessagingException e){
//            throw new RuntimeException(e);
//        }
//    }
//
//
//    public void sendEmailToRecipient(){
//
//        ZoneId zone = ZoneId.of("America/New_York");
//        ZonedDateTime dateTime = ZonedDateTime.now(zone);
//        String day =dateTime.getDayOfWeek().toString();
//
//        if (day.equalsIgnoreCase("Saturday")){
//            ReadData data = new ReadData();
//            List<String> emails = data.getEmails();
//            for (String email : emails){
//                sendAttachmentInEmail(EncryptionUtils.decrypt(email));
//            }
//            System.out.println("Sent email successfully....");
//        }else {
//
//            System.out.println(day + " is not a scheduled day ");
//        }
//    }
//
//}
