package edu.bbte.idde.keim2152.spring.repository.impl.jpa;

import edu.bbte.idde.keim2152.spring.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface JpaUserDao extends JpaRepository<User, Long> {
    Collection<User> findAllByEmail(String email);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
