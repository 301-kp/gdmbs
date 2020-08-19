package cn.guet.gdmbs.controller;

import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.service.IStudentService;
import cn.guet.gdmbs.util.PageModel;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    IStudentService studentService;

    @Autowired
    private Redisson redisson;

    /***************************登录**************************************/
    @RequestMapping("/login")
    public String login(String id, String password, HttpServletRequest request){
        Student s= null;
        HttpSession session=request.getSession();
        String studentid=(String)session.getAttribute("studentId");
        try {
            if((studentid!=null) && (!id.equals(studentid))){
                return "err";
            }
            else{
                s = studentService.verifyLogin(id,password);
                if(s!=null){
                    session.setAttribute("studentId",id);
                    return "student";
                }
            }
        } catch (DaoException e) {
            return e.getMessage();
        }
        return "";
    }

    /********************************显示主页内容****************************************/
    @RequestMapping("/showInform")
    public String showInform(){
        return studentService.showInfo("student");
    }

    /********************************显示名字****************************************/
    @RequestMapping("/showName")
    public String showName(HttpServletRequest request){
        HttpSession session=request.getSession();
        Student student=studentService.listStudentInfo((String)session.getAttribute("studentId")).get(0);
        return student.getStudentname();
    }

    /********************************查询单个教师****************************************/
    @RequestMapping("/viewStudentInfo")
    public Map<String,String> viewStudentInfo(HttpServletRequest request){
        HttpSession session=request.getSession();
        String studentid=(String)session.getAttribute("studentId");
        List<Student> list=studentService.listStudentInfo(studentid);
        Map<String,String> map=new HashMap<String,String>();
        for(Student s:list){
            map.put("studentId",s.getStudentid());
            map.put("studentName",s.getStudentname());
            map.put("departmentName",s.getDepartmentName());
            map.put("phone",s.getPhone());
            map.put("status",s.getStatus());
        }
        return map;
    }

    /***************************查看教师列表**************************************/
    @RequestMapping("/listTeacher")
    public PageModel<Teacher> listTeacher(int currentPage, HttpServletRequest request){
        PageModel<Teacher> pm=new PageModel<Teacher>();
        try {
            HttpSession session=request.getSession();
            Object studentId=session.getAttribute("studentId");
            pm=studentService.getAllTeacher(studentId.toString(),currentPage,10);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return pm;
    }

    /********************************查询单个教师****************************************/
    @RequestMapping("/getTeacherByName")
    public List<Teacher> getTeacherByName(String selectName){
        List<Teacher> list=new ArrayList<Teacher>();
        try {
            list =studentService.getTeacher(selectName);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return list;
    }

    /********************************查询教师简介****************************************/
    @RequestMapping("/viewInfo")
    public String viewInfo(String teacherId){
        return studentService.getInfo(teacherId);
    }

    /********************************选择指导老师***************************************/
    @RequestMapping(value = "/select")
    public String select(String teacherId,HttpServletRequest request){
        String lockKey="lockKey";
        RLock redissonLock=null;
        try {
            redissonLock = redisson.getLock("lockKey");//加锁
            redissonLock.tryLock(30, TimeUnit.SECONDS);//超时时间：每间隔10秒（1/3）
            HttpSession session=request.getSession();
            Object studentId=session.getAttribute("studentId");
            String status = studentService.getStuStatus(studentId.toString());
            int total=studentService.totalSelect(teacherId);//已选
            int studentNum=studentService.getStudentNum(teacherId);//容量
            if(status.equals("已选上")||status.equals("待审核")){
                return "false";
            }else if(status.equals("未选上")){
                return "操作失败，请联系管理员";
            }
            else{
                if(total<studentNum+2){
                    Teacher teacher=new Teacher();
                    teacher.setTeacherId(teacherId);
                    studentService.selectTeachers(teacherId, studentId.toString());
                    return "true";
                }
                else{
                    return "教师选择人数已达上限";
                }
            }
        } catch (DaoException e) {
            return e.getMessage();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonLock.unlock();//释放锁
        }
        return "";
    }



    /********************************查看已选指导老师***************************************/
    @RequestMapping("/viewChoose")
    public Map<String,Object> viewChoose(HttpServletRequest request){
        List<Teacher> teacherList=null;
        String teacherId="";
        int totalSelect=0;
        String status="";
        String note="";
        try {
            HttpSession session=request.getSession();
            Object studentId=session.getAttribute("studentId");
            teacherList=studentService.getSelectTea(studentId.toString());
            status=studentService.getStuStatus(studentId.toString());
            note=studentService.getStuNote(studentId.toString());
            for(Teacher teacher:teacherList){
                teacherId=teacher.getTeacherId();
                totalSelect=studentService.totalSelect(teacherId);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("teacherList",teacherList);
        map.put("totalSelect",totalSelect);
        map.put("status",status);
        map.put("note",note);
        return map;
    }

    /********************************取消选择当前指导老师***************************************/
    @RequestMapping(value = "/delete", produces = {"text/plain;charset=UTF-8"})
    public String select(HttpServletRequest request){
        try {
            HttpSession session=request.getSession();
            Object studentId=session.getAttribute("studentId");
            String teacherId=studentService.getSelectTeacher(studentId.toString());
            Teacher teacher=new Teacher();
            teacher.setTeacherId(teacherId);
            studentService.deleteTeachers(studentId.toString());
        } catch (DaoException e) {
            return e.getMessage();
        }
        return "";
    }

    /********************************修改手机号码***************************************/
    @RequestMapping(value = "/changeInfo", produces = {"text/plain;charset=UTF-8"})
    public String changeInfo(String phone,HttpServletRequest request){
        try {
            HttpSession session=request.getSession();
            Object studentId=session.getAttribute("studentId");
            studentService.changeInfo(phone,studentId.toString());
            return "修改成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /********************************修改密码***************************************/
    @RequestMapping( "/changepasswd")
    public String changePassword(String oldPassword,String newPassword,HttpServletRequest request){
        try {
            HttpSession session=request.getSession();
            Object studentId=session.getAttribute("studentId");
            return studentService.changePasswords(studentId.toString(),oldPassword,newPassword);
        } catch (DaoException e) {
            return e.getMessage();
        }
    }


    /********************************退出登录***************************************/
    @RequestMapping( "/stuLogout")
    public void stuLogout(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.invalidate();
    }
}
