package com.eventoapp.dtos;

import com.eventoapp.models.Evento;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

// Classe DTO para evitar Web Parameter Tampering
// Tirei o ID, assim não tem como o usuário manipular o ID
// EventoDto = valida os formulários do evento
// Evento = configura a criação de tabela
// OBS: Se colocar @Entity criará uma tabela no banco
public class EventoDto {
    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode começar com espaço")
    @Size(min = 3, max = 60, message = "Nome deve ter entre 3 e 60 caracteres")
    private String nome;
    @NotEmpty(message = "Local não pode ser vazio")
    @NotBlank(message = "Local não pode começar com espaço")
    @Size(min = 10, max = 30, message = "Local deve ter entre 10 e 30 caracteres")
    private String local;
    @NotEmpty @NotBlank
    @Size(min = 8, max = 10)
    private String data;
    @NotEmpty @NotBlank
    @Size(min = 4, max = 5)
    private String horario;

    public Evento toEvento() {   //Converte de EventoDto para Evento - sem ID
        Evento evento = new Evento();
        evento.setNome(this.getNome());
        evento.setLocal(this.getLocal());
        evento.setData(this.getData());
        evento.setHorario(this.getHorario());
        return evento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }
}
