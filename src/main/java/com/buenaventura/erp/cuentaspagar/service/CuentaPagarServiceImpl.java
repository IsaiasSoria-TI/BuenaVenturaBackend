package com.buenaventura.erp.cuentaspagar.service;

import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarDisponibleResponse;
import com.buenaventura.erp.cuentaspagar.dto.CuentaPagarRequest;
import com.buenaventura.erp.cuentaspagar.entity.CuentaPagar;
import com.buenaventura.erp.cuentaspagar.repository.CuentaPagarRepository;
import com.buenaventura.erp.recepciones.entity.Recepcion;
import com.buenaventura.erp.recepciones.repository.RecepcionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CuentaPagarServiceImpl implements CuentaPagarService {

    private final CuentaPagarRepository cuentaPagarRepository;
    private final RecepcionRepository recepcionRepository;

    public CuentaPagarServiceImpl(CuentaPagarRepository cuentaPagarRepository,
                                  RecepcionRepository recepcionRepository) {
        this.cuentaPagarRepository = cuentaPagarRepository;
        this.recepcionRepository = recepcionRepository;
    }

    @Override
    public List<CuentaPagar> listar() {
        return cuentaPagarRepository.findByFlgActivoTrueOrderByFechaCreacionDesc();
    }

    @Override
    public CuentaPagar obtenerPorId(Integer id) {
        return cuentaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));
    }

    @Override
    public List<CuentaPagarDisponibleResponse> listarDisponibles() {
        List<Recepcion> recepcionesCompletas =
                recepcionRepository.findByEstadoAndCompraFlgActivoTrueOrderByFechaRecepcionDesc("COMPLETO");

        List<CuentaPagarDisponibleResponse> response = new ArrayList<>();

        for (Recepcion recepcion : recepcionesCompletas) {
            Integer idRecepciones = recepcion.getIdRecepciones();

            if (cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(idRecepciones)) {
                continue;
            }

            CuentaPagarDisponibleResponse item = new CuentaPagarDisponibleResponse();
            item.setIdCompras(recepcion.getCompra().getIdCompras());
            item.setIdRecepciones(recepcion.getIdRecepciones());

            item.setProveedor(null);
            item.setArticulo(null);

            item.setRecibido(recepcion.getRecibido());
            item.setFechaCompra(recepcion.getCompra().getFechaCompras());
            item.setFechaRecepcion(recepcion.getFechaRecepcion());
            item.setEstadoCompra(recepcion.getCompra().getEstado());
            item.setEstadoRecepcion(recepcion.getEstado());

            response.add(item);
        }

        return response;
    }

    @Override
    public List<CuentaPagar> registrar(CuentaPagarRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debes enviar al menos un detalle");
        }

        if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
            if (request.getNumeroFactura() == null || request.getNumeroFactura().isBlank()) {
                throw new RuntimeException("Debes enviar el número de factura cuando el tipo es UNICA");
            }
        }

        List<CuentaPagar> cuentasRegistradas = new ArrayList<>();

        for (CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle : request.getDetalles()) {

            if (cuentaPagarRepository.existsByIdRecepcionesAndFlgActivoTrue(detalle.getIdRecepciones())) {
                throw new RuntimeException(
                        "La recepción con ID " + detalle.getIdRecepciones() + " ya fue registrada en cuentas por pagar"
                );
            }

            String numeroFacturaFinal;

            if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
                numeroFacturaFinal = request.getNumeroFactura();
            } else {
                if (detalle.getNumeroFactura() == null || detalle.getNumeroFactura().isBlank()) {
                    throw new RuntimeException(
                            "Debes enviar el número de factura en cada detalle cuando el tipo es MULTIPLE"
                    );
                }
                numeroFacturaFinal = detalle.getNumeroFactura();
            }

            CuentaPagar cuentaPagar = new CuentaPagar();
            cuentaPagar.setIdCompras(detalle.getIdCompras());
            cuentaPagar.setIdRecepciones(detalle.getIdRecepciones());
            cuentaPagar.setNumeroFactura(numeroFacturaFinal);
            cuentaPagar.setMoneda(request.getMoneda());
            cuentaPagar.setCodigoDetRet(request.getCodigoDetRet());
            cuentaPagar.setEstado("PAGADO");
            cuentaPagar.setFlgActivo(true);

            cuentasRegistradas.add(cuentaPagarRepository.save(cuentaPagar));
        }

        return cuentasRegistradas;
    }

    @Override
    public CuentaPagar actualizar(Integer id, CuentaPagarRequest request) {
        CuentaPagar existente = cuentaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("Debes enviar al menos un detalle para actualizar");
        }

        CuentaPagarRequest.CuentaPagarRegistroDetalleRequest detalle = request.getDetalles().get(0);

        existente.setIdCompras(detalle.getIdCompras());
        existente.setIdRecepciones(detalle.getIdRecepciones());

        if ("UNICA".equalsIgnoreCase(request.getTipoFactura())) {
            if (request.getNumeroFactura() == null || request.getNumeroFactura().isBlank()) {
                throw new RuntimeException("Debes enviar el número de factura cuando el tipo es UNICA");
            }
            existente.setNumeroFactura(request.getNumeroFactura());
        } else {
            if (detalle.getNumeroFactura() == null || detalle.getNumeroFactura().isBlank()) {
                throw new RuntimeException("Debes enviar el número de factura en el detalle cuando el tipo es MULTIPLE");
            }
            existente.setNumeroFactura(detalle.getNumeroFactura());
        }

        existente.setMoneda(request.getMoneda());
        existente.setCodigoDetRet(request.getCodigoDetRet());
        existente.setFechaActualizacion(LocalDateTime.now());

        return cuentaPagarRepository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        CuentaPagar existente = cuentaPagarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta por pagar no encontrada"));

        existente.setFlgActivo(false);
        existente.setFechaActualizacion(LocalDateTime.now());

        cuentaPagarRepository.save(existente);
    }
}