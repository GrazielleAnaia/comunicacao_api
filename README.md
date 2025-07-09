# Introduction
## Projeto comunicacao_api
Este projeto foi desevolvido para que o usuario possa agendar, consultar, alterar ou cancelar uma mensagem de comunicacao a ser enviada por email. 
Comunicacao_api foi desenvolvido usando Java 17, Spring Boot 3 e com o banco de dados MySql. O projeto usa comunicacao assincrona usando a plataforma FeignClient, conectando uma api externa para servico de envio de email.
Testes unitarios forma desenvolvidos usando o JUnit5 com o Mockito.


# Usage
## Configuration
Clona-se a comunicacao_api para o repositorio local, e se verifica a insercao da senha do banco de dados (=1234),
no arquivo 
```bash
application.properties
```

Configura-se o  MySql ou outra ferramenta gestora de banco de dados para configurar o root.

```bash
 url=jdbc:mysql://localhost:3306/comunicacao1

username=root

senha = 1234
```
Precisa-se startar o projeto no run 

```bash
 ComunicacaoApiApplication
```
## Endpoints

Para acessar os endpoints da aplicação e testá-la basta ir até o swagger na url: http://localhost:8181/swagger-ui/index.html 

O primeiro endpoint a ser testado deve ser o POST /comunicacao/agendar

No Swagger há a informação de todos os dados necessários para que ocorra o agendamento da comunicação. São eles:

{ "dataHoraEvento: "yyyy-MM-dd HH:mm:ss", "dataHoraEnvio": "yyyy-MM-dd HH:mm:ss", "emailDestinatario": "string", "mensagem": "string", "modoDeEnvio": "EMAIL", "nomeDestinatario": "string", "telefoneDestinatario": "string" }

Não é necessário informar o Status do Agendamento, este é automaticamente preenchido como PENDENTE.

O segundo endpoint é o GET /comunicacao/
Este endpoint retorna as informações do agendamento de acordo com o email do Destinatario fornecido.

A escolha do email se deu pela impossibilidade de que existam e-mails iguais para mais de um destinatário, pois cada pessoa tem o seu email único.

Com a informação do parâmetro de e-mail, todas as informações do agendamento são retornadas.

O terceiro endpoint é o PATCH /comunicacao/cancelar
Este endpoint é responsável pelo cancelamento da mensagem agendada.

O quarto endpoint e o POST /comunicacao/email
Este endpoint acessa e api externa de envio de email usando como parametro a mensagem de comunicacao criada.

O quinto endpoint e o PUT /comunicacao
Este endpoint atualiza os dados da comunicacao atraves do id da mensagem.

O sexto endpoint e o DELETE /comunicacao/{id}
Esse endpoint deleta no banco de dados atraves do id da mensagem. 

Basta que o parâmetro email do usuário seja fornecido para que o status do Envio seja automaticamente substituído por CANCELADO.

Não é necessário Token para testar a aplicação.

Por fim, foram realizados testes de unidade nas três principais classes da aplicação = Service, Converter e Controller.

