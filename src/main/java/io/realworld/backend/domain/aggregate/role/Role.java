package io.realworld.backend.domain.aggregate.role;

import io.realworld.backend.domain.aggregate.permission.Permission;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

@Entity
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.FIELD)
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id = 0;

  private @NotNull String name = "";

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "roles_permissions",
      joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
  private @NotNull Collection<Permission> permissions = new ArrayList<>();

  public Role() {}

  public Role(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<Permission> getPermissions() {
    return permissions;
  }
}
