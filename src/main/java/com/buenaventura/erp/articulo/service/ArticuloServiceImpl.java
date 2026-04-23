package com.buenaventura.erp.articulo.service;

import com.buenaventura.erp.articulo.dto.ArticuloRequest;
import com.buenaventura.erp.articulo.dto.ArticuloResponse;
import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.categoria.entity.Categoria;
import com.buenaventura.erp.categoria.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final CategoriaRepository categoriaRepository;

    public ArticuloServiceImpl(ArticuloRepository articuloRepository,
                               CategoriaRepository categoriaRepository) {
        this.articuloRepository = articuloRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<ArticuloResponse> listar() {
        return articuloRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ArticuloResponse registrar(ArticuloRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Articulo articulo = new Articulo();
        articulo.setDescripcion(request.getDescripcion());
        articulo.setMedida(request.getMedida());
        articulo.setStock(request.getStock() == null ? BigDecimal.ZERO : request.getStock());
        articulo.setCategoria(categoria);
        articulo.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? "Activo"
                        : request.getEstado()
        );

        Articulo guardado = articuloRepository.save(articulo);
        return toResponse(guardado);
    }

    @Override
    public ArticuloResponse actualizar(Integer id, ArticuloRequest request) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        articulo.setDescripcion(request.getDescripcion());
        articulo.setMedida(request.getMedida());
        articulo.setStock(request.getStock() == null ? BigDecimal.ZERO : request.getStock());
        articulo.setCategoria(categoria);
        articulo.setEstado(
                request.getEstado() == null || request.getEstado().isBlank()
                        ? articulo.getEstado()
                        : request.getEstado()
        );

        Articulo actualizado = articuloRepository.save(articulo);
        return toResponse(actualizado);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        articulo.setEstado("Inactivo");
        articuloRepository.save(articulo);
    }

    private ArticuloResponse toResponse(Articulo articulo) {
        ArticuloResponse response = new ArticuloResponse();
        response.setIdArticulo(articulo.getIdArticulo());
        response.setDescripcion(articulo.getDescripcion());
        response.setMedida(articulo.getMedida());
        response.setStock(articulo.getStock());
        response.setIdCategoria(
                articulo.getCategoria() != null ? articulo.getCategoria().getIdCategoria() : null
        );
        response.setDescripcionCategoria(
                articulo.getCategoria() != null ? articulo.getCategoria().getDescripcion() : null
        );
        response.setEstado(articulo.getEstado());
        return response;
    }
}