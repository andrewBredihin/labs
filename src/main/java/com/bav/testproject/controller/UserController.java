package com.bav.testproject.controller;

import com.bav.testproject.MongoUserDetails;
import com.bav.testproject.entity.Product;
import com.bav.testproject.repository.BasketRepository;
import com.bav.testproject.repository.ProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private ProductRepositoryCustom productRepositoryCustom;

    @Autowired
    private BasketRepository basketRepository;

    //Страница пользователя
    @RequestMapping(value = { "/userPage" }, method = RequestMethod.GET)
    public String userPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MongoUserDetails user = (MongoUserDetails)authentication.getPrincipal();
        long userId = user.getUser().getId();
        //Получаем список товаров из корзины пользователя
        List<Product> products = this.productRepositoryCustom.findFromBasket(userId);
        model.addAttribute("products", products);

        return "userPage";
    }
    //Удаление товара из корзины
    @RequestMapping(value = { "/userPage" }, method = RequestMethod.POST)
    public String userPagePost(@RequestParam(name = "deleteProductButton", defaultValue = "") String id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MongoUserDetails user = (MongoUserDetails)authentication.getPrincipal();
        long userId = user.getUser().getId();
        long productId = Long.parseLong(id);

        long basketId = basketRepository.findByUserIdAndProductId(userId, productId).getId();
        basketRepository.deleteById(basketId);

        return "redirect:/userPage";
    }
}
