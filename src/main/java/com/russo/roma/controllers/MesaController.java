package com.russo.roma.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.russo.roma.model.negocio.Mesa;
import com.russo.roma.services.interfaces.IMesaService;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    IMesaService mesaService;

    public MesaController(IMesaService mesaService){
        this.mesaService = mesaService;
    }

    @GetMapping
    ResponseEntity<List<Mesa>> listar(){
        return ResponseEntity.ok(mesaService.listarMesas());
    }

    @GetMapping("/{num}")
    ResponseEntity<Mesa> buscarPorNumero(@PathVariable Integer num){
        return ResponseEntity.ok(mesaService.buscarPorNumero(num));
    }

    @PostMapping
    ResponseEntity<Void> altaMesa(@RequestBody Mesa mesa, UriComponentsBuilder ucb){
        mesaService.altaMesa(mesa);
        URI uri = ucb.path("/api/mesas/{num}").buildAndExpand(mesa.getNumero()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{num}")
    ResponseEntity<Void> modificarMesa(@PathVariable Integer num, @RequestBody Mesa mesa ){
        mesaService.modificarMesa(num, mesa);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{num}")
    ResponseEntity<Void> borrarMesa(@PathVariable Integer num){
        mesaService.borrarMesa(num);
        return ResponseEntity.noContent().build();
    }
}
