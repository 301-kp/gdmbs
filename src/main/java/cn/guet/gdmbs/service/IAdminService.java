package cn.guet.gdmbs.service;

import cn.guet.gdmbs.entity.Admin;
import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.util.PageModel;

import java.util.List;
import java.util.Map;

public interface IAdminService {
    void addTeacher(Teacher teacher) throws DaoException;
    void deleteChildStuByTeacher(String teacherId);
    Admin adminLogin(String adminId, String password);
    void deleteTeacher(String teacherId) throws DaoException;
    void resetStuPassword(String studentId) throws DaoException;
    void resetTeaPassword(String teacherId) throws DaoException;
    void updateTeaNameNum(Teacher teacher) throws DaoException;
    List<Student> selectStatusByStu(String departmentId, int pageNum, int pageSize);
    List<Student> selectStudentBySta(String status, int pageNum, int pageSize);
    List<Teacher> listTeacher(int pageNum, int pageSize, String... status);
    Integer totalStudent(String major);
    Integer totalStudentByStatus(String status);
    PageModel<Teacher> getAllTeacher(int pageNum, int pageSize, String... status);
    PageModel<Student> getAllStudent(String departmentId, int pageNum, int pageSize);
    PageModel<Student> getAllStudentByStatus(String status, int pageNum, int pageSize);
    void addStudent(Student student) throws DaoException;
    void cancleConfirm(String teacherId) throws DaoException;
    String getNameById(String departmentId);
    List<Student> getStudent(String studentId);
    List<Teacher> getTeacher(String teacherId);
    void updateStu(String new_studentId, String new_studentName, String old_studentId) throws DaoException;
    void addInform(String content, String inform_obj) throws DaoException;
    List<Student> getStudentByName(String studentName);
    List<Student> getStudentByTeaName(String teacherName);
    String getTeacherName(String teacherId);
    String changePasswords(String adminid, String oldPassword, String newPassword) throws DaoException;
    void deleteData() throws DaoException;
    Map<String,String> showInfo();
    void deleteTeachers(String studentId) throws DaoException;
    void deleteStudent(String studentId);
}
