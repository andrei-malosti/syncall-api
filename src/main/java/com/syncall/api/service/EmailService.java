package com.syncall.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendFirstTimeEmail(String name, String email, String password) {
        String htmlTemplate = """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <body>
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                        <h2>Olá, ${name}!</h2>
                        <p>Sua conta Syncall acabou de ser criada.</p>
                        <p>Abaixo estão suas informações de login</p>
                        <p>email: ${email}</p>
                        <p>senha: ${password}</p>
                        <p>Não se esqueca de trocar sua senha de acesso gerada aleatoriamente para uma mais segura!</p>
                        <p>Se você não possui relação a esta solicitação, pode ignorar este e-mail com segurança.</p>
                    </div>
                </body>
                </html>
                """;

        String htmlBody = htmlTemplate
                .replace("${name}", name)
                .replace("${email}", email)
                .replace("${password}", password);


        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(email);
            helper.setSubject("Acesso a plataforma Syncall");
            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("E-mail de primeiro acesso enviado com sucesso para: {}", email);
        } catch (MailException | MessagingException e) {
            log.error("Falha ao enviar e-mail de primeiro acesso para {}. Erro: {}", email, e.getMessage());
        }
    }

}
