package io.realworld.backend.domain.aggregate.user;

import com.google.common.base.MoreObjects;
import io.realworld.backend.domain.aggregate.role.Role;
import java.util.Optional;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

@Entity
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.FIELD)
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id = 0;

  private @NotNull String email = "";
  private @NotNull String username = "";
  private @NotNull String passwordHash = "";

  private String bio = null;
  private String image = null;

  protected User() {}

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  private @NotNull Role role = new Role();

  public User(String email, String username, String passwordHash, Role role) {
    this(email, username, passwordHash);
    this.role = role;
  }

  /** Creates User instance. */
  public User(String email, String username, String passwordHash) {
    this.email = email;
    this.username = username;
    this.passwordHash = passwordHash;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public Optional<String> getBio() {
    return Optional.ofNullable(bio);
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public Optional<String> getImage() {
    return Optional.ofNullable(image);
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Role getRole() {
    return role;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("email", email)
        .add("username", username)
        .add("passwordHash", passwordHash)
        .add("bio", bio)
        .add("image", image)
        .toString();
  }
}
