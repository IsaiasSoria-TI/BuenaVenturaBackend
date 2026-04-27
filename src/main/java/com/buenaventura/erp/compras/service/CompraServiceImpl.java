package com.buenaventura.erp.compras.service;

import com.buenaventura.erp.articulo.entity.Articulo;
import com.buenaventura.erp.articulo.repository.ArticuloRepository;
import com.buenaventura.erp.compras.dto.CompraDetalleRequest;
import com.buenaventura.erp.compras.dto.CompraDetalleResponse;
import com.buenaventura.erp.compras.dto.CompraImpuestoRequest;
import com.buenaventura.erp.compras.dto.CompraImpuestoResponse;
import com.buenaventura.erp.compras.dto.CompraRequest;
import com.buenaventura.erp.compras.dto.CompraResponse;
import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.entity.CompraDetalle;
import com.buenaventura.erp.compras.entity.CompraImpuesto;
import com.buenaventura.erp.compras.repository.CompraDetalleRepository;
import com.buenaventura.erp.compras.repository.CompraImpuestoRepository;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.impuesto.entity.Impuesto;
import com.buenaventura.erp.impuesto.repository.ImpuestoRepository;
import com.buenaventura.erp.pago.entity.Pago;
import com.buenaventura.erp.pago.repository.PagoRepository;
import com.buenaventura.erp.proveedores.entity.Proveedor;
import com.buenaventura.erp.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraServiceImpl implements CompraService {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final BigDecimal PORCENTAJE_IGV_DEFAULT = BigDecimal.valueOf(18).setScale(2, RoundingMode.HALF_UP);
    private static final BigDecimal ZERO_2 = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;
    private final CompraImpuestoRepository compraImpuestoRepository;
    private final ImpuestoRepository impuestoRepository;
    private final PagoRepository pagoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ArticuloRepository articuloRepository;

    public CompraServiceImpl(
            CompraRepository compraRepository,
            CompraDetalleRepository compraDetalleRepository,
            CompraImpuestoRepository compraImpuestoRepository,
            ImpuestoRepository impuestoRepository,
            PagoRepository pagoRepository,
            ProveedorRepository proveedorRepository,
            ArticuloRepository articuloRepository
    ) {
        this.compraRepository = compraRepository;
        this.compraDetalleRepository = compraDetalleRepository;
        this.compraImpuestoRepository = compraImpuestoRepository;
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
    @Transactional
    public CompraResponse registrar(CompraRequest request) {
        validarRequest(request);

        Pago pago = pagoRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Condición de pago no encontrada"));

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Articulo primerArticulo = articuloRepository.findById(request.getDetalles().get(0).getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        Impuesto primerImpuesto = resolverImpuestoCabecera(request);

        Compra compra = new Compra();
        compra.setPago(pago);
        compra.setProveedor(proveedor);
        compra.setArticulo(primerArticulo);
        compra.setImpuesto(primerImpuesto);
        compra.setFechaCompras(request.getFechaCompras());
        compra.setZonaProduccion(request.getZonaProduccion());
        compra.setHectareas(request.getHectareas());
        compra.setEstado("Pendiente");
        compra.setFlgActivo(true);

        compra.setPeso(BigDecimal.ZERO);
        compra.setCostoKilo(BigDecimal.ZERO);
        compra.setCostoTotal(BigDecimal.ZERO);
        compra.setImporteImpuesto(BigDecimal.ZERO);
        compra.setAplicaIgv(Boolean.TRUE.equals(request.getAplicaIgv()));
        compra.setPorcentajeIgv(resolverPorcentajeIgv(request));
        compra.setImporteIgv(ZERO_2);

        Compra guardada = compraRepository.save(compra);

        TotalesCompra totales = guardarDetallesEImpuestos(guardada, request);

        guardada.setPeso(totales.pesoTotal());
        guardada.setCostoKilo(request.getDetalles().get(0).getCostoKilo());
        guardada.setCostoTotal(totales.totalGeneral());
        guardada.setImporteImpuesto(totales.totalImpuestos());
        guardada.setAplicaIgv(totales.aplicaIgv());
        guardada.setPorcentajeIgv(totales.porcentajeIgv());
        guardada.setImporteIgv(totales.importeIgv());

        Compra actualizada = compraRepository.save(guardada);

        return toResponse(actualizada);
    }

    @Override
    @Transactional
    public CompraResponse actualizar(Integer id, CompraRequest request) {
        validarRequest(request);

        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if (Boolean.FALSE.equals(compra.getFlgActivo())) {
            throw new RuntimeException("La compra está inactiva");
        }

        if ("Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede editar una compra completa");
        }

        Pago pago = pagoRepository.findById(request.getIdPago())
                .orElseThrow(() -> new RuntimeException("Condición de pago no encontrada"));

        Proveedor proveedor = proveedorRepository.findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        Articulo primerArticulo = articuloRepository.findById(request.getDetalles().get(0).getIdArticulo())
                .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

        Impuesto primerImpuesto = resolverImpuestoCabecera(request);

        inactivarDetalles(compra.getIdCompras());
        inactivarImpuestos(compra.getIdCompras());

        compra.setPago(pago);
        compra.setProveedor(proveedor);
        compra.setArticulo(primerArticulo);
        compra.setImpuesto(primerImpuesto);
        compra.setFechaCompras(request.getFechaCompras());
        compra.setZonaProduccion(request.getZonaProduccion());
        compra.setHectareas(request.getHectareas());
        compra.setFechaActualizacion(LocalDateTime.now());
        compra.setEstado("Pendiente");
        compra.setAplicaIgv(Boolean.TRUE.equals(request.getAplicaIgv()));
        compra.setPorcentajeIgv(resolverPorcentajeIgv(request));
        compra.setImporteIgv(ZERO_2);

        Compra guardada = compraRepository.save(compra);

        TotalesCompra totales = guardarDetallesEImpuestos(guardada, request);

        guardada.setPeso(totales.pesoTotal());
        guardada.setCostoKilo(request.getDetalles().get(0).getCostoKilo());
        guardada.setCostoTotal(totales.totalGeneral());
        guardada.setImporteImpuesto(totales.totalImpuestos());
        guardada.setAplicaIgv(totales.aplicaIgv());
        guardada.setPorcentajeIgv(totales.porcentajeIgv());
        guardada.setImporteIgv(totales.importeIgv());

        Compra actualizada = compraRepository.save(guardada);

        return toResponse(actualizada);
    }

    @Override
    @Transactional
    public void eliminarLogico(Integer id) {
        Compra compra = compraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        if ("Completo".equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede eliminar una compra completa");
        }

        compra.setFlgActivo(false);
        compra.setFechaActualizacion(LocalDateTime.now());
        compraRepository.save(compra);

        inactivarDetalles(id);
        inactivarImpuestos(id);
    }

    private void validarRequest(CompraRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debe agregar al menos un artículo");
        }

        if (request.getPorcentajeIgv() != null && request.getPorcentajeIgv().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("El porcentaje de IGV no puede ser negativo");
        }
    }

    private TotalesCompra guardarDetallesEImpuestos(Compra compra, CompraRequest request) {
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal pesoTotal = BigDecimal.ZERO;

        for (CompraDetalleRequest detalleRequest : request.getDetalles()) {
            Articulo articulo = articuloRepository.findById(detalleRequest.getIdArticulo())
                    .orElseThrow(() -> new RuntimeException("Artículo no encontrado"));

            BigDecimal costoTotalDetalle = detalleRequest.getPeso()
                    .multiply(detalleRequest.getCostoKilo())
                    .setScale(2, RoundingMode.HALF_UP);

            CompraDetalle detalle = new CompraDetalle();
            detalle.setCompra(compra);
            detalle.setArticulo(articulo);
            detalle.setPeso(detalleRequest.getPeso());
            detalle.setCostoKilo(detalleRequest.getCostoKilo());
            detalle.setCostoTotal(costoTotalDetalle);
            detalle.setFlgActivo(true);

            compraDetalleRepository.save(detalle);

            subtotal = subtotal.add(costoTotalDetalle);
            pesoTotal = pesoTotal.add(detalleRequest.getPeso());
        }

        BigDecimal totalImpuestos = BigDecimal.ZERO;

        for (CompraImpuestoRequest impuestoRequest : obtenerImpuestosRequest(request)) {
            if (impuestoRequest == null || impuestoRequest.getIdImpuesto() == null) {
                continue;
            }

            Impuesto impuesto = impuestoRepository.findById(impuestoRequest.getIdImpuesto())
                    .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

            if (esIgv(impuesto)) {
                continue;
            }

            BigDecimal porcentaje = BigDecimal.valueOf(impuesto.getValor()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal importe = subtotal
                    .multiply(porcentaje)
                    .divide(CIEN, 2, RoundingMode.HALF_UP);

            CompraImpuesto compraImpuesto = new CompraImpuesto();
            compraImpuesto.setCompra(compra);
            compraImpuesto.setImpuesto(impuesto);
            compraImpuesto.setPorcentaje(porcentaje);
            compraImpuesto.setImporte(importe);
            compraImpuesto.setFlgActivo(true);

            compraImpuestoRepository.save(compraImpuesto);

            totalImpuestos = totalImpuestos.add(importe);
        }

        boolean aplicaIgv = Boolean.TRUE.equals(request.getAplicaIgv());
        BigDecimal porcentajeIgv = resolverPorcentajeIgv(request);
        BigDecimal importeIgv = aplicaIgv
                ? subtotal.multiply(porcentajeIgv).divide(CIEN, 2, RoundingMode.HALF_UP)
                : ZERO_2;

        return new TotalesCompra(
                pesoTotal.setScale(2, RoundingMode.HALF_UP),
                subtotal.setScale(2, RoundingMode.HALF_UP),
                totalImpuestos.setScale(2, RoundingMode.HALF_UP),
                aplicaIgv,
                porcentajeIgv,
                importeIgv,
                subtotal.add(totalImpuestos).add(importeIgv).setScale(2, RoundingMode.HALF_UP)
        );
    }

    private Impuesto resolverImpuestoCabecera(CompraRequest request) {
        for (CompraImpuestoRequest impuestoRequest : obtenerImpuestosRequest(request)) {
            if (impuestoRequest == null || impuestoRequest.getIdImpuesto() == null) {
                continue;
            }

            Impuesto impuesto = impuestoRepository.findById(impuestoRequest.getIdImpuesto())
                    .orElseThrow(() -> new RuntimeException("Impuesto no encontrado"));

            if (!esIgv(impuesto)) {
                return impuesto;
            }
        }

        return impuestoRepository.findAll()
                .stream()
                .filter(impuesto -> !esIgv(impuesto))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Debe existir al menos un impuesto normal configurado"));
    }

    private List<CompraImpuestoRequest> obtenerImpuestosRequest(CompraRequest request) {
        return request.getImpuestos() == null ? List.of() : request.getImpuestos();
    }

    private BigDecimal resolverPorcentajeIgv(CompraRequest request) {
        BigDecimal porcentajeIgv = request.getPorcentajeIgv() == null
                ? PORCENTAJE_IGV_DEFAULT
                : request.getPorcentajeIgv();

        return porcentajeIgv.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean esIgv(Impuesto impuesto) {
        if (impuesto == null || impuesto.getTipoImpuesto() == null) {
            return false;
        }

        String tipoNormalizado = impuesto.getTipoImpuesto().trim().replace(".", "").replace(" ", "");
        return "IGV".equalsIgnoreCase(tipoNormalizado);
    }

    private void inactivarDetalles(Integer idCompras) {
        List<CompraDetalle> detalles = compraDetalleRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(idCompras);

        detalles.forEach(detalle -> {
            detalle.setFlgActivo(false);
            detalle.setFechaActualizacion(LocalDateTime.now());
        });

        compraDetalleRepository.saveAll(detalles);
    }

    private void inactivarImpuestos(Integer idCompras) {
        List<CompraImpuesto> impuestos = compraImpuestoRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraImpuestoAsc(idCompras);

        impuestos.forEach(impuesto -> {
            impuesto.setFlgActivo(false);
            impuesto.setFechaActualizacion(LocalDateTime.now());
        });

        compraImpuestoRepository.saveAll(impuestos);
    }

    private CompraResponse toResponse(Compra compra) {
        CompraResponse response = new CompraResponse();

        response.setIdCompras(compra.getIdCompras());

        response.setIdPago(compra.getPago().getIdPago());
        response.setPago(compra.getPago().getPago());

        response.setIdProveedor(compra.getProveedor().getIdProveedor());
        response.setRuc(compra.getProveedor().getRuc());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setDireccion(compra.getProveedor().getDireccion());

        response.setFechaCompras(compra.getFechaCompras());
        response.setZonaProduccion(compra.getZonaProduccion());
        response.setHectareas(compra.getHectareas());
        response.setPeso(compra.getPeso());
        response.setCostoTotal(compra.getCostoTotal());
        response.setImporteImpuesto(compra.getImporteImpuesto());
        response.setAplicaIgv(Boolean.TRUE.equals(compra.getAplicaIgv()));
        response.setPorcentajeIgv(compra.getPorcentajeIgv() == null ? PORCENTAJE_IGV_DEFAULT : compra.getPorcentajeIgv());
        response.setImporteIgv(compra.getImporteIgv() == null ? ZERO_2 : compra.getImporteIgv());
        response.setTotalGeneral(compra.getCostoTotal());
        response.setEstado(compra.getEstado());

        response.setDetalles(toDetalleResponse(compra.getIdCompras()));
        response.setImpuestos(toImpuestoResponse(compra.getIdCompras()));

        return response;
    }

    private List<CompraDetalleResponse> toDetalleResponse(Integer idCompras) {
        return compraDetalleRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(idCompras)
                .stream()
                .map(detalle -> {
                    CompraDetalleResponse response = new CompraDetalleResponse();
                    response.setIdCompraDetalle(detalle.getIdCompraDetalle());
                    response.setIdArticulo(detalle.getArticulo().getIdArticulo());
                    response.setDescripcionArticulo(detalle.getArticulo().getDescripcion());
                    response.setMedida(detalle.getArticulo().getMedida());
                    response.setPeso(detalle.getPeso());
                    response.setCostoKilo(detalle.getCostoKilo());
                    response.setCostoTotal(detalle.getCostoTotal());
                    return response;
                })
                .toList();
    }

    private List<CompraImpuestoResponse> toImpuestoResponse(Integer idCompras) {
        return compraImpuestoRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraImpuestoAsc(idCompras)
                .stream()
                .filter(impuesto -> !esIgv(impuesto.getImpuesto()))
                .map(impuesto -> {
                    CompraImpuestoResponse response = new CompraImpuestoResponse();
                    response.setIdCompraImpuesto(impuesto.getIdCompraImpuesto());
                    response.setIdImpuesto(impuesto.getImpuesto().getIdImpuesto());
                    response.setTipoImpuesto(impuesto.getImpuesto().getTipoImpuesto());
                    response.setPorcentaje(impuesto.getPorcentaje());
                    response.setImporte(impuesto.getImporte());
                    return response;
                })
                .toList();
    }

    private record TotalesCompra(
            BigDecimal pesoTotal,
            BigDecimal subtotal,
            BigDecimal totalImpuestos,
            boolean aplicaIgv,
            BigDecimal porcentajeIgv,
            BigDecimal importeIgv,
            BigDecimal totalGeneral
    ) {
    }
}
