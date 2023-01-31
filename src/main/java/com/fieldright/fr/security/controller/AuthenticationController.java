package com.fieldright.fr.security.controller;

import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.security.JwtUser;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.dto.JwtAuthenticationDTO;
import com.fieldright.fr.security.dto.TokenDTO;
import com.fieldright.fr.security.service.JwtUserDetailsServiceImpl;
import com.fieldright.fr.security.util.JwtTokenUtil;
import com.fieldright.fr.service.interfaces.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/auth")
@Api(
        tags = "Authentication",
        description = "Autenticação para acessar aos endpoints privados da api"
)
@AllArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsServiceImpl userDetailsService;
    private UserService userService;

    @PostMapping
    @ApiOperation(
            value = "Login",
            notes = "Este endpoint deve ser chamado para realizar o login na api. \n" +
                    "São passados apenas dois campos no body : \n" +
                    "   * ***email*** : *Email cadastrado* \n" +
                    "   * ***password*** : *Senha do usuário* \n\n" +
                    "Na response é retornado uma entidade com informações do usuário logado e o **token** que deve ser usado em todos os endpoints privados da api. \n" +
                    "O campo **alterPassword** indica se o usuário precisa alterar a senha dele. Caso for **true**, é preciso levá-lo para a página de alteração de senhas.\n\n" +
                    "Em caso de erro, basta conferir a mensagem no campo **message** do objeto retornado, que pode ter as seguintes mensagens: \n" +
                    "   * ***Bad credentials*** : *Esta mensagem indica que a senha informada está incorreta.* \n" +
                    "   * ***Access denied. You must be authenticated in the system to access the requested url*** : *Esta mensagem indica que o e-mail informado está errado* \n" +
                    "   * ***User disabled*** : *Esta mensagem indica que a conta do usuário informado está desabilitada*",
            response = TokenDTO.class,
            ignoreJsonView = true
    )
    public ResponseEntity<Response<TokenDTO>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDTO authenticationDTO,
                                                            BindingResult result) {
        Response<TokenDTO> response = new Response<>();

        try {
            if (result.hasErrors()) {
                result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
                return ResponseEntity.badRequest().body(response);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            JwtUser userDetails = userDetailsService.loadUserByUsername2(authenticationDTO.getEmail());
            String token = jwtTokenUtil.getToken(userDetails);
            TokenDTO dto = userService.internalCompleteLogin(token, userDetails.getId());
            response.setStatus(HttpStatus.OK);
            response.setData(dto);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return new ResponseEntity<>(new Response.Builder()
                    .withStatus(HttpStatus.UNAUTHORIZED)
                    .withData(null)
                    .withErrors(asList(e.getMessage()))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/validate")
    @ApiOperation(
            value = "validate",
            notes = "Este endpoint deve ser chamado quando for preciso validar o token de um usuário.\n" +
                    "O token deve ser informado no header no formato Bearer como nos outros endpoints privados.\n" +
                    "* Caso o token informado for válido, será retornado os dados do usuário igual no endpoint de login.\n" +
                    "* Caso o token informado for inválido, será retornado a mensagem 'O Token informado não está válido.' com status 401.")
    public ResponseEntity<?> validaToken(HttpServletRequest request) {
        String email = jwtTokenUtil.recuperaEmailPelo(request);
        if (email != null) {
            UserDTO user = userService.internalFindUserByEmail(email);
            user.setPassword(null);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("O Token informado não está válido.", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(
            path = "/privacy.html"
    )
    public String getHtml() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"pt-br\">\n" +
                "<head>\n" +
                "  <meta charset=\"UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <title>Política e Privacidade</title>\n" +
                "  <style>\n" +
                "    *{\n" +
                "      padding: 0;\n" +
                "      margin: 0;\n" +
                "    }\n" +
                "    body{\n" +
                "      font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial, sans-serif;\n" +
                "      padding: 16px;\n" +
                "      display: flex;\n" +
                "      align-items: center;\n" +
                "      justify-content: center;\n" +
                "    }\n" +
                "    section{\n" +
                "      max-width: 800px;\n" +
                "    }\n" +
                "    header p{\n" +
                "      margin-top: 16px;\n" +
                "      text-indent: 16px;\n" +
                "    }\n" +
                "    ul{\n" +
                "      padding: 16px 32px;\n" +
                "    }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <section>\n" +
                "    <header>\n" +
                "      <h1>Política e privacidade</h1>\n" +
                "      <h3>Tratamento de dados, pessoal</h3>\n" +
                "      <p>\n" +
                "        Os usuários do aplicativo FIELD RIGHT concedem sua versão para o processamento de dados por uma instalação do aplicativo FIELD RIGHT.\n" +
                "      </p>\n" +
                "      <p>\n" +
                "        Suas informações pessoais são coletadas e processadas pela FIELD RIGHT Prestação de Serviços Tecnológicos, uma empresa de direito Brasileiro, Com sede na Av. Afonso Delabert Neto n 103. CNPJ n 39.363.522/0001-93. \n" +
                "      </p>\n" +
                "      <p>\n" +
                "        A FIELD RIGHT Prestação de Serviços Tecnológicos, LDA apenas coleta e processa os dados por telefone, por meio de aplicativos para instalação e uso do aplicativo. FIELD RIGHT e para os fins listados não classificados 2 abaixo, de acordo com a disposição sobre a proteção de dados Brasil, Angola e no Mundo. Uma divulgação de dados de localização do usuário é um pré-requisito para usar o aplicativo FIELD RIGHT.\n" +
                "      </p>\n" +
                "      <p>\n" +
                "    </header>\n" +
                "      <h4>\n" +
                "        1. Que tipo de dados pessoais coletamos e processamos\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Nome, número de telefone, endereço de e-mail, avaliações e comentários e informações de pagamento do usuário do aplicativo;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Localização geográfica do usuário, hora do serviço e destino;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Dados de identificação do dispositivo no qual o aplicativo FIELD RIGHT foi instalado;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Cookies em www.fieldright.com armazenam o número de telefone do usuário do site e informações de localização geográfica.\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        2. O objetivo da coleta e o período de processamento de dados pessoais\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Recolhemos e processamos dados pessoais para fins de correspondência de um usuário com um Motorista;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Encaminhamos as informações da localização geográfica do usuário e do número de telefone para o condutor para decidir se o usuário está ou não próximo;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Reservamo-nos o direito de usar dados pessoais para resolver problemas de qualidade do serviço do Motorista;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Usamos como informações de contato para informá-lo sobre atualizações de aplicativos FIELD RIGHT;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Recolhemos dados dos caminhos dos usuários do aplicativo FIELD RIGHT para analisar uma cobertura geográfica para fazer uma pergunta aos Motoristas;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Como informações pessoais coletadas através do aplicativo FIELD RIGHT não são usadas para a identificação de pessoas ou seus locais, nem para fins de marketing comportamental.\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Nós armazenamos e processamos dados pessoais dos usuários durante o tempo que estão ativos.\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        3. Metodologia para a recolha de dados de pessoal\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Coletamos dados sobre nomes, números de telefone e endereços de e-mail dos usuários do FIELD RIGHT durante o processo de instalação;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Coletamos informações apenas na localização geográfica e quando o aplicativo FIELD RIGHT é ativado;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Uma recolha de informações de localização quando o aplicativo está FIELD RIGHT fechado no dispositivo do usuário.\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        4. Divulgação de Informações Pessoais e Terceiros\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Os dados pessoais dos usuários são divulgados apenas para Motoristas que ativam o aplicativo FIELD RIGHT; no caso, eles podem ver o nome do usuário, número de telefone e localização;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Depois que o serviço para fornecido, o nome do usuário permanece visível para o driver por 24 horas. O que é necessário para que os drivers sejam solucionados e os problemas associados ao fornecimento, por exemplo, para entrar em contato com o cliente.\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          O feedback sobre uma qualidade do serviço é anônimo e os drivers não são fornecidos informações sobre os nomes e números de telefone dos usuários que comentaram o serviço;\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        5. Eliminação de segurança e dados\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Os dados obtidos durante a prestação do serviço, são transferidos e armazenados em data centers localizados em Portugal. O FIELD RIGHT (incluindo o disputas com Motoristas); os usuários e os fornecedores de serviços locais têm acesso a dados e podem acessar informações apenas com a finalidade de resolver problemas associados ao uso do aplicativo.\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Se desejar eliminar informações pessoais recolhidas pela FIELD RIGHT, pode fazê-lo enviando uma solicitação por escrito para suporte ao cliente em fieldrightapp2017@gmail.com . Depois de recebermos o pedido, iremos excluir a conta e os dados pessoais associados à conta se não houver pagamentos pendentes. Após a eliminação, as informações pessoais serão desprovidas de qualquer identificação e usadas para análise;\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Lembre-se: a desinstalação do aplicativo FIELD RIGHT em seu dispositivo não exclui seus dados pessoais. Se você deseja excluir seus dados pessoais coletados pela FIELD RIGHT, envie um pedido por e-mail por e-mail, conforme indicado acima. Você também deve estar ciente de que um pedido para excluir informações pessoais só é possível se excluirmos sua conta. Consequentemente, você não poderá usar os serviços do FIELD RIGHT em sua conta existente.\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Se assumirmos que nós (ou qualquer outra entidade FIELD RIGHT que processa suas informações) realizam esse processamento contrariamente aos seus direitos pessoais ou em contradição com a lei, em particular se os dados pessoais estiverem incorretos, você pode:\n" +
                "          solicitar uma explicação (e / ou qualquer outra entidade FIELD RIGHT que processe suas informações pessoais);\n" +
                "          solicite-nos (e / ou qualquer entidade FIELD RIGHT que processa seus dados pessoais) para remediar o estado atual das informações (particularmente bloqueando, corrigindo ou completando dados pessoais);\n" +
                "          entre em contato diretamente com sua autoridade de proteção de dados com jurisdição sobre suas informações pessoais.\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        6. Marketing direto\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Usamos seu endereço de e-mail e número de telefone apenas para compartilhar nossas mensagens de marketing relacionadas ao produto se você tiver dado seu consentimento no site www.fiedlright.com ou no aplicativo FIELD RIGHT.\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        7. Informações pessoais dos motoristas de veículos de aluguel particulares\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Coletamos e processamos dados pessoais do driver apenas na medida necessária para verificar as qualificações do motorista para prosseguir a atividade profissional e salvaguardar as relações contratuais associadas ao serviço de aplicativos FIELD RIGHT.\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Qualquer recolha e processamento de dados pessoais do condutor pelos provedores de serviços locais da FIELD RIGHT deve ocorrer nas mesmas condições estabelecidas nesta política de privacidade.\n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Ao usar o aplicativo FIELD RIGHT com o recurso de distribuição de corrida, o operador do serviço pode ver a localização geográfica de um driver, informações sobre as distribuições e os horários de uso do serviço. Se um motorista quiser que sua localização geográfica não seja visível para o funcionário do serviço de distribuição, você pode desativar o aplicativo selecionando \"Sair\".\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "    <p>\n" +
                "      <h4>\n" +
                "        8. Perguntas e organizações internacionais.\n" +
                "      </h4>\n" +
                "      <ul>\n" +
                "        <li>\n" +
                "          Se você tiver dúvidas ou preocupações sobre esta política de privacidade ou suas informações, entre em contato conosco em fieldrightapp2017@gmail.com \n" +
                "        </li>\n" +
                "        <li>\n" +
                "          Esta Política de Privacidade foi publicada em 12 de janeiro de 2021. Qualquer alteração que seja utilizada nesta política de privacidade seja publicada nesta página e possivelmente notificada por e-mail. Estabeleça regularmente esta página por se manter informado de enriquecimentos ou mudanças em nossa política de privacidade.\n" +
                "        </li>\n" +
                "      </ul>\n" +
                "    </p>\n" +
                "  </section>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
