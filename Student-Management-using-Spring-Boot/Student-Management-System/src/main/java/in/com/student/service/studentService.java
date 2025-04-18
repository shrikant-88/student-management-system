package in.com.student.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.com.student.entity.Admin;
import in.com.student.entity.Studententity;
import in.com.student.repository.AdminRepo;
import in.com.student.repository.studentRepo;
import jakarta.servlet.http.HttpSession;

@Service
public class studentService {

	@Autowired
	private studentRepo repo;
	@Autowired
	private AdminRepo adminRepo;
	
	
	public void addStudent(Studententity student, HttpSession session)throws Exception {
	    String adminEmail = (String) session.getAttribute("userId"); 
	    Admin admin = adminRepo.findByEmail(adminEmail); 
	    if (admin != null) {
	    	if (repo.existsByEmail(student.getEmail())) {
	            throw new Exception("Student with this email already exists");
	        }else {
	        	student.setAdmin(admin);
		        repo.save(student);
	        }
	        
	    } else {
	        throw new RuntimeException("Admin not found!");
	    }
	}
	
	
//	public void addStudent(Studententity student) throws Exception {
//        if (repo.existsByEmail(student.getEmail())) {
//            throw new Exception("Student with this email already exists");
//        }
//        repo.save(student);
//    }
	
	public void updateStudent(Studententity student,Admin admin) {
        student.setAdmin(admin);
        repo.save(student);
    }
	
	
	
	public List<Studententity> viewStudent() {
		return repo.findAll();
	}
	
	public Studententity editStudent(int id){
		Optional<Studententity> e = repo.findById(id);
		if (e.isPresent()) {
			return e.get();
		}
		return null;
	}
	
	public void deleteStudent(int id) {
		repo.deleteById(id);
	}
	
//	public Studententity findByEmail(String email) {
//	    return repo.findByEmail(email);
//	}

	public Studententity findByEmailAndAdmin(String email, Admin admin) {
	    return repo.findByEmailAndAdmin(email, admin);
	}



	public Admin getEmailAndPassword(String email, String password) {
		// TODO Auto-generated method stub
		return adminRepo.findByEmailAndPassword(email, password);
	}


	public void addAdmin(Admin admin) {
		// TODO Auto-generated method stub
		adminRepo.save(admin);
		
	}
	
	
	public long countStudents() {
	    return repo.count();
	}

	public long countByGender(String gender) {
	    return repo.countByGender(gender);
	}
	public Admin findAdminByEmail(String adminEmail) {
	    return adminRepo.findByEmail(adminEmail);
	}


	public List<Studententity> getStudentsByAdmin(Admin admin) {
	    return repo.findByAdmin(admin);
	}



	public long countStudentsByAdmin(Admin admin) {
	    return repo.countStudentsByAdmin(admin);
	}

	public long countByGenderAndAdmin(String gender, Admin admin) {
	    return repo.countByGenderAndAdmin(gender, admin);
	}

	public Map<String, Long> countStudentsByCourseForAdmin(Admin admin) {
	    List<Object[]> results = repo.countStudentsByCourseForAdmin(admin);
	    Map<String, Long> courseCounts = new HashMap<>();
	    for (Object[] result : results) {
	        courseCounts.put((String) result[0], (Long) result[1]);
	    }
	    return courseCounts;
	}

	
	
	
}
