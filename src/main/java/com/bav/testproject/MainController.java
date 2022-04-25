package com.bav.testproject;

import com.bav.testproject.database.Product;
import com.bav.testproject.database.ProductForm;
import com.bav.testproject.database.ProductRepository;
import com.bav.testproject.database.ProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SpringBootTest
@Controller
public class MainController {

    //Интерфейсы с запросами к БД
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductRepositoryCustom productRepositoryCustom;

    //Загрузка стартовой страницы - отображение всех объектов из БД
    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) {
        List<Product> products = this.productRepository.findAll();

        model.addAttribute("products", products);

        String username = "Вход";
        String href = "/login";
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
    public String productPage(@RequestParam(name = "id", defaultValue = "") String id, Model model) {
        Product product = this.productRepository.findById(Long.parseLong(id));
        model.addAttribute("product", product);
        return "productPage";
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
        return "userPage";
    }
}
