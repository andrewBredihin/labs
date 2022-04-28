package com.bav.testproject.controller;

import com.bav.testproject.entity.Product;
import com.bav.testproject.repository.ProductRepository;
import com.bav.testproject.repository.ProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRepositoryCustom productRepositoryCustom;

    //Загрузка стартовой страницы
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        //Отображение всех объектов из БД
        List<Product> products = this.productRepository.findAll();
        model.addAttribute("products", products);

        //Вход или переход на страницу пользователя
        String username = "Вход";
        String href = "/login";

        //Если пользователь вошел в систему - перенаправляем на страницу пользователя
        if (request.getUserPrincipal() != null){
            username = request.getUserPrincipal().getName();
            href = "/userPage";
        }
        model.addAttribute("user", username);
        model.addAttribute("href", href);
        return "index";
    }

    //Переход на страницу с поиском объектов по тегу, с передачей значения ля поиска в поле ввода
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.POST)
    public String indexPost(@RequestParam(name = "search", defaultValue = "test") String search,
                            RedirectAttributes redirectAttributes, Model model) {
        redirectAttributes.addAttribute("search", search);
        return "redirect:/productList";
    }

    //Загрузка страницы отображения объектов по тегу
    @RequestMapping(value = { "/productList" }, method = RequestMethod.GET)
    public String productList(@RequestParam(name = "search", defaultValue = "") String search, Model model) {
        List<Product> products = this.productRepositoryCustom.findByTags(search);
        model.addAttribute("products", products);
        return "productList";
    }
}
