package com.eventoapp.services;

import com.eventoapp.dtos.ConvidadoDto;
import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
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
class ConvidadoServiceTest {

    public static final Long ID = 1L;
    public static final String RG = "10.815.185-2";
    public static final String NOME = "Paulino";

    private Convidado convidado;
    private ConvidadoDto convidadoDto;
    private Optional<Convidado> optionalConvidado;
    private Optional<Convidado> optionalConvidadoVazio;

    @Mock
    private ConvidadoRepository convidadoRepository;
    private ConvidadoService underTest;

    @BeforeEach
    void setUp() {
        underTest = new ConvidadoService(convidadoRepository);
        iniciarConvidado();
    }

    private void iniciarConvidado() {
        //Essas variáveis substituem os "Given" que seriam repetidos
        //OBS: daria para colocar essas atribuições nas linhas 27-30
        convidado = new Convidado(ID, RG, NOME);
        convidadoDto = new ConvidadoDto(RG, NOME);
        optionalConvidado = Optional.of(convidado);
        optionalConvidadoVazio = Optional.empty();
    }

    @Test
    @DisplayName("findByEvento(): verifica se chamou o método no mesmo objeto passado")
    void acharPorEvento() {
        //Given
        Evento evento = new Evento();

        //When
        underTest.acharPorEvento(evento);

        //Then
        verify(convidadoRepository).findByEvento(evento);
    }

    @Test
    @DisplayName("save(): verifica se o objeto enviado e recebido são iguais")
    void salvarConvidadoSucesso() {
        //TODO: 1 - fazer o convidadoUnico() retornar true aqui
        //TODO: 2 - criar o salvarConvidadoErro(), onde o convidadoUnico() retornar false
        //Given
        Convidado convidadoValidado = convidadoDto.toConvidado();

        //When
        underTest.salvarConvidado(convidadoValidado);

        //Then
        ArgumentCaptor<Convidado> convidadoArgumentCaptor =
                ArgumentCaptor.forClass(Convidado.class);
        verify(convidadoRepository)
                .save(convidadoArgumentCaptor.capture());

        Convidado capturedConvidado =
                convidadoArgumentCaptor.getValue();
        assertThat(capturedConvidado).isEqualTo(convidadoValidado);
    }

    @Test
    @DisplayName("convidadoId(): verifica se o retorno é não nulo e da classe Convidado")
    void convidadoIdSucesso() {
        //When
        when(convidadoRepository
                .findById(ID))
                .thenReturn(optionalConvidado);
        Convidado retorno = underTest.convidadoId(ID);

        //Then
        assertThat(retorno).isNotNull();
        assertThat(retorno.getClass()).isEqualTo(Convidado.class);
        assertThat(ID).isEqualTo(retorno.getId()); //Verificação opcional
    }

    @Test
    @DisplayName("convidadoId(): verifica se o retorno é nulo")
    void convidadoIdErro() {
        //When
        when(convidadoRepository
                .findById(ID))
                .thenReturn(optionalConvidadoVazio);
        Convidado retorno = underTest.convidadoId(ID);

        //Then
        assertThat(retorno).isNull();
    }

    @Test
    @DisplayName("deletarConvidado(): verifica se o retorno é true e que usou o deleteById() no mesmo ID enviado")
    void deletarConvidadoSucesso() {
        //When
        when(convidadoRepository
                .findById(ID))
                .thenReturn(optionalConvidado);
        boolean retorno = underTest.deletarConvidado(ID);

        //Then
        assertThat(retorno).isEqualTo(true);
        verify(convidadoRepository).deleteById(ID);
    }

    @Test
    @DisplayName("deletarConvidado(): verifica se o retorno é false e que nunca usou deleteById() no ID enviado")
    void deletarConvidadoErro() {
        //When
        when(convidadoRepository
                .findById(ID))
                .thenReturn(optionalConvidadoVazio);
        boolean retorno = underTest.deletarConvidado(ID);

        //Then
        assertThat(retorno).isEqualTo(false);
        verify(convidadoRepository, never()).deleteById(ID);
    }
}