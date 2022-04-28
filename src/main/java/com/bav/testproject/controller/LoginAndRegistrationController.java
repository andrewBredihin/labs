package com.bav.testproject.controller;

import com.bav.testproject.form.UserForm;
import com.bav.testproject.entity.User;
import com.bav.testproject.repository.UserRepositoryCustom;
import com.bav.testproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//Контроллер страницы регистрации
@Controller
public class LoginAndRegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryCustom customUserRepository;

    private static final String REGISTRATION = "registration";

    //Страница login
    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    //Создание и заполнение формы пользователя
    @RequestMapping(value = { "/registration" }, method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new UserForm());

        return REGISTRATION;
    }

    //Создание нового пользователя
    @RequestMapping(value = { "/registration"}, method = RequestMethod.POST)
    public String addUser(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return REGISTRATION;
        }
        //Проверяем введенный пароль и его подтверждение
        if (!userForm.checkPasswordConfirm()){
            model.addAttribute("errorMessage", "Пароли не совпадают");
            return REGISTRATION;
        }
        MyMessage message = saveUser(userForm);
        if (!message.isaBoolean()){
            model.addAttribute("errorMessage", message.getMessage());
            return REGISTRATION;
        }

        return "redirect:/";
    }

    //Метод сохранения пользователя
    private MyMessage saveUser(UserForm userForm){
        //Проверяем на наличие пользователей с введенными именем и почтой,если таких нет - сохраняем нового пользователя
        if (userRepository.findByUsername(userForm.getUsername()) != null)
            return new MyMessage(false, "Пользователь с таким именем уже существует");
        else if (userRepository.findByEmail(userForm.getEmail()) != null)
            return new MyMessage(false, "Пользователь с такой почтой уже существует");

        User user = new User();
        user.setId(customUserRepository.getMaxId() + 1);
        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPassword("{noop}" + userForm.getPassword());
        user.setAuthorities(userForm.getAuthorities());

        userRepository.save(user);
        return new MyMessage(true, "Ok");
    }

    private class MyMessage{
        private String message;
        private boolean aBoolean;

        protected MyMessage(boolean aBoolean, String message){
            this.aBoolean = aBoolean;
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public boolean isaBoolean() {
            return aBoolean;
        }
    }
}
