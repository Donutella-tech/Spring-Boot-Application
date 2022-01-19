package com.example.sweater.controller;

import com.example.sweater.domain.Car;
import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;

    @GetMapping
    public String UserList(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
                return "userEdit";
    }


    @PostMapping
    public String userSave(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam Map<String, String> form,
                            @RequestParam("userId") User user

    ) {
        user.setPassword(password);
        user.setUsername(username);
      Set<String> roles=Arrays.stream(Role.values())
              .map(Role::name)
              .collect(Collectors.toSet());
      user.getRoles().clear();

      for(String key: form.keySet()) {

          if(roles.contains(key)){

              user.getRoles().add(Role.valueOf(key));
      }
      }
        userRepo.save(user);
        return "redirect:/user";

    }
    @GetMapping("/{id}/delete")
    public String userDelete(@PathVariable(value = "id")Long id)

     {
         User user=userRepo.findById(id).orElseThrow();
         userRepo.delete(user);

         return "redirect:/user";
    }


  /*  public String UserSettings(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "userSettings";

    }*/



}
