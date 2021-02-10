package graduatedesign.ghost;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import graduatedesign.ghost.controller.DashboardController;
import graduatedesign.ghost.dto.ReportDTO;
import graduatedesign.ghost.mapper.UserMapper;
import graduatedesign.ghost.myEnum.LogStatus;
import graduatedesign.ghost.service.UserRoleService;
import graduatedesign.ghost.utils.EncoderUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

@SpringBootTest
class GhostApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserMapper userMapper;

    @Autowired
    EncoderUtils encoderUtils;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    DashboardController dashboardController;
    @Test
    void contextLoads() throws Exception {
        //List<ReportDTO> report = dashboardController.getReport();
        //System.out.println(report.get(1));

        System.out.println(IdUtil.getSnowflake(1, 1).nextId());
        //1288834974657
        //1357982645979582464
        //1357982724824109056
    }

}
