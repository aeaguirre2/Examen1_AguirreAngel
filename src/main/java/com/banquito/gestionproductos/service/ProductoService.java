package com.banquito.gestionproductos.service;

import com.banquito.gestionproductos.entity.Producto;
import com.banquito.gestionproductos.entity.CategoriaProducto;
import com.banquito.gestionproductos.repository.ProductoRepository;
import com.banquito.gestionproductos.repository.CategoriaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;

    @Transactional(readOnly = true)
    public Optional<Producto> obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Producto crearProducto(Producto producto) {

        if (productoRepository.existsByNombreProducto(producto.getNombreProducto())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + producto.getNombreProducto());
        }

        if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() != null) {
            Optional<CategoriaProducto> categoria = categoriaProductoRepository.findById(producto.getCategoria().getIdCategoria());
            if (categoria.isEmpty()) {
                throw new RuntimeException("No se encontró la categoría con ID: " + producto.getCategoria().getIdCategoria());
            }
            producto.setCategoria(categoria.get());
        } else {
            throw new RuntimeException("La categoría es obligatoria para crear un producto");
        }

        if (producto.getEstadoProducto() == null || producto.getEstadoProducto().isEmpty()) {
            producto.setEstadoProducto("Activo");
        }

        return productoRepository.save(producto);
    }

    public Producto cambiarEstadoProducto(Integer id, String nuevoEstado) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("No se encontró el producto con ID: " + id);
        }

        if (!nuevoEstado.matches("^(Activo|Inactivo|Agotado)$")) {
            throw new RuntimeException("Estado inválido. Debe ser: Activo, Inactivo o Agotado");
        }

        Producto producto = productoOpt.get();
        producto.setEstadoProducto(nuevoEstado);

        return productoRepository.save(producto);
    }

    public Producto aumentarStock(Integer id, Integer cantidad, BigDecimal precioCompra) {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("No se encontró el producto con ID: " + id);
        }

        Producto producto = productoOpt.get();


        producto.setStockActual(producto.getStockActual() + cantidad);

        if (precioCompra != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            producto.setCostoCompra(precioCompra);

            BigDecimal nuevoPrecioVenta = precioCompra
                .multiply(new BigDecimal("1.25"))
                .setScale(2, RoundingMode.HALF_UP);
            producto.setPrecioVenta(nuevoPrecioVenta);
        }

        producto.setEstadoProducto("Activo");

        return productoRepository.save(producto);
    }

    public Producto disminuirStock(Integer id, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a 0");
        }

        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("No se encontró el producto con ID: " + id);
        }

        Producto producto = productoOpt.get();

        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + producto.getStockActual() + 
                                     ", cantidad solicitada: " + cantidad);
        }

        int nuevoStock = producto.getStockActual() - cantidad;
        producto.setStockActual(nuevoStock);

        if (nuevoStock == 0) {
            producto.setEstadoProducto("Agotado");
        }

        return productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorEstado(String estado) {
        return productoRepository.findByEstadoProducto(estado);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosPorCategoria(Integer idCategoria) {
        return productoRepository.findByCategoria(idCategoria);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosConStockBajo(Integer stockMinimo) {
        return productoRepository.findProductosConStockBajo(stockMinimo);
    }

    @Transactional(readOnly = true)
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreProductoContaining(nombre);
    }

    @Transactional(readOnly = true)
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findProductosDisponibles();
    }

    public Producto actualizarProducto(Integer id, Producto productoActualizado) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("No se encontró el producto con ID: " + id);
        }

        Producto producto = productoOpt.get();

        if (!producto.getNombreProducto().equals(productoActualizado.getNombreProducto()) &&
            productoRepository.existsByNombreProducto(productoActualizado.getNombreProducto())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + productoActualizado.getNombreProducto());
        }

        producto.setNombreProducto(productoActualizado.getNombreProducto());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecioVenta(productoActualizado.getPrecioVenta());
        producto.setCostoCompra(productoActualizado.getCostoCompra());
        producto.setStockActual(productoActualizado.getStockActual());
        producto.setEstadoProducto(productoActualizado.getEstadoProducto());

        if (productoActualizado.getCategoria() != null && productoActualizado.getCategoria().getIdCategoria() != null) {
            Optional<CategoriaProducto> categoria = categoriaProductoRepository.findById(productoActualizado.getCategoria().getIdCategoria());
            if (categoria.isEmpty()) {
                throw new RuntimeException("No se encontró la categoría con ID: " + productoActualizado.getCategoria().getIdCategoria());
            }
            producto.setCategoria(categoria.get());
        }

        return productoRepository.save(producto);
    }


    public void eliminarProducto(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("No se encontró el producto con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
} 