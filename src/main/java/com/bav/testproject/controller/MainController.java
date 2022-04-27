package com.bav.testproject.controller;

import com.bav.testproject.repository.BasketRepository;
import com.bav.testproject.repository.CustomBasketRepository;
import com.bav.testproject.entity.ProductOnBasket;
import com.bav.testproject.entity.Product;
import com.bav.testproject.form.ProductForm;
import com.bav.testproject.repository.ProductRepository;
import com.bav.testproject.repository.ProductRepositoryCustom;
import com.bav.testproject.MongoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private CustomBasketRepository customBasketRepository;

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

    //Страница товара
    @RequestMapping(value = { "/productPage"}, method = RequestMethod.GET)
    public String productPage(@RequestParam(name = "id", defaultValue = "") String id,
                              Model model,
                              HttpServletRequest request) {
        Product product = this.productRepository.findById(Long.parseLong(id));
        model.addAttribute("product", product);

        //Отображение кнопки "В корзину"
        String toBasketText = "";
        //Если пользователь вошел в систему - отображаем кнопку
        if (request.getUserPrincipal() != null)
            toBasketText = "В корзину";
        model.addAttribute("toBasketText", toBasketText);
        return "productPage";
    }

    //Страница товара - добавление в корзину
    @RequestMapping(value = { "/productPage"}, method = RequestMethod.POST)
    public String productPagePost(@RequestParam(name = "addProduct", defaultValue = "") String id,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MongoUserDetails user = (MongoUserDetails)authentication.getPrincipal();
        long userId = user.getUser().getId();
        long productId = Long.parseLong(id);

        ProductOnBasket product = new ProductOnBasket();
        product.setId(customBasketRepository.getMaxId() + 1);
        product.setUserId(userId);
        product.setProductId(productId);

        //Проверка на наличие товара в корзине: если товар отсутствует - добавляем его в БД
        if (!customBasketRepository.checkProductOnBasket(userId, productId))
            basketRepository.insert(product);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/productPage";
    }

    //Переход на страницу добавления объекта в БД
    @RequestMapping(value = { "/addProduct" }, method = RequestMethod.GET)
    public String showAddProductPage(Model model) {
        ProductForm productForm = new ProductForm();
        model.addAttribute("productForm", productForm);
        return "addProduct";
    }

    //Добавление объекта в БД
    @RequestMapping(value = { "/addProduct" }, method = RequestMethod.POST)
    public String saveProduct(Model model, //
                             @ModelAttribute("productForm") ProductForm productForm) {
        String title = productForm.getTitle();
        String info = productForm.getInfo();
        String characteristics = productForm.getCharacteristics();
        String cost = productForm.getCost();
        String tags = productForm.getTags();

        //Проверка на заполненность полей
        if (title != "" && info != "" && characteristics != "" && cost != "" && tags != ""){
            Product product;
            long id = this.productRepositoryCustom.getMaxId() + 1;

            product = new Product();
            product.setId(id);
            product.setTitle(title);
            product.setInfo(info);
            product.setCharacteristics(characteristics.split(";"));
            product.setCost(cost);
            product.setTags(tags.split(";"));

            this.productRepository.insert(product);

            return "redirect:/adminPanel";
        }

        model.addAttribute("errorMessage", "ERROR");
        return "addProduct";
    }

    //Загрузка панели администратора
    @RequestMapping(value = { "/adminPanel" }, method = RequestMethod.GET)
    public String adminPanelGet(Model model) {
        //Вывод всех товаров в виде таблицы
        List<Product> products = this.productRepository.findAll();
        model.addAttribute("products", products);
        return "adminPanel";
    }

    //Страница удаления товара
    @RequestMapping(value = { "/deletePage" }, method = RequestMethod.GET)
    public String deletePage(@RequestParam(name = "id", defaultValue = "") long id, Model model) {
        model.addAttribute("id", id);
        return "deletePage";
    }

    //Удаление или отмена удаления товара
    @RequestMapping(value = { "/deletePage" }, method = RequestMethod.POST)
    public String deletePagePost(@RequestParam(name = "yes", defaultValue = "") String yes) {
        try {
            if (!yes.equals("")){
                System.out.println("DELETE ID: " + yes);
                this.productRepository.deleteById(Long.parseLong(yes));
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "redirect:/adminPanel";
    }

    //Страница login
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

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
