package com.banquito.gestionproductos.service;

import com.banquito.gestionproductos.entity.CategoriaProducto;
import com.banquito.gestionproductos.repository.CategoriaProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaProductoService {

    @Autowired
    private CategoriaProductoRepository categoriaProductoRepository;


    @Transactional(readOnly = true)
    public List<CategoriaProducto> obtenerTodasLasCategorias() {
        return categoriaProductoRepository.findAll();
    }



    public CategoriaProducto crearCategoria(CategoriaProducto categoria) {
        if (categoriaProductoRepository.existsByNombreCategoria(categoria.getNombreCategoria())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoria.getNombreCategoria());
        }
        return categoriaProductoRepository.save(categoria);
    }


    public CategoriaProducto actualizarCategoria(Integer id, CategoriaProducto categoriaActualizada) {
        Optional<CategoriaProducto> categoriaExistente = categoriaProductoRepository.findById(id);
        
        if (categoriaExistente.isEmpty()) {
            throw new RuntimeException("No se encontró la categoría con ID: " + id);
        }

        CategoriaProducto categoria = categoriaExistente.get();

        if (!categoria.getNombreCategoria().equals(categoriaActualizada.getNombreCategoria()) &&
            categoriaProductoRepository.existsByNombreCategoria(categoriaActualizada.getNombreCategoria())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaActualizada.getNombreCategoria());
        }

        categoria.setNombreCategoria(categoriaActualizada.getNombreCategoria());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());

        return categoriaProductoRepository.save(categoria);
    }

    public void eliminarCategoria(Integer id) {
        if (!categoriaProductoRepository.existsById(id)) {
            throw new RuntimeException("No se encontró la categoría con ID: " + id);
        }
        categoriaProductoRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public boolean existeCategoria(Integer id) {
        return categoriaProductoRepository.existsById(id);
    }
} 