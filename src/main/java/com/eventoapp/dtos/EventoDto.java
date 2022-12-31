package com.eventoapp.dtos;

import com.eventoapp.models.Evento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

// Classe DTO para evitar Web Parameter Tampering
// Tirei o ID, assim não tem como o usuário manipular o ID
// EventoDto = valida os formulários do evento
// Evento = configura a criação de tabela
// OBS: Se colocar @Entity criará uma tabela no banco
@Getter @Setter @AllArgsConstructor
public class EventoDto {
    @NotEmpty(message = "Nome não pode ser vazio")
    @NotBlank(message = "Nome não pode começar com espaço")
    @Size(min = 3, max = 60, message = "Nome deve ter entre 3 e 60 caracteres")
    private String nome;
    @NotEmpty(message = "Local não pode ser vazio")
    @NotBlank(message = "Local não pode começar com espaço")
    @Size(min = 10, max = 30, message = "Local deve ter entre 10 e 30 caracteres")
    private String local;
    @NotNull //@FutureOrPresent Não permite atualizar eventos com datas no passado
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data; //Validações Empty, Blank e Size são válidas apenas para Strings, não datas
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
}
