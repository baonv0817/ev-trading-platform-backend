package com.fpt.evplatform.modules.auth;


import com.fpt.evplatform.modules.salepost.repository.SalePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component("postAuth")
@RequiredArgsConstructor
public class PostAuthorization {

    private final SalePostRepository salePostRepository;

    /**
     * Cho phép nếu: ADMIN hoặc chủ bài đăng (so theo username).
     */
    public boolean canModifySalePost(Authentication authentication, Integer listingId) {
        if (authentication == null || !authentication.isAuthenticated() || listingId == null) return false;

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ADMIN".equals(a.getAuthority()));
        if (isAdmin) return true;

        String username = null;
        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            var token = jwtAuth.getToken();
            username = token.getClaimAsString("preferred_username");
            if (username == null || username.isBlank()) {
                username = token.getSubject();
            }
        } else {
            username = authentication.getName();
        }
        if (username == null || username.isBlank()) return false;

        String finalUsername = username;
        return salePostRepository.findById(listingId)
                .map(post -> post.getSeller() != null
                        && post.getSeller().getUsername() != null
                        && post.getSeller().getUsername().equalsIgnoreCase(finalUsername))
                .orElse(false);
    }
}
