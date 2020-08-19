package cn.guet.gdmbs.mapper;

import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TeacherMapper {
    Integer getStudentNum(@Param("teacherId") String teacherId);
    List<String> listTeacherNotLock();
    List<Teacher> selectTeacher(@Param("teacherName") String teacherName) throws DataAccessException;
    Integer totalTeacherByStatus(@Param("status") String status);
    Integer totalTeacher();
    List<Teacher> listTeacher(@Param("teacherId") String teacherId) throws DataAccessException;
    String getTeacherName(@Param("teacherId") String teacherId);
    void deleteTea() throws DataAccessException;
    List<Teacher> getTeacher(@Param("teacherId") String teacherId);
    void cancleConfirm(@Param("teacherId") String teacherId) throws DaoException;
    List<Teacher> listTeacherByStstua(@Param("status") String status);
    List<Teacher> listTeacherAll();
    void updateTeaNameNum(Teacher teacher) throws DataAccessException;
    void resetTeaPassword(@Param("teacherId") String teacherId) throws DaoException;
    void deleteTeacher(@Param("teacherId") String teacherId);
    void addTeacher(Teacher teacher) throws DataAccessException;
    Teacher teacherLogin(@Param("teacherId") String teacherId, @Param("password") String password);
    void changePasswd(@Param("teacherId") String teacherid, @Param("newpassword") String newpasswd) throws DataAccessException;
    void lock(@Param("teacherId") String teacherid) throws DataAccessException;
    String getStatus(@Param("teacherId") String teacherid);
    Teacher getOneTeacher(@Param("teacherId") String teacherId);
    void updateInfo(@Param("info") String info, @Param("teacherId") String teacherId);
    void updatephoneNumber(@Param("phoneNumber") String phoneNumber, @Param("teacherId") String teacherId) throws DataAccessException;
}
