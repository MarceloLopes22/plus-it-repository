package com.plusit.desafio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Compras {

    private String codigo;

    private Integer quantidade;

    private String tipo_vinho;

    private Float preco;

    private String safra;

    private Integer ano_compra;

    private Float valor_total_compra;

    private Boolean isMaiorCompra = false;
}
