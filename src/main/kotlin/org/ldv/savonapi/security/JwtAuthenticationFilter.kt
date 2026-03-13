package org.ldv.savonapi.security
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtService: JwtService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val header = request.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = header.substring(7)
        val username = jwtService.extractUsername(token)
        val role = jwtService.extractClaim(token)

        val auth = UsernamePasswordAuthenticationToken(
            username,
            null,
            listOf(SimpleGrantedAuthority(role))
        )

        SecurityContextHolder.getContext().authentication = auth

        filterChain.doFilter(request, response)
    }
}