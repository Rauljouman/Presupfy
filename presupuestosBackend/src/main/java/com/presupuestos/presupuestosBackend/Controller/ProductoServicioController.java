
package com.presupuestos.presupuestosBackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import com.presupuestos.presupuestosBackend.dto.ProductoServicioRequestDTO;
import com.presupuestos.presupuestosBackend.dto.ProductoServicioResponseDTO;
import com.presupuestos.presupuestosBackend.service.ProductoServicioService;
import com.presupuestos.presupuestosBackend.service.UsuarioAutenticadoService;


import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos-servicios")
public class ProductoServicioController {

    private final ProductoServicioService productoServicioService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public ProductoServicioController(ProductoServicioService productoServicioService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.productoServicioService = productoServicioService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping
    public List<ProductoServicioResponseDTO> listarProductoServicio(@RequestParam Long empresaId){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return productoServicioService.listarProductoServicio(empresaId, usuarioId);
    }

    @GetMapping("/{id}")
    public ProductoServicioResponseDTO buscarProductoServicioId(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return productoServicioService.buscarProductoServicioId(id, usuarioId);
    }

    @PostMapping
    public ProductoServicioResponseDTO crearProductoServicio(@Valid @RequestBody ProductoServicioRequestDTO dto){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return productoServicioService.crearProductoServicio(dto, usuarioId);
    }

    @PutMapping("/{id}")
    public ProductoServicioResponseDTO actualizarProductoServicio(@PathVariable Long id, @Valid @RequestBody ProductoServicioRequestDTO dto){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        return productoServicioService.actualizarProductoServicio(id, dto, usuarioId);
    }

    @DeleteMapping("/{id}")
    public void desactivarProductoServicio(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        productoServicioService.desactivarProductoServicio(id, usuarioId);
    }

    @PutMapping("/{id}/reactivar")
    public void reactivarProductoServicio(@PathVariable Long id){
        Long usuarioId = usuarioAutenticadoService.obtenerUsuarioAutenticado().getId();
        productoServicioService.reactivarProductoServicio(id, usuarioId);
    }
}
