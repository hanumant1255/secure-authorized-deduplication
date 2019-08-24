package com.hybrid.cloud.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hybrid.cloud.models.User;
import com.hybrid.cloud.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/registration")
	public String registration(Map<String, Object> model, @RequestBody User userForm) {
		userService.save(userForm);
		return "redirect:/welcome";
	}

	@PostMapping("/login")
	public String login(Map<String, Object> model, @RequestBody User userForm) {
		Boolean b = userService.validate(userForm);
		return "redirect:/home";
	}

	@GetMapping({ "/", "/home" })
	public String welcome(Map<String, Object> model) {
		model.put("message", "haumant");
		return "welcome";
	}
}