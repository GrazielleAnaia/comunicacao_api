# *italic*, jnlxcjnx
# 
### Gradle

## Diagrams
Mermaid diagram:

```mermaid
graph LR

A[Write Code] --> B{Does it work?}
B -- Yes --> C[Great!]
B -- No --> D[Google]
D --> A 

```
Este projeto foi desenvolvido para que o usuário possa agendar, consultar ou cancelar uma mensagem de comunicação a ser enviada através de SMS, Email, Whatsapp ou Push.

O projeto foi desenvolvido com Java 12, com SpringBoot e com banco de dados MySql.

Para iniciar o projeto é preciso clonar o repositório localmente e verificar se a senha do banco de dados está inserida (1234) no arquivo application.properties. Além disso, é preciso ter o mysql ou outra ferramenta gestora de banco de dados para configurar o root.

url=jdbc:mysql://localhost:3306/comunicacao1

username=root

senha = 1234

Após, é preciso startar o projeto no run ComunicacaoApiApplication

Para acessar os endpoints da aplicação e testá-la basta ir até o swagger na url: http://localhost:8080/swagger-ui/index.html

O primeiro endpoint a ser testado deve ser o Post /comunicacao/agendar

No Swagger há a informação de todos os dados necessários para que ocorra o agendamento da comunicação. São eles:

{ "dataHoraEnvio": "yyyy-MM-dd HH:mm:ss", "emailDestinatario": "string", "mensagem": "string", "modoDeEnvio": "EMAIL", "nomeDestinatario": "string", "telefoneDestinatario": "string" }

Não é necessário informar o Status do Agendamento, este é automaticamente preenchido como PENDENTE.

O segundo endpoint é o GET/comunicacao/

Este endpoint retorna as informações do agendamento de acordo com o email do Destinatario fornecido.

A escolha do email se deu pela impossibilidade de que existam e-mails iguais para mais de um destinatário, pois cada pessoa tem o seu e-mail único.

Com a informação do parâmetro de e-mail, todas as informações do agendamento são retornadas.

O terceiro endpoint é o PATCH /comunicacao/cancelar

Este endpoint é responsável pelo cancelamento da mensagem agendada.

Basta que o parâmetro email do usuário seja fornecido para que o status do Envio seja automaticamente substituído por CANCELADO.

Não é necessário Token para testar a aplicação.

Por fim, foram realizados testes de unidade nas três principais classes da aplicação = Service, Converter e Controller.

Após, basta ir até a Classe de Teste escolhida e startar a classe.