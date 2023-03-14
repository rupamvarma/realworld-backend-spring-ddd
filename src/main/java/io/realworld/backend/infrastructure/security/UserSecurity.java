package io.realworld.backend.infrastructure.security;

import io.realworld.backend.domain.aggregate.article.Article;
import io.realworld.backend.domain.aggregate.article.ArticleRepository;
import io.realworld.backend.domain.aggregate.comment.Comment;
import io.realworld.backend.domain.aggregate.comment.CommentRepository;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

  private final ArticleRepository articleRepo;

  private final CommentRepository commentRepo;

  @Autowired
  public UserSecurity(ArticleRepository articleRepo, CommentRepository commentRepo) {
    this.articleRepo = articleRepo;
    this.commentRepo = commentRepo;
  }

  /**
   * Method for performing authority
   */
  public boolean checkAuthority(Authentication authentication, String slug, long id) {
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority ga : authorities) {
      if ("hasAdminAccess".equals(ga.getAuthority())) {
        return true;
      }
    }
    Optional<Article> articleOpt = articleRepo.findBySlug(slug);
    String username = "";
    if (articleOpt.isPresent()) {
      username = articleOpt.get().getAuthor().getEmail();
    }
    if (username.equals(authentication.getName())) {
      return true;
    }

    if (id != -1) {
      Optional<Comment> commentOptional = commentRepo.findById(id);
      if (commentOptional.isPresent()) {
        Comment comment = commentOptional.get();
        username = comment.getAuthor().getEmail();
        return username.equals(authentication.getName());
      }
    }
    return false;
  }
}
