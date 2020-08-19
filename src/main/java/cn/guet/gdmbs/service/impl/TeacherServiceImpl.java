package cn.guet.gdmbs.service.impl;

import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.mapper.AdminMapper;
import cn.guet.gdmbs.mapper.StudentMapper;
import cn.guet.gdmbs.mapper.TeacherMapper;
import cn.guet.gdmbs.service.ITeacherService;
import cn.guet.gdmbs.util.Dic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service("teacherService")
public class TeacherServiceImpl implements ITeacherService {
    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    AdminMapper adminMapper;

    @Override
    public Teacher teacherLogin(String teacherId, String password) {
        return teacherMapper.teacherLogin(teacherId,password);
        //SELECT * FROM teacher WHERE teacherid=#{teacherId} AND password=#{password}
    }

    @Override
    public String changePasswd(String teacherid, String newpasswd,String oldpasswd) throws DaoException {
        if(teacherLogin(teacherid,oldpasswd)!=null) {
            try{
                teacherMapper.changePasswd(teacherid, newpasswd);
                //UPDATE teacher SET password=#{newpassword} WHERE teacherid=#{teacherId}
            }
            catch (DataAccessException e){
                throw new DaoException(Dic.AMEND_FAILED);
            }
            return "true";
        }
        else{
            return "false";
        }
    }

    @Override
    public void lock(String teacherid) throws DaoException{
        try {
            teacherMapper.lock(teacherid);
            //UPDATE teacher SET status='true' WHERE teacherid=#{teacherId}
        }catch (DataAccessException e){
            throw new DaoException(Dic.SELECT_FAILED);
        }
    }

    @Override
    public void deleteStudent(String studentId)throws DaoException {
        try {
            studentMapper.deleteStudent(studentId);
            //UPDATE student SET status='未选上',note='true' WHERE studentid=#{studentId} AND (status='待审核' OR status='已选上')
        }catch (DataAccessException e){
            throw new DaoException(Dic.DELETE_FAILED);
        }
    }

    @Override
    public List<Student> listStudent(String teacherId) throws DaoException{
        try{
            List<Student> listStudent=studentMapper.listStudent(teacherId);
            //SELECT * FROM student WHERE teacherid=#{teacherId} AND (status='待审核' OR status='已选上')
            List<Student> list= new ArrayList<Student>();
            String departmentName;
            for(Student student:listStudent){
                departmentName=departmentTransform(student.getDepartmentid());
                student.setDepartmentName(departmentName);
                list.add(student);
            }
            return list;
        }catch (DataAccessException e){
            throw new DaoException(Dic.QUERY_FAILED);
        }
    }

    @Override
    public String getStatus(String teacherid) {
        return teacherMapper.getStatus(teacherid);
        //SELECT status FROM teacher WHERE teacherid=#{teacherId}
    }

    @Override
    public Integer getNum(String teacherId,String status) {
        Integer a=adminMapper.getNum(teacherId,status);
        //SELECT studentNum FROM teacher WHERE teacherid=#{teacherId}
        return (a==null)?0:a;
    }

    @Override
    public Teacher getInfo(String teacherId) {
        return teacherMapper.getOneTeacher(teacherId);
        //SELECT info FROM teacher WHERE teacherid=#{teacherId}
    }

    @Override
    public void updateInfo(String teacherId, String info) throws DaoException {
        try{
            teacherMapper.updateInfo(info,teacherId);
            //UPDATE teacher SET info=#{info} WHERE teacherid=#{teacherId}
        }catch (DataAccessException e){
            throw new DaoException(Dic.AMEND_FAILED);
        }
    }

    @Override
    public void addBlackList(String studentId, String teacherId) {
        try{
            adminMapper.addBlackList(studentId,teacherId);
            //INSERT INTO blacklist(studentId,teacherId) VALUES(#{studentId},#{teacherId})
        }catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    @Override
    public void selectStudent(String teacherId, String studentId) throws DaoException {
        try{
            studentMapper.selectStudent(teacherId,studentId);
            //UPDATE student SET teacherId=#{teacherId},status='已选上' WHERE studentId=#{studentId}
        }catch (DataAccessException e){
            throw new DaoException(Dic.SELECT_FAILED);
        }
    }

    @Override
    public List<String> blackListStudentId(String teacherId) {
        return studentMapper.blackListStudentId(teacherId);
        //SELECT studentid FROM student WHERE teacherid=#{teacherId} AND status='待审核'
    }

    @Override
    public String showInfo(String inform_obj) {
        return adminMapper.showInfo(inform_obj);
        //SELECT inform_content FROM inform WHERE inform_obj=#{inform_obj}
    }

    @Override
    public List<String> getIdToR(){//查询选课人数为0 的老师
        List<Teacher> list= new ArrayList<Teacher>();
        List<String> list2=new ArrayList<String>();
        List<Teacher> teacherList=teacherMapper.listTeacherAll();
        //SELECT teacherId,teacherName,studentNum,departmentId,title,content,status,phone FROM teacher
        int total=0;
        for(Teacher teacher:teacherList){
            total=totalSelect(teacher.getTeacherId());
            teacher.setTotal(total);
            list.add(teacher);
        }
        for(Teacher t:list){
            if(t.getTotal()==0){
                list2.add(t.getTeacherId());
            }
        }
        return list2;
    }

    @Override
    public String yx_getRandomId(String studentId,List<String> list) {//选课人数为0 优先分配
        List<String> list2=getTeaIdByStuFromBlack(studentId);         //获取黑名单,list2一定不是空的
        String teacherId="";
        Random random=new Random();
        list.removeAll(list2);
        if(list.isEmpty()){
            List<String> list1=teacherMapper.listTeacherNotLock();
            //select teacherId from teacher where STATUS='false'
            list1.removeAll(list2);
            int x=random.nextInt(list1.size());
            teacherId=list1.get(x);
        }else{
            teacherId=list.get((int) (Math.random()* list.size()));
        }
        return teacherId;
    }

    @Override
    public String qz_getRandomId(String studentId) {    //按权重分配
        List<String> list=teacherMapper.listTeacherNotLock();
        //select teacherId from teacher where STATUS='false'
        List<String> list2=getTeaIdByStuFromBlack(studentId);
        list.removeAll(list2);
        String teacherId=list.get((int) (Math.random()* list.size()));
        return teacherId;
    }

    @Override
    public void selectTeachers(String teacherId,String studentId) throws DaoException{
        try {
            studentMapper.selectTeachers(teacherId,studentId);
            //UPDATE student SET teacherId=#{teacherId},status='待审核' WHERE studentId=#{studentId}
        }catch (DataAccessException e){
            throw new DaoException(Dic.QUERY_FAILED);
        }
    }

    @Override
    public void updatephoneNumber(String phoneNumber,String teacherId) throws DaoException {
        try{
            teacherMapper.updatephoneNumber(phoneNumber,teacherId);
        }catch (DataAccessException e){
            throw new DaoException(Dic.AMEND_FAILED);
        }

    }

    public List<String> getTeaIdByStuFromBlack(String studentId) {//查询某个学生的黑名单
        return adminMapper.getTeaIdByStuFromBlack(studentId);
        // SELECT teacherId FROM blacklist WHERE studentId=#{studentId}
    }

    public Integer totalSelect(String teacherId) throws DataAccessException {
        Integer a=studentMapper.totalSelect(teacherId);
        //SELECT COUNT(*) FROM student WHERE teacherId=#{teacherId} AND (status='待审核' OR status='已选上') GROUP BY teacherId
        return (a==null)?0:a;
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
