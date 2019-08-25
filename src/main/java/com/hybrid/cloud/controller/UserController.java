package com.hybrid.cloud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;
import com.hybrid.cloud.service.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public String registration(Model model, @ModelAttribute("user") User userForm) {		
		try {
			userService.save(userForm);
			return "welcome";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());

		}
		return "error";

	}

	@PostMapping("/login")
	public String login(Model model,@ModelAttribute("user") User userForm) {	
		User user;
		try {
			user = userService.validate(userForm);
			if(user!=null) {
				List<FileMetadata> fileList=userService.getFiles(user);
				model.addAttribute("user", user);
				model.addAttribute("fileList", fileList);
				return "home";
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", e.getMessage());

		}
			return "error";
	}

	@GetMapping({ "/", "/home" })
	public String welcome(Model model) {
		return "welcome";
	}
}