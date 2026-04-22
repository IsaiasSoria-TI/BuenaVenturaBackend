package com.buenaventura.erp.recepciones.service;

import com.buenaventura.erp.compras.entity.Compra;
import com.buenaventura.erp.compras.repository.CompraRepository;
import com.buenaventura.erp.recepciones.dto.RecepcionDetalleResponse;
import com.buenaventura.erp.recepciones.dto.RecepcionRequest;
import com.buenaventura.erp.recepciones.dto.RecepcionResponse;
import com.buenaventura.erp.recepciones.entity.Recepcion;
import com.buenaventura.erp.recepciones.repository.RecepcionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecepcionServiceImpl implements RecepcionService {

    private static final String ESTADO_PENDIENTE = "Pendiente";
    private static final String ESTADO_COMPLETA_PARCIAL = "Completa parcial";
    private static final String ESTADO_COMPLETA = "Completa";

    private final RecepcionRepository recepcionRepository;
    private final CompraRepository compraRepository;

    public RecepcionServiceImpl(RecepcionRepository recepcionRepository,
                                CompraRepository compraRepository) {
        this.recepcionRepository = recepcionRepository;
        this.compraRepository = compraRepository;
    }

    @Override
    public List<RecepcionResponse> listar() {
        return recepcionRepository.findAllByOrderByFechaRecepcionDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public RecepcionResponse registrar(RecepcionRequest request) {
        Compra compra = compraRepository.findById(request.getIdCompras())
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        if (Boolean.FALSE.equals(compra.getFlgActivo())) {
            throw new RuntimeException("La compra está inactiva");
        }

        if (ESTADO_COMPLETA.equalsIgnoreCase(compra.getEstado())) {
            throw new RuntimeException("No se puede registrar recepción para una compra completa");
        }

        if (request.getRecibido() == null || request.getRecibido().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El peso recibido debe ser mayor a cero");
        }

        BigDecimal totalRecibidoActual = recepcionRepository.sumarRecibidoPorCompra(compra.getIdCompras());
        BigDecimal nuevoTotal = totalRecibidoActual.add(request.getRecibido());

        if (nuevoTotal.compareTo(compra.getPeso()) > 0) {
            throw new RuntimeException("Excede el peso comprado");
        }

        Recepcion recepcion = new Recepcion();
        recepcion.setCompra(compra);
        recepcion.setRecibido(request.getRecibido());
        recepcion.setFechaRecepcion(LocalDateTime.now());

        if (nuevoTotal.compareTo(compra.getPeso()) == 0) {
            recepcion.setEstado(ESTADO_COMPLETA);
        } else {
            recepcion.setEstado(ESTADO_COMPLETA_PARCIAL);
        }

        Recepcion guardada = recepcionRepository.save(recepcion);

        if (nuevoTotal.compareTo(compra.getPeso()) == 0) {
            compra.setEstado(ESTADO_COMPLETA);
            compra.setFechaActualizacion(LocalDateTime.now());
            compraRepository.save(compra);

            List<Recepcion> recepciones = recepcionRepository.obtenerPorCompra(compra.getIdCompras());
            for (Recepcion r : recepciones) {
                r.setEstado(ESTADO_COMPLETA);
            }
            recepcionRepository.saveAll(recepciones);

            guardada.setEstado(ESTADO_COMPLETA);
        } else {
            compra.setEstado(ESTADO_COMPLETA_PARCIAL);
            compra.setFechaActualizacion(LocalDateTime.now());
            compraRepository.save(compra);

            guardada.setEstado(ESTADO_COMPLETA_PARCIAL);
        }

        return toResponse(guardada);
    }

    @Override
    public List<RecepcionResponse> listarComprasPendientes() {
        return compraRepository.findByFlgActivoTrueOrderByFechaComprasDesc()
                .stream()
                .filter(c -> !ESTADO_COMPLETA.equalsIgnoreCase(c.getEstado()))
                .map(this::toCompraPendienteResponse)
                .toList();
    }

    @Override
    public RecepcionDetalleResponse verDetalleCompra(Integer idCompras) {
        Compra compra = compraRepository.findById(idCompras)
                .orElseThrow(() -> new RuntimeException("La compra no existe"));

        BigDecimal totalRecibido = recepcionRepository.sumarRecibidoPorCompra(idCompras);
        BigDecimal pesoPendiente = compra.getPeso().subtract(totalRecibido);

        RecepcionDetalleResponse response = new RecepcionDetalleResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setFechaCompras(compra.getFechaCompras());
        response.setPesoComprado(compra.getPeso());
        response.setEstado(compra.getEstado());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setRuc(compra.getProveedor().getRuc());
        response.setArticulo(compra.getArticulo().getDescripcion());
        response.setMedida(compra.getArticulo().getMedida());
        response.setZonaProduccion(compra.getZonaProduccion());
        response.setHectareas(compra.getHectareas());
        response.setCostoKilo(compra.getCostoKilo());
        response.setCostoTotal(compra.getCostoTotal());
        response.setTotalRecibido(totalRecibido);
        response.setPesoPendiente(pesoPendiente);

        return response;
    }

    private RecepcionResponse toResponse(Recepcion recepcion) {
        RecepcionResponse response = new RecepcionResponse();
        response.setIdRecepciones(recepcion.getIdRecepciones());
        response.setFechaRecepcion(recepcion.getFechaRecepcion());
        response.setEstado(recepcion.getEstado());
        response.setIdCompras(recepcion.getCompra().getIdCompras());
        response.setPesoComprado(recepcion.getCompra().getPeso());
        response.setRecibido(recepcion.getRecibido());
        response.setEstadoCompra(recepcion.getCompra().getEstado());
        response.setRazonSocial(recepcion.getCompra().getProveedor().getRazonSocial());
        response.setRuc(recepcion.getCompra().getProveedor().getRuc());
        response.setArticulo(recepcion.getCompra().getArticulo().getDescripcion());
        response.setMedida(recepcion.getCompra().getArticulo().getMedida());
        return response;
    }

    private RecepcionResponse toCompraPendienteResponse(Compra compra) {
        RecepcionResponse response = new RecepcionResponse();
        response.setIdCompras(compra.getIdCompras());
        response.setPesoComprado(compra.getPeso());
        response.setEstadoCompra(compra.getEstado());
        response.setRazonSocial(compra.getProveedor().getRazonSocial());
        response.setRuc(compra.getProveedor().getRuc());
        response.setArticulo(compra.getArticulo().getDescripcion());
        response.setMedida(compra.getArticulo().getMedida());
        return response;
    }
}