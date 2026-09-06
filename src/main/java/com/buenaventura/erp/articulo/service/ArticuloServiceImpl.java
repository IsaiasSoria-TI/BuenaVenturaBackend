package com.buenaventura.erp.articulo.service;

import com.buenaventura.erp.articulo.dto.ArticuloRequest;
import com.buenaventura.erp.articulo.dto.ArticuloResponse;
import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.categoria.entity.Categoria;
import com.buenaventura.erp.categoria.repository.CategoriaRepository;
import com.buenaventura.erp.inventario.tipoenvase.entity.TipoEnvase;
import com.buenaventura.erp.inventario.tipoenvase.repository.TipoEnvaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final CategoriaRepository categoriaRepository;
    private final TipoEnvaseRepository tipoEnvaseRepository;

    public ArticuloServiceImpl(ArticuloRepository articuloRepository,
                               CategoriaRepository categoriaRepository,
                               TipoEnvaseRepository tipoEnvaseRepository) {
        this.articuloRepository = articuloRepository;
        this.categoriaRepository = categoriaRepository;
        this.tipoEnvaseRepository = tipoEnvaseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloResponse> listar() {
        return articuloRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ArticuloResponse registrar(ArticuloRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        TipoEnvase tipoEnvase = tipoEnvaseRepository.findById(request.getIdTipoEnvase())
                .orElseThrow(() -> new RuntimeException("Tipo de envase no encontrado"));

        Articulo articulo = new Articulo();
        articulo.setDescripcion(request.getDescripcion().trim());
        articulo.setMedida(request.getMedida().trim());
        articulo.setTipoEnvase(tipoEnvase);
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
    @Transactional
    public ArticuloResponse actualizar(Integer id, ArticuloRequest request) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        TipoEnvase tipoEnvase = tipoEnvaseRepository.findById(request.getIdTipoEnvase())
                .orElseThrow(() -> new RuntimeException("Tipo de envase no encontrado"));

        articulo.setDescripcion(request.getDescripcion().trim());
        articulo.setMedida(request.getMedida().trim());
        articulo.setTipoEnvase(tipoEnvase);
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
    @Transactional
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
        if (articulo.getTipoEnvase() != null) {
            response.setIdTipoEnvase(articulo.getTipoEnvase().getIdTipoEnvase());
            response.setTipoEnvase(articulo.getTipoEnvase().getNombre());
        }
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
