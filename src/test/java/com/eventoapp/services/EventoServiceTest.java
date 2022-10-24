package com.eventoapp.services;

import com.eventoapp.dtos.EventoDto;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.EventoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    public static final Long ID = 1L;
    public static final String NOME = "Aurora Boreal";
    public static final String LOCAL = "Ilhas Lofoten";
    public static final String DATA = "2022-10-01";
    public static final String HORARIO = "01:10";

    private Evento evento;
    private Evento eventoValidado;
    private EventoDto eventoDto;
    private Optional<Evento> optionalEvento;
    private Optional<Evento> optionalEventoVazio;

    @Mock
    private EventoRepository eventoRepository;
    private EventoService underTest;

    @BeforeEach
    void setUp() {
        underTest = new EventoService(eventoRepository);
        iniciarEvento();
    }

    private void iniciarEvento() {
        evento = new Evento(ID, NOME, LOCAL, DATA, HORARIO);
        eventoDto = new EventoDto(NOME, LOCAL, DATA, HORARIO);
        eventoValidado = eventoDto.toEvento();
        optionalEvento = Optional.of(evento);
        optionalEventoVazio = Optional.empty();
    }

    @Test
    @DisplayName("findAll(): verifica se chamou o método")
    void todosEventos() {
        //When
        underTest.todosEventos();

        //Then
        verify(eventoRepository).findAll();
    }

    @Test
    @DisplayName("save(): verifica se o objeto enviado e recebido são iguais")
    void salvarEventoSucesso() {
        //TODO: 1 - fazer o eventoUnico() retornar true aqui
        //TODO: 2 - criar o salvarEventoErro(), onde o eventoUnico() retornar false
        //When
        underTest.salvarEvento(eventoValidado);

        //Then
        ArgumentCaptor<Evento> eventoArgumentCaptor =
                ArgumentCaptor.forClass(Evento.class);
        verify(eventoRepository)
                .save(eventoArgumentCaptor.capture());

        Evento capturedEvento =
                eventoArgumentCaptor.getValue();
        assertThat(capturedEvento).isEqualTo(eventoValidado);
    }

    @Test
    @DisplayName("eventoId(): verifica se o retorno é não nulo e da classe Evento")
    void eventoIdSucesso() {
        //When
        when(eventoRepository
                .findById(ID))
                .thenReturn(optionalEvento);
        Evento retorno = underTest.eventoId(ID);

        //Then
        assertThat(retorno).isNotNull();
        assertThat(retorno.getClass()).isEqualTo(Evento.class);
    }

    @Test
    @DisplayName("eventoId(): verifica se o retorno é nulo")
    void eventoIdErro() {
        //When
        when(eventoRepository
                .findById(ID))
                .thenReturn(optionalEventoVazio);
        Evento retorno = underTest.eventoId(ID);

        //Then
        assertThat(retorno).isNull();
    }

    @Test
    @DisplayName("deletarEvento(): verifica se o retorno é true e que usou o deleteById() no mesmo ID enviado")
    void deletarEventoSucesso() {
        //When
        when(eventoRepository
                .findById(ID))
                .thenReturn(optionalEvento);
        boolean retorno = underTest.deletarEvento(ID);

        //Then
        assertThat(retorno).isEqualTo(true);
        verify(eventoRepository).deleteById(ID);
    }

    @Test
    @DisplayName("deletarEvento(): verifica se o retorno é false e que nunca usou deleteById() no ID enviado")
    void deletarEventoErro() {
        //When
        when(eventoRepository
                .findById(ID))
                .thenReturn(optionalEventoVazio);
        boolean retorno = underTest.deletarEvento(ID);

        //Then
        assertThat(retorno).isEqualTo(false);
        verify(eventoRepository, never()).deleteById(ID);
    }

    @Test
    @DisplayName("atualizarEvento(): verifica se atualizou os atributos")
    void atualizarEventoSucesso() {
        //TODO: 3 - fazer o eventoUnico() retornar true aqui
        //TODO: 4 - criar o atualizarEventoErro(), onde o eventoUnico() retornar false
        //Given
        evento.setNome(">>>Aurora Boreal<<<");
        evento.setLocal(">>>Ilhas Lofoten<<<");

        //When
        underTest.atualizarEvento(evento, eventoValidado);

        //Then
        verify(eventoRepository).save(evento);
        assertThat(NOME).isEqualTo(evento.getNome());
        assertThat(LOCAL).isEqualTo(evento.getLocal());
    }
}