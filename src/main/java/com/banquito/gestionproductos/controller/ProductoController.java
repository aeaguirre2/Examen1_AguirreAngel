package com.banquito.gestionproductos.controller;

import com.banquito.gestionproductos.entity.Producto;
import com.banquito.gestionproductos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/gestion-productos/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;


    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Integer id) {
        try {
            Optional<Producto> producto = productoService.obtenerProductoPorId(id);
            return producto.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodosLosProductos() {
        try {
            List<Producto> productos = productoService.obtenerTodosLosProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.crearProducto(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoProducto(@PathVariable Integer id, 
                                                  @RequestBody Map<String, Object> requestData) {
        try {
            String nuevoEstado = (String) requestData.get("nuevoEstado");
            if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("El campo 'nuevoEstado' es obligatorio");
            }
            
            Producto producto = productoService.cambiarEstadoProducto(id, nuevoEstado);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @PutMapping("/{id}/aumentar-stock")
    public ResponseEntity<?> aumentarStock(@PathVariable Integer id, 
                                          @RequestBody Map<String, Object> requestData) {
        try {
            Object cantidadObj = requestData.get("cantidad");
            Object precioCompraObj = requestData.get("precioCompra");
            
            if (cantidadObj == null) {
                return ResponseEntity.badRequest().body("El campo 'cantidad' es obligatorio");
            }
            
            Integer cantidad = Integer.valueOf(cantidadObj.toString());
            BigDecimal precioCompra = null;
            
            if (precioCompraObj != null) {
                precioCompra = new BigDecimal(precioCompraObj.toString());
            }
            
            Producto producto = productoService.aumentarStock(id, cantidad, precioCompra);
            return ResponseEntity.ok(producto);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Formato de número inválido");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @PutMapping("/{id}/disminuir-stock")
    public ResponseEntity<?> disminuirStock(@PathVariable Integer id, 
                                           @RequestBody Map<String, Object> requestData) {
        try {
            Object cantidadObj = requestData.get("cantidad");
            
            if (cantidadObj == null) {
                return ResponseEntity.badRequest().body("El campo 'cantidad' es obligatorio");
            }
            
            Integer cantidad = Integer.valueOf(cantidadObj.toString());
            
            Producto producto = productoService.disminuirStock(id, cantidad);
            return ResponseEntity.ok(producto);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Formato de número inválido");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Producto>> obtenerProductosPorEstado(@PathVariable String estado) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorEstado(estado);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Producto>> obtenerProductosPorCategoria(@PathVariable Integer idCategoria) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorCategoria(idCategoria);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Producto>> obtenerProductosConStockBajo(@RequestParam Integer stockMinimo) {
        try {
            List<Producto> productos = productoService.obtenerProductosConStockBajo(stockMinimo);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductosPorNombre(@RequestParam String nombre) {
        try {
            List<Producto> productos = productoService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> obtenerProductosDisponibles() {
        try {
            List<Producto> productos = productoService.obtenerProductosDisponibles();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Integer id, 
                                               @Valid @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Integer id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Error interno del servidor");
        }
    }
} 