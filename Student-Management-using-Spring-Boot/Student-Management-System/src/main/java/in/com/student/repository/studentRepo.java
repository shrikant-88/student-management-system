package in.com.student.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.com.student.entity.Admin;
import in.com.student.entity.Studententity;
@Repository
public interface studentRepo extends JpaRepository<Studententity,Integer> {
	
	Studententity findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	long countByGender(String gender);
    
    @Query("SELECT s FROM Studententity s WHERE s.admin = :admin")
    List<Studententity> findByAdmin(Admin admin);

    @Query("SELECT COUNT(s) FROM Studententity s WHERE s.admin = :admin")
    long countStudentsByAdmin(Admin admin);

    @Query("SELECT COUNT(s) FROM Studententity s WHERE s.admin = :admin AND s.gender = :gender")
    long countByGenderAndAdmin(String gender, Admin admin);

    @Query("SELECT s.course, COUNT(s) FROM Studententity s WHERE s.admin = :admin GROUP BY s.course")
    List<Object[]> countStudentsByCourseForAdmin(Admin admin);

    @Query("SELECT s FROM Studententity s WHERE s.email = :email AND s.admin = :admin")
    Studententity findByEmailAndAdmin(@Param("email") String email, @Param("admin") Admin admin);

}
