package cn.guet.gdmbs.service;

import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;

import java.util.List;

public interface ITeacherService {
    Teacher teacherLogin(String teacherId, String password);
    String changePasswd(String teacherid, String newpasswd, String oldpasswd) throws DaoException;
    void lock(String teacherid) throws DaoException;
    void deleteStudent(String studentId) throws DaoException;
    List<Student> listStudent(String teacherId)throws DaoException;
    String getStatus(String teacherid);
    Integer getNum(String teacherId, String status);
    Teacher getInfo(String teacherId);
    void updateInfo(String teacherId, String info) throws DaoException;
    void addBlackList(String studentId, String teacherId) throws DaoException;
    void selectStudent(String teacherId, String studentId) throws DaoException;
    List<String> blackListStudentId(String teacherId);
    String showInfo(String inform_obj);
    List<String>  getIdToR();
    String yx_getRandomId(String studentId, List<String> list);//选课人数为0 优先分配
    String qz_getRandomId(String studentId);//按权重分配
    void selectTeachers(String teacherId, String studentId) throws DaoException;
    void updatephoneNumber(String phoneNumber, String teacherId) throws DaoException;
}
