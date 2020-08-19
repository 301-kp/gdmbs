package cn.guet.gdmbs.controller;

import cn.guet.gdmbs.entity.Admin;
import cn.guet.gdmbs.entity.Student;
import cn.guet.gdmbs.entity.Teacher;
import cn.guet.gdmbs.exception.DaoException;
import cn.guet.gdmbs.service.IAdminService;
import cn.guet.gdmbs.util.DownloadFile;
import cn.guet.gdmbs.util.POIUtil;
import cn.guet.gdmbs.util.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class adminController {

    @Autowired
    IAdminService adminService;

    /***************************登录**************************************/
    @ResponseBody
    @RequestMapping(value = "/login",produces = {"application/json;charset=UTF-8"})
    public String login(String id, String password, HttpSession session){
        Admin admin=adminService.adminLogin(id,password);
        Admin admin1=(Admin)session.getAttribute("admin");
        if((admin1!=null) && (!id.equals(admin1.getAdminid()))){
            return "err";
        }
        else if(admin!=null){
            session.setAttribute("admin",admin);
            return "admin";
        }
        else {
            return "false";
        }
    }

    /***************************退出登录**************************************/
    @ResponseBody
    @RequestMapping(value = "/logout",produces = {"text/plain;charset=UTF-8"})
    public String logout(HttpSession httpSession){
        httpSession.removeAttribute("admin");
        return "true";
    }

    /***************************添加学生主页内容**************************************/
    @ResponseBody
    @RequestMapping(value = "/addStudentInform",produces = {"text/plain;charset=UTF-8"})
    public String addStudentInform(String content){
        try {
            adminService.addInform(content,"student");
            return "保存成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************添加教师主页内容**************************************/
    @ResponseBody
    @RequestMapping(value = "/addTeacherInform",produces = {"text/plain;charset=UTF-8"})
    public String addTeacherInform(String content){
        try {
            adminService.addInform(content,"teacher");
            return "保存成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************添加教师**************************************/
    @ResponseBody
    @RequestMapping(value = "/addTeacher",produces = {"text/plain;charset=UTF-8"})
    public String changePasswd(Teacher teacher){
        teacher.setPassword(teacher.getTeacherId());
        teacher.setStatus("false");
        try {
            adminService.addTeacher(teacher);
            return "保存成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************添加学生**************************************/
    @ResponseBody
    @RequestMapping(value = "/addStudent",produces = {"text/plain;charset=UTF-8"})
    public String addStudent(Student student){
        student.setPassword(student.getStudentid());
        student.setStatus("未选课");
        try {
            adminService.addStudent(student);
            return "保存成功";
        } catch (DaoException e) {
            return "保存失败，请检查学号是否存在";
        }
    }

    /***************************查询教师列表**************************************/
    @ResponseBody
    @RequestMapping("/listTeacher")
    public PageModel<Teacher> listTeacher(int currentPage){
        PageModel<Teacher> pm=adminService.getAllTeacher(currentPage,20);
        return pm;
    }

    /***************************按状态查询教师列表**************************************/
    @ResponseBody
    @RequestMapping("/listTeaByStatus")
    public PageModel<Teacher> listTeaByStatus(String status,int currentPage){
        PageModel<Teacher> pm=adminService.getAllTeacher(currentPage,20,status);
        return pm;
    }

    /***************************修改教师信息**************************************/
    @ResponseBody
    @RequestMapping(value = "/changeinfo",produces = {"text/plain;charset=UTF-8"})
    public String changeinfo(Teacher teacher,HttpSession httpSession){
        try {
            adminService.updateTeaNameNum(teacher);
            return "修改成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************删除教师**************************************/
    @ResponseBody
    @RequestMapping(value = "/deleteTeacher",produces = {"text/plain;charset=UTF-8"})
    public String deleteTeacher(String teacherId){
        try {
            adminService.deleteTeacher(teacherId);
            return "删除成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************查询学生列表**************************************/
    @ResponseBody
    @RequestMapping("/listStudentStatus")
    public PageModel<Student> listStudentStatus(String departmentId, int currentPage){
        PageModel<Student> pm=adminService.getAllStudent(departmentId,currentPage,20);
        return pm;
    }

    /***************************删除学生**************************************/
    @ResponseBody
    @RequestMapping("/deleteStudent")
    public String deleteStudent(String studentId){
        adminService.deleteStudent(studentId);
        return "删除成功";
    }

    /***************************查询学生列表状态**************************************/
    @ResponseBody
    @RequestMapping("/listStudentByStatus")
    public PageModel<Student> listStudentByStatus(String status, int currentPage){
        PageModel<Student> pm=adminService.getAllStudentByStatus(status,currentPage,20);
        return pm;
    }

    /***************************查询单个学生**************************************/
    @ResponseBody
    @RequestMapping("/selectOneStudent")
    public List<Student> selectOneStudent(String studentName){
        List<Student> list=adminService.getStudentByName(studentName);
        return list;
    }

    /***************************查询教师所带学生**************************************/
    @ResponseBody
    @RequestMapping("/selectByTea")
    public List<Student> selectByTea(String teacherName){
        List<Student> list=adminService.getStudentByTeaName(teacherName);
        return list;
    }

    /********************************修改密码***************************************/
    @ResponseBody
    @RequestMapping( "/changepasswd")
    public String changePassword(String oldPassword,String newPassword,HttpServletRequest request){
        try {
            HttpSession session=request.getSession();
            Admin admin= (Admin) session.getAttribute("admin");
            return adminService.changePasswords(admin.getAdminid(),oldPassword,newPassword);
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************修改学生信息**************************************/
    @ResponseBody
    @RequestMapping(value = "/updateStudent",produces = {"text/plain;charset=UTF-8"})
    public String updateStudent(String new_studentId,String new_studentName,String old_studentId){
        try {
            adminService.updateStu(new_studentId,new_studentName,old_studentId);
            return "修改成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************取消学生选课**************************************/
    @ResponseBody
    @RequestMapping(value = "/cancleAffirmStu",produces = {"text/plain;charset=UTF-8"})
    public String cancleAffirmStu(String studentId){
        try {
            adminService.deleteTeachers(studentId);//UPDATE student SET teacherId='',status='未选课' WHERE studentId=#{studentId}
            return "取消成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************重置密码**************************************/
    @ResponseBody
    @RequestMapping(value = "/checkId",produces = {"text/plain;charset=UTF-8"})
    public String checkId(String role,String id){
        if ("教师".equals(role)) {
            List<Teacher> teacherList=adminService.getTeacher(id);
            if(!teacherList.isEmpty()){
                return "true";
            }else{
                return "该教师编号不存在";
            }
        }
        else if("学生".equals(role)){
            List<Student> studentList=adminService.getStudent(id);
            if(!studentList.isEmpty()){
                return "true";
            }else{
                return "该学号不存在";
            }
        }else{
            return "请选择身份";
        }
    }
    @ResponseBody
    @RequestMapping(value = "/resetpassword",produces = {"text/plain;charset=UTF-8"})
    public String resetpassword(String role,String id){
        try {
            if ("教师".equals(role)) {
                adminService.resetTeaPassword(id);
            }
            else if("学生".equals(role)){
                adminService.resetStuPassword(id);
            }
            return "重置成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /***************************取消最终确认**************************************/
    @ResponseBody
    @RequestMapping(value = "/cancleConfirm",produces = {"text/plain;charset=UTF-8"})
    public String cancleConfirm(String teacherId){
        try {
            adminService.cancleConfirm(teacherId);//UPDATE teacher SET status='false' WHERE teacherId=#{teacherId}
            return "取消成功";
        } catch (DaoException e) {
            return e.getMessage();
        }
    }

    /**
     * 下载模板
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadStudentTemplate")
    public String downloadTemplate(HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = new ArrayList<>();
        List<String> attributes = Arrays.asList("序号", "学号","姓名","联系方式");
        String fileName = "学生信息统计." +  POIUtil.XLS;
        String storePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        // 生成workbook
        DownloadFile df=new DownloadFile();
        df.download(response,fileName,storePath,data,attributes);
        return null;
    }

    /**
     * 下载模板
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadTeacherTemplate")
    public String downloadTeacherTemplate(HttpServletRequest request, HttpServletResponse response){
        List<List<String>> data = new ArrayList<>();
        List<String> attributes = Arrays.asList("序号", "教师编号","姓名","联系电话","所在院系","职称","可指导人数","拟指导内容","备注");
        String storePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        String fileName = "教师信息统计." +  POIUtil.XLS;
        DownloadFile df=new DownloadFile();
        df.download(response,fileName,storePath,data,attributes);
        return null;
    }


    /**
     * ********************************下载文件***********************************88
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String download(HttpServletRequest request, HttpServletResponse response){
// 查询数据
        List<List<String>> data = new ArrayList<>();
        List<String> attributes = Arrays.asList("学号", "姓名","所属专业","指导老师姓名","联系方式", "状态");
        String storePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        String fileName = "学生毕设互选状态表." +  POIUtil.XLS;
        for(int i=1;i<=3;i++) {
            List<Student> userInfoList = adminService.getAllStudent("0700"+i, 1, 150).getList();
            // 组装输入参数
            for (Student excelUserInfo : userInfoList) {
                List<String> rowInfo = new ArrayList<>();
                rowInfo.add(excelUserInfo.getStudentid());
                rowInfo.add(excelUserInfo.getStudentname());
                rowInfo.add(excelUserInfo.getDepartmentName());
                rowInfo.add(excelUserInfo.getTeacher().getTeacherName());
                rowInfo.add(excelUserInfo.getPhone());
                rowInfo.add(excelUserInfo.getStatus());
                data.add(rowInfo);
            }
        }
        DownloadFile df=new DownloadFile();
        df.download(response,fileName,storePath,data,attributes);
        return null;
    }
    @Value("${projectPath:}")
    private String projectPath;

    /**
     * 下载教师的EXCEL文档
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/downloadTeacher")
    public String downloadTeacher(HttpServletRequest request, HttpServletResponse response){// 查询数据
        String storePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
        List<List<String>> data = new ArrayList<>();
        List<String> attributes = Arrays.asList("教师编号", "教师姓名","所在院系","职称","拟指导内容", "已选/容量","状态","电话号码");
        String fileName = "教师确认状态表." +  POIUtil.XLS;
        PageModel<Teacher> pm=adminService.getAllTeacher(1,100);
        List<Teacher> userInfoList=pm.getList();
        for (Teacher excelUserInfo : userInfoList) {
            List<String> rowInfo = new ArrayList<>();
            rowInfo.add(excelUserInfo.getTeacherId());
            rowInfo.add(excelUserInfo.getTeacherName());
            rowInfo.add(excelUserInfo.getDepartmentName());
            rowInfo.add(excelUserInfo.getTitle());
            rowInfo.add(excelUserInfo.getContent());
            rowInfo.add(excelUserInfo.getTotal()+"/"+excelUserInfo.getStudentNum());
            if("true".equals(excelUserInfo.getStatus())) {
                rowInfo.add("已确认");
            }else if("false".equals(excelUserInfo.getStatus())){
                rowInfo.add("未确认");
            }
            rowInfo.add(excelUserInfo.getPhone());
            data.add(rowInfo);
        }
        DownloadFile df=new DownloadFile();
        df.download(response,fileName,storePath,data,attributes);
        return null;
    }

    /**
     * ******************************上传文件********************************************
     * @param excelFile
     * @param request
     */
    @RequestMapping("/getfile")
    public ModelAndView upload(@RequestParam("excelFile") MultipartFile excelFile, HttpServletRequest request){

        List<String> list=new ArrayList<String>();
        String name = excelFile.getOriginalFilename();
        // 1: 转存文件
        if (!excelFile.isEmpty()) {
            /**
             * 这里的getRealPath("/")是打包之后的项目的根目录。
             * 也就是 target\项目名-1.0-SNAPSHOT\
             */
            String storePath= StringUtils.isNotBlank(projectPath) ? projectPath + "/data/tmp/" : "/data/tmp/";
            try {
                excelFile.transferTo(new File(storePath + name));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 2: 解析excel数据
        List<String[]> excelData = null;
        try {
            excelData = POIUtil.readExcelFile(excelFile,1);
            if(name.contains("教师")){
                for (String[] arr : excelData) {
                    Teacher teacher=new Teacher();
                    if(arr[4].contains("数学与应用数学")){
                        teacher.setDepartmentid("07003");//所在院系
                    }else if(arr[4].contains("信息与计算科学")){
                        teacher.setDepartmentid("07001");
                    }else if(arr[4].contains("应用统计")){
                        teacher.setDepartmentid("07002");
                    }
                    teacher.setTeacherId(arr[1]);//教师编号
                    teacher.setTeacherName(arr[2]);//教师姓名
                    teacher.setPassword(arr[1]);
                    teacher.setPhone(arr[3]);
                    teacher.setStudentNum(Integer.parseInt(arr[6].equals("")?"0":arr[6]));//可指导人数
                    teacher.setTitle(arr[5]);//职称
                    teacher.setContent(arr[7]);//拟指导内容
                    teacher.setStatus("false");
                    try {
                        adminService.addTeacher(teacher);
                    } catch (DaoException e) {
                        list.add("异常："+arr[2] + "导入失败,请检查信息是否正确，或是信息已存在");
                        continue;
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }else if(name.contains("学生")){
                for (String[] arr : excelData) {
                    if ("".equals(arr[1])) {
                        continue;
                    } else {
                        Student student = new Student();
                        if(arr[1].substring(5, 6).equals("1")){
                            student.setDepartmentid("07001");//所在院系
                        }else if(arr[1].substring(5, 6).equals("2")){
                            student.setDepartmentid("07002");
                        }else if(arr[1].substring(5, 6).equals("3")){
                            student.setDepartmentid("07003");
                        }
                        student.setStudentid(arr[1]);
                        student.setStudentname(arr[2]);
                        student.setPhone(arr[3]);
                        student.setPassword(arr[1]);
                        student.setStatus("未选课");
                        try {
                            adminService.addStudent(student);
                        } catch (DaoException e) {
                            list.add(arr[2] + e.getMessage()+"，请检查信息是否正确，或是信息已存在");
                            continue;
                        }
                    }
                }
            }else {
                list.add("请输入正确的文件名");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.isEmpty()){
            list.add("数据全部保存成功！");
        }
        ModelAndView mav = new ModelAndView("item");
        mav.addObject("list", list);
        return mav;
    }


    /***************************取消最终确认**************************************/
    @ResponseBody
    @RequestMapping(value = "/deleteData",produces = {"text/plain;charset=UTF-8"})
    public String deleteData(String teacherId){
        try{
            adminService.deleteData();
        }catch (DaoException e){
            return e.getMessage();
        }
        return "删除成功";
    }

    /***************************显示主页信息**************************************/
    @ResponseBody
    @RequestMapping(value = "/showInfo")
    public Map<String,String> showInfo(){
        Map<String,String> info=adminService.showInfo();
        return info;
    }
}



