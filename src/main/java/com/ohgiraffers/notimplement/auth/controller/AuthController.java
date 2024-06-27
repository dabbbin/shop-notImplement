package com.ohgiraffers.notimplement.auth.controller;

import com.ohgiraffers.notimplement.auth.model.dto.UserDTO;
import com.ohgiraffers.notimplement.auth.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/index.html";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/index.html";
    }

    @GetMapping("/adminMain")
    public String adminMain() {
        return "adminPage/main";
    }

    @GetMapping("/userMain")
    public String userMain() {
        return "userPage/main";
    }

    @PostMapping("main")
    public String login1(HttpSession session,
                         HttpServletRequest request,
                        @RequestParam(value = "id", required = false) String inId,
                        @RequestParam(value = "password", required = false) String inPassword) {

        session.setAttribute("id", inId);
        session.setAttribute("password", inPassword);
//        session = request.getSession();
//        String userId = (String) session.getAttribute("id");

        if("1".equals(inId) && "1".equals(inPassword)) {
            return "redirect:/auth/adminMain";
        }
        else if(userService.login(inId, inPassword)) {
            return "redirect:/auth/userMain";
        } else {
            return "redirect:/main/index.html";
        }
    }

    @GetMapping("/signup")
    public String signup() {
        return "userPage/auth/signup";
    }

    @PostMapping("/result")
    public String result() {
        return "userPage/auth/result";
    }

    @PostMapping("/signup")
//    @ResponseBody
    public String signup(@ModelAttribute UserDTO user,
                         RedirectAttributes rttr,
//                         @RequestParam String id,
                         Model model) {
        userService.signup(user);
        log.info("user = {}", user);
//        if (!userService.findUserById(id)) {
//            userService.signup(user);
//        } else {
//            result();
//        }
        rttr.addFlashAttribute("message", "가입성공?? ");
        return "redirect:/auth/login";
    }

    @GetMapping("userList")
    public String findAllUser(Model model) {

        List<UserDTO> userList = userService.findAllUser();
        model.addAttribute("userList", userList);

        return "adminPage/auth/userList";
    }

    @PostMapping("checkId")
    @ResponseBody
    public String checkId(@RequestParam String id){
        String message = "사용 가능한 아이디입니다. ";
        if (!userService.findUserById(id)) {
            message = id + "는 이미 있는 아이디 값입니다. 다른 아이디를 사용해주세요. ";
            return message;
        }

        return message;
    }



}
