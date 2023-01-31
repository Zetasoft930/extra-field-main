package com.fieldright.fr.controller.implementation;

import com.fieldright.fr.controller.interfaces.AdminController;
import com.fieldright.fr.entity.Avaliacao;
import com.fieldright.fr.entity.architecture.Category;
import com.fieldright.fr.entity.architecture.MoneyConfig;
import com.fieldright.fr.entity.architecture.RemuneracaoEntregadoresConfig;
import com.fieldright.fr.entity.architecture.Taxa;
import com.fieldright.fr.entity.dto.*;
import com.fieldright.fr.response.Response;
import com.fieldright.fr.security.util.JwtUserUtil;
import com.fieldright.fr.service.interfaces.AdminService;
import com.fieldright.fr.util.mapper.CategoryMapper;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/v1")
@CrossOrigin
public class AdminControllerImpl implements AdminController {

    private AdminService adminService;

    @Override
    @GetMapping(
            value = "/user/findAll"
    )
    public Response<List<UserDTO>> findAllUser() {
        return adminService.findAllUser(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PostMapping(
            value = "/user/create"
    )
    public Response<List<UserDTO>> createVendedor(@RequestBody UserDTO vendedor) {
        return adminService.createVendedor(JwtUserUtil.getUserAuthenticated(), vendedor);
    }

    @Override
    @PutMapping(
            value = "/user/ative",
            params = "userId"
    )
    public Response<List<UserDTO>> ativeUsuario(long userId) {
        return adminService.ativeUsuario(userId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/user/desative",
            params = "userId"
    )
    public Response<List<UserDTO>> desativeUsuario(long userId) {
        return adminService.desativeUsuario(userId, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/product/findAll"
    )
    public Response<List<ProductDTO>> findAllProduct() {
        return adminService.findAllProduct(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @DeleteMapping(
            value = "/product/delete",
            params = "productId"
    )
    public Response<List<ProductDTO>> deleteProduct(@RequestParam long productId) {
        return adminService.deleteProduct(JwtUserUtil.getUserAuthenticated(), productId);
    }

    @Override
    @PostMapping(
            value = "/product/addCategory",
            params = "categoryName"
    )
    public Response<List<Category>> addCategory(String categoryName) {
        return adminService.addCategory(categoryName, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @DeleteMapping(
            value = "/product/deleteCategory/{categoryName}"
    )
    public Response<List<Category>> deleteCategory(@PathVariable String categoryName) {
        return adminService.deleteCategory(categoryName, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/venda/findAll"
    )
    public Response<List<VendaDTO>> findAllVenda() {
        return adminService.findAllVenda(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/compra/findAll"
    )
    public Response<List<CompraDTO>> findAllCompra() {
        return adminService.findAllCompra(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/compra/alterStatus"
    )
    public Response<List<CompraDTO>> alterStatusCompra(@RequestParam long compraId, @RequestParam String newStatus) {
        return adminService.alterStatusCompra(compraId, newStatus, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/avaliacoes/findAll"
    )
    public Response<List<Avaliacao>> findAllAvaliacao() {
        return adminService.findAllAvaliacao(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/entrega/findAll"
    )
    public Response<List<EntregaDTO>> findAllEntrega() {
        return adminService.findAllEntrega(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/moneyConfig/findAll"
    )
    public Response<List<MoneyConfig>> findAllMoneyConfig() {
        return adminService.findAllMoneyConfig(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/moneyConfig/edit",
            params = {"id", "valorMínimo"}
    )
    public Response<MoneyConfig> editMoneyConfig(long id, double valorMínimo) {
        return adminService.editMoneyConfig(id, valorMínimo, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @GetMapping(
            value = "/moneyConfigDelivery/findAll"
    )
    public Response<List<RemuneracaoEntregadoresConfig>> findAllMoneyConfigDelivery() {
        return adminService.findAllMoneyConfigDelivery(JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/moneyConfigDelivery/edit"
    )
    public Response<RemuneracaoEntregadoresConfig> editMoneyConfigDelivery(@RequestBody RemuneracaoEntregadoresConfig config) {
        return adminService.editMoneyConfigDelivery(config, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PutMapping(
            value = "/taxa/edit",
            params = {"id", "percentual"}
    )
    public Response<Taxa> editTaxa(long id, double percentual) {
        return adminService.editTaxa(id, percentual, JwtUserUtil.getUserAuthenticated());
    }

    @Override
    @PostMapping(
            value = "/saque/pay",
            params = "userId"
    )
    public Response<HttpStatus> pay(@RequestParam long userId) {
        return adminService.pay(JwtUserUtil.getUserAuthenticated(), userId);
    }

    @Override
    @PutMapping(
            value = "/tarefas/canceleComprasNaoPagas"
    )
    public void canceleComprasNaoPagas() {
        adminService.canceleComprasNaoPagas(JwtUserUtil.getUserAuthenticated());
    }

	@Override
	@PutMapping(value = "/depoimento/aprove", params = "depoimentoId")
	public Response aprovarDepoimento(long depoimentoId) {
		return adminService.aprovarDepoimento(depoimentoId, JwtUserUtil.getUserAuthenticated());
	}
	
	@Override
	 @PostMapping(
	            value = "/newCategory"
	    )
	public CategoriaDTO create(@RequestBody CategoriaDTO dto) {
		return adminService.create(dto,  JwtUserUtil.getUserAuthenticated());
	}
	
	@Override
	 @PostMapping(
	            value = "/newSuperCategory"
	    )
	public SuperCategoryDTO create(@RequestBody SuperCategoryDTO  dto) {
		return adminService.create(dto, JwtUserUtil.getUserAuthenticated());
	}
	
	@Override
	 @PostMapping(
	            value = "/newPromotion"
	    )
	public PromocaoFreteDTO create(@RequestBody PromocaoFreteDTO dto) {
		return adminService.save(dto, JwtUserUtil.getUserAuthenticated());
	}
}
