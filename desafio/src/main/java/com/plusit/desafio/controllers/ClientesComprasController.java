package com.plusit.desafio.controllers;

import com.plusit.desafio.domain.Cliente;
import com.plusit.desafio.service.ClientesComprasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes-compras")
@CrossOrigin(origins = "*")
public class ClientesComprasController {

    @Autowired
    private ClientesComprasService clientesComprasService;


    @GetMapping(value = "/compras")
    public ResponseEntity getCompras(){
        List<Cliente> clienteList = this.clientesComprasService.listarCompras();
        return ResponseEntity.ok(clienteList);
    }

    @GetMapping(path = {"/maior-compra/{ano}"})
    public ResponseEntity maiorCompraAno(@PathVariable(required = false,name = "ano") int ano) {
        Cliente cliente = this.clientesComprasService.maiorCompraAno(ano);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping(value = "/clientes-fieis")
    public ResponseEntity getClientesFieis(){
        List<Cliente> clienteTop3 = this.clientesComprasService.getClientesFieis();
        return ResponseEntity.ok(clienteTop3);
    }

    @GetMapping(value = "/recomendacao/cliente/tipo")
    public ResponseEntity getRecomendacaoTipoVinho(){
        String vinho = this.clientesComprasService.getRecomendacaoTipoVinho();
        return ResponseEntity.ok(vinho);
    }
}
