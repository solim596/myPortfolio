package com.example.myPortfolio.controller;

import com.example.myPortfolio.service.UserService;
import com.example.myPortfolio.user.UserCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup_form";
        }
        //비밀번호(password1)와 비밀번호 확인(password2)가 같지 않으면 메시지 출력
        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 비밀번호가 일치하지 않습니다.");
            return "user/signup_form";
        }
        // 아이디 중복 검사
        if (userService.checkUsername(userCreateForm.getUsername())) {
            bindingResult.rejectValue("username", "usernameExists", "이미 사용 중인 아이디입니다.");
            return "user/signup_form";  // 중복이 있을 경우 가입 폼을 다시 표시
        }
        //userService의 create메서드 호출
        userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail());
        return "redirect:/";
    }

    // 로그인
    // @RequestParam(value = "매개변수의 값", required = false) : null값도 허용
    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        boolean exists = userService.checkUsername(username);
        if (!exists) {
            model.addAttribute("message", "일치하는 사용자 정보가 없습니다.");
            return "user/login_form";
        }
        return "redirect:/";
    }

    // 아이디 중복 검사
    @GetMapping("/username_check")
    public String checkUsername(@RequestParam("username") String username, Model model) {
        // userService의 checkUsername메서드를 호출해서 site_user테이블에 사용자가 입력한 아이디가 있는지 겁사해서 결과를 exists에 저장(true, false)
        boolean exists = userService.checkUsername(username);
        //
        if (exists) {
            model.addAttribute("message", "이미 사용중인 아이디 입니다.");
        } else {
            model.addAttribute("message", "사용 가능한 아이디 입니다.");
        }
        return "user/username_check";
    }

    // 로그인 할때 사용자 정보 확인
    // @ResponseBody : 자바객체를 HTTP요청의 body로 매핑하여 클라이언트로 전송
    // @RequestBody : HTTP요청의 body를 그대로 서버로 전송
    @GetMapping("/username_exists")
    @ResponseBody
    public boolean checkUsernameExists(@RequestParam("username") String username) {
        return userService.checkUsername(username);
    }

}
