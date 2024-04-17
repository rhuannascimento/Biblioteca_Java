package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{

}