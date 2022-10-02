package com.eventoapp.dtos;

import com.eventoapp.models.Convidado;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ConvidadoDto {
    @NotEmpty(message = "RG não pode ser vazio")
    @NotBlank(message = "RG não pode começar com espaço")
    @Size(min = 9, max = 12, message = "RG deve ter entre 9 e 12 caracteres")
    private String rg;
    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode começar com espaço")
    @Size(min = 3, max = 60, message = "Nome deve ter entre 3 e 60 caracteres")
    private String nomeConvidado;

    public ConvidadoDto(String rg, String nomeConvidado) {
        this.rg = rg;
        this.nomeConvidado = nomeConvidado;
    }

    public Convidado toConvidado() {
        Convidado convidado = new Convidado();
        convidado.setRg(this.getRg());
        convidado.setNomeConvidado(this.getNomeConvidado());
        return convidado;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getNomeConvidado() {
        return nomeConvidado;
    }

    public void setNomeConvidado(String nomeConvidado) {
        this.nomeConvidado = nomeConvidado;
    }
}
