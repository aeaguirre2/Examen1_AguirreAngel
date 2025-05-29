package com.banquito.gestionproductos.repository;

import com.banquito.gestionproductos.entity.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Integer> {

    Optional<CategoriaProducto> findByNombreCategoria(String nombreCategoria);
    boolean existsByNombreCategoria(String nombreCategoria);

    @Query("SELECT c FROM CategoriaProducto c WHERE " +
           "LOWER(c.nombreCategoria) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    java.util.List<CategoriaProducto> buscarPorTexto(@Param("texto") String texto);
} 