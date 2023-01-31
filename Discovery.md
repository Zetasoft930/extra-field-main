# Refinamento das funcionalidades

## Cadastro
Para realizar cadastro na Field Right, basta ir no aplicativo respectivo para realizá-lo. Para isso seria necessário 
informar:

* Nome completo
* Email
* Celular
* CPF (ou CNPJ para agricultores caso tenham)
* CNH (para entregadores)
* Documentos do veículo (para entregadores)

Após mandar as informações solicitadas, o usuário receberá um email no endereço informado com uma senha provisória para 
realizar o primeiro acesso.
Acessando com aquela senha no aplicativo, será solicitada a criação de uma nova senha para o usuário.

Para os entregadores, os documentos enviados passarão por uma validação da parte da Field Right antes de poder começar 
a trabalhar na plataforma.
Enquanto tiver uma pendência nesse processo de avaliação, o motorista não poderá realizar entregas

## Usuários
Todos os usuários poderão
* Se cadastrar nos respectivos aplicativos
* Realizar o login nos respectivos aplicativos
* Alterar a senha caso for esquecida
* Editar o perfil dentro dos respectivos aplicativos

## Comprador
O comprador é o usuário que estaria precisando realizar a compra de algum produto. No aplicaivo voltado ao perfil dele, 
ele poderá

#### Navegando/pesquisando
* Pesquisar algum produto pelo nome deste
* Ver a lista de todos os produtos
* Utilizar filtros para ordenar essa visualização dos produtos
* Ver as informações de um vendedor(agricultor)
* Ver todos os produtos de um vendedor
* Avaliar um vendedor
* Adicionar produtos no carrinho
* Ver o carrinho

#### Comprando
* Retirar produtos do carrinho
* Selecionar a forma de pagamento

#### Acompanhando
* Ver a lista de todas as compras realizadas
* Acompanhar os pedidos (Aceito/à caminho/recusado/entregue)

## Motorista (entregador)
O entregador é aquele usuário que estaria usando seu veículo para buscar o produto no agricultor e entregar para o 
comprador após a realização de uma compra. Ele poderá

#### Conta
* Cadastrar uma conta bancária

#### Navegando/Alertas
* Ver coleitas disponíveis (compras realizadas onde os produtos precisam ser entregues)
* Aceitar ou recusar uma coleita

#### Entregando
* Informar quando o produto for entregue
* Usar o maps (Google maps) para consegiur chegar até o destino


## Agricultor (vendedor)
O agricultor ou vendedor é aquele usuário que possui produtos para serem vendidos. Ele poderá

#### Navegando
* Cadastrar seus produtos
* Atualizar informações(detalhes) dos produtos
* Excluir seus produtos
* Ver a lista de todos seus produtos
* Ver todos os pedidos de compra (com detalhes)
* Ver todas as compras (suas vendas)
* Ver suas avaliações

#### Conta
* Cadastrar uma conta bancária

# Detalhes de regra de negócio

## Cadastro
Para realizar o cadastro no app da Field Right, é preciso de acordo com o tipo de usuário, preencher as informações seguintes:

#### Agricultor:
* **Nome** *Nome da pessoa ou da empresa*
* **CPF** *Caso não tiver um cnpj*
* **CNPJ** *caso for a conta de uma empresa*
***Obs:*** *É obrigatório ter pelo menos um dos dois dados (cpf,cnpj) para finalizar o cadastro*
* **Email** *O email deve ser válido pois precisará de uma validação para ativar sua conta*
* **Endereço** *Endereço da empresa ou endereço onde estão os produtos*

#### Comprador:
* **Nome**
* **Email** *O email deve ser válido pois precisará dele para recuperar a sua senha*

#### Motorista:
* **Nome** *Nome da pessoa ou da empresa*
* **CPF**
* **Email** *O email deve ser válido pois precisará de uma validação para ativar sua conta*
* **CNH** *É preciso informar um CNH válido*
* **RENAVAM** *É preciso informar um RENAVAM válido*

Depois de realizar o cadastro, o usuário receberá um email de confirmação contendo uma senha provisória para poder 
realizar seu primeiro login. No caso do agricultor e do motorista, ainda não conseguirão realizar login nos respectivos 
applicativos até que suas contas estejam validadas pela administração da Field Right.
***Obs:*** *Somente o comprador poderá realizar o login sem necessidade de validaç~çao de contas.*

## Login
Ambos os perfis, ao realizar o primeiro login, será necessário cadastrar uma senha. Para realizar o login basta 
preencher o e-mail e senha (provisório ou a nova caso for alterada).


## Vendedor
#### Vendas
A cada venda realizada, o vendedor recebe um valor na conta virtual dele.
Este valor é calculado pela fórmula seguinte: vr = vc-vp, onde : 
* vr : o valor que o vendedor receberá,
* vc : o valor da venda,
* vp : o percentual aplicado sobre o valor da venda. 
**Obs** : *Este percentual é definido pela Field Right*

###### Exemplo
Se o vendedor fechar uma venda de R$100 e que o percentual da taxa é de 30%, o vendedor receberá R$70 na conta dele, 
relacionada à venda realizada.

#### Conta
Como indicado no tópico de vendas, a conta virtual do vendedor ficará acumulando os valores recebidas a cada venda. Para
 a Field Right se responsabiliza para transferir esse saldo para a conta bancária do vendedor, por isso cada vendedor 
 deverá cadastrar pelo aplicativo as informações da sua conta bancária para poder receber as transferências.
 
## Discovery Motorista - Entregas
### Cadastro
Os entregadores podem trabalhar com :
* Bicicleta
* Moto
* Carro
* Carro pesado
* Empresas transportadoras (DHL, Sedex...)

###### Implicações
* Ao fazer o cadastro, o entregador deverá escolher qual tipo de veículo será utilizado.
* De acordo com o veículo escolhido, deverá ter uma validação adequada para os documentos do entregador.

### Remuneração
* Bicicleta:
Entregadores de bicicleta têm uma remuneração de R$3 para cada entrega realizada. As corridas deles são limitadas à 4km
de distância.
* Moto: Entregadores de moto têm remuneração de R$5 para corridas de 0 - 50km de distância. 
Realizando uma corrida de mais de 50km, ele ganhará R$1 para cada km a mais.
* Carros / Carros pesados: Remuneração igual à de motoboy
###### Implicações
* O entregador de bike deverá receber notificações só de entregas de no máximo 4km de distância.
* Calcular a remuneração do motoboy de acordo com a distância da entrega realizada.

### Taxa de entrega
O comprador não paga taxa de entrega se realizar a compra de um vendedor cituado na mesma cidade.
Caso realizar uma compra fora da cidade de entrega, será cobrada uma taxa de entrega conforme a tabela de preços por peso
somado com o preço da distância (i.e para cada km o valor é R$1).
**taxa = p + (x\*k)** onde:\
p = o preço equivalente ao peso do produto, conforme a tabela,\
x = a distância em km, \
k = o valor definido pela Field Right. (R$1 inicialmente)
##### Gramas
* Até 20 	 R$     1,30
* 20 até   50	 R$     1,95
* 50 até 100	 R$     2,50
* 100 até 150	 R$     3,05
* 150 até 200	 R$     3,65
* 200 até 250	 R$     4,20
* 250 até 300	 R$     4,75
* 300 até 350	 R$     5,25
* 350 até 400	 R$     5,90
* 400 até 450	 R$    6,50
* 450 até 500	 R$    7,10
* 500 até 550	 R$     7,50
* 550 até 600	 R$    8,10
* 600 até 650	 R$    8,60
* 650 até 700	 R$    9,00
* 700 até 750	 R$     9,50
* 750 até 800	 R$     9,95
* 800 até 850	 R$     10,55
* 850 até 900	 R$    11,15
* 900 até 950	 R$     11,60
* 950 ate 1000 R$ 33,50
* 1kg até 2 kg R$ 35
* 2 kg até 5 kg R$ 44 
* 5 kg até 10 kg R$ 65 
* 10 kg até 15 kg R$ 100
* 15 kg até 25 kg R$ 130
* 25 kg até 50 kg R$ 170
* 50kg até 100 kg R$ 230
* 100kg até 150 kg R$ 255
* 150 kg até 200 kg R$ 320
* 200kg até 220 kg R$ 367
* 220kg até 250 kg R$ 420
* 250 kg até 270 kg R$ 470
* 270 kg até 300kg 500 
* 300 kg até 320 kg 599 
* 320 kg até 350 kg 622
* 350 kg até 400 kg 699
* 400 kg até 450 kg 715
* 450 kg até 500 kg 744
* 500 kg até 550 kg 777
* 550 kg até 600 kg 999
* 600 kg até 650 kg 1020
* 650 kg até 700 kg 1077
* 700 kg até 750 kg 1200
* 750 kg até 800 kg 1500
* 800 kg até 850 kg 1700
* 850 kg até 900 kg 2000
* 900 kg até 950 kg 2250
* 950 kg até 1000 kg 2700
* 1000 kg até2000 kg 3000
* 2000 kg até 3000kg 4500