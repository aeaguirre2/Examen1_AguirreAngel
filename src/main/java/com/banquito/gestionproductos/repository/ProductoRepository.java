package com.banquito.gestionproductos.repository;

import com.banquito.gestionproductos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByEstadoProducto(String estado);
    @Query("SELECT p FROM Producto p WHERE p.categoria.idCategoria = :idCategoria")
    List<Producto> findByCategoria(@Param("idCategoria") Integer idCategoria);

    @Query("SELECT p FROM Producto p WHERE p.stockActual < :stockMinimo")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombreProducto) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Producto> findByNombreProductoContaining(@Param("nombre") String nombre);

    @Query("SELECT p FROM Producto p WHERE p.estadoProducto = 'Activo' AND p.stockActual > 0")
    List<Producto> findProductosDisponibles();

    boolean existsByNombreProducto(String nombreProducto);

    @Query("SELECT p FROM Producto p WHERE p.precioVenta BETWEEN :precioMin AND :precioMax")
    List<Producto> findByRangoPrecios(@Param("precioMin") java.math.BigDecimal precioMin, 
                                     @Param("precioMax") java.math.BigDecimal precioMax);
} 