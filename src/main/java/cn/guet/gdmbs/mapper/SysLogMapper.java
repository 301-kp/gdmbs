package cn.guet.gdmbs.mapper;

import cn.guet.gdmbs.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SysLogMapper {
    void saveSysLog(SysLog syslog);
}
