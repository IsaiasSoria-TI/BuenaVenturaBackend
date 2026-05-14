package com.buenaventura.erp.recepciones.service;

import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.entity.CompraDetalle;
import com.buenaventura.erp.compras.repository.CompraDetalleRepository;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.recepciones.dto.RecepcionDetalleItemResponse;
import com.buenaventura.erp.recepciones.dto.RecepcionDetalleRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionDetalleResponse;
import com.buenaventura.erp.recepciones.dto.RecepcionRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionResponse;
import com.buenaventura.erp.recepciones.entity.Recepcion;
import com.buenaventura.erp.recepciones.entity.RecepcionDetalle;
import com.buenaventura.erp.recepciones.repository.RecepcionDetalleRepository;
import com.buenaventura.erp.recepciones.repository.RecepcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecepcionServiceImpl implements RecepcionService {

    private static final String ESTADO_PENDIENTE = "Pendiente";
    private static final String ESTADO_COMPLETA_PARCIAL = "Completa parcial";
    private static final String ESTADO_COMPLETA = "Completa";
    private static final BigDecimal ZERO_2 = BigDecimal.ZERO.setScale(2);

    private final RecepcionRepository recepcionRepository;
    private final RecepcionDetalleRepository recepcionDetalleRepository;
    private final CompraRepository compraRepository;
    private final CompraDetalleRepository compraDetalleRepository;

    public RecepcionServiceImpl(RecepcionRepository recepcionRepository,
                                RecepcionDetalleRepository recepcionDetalleRepository,
                                CompraRepository compraRepository,
                                CompraDetalleRepository compraDetalleRepository) {
        this.recepcionRepository = recepcionRepository;
        this.recepcionDetalleRepository = recepcionDetalleRepository;
        this.compraRepository = compraRepository;
        this.compraDetalleRepository = compraDetalleRepository;
    }

    @Override
    public List<RecepcionResponse> listar() {
        return recepcionRepository.findAllByOrderByFechaRecepcionDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RecepcionResponse registrar(RecepcionRequest request) {
        Compra compra = compraRepository.findById(request.getIdCompras())
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        if (Boolean.FALSE.equals(compra.getFlgActivo())) {
            throw new RuntimeException("La compra está inactiva");
        }

        if (ESTADO_COMPLETA.equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede registrar recepción para una compra completa");
        }

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debe agregar al menos un detalle de recepción");
        }

        if (request.getCantidadJabas() != null && request.getCantidadJabas().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La cantidad de jabas no puede ser negativa");
        }

        BigDecimal totalRecibidoRecepcion = BigDecimal.ZERO;

        for (RecepcionDetalleRequest detalleRequest : request.getDetalles()) {
            if (detalleRequest.getRecibido() == null ||
                    detalleRequest.getRecibido().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El peso recibido debe ser mayor a cero");
            }

            CompraDetalle compraDetalle = compraDetalleRepository.findById(detalleRequest.getIdCompraDetalle())
                    .orElseThrow(() -> new RuntimeException("Detalle de compra no encontrado"));

            if (!compraDetalle.getCompra().getIdCompras().equals(compra.getIdCompras())) {
                throw new RuntimeException("El detalle no pertenece a la compra seleccionada");
            }

            BigDecimal recibidoActual = recepcionDetalleRepository
                    .sumarRecibidoPorCompraDetalle(compraDetalle.getIdCompraDetalle());

            BigDecimal nuevoTotalDetalle = recibidoActual.add(detalleRequest.getRecibido());

            if (nuevoTotalDetalle.compareTo(compraDetalle.getPeso()) > 0) {
                throw new RuntimeException(
                        "El recibido excede el peso comprado del artículo: "
                                + compraDetalle.getArticulo().getDescripcion()
                );
            }

            totalRecibidoRecepcion = totalRecibidoRecepcion.add(detalleRequest.getRecibido());
        }

        Recepcion recepcion = new Recepcion();
        recepcion.setCompra(compra);
        recepcion.setRecibido(totalRecibidoRecepcion);
        recepcion.setFechaRecepcion(LocalDateTime.now());
        recepcion.setGuiaRemision(normalizarGuiaRemision(request.getGuiaRemision()));
        recepcion.setCantidadJabas(normalizarCantidadJabas(request.getCantidadJabas()));
        recepcion.setEstado(ESTADO_COMPLETA_PARCIAL);

        Recepcion guardada = recepcionRepository.save(recepcion);

        for (RecepcionDetalleRequest detalleRequest : request.getDetalles()) {
            CompraDetalle compraDetalle = compraDetalleRepository.findById(detalleRequest.getIdCompraDetalle())
                    .orElseThrow(() -> new RuntimeException("Detalle de compra no encontrado"));

            BigDecimal recibidoActual = recepcionDetalleRepository
                    .sumarRecibidoPorCompraDetalle(compraDetalle.getIdCompraDetalle());

            BigDecimal nuevoTotalDetalle = recibidoActual.add(detalleRequest.getRecibido());

            RecepcionDetalle detalle = new RecepcionDetalle();
            detalle.setRecepcion(guardada);
            detalle.setCompraDetalle(compraDetalle);
            detalle.setRecibido(detalleRequest.getRecibido());
            detalle.setEstado(
                    nuevoTotalDetalle.compareTo(compraDetalle.getPeso()) == 0
                            ? ESTADO_COMPLETA
                            : ESTADO_COMPLETA_PARCIAL
            );
            detalle.setFlgActivo(true);

            recepcionDetalleRepository.save(detalle);
        }

        String nuevoEstadoCompra = calcularEstadoCompra(compra.getIdCompras());

        compra.setEstado(nuevoEstadoCompra);
        compra.setFechaActualizacion(LocalDateTime.now());
        compraRepository.save(compra);

        guardada.setEstado(nuevoEstadoCompra);
        recepcionRepository.save(guardada);

        if (ESTADO_COMPLETA.equalsIgnoreCase(nuevoEstadoCompra)) {
            List<Recepcion> recepcionesCompra =
                    recepcionRepository.findByCompra_IdComprasOrderByFechaRecepcionAsc(compra.getIdCompras());

            for (Recepcion item : recepcionesCompra) {
                item.setEstado(ESTADO_COMPLETA);
            }

            recepcionRepository.saveAll(recepcionesCompra);
        }

        return toResponse(guardada);
    }

    @Override
    public List<RecepcionResponse> listarComprasPendientes() {
        return compraRepository.findByFlgActivoTrueOrderByFechaComprasDesc()
                .stream()
                .filter(compra -> !ESTADO_COMPLETA.equalsIgnoreCase(compra.getEstado()))
                .map(this::toCompraPendienteResponse)
                .toList();
    }

    @Override
    public RecepcionDetalleResponse verDetalleCompra(Integer idCompras) {
        Compra compra = compraRepository.findById(idCompras)
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        RecepcionDetalleResponse response = new RecepcionDetalleResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setFechaCompras(compra.getFechaCompras());
        response.setEstado(compra.getEstado());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setRuc(compra.getProveedor().getRuc());
        response.setZonaProduccion(compra.getZonaProduccion());
        response.setNumeroLote(compra.getNumeroLote());
        response.setCostoTotal(compra.getCostoTotal());

        List<RecepcionDetalleItemResponse> detalles = obtenerDetallesCompra(compra.getIdCompras());

        BigDecimal pesoComprado = detalles.stream()
                .map(RecepcionDetalleItemResponse::getPesoComprado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRecibido = detalles.stream()
                .map(RecepcionDetalleItemResponse::getTotalRecibido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setPesoComprado(pesoComprado);
        response.setTotalRecibido(totalRecibido);
        response.setPesoPendiente(pesoComprado.subtract(totalRecibido));
        response.setDetalles(detalles);

        return response;
    }

    private String calcularEstadoCompra(Integer idCompras) {
        List<RecepcionDetalleItemResponse> detalles = obtenerDetallesCompra(idCompras);

        if (detalles.isEmpty()) {
            return ESTADO_PENDIENTE;
        }

        boolean tieneRecibido = false;
        boolean todoCompleto = true;

        for (RecepcionDetalleItemResponse detalle : detalles) {
            if (detalle.getTotalRecibido().compareTo(BigDecimal.ZERO) > 0) {
                tieneRecibido = true;
            }

            if (detalle.getTotalRecibido().compareTo(detalle.getPesoComprado()) < 0) {
                todoCompleto = false;
            }
        }

        if (todoCompleto) {
            return ESTADO_COMPLETA;
        }

        if (tieneRecibido) {
            return ESTADO_COMPLETA_PARCIAL;
        }

        return ESTADO_PENDIENTE;
    }

    private List<RecepcionDetalleItemResponse> obtenerDetallesCompra(Integer idCompras) {
        return compraDetalleRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(idCompras)
                .stream()
                .map(this::toDetalleCompraResponse)
                .toList();
    }

    private RecepcionDetalleItemResponse toDetalleCompraResponse(CompraDetalle compraDetalle) {
        BigDecimal totalRecibido = recepcionDetalleRepository
                .sumarRecibidoPorCompraDetalle(compraDetalle.getIdCompraDetalle());

        BigDecimal pesoPendiente = compraDetalle.getPeso().subtract(totalRecibido);

        RecepcionDetalleItemResponse response = new RecepcionDetalleItemResponse();
        response.setIdCompraDetalle(compraDetalle.getIdCompraDetalle());
        response.setIdArticulo(compraDetalle.getArticulo().getIdArticulo());
        response.setArticulo(compraDetalle.getArticulo().getDescripcion());
        response.setMedida(compraDetalle.getArticulo().getMedida());
        response.setPesoComprado(compraDetalle.getPeso());
        response.setTotalRecibido(totalRecibido);
        response.setPesoPendiente(pesoPendiente);
        response.setCostoKilo(compraDetalle.getCostoKilo());
        response.setCostoTotal(compraDetalle.getCostoTotal());
        response.setEstado(
                pesoPendiente.compareTo(BigDecimal.ZERO) == 0
                        ? ESTADO_COMPLETA
                        : ESTADO_COMPLETA_PARCIAL
        );
        return response;
    }

    private RecepcionResponse toResponse(Recepcion recepcion) {
        List<RecepcionDetalleItemResponse> detalles = recepcionDetalleRepository
                .findByRecepcion_IdRecepcionesAndFlgActivoTrueOrderByIdRecepcionDetalleAsc(
                        recepcion.getIdRecepciones()
                )
                .stream()
                .map(detalle -> {
                    RecepcionDetalleItemResponse response = new RecepcionDetalleItemResponse();
                    response.setIdRecepcionDetalle(detalle.getIdRecepcionDetalle());
                    response.setIdCompraDetalle(detalle.getCompraDetalle().getIdCompraDetalle());
                    response.setIdArticulo(detalle.getCompraDetalle().getArticulo().getIdArticulo());
                    response.setArticulo(detalle.getCompraDetalle().getArticulo().getDescripcion());
                    response.setMedida(detalle.getCompraDetalle().getArticulo().getMedida());
                    response.setPesoComprado(detalle.getCompraDetalle().getPeso());
                    response.setRecibido(detalle.getRecibido());
                    response.setCostoKilo(detalle.getCompraDetalle().getCostoKilo());
                    response.setCostoTotal(detalle.getCompraDetalle().getCostoTotal());
                    response.setEstado(detalle.getEstado());
                    return response;
                })
                .toList();

        RecepcionResponse response = new RecepcionResponse();
        response.setIdRecepciones(recepcion.getIdRecepciones());
        response.setFechaRecepcion(recepcion.getFechaRecepcion());
        response.setGuiaRemision(recepcion.getGuiaRemision());
        response.setCantidadJabas(recepcion.getCantidadJabas());
        response.setEstado(recepcion.getEstado());
        response.setIdCompras(recepcion.getCompra().getIdCompras());
        response.setPesoComprado(recepcion.getCompra().getPeso());
        response.setRecibido(recepcion.getRecibido());
        response.setEstadoCompra(recepcion.getCompra().getEstado());
        response.setRazonSocial(recepcion.getCompra().getProveedor().getRazonSocial());
        response.setRuc(recepcion.getCompra().getProveedor().getRuc());
        response.setDetalles(detalles);

        if (!detalles.isEmpty()) {
            response.setArticulo(
                    detalles.stream()
                            .map(RecepcionDetalleItemResponse::getArticulo)
                            .distinct()
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("-")
            );
            response.setMedida(detalles.get(0).getMedida());
        }

        return response;
    }

    private String normalizarGuiaRemision(String guiaRemision) {
        if (guiaRemision == null) {
            return null;
        }

        String normalizado = guiaRemision.trim();
        return normalizado.isBlank() ? null : normalizado;
    }

    private BigDecimal normalizarCantidadJabas(BigDecimal cantidadJabas) {
        return cantidadJabas == null ? ZERO_2 : cantidadJabas.setScale(2, RoundingMode.HALF_UP);
    }

    private RecepcionResponse toCompraPendienteResponse(Compra compra) {
        List<RecepcionDetalleItemResponse> detalles = obtenerDetallesCompra(compra.getIdCompras());

        BigDecimal pesoComprado = detalles.stream()
                .map(RecepcionDetalleItemResponse::getPesoComprado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRecibido = detalles.stream()
                .map(RecepcionDetalleItemResponse::getTotalRecibido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RecepcionResponse response = new RecepcionResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setPesoComprado(pesoComprado);
        response.setRecibido(totalRecibido);
        response.setEstadoCompra(compra.getEstado());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setRuc(compra.getProveedor().getRuc());
        response.setDetalles(detalles);

        if (!detalles.isEmpty()) {
            response.setArticulo(
                    detalles.stream()
                            .map(RecepcionDetalleItemResponse::getArticulo)
                            .distinct()
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("-")
            );
            response.setMedida(detalles.get(0).getMedida());
        }

        return response;
    }
}
