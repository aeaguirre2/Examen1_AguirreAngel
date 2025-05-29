package com.banquito.gestionproductos.controller;

import com.banquito.gestionproductos.entity.CategoriaProducto;
import com.banquito.gestionproductos.service.CategoriaProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestion-productos/categorias")
@CrossOrigin(origins = "*")
public class CategoriaProductoController {

    @Autowired
    private CategoriaProductoService categoriaProductoService;

    @GetMapping
    public ResponseEntity<List<CategoriaProducto>> obtenerTodasLasCategorias() {
        try {
            List<CategoriaProducto> categorias = categoriaProductoService.obtenerTodasLasCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PostMapping
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaProducto categoria) {
        try {
            CategoriaProducto nuevaCategoria = categoriaProductoService.crearCategoria(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, 
                                                @Valid @RequestBody CategoriaProducto categoria) {
        try {
            CategoriaProducto categoriaActualizada = categoriaProductoService.actualizarCategoria(id, categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Integer id) {
        try {
            categoriaProductoService.eliminarCategoria(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeCategoria(@PathVariable Integer id) {
        try {
            boolean existe = categoriaProductoService.existeCategoria(id);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 