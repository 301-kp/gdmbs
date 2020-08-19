package cn.guet.gdmbs.service.impl;

import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.mapper.AdminMapper;
import cn.guet.gdmbs.mapper.StudentMapper;
import cn.guet.gdmbs.mapper.TeacherMapper;
import cn.guet.gdmbs.service.IStudentService;
import cn.guet.gdmbs.util.Dic;
import cn.guet.gdmbs.util.PageModel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("studentService")
public class StudentServiceImpl implements IStudentService {

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public Student verifyLogin(String studentId, String password) throws DaoException {
        try{
            return studentMapper.verifyLogin(studentId,password);
            //SELECT * FROM student WHERE studentId=#{studentId} AND password=#{password}
        }catch (DataAccessException e){
            throw new DaoException(Dic.LOGIN_FAILED);
        }
    }

    @Override
    public PageModel<Teacher> getAllTeacher(String studentId, int pageNum, int pageSize) throws DaoException {
        try {
            List<Teacher> teacherList=listTeacher(studentId,pageNum,pageSize);
            List<Teacher> list= new ArrayList<Teacher>();
            Integer total=0;
            String departmentName="";
            int confirmNum;
            for(Teacher teacher:teacherList){
                confirmNum=getNum(teacher.getTeacherId(),"已选上");
                teacher.setConfirmNum(confirmNum);
                total=totalSelect(teacher.getTeacherId());
                teacher.setTotal(total);
                departmentName=departmentTransform(teacher.getDepartmentid());
                teacher.setDepartmentName(departmentName);
                list.add(teacher);
            }
            int totalRows=totalTeacher();
            int totalPage=0;
            totalPage=(totalRows%pageSize==0)?totalRows/pageSize:totalRows/pageSize+1;
            PageModel<Teacher> pm=new PageModel<Teacher>();
            pm.setList(list);
            pm.setCurrentPage(pageNum);
            pm.setTotalPage(totalPage);
            return pm;
        } catch (DataAccessException e) {
            throw new DaoException(Dic.QUERY_FAILED);
        }
    }

    @Override
    public Integer totalSelect(String teacherId) throws DataAccessException {
        Integer a=studentMapper.totalSelect(teacherId);
        //SELECT COUNT(*) FROM student WHERE teacherId=#{teacherId} AND (status='待审核' OR status='已选上') GROUP BY teacherId
        return (a==null)?0:a;
    }

    @Override
    public List<Teacher> getTeacher(String teacherName) throws DaoException {
        try {
            List<Teacher> teacherList=selectTeacher(teacherName);
            List<Teacher> list= new ArrayList<Teacher>();
            Integer total=0;
            String departmentName;
            int confirmNum;
            for(Teacher teacher:teacherList){
                confirmNum=getNum(teacher.getTeacherId(),"已选上");
                teacher.setConfirmNum(confirmNum);
                total=totalSelect(teacher.getTeacherId());
                teacher.setTotal(total);
                departmentName=departmentTransform(teacher.getDepartmentid());
                teacher.setDepartmentName(departmentName);
                list.add(teacher);
            }
            return list;
        } catch (DataAccessException e) {
            throw new DaoException(Dic.QUERY_FAILED);
        }
    }

    @Override
    public void selectTeachers(String teacherId,String studentId) throws DaoException{
        try {
            studentMapper.selectTeachers(teacherId,studentId);
            //select surplusnum from teacher where teacherid=#{teacherId}
        }catch (DataAccessException e){
            throw new DaoException(Dic.QUERY_FAILED);
        }
    }

    @Override
    public List<Teacher> getSelectTea(String studentId) throws DaoException {
        try {
            List<Teacher> teacherList= adminMapper.getSelectTea(studentId);
            // select teacherId from student where studentid=#{studentId}
            List<Teacher> list= new ArrayList<Teacher>();
            String departmentName;
            for(Teacher teacher:teacherList){
                departmentName=departmentTransform(teacher.getDepartmentid());
                teacher.setDepartmentName(departmentName);
                list.add(teacher);
            }
            return list;
        } catch (DataAccessException e) {
            throw new DaoException(Dic.QUERY_FAILED);
        }
    }

    @Override
    public void deleteTeachers(String studentId) throws DaoException{
        try {
            studentMapper.deleteTeachers(studentId);
            //UPDATE student SET teacherId='',status='未选课' WHERE studentId=#{studentId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.DELETE_FAILED);
        }
    }

    @Override
    public String changePasswords(String studentId, String oldPassword, String newPassword) throws DaoException {
        try {
            if(getPasswordByStu(studentId).equals(oldPassword)) {
                studentMapper.changePasswords(newPassword, studentId);
                //UPDATE student SET password=#{password} WHERE studentId=#{studentId}
                return "true";
            }else{
                return "false";
            }
        }catch (DataAccessException e) {
            throw new DaoException(Dic.LOGIN_FAILED);
        }
    }

    @Override
    public String getPasswordByStu(String studentId) throws DaoException {
        try {
            return studentMapper.getPasswordByStu(studentId);
            //SELECT password FROM student WHERE studentId=#{studentId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.LOGIN_FAILED);
        }
    }

    @Override
    public Integer getNum(String teacherId,String status) {
        Integer a=adminMapper.getNum(teacherId,status);
        //SELECT COUNT(*) FROM student s,teacher t
        //WHERE t.teacherId=s.teacherId and t.teacherId=#{teacherId} and s.status=#{status}
        return (a==null)?0:a;
    }

    @Override
    public String getStuStatus(String studentId) throws DaoException {
        return studentMapper.getStuStatus(studentId);
        //SELECT status FROM student WHERE studentId=#{studentId}
    }

    @Override
    public String getStuNote(String studentId) throws DaoException {
        return studentMapper.getStuNote(studentId);
        //SELECT note FROM student WHERE studentId=#{studentId}
    }

    @Override
    public Integer totalTeacher() {
        Integer a=teacherMapper.totalTeacher();
        //SELECT COUNT(*) FROM teacher
        return (a==null)?0:a;
    }

    @Override
    public String getTeacherId(String studentId) {
        return studentMapper.getTeacherId(studentId);
        //select teacherId from student where (status='待审核' or status='已选上') and studentid=#{studentId}
    }

    @Override
    public void changeInfo(String phone, String studentId) throws DaoException {
        try {
            studentMapper.changeInfo(phone,studentId);
            //UPDATE student SET phone=#{phone} WHERE studentId=#{studentId}
        } catch (DataAccessException e) {
            throw new DaoException(Dic.AMEND_FAILED);
        }
    }

    @Override
    public String showInfo(String inform_obj) {
        return adminMapper.showInfo(inform_obj);
        //SELECT inform_content FROM inform WHERE inform_obj=#{inform_obj}
    }

    @Override
    public String getSelectTeacher(String studentId) {
        return studentMapper.getSelectTeacher(studentId);
        //select teacherId from student where studentid=#{studentId}
    }

    @Override
    public List<Student> listStudentInfo(String studentId) {
        List<Student> listStudent=studentMapper.listStudentInfo(studentId);
        //select * from student where studentId=#{studentId}
        List<Student> list=new ArrayList<Student>();
        String departmentName="";
        for(Student student:listStudent){
            departmentName=departmentTransform(student.getDepartmentid());
            student.setDepartmentName(departmentName);
            list.add(student);
        }
        return list;
    }

    @Override
    public Integer totalTeacherByStatus(String status) {
        Integer a=teacherMapper.totalTeacherByStatus(status);
        //SELECT COUNT(*) FROM teacher WHERE status=#{status}
        return (a==null)?0:a;
    }

    @Override
    public Integer getStudentNum(String teacherId) {
        Integer a=teacherMapper.getStudentNum(teacherId);
        //select studentNum from teacher where teacherId=#{teacherId}
        return (a==null)?0:a;
    }

    public String getInfo(String teacherId) {
        return teacherMapper.getOneTeacher(teacherId).getInfo();
        //SELECT info FROM teacher WHERE teacherid=#{teacherId}
    }

    public List<Teacher> listTeacher(String studentId,int pageNum, int pageSize) throws DataAccessException {
        String teacherId=getTeacherId(studentId);
        PageHelper.startPage(pageNum, pageSize);
        Page<Teacher> pageList=(Page<Teacher>)teacherMapper.listTeacher(teacherId);
        return pageList;
    }

    public List<String> getTeaIdByStuFromBlack(String studentId) {//查询某个学生的黑名单
        return adminMapper.getTeaIdByStuFromBlack(studentId);
        //SELECT teacherId FROM blacklist WHERE studentId=#{studentId}
    }

    public List<Teacher> selectTeacher(String teacherName) throws DataAccessException {
        return teacherMapper.selectTeacher(teacherName);
        //UPDATE student SET teacherId=#{teacherId},status='待审核' WHERE studentId=#{studentId}
    }

    public String departmentTransform(String departmentCode){
        if(departmentCode.contains("07001")){
            return "信息与计算科学";
        }else if(departmentCode.contains("07002")){
            return "应用统计学";
        }else if(departmentCode.contains("07003")){
            return "数学与应用数学";
        }else{
            return "";
        }
    }
}
