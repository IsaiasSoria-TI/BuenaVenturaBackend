package com.buenaventura.erp.recepciones.service;

import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.entity.CompraDetalle;
import com.buenaventura.erp.compras.repository.CompraDetalleRepository;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.moneda.entity.Moneda;
import com.buenaventura.erp.recepciones.dto.RecepcionDatosRequest;
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
import java.time.ZoneId;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecepcionServiceImpl implements RecepcionService {

    private static final String ESTADO_PENDIENTE = "Pendiente";
    private static final String ESTADO_COMPLETA_PARCIAL = "Completa parcial";
    private static final String ESTADO_COMPLETA = "Completa";
    private static final ZoneId ZONA_PERU = ZoneId.of("America/Lima");
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

        if (request.getCantidadEnvase() != null && request.getCantidadEnvase() < 0) {
            throw new RuntimeException("La cantidad de envase no puede ser negativa");
        }

        BigDecimal totalRecibidoRecepcion = BigDecimal.ZERO;
        Set<String> tiposEnvaseRecepcion = new LinkedHashSet<>();

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

            agregarTipoEnvase(tiposEnvaseRecepcion, compraDetalle);

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
        recepcion.setFechaRecepcion(LocalDateTime.now(ZONA_PERU));
        recepcion.setGuiaRemision(normalizarGuiaRemision(request.getGuiaRemision()));
        recepcion.setTipoEnvase(formatearTiposEnvase(tiposEnvaseRecepcion));
        recepcion.setCantidadEnvase(normalizarCantidadEnvase(request.getCantidadEnvase()));
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
    @Transactional
    public RecepcionResponse actualizarDatos(Integer id, RecepcionDatosRequest request) {
        Recepcion recepcion = recepcionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La recepciÃ³n no existe"));

        if (request.getCantidadEnvase() != null && request.getCantidadEnvase() < 0) {
            throw new RuntimeException("La cantidad de envase no puede ser negativa");
        }

        recepcion.setGuiaRemision(normalizarGuiaRemision(request.getGuiaRemision()));
        recepcion.setTipoEnvase(obtenerTipoEnvaseRecepcion(recepcion));
        recepcion.setCantidadEnvase(normalizarCantidadEnvase(request.getCantidadEnvase()));

        return toResponse(recepcionRepository.save(recepcion));
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
        response.setCostoTotal(calcularCostoTotalCompra(compra.getIdCompras()));
        completarMoneda(response, compra);

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
        if (compraDetalle.getArticulo().getCategoria() != null) {
            response.setIdCategoria(compraDetalle.getArticulo().getCategoria().getIdCategoria());
            response.setDescripcionCategoria(compraDetalle.getArticulo().getCategoria().getDescripcion());
        }
        response.setTipoEnvase(compraDetalle.getArticulo().getTipoEnvase());
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
                    if (detalle.getCompraDetalle().getArticulo().getCategoria() != null) {
                        response.setIdCategoria(detalle.getCompraDetalle().getArticulo().getCategoria().getIdCategoria());
                        response.setDescripcionCategoria(detalle.getCompraDetalle().getArticulo().getCategoria().getDescripcion());
                    }
                    response.setTipoEnvase(detalle.getCompraDetalle().getArticulo().getTipoEnvase());
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
        response.setTipoEnvase(recepcion.getTipoEnvase());
        response.setCantidadEnvase(recepcion.getCantidadEnvase());
        response.setEstado(recepcion.getEstado());
        response.setIdCompras(recepcion.getCompra().getIdCompras());
        response.setPesoComprado(recepcion.getCompra().getPeso());
        response.setRecibido(recepcion.getRecibido());
        response.setEstadoCompra(recepcion.getCompra().getEstado());
        response.setRazonSocial(recepcion.getCompra().getProveedor().getRazonSocial());
        response.setRuc(recepcion.getCompra().getProveedor().getRuc());
        response.setDetalles(detalles);
        response.setCostoTotal(calcularCostoTotalCompra(recepcion.getCompra().getIdCompras()));
        completarMoneda(response, recepcion.getCompra());

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

    private Integer normalizarCantidadEnvase(Integer cantidadEnvase) {
        return cantidadEnvase == null ? 0 : cantidadEnvase;
    }

    private void agregarTipoEnvase(Set<String> tiposEnvase, CompraDetalle compraDetalle) {
        if (compraDetalle == null || compraDetalle.getArticulo() == null) {
            return;
        }

        String tipoEnvase = compraDetalle.getArticulo().getTipoEnvase();
        if (tipoEnvase != null && !tipoEnvase.isBlank()) {
            tiposEnvase.add(tipoEnvase.trim());
        }
    }

    private String obtenerTipoEnvaseRecepcion(Recepcion recepcion) {
        Set<String> tiposEnvase = new LinkedHashSet<>();

        recepcionDetalleRepository
                .findByRecepcion_IdRecepcionesAndFlgActivoTrueOrderByIdRecepcionDetalleAsc(
                        recepcion.getIdRecepciones()
                )
                .forEach(detalle -> agregarTipoEnvase(tiposEnvase, detalle.getCompraDetalle()));

        return formatearTiposEnvase(tiposEnvase);
    }

    private String formatearTiposEnvase(Set<String> tiposEnvase) {
        if (tiposEnvase == null || tiposEnvase.isEmpty()) {
            return null;
        }

        return String.join(", ", tiposEnvase);
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
        response.setCostoTotal(calcularCostoTotalCompra(compra.getIdCompras()));
        completarMoneda(response, compra);

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

    private BigDecimal calcularCostoTotalCompra(Integer idCompras) {
        return compraDetalleRepository
                .findByCompra_IdComprasAndFlgActivoTrueOrderByIdCompraDetalleAsc(idCompras)
                .stream()
                .map(CompraDetalle::getCostoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void completarMoneda(RecepcionResponse response, Compra compra) {
        if (compra == null) {
            return;
        }

        Moneda moneda = compra.getMoneda();
        if (moneda != null) {
            response.setIdMoneda(moneda.getIdMoneda());
            response.setCodigoMoneda(moneda.getCodigo());
            response.setMoneda(moneda.getNombre());
            response.setSimboloMoneda(moneda.getSimbolo());
        }

        response.setTipoCambioAplicado(compra.getTipoCambioAplicado());
    }

    private void completarMoneda(RecepcionDetalleResponse response, Compra compra) {
        if (compra == null) {
            return;
        }

        Moneda moneda = compra.getMoneda();
        if (moneda != null) {
            response.setIdMoneda(moneda.getIdMoneda());
            response.setCodigoMoneda(moneda.getCodigo());
            response.setMoneda(moneda.getNombre());
            response.setSimboloMoneda(moneda.getSimbolo());
        }

        response.setTipoCambioAplicado(compra.getTipoCambioAplicado());
    }
}
