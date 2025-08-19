package com.russo.roma.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.russo.roma.model.negocio.ItemMenu;
import com.russo.roma.services.interfaces.IitemMenuService;

@RestController
@RequestMapping("api/items")
@PreAuthorize("hasRole('ADMIN')")
public class ItemMenuController {

    private IitemMenuService itemService;

    public ItemMenuController(IitemMenuService itemService){
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemMenu> buscarPorId(@PathVariable Integer id){
        ItemMenu item = itemService.buscarPorId(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Void> altaItem(@RequestBody ItemMenu nuevoItem, UriComponentsBuilder ucb){
        Integer id = itemService.altaItemMenu(nuevoItem);
        URI uri = ucb.path("api/items/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ItemMenu>> bucarPorNombre(@RequestParam("nombre") String nombre){
        List<ItemMenu> litaItems = itemService.buscarPorNombre(nombre);
        return ResponseEntity.ok(litaItems);
    }

    @PatchMapping("/{id}/no-disponible")
    public ResponseEntity<Void> marcarNoDisponible(@PathVariable Integer id){
        itemService.ponerNoDisponible(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disponible")
    public ResponseEntity<Void> marcarDisponible(@PathVariable Integer id){
        itemService.ponerDisponible(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarItem(@PathVariable Integer id){
        itemService.borrarItemMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> modificarItem(@RequestBody(required = true) ItemMenu item, @PathVariable Integer id){
        itemService.modificarItemMenu(id, item);
        return ResponseEntity.noContent().build();
    }
}
