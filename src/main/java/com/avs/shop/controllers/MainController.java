package com.avs.shop.controllers;

import com.avs.shop.domain.User;
import com.avs.shop.dto.UserDTO;
import com.avs.shop.service.MailSenderService;
import com.avs.shop.service.SessionObjectHolder;
import com.avs.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class MainController {

    private final SessionObjectHolder sessionObjectHolder;
    private final UserService userService;
    private final MailSenderService mailSenderService;

    public MainController(SessionObjectHolder sessionObjectHolder, UserService userService,
                          MailSenderService mailSenderService) {
        this.sessionObjectHolder = sessionObjectHolder;
        this.userService = userService;
        this.mailSenderService = mailSenderService;
    }

    @RequestMapping({"", "/"})
    public String index(Model model, HttpSession httpSession) {
        model.addAttribute("amountClicks", sessionObjectHolder.getAmountClicks());
        if (httpSession.getAttribute("myID") == null) {
            String uuid = UUID.randomUUID().toString();
            httpSession.setAttribute("myID", uuid);
            System.out.println("Generated UUID -> " + uuid);
        }
        model.addAttribute("uuid", httpSession.getAttribute("myID"));
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signIn";
    }

    @PostMapping("/signIn")
    public String saveUser(UserDTO dto, Model model) {
        if (userService.save(dto)) {
            User user = userService.findByName(dto.getUsername());
            if (!dto.getEmail().equals("")) {
                mailSenderService.sendActivateCode(user);
            }
            return "redirect:/login";
        } else {
            model.addAttribute("user", dto);
            return "login";
        }
    }

}
