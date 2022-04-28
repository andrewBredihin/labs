package com.bav.testproject.controller;

import com.bav.testproject.entity.Product;
import com.bav.testproject.form.ProductForm;
import com.bav.testproject.repository.ProductRepository;
import com.bav.testproject.repository.ProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductRepositoryCustom productRepositoryCustom;

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
}
