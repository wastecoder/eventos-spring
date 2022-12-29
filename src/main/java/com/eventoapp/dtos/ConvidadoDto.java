package com.eventoapp.dtos;

import com.eventoapp.models.Convidado;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter @AllArgsConstructor
public class ConvidadoDto {
    @NotEmpty(message = "RG não pode ser vazio")
    @NotBlank(message = "RG não pode começar com espaço")
    @Size(min = 9, max = 12, message = "RG deve ter entre 9 e 12 caracteres")
    private String rg;
    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode começar com espaço")
    @Size(min = 3, max = 60, message = "Nome deve ter entre 3 e 60 caracteres")
    private String nomeConvidado;

    public Convidado toConvidado() {
        Convidado convidado = new Convidado();
        convidado.setRg(this.getRg());
        convidado.setNomeConvidado(this.getNomeConvidado());
        return convidado;
    }
}
