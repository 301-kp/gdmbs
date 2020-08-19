package cn.guet.gdmbs.controller;

import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    ITeacherService teacherService;

    /***************************登录**************************************/
    @RequestMapping(value = "/login",produces = {"text/plain;charset=UTF-8"})
    public String login(String id, String password, HttpSession session){
        Teacher teachers=teacherService.teacherLogin(id,password);
        Teacher teacher=(Teacher)session.getAttribute("teacher");
        if((teacher!=null)&&(!id.equals(teacher.getTeacherId()))){
            return "err";
        }
        else if(teachers!=null){
            session.setAttribute("teacher",teachers);
            return "teacher";
        }
        else {
            return "false";
        }
    }

    /********************************显示主页内容****************************************/
    @RequestMapping("/showInform")
    public String showInform(){
        return teacherService.showInfo("teacher");
    }

    /********************************显示名字****************************************/
    @RequestMapping("/showName")
    public String showName(HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        return teacher.getTeacherName();
    }

    /***************************查询教师简介**************************************/
    @RequestMapping(value = "/getInfo")
    public Teacher getInfo(HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        return teacherService.getInfo(teacher.getTeacherId());
    }

    /***************************添加/修改教师简介**************************************/
    @RequestMapping(value = "/updateInfo",produces = {"text/plain;charset=UTF-8"})
    public String updateInfo(String teaInfo,HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        try {
            teacherService.updateInfo(teacher.getTeacherId(),teaInfo);
            return "保存成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************修改电话号码**************************************/
    @RequestMapping(value = "/updatePhoneNumber",produces = {"text/plain;charset=UTF-8"})
    public String updatePhoneNumber(String phoneNumber,HttpSession httpSession){
        try {
            Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
            teacherService.updatephoneNumber(phoneNumber,teacher.getTeacherId());
            return "修改成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************查询学生人数**************************************/
    @RequestMapping(value = "/selectStudentNum",produces = {"text/plain;charset=UTF-8"})
    public String addTeacher(HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        return teacher.getStudentNum()+"";
    }

    /***************************教师选择学生**************************************/
    @RequestMapping(value = "/selectStudent",produces = {"text/plain;charset=UTF-8"})
    public String selectStudent(String studentId,HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        try {
            int Num=teacherService.getNum(teacher.getTeacherId(),"已选上");
            if(teacher.getStudentNum()>Num+1) {   //需要修改为已确定人数
                teacherService.selectStudent(teacher.getTeacherId(), studentId);
                return "选择成功";
            }else if(teacher.getStudentNum()==Num+1){  //需要修改为已确定人数
                teacherService.selectStudent(teacher.getTeacherId(), studentId);
                teacherService.lock(teacher.getTeacherId());
                return "最终确认";
            }else{
                return "指导人数已满";
            }
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************删除学生**************************************/
    @RequestMapping(value = "/delete",produces = {"text/plain;charset=UTF-8"})
    public String deleteStudent(String studentId,HttpSession httpSession){
        try {
            Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
            teacherService.deleteStudent(studentId);//UPDATE student SET status='未选上',note='true' WHERE studentid=#{studentId} AND (status='待审核' OR status='已选上')
            teacherService.addBlackList(studentId,teacher.getTeacherId());
            return "删除成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************教师最终确定**************************************/
    @RequestMapping(value = "/lock",produces = {"text/plain;charset=UTF-8"})
    public String lock(HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        try {
            teacherService.lock(teacher.getTeacherId());
            return "确认成功";
        } catch (DaoException e) {
            return "您已确认";
        }
    }

    /***************************修改密码**************************************/
    @RequestMapping(value = "/changepasswd",produces = {"text/plain;charset=UTF-8"})
    public String changePasswd(String newPassword, String oldPassword, HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        String result= null;
        try {
            result = teacherService.changePasswd(teacher.getTeacherId(),newPassword,oldPassword);
        } catch (DaoException e) {
            return e.getMessage();
        }
        return result;
    }

    /***************************查询选择当前教师的学生列表**************************************/
    @RequestMapping("/listStudent")
    public Map<String,Object> listStudent( HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        int Num=0;
        int Num2=0;
        int Num1=0;
        List<Student> list= null;
        try {
            list = teacherService.listStudent(teacher.getTeacherId());
            Num=teacher.getStudentNum();                        //带领学生总数
            Num1=teacherService.getNum(teacher.getTeacherId(),"待审核"); //待审核学生人数
            Num2=teacherService.getNum(teacher.getTeacherId(),"已选上");//已选上学生人数
        } catch (DaoException e) {
            e.printStackTrace();
        }
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("studentList",list);
        map.put("num",Num);
        map.put("num1",Num1);
        map.put("num2",Num2);
        if(teacherService.getStatus(teacher.getTeacherId()).equals("true")){
            map.put("teachersta","true");
        }else{
            map.put("teachersta","false");
        }
        return map;
    }

    /***************************退出登录**************************************/
    @RequestMapping(value = "/logout",produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("teacher");
        return "true";
    }

    /********************************随机分配指导老师***************************************/
    @RequestMapping(value = "/randomDispatcher")
    public void randomDispatcher(String studentId){
        String randomTeacherId;
        try {
            List<String> list =teacherService.getIdToR();//查询是否有选课人数为0 的老师
            if (!list.isEmpty()) {
                randomTeacherId =teacherService.yx_getRandomId(studentId,list);//选课人数为0 优先分配
                teacherService.selectTeachers(randomTeacherId, studentId);
            }
            else{
                randomTeacherId = teacherService.qz_getRandomId(studentId);//按权重分配
                teacherService.selectTeachers(randomTeacherId, studentId);
            }
        }catch (DaoException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/randomDispatcherLock")
    public void randomDispatcherLock(HttpSession httpSession){
        Teacher teacher= (Teacher) httpSession.getAttribute("teacher");
        List<String> list=teacherService.blackListStudentId(teacher.getTeacherId());//SELECT studentid FROM student WHERE teacherid=#{teacherId} AND status='待审核'
        for(String studentId:list){
            String randomTeacherId;
            try {
                teacherService.deleteStudent(studentId);
                List<String> list1 = teacherService.getIdToR();//查询是否有选课人数为0 的老师
                if (!list1.isEmpty()) {
                    randomTeacherId =teacherService.yx_getRandomId(studentId,list1);//选课人数为0 优先分配
                    teacherService.selectTeachers(randomTeacherId, studentId);
                }
                else{
                    randomTeacherId = teacherService.qz_getRandomId(studentId);//按权重分配
                    teacherService.selectTeachers(randomTeacherId, studentId);
                }
            }catch (DaoException e) {
                e.printStackTrace();
            }
        }
    }

}
