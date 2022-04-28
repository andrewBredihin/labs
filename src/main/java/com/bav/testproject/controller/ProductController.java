package com.bav.testproject.controller;

import com.bav.testproject.MongoUserDetails;
import com.bav.testproject.entity.Comment;
import com.bav.testproject.entity.Product;
import com.bav.testproject.entity.ProductOnBasket;
import com.bav.testproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private BasketRepositoryCustom basketRepositoryCustom;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentRepositoryCustom customCommentRepository;

    //Страница товара
    @RequestMapping(value = { "/productPage"}, method = RequestMethod.GET)
    public String productPage(@RequestParam(name = "id", defaultValue = "") long id,
                              Model model,
                              HttpServletRequest request) {
        Product product = this.productRepository.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("productId", product.getId());

        //Отображение кнопки "В корзину"
        String toBasketText = "";
        //Если пользователь вошел в систему - отображаем кнопку
        if (request.getUserPrincipal() != null)
            toBasketText = "В корзину";
        model.addAttribute("toBasketText", toBasketText);

        //Отображение комментариев у товара
        List<Comment> comments = commentRepository.findAllByProductId(id);
        List<CommentAndUser> list = new ArrayList<>();
        for (Comment x : comments){
            list.add(new CommentAndUser(x.getMessage(), userRepository.findById(x.getUserId()).getUsername()));
        }
        model.addAttribute("comments", list);
        return "productPage";
    }

    //Страница товара - добавление в корзину
    @RequestMapping(value = { "/productPage"}, method = RequestMethod.POST)
    public String productPagePost(@RequestParam(name = "addProduct", defaultValue = "") String id,
                                  RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MongoUserDetails user = (MongoUserDetails)authentication.getPrincipal();
        long userId = user.getUser().getId();
        long productId = Long.parseLong(id);

        ProductOnBasket product = new ProductOnBasket();
        product.setId(basketRepositoryCustom.getMaxId() + 1);
        product.setUserId(userId);
        product.setProductId(productId);

        //Проверка на наличие товара в корзине: если товар отсутствует - добавляем его в БД
        if (!basketRepositoryCustom.checkProductOnBasket(userId, productId))
            basketRepository.insert(product);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/productPage";
    }

    //Страница товара - добавление в корзину
    @RequestMapping(value = { "/productPageNewMessage"}, method = RequestMethod.POST)
    public String productPageNewMessage(@RequestParam(name = "productId", defaultValue = "") long id,
                                        @RequestParam(name = "message", defaultValue = "") String message,
                                        RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MongoUserDetails user = (MongoUserDetails)authentication.getPrincipal();

        Comment comment = new Comment();
        comment.setId(customCommentRepository.getMaxId() + 1);
        comment.setUserId(user.getUser().getId());
        comment.setProductId(id);
        comment.setMessage(message);
        commentRepository.insert(comment);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/productPage";
    }

    private class CommentAndUser{
        private String message;
        private String username;

        public CommentAndUser(String message, String username){
            this.message = message;
            this.username = username;
        }

        public String getMessage() {
            return message;
        }

        public String getUsername() {
            return username;
        }
    }
}
