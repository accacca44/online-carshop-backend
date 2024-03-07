package edu.bbte.idde.keim2152.spring.repository;

import edu.bbte.idde.keim2152.spring.model.domain.User;

public interface UserDao extends AbstractDao<User> {
    User findByEmail(String email);

    User findByUsername(String username);
}
