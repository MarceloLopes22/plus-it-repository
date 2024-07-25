package com.plusit.desafio.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.plusit.desafio.domain.Cliente;
import com.plusit.desafio.domain.Compras;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientesComprasService {

    public static final String LISTAR_PRODUTOS = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/produtos-mnboX5IPl6VgG390FECTKqHsD9SkLS.json";
    public static final String LISTAR_CLIENTES_COMPRAS = "https://rgr3viiqdl8sikgv.public.blob.vercel-storage.com/clientes-Vz1U6aR3GTsjb3W8BRJhcNKmA81pVh.json";

    public List<Cliente> listarCompras() {
        List<Cliente> clienteList = new ArrayList<>();
        try {
            URL url = new URL(LISTAR_CLIENTES_COMPRAS);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray jsonArrayClientes = root.getAsJsonArray();

            for (int i = 0; i < jsonArrayClientes.size(); i++) {

                List<Compras> comprasList = new ArrayList<>();

                Cliente cliente = Cliente.builder()
                        .nome(jsonArrayClientes.get(i).getAsJsonObject().get("nome").getAsString())
                        .cpf(jsonArrayClientes.get(i).getAsJsonObject().get("cpf").getAsString())
                        .build();

                for (int a = 0; a < jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().size(); a++) {

                    Compras compras = Compras.builder()
                            .codigo(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("codigo").getAsString())
                            .quantidade(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("quantidade").getAsInt())
                            .build();

                    URL url2 = new URL(LISTAR_PRODUTOS);
                    URLConnection request2 = url2.openConnection();
                    request.connect();

                    JsonParser jp2 = new JsonParser();
                    JsonElement root2 = jp2.parse(new InputStreamReader((InputStream) request2.getContent()));
                    JsonArray jsonArrayProduto = root2.getAsJsonArray();

                    for (int s = 0; s < jsonArrayProduto.size(); s++) {
                        if(Integer.valueOf(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("codigo").getAsString()) ==
                                jsonArrayProduto.get(s).getAsJsonObject().get("codigo").getAsInt()) {
                            compras.setTipo_vinho(jsonArrayProduto.get(s).getAsJsonObject().get("tipo_vinho").getAsString());
                            compras.setPreco(jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsFloat());
                            compras.setSafra(jsonArrayProduto.get(s).getAsJsonObject().get("safra").getAsString());
                            compras.setAno_compra(jsonArrayProduto.get(s).getAsJsonObject().get("ano_compra").getAsInt());
                            compras.setValor_total_compra(compras.getQuantidade() * jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsFloat());
                        }
                    }

                    comprasList.add(compras);
                }

                cliente.setCompras(comprasList);

                clienteList.add(cliente);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    return clienteList;
    }

    public Cliente maiorCompraAno(Integer ano) {
        Cliente cliente = null;
        try {
            URL url = new URL(LISTAR_CLIENTES_COMPRAS);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray jsonArrayClientes = root.getAsJsonArray();

            for (int i = 0; i < jsonArrayClientes.size(); i++) {

                List<Compras> comprasList = new ArrayList<>();

                cliente = Cliente.builder()
                        .nome(jsonArrayClientes.get(i).getAsJsonObject().get("nome").getAsString())
                        .cpf(jsonArrayClientes.get(i).getAsJsonObject().get("cpf").getAsString())
                        .build();

                for (int a = 0; a < jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().size(); a++) {

                    Compras compras = Compras.builder()
                            .quantidade(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("quantidade").getAsInt())
                            .build();

                    URL url2 = new URL(LISTAR_PRODUTOS);
                    URLConnection request2 = url2.openConnection();
                    request.connect();

                    JsonParser jp2 = new JsonParser();
                    JsonElement root2 = jp2.parse(new InputStreamReader((InputStream) request2.getContent()));
                    JsonArray jsonArrayProduto = root2.getAsJsonArray();

                    double maiorCompra = 0.00;
                    for (int s = 0; s < jsonArrayProduto.size(); s++) {
                        if (ano == jsonArrayProduto.get(s).getAsJsonObject().get("ano_compra").getAsInt()) {
                            if (jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsDouble() > maiorCompra) {
                                maiorCompra = jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsDouble();
                                compras.setIsMaiorCompra(true);
                            }
                        }
                        compras.setValor_total_compra(compras.getQuantidade() * jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsFloat());
                    }

                    comprasList.add(compras);
                }

                cliente.setCompras(comprasList);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cliente;
    }

    public List<Cliente> getClientesFieis() {

        List<Cliente> top3Clientes = new ArrayList<>();
        int primeiroMaior = 0,segundoMaior = 0,terceiroMaior = 0;
        Cliente primeiroCliente = null, segundoCliente = null, terceiroCliente = null;
        try {
            URL url = new URL(LISTAR_CLIENTES_COMPRAS);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray jsonArrayClientes = root.getAsJsonArray();

            for (int i = 0; i < jsonArrayClientes.size(); i++) {

                List<Compras> comprasList = new ArrayList<>();

                Cliente cliente = Cliente.builder()
                        .nome(jsonArrayClientes.get(i).getAsJsonObject().get("nome").getAsString())
                        .cpf(jsonArrayClientes.get(i).getAsJsonObject().get("cpf").getAsString())
                        .build();

                for (int a = 0; a < jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().size(); a++) {

                    Compras compras = Compras.builder()
                            .codigo(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("codigo").getAsString())
                            .quantidade(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("quantidade").getAsInt())
                            .build();

                    URL url2 = new URL(LISTAR_PRODUTOS);
                    URLConnection request2 = url2.openConnection();
                    request.connect();

                    JsonParser jp2 = new JsonParser();
                    JsonElement root2 = jp2.parse(new InputStreamReader((InputStream) request2.getContent()));
                    JsonArray jsonArrayProduto = root2.getAsJsonArray();


                    for (int s = 0; s < jsonArrayProduto.size(); s++) {
                        if(Integer.valueOf(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("codigo").getAsString()) ==
                                jsonArrayProduto.get(s).getAsJsonObject().get("codigo").getAsInt()) {

                            compras.setTipo_vinho(jsonArrayProduto.get(s).getAsJsonObject().get("tipo_vinho").getAsString());
                            compras.setPreco(jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsFloat());
                            compras.setSafra(jsonArrayProduto.get(s).getAsJsonObject().get("safra").getAsString());
                            compras.setAno_compra(jsonArrayProduto.get(s).getAsJsonObject().get("ano_compra").getAsInt());
                            compras.setValor_total_compra(compras.getQuantidade() * jsonArrayProduto.get(s).getAsJsonObject().get("preco").getAsFloat());
                        }
                    }

                    comprasList.add(compras);
                }

                cliente.setCompras(comprasList);

                if (comprasList.size() > primeiroMaior){
                    terceiroMaior = segundoMaior;
                    segundoMaior = primeiroMaior;
                    primeiroMaior = comprasList.size();
                    primeiroCliente = cliente;
                } else if(comprasList.size() > segundoMaior) {
                    terceiroMaior = segundoMaior;
                    segundoMaior = comprasList.size();
                    segundoCliente = cliente;
                }else if(comprasList.size() > terceiroMaior) {
                    terceiroMaior = comprasList.size();
                    terceiroCliente = cliente;
                }

            }
            top3Clientes.add(primeiroCliente);
            top3Clientes.add(segundoCliente);
            top3Clientes.add(terceiroCliente);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return top3Clientes;
    }

    public String getRecomendacaoTipoVinho() {
        int codigoVinhoAnterior = 0, codigoVinhoAtual = 0;
        String tipoVinhoAtual = "";
        try {
            URL url = new URL(LISTAR_CLIENTES_COMPRAS);
            URLConnection request = url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonArray jsonArrayClientes = root.getAsJsonArray();

            for (int i = 0; i < jsonArrayClientes.size(); i++) {

                for (int a = 0; a < jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().size(); a++) {

                    Compras compras = Compras.builder()
                            .codigo(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("codigo").getAsString())
                            .quantidade(jsonArrayClientes.get(i).getAsJsonObject().get("compras").getAsJsonArray().get(a).getAsJsonObject().get("quantidade").getAsInt())
                            .build();

                    codigoVinhoAtual = Integer.valueOf(compras.getCodigo());

                    if (codigoVinhoAtual != codigoVinhoAnterior){
                        codigoVinhoAnterior = codigoVinhoAtual;
                    }
                }

            }

            URL url2 = new URL(LISTAR_PRODUTOS);
            URLConnection request2 = url2.openConnection();
            request.connect();

            JsonParser jp2 = new JsonParser();
            JsonElement root2 = jp2.parse(new InputStreamReader((InputStream) request2.getContent()));
            JsonArray jsonArrayProduto = root2.getAsJsonArray();

            for (int s = 0; s < jsonArrayProduto.size(); s++) {
                if (jsonArrayProduto.get(s).getAsJsonObject().get("codigo").getAsInt() == codigoVinhoAtual) {
                    tipoVinhoAtual = jsonArrayProduto.get(s).getAsJsonObject().get("tipo_vinho").getAsString();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tipoVinhoAtual;
    }
}
