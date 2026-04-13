package com.buenaventura.erp.compras.service;

import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.compras.dto.CompraRequest;
import com.buenaventura.erp.compras.dto.CompraResponse;
import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.impuesto.entity.Impuesto;
import com.buenaventura.erp.impuesto.repository.ImpuestoRepository;
import com.buenaventura.erp.pago.entity.Pago;
import com.buenaventura.erp.pago.repository.PagoRepository;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ImpuestoRepository impuestoRepository;
    private final PagoRepository pagoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ArticuloRepository articuloRepository;

    public CompraServiceImpl(CompraRepository compraRepository,
                             ImpuestoRepository impuestoRepository,
                             PagoRepository pagoRepository,
                             ProveedorRepository proveedorRepository,
                             ArticuloRepository articuloRepository) {
        this.compraRepository = compraRepository;
        this.impuestoRepository = impuestoRepository;
        this.pagoRepository = pagoRepository;
        this.proveedorRepository = proveedorRepository;
        this.articuloRepository = articuloRepository;
    }

    @Override
    public List<CompraResponse> listar() {
        return compraRepository.findByFlgActivoTrueOrderByFechaComprasDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CompraResponse registrar(CompraRequest request) {
        Impuesto impuesto = impuestoRepository.findById(request.getIdImpuesto())
                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

        Pago pago = pagoRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Condición de pago no encontrada"));

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Articulo articulo = articuloRepository.findById(request.getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        BigDecimal costoTotal = calcularCostoTotal(request.getPeso(), request.getCostoKilo());
        BigDecimal importeImpuesto = calcularImporteImpuesto(costoTotal, impuesto.getValor());

        Compra compra = new Compra();
        compra.setImpuesto(impuesto);
        compra.setPago(pago);
        compra.setProveedor(proveedor);
        compra.setArticulo(articulo);
        compra.setFechaCompras(request.getFechaCompras());
        compra.setZonaProduccion(request.getZonaProduccion());
        compra.setHectareas(request.getHectareas());
        compra.setPeso(request.getPeso());
        compra.setCostoKilo(request.getCostoKilo());
        compra.setCostoTotal(costoTotal);
        compra.setImporteImpuesto(importeImpuesto);
        compra.setEstado("Pendiente");
        compra.setFlgActivo(true);

        Compra guardada = compraRepository.save(compra);
        return toResponse(guardada);
    }

    @Override
    public CompraResponse actualizar(Integer id, CompraRequest request) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if (Boolean.FALSE.equals(compra.getFlgActivo())) {
            throw new RuntimeException("La compra está inactiva");
        }

        if ("Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede editar una compra completa");
        }

        Impuesto impuesto = impuestoRepository.findById(request.getIdImpuesto())
                .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

        Pago pago = pagoRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Condición de pago no encontrada"));

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Articulo articulo = articuloRepository.findById(request.getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        BigDecimal costoTotal = calcularCostoTotal(request.getPeso(), request.getCostoKilo());
        BigDecimal importeImpuesto = calcularImporteImpuesto(costoTotal, impuesto.getValor());

        compra.setImpuesto(impuesto);
        compra.setPago(pago);
        compra.setProveedor(proveedor);
        compra.setArticulo(articulo);
        compra.setFechaCompras(request.getFechaCompras());
        compra.setZonaProduccion(request.getZonaProduccion());
        compra.setHectareas(request.getHectareas());
        compra.setPeso(request.getPeso());
        compra.setCostoKilo(request.getCostoKilo());
        compra.setCostoTotal(costoTotal);
        compra.setImporteImpuesto(importeImpuesto);
        compra.setFechaActualizacion(LocalDateTime.now());
        compra.setEstado("Pendiente");

        Compra actualizada = compraRepository.save(compra);
        return toResponse(actualizada);
    }

    @Override
    public void eliminarLogico(Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if ("Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede eliminar una compra completa");
        }

        compra.setFlgActivo(false);
        compra.setFechaActualizacion(LocalDateTime.now());
        compraRepository.save(compra);
    }

    private BigDecimal calcularCostoTotal(BigDecimal peso, BigDecimal costoKilo) {
        return peso.multiply(costoKilo).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularImporteImpuesto(BigDecimal costoTotal, Integer valorImpuesto) {
        return costoTotal
                .multiply(BigDecimal.valueOf(valorImpuesto))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private CompraResponse toResponse(Compra compra) {
        CompraResponse response = new CompraResponse();
        response.setIdCompras(compra.getIdCompras());

        response.setIdImpuesto(compra.getImpuesto().getIdImpuesto());
        response.setTipoImpuesto(compra.getImpuesto().getTipoImpuesto());
        response.setValorImpuesto(compra.getImpuesto().getValor());

        response.setIdPago(compra.getPago().getIdPago());
        response.setPago(compra.getPago().getPago());

        response.setIdProveedor(compra.getProveedor().getIdProveedor());
        response.setRuc(compra.getProveedor().getRuc());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setDireccion(compra.getProveedor().getDireccion());

        response.setIdArticulo(compra.getArticulo().getIdArticulo());
        response.setDescripcionArticulo(compra.getArticulo().getDescripcion());
        response.setMedida(compra.getArticulo().getMedida());

        response.setFechaCompras(compra.getFechaCompras());
        response.setZonaProduccion(compra.getZonaProduccion());
        response.setHectareas(compra.getHectareas());
        response.setPeso(compra.getPeso());
        response.setCostoKilo(compra.getCostoKilo());
        response.setCostoTotal(compra.getCostoTotal());
        response.setImporteImpuesto(compra.getImporteImpuesto());
        response.setEstado(compra.getEstado());

        return response;
    }
}