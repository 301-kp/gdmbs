package cn.guet.gdmbs.service;


import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.util.PageModel;

import java.util.List;

public interface IStudentService {
    Student verifyLogin(String studentId, String password) throws DaoException;
    PageModel<Teacher> getAllTeacher(String studentId, int pageNum, int pageSize) throws DaoException;//查询教师列表
    Integer totalSelect(String teacherId);//不需要异常处理
    List<Teacher> getTeacher(String teacherName) throws DaoException;
    void selectTeachers(String teacherId, String studentId) throws DaoException;
    List<Teacher> getSelectTea(String studentId) throws DaoException;
    void deleteTeachers(String studentId) throws DaoException;
    String changePasswords(String studentId, String oldPassword, String newPassword) throws DaoException;
    String getInfo(String teacherId);
    String getPasswordByStu(String studentId) throws DaoException;
    Integer getNum(String teacherId, String status);
    String getStuStatus(String studentId) throws DaoException;
    String getStuNote(String studentId) throws DaoException;
    Integer totalTeacher();
    String getTeacherId(String studentId);
    void changeInfo(String phone, String studentId) throws DaoException;
    String showInfo(String inform_obj);
    String getSelectTeacher(String studentId);
    List<Student> listStudentInfo(String studentId);
    Integer totalTeacherByStatus(String status);
    Integer getStudentNum(String teacherId);
}
