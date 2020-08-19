package cn.guet.gdmbs.mapper;


import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.exception.DaoException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StudentMapper {
    void selectStudent(@Param("teacherId") String teacherId, @Param("studentId") String studentId);
    List<String> blackListStudentId(@Param("teacherId") String teacherId);
    List<Student> listStudent(@Param("teacherId") String teacherId) throws DataAccessException;
    void deleteStudent(@Param("studentId") String studentId) throws DataAccessException;
    void deleteOneStudent(@Param("studentId") String studentId);
    void deleteStu() throws  DataAccessException;
    void updateStu(@Param("new_studentId") String new_studentId, @Param("new_studentName") String new_studentName, @Param("old_studentId") String old_studentId);
    List<Student> getStudentByName(@Param("studentName") String studentName);
    void cancleConfirmStu(@Param("teacherId") String teacherId) throws DaoException;
    void resetStuPassword(@Param("studentId") String studentId) throws DaoException;
    void deleteChildStu(@Param("teacherId") String teacherId);
    void addStudent(Student student) throws DataAccessException;
    Student verifyLogin(@Param("studentId") String studentId, @Param("password") String password)throws DataAccessException;
    Integer totalSelect(@Param("teacherId") String teacherId) throws DataAccessException;
    void selectTeachers(@Param("teacherId") String teacherId, @Param("studentId") String studentId) throws DataAccessException;
    void deleteTeachers(@Param("studentId") String studentId) throws DataAccessException;
    void changePasswords(@Param("password") String password, @Param("studentId") String studentId) throws DataAccessException;
    String getPasswordByStu(@Param("studentId") String studentId) throws DataAccessException;
    String getStuStatus(@Param("studentId") String studentId) throws DaoException;
    String getStuNote(@Param("studentId") String studentId) throws DaoException;
    String getSelectTeacher(@Param("studentId") String studentId);
    String getTeacherId(@Param("studentId") String studentId);
    void changeInfo(@Param("phone") String phone, @Param("studentId") String studentId);
    List<Student> listStudentInfo(@Param("studentId") String studentId);
}

