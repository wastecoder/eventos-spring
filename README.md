# Aplicação de Eventos
Projeto CRUD com Spring Boot, Bootstrap e WampServer.

## Descrição do projeto
- É baseado no sistema criado no [curso](https://www.youtube.com/playlist?list=PL8iIphQOyG-DHLpEx1TPItqJamy08fs1D) da Michelli Brito, mas com alterações e adições.
- Nele o usuário cria **eventos** ⸻ com _nome, local, data e horário_.
- Os eventos existentes podem ter **convidados** ⸻ com _nome e RG_.
- Ambos podem ser cadastrados, lidos, editados e deletados.

## Instalação
Após clonar e importar o repositório, basta configurar o banco de dados:
- No seu servidor local, crie o banco de dados __eventosapp__, sem tabelas.
- No arquivo **application.properties** altere a _url, username e password_ adequado ao seu servidor.
- Execute o arquivo **EventoApplication** e entre na porta [8080](http://localhost:8080/).

## Funcionalidades
- [x] Friendly URL
- [x] Fragments do Thymeleaf para reaproveitamento
- [x] Dados dos formulários são validados no HTML e Spring
- [x] Formulários inválidos são retornados preenchidos e informam o erro
- [x] Usa DTO para prevenir Web Parameter Tampering
- [x] Janela de confirmação para deletar objetos
- [x] Convidados são removidos automaticamente ao deletar seu evento
- [ ] Pop up de notificação para confirmação e erro das ações de CRUD
- [ ] Camada service como intermediário entre controlador e banco de dados
- [ ] Paginação e ordenação
- [ ] Lombok nas entidades
- [ ] RG como number na entidade
- [ ] Data como Calendar() na entidade
- [ ] Data no padrão brasileiro na exibição
- [ ] Input mask no RG
- [ ] RG validado no DTO
