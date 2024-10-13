package com.mna.aipj.external.email;

import com.mna.aipj.dto.exception.UserException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendSimpleEmail(EmailMessageInfo emailInfo) {
        logger.info("sending email...");
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String htmlContent = getHtmlTemplate(emailInfo);

            helper.setFrom("ai-gov-no-reply@tec.mx");
            helper.setTo(emailInfo.getToEmail());
            helper.setSubject("Resumen de Alertas Importantes");

            // Enviar correo con HTML
            helper.setText(htmlContent, true);

        } catch (MessagingException e) {
            throw new UserException("failed to send alert email", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        mailSender.send(mimeMessage);
        logger.info("email sent successfully");
    }

    private String getHtmlTemplate(EmailMessageInfo emailMessageInfo) {
        StringBuilder alertsHtml = new StringBuilder();

        for (AlarmInfo alarmInfo : emailMessageInfo.getAlarmInfoList()) {
            alertsHtml.append("""
                        <div style="border: 1px solid #ddd; padding: 10px; margin-top: 10px;">
                            <p><strong>Actor:</strong> %s</p>
                            <p><strong>Alerta:</strong> <span class="alert-%s">%s</span></p>
                            <p><strong>Tema:</strong> %s</p>
                            <p><strong>Título:</strong> %s</p>
                            <p><strong>Contenido:</strong> %s</p>
                        </div>
                    """.formatted(alarmInfo.getActor(),
                    alarmInfo.getAlertLevel(),
                    alarmInfo.getAlertLevel(),
                    alarmInfo.getTopic(),
                    alarmInfo.getTitle(),
                    alarmInfo.getContent()));
        }

        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Resumen del día %s AI Gov</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            margin: 0;
                            padding: 20px;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #fff;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        }
                        h1 {
                            color: #333;
                            text-align: center;
                        }
                        p {
                            color: #555;
                        }
                        .alert-high {
                            color: #ff0000;
                            font-weight: bold;
                        }
                        .alert-medium {
                            color: #ff9900;
                            font-weight: bold;
                        }
                        .alert-low {
                            color: #3399ff;
                            font-weight: bold;
                        }
                        .footer {
                            margin-top: 20px;
                            font-size: 12px;
                            color: #888;
                            text-align: center;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>Panel</h1>
                        <p><strong>Menciones totales:</strong> %d</p>
                        <p><strong>Menciones analizadas:</strong> %d</p>
                        <p><strong>Menciones desde la fecha (%s):</strong> %d</p>
                        <p><strong>Analizadas desde la fecha (%s):</strong> %d</p>
                
                        %s <!-- Aquí se agregarán las alertas -->
                
                        <p>Le recomendamos tomar las acciones necesarias basándose en estas alertas.</p>
                
                        <div class="footer">
                            <p>Este es un correo automatizado. No responda a este mensaje.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(LocalDate.now(), emailMessageInfo.getTotalMentions(),
                emailMessageInfo.getTotalAnalysed(), emailMessageInfo.getSinceLastDate()
                , emailMessageInfo.getMentionsSinceLastDate(), emailMessageInfo.getSinceLastDate(),
                emailMessageInfo.getAnalysedSinceLastDate(), alertsHtml.toString());
    }
}
