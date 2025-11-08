package eventhub.main.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import eventhub.main.domain.EHUser;
import eventhub.main.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new EHUser());
        return "signup";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") EHUser user, 
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        // Check for validation errors
        if (result.hasErrors()) {
            return "signup";
        }
        
        // Check if username already exists
        if (userService.usernameExists(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            return "signup";
        }
        
        // Check if email already exists
        if (userService.emailExists(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            return "signup";
        }
        
        try {
            // Register the user
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Registration successful! Please log in with your credentials.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed. Please try again.");
            return "signup";
        }
    }
}