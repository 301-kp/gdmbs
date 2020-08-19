package cn.guet.gdmbs.service.impl;

import cn.guet.gdmbs.entity.Admin;
import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.mapper.AdminMapper;
import cn.guet.gdmbs.mapper.StudentMapper;
import cn.guet.gdmbs.mapper.TeacherMapper;
import cn.guet.gdmbs.service.IAdminService;
import cn.guet.gdmbs.util.Dic;
import cn.guet.gdmbs.util.PageModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("adminService")
public class AdminServiceImpl implements IAdminService {

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public void addTeacher(Teacher teacher) throws DaoException {
        try {

            teacherMapper.addTeacher(teacher);
            //INSERT INTO teacher(teacherId,teacherName,password,studentNum,status,departmentid,title,content,phone) VALUES(#{teacherId},#{teacherName},#{password},#{studentNum},#{status},#{departmentid},#{title},#{content},#{phone})
        } catch (DataAccessException e) {
            throw new DaoException(Dic.SAVE_FAILED);
        }
    }

    @Override
    public void deleteChildStuByTeacher(String teacherId) {
        studentMapper.deleteChildStu(teacherId);
        //UPDATE student SET teacherId='',status='未选课' WHERE teacherId=#{teacherId}
    }

    @Override
    public Admin adminLogin(String adminId, String password) {
        return adminMapper.adminLogin(adminId,password);
        //SELECT * FROM admin WHERE adminid=#{adminId} AND password=#{password}
    }

    @Override
    public void deleteTeacher(String teacherId) throws DaoException {
        try {
            deleteChildStuByTeacher(teacherId);
            teacherMapper.deleteTeacher(teacherId);
            //DELETE FROM teacher WHERE teacherId=#{teacherId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.DELETE_FAILED);
        }
    }

    @Override
    public void resetStuPassword(String studentId) throws DaoException {
        try {
            studentMapper.resetStuPassword(studentId);
            //UPDATE student SET password=#{studentId} WHERE studentId=#{studentId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.RESET_FAILED);
        }
    }

    @Override
    public void resetTeaPassword(String teacherId) throws DaoException {
        try {
            teacherMapper.resetTeaPassword(teacherId);
            //UPDATE teacher SET password=#{teacherId} WHERE teacherId=#{teacherId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.RESET_FAILED);
        }
    }

    @Override
    public void updateTeaNameNum(Teacher teacher) throws DaoException {
        try {
            teacherMapper.updateTeaNameNum(teacher);
        } catch (DataAccessException e) {
            throw new DaoException(Dic.AMEND_FAILED);
        }
    }

    @Override
    public List<Student> selectStatusByStu(String major, int pageNum, int pageSize) {//根据专业查学生列表
        PageHelper.startPage(pageNum, pageSize);
        Page<Student> pageList=( Page<Student>)adminMapper.selectStatusByStu(major);
        return pageList;
    }

    @Override
    public List<Student> selectStudentBySta(String status, int pageNum, int pageSize) {//根据状态查询学生列表
        PageHelper.startPage(pageNum, pageSize);
        Page<Student> pageList=( Page<Student>)adminMapper.selectStudentBySta(status);
        return pageList;
    }

    @Override
    public String getNameById(String departmentId) {
        return departmentTransform(departmentId);//SELECT distinct(departmentName) FROM teacher t,department d WHERE t.departmentId=d.departmentId AND t.departmentId=#{departmentId}
    }

    @Override
    public List<Student> getStudent(String studentId) {
        return studentMapper.listStudentInfo(studentId);
        //select * from student where studentId=#{studentId}
    }

    @Override
    public List<Teacher> getTeacher(String teacherId) {
        return teacherMapper.getTeacher(teacherId);
        //SELECT * FROM teacher WHERE teacherId=#{teacherId}
    }

    @Override
    public void updateStu(String new_studentId, String new_studentName, String old_studentId) throws DaoException {
        try {
            studentMapper.updateStu(new_studentId,new_studentName,old_studentId);
            //UPDATE student SET studentId=#{new_studentId},studentName=#{new_studentName},password=#{new_studentId} WHERE studentId=#{old_studentId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.AMEND_FAILED);
        }
    }

    @Override
    public void addInform(String content, String inform_obj) throws DaoException {
        try {
            adminMapper.addInform(content,inform_obj);
            //UPDATE inform SET inform_content=#{content} WHERE inform_obj=#{inform_obj}
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new DaoException(Dic.SAVE_FAILED);
        }
    }

    @Override
    public List<Student> getStudentByName(String studentName) {
    List<Student> studentList=studentMapper.getStudentByName(studentName);
    //SELECT * FROM student WHERE studentname=#{studentName}
    List<Student> list=new ArrayList<Student>();
        String teacherId="";
        for(Student student:studentList){
            teacherId=student.getTeacher().getTeacherId();
            student.setDepartmentName(departmentTransform(student.getDepartmentid()));
            if(teacherId!=null){
                student.setTeacherName(getTeacherName(teacherId));
            }
            else{
                student.setTeacherName("");
            }
            list.add(student);
        }
        return list;
    }

    @Override
    public List<Student> getStudentByTeaName(String teacherName) {
        List<Student> studentList=adminMapper.getStudentByTeaName(teacherName);
        List<Student> list=new ArrayList<Student>();
        String teacherId="";
        for(Student student:studentList){
            teacherId=student.getTeacher().getTeacherId();
            student.setDepartmentName(departmentTransform(student.getDepartmentid()));
            if(teacherId!=null){
                student.setTeacherName(getTeacherName(teacherId));
            }
            else{
                student.setTeacherName("");
            }
            list.add(student);
        }
        return list;
    }

    @Override
    public String getTeacherName(String teacherId) {
        return teacherMapper.getTeacherName(teacherId);
        //SELECT teacherName FROM teacher WHERE teacherId=#{teacherId}
    }

    @Override
    public String changePasswords(String adminId, String oldPassword, String newPassword) throws DaoException {
        try {
            if(getPasswordByStu(adminId).equals(oldPassword)) {
                adminMapper.changePasswords(newPassword, adminId);
                return "true";
            }else{
                return "false";
            }
        }catch (DataAccessException e) {
            throw new DaoException(Dic.LOGIN_FAILED);
        }
    }

    @Override
    public void deleteData() throws DaoException {
        try{
            adminMapper.deleteBlacklist();
            //DELETE FROM blacklist
            studentMapper.deleteStu();
            //DELETE FROM student
            teacherMapper.deleteTea();
            //DELETE FROM teacher
        }catch (DataAccessException e){
            throw new DaoException(Dic.DELETE_FAILED);
        }

    }

    @Override
    public Map<String,String> showInfo() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("student",adminMapper.showInfo("student"));
        map.put("teacher",adminMapper.showInfo("teacher"));
        //SELECT inform_content FROM inform WHERE inform_obj=#{inform_obj}
        return map;
    }

    private String getPasswordByStu(String adminId) throws DaoException {
        try {
            return adminMapper.getPasswordByStu(adminId);
            //SELECT password FROM admin WHERE adminId=#{adminId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.LOGIN_FAILED);
        }
    }

    @Override
    public List<Teacher> listTeacher(int pageNum, int pageSize,String...status) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Teacher> pageList=null;
        if(status.length!=0){
            pageList=( Page<Teacher>)teacherMapper.listTeacherByStstua(status[0]);
        }else{
            pageList=( Page<Teacher>)teacherMapper.listTeacherAll();
        }
        //SELECT teacherId,teacherName,studentNum,departmentId,title,content,status,surplusNum,phone FROM teacher
        return pageList;
    }

    @Override
    public Integer totalStudent(String departmentId) {
        Integer a=adminMapper.totalStudent(departmentId);
        return (a==null)?0:a;
    }

    @Override
    public Integer totalStudentByStatus(String status) {
        Integer a=adminMapper.totalStudentByStatus(status);
        return (a==null)?0:a;
    }

    @Override
    public PageModel<Teacher> getAllTeacher(int pageNum, int pageSize,String...status) {
        List<Teacher> teacherList=null;
        if(status.length!=0){
            teacherList=listTeacher(pageNum,pageSize,status[0]);
        }else{
            teacherList=listTeacher(pageNum,pageSize);
        }
        List<Teacher> list= new ArrayList<Teacher>();
        String departmentName;
        for(Teacher teacher:teacherList){
            //teacher.setTotal(studentService.totalSelect(teacher.getTeacherId()));
            Integer a=studentMapper.totalSelect(teacher.getTeacherId());
            teacher.setTotal((a==null)?0:a);
            departmentName=departmentTransform(teacher.getDepartmentid());
            teacher.setDepartmentName(departmentName);
            list.add(teacher);
        }
        Integer totalRows=0;
        if(status.length!=0){
            totalRows=teacherMapper.totalTeacherByStatus(status[0]);
        }else{
            totalRows=teacherMapper.totalTeacher();
        }
        totalRows=(totalRows==null)?0:totalRows;
        int totalPage=0;
        totalPage=(totalRows%pageSize==0)?totalRows/pageSize:totalRows/pageSize+1;
        PageModel<Teacher> pm=new PageModel<Teacher>();
        pm.setList(list);
        pm.setCurrentPage(pageNum);
        pm.setTotalPage(totalPage);
        return pm;
    }

    @Override
    public PageModel<Student> getAllStudent(String departmentId, int pageNum, int pageSize) {
        List<Student> teacherList=selectStatusByStu(departmentId,pageNum,pageSize);
        for(Student student:teacherList){
            student.setDepartmentName(departmentTransform(student.getDepartmentid()));
        }
        int totalRows=totalStudent(departmentId);
        int totalPage=(totalRows%pageSize==0)?totalRows/pageSize:totalRows/pageSize+1;
        PageModel<Student> pm=new PageModel<Student>();
        pm.setList(teacherList);
        pm.setCurrentPage(pageNum);
        pm.setTotalPage(totalPage);
        return pm;
    }

    @Override
    public PageModel<Student> getAllStudentByStatus(String status, int pageNum, int pageSize) {
        List<Student> teacherList=selectStudentBySta(status,pageNum,pageSize);
        for(Student student:teacherList){
            student.setDepartmentName(departmentTransform(student.getDepartmentid()));
        }
        int totalRows=totalStudentByStatus(status);
        int totalPage=(totalRows%pageSize==0)?totalRows/pageSize:totalRows/pageSize+1;
        PageModel<Student> pm=new PageModel<Student>();
        pm.setList(teacherList);
        pm.setCurrentPage(pageNum);
        pm.setTotalPage(totalPage);
        return pm;
    }

    @Override
    public void addStudent(Student student) throws DaoException {
        try {
            studentMapper.addStudent(student);
            //INSERT INTO student(studentId,password,studentname,phone,status,TEACHERID,departmentid) VALUES(#{studentid},#{password},#{studentname},#{phone},#{status},'',#{departmentid})
        } catch (DataAccessException e) {
            throw new DaoException(Dic.SAVE_FAILED);
        }
    }

    @Override
    public void cancleConfirm(String teacherId) throws DaoException {
        try {
            studentMapper.cancleConfirmStu(teacherId);
            //UPDATE student SET status='待审核' WHERE teacherId=#{teacherId} AND status='已选上'
            teacherMapper.cancleConfirm(teacherId);
            //UPDATE teacher SET status='false' WHERE teacherId=#{teacherId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.AMEND_FAILED);
        }
    }

    @Override
    public void deleteTeachers(String studentId) throws DaoException{
        try {
            studentMapper.deleteTeachers(studentId);//UPDATE student SET teacherId='',status='未选课' WHERE studentId=#{studentId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.DELETE_FAILED);
        }
    }

    @Override
    public void deleteStudent(String studentId) {
        studentMapper.deleteOneStudent(studentId);
    }

    public String departmentTransform(String departmentCode){
        if(departmentCode.contains("07001")){
            return "信息与计算科学";
        }else if(departmentCode.contains("07002")){
            return "应用统计学";
        }else if(departmentCode.contains("07003")){
            return "数学与应用数学";
        }
        return "";
    }
}
