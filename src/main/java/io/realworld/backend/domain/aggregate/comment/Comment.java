package io.realworld.backend.domain.aggregate.comment;

import com.google.common.base.MoreObjects;
import io.realworld.backend.domain.aggregate.article.Article;
import io.realworld.backend.domain.aggregate.user.User;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

@Entity
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.FIELD)
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id = 0;

  @ManyToOne private @NonNull Article article = new Article();
  @ManyToOne private @NonNull User author = new User("", "", "");
  private @NotNull String body = "";
  private @NotNull Instant createdAt = Instant.now();
  private @NotNull Instant updatedAt = Instant.now();

  private int likes = 0;

  private int dislikes = 0;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("article", article)
        .add("author", author)
        .add("createdAt", createdAt)
        .add("updatedAt", updatedAt)
        .toString();
  }

  public int getLikes() {
    return likes;
  }

  public void setLikes(int likes) {
    this.likes = likes;
  }

  public int getDislikes() {
    return dislikes;
  }

  public void setDislikes(int dislikes) {
    this.dislikes = dislikes;
  }

  public void incrementCommentLikes() {
    this.setLikes(this.getLikes() + 1);
  }

  public void decrementCommentLikes() {
    this.setLikes(this.getLikes() - 1);
  }

  public void incrementCommentDislikes() {
    this.setDislikes(this.getDislikes() + 1);
  }

  public void decrementCommentDislikes() {
    this.setDislikes(this.getDislikes() - 1);
  }
}
