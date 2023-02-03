package com.fieldright.fr.service.implementation;

import com.fieldright.fr.entity.Carrinho;
import com.fieldright.fr.entity.CidadeAtuacao;
import com.fieldright.fr.entity.Comprador;
import com.fieldright.fr.entity.Endereco;
import com.fieldright.fr.entity.Motorista;
import com.fieldright.fr.entity.Product;
import com.fieldright.fr.entity.Usuario;
import com.fieldright.fr.entity.Vendedor;
import com.fieldright.fr.entity.dto.DepoimentoDTO;
import com.fieldright.fr.entity.dto.Loja;
import com.fieldright.fr.entity.dto.UserCompraDTO;
import com.fieldright.fr.entity.dto.UserDTO;
import com.fieldright.fr.entity.security.UserAuthenticated;
import com.fieldright.fr.mail.EMailSender;
import com.fieldright.fr.repository.CidadeAtuacaoRepository;
import com.fieldright.fr.repository.MotoristaRepository;
import com.fieldright.fr.repository.UserRepository;
import com.fieldright.fr.repository.VendedorRepository;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.dto.TokenDTO;
import com.fieldright.fr.service.interfaces.CarrinhoService;
import com.fieldright.fr.service.interfaces.CategoryService;
import com.fieldright.fr.service.interfaces.ContaService;
import com.fieldright.fr.service.interfaces.EnderecoService;
import com.fieldright.fr.service.interfaces.PictureService;
import com.fieldright.fr.service.interfaces.SuperCategoryService;
import com.fieldright.fr.service.interfaces.UserService;
import com.fieldright.fr.util.StringUtil;
import com.fieldright.fr.util.enums.StatusVenda;
import com.fieldright.fr.util.enums.TipoVeiculo;
import com.fieldright.fr.util.exception.CountryNotSupportedException;
import com.fieldright.fr.util.exception.UserAlreadyExistException;
import com.fieldright.fr.util.exception.UserNotFoundException;
import com.fieldright.fr.util.form.NewPasswordForm;
import com.fieldright.fr.util.form.SingUpForm;
import com.fieldright.fr.util.form.UpdateForm;
import com.fieldright.fr.util.mapper.LojaMapper;
import com.fieldright.fr.util.mapper.UserMapper;
import com.fieldright.fr.util.password.Bcrypt;
import com.fieldright.fr.util.validador.ValidadorDocumentos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;


@Service
public class UserServiceImpl implements UserService {

    private static final String NO_USER_FOUND_BY_EMAIL_MESSAGE = "No user found with the email: ";
    @Value("${src.images.url}")
    private String imageUrl;
    @Value("${src.images.past}")
    private String imagesPast;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LojaMapper lojaMapper;
    @Autowired
    private EMailSender EMailSender;
    @Autowired
    private ContaService contaService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EnderecoService enderecoService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CarrinhoService carrinhoService;
    @Autowired
    private VendedorRepository vendedorRepository;
    @Autowired
    private MotoristaRepository motoristaRepository;
    @Autowired
    private ValidadorDocumentos validadorDocumentos;
    @Autowired
    private SuperCategoryService superCategoryService;
    @Autowired
    private CidadeAtuacaoRepository cidadeAtuacaoRepository;

    /**
     * Método para recuperar um usuário do banco pelo email, sem alterar os campos sensíveis. Utilizado apenas internamente
     *
     * @param email
     * @return
     */
    @Override
    public UserDTO internalFindUserByEmail(String email) {
        Optional<Usuario> userByEmail = userRepository.findUserByEmailIgnoreCase(email);
        if (userByEmail.isPresent()) {
            return userMapper.userDTOFrom(userByEmail.get());
        } else
            throw new UserNotFoundException(NO_USER_FOUND_BY_EMAIL_MESSAGE + email);
    }

    @Override
    public Usuario internalFindUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public Response<HttpStatus> createUser(SingUpForm form) {
        switch (form.getPerfil().toLowerCase(Locale.ROOT)) {
            case "vendedor":
                return createVendedor(form);
            case "comprador":
                return createComprador(form);
            case "motorista":
                return createMotorista(form);
            default:
                return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST, Arrays.asList(
                        "Verifique o valor do campo 'perfil' do seu formulário: Os valores aceitos são (vendedor/comprador/motorista)"));
        }
    }

    @Override
    public Response<HttpStatus> alterPassword(NewPasswordForm form, UserAuthenticated authenticated) {
        if (form.matchingPasswords()) {
            Usuario usuario = userRepository.findById(authenticated.getId());
            usuario.setPassword(Bcrypt.getHash(form.getNewPassword()));
            usuario.setAlterPassword(false);
            userRepository.save(usuario);
            return returnHttpStatusResponse(HttpStatus.ACCEPTED, HttpStatus.OK, null);
        } else
            return returnHttpStatusResponse(HttpStatus.NOT_ACCEPTABLE, HttpStatus.BAD_REQUEST,
                    Arrays.asList("As senhas informadas não são iguais"));
    }

    @Override
    public Response<HttpStatus> forgetPassword(String email) {
        Optional<Usuario> user = userRepository.findUserByEmailIgnoreCase(email);
        if (user.isPresent()) {
            Usuario usuario = user.get();
            String randomPassword = getRandomPassword();
            usuario.setPassword(Bcrypt.getHash(randomPassword));
            usuario.setAlterPassword(true);
            userRepository.save(usuario);
            EMailSender.sendRedefinicaoDeSenha(usuario, randomPassword);
            return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.ACCEPTED, null);
        }
        return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.NOT_FOUND,
                Arrays.asList(NO_USER_FOUND_BY_EMAIL_MESSAGE + email));
    }

    @Override
    public Response<HttpStatus> update(UpdateForm form, UserAuthenticated authenticated) {
        try {
            if (form.getEmail() != null)
                verifyIfUserAlreadyExist(form.getEmail());

            Usuario usuario = userRepository.findById(authenticated.getId());
            updateUser(usuario, form);
            userRepository.save(usuario);
            return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.OK, null);
        } catch (Exception e) {
            return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST, Arrays.asList(e.getMessage()));
        }
    }

    @Override
    public String updateAvatar(MultipartFile avatar, UserAuthenticated authenticated) throws IOException {
        Usuario usuario = userRepository.findById(authenticated.getId());
        String stringId = StringUtil.getTextAfterElement(usuario.getAvatar(), imagesPast);
        Long avatarId = (stringId.equals("")) ? null : Long.parseLong(stringId);
        long pictureId = pictureService.updatePicture(avatar, avatarId);
        String newAvatar = imageUrl + pictureId;
        usuario.setAvatar(newAvatar);
        userRepository.save(usuario);

        return newAvatar;
    }

    @Override
    public void internalUpdateUser(Usuario usuario) {
        userRepository.save(usuario);
    }

    @Override
    public TokenDTO internalCompleteLogin(String token, long userId) {
        Usuario usuario = userRepository.findById(userId);
        UserDTO userDTO = userMapper.userDTOFrom(usuario);
        userDTO.setPassword(null);
        userDTO.setCidadeAtuacao(retornaCidadeAtuacaoSeForMotorista(usuario));
        List<String> categories = categoryService.internalFindAll();

        return new TokenDTO(token, userDTO, categories);
    }

    private CidadeAtuacao retornaCidadeAtuacaoSeForMotorista(Usuario usuario) {
        CidadeAtuacao cidade = null;
        if (usuario.getClass().equals(Motorista.class)) {
            cidade = cidadeAtuacaoRepository.findCidadeAtuacaoById(((Motorista) usuario).getCidadeAtuacao());
        }
        return cidade;
    }

    @Override
    public void internalAdicioneComprasParaComprador(Long carrinhoId, long userId) {
        Comprador comprador = (Comprador) userRepository.findById(userId);
        Carrinho carrinho = carrinhoService.internalFindById(carrinhoId);
        comprador.getCompras().add(carrinho);
        atualizaEnderecosComprador(comprador, carrinho);
        userRepository.save(comprador);

    }

    private void atualizaEnderecosComprador(Comprador comprador, Carrinho carrinho) {
        Endereco enderecoEntrega = carrinho.getCompras().get(0).getEnderecoEntrega();
        for (Endereco endereco : comprador.getEnderecosSalvos()) {
            if (enderecoEntrega.toString().equalsIgnoreCase(endereco.toString())) {
                return;
            }
        }
        comprador.getEnderecosSalvos().add(enderecoEntrega);
    }

    @Override
    public Response<HttpStatus> updateExponentPushToken(UserAuthenticated authenticated, long userId, String exponentPushToken) {

        if (authenticated.getId() != userId)
            return returnHttpStatusResponse(HttpStatus.UNAUTHORIZED, null, Arrays.asList(
                    "Não é permitido alterar informações de outro usuário!"));

        Usuario usuario = userRepository.findById(userId);
        usuario.setExponentPushToken(exponentPushToken);
        userRepository.save(usuario);
        return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.CREATED, null);
    }

    @Override
    public List<Product> addNewProductInVendedor(Product product, Vendedor vendedor) {
        List<Product> productList = vendedor.getProducts();
        productList.add(product);
        userRepository.save(vendedor);

        return productList;
    }

    @Override
    public List<Motorista> internalFindAllEntregadorByTiposVeiculoAndCidades(List<TipoVeiculo> veiculos, String... cidades) {
        List<Usuario> usuarios = motoristaRepository.findAllByTipoVeiculoInAndCidadeAtuacaoIn(veiculos, cidades);
        List<Motorista> entregadores = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            entregadores.add((Motorista) usuario);
        }
        return entregadores;
    }

    @Override
    public List<Motorista> internalFindAllEntregadorByTipoVeiculo(TipoVeiculo tipoVeiculo) {
        List<Usuario> usuarios = motoristaRepository.findAllByTipoVeiculoIsLike(tipoVeiculo);
        List<Motorista> entregadores = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            entregadores.add((Motorista) usuario);
        }
        return entregadores;
    }

    @Override
    public Response<Page<Loja>> getLojas(String name, long category, Pageable pageable) {
        name = name == null || name.isBlank() ? "" : name;
        Page<Vendedor> vendedors = vendedorRepository.findByName(name.toLowerCase(Locale.ROOT), category, pageable);
        Page<Loja> lojas = vendedors.map(vendedor -> {
            return lojaMapper.toLoja(vendedor);
        });
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(lojas)
                .withErrors(null)
                .build();
    }

    private String getValue(Object objs[],Integer index)
    {
        try{

          return   objs[index].toString();

        }catch (Exception e){ }

        return "-";
    }

    private Boolean getValueBoolValue(Object objs[],Integer index)
    {
        try{

            return   Boolean.valueOf(objs[index].toString());

        }catch (Exception e){ }

        return false;
    }
    private BigDecimal getValueBigDecimalValue(Object objs[],Integer index)
    {
        try{

            return (BigDecimal) objs[index];

        }catch (Exception e){ }

        return BigDecimal.ZERO;
    }
    @Override
    public Response<Page<UserCompraDTO>> findByFilter(long usuarioLojaId, StatusVenda status, Pageable pageable) {

        Page<Object[]> objs = userRepository.findByFilters(usuarioLojaId,status.getText(),pageable);
        Page<UserCompraDTO> dtos = null;
        dtos = objs.map(Object -> {
            return UserCompraDTO.builder().id(Long.parseLong(Object[1].toString()))
                    .active(Boolean.valueOf(Object[2].toString()))
                    .alterPassword(Boolean.valueOf(Object[3].toString()))
                    .avatar(Object[4].toString())
                    .createdAt(Timestamp.valueOf(Object[5].toString()))
                    .email(getValue(Object,6))
                    .firstName(getValue(Object,8))
                    .lastName(getValue(Object,9))
                    .perfil(getValue(Object,11))
                    .phone(getValue(Object,12))
                    .bilheteIdentidade(getValue(Object,13))
                    .cnpj(getValue(Object,14))
                    .country(getValue(Object,15))
                    .cpf(getValue(Object,16))
                    .nif(getValue(Object,17))
                    .possuiEntregadores(getValueBoolValue(Object,17))
                    .cnh(getValue(Object,21))
                    .purchaseTotal(getValueBigDecimalValue(Object,30)).build();
        });
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(dtos)
                .withErrors(null)
                .build();

    }

    @Override
    public Response<BigDecimal> findByVendedorFilter(long usuarioLojaId, StatusVenda status) {

        Optional<BigDecimal> objs = userRepository.findCountVendaByVendedor(usuarioLojaId,status.getText());
        BigDecimal total = BigDecimal.ZERO;

        if(objs.isPresent())
        {
            total = objs.get();
        }
        return new Response.Builder()
                .withStatus(HttpStatus.OK)
                .withData(total)
                .withErrors(null)
                .build();
    }

    private void updateUser(Usuario usuario, UpdateForm form) {
        String email = (form.getEmail() == null) ? usuario.getEmail() : form.getEmail();
        String password = (form.getPassword() == null) ? usuario.getPassword() : Bcrypt.getHash(form.getPassword());
        String firstName = (form.getFirstName() == null) ? usuario.getFirstName() : form.getFirstName();
        String lastName = (form.getLastName() == null) ? usuario.getLastName() : form.getLastName();
        String phone = (form.getPhone() == null) ? usuario.getPhone() : form.getPhone();

        if (usuario.getClass().equals(Vendedor.class)) {
            String cpf = (form.getCpf() == null) ? ((Vendedor) usuario).getCpf() : form.getCpf();
            String cnpj = (form.getCnpj() == null) ? ((Vendedor) usuario).getCnpj() : form.getCnpj();
            Boolean possuiEntregadores = form.getPossuiEntregadores() == null ? ((Vendedor) usuario).getPossuiEntregadores() : form.getPossuiEntregadores();

            ((Vendedor) usuario).setCpf(cpf);
            ((Vendedor) usuario).setCnpj(cnpj);
            ((Vendedor) usuario).setPossuiEntregadores(possuiEntregadores);

            if (form.getEndereco() != null) {
                enderecoService.updateEndereco(form.getEndereco(), ((Vendedor) usuario).getEndereco().getId());
            }
        }
        if (usuario.getClass().equals(Motorista.class)) {
            String cpf = (form.getCpf() == null) ? ((Motorista) usuario).getCpf() : form.getCpf();
            String renavam = (form.getRenavam() == null) ? ((Motorista) usuario).getRenavam() : form.getRenavam();
            String cnh = (form.getCnh() == null) ? ((Motorista) usuario).getCnh() : form.getCnh();
            Long cidadeAtuacao = (form.getCidadeAtuacao() == null) ? ((Motorista) usuario).getCidadeAtuacao() : retorneCodigoDa(form.getCidadeAtuacao());
            TipoVeiculo tipoVeiculo = (form.getTipoVeiculo() == null) ? ((Motorista) usuario).getTipoVeiculo() : TipoVeiculo.valueOf(form.getTipoVeiculo());

            ((Motorista) usuario).setCpf(cpf);
            ((Motorista) usuario).setRenavam(renavam);
            ((Motorista) usuario).setCnh(cnh);
            ((Motorista) usuario).setCidadeAtuacao(cidadeAtuacao);
            ((Motorista) usuario).setTipoVeiculo(tipoVeiculo);
        }

        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setFirstName(firstName);
        usuario.setLastName(lastName);
        usuario.setPhone(phone);
    }

    private Long retorneCodigoDa(CidadeAtuacao cidadeAtuacao) {
        CidadeAtuacao cidade = cidadeAtuacaoRepository.findCidadeAtuacaoByCodigoUFAndCodigoMunicipio(cidadeAtuacao.getCodigoUF(), cidadeAtuacao.getCodigoMunicipio());
        if (cidade == null) {
            cidade = cidadeAtuacaoRepository.save(cidadeAtuacao);
        }
        return cidade.getId();
    }

    private Response<HttpStatus> createMotorista(SingUpForm form) {
        try {
            verifyIfUserAlreadyExist(form.getEmail());
            validaDocumentosMotorista(form);
            Motorista motorista = userMapper.motoristaFrom(form);
            String randomPassword = getRandomPassword();
            motorista.setPassword(Bcrypt.getHash(randomPassword));
            motorista.setAvatar(imageUrl);
            motorista.setCidadeAtuacao(retorneCodigoDa(form.getCidadeAtuacao()));
            contaService.internalCreateForNewMotorista(motoristaRepository.save(motorista));
            EMailSender.sendBoasVindas(motorista, randomPassword);
        } catch (RuntimeException e) {
            return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST, Arrays.asList(e.getMessage()));
        }
        
        return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.CREATED, null);
    }

    private void validaDocumentosMotorista(SingUpForm form) throws CountryNotSupportedException {
        switch (form.getCountry()) {
            case "BRA": //Brasil
                validadorDocumentos.validaMotoristaBRA(form.getCpf(), form.getRenavam(), form.getCnh(), TipoVeiculo.valueOf(form.getTipoVeiculo().toUpperCase()));
                break;
            case "AGO": //Angola
                validadorDocumentos.validaMotoristaAGO(form.getBilheteIdentidade(), form.getCartaConducao(), TipoVeiculo.valueOf(form.getTipoVeiculo().toUpperCase()));
                break;
            default:
                throw new CountryNotSupportedException("Não foi possível criar uma conta com o país " + form.getCountry());
        }
    }

    private Response<HttpStatus> createComprador(SingUpForm form) {
        /**
         * Verifique se o usuário já existe
         * Colocar uma senha default aleatória
         * Mandar um e-mail com as informações de login (para o usuário alterar essa senha default e ativar a conta)
         * Setar active=true "O comprador não precisa de uma validação de conta"
         */
        try {
            verifyIfUserAlreadyExist(form.getEmail());
            Comprador comprador = userMapper.compradorFrom(form);
            String randomPassword = getRandomPassword();
            comprador.setPassword(Bcrypt.getHash(randomPassword));
            comprador.setActive(true);
            comprador.setAvatar(imageUrl);
            userRepository.save(comprador);
            EMailSender.sendBoasVindas(comprador, randomPassword);
        } catch (Exception e) {
            return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST, Arrays.asList(e.getMessage()));
        }
        return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.CREATED, null);
    }

    /**
     * * Verifique se o usuário já existe
     * * Colocar uma senha default aleatória
     * * Inicializar o saldo do vendedor (criar uma conta "vazia")
     * * Mandar um e-mail com as informações de login (para o usuário alterar essa senha default e ativar a conta)
     * *
     * *      CAMPOS OBRIGATÓRIOS
     * *    * CPF ou CNPJ
     * *    * Endereço da loja
     *
     * @param form
     * @return
     */
    private Response<HttpStatus> createVendedor(SingUpForm form) {
        try {
            verifyIfUserAlreadyExist(form.getEmail());
            validaDocumentosVendedor(form);
            Map<String, Vendedor> map = montaVendedor(form);
            Vendedor vendedor = (Vendedor) map.values().toArray()[0];
            contaService.internalCreateForNewVendedor(vendedorRepository.save(vendedor));
            EMailSender.sendBoasVindas(vendedor, (String) map.keySet().toArray()[0]);
        } catch (ConstraintViolationException e) {
            ArrayList<String> errors = new ArrayList<>();
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
            while (iterator.hasNext())
                errors.add(iterator.next().getMessageTemplate());
            return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST, errors);
        } catch (RuntimeException e) {
            return returnHttpStatusResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST, Arrays.asList(e.getMessage()));
        }
        return returnHttpStatusResponse(HttpStatus.OK, HttpStatus.CREATED, null);
    }

    private Map<String, Vendedor> montaVendedor(SingUpForm form) {
        Map<String, Vendedor> map = new HashMap<>(1);
        Vendedor vendedor = userMapper.vendedorFrom(form);
        String randomPassword = getRandomPassword();

        vendedor.setEndereco(enderecoService.saveEndereco(form.getEndereco()).getData());
        vendedor.setPassword(Bcrypt.getHash(randomPassword));
        vendedor.setAvatar(imageUrl);
        vendedor.setProximaDesativacao(getProximaDesativacao());
        vendedor.setCategoria(form.getCategoria());
        map.put(randomPassword, vendedor);

        return map;
    }

    private Date getProximaDesativacao() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);

        return calendar.getTime();
    }

    private void validaDocumentosVendedor(SingUpForm form) throws CountryNotSupportedException {
        switch (form.getCountry()) {
            case "BRA": //Brasil
                validadorDocumentos.validaVendedorBRA(form.getCpf(), form.getCnpj());
                break;
            case "AGO": //Angola
                validadorDocumentos.validaVendedorAGO(form.getNif(), form.getBilheteIdentidade());
                break;
            default:
                throw new CountryNotSupportedException("Não foi possível criar uma conta com o país " + form.getCountry());
        }
    }

    private String getRandomPassword() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            builder.append(random.nextInt(9));
        }
        return builder.toString();
    }

    private void verifyIfUserAlreadyExist(String email) {
        if (userRepository.findUserByEmailIgnoreCase(email).isPresent())
            throw new UserAlreadyExistException("This email already used");
    }

    private Response<HttpStatus> returnHttpStatusResponse(HttpStatus status, HttpStatus data, List<String> erros) {
        return new Response.Builder()
                .withStatus(status)
                .withData(data)
                .withErrors(erros)
                .build();
    }
}
