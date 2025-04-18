package in.com.student.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.com.student.entity.Admin;
import in.com.student.entity.Studententity;
import in.com.student.service.studentService;
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class studentController {
	@Autowired
	private studentService service;
	
	@Autowired
	private HttpSession session;
	
	
//	@GetMapping("/home")
//	public String home() {
//		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
//	    if (Boolean.TRUE.equals(loggedIn)) {
//	        return "home";
//	    } else {
//	        return "redirect:login";
//	    }
//	}
	
	
	@GetMapping("/addadmin")
    public String addAdminPage(Model model) {
        
            model.addAttribute("admin", new Admin());
            return "addAdmin";
        
    }

    @PostMapping("/registerAdmin")
    public String registerAdmin(@ModelAttribute Admin admin, HttpSession session) {
    	
    	
	    	 service.addAdmin(admin);
	         session.setAttribute("message", "New Admin Added Successfully");
	         return "redirect:dashboard";
	           
    }
	
	
	@GetMapping(value = "/login")
	public String getLoginPage(Model model) {
		Admin user = new Admin();
		model.addAttribute("user",user);
		return "index";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();  // Destroy session
	    return "redirect:login";
	}

	
	@PostMapping(value = "/save")
	public String saveForm(@ModelAttribute Admin user) {
	    Admin tempUser = service.getEmailAndPassword(user.getEmail(), user.getPassword());

	    if (tempUser != null && tempUser.getEmail() != null) {
	    	session.setAttribute("loggedIn", true);
	        session.setAttribute("userName", tempUser.getName());
	        session.setAttribute("userId", tempUser.getEmail());
	        System.out.println("loggedin success");
	        return "redirect:dashboard";
	    } else {
	    	session.setAttribute("message", "invalid cradentials!");
	    	System.out.println("loggedin faild");
	        return "redirect:login";
	    }
	}
	
	
//	@PostMapping("/register")
//	public String studentregister( @ModelAttribute Studententity s) {
//		
//		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
//	    if (Boolean.TRUE.equals(loggedIn)) {
//	    	service.addStudent(s);
//			session.setAttribute("message", "New Student Added Successfully");
//			return "redirect:dashboard";
//	    } else {
//	        return "redirect:login";
//	    }
//	}
	
	@PostMapping("/register")
    public String addStudent(@ModelAttribute Studententity student, Model model) {
		
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	    	try {
	            service.addStudent(student, session);
	            return "redirect:/addstudent"; // Redirect to student list after successful addition
	        } catch (Exception e) {
	            model.addAttribute("errorMessage", "Student with the same email already exists!");
	            return "errorPage"; // Redirect to a custom error page
	        }
	    } else {
	        return "redirect:login";
	    }
		
        
    }
	
	
	@GetMapping("/addstudent")
	public String addStudentPage() {
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	        return "addStudent";
	    } else {
	        return "redirect:login";
	    }
		
	}
	
	
	@GetMapping("/viewstudent")
	public String viewStudentPage(Model model) {
	    Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	        String adminEmail = (String) session.getAttribute("userId");
	        Admin admin = service.findAdminByEmail(adminEmail);
	        List<Studententity> students = service.getStudentsByAdmin(admin);
	        model.addAttribute("s", students);
	        return "view";
	    } else {
	        return "redirect:login";
	    }
	}

//	@GetMapping("/viewstudent")
//	public String viewStudentPage(Model model ) {
//		
//		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
//	    if (Boolean.TRUE.equals(loggedIn)) {
//	    	List<Studententity> std= service.viewStudent();
//			model.addAttribute("s",std);
//			return "view";
//	    } else {
//	        return "redirect:login";
//	    }
//		
//		
//	}
	
//	@GetMapping("/editstudent/viewstudent")
//	public String editeditstudentViewstudentiewStudentPage(Model model ) {
//		List<Studententity> std= service.viewStudent();
//		model.addAttribute("s",std);
//		return "view";
//	}
	
	
	@GetMapping("/editstudent/{id}")
	public String edit(@PathVariable int id,Model model) {
		
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	    	Studententity s = service.editStudent(id);
			model.addAttribute("s",s);
			return "editstudent";
	    } else {
	        return "redirect:login";
	    }
		
		
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute Studententity e,Model model, HttpSession session) {
		
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	    	String adminEmail = (String)session.getAttribute("userId");
	    	Admin admin = service.findAdminByEmail(adminEmail);
	    	service.updateStudent(e,admin);
			 session.setAttribute("message", "Successfully Updated !");

			return "redirect:/viewstudent";
	    } else {
	        return "redirect:login";
	    }
		
		
	}
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable int id, HttpSession session) {
		
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	    	service.deleteStudent(id);
			session.setAttribute("message", "Successfully Deleted !");
			return "redirect:/viewstudent";
	    } else {
	        return "redirect:login";
	    }
	}
	
	
	@GetMapping("student/delete/{id}")
	public String deleteStudent(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		
		
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	    	service.deleteStudent(id);
		    redirectAttributes.addFlashAttribute("message", "Student deleted successfully!");
		    return "redirect:/searchstudent"; 
	    } else {
	        return "redirect:login";
	    }
		
		
	    
	}
	
	
	@GetMapping("/searchstudent")
	public String searchStudentPage() {
		
		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	    	 return "searchStudent";  
	    } else {
	        return "redirect:login";
	    }
		
	   
	}
	
	
	
	@GetMapping("/findstudent")
	public String findStudent(@RequestParam("email") String email, Model model) {
	    Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	        String adminEmail = (String) session.getAttribute("userId");
	        Admin admin = service.findAdminByEmail(adminEmail);

	        Studententity student = service.findByEmailAndAdmin(email, admin);
	        if (student == null) {
	            model.addAttribute("message", "Student not found or not assigned to you!");
	        } else {
	            model.addAttribute("student", student);
	        }
	        return "searchStudent";  
	    } else {
	        return "redirect:login";
	    }
	}

	
	

//	@GetMapping("/findstudent")
//	public String findStudent(@RequestParam("email") String email, Model model) {
//		
//		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
//	    if (Boolean.TRUE.equals(loggedIn)) {
//	    	Studententity student = service.findByEmail(email);
//		    if (student == null) {
//		        model.addAttribute("message", "Student not found!");
//		    } else {
//		        model.addAttribute("student", student);
//		    }
//		    return "searchStudent";  
//	    } else {
//	        return "redirect:login";
//	    }
//	}
	
	
//	@GetMapping("/dashboard")
//	public String dashboard(Model model) {
//	    Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
//	    if (Boolean.TRUE.equals(loggedIn)) {
//	        String adminEmail = (String) session.getAttribute("userId");
//	        Admin admin = service.findAdminByEmail(adminEmail);
//	        long totalStudents = service.countStudentsByAdmin(admin);
//	        long maleCount = service.countByGenderAndAdmin("Male", admin);
//	        long femaleCount = service.countByGenderAndAdmin("Female", admin);
//	        model.addAttribute("totalStudents", totalStudents);
//	        model.addAttribute("maleCount", maleCount);
//	        model.addAttribute("femaleCount", femaleCount);
//	        return "dashboard";
//	    } else {
//	        return "redirect:login";
//	    }
//	}
	
	@GetMapping("/dashboard")
	public String dashboard(Model model) throws JsonProcessingException {
	    Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
	    if (Boolean.TRUE.equals(loggedIn)) {
	        String adminEmail = (String) session.getAttribute("userId");
	        Admin admin = service.findAdminByEmail(adminEmail);

	        long totalStudents = service.countStudentsByAdmin(admin);
	        long maleCount = service.countByGenderAndAdmin("Male", admin);
	        long femaleCount = service.countByGenderAndAdmin("Female", admin);

	        Map<String, Long> courseData = service.countStudentsByCourseForAdmin(admin);
	        List<String> courseLabels = new ArrayList<>(courseData.keySet());
	        List<Long> courseCounts = new ArrayList<>(courseData.values());

	        ObjectMapper objectMapper = new ObjectMapper();
	        String courseLabelsJson = objectMapper.writeValueAsString(courseLabels);
	        String courseCountsJson = objectMapper.writeValueAsString(courseCounts);

	        model.addAttribute("totalStudents", totalStudents);
	        model.addAttribute("maleCount", maleCount);
	        model.addAttribute("femaleCount", femaleCount);
	        model.addAttribute("courseLabels", courseLabelsJson);
	        model.addAttribute("courseCounts", courseCountsJson);

	        return "dashboard";
	    } else {
	        return "redirect:login";
	    }
	}


	
	
	
//	@GetMapping("/dashboard")
//	public String dashboard(Model model) throws JsonProcessingException {
//		
//		Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
//	    if (Boolean.TRUE.equals(loggedIn)) {
//	    	 long totalStudents = service.countStudents();
//	 	    long maleCount = service.countByGender("Male");
//	 	    long femaleCount = service.countByGender("Female");
//
//	 	    Map<String, Long> courseData = service.countStudentsByCourse();
//	 	    List<String> courseLabels = new ArrayList<>(courseData.keySet());
//	 	    List<Long> courseCounts = new ArrayList<>(courseData.values());
//
//	 	    ObjectMapper objectMapper = new ObjectMapper();
//	 	    String courseLabelsJson = objectMapper.writeValueAsString(courseLabels);
//	 	    String courseCountsJson = objectMapper.writeValueAsString(courseCounts);
//
//	 	    model.addAttribute("totalStudents", totalStudents);
//	 	    model.addAttribute("maleCount", maleCount);
//	 	    model.addAttribute("femaleCount", femaleCount);
//	 	    model.addAttribute("courseLabels", courseLabelsJson);
//	 	    model.addAttribute("courseCounts", courseCountsJson);
//
//	 	    return "dashboard";  
//	    } else {
//	        return "redirect:login";
//	    }
//	}


	
	
	
}

