package cn.guet.gdmbs.mapper;

import cn.guet.gdmbs.entity.Admin;
import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AdminMapper {
    void addBlackList(@Param("studentId") String studentId, @Param("teacherId") String teacherId);
    Integer getNum(@Param("teacherId") String teacherId, @Param("status") String status);
    List<Teacher> getSelectTea(@Param("studentId") String studentId)throws DataAccessException;
    void deleteBlacklist() throws  DataAccessException;
    List<Student> selectStatusByStu(@Param("departmentId") String departmentId);
    List<Student> selectStudentBySta(@Param("status") String status);
    List<String> getTeaIdByStuFromBlack(@Param("studentId") String studentId);
    Integer totalStudent(@Param("departmentId") String departmentId);
    Integer totalStudentByStatus(@Param("status") String status);
    Admin adminLogin(@Param("adminId") String adminId, @Param("password") String password);
    void addInform(@Param("content") String content, @Param("inform_obj") String inform_obj);
    String getPasswordByStu(@Param("adminId") String adminId);
    void changePasswords(@Param("newPassword") String newPassword, @Param("adminId") String adminId);
    List<Student> getStudentByTeaName(@Param("teacherName") String teacherName);
    String showInfo(@Param("inform_obj") String inform_obj);
}
