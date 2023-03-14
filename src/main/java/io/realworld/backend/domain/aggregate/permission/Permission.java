package io.realworld.backend.domain.aggregate.permission;

import io.realworld.backend.domain.aggregate.role.Role;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.checkerframework.framework.qual.TypeUseLocation;

@Entity
@DefaultQualifier(value = Nullable.class, locations = TypeUseLocation.FIELD)
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id = 0;

  private @NotNull String name = "";

  @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
  private Collection<Role> roles = new ArrayList<>();

  protected Permission() {}

  public Permission(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
