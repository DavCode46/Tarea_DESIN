package com.davidmb.tarea3ADbase.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.davidmb.tarea3ADbase.models.User;


/**
 * Repositorio que gestiona las operaciones de acceso a datos para la entidad `User`.
 * 
 * `User` representa a los usuarios registrados en la aplicación.
 * 
 * Extiende `JpaRepository`, proporcionando métodos para realizar operaciones CRUD sobre `User`.
 * 
 * @author DavidMB
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     * 
     * @param email Dirección de correo electrónico del usuario.
     * @return El objeto `User` correspondiente si existe, o `null` en caso contrario.
     */
	User findByEmail(String email);
}
