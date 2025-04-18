package in.com.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.com.student.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin,Integer> {
     Admin findByEmailAndPassword(String email, String password);

	Admin findByEmail(String adminEmail);
}
