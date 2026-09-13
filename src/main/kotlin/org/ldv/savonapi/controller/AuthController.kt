package org.ldv.savonapi.controller

import org.ldv.savonapi.dto.DemandeMdpReset
import org.ldv.savonapi.dto.RequeteInscription
import org.ldv.savonapi.dto.RequeteLogin
import org.ldv.savonapi.dto.RequeteMdpDTO
import org.ldv.savonapi.model.dao.ConfirmationUtilisateurDAO
import org.ldv.savonapi.model.dao.ResetMdpDAO
import org.ldv.savonapi.model.dao.RoleDAO
import org.ldv.savonapi.model.dao.UtilisateurDAO
import org.ldv.savonapi.model.entity.ConfirmationUtilisateur
import org.ldv.savonapi.model.entity.ResetMdp
import org.ldv.savonapi.model.entity.Utilisateur
import org.ldv.savonapi.security.JwtService
import org.ldv.savonapi.service.MailService
import org.ldv.savonapi.service.TokenService
import org.springframework.security.access.prepost.PreAuthorize

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@CrossOrigin
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val utilisateurRepository: UtilisateurDAO,
    private val roleRepository: RoleDAO,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val mailService: MailService,
    private val tokenService: TokenService,
    private val confirmationUtilisateurDAO: ConfirmationUtilisateurDAO,
    private val resetMdpRepository: ResetMdpDAO
) {

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    fun login(@RequestBody request: RequeteLogin): Map<String, String> {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.identifier,
                request.password
            )
        )

        val token = jwtService.generateToken(request.identifier)

        return mapOf("token" to token)
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    fun register(@RequestBody request: RequeteInscription): Map<String, String> {

        if (utilisateurRepository.existsByUsername(request.username)) {
            throw RuntimeException("Username déjà utilisé")
        }

        if (utilisateurRepository.existsByEmail(request.email)) {
            throw RuntimeException("Email déjà utilisé")
        }

        val roleUser = roleRepository.findByNomLogic("ROLE_UTILISATEUR")
            ?: throw RuntimeException("Role introuvable")

        val utilisateur = Utilisateur(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = roleUser
        )

        utilisateurRepository.save(utilisateur)
        val tokenConfirmation = tokenService.generateToken()
        this.mailService.envoyerConfirmationInscription(utilisateur.email, tokenConfirmation)
        val tokenConfirmationGHash = tokenService.hashToken(tokenConfirmation)
        val confirmation = ConfirmationUtilisateur(utilisateur = utilisateur, tokenHash = tokenConfirmationGHash)
        confirmationUtilisateurDAO.save(confirmation)
        val token = jwtService.generateToken(utilisateur.username)

        return mapOf("token" to token)
    }

    @GetMapping("/confirm-inscription")
    fun confirmInscription(@RequestParam("key") key: String): Map<String, String>? {

        val confirmation = confirmationUtilisateurDAO.findBytokenHash(tokenService.hashToken(key))
        if (confirmation != null && LocalDateTime.now().isBefore(confirmation.expiration)) {
            val utilisateur = confirmation.utilisateur
            utilisateur.estActif = true
            utilisateurRepository.save(confirmation.utilisateur)
            return mapOf("result" to "ok")
        }
        return mapOf("result" to "echec")
    }

    @PostMapping("/mdp-oublie")
    fun mdpOublie(@RequestBody demande: DemandeMdpReset){
        val email = demande.email
        val errors = mutableListOf<String>()
        val utilisateur = utilisateurRepository.findByUsernameOrEmail(email, email)
        if (utilisateur == null) {
            errors.add("Email introuvable")
        }
        else{
            if (!utilisateur.estActif ) {
                errors.add("Utilisateur non actif")
            }
            else {
                val exDemande = resetMdpRepository.findByUtilisateur_Id(utilisateur.id!!)
                exDemande.forEach { it.estUtilise = true }
                resetMdpRepository.saveAll(exDemande)

                val tokenMdp = tokenService.generateToken()
                val tokenMdpHash = tokenService.hashToken(tokenMdp)
                val resetMdp = ResetMdp(keyHash = tokenMdpHash, utilisateur = utilisateur)
                resetMdpRepository.save(resetMdp) // Envoi du mail
                mailService.envoyerMailResetMdp(
                    email = utilisateur.email,
                    token = tokenMdp,
                    codeSecret = resetMdp.codeSecret
                )
            }
            }
        println(errors)
    }

    @PostMapping("/mdp-reset")
    fun mdpReset(@RequestParam("key")key: String,@RequestBody requeteMdpDTO: RequeteMdpDTO): Map<String, Any>{
val errors = mutableListOf<String>()
        val demande = resetMdpRepository.findByKeyHash(tokenService.hashToken(key))
        if (demande !=null){
            if (LocalDateTime.now().isAfter(demande.expiration)){
                errors.add("expiration du code")
            }
            if (requeteMdpDTO.nouveauMotDePasse != requeteMdpDTO.nouveauMotDePasseConfirmation){
                errors.add("les mots de passe ne correspondent pas")
            }
            if(requeteMdpDTO.code != demande.codeSecret){
                errors.add("code invalide")
            }
            if (demande.estUtilise){
                errors.add("deja utilise")
            }
        }
        else{
            errors.add("code invalide")
        }
        if (errors.isEmpty()){
            val utilisateur = demande!!.utilisateur
            utilisateur.password = passwordEncoder.encode(requeteMdpDTO.nouveauMotDePasse)
            utilisateurRepository.save(utilisateur)
            demande.estUtilise = true
            resetMdpRepository.save(demande)
            return mapOf("result" to "ok")
        }
        else{
            return mapOf("errors" to errors)
        }
    }

    @GetMapping("/testMail")
    fun testMail() {
        this.mailService.envoyerMail("timomoulin@msn.com", "test", "test")
    }
}