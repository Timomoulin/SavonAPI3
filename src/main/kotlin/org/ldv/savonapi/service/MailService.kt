package org.ldv.savonapi.service


import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender
) {

    fun envoyerMail(
        destinataire: String,
        sujet: String,
        contenu: String
    ) {

        val message = SimpleMailMessage()

        message.from = "SavonApp.noreply@gmail.com"
        message.setTo(destinataire)
        message.subject = sujet
        message.text = contenu

        mailSender.send(message)
    }

    fun envoyerConfirmationInscription(
        destinataire: String,
        token: String
    ) {
        //TODO : A modifier si en fonction du serveur ou port du serveur
        val url = "http://localhost:8080/auth/confirm-inscription?key=$token"
        val message =
            """ Bonjour, Merci pour votre inscription. Pour confirmer votre adresse email, cliquez sur le lien suivant : $url Ce lien est valable pendant 24 heures. Si vous n'êtes pas à l'origine de cette inscription, vous pouvez ignorer cet email. Cordialement, L'équipe Savon API """.trimIndent()
        envoyerMail(destinataire, "Confirmation de votre inscription", message)
    }


    fun envoyerMailResetMdp(
        email: String,
        token: String,
        codeSecret: String
    ) {

        val lien = "http://localhost:4200/reset-mdp?key=$token"

        val message = SimpleMailMessage()

        message.setTo(email)
        message.subject = "Réinitialisation de votre mot de passe"

        message.text = """
        Bonjour,

        Une demande de réinitialisation de votre mot de passe a été effectuée.

        Pour continuer, cliquez sur le lien suivant :

        $lien

        Votre code secret est :

        $codeSecret

        Ce code est valable pendant 30 minutes.

        Si vous n'êtes pas à l'origine de cette demande, vous pouvez ignorer cet email.

        Cordialement,
        L'équipe Savon API
    """.trimIndent()

        mailSender.send(message)
    }


}

